package com.bablooka.idempotency.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface IdempotentRpc<T extends Object> {

  @Builder
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  class IdempotentRpcContext<T> {
    /** The idempotency key for this request */
    @Getter private String idempotencyKey;

    /** Process data to be passed around between the various processing phases of this request */
    @Getter private T processData;

    /** The response to be returned to the caller */
    @Getter private byte[] response;
  }

  IdempotentRpcContext<T> prepare(IdempotentRpcContext<T> context);

  IdempotentRpcContext<T> execute(IdempotentRpcContext<T> context);

  IdempotentRpcContext<T> processResults(IdempotentRpcContext<T> context);
}
