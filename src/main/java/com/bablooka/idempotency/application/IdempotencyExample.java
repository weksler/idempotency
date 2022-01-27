package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotencyHandler;
import com.bablooka.idempotency.core.IdempotencyStore;
import java.sql.Connection;
import java.sql.SQLException;
import javax.inject.Inject;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Log4j2
public class IdempotencyExample {

  public static final String JDBC_URL = "jdbcUrl";

  @Getter private static IdempotencyExampleConfig idempotencyExampleConfig;

  private final IdempotencyHandler<String> idempotencyHandler;
  private final IdempotencyConnectionProvider idempotencyConnectionProvider;
  private final IdempotencyStore idempotencyStore;
  private final FakePaymentProcessor fakePaymentProcessor;
  private final SqliteStore sqliteStore;

  @Data
  private static class Params {
    String jdbcUrl;
  }

  @Inject
  IdempotencyExample(
      @NonNull IdempotencyHandler<String> idempotencyHandler,
      @NonNull IdempotencyConnectionProvider idempotencyConnectionProvider,
      @NonNull IdempotencyStore idempotencyStore,
      @NonNull FakePaymentProcessor fakePaymentProcessor,
      @NonNull SqliteStore sqliteStore) {
    this.idempotencyHandler = idempotencyHandler;
    this.idempotencyConnectionProvider = idempotencyConnectionProvider;
    this.idempotencyStore = idempotencyStore;
    this.fakePaymentProcessor = fakePaymentProcessor;
    this.sqliteStore = sqliteStore;
  }

  public static void main(String args[]) throws SQLException, IdempotencyExampleException {
    log.info("Starting up!");
    Params params = parseParams(args);
    idempotencyExampleConfig =
        IdempotencyExampleConfig.builder().jdbcUrl(params.getJdbcUrl()).build();

    IdempotencyExampleRoot idempotencyExampleRoot = DaggerIdempotencyExampleRoot.create();
    IdempotencyExample idempotencyExample = idempotencyExampleRoot.idempotencyExample();

    log.info("Creating DB");
    idempotencyExample.prepareDb();

    log.info("Simulating a payment request");
    idempotencyExample.simulateIncomingPaymentRequest();

    log.info("Shutting down...");
  }

  private void simulateIncomingPaymentRequest() throws SQLException {
    Connection connection = idempotencyConnectionProvider.acquire();
    idempotencyHandler.handleRpc(connection, idempotencyStore, fakePaymentProcessor);
    connection.commit();
    idempotencyConnectionProvider.release(connection);
  }

  private void prepareDb() {
    sqliteStore.countRows();
  }

  private static Params parseParams(String[] args) throws IdempotencyExampleException {
    Options options = new Options();

    Option jdbcUrlOption = new Option("u", "jdbcUrl", true, "JDBC url");
    jdbcUrlOption.setRequired(true);
    options.addOption(jdbcUrlOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();

    try {
      CommandLine cmd = parser.parse(options, args);
      String jdbcUrl = cmd.getOptionValue(jdbcUrlOption);
      Params params = new Params();
      params.setJdbcUrl(jdbcUrl);
      log.info("JDBC url is: \"{}\"", jdbcUrl);
      return params;
    } catch (ParseException e) {
      log.error(e.getMessage());
      // TODO(weksler): Make this go to the log
      formatter.printHelp("IdempotencyExample", options);
      throw new IdempotencyExampleException(e);
    }
  }
}
