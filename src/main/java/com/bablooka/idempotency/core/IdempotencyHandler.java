package com.bablooka.idempotency.core;

import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor(
    access = AccessLevel.PRIVATE,
    onConstructor_ = {@Inject})
public class IdempotencyHandler {

  private final IdempotentRpcContextFactory idempotentRpcContextFactory;

  public void handleRpc(
      IdempotencyStoreConnection idempotencyStoreConnection,
      IdempotencyStore idempotencyStore,
      IdempotentRpc<?> idempotentRpc) {
    // idempotentRpc.prepare();
  }
}
