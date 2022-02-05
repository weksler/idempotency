package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.core.CoreModule.IDEMPOTENCY_KEY;
import static org.jooq.SQLDialect.SQLITE;

import com.bablooka.idempotency.application.FakePaymentProcessor.FakePaymentData;
import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import com.bablooka.idempotency.core.IdempotentRpcContextFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.util.function.Supplier;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@Module(includes = {CoreModule.class, IdempotencyExampleModule.IdempotencyStoreModule.class})
public class IdempotencyExampleModule {

  @Module
  public abstract static class IdempotencyStoreModule {
    @Binds
    abstract IdempotencyStore provideIdempotencyStore(
        ExampleIdempotencyStore exampleIdempotencyStore);
  }

  @Provides
  IdempotentRpcContextFactory<FakePaymentData> provideIdempotencyRpcContextFactory(
      @Named(IDEMPOTENCY_KEY) Supplier<String> idempotencyKeySupplier) {
    return (processData) -> {
      String idempotencyKey = idempotencyKeySupplier.get();
      return IdempotentRpcContext.builder()
          .idempotencyKey(idempotencyKey)
          .processData(processData)
          .build();
    };
  }

  @Provides
  Connection provideConnection(IdempotencyConnectionProvider idempotencyConnectionProvider) {
    return idempotencyConnectionProvider.acquire();
  }

  @Singleton
  @Provides
  IdempotencyConnectionProvider provideConnectionProvider() {
    return new IdempotencyConnectionProvider(
        IdempotencyExample.getIdempotencyExampleConfig().getJdbcUrl());
  }

  @Provides
  DSLContext providesDslContext(IdempotencyConnectionProvider idempotencyConnectionProvider) {
    return DSL.using(idempotencyConnectionProvider, SQLITE);
  }
}
