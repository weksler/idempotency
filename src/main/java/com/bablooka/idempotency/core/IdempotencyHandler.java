package com.bablooka.idempotency.core;

import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.EXECUTING;
import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.RESPONDED;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;

@Log4j2
public class IdempotencyHandler<T extends Object> {

  private final IdempotentRpcContextFactory<T> idempotentRpcContextFactory;
  private final Util util;
  private final Duration leaseDuration;
  private final JsonFormat.Printer jsonFormatPrinter;

  @Inject
  public IdempotencyHandler(
      @NonNull IdempotentRpcContextFactory<T> idempotentRpcContextFactory,
      @NonNull Util util,
      @NonNull Duration leaseDuration,
      @NonNull JsonFormat.Printer jsonFormatPrinter) {
    this.idempotentRpcContextFactory = idempotentRpcContextFactory;
    this.util = util;
    this.leaseDuration = leaseDuration;
    this.jsonFormatPrinter = jsonFormatPrinter;
  }

  public byte[] handleRpc(
      DSLContext dslContext,
      IdempotencyStore idempotencyStore,
      IdempotentRpc<T> idempotentRpc,
      T processData)
      throws IdempotencyException {
    dslContext.connection(
        c -> {
          try {
            checkState(
                c.getAutoCommit() == false, "Connection %s must have auto commit off.", dslContext);
          } catch (SQLException e) {
            Util.logAndThrow(log, e, "Exception while checking auto commit state");
          }
        });

    // Prepare for the outbound RPC
    IdempotentRpcContext idempotentRpcContext =
        idempotentRpcContextFactory.getIdempotencyRpcContext(processData);
    checkNotNull(idempotentRpcContext, "IdempotencyRpcContext can not be null.");
    String idempotencyKey = idempotentRpcContext.getIdempotencyKey();
    checkNotNull(idempotencyKey, "Idempotency key can not be null.");
    Instant leaseExpiresAt = util.now().plus(leaseDuration);
    Timestamp timestamp =
        Timestamp.newBuilder()
            .setSeconds(leaseExpiresAt.getEpochSecond())
            .setNanos(leaseExpiresAt.getNano())
            .build();
    IdempotencyRecord idempotencyRecord =
        IdempotencyRecord.newBuilder()
            .setIdempotencyKey(idempotencyKey)
            .setRequestFingerprint("")
            .setStatus(EXECUTING)
            .setLeaseExpiresAt(timestamp)
            .build();
    idempotentRpcContext = idempotentRpc.prepare(idempotentRpcContext);
    log.debug("Prepare {}: {}", idempotencyKey, util.protoToJsonString(idempotencyRecord));
    idempotencyStore.upsertIdempotencyRecord(
        idempotentRpcContext.getIdempotencyKey(), util.protoToDbFormat(idempotencyRecord));

    dslContext.connection(
        c -> {
          try {
            c.commit();
          } catch (SQLException e) {
            Util.logAndThrow(log, e, "Exception while committing");
          }
        });

    // Execute the outbound RPC
    idempotentRpcContext = idempotentRpc.execute(idempotentRpcContext);

    // Process the response from the outbound RPC.
    idempotentRpcContext = idempotentRpc.processResults(idempotentRpcContext);
    idempotencyRecord = idempotencyRecord.toBuilder().setStatus(RESPONDED).build();
    log.debug("Responded {}: {}", idempotencyKey, util.protoToJsonString(idempotencyRecord));
    idempotencyStore.upsertIdempotencyRecord(
        idempotentRpcContext.getIdempotencyKey(), util.protoToDbFormat(idempotencyRecord));

    return idempotentRpcContext.getResponse();
  }
}
