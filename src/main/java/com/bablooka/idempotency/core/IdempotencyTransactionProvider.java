package com.bablooka.idempotency.core;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import dagger.Lazy;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.exception.DataAccessException;

@Log4j2
public class IdempotencyTransactionProvider implements TransactionProvider {

  private final Lazy<IdempotencyStore> idempotencyStoreLazy;
  private final Util util;

  @Inject
  public IdempotencyTransactionProvider(
      @NonNull Lazy<IdempotencyStore> idempotencyStoreLazy, @NonNull Util util) {
    this.idempotencyStoreLazy = idempotencyStoreLazy;
    this.util = util;
  }

  @Override
  public void begin(TransactionContext ctx) throws DataAccessException {
    log.info("begin: Transaction context: {}", ctx);
  }

  @SneakyThrows
  @Override
  public void commit(TransactionContext ctx) throws DataAccessException {
    log.info("commit: Transaction context: {}", ctx);
    idempotencyStoreLazy
        .get()
        .upsertIdempotencyRecord(
            "Hey ho!!!", util.protoToDbFormat(IdempotencyRecord.newBuilder().build()));
  }

  @Override
  public void rollback(TransactionContext ctx) throws DataAccessException {
    log.info("rollback: Transaction context: {}", ctx);
  }
}
