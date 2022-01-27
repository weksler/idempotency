package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.dao.tables.Transactions.TRANSACTIONS;

import javax.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;

@Log4j2
public class SqliteStore {

  private final DSLContext dslContext;

  @Inject
  SqliteStore(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  void countRows() {
    log.info("Counting rows in the transactions table...");
    int count = dslContext.selectCount().from(TRANSACTIONS).fetchOne(0, int.class);
    log.info("Tables has {} rows.", count);
  }
}
