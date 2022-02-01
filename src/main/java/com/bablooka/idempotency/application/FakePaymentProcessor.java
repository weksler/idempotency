package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotentRpc;
import javax.inject.Inject;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FakePaymentProcessor implements IdempotentRpc<FakePaymentProcessor.FakePaymentData> {

  @Builder
  @ToString
  public static class FakePaymentData {
    @Getter String isoCurrencyCode;
    @Getter long amountMicros;
    @Getter String paymentId;
  }

  private final IdempotencyConnectionProvider idempotencyConnectionProvider;
  private final TransactionsStore transactionsStore;

  @Inject
  FakePaymentProcessor(
      @NonNull IdempotencyConnectionProvider idempotencyConnectionProvider,
      @NonNull TransactionsStore transactionsStore) {
    this.idempotencyConnectionProvider = idempotencyConnectionProvider;
    this.transactionsStore = transactionsStore;
  }

  @Override
  public IdempotentRpcContext<FakePaymentData> prepare(
      IdempotentRpcContext<FakePaymentData> context) {
    log.info("Preparing to process payment {}", context.getProcessData());
    transactionsStore.insertPendingPayment(context.getProcessData());
    return context;
  }

  @Override
  public IdempotentRpcContext<FakePaymentData> execute(
      IdempotentRpcContext<FakePaymentData> context) {
    return context;
  }

  @Override
  public IdempotentRpcContext<FakePaymentData> processResults(
      IdempotentRpcContext<FakePaymentData> context) {
    return context;
  }
}
