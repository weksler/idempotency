package com.bablooka.idempotency.core;

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

  public void handleRpc(
      IdempotencyStoreConnection idempotencyStoreConnection,
      IdempotencyStore idempotencyStore,
      IdempotentRpc<T> idempotentRpc) {
    // idempotentRpc.prepare();
  }
}
