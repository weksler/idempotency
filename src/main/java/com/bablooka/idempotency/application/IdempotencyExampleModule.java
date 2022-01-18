package com.bablooka.idempotency.application;

import static org.jooq.SQLDialect.SQLITE;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.IdempotentRpc;
import dagger.Module;
import dagger.Provides;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Named;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@Module(includes = CoreModule.class)
public class IdempotencyExampleModule {

  // TODO(weksler): Allow this to be configured
  private static final String SQLITE_DB_URI = "jdbc:sqlite:idempotency.db";

  static final String FAKE_PAYMENT_PROCESSOR = "fake_payment_processor";

  @Provides
  IdempotencyStore provideIdempotencyStore() {
    return (idempotencyKey, idempotencyRecord) -> {};
  }

  @Provides
  @Named(FAKE_PAYMENT_PROCESSOR)
  IdempotentRpc<String> provideIdempotencyRpc(FakePaymentProcessor fakePaymentProcessor) {
    return fakePaymentProcessor;
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

  @Provides
  DSLContext providesDslContext(Connection connection) {
    return DSL.using(connection, SQLITE);
  }
}
