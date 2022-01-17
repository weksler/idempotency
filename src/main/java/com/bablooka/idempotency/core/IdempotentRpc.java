package com.bablooka.idempotency.core;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface IdempotentRpc<T extends Object> {

  @Data
  @AllArgsConstructor
  class IdempotentRpcContext<T> {
    /** The idempotency key for this request */
    private String idempotencyKey;

    /** Process data to be passed around between the various processing phases of this request */
    private T processData;

    /** The response to be returned to the caller */
    private byte[] response;
  }

  IdempotentRpcContext prepare(IdempotentRpcContext context);

  IdempotentRpcContext execute(IdempotentRpcContext context);

  IdempotentRpcContext processResults(IdempotentRpcContext context);
}
