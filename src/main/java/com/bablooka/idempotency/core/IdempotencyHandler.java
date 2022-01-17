package com.bablooka.idempotency.core;

import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.EXECUTING;
import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.RESPONDED;
import static com.google.common.base.Preconditions.checkState;
import static dagger.internal.Preconditions.checkNotNull;

import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class IdempotencyHandler<T extends Object> {

  private final IdempotentRpcContextFactory<T> idempotentRpcContextFactory;
  private final Util util;
  private final Duration leaseDuration;

  @Inject
  public IdempotencyHandler(
      @NonNull IdempotentRpcContextFactory<T> idempotentRpcContextFactory,
      @NonNull Util util,
      @NonNull Duration leaseDuration) {
    this.idempotentRpcContextFactory = idempotentRpcContextFactory;
    this.util = util;
    this.leaseDuration = leaseDuration;
  }

  public byte[] handleRpc(
      Connection connection, IdempotencyStore idempotencyStore, IdempotentRpc<T> idempotentRpc) {
    try {
      checkState(
          connection.getAutoCommit() == false,
          "Connection %s must have auto commit off.",
          connection);
    } catch (SQLException e) {
      // TODO(weksler): Exception handling :-)
      log.error("Exception while checking auto commit state", e);
    }

    // Prepare for the outbound RPC
    IdempotentRpcContext idempotentRpcContext =
        idempotentRpcContextFactory.getIdempotencyRpcContext();
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
    idempotencyStore.upsertIdempotencyRecord(
        idempotentRpcContext.getIdempotencyKey(), idempotencyRecord);

    try {
      connection.commit();
    } catch (SQLException e) {
      // TODO(weksler): Exception handling :-)
      log.error("Exception while committing", e);
    }

    // Execute the outbound RPC
    idempotentRpcContext = idempotentRpc.execute(idempotentRpcContext);

    // Process the response from the outbound RPC.
    idempotentRpcContext = idempotentRpc.processResults(idempotentRpcContext);
    idempotencyRecord = idempotencyRecord.toBuilder().setStatus(RESPONDED).build();
    idempotencyStore.upsertIdempotencyRecord(
        idempotentRpcContext.getIdempotencyKey(), idempotencyRecord);

    return idempotentRpcContext.getResponse();
  }
}
