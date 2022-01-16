package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotencyStoreConnection;
import com.bablooka.idempotency.core.IdempotentRpc;
import dagger.Component;

@Component(modules = {CoreModule.class, IdempotencyExampleModule.class})
public interface IdempotencyExampleRoot {
  IdempotencyStore idempotencyStore();

  IdempotencyStoreConnection idempotencyStoreConnection();

  IdempotentRpc<String> idempotencyRpc();
}
