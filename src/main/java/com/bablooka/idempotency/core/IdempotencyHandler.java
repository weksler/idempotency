package com.bablooka.idempotency.core;

// import lombok.extern.log4j.Log4j2;

// @Log4j2
public class IdempotencyHandler {

  // @Inject
  private IdempotentRpcContextFactory idempotentRpcContextFactory;

  public void handleRpc(
      IdempotencyStoreConnection idempotencyStoreConnection,
      IdempotencyStore idempotencyStore,
      IdempotentRpc<?> idempotentRpc) {
    // idempotentRpc.prepare();
  }
}
