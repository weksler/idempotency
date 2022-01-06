package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import com.bablooka.idempotency.core.IdempotentRpcContextFactory;
import dagger.Module;
import dagger.Provides;

@Module
interface IdempotencyExampleModule {

  @Provides
  static IdempotentRpcContextFactory<String> provideIdempotencyRpcContextFactory() {
    return (IdempotentRpcContextFactory) () -> new IdempotentRpcContext<String>();
  }
}
