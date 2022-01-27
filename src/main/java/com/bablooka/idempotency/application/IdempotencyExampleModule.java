package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.core.CoreModule.IDEMPOTENCY_KEY;
import static org.jooq.SQLDialect.SQLITE;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import com.bablooka.idempotency.core.IdempotentRpcContextFactory;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.util.function.Supplier;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@Module(includes = CoreModule.class)
public class IdempotencyExampleModule {

  @Provides
  IdempotencyStore provideIdempotencyStore() {
    return (idempotencyKey, idempotencyRecord) -> {};
  }

  @Provides
  IdempotentRpcContextFactory<String> provideIdempotencyRpcContextFactory(
      @Named(IDEMPOTENCY_KEY) Supplier<String> idempotencyKeySupplier) {
    return () -> {
      String idempotencyKey = idempotencyKeySupplier.get();
      return IdempotentRpcContext.builder().idempotencyKey(idempotencyKey).build();
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
