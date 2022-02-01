package com.bablooka.idempotency.application;

import com.bablooka.idempotency.application.FakePaymentProcessor.FakePaymentData;
import com.bablooka.idempotency.core.IdempotencyHandler;
import com.bablooka.idempotency.core.IdempotencyStore;
import java.sql.Connection;
import java.sql.SQLException;
import javax.inject.Inject;
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

/**
 * An example use of the idempotency framework. This code exists primarily to provide the
 * scaffolding, into which the framework can be built. I am using it to help me develop the core
 * features of the framework in a mini real world scenario.
 *
 * @author Michel Weksler
 */
@Log4j2
public class IdempotencyExample {

  /**
   * Having the config available like this, where it is set from the {@code main} static method, is
   * not the best idea, as it introduces potential threading and concurrency issues. However, those
   * issues are mostly theoretical - there will really is only going to be a single main thread -
   * and the alternative, which is to use dagger's {@code AssistedInject} framework, is a lot more
   * cumbersome and verbose, and would create more confusion that it is worth.
   */
  @Getter private static IdempotencyExampleConfig idempotencyExampleConfig;

  private final IdempotencyHandler<FakePaymentData> idempotencyHandler;
  private final IdempotencyConnectionProvider idempotencyConnectionProvider;
  private final IdempotencyStore idempotencyStore;
  private final FakePaymentProcessor fakePaymentProcessor;
  private final TransactionsStore transactionsStore;

  @Inject
  IdempotencyExample(
      @NonNull IdempotencyHandler<FakePaymentData> idempotencyHandler,
      @NonNull IdempotencyConnectionProvider idempotencyConnectionProvider,
      @NonNull IdempotencyStore idempotencyStore,
      @NonNull FakePaymentProcessor fakePaymentProcessor,
      @NonNull TransactionsStore transactionsStore) {
    this.idempotencyHandler = idempotencyHandler;
    this.idempotencyConnectionProvider = idempotencyConnectionProvider;
    this.idempotencyStore = idempotencyStore;
    this.fakePaymentProcessor = fakePaymentProcessor;
    this.transactionsStore = transactionsStore;
  }

  public static void main(String args[]) throws SQLException, IdempotencyExampleException {
    log.info("Starting up!");
    idempotencyExampleConfig = parseParams(args);

    IdempotencyExampleRoot idempotencyExampleRoot = DaggerIdempotencyExampleRoot.create();
    IdempotencyExample idempotencyExample = idempotencyExampleRoot.idempotencyExample();

    log.info("Verifying DB connection...");
    idempotencyExample.prepareDb();

    log.info("Simulating a payment request");
    idempotencyExample.simulateIncomingPaymentRequest();

    log.info("Shutting down...");
  }

  private void simulateIncomingPaymentRequest() throws SQLException {
    Connection connection = idempotencyConnectionProvider.acquire();
    FakePaymentData fakePaymentData =
        FakePaymentData.builder()
            .amountMicros(23_000_000)
            .paymentId("payment_id")
            .isoCurrencyCode("USD")
            .build();
    idempotencyHandler.handleRpc(
        connection, idempotencyStore, fakePaymentProcessor, fakePaymentData);
    idempotencyConnectionProvider.release(connection);
  }

  private void prepareDb() {
    transactionsStore.countRows();
  }

  private static IdempotencyExampleConfig parseParams(String[] args)
      throws IdempotencyExampleException {
    Options options = new Options();

    Option jdbcUrlOption = new Option("u", "jdbcUrl", true, "JDBC url");
    jdbcUrlOption.setRequired(true);
    options.addOption(jdbcUrlOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();

    try {
      CommandLine cmd = parser.parse(options, args);
      String jdbcUrl = cmd.getOptionValue(jdbcUrlOption);
      IdempotencyExampleConfig idempotencyExampleConfig =
          IdempotencyExampleConfig.builder().jdbcUrl(jdbcUrl).build();
      log.info("JDBC url is: \"{}\"", jdbcUrl);
      return idempotencyExampleConfig;
    } catch (ParseException e) {
      log.error(e.getMessage());
      // TODO(weksler): Make this go to the log
      formatter.printHelp("IdempotencyExample", options);
      throw new IdempotencyExampleException(e);
    }
  }
}
