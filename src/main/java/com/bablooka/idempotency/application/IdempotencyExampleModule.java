package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotencyStoreConnection;
import com.bablooka.idempotency.core.IdempotentRpc;
import dagger.Module;
import dagger.Provides;

@Module
public class IdempotencyExampleModule {

  @Provides
  IdempotencyStore provideIdempotencyStore() {
    return (idempotencyKey, idempotencyRecord) -> {};
  }

  @Provides
  IdempotencyStoreConnection provideIdempotencyStoreConnection() {
    return () -> {};
  }

  @Provides
  IdempotentRpc<String> provideIdempotencyRpc() {
    return new IdempotentRpc<>() {
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
    };
  }
}
