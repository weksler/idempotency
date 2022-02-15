package com.bablooka.idempotency.core;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import dagger.Lazy;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.exception.DataAccessException;

@Log4j2
public class IdempotencyTransactionProvider implements TransactionProvider {

  private static final String IDEMPOTENCY_TRANSACTION_PROVIDER_ACTIVE =
      "idempotencyTransactionProviderActive";

  private final Lazy<IdempotencyStore> idempotencyStoreLazy;
  private final Lazy<DSLContext> dslContextLazy;
  private final Util util;

  @Inject
  public IdempotencyTransactionProvider(
      @NonNull Lazy<IdempotencyStore> idempotencyStoreLazy,
      @NonNull Util util,
      @NonNull Lazy<DSLContext> dslContextLazy) {
    this.idempotencyStoreLazy = idempotencyStoreLazy;
    this.util = util;
    this.dslContextLazy = dslContextLazy;
  }

  @Override
  public void begin(TransactionContext ctx) throws DataAccessException {
    log.info("begin: Transaction context: {}", ctx);
  }

  @SneakyThrows
  @Override
  public void commit(TransactionContext ctx) throws DataAccessException {
    log.info("commit: Transaction context: {}", ctx);
    // TODO(weksler): Handle multi threading w.r.t global data
    // Need to see if putting this in the dslContext is too broad, e.g. if it leaks between threads
    // The use case I'm worried about is two threads performing two independent transactions, but
    // sharing the same dslContext. So this may not work, and I might need to find a better way
    // of detecting that I'm in the middle of a idempotency handler related commit.
    if (dslContextLazy.get().data().putIfAbsent(IDEMPOTENCY_TRANSACTION_PROVIDER_ACTIVE, "yes")
        != null) {
      log.info("commit: inside idempotency related commit. ignoring this one.");
      return;
    }

    try {
      idempotencyStoreLazy
          .get()
          .upsertIdempotencyRecord(
              "Hey ho!!!", util.protoToDbFormat(IdempotencyRecord.newBuilder().build()));
    } catch (Throwable t) {
      log.error("Exception thrown during idempotency layer work. Rethrowing.");
      dslContextLazy.get().data().remove(IDEMPOTENCY_TRANSACTION_PROVIDER_ACTIVE);
      throw t;
    }
  }

  @Override
  public void rollback(TransactionContext ctx) throws DataAccessException {
    log.info("rollback: Transaction context: {}", ctx);
  }
}
