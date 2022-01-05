package com.bablooka.idempotency.core;

import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;

public class IdempotentRpcContextFactory {
  <T extends Object> IdempotentRpcContext<T> generateIdempotentRpcContext() {
    return new IdempotentRpcContext<>();
  }
}
