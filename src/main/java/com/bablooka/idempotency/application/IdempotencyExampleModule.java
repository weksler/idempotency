package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Module(includes = CoreModule.class)
public class IdempotencyExampleModule {

  // TODO(weksler): Allow this to be configured
  private static final String SQLITE_DB_URI = "jdbc:sqlite:idempotency.db";

  @Provides
  IdempotencyStore provideIdempotencyStore() {
    return (idempotencyKey, idempotencyRecord) -> {};
  }

  @Provides
  IdempotentRpc<String> provideIdempotencyRpc() {
    return new IdempotentRpc<>() {
      @Override
      public IdempotentRpcContext prepare(IdempotentRpcContext context) {
        return context;
      }

      @Override
      public IdempotentRpcContext execute(IdempotentRpcContext context) {
        return context;
      }

      @Override
      public IdempotentRpcContext processResults(IdempotentRpcContext context) {
        return context;
      }
    };
  }

  @Provides
  Connection provideConnection() {
    try {
      Connection connection = DriverManager.getConnection(SQLITE_DB_URI);
      connection.setAutoCommit(false);
      return connection;
    } catch (SQLException e) {
      throw new IllegalStateException("Unable to instantiate SQLite db " + SQLITE_DB_URI, e);
    }
  }
}
