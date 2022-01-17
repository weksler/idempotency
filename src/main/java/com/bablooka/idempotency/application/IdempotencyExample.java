package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.*;
import java.sql.Connection;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class IdempotencyExample {
  public static void main(String args[]) {
    log.info("Starting up!");
    IdempotencyExampleRoot idempotencyExampleRoot = DaggerIdempotencyExampleRoot.create();

    IdempotencyHandler<String> idempotencyHandler = idempotencyExampleRoot.idempotencyHandler();
    Connection idempotencyStoreConnection = idempotencyExampleRoot.idempotencyStoreConnection();
    IdempotencyStore idempotencyStore = idempotencyExampleRoot.idempotencyStore();
    IdempotentRpc<String> idempotencyRpc = idempotencyExampleRoot.idempotencyRpc();

    idempotencyHandler.handleRpc(idempotencyStoreConnection, idempotencyStore, idempotencyRpc);
    log.info("Shutting down...");
  }
}
