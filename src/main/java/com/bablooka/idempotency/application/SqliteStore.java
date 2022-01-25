package com.bablooka.idempotency.application;

import javax.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;

@Log4j2
public class SqliteStore {

  private final DSLContext dslContext;

  @Inject
  SqliteStore(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  void createDb() {
    log.info("About to create the payments table...");
    Record result =
        dslContext.fetchSingle(
            "CREATE TABLE payments (payment_id INTEGER PRIMARY KEY, amount INTEGER, description TEXT);");
    log.info("Created!");
  }
}
