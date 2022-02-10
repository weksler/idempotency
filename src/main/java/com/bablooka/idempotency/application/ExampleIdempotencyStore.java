package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.dao.Tables.IDEMPOTENCY_RECORDS;

import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.Util;
import com.bablooka.idempotency.dao.tables.records.IdempotencyRecordsRecord;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@Log4j2
public class ExampleIdempotencyStore implements IdempotencyStore {

  private final DSLContext dslContext;
  private final Util util;

  @Inject
  public ExampleIdempotencyStore(@NonNull DSLContext dslContext, @NonNull Util util) {
    this.dslContext = dslContext;
    this.util = util;
  }

  @Override
  public void upsertIdempotencyRecord(
      @NonNull String idempotencyKey, @NonNull final byte[] serializedIdempotencyRecord) {
    log.info("Upsert record for {}", idempotencyKey);
    dslContext.transaction(
        configuration -> {
          DSLContext dslContext = DSL.using(configuration);
          IdempotencyRecordsRecord idempotencyRecordsRecord =
              dslContext
                  .selectFrom(IDEMPOTENCY_RECORDS)
                  .where(IDEMPOTENCY_RECORDS.IDEMPOTENCYKEY.eq(idempotencyKey))
                  .fetchOne();
          long nowMillis = util.now().toEpochMilli();
          if (idempotencyRecordsRecord == null) {
            log.debug("Idempotency key {} not found, inserting a new record", idempotencyKey);
            idempotencyRecordsRecord = dslContext.newRecord(IDEMPOTENCY_RECORDS);
            idempotencyRecordsRecord.setIdempotencykey(idempotencyKey);
            idempotencyRecordsRecord.setCreatedat(nowMillis);
          }
          idempotencyRecordsRecord.setIdempotencyrecord(serializedIdempotencyRecord);
          idempotencyRecordsRecord.setUpdatedat(nowMillis);
          idempotencyRecordsRecord.store();
        });
  }
}
