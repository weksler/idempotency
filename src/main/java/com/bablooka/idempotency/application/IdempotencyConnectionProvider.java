package com.bablooka.idempotency.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Inject;
import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;

public class IdempotencyConnectionProvider implements ConnectionProvider {

  private Connection connection;

  private final String jdbcUrl;

  @Inject
  public IdempotencyConnectionProvider(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  @Override
  public synchronized Connection acquire() throws DataAccessException {
    // Only a single app wide connection!
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(jdbcUrl);
        connection.setAutoCommit(false);
      }
    } catch (SQLException e) {
      throw new DataAccessException("Unable to get a connection to " + jdbcUrl, e);
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
}
