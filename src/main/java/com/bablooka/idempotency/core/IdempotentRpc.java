package com.bablooka.idempotency.core;

import lombok.Data;

public interface IdempotentRpc<T extends Object> {
  @Data
  class IdempotentRpcContext<T> {
    T processData;

    String idempotencyKey;
  }

  IdempotentRpcContext prepare(IdempotentRpcContext context);

  IdempotentRpcContext execute(IdempotentRpcContext context);

  IdempotentRpcContext processResults(IdempotentRpcContext context);
}
