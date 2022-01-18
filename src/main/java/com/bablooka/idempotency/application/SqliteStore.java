package com.bablooka.idempotency.application;

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

  void createDb() {
    dslContext.createDatabase("fake_payment_handler_dev");
    dslContext.createTable("payments");
  }
}
