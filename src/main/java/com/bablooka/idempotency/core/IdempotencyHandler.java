package com.bablooka.idempotency.core;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.Timestamp;
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

  public byte[] handleRpc(
      IdempotencyStoreConnection idempotencyStoreConnection,
      IdempotencyStore idempotencyStore,
      IdempotentRpc<T> idempotentRpc) {
    IdempotentRpc.IdempotentRpcContext<T> idempotentRpcContext =
        idempotentRpcContextFactory.generateIdempotentRpcContext();
    Instant time = Instant.now();
    Timestamp timestamp =
        Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
    IdempotencyRecord idempotencyRecord =
        IdempotencyRecord.newBuilder()
            .setIdempotencyKey(idempotentRpcContext.getIdempotencyKey())
            .setRequestFingerprint("")
            .setStatus(IdempotencyRecord.Status.EXECUTING)
            .setLeaseExpiresAt(timestamp)
            .build();
    // Prepare for the outbound RPC
    idempotentRpcContext = idempotentRpc.prepare(idempotentRpcContext);
    idempotencyStore.upsertIdempotencyRecord(
        idempotentRpcContext.getIdempotencyKey(), idempotencyRecord);
    idempotencyStoreConnection.commit();

    // Execute the outbound RPC
    idempotentRpcContext = idempotentRpc.execute(idempotentRpcContext);

    // Process the response from the outbound RPC
    idempotentRpcContext = idempotentRpc.processResults(idempotentRpcContext);

    return idempotentRpcContext.getResponse();
  }
}
