package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.core.CoreModule.IDEMPOTENCY_KEY;
import static org.jooq.SQLDialect.SQLITE;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc;
import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import com.bablooka.idempotency.core.IdempotentRpcContextFactory;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.inject.Named;
import javax.inject.Singleton;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

@Module(includes = CoreModule.class)
public class IdempotencyExampleModule {

  // TODO(weksler): Allow this to be configured
  private static final String SQLITE_DB_URI = "jdbc:sqlite:/tmp/idempotency.db";

  static final String FAKE_PAYMENT_PROCESSOR = "fake_payment_processor";

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
  @Named(FAKE_PAYMENT_PROCESSOR)
  IdempotentRpc<String> provideIdempotencyRpc(FakePaymentProcessor fakePaymentProcessor) {
    return fakePaymentProcessor;
  }

  @Provides
  Connection provideConnection(ConnectionProvider connectionProvider) {
    return connectionProvider.acquire();
  }

  @Singleton
  @Provides
  ConnectionProvider provideConnectionProvider() {
    return new ConnectionProvider() {
      Connection connection;

      @Override
      public synchronized Connection acquire() throws DataAccessException {
        // Only a single app wide connection!
        try {
          if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(SQLITE_DB_URI);
            connection.setAutoCommit(false);
          }
        } catch (SQLException e) {
          throw new DataAccessException("Unable to get a connection to " + SQLITE_DB_URI, e);
        }
        return connection;
      }

      @Override
      public synchronized void release(Connection connection) throws DataAccessException {
        try {
          connection.close();
        } catch (SQLException e) {
          throw new DataAccessException("Unable to close DB connection ", e);
        }
      }
    };
  }

  @Provides
  DSLContext providesDslContext(ConnectionProvider connectionProvider) {
    return DSL.using(connectionProvider, SQLITE);
  }
}
