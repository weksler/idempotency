package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyHandler;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc;
import dagger.Component;
import java.sql.Connection;

@Component(modules = {CoreModule.class, IdempotencyExampleModule.class})
public interface IdempotencyExampleRoot {
  IdempotencyStore idempotencyStore();

  Connection idempotencyStoreConnection();

  IdempotentRpc<String> idempotencyRpc();

  IdempotencyHandler<String> idempotencyHandler();
}
