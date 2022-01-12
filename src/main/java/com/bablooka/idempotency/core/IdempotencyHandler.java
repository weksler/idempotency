package com.bablooka.idempotency.core;

import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.EXECUTING;
import static com.bablooka.idempotency.proto.IdempotencyRecord.Status.RESPONDED;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.Timestamp;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor(
    access = AccessLevel.PUBLIC,
    onConstructor_ = {@Inject})
public class IdempotencyHandler<T extends Object> {

  private final IdempotentRpcContextFactory<T> idempotentRpcContextFactory;
  private final Util util;
  private final Duration leaseDuration;

  public byte[] handleRpc(
      IdempotencyStoreConnection idempotencyStoreConnection,
      IdempotencyStore idempotencyStore,
      IdempotentRpc<T> idempotentRpc) {

    // Prepare for the outbound RPC
    IdempotentRpc.IdempotentRpcContext<T> idempotentRpcContext =
        idempotentRpcContextFactory.generateIdempotentRpcContext();
    Instant leaseExpiresAt = util.now().plus(leaseDuration);
    Timestamp timestamp =
        Timestamp.newBuilder()
            .setSeconds(leaseExpiresAt.getEpochSecond())
            .setNanos(leaseExpiresAt.getNano())
            .build();
    IdempotencyRecord idempotencyRecord =
        IdempotencyRecord.newBuilder()
            .setIdempotencyKey(idempotentRpcContext.getIdempotencyKey())
            .setRequestFingerprint("")
            .setStatus(EXECUTING)
            .setLeaseExpiresAt(timestamp)
            .build();
    idempotentRpcContext = idempotentRpc.prepare(idempotentRpcContext);
    idempotencyStore.upsertIdempotencyRecord(
        idempotentRpcContext.getIdempotencyKey(), idempotencyRecord);
    idempotencyStoreConnection.commit();

    // Execute the outbound RPC
    idempotentRpcContext = idempotentRpc.execute(idempotentRpcContext);

    // Process the response from the outbound RPC.
    idempotentRpcContext = idempotentRpc.processResults(idempotentRpcContext);
    idempotencyRecord = idempotencyRecord.toBuilder().setStatus(RESPONDED).build();

    return idempotentRpcContext.getResponse();
  }
}
