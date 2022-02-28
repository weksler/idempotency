package com.bablooka.idempotency.core;

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

  private static final String IDEMPOTENCY_ACTIVE = "idempotencyActive";

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
    log.info(
        "begin: Transaction context: {}, idempotency is {} active.",
        ctx,
        isIdempotencyActive(dslContextLazy.get().data().get(IDEMPOTENCY_ACTIVE)) ? "" : "NOT");
  }

  @SneakyThrows
  @Override
  public void commit(TransactionContext ctx) throws DataAccessException {
    log.info(
        "commit: Transaction context: {}, idempotency is {} active.",
        ctx,
        isIdempotencyActive(dslContextLazy.get().data().get(IDEMPOTENCY_ACTIVE)) ? "" : "NOT");
  }

  @Override
  public void rollback(TransactionContext ctx) throws DataAccessException {
    log.info(
        "rollback: Transaction context: {}, idempotency is {} active.",
        ctx,
        isIdempotencyActive(dslContextLazy.get().data().get(IDEMPOTENCY_ACTIVE)) ? "" : "NOT");
  }

  private static boolean isIdempotencyActive(Object nullableIdempotencyActiveState) {
    if (nullableIdempotencyActiveState == null) {
      return false;
    }

    return true;
  }
}
