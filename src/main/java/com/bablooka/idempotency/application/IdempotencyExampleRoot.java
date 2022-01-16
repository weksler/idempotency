package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.*;
import dagger.Component;

@Component(modules = {CoreModule.class, IdempotencyExampleModule.class})
public interface IdempotencyExampleRoot {
  IdempotencyStore idempotencyStore();

  IdempotencyStoreConnection idempotencyStoreConnection();

  IdempotentRpc<String> idempotencyRpc();

  IdempotencyHandler<String> idempotencyHandler();
}
