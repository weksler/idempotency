package com.bablooka.idempotency.core;

import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;

public interface IdempotentRpcContextFactory<T extends Object> {
  IdempotentRpcContext getIdempotencyRpcContext();
}
