package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotentRpc;
import java.sql.Connection;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FakePaymentProcessor implements IdempotentRpc<String> {

  private final Connection connection;

  @Inject
  FakePaymentProcessor(Connection connection) {
    this.connection = connection;
  }

  @Override
  public IdempotentRpcContext prepare(IdempotentRpcContext context) {
    return null;
  }

  @Override
  public IdempotentRpcContext execute(IdempotentRpcContext context) {
    return null;
  }

  @Override
  public IdempotentRpcContext processResults(IdempotentRpcContext context) {
    return null;
  }
}
