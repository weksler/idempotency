package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotencyHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class IdempotencyExample {
  public static void main(String args[]) {
    log.info("Starting up.");
    IdempotencyHandler<String> idempotencyHandler =
        DaggerIdempotencyExampleRoot.create().idempotencyHandler();
    log.info("Shutting down...");
  }
}
