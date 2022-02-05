package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.proto.IdempotencyRecord;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;

@Log4j2
public class ExampleIdempotencyStore implements IdempotencyStore {

  private final DSLContext dslContext;

  @Inject
  public ExampleIdempotencyStore(@NonNull DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  public void upsertIdempotencyRecord(String idempotencyKey, IdempotencyRecord idempotencyRecord) {
    log.info("Upsert {}", idempotencyRecord);
    dslContext.transaction(
        configuration -> {
          // int i = DSL.using(configuration).newRecord(IDEMP)
        });
  }
}
