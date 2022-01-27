package com.bablooka.idempotency.application;

import lombok.Builder;
import lombok.Getter;

@Builder
public class IdempotencyExampleConfig {
  @Getter private String jdbcUrl;
}
