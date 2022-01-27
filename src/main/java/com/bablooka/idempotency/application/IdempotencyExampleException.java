package com.bablooka.idempotency.application;

class IdempotencyExampleException extends Exception {
  public IdempotencyExampleException() {}

  public IdempotencyExampleException(String message) {
    super(message);
  }

  public IdempotencyExampleException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdempotencyExampleException(Throwable cause) {
    super(cause);
  }

  public IdempotencyExampleException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
