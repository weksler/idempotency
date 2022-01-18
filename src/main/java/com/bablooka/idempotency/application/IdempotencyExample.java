package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.application.IdempotencyExampleModule.FAKE_PAYMENT_PROCESSOR;

import com.bablooka.idempotency.core.IdempotencyHandler;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc;
import java.sql.Connection;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class IdempotencyExample {

  private final IdempotencyHandler<String> idempotencyHandler;
  private final Connection connection;
  private final IdempotencyStore idempotencyStore;
  private final IdempotentRpc<String> fakePaymentProcessor;
  private final SqliteStore sqliteStore;

  @Inject
  IdempotencyExample(
      @NonNull IdempotencyHandler<String> idempotencyHandler,
      @NonNull Connection connection,
      @NonNull IdempotencyStore idempotencyStore,
      @NonNull @Named(FAKE_PAYMENT_PROCESSOR) IdempotentRpc<String> fakePaymentProcessor,
      @NonNull SqliteStore sqliteStore) {
    this.idempotencyHandler = idempotencyHandler;
    this.connection = connection;
    this.idempotencyStore = idempotencyStore;
    this.fakePaymentProcessor = fakePaymentProcessor;
    this.sqliteStore = sqliteStore;
  }

  public static void main(String args[]) {
    log.info("Starting up!");
    IdempotencyExampleRoot idempotencyExampleRoot = DaggerIdempotencyExampleRoot.create();
    IdempotencyExample idempotencyExample = idempotencyExampleRoot.idempotencyExample();

    log.info("Creating DB");
    idempotencyExample.prepareDb();

    log.info("Simulating a payment request");
    idempotencyExample.simulateIncomingPaymentRequest();

    log.info("Shutting down...");
  }

  private void simulateIncomingPaymentRequest() {
    idempotencyHandler.handleRpc(connection, idempotencyStore, fakePaymentProcessor);
  }

  private void prepareDb() {
    sqliteStore.createDb();
  }
}
