package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotentRpc;
import javax.inject.Inject;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FakePaymentProcessor implements IdempotentRpc<FakePaymentProcessor.FakePaymentData> {

  @Builder
  public static class FakePaymentData {
    @Getter String isoCurrencyCode;
    @Getter long amountMicros;
    @Getter String paymentId;
  }

  private final IdempotencyConnectionProvider idempotencyConnectionProvider;

  @Inject
  FakePaymentProcessor(IdempotencyConnectionProvider idempotencyConnectionProvider) {
    this.idempotencyConnectionProvider = idempotencyConnectionProvider;
  }

  @Override
  public IdempotentRpcContext prepare(IdempotentRpcContext context) {
    log.info("Preparing to process payment {}", context.getProcessData());

    return context;
  }

  @Override
  public IdempotentRpcContext execute(IdempotentRpcContext context) {
    return context;
  }

  @Override
  public IdempotentRpcContext processResults(IdempotentRpcContext context) {
    return context;
  }
}
