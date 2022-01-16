package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class IdempotencyExample {
  public static void main(String args[]) {
    log.info("Starting up!");
    IdempotencyHandler<String> idempotencyHandler = DaggerCoreRoot.create().idempotencyHandler();
    IdempotencyStoreConnection idempotencyStoreConnection =
        DaggerIdempotencyExampleRoot.create().idempotencyStoreConnection();
    IdempotencyStore idempotencyStore = DaggerIdempotencyExampleRoot.create().idempotencyStore();
    IdempotentRpc<String> idempotencyRpc = DaggerIdempotencyExampleRoot.create().idempotencyRpc();

    idempotencyHandler.handleRpc(idempotencyStoreConnection, idempotencyStore, idempotencyRpc);
    log.info("Shutting down...");
  }
}
