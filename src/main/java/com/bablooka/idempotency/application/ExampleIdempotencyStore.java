package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.dao.Tables.IDEMPOTENCY_RECORDS;

import com.bablooka.idempotency.core.IdempotencyStore;
import com.bablooka.idempotency.core.Util;
import com.bablooka.idempotency.dao.tables.records.IdempotencyRecordsRecord;
import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@Log4j2
public class ExampleIdempotencyStore implements IdempotencyStore {

  private final DSLContext dslContext;

  @Inject
  public ExampleIdempotencyStore(@NonNull DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  public void upsertIdempotencyRecord(
      @NonNull String idempotencyKey, @NonNull final IdempotencyRecord idempotencyRecord) {
    log.info("Upsert {}", idempotencyRecord);
    dslContext.transaction(
        configuration -> {
          DSLContext dslContext = DSL.using(configuration);
          IdempotencyRecordsRecord idempotencyRecordsRecord =
              dslContext
                  .selectFrom(IDEMPOTENCY_RECORDS)
                  .where(IDEMPOTENCY_RECORDS.IDEMPOTENCYKEY.eq(idempotencyKey))
                  .fetchOne();
          if (idempotencyRecordsRecord == null) {
            log.debug("Idempotency key {} not found, inserting a new record", idempotencyKey);
            idempotencyRecordsRecord = dslContext.newRecord(IDEMPOTENCY_RECORDS);
            idempotencyRecordsRecord.setIdempotencykey(idempotencyKey);
            idempotencyRecordsRecord.setIdempotencyrecord(toDbFormat(idempotencyRecord));
          } else {
            idempotencyRecordsRecord.setIdempotencyrecord(
                toDbFormat(
                    Util.validateAndUpdateIdempotencyRecord(
                        fromIdempotencyRecordsRecord(idempotencyRecordsRecord),
                        idempotencyRecord)));
          }
          idempotencyRecordsRecord.store();
        });
  }

  private static IdempotencyRecord fromIdempotencyRecordsRecord(
      @NonNull IdempotencyRecordsRecord idempotencyRecordsRecord)
      throws IdempotencyExampleException {
    return fromDbFormat(idempotencyRecordsRecord.getIdempotencyrecord());
  }

  private static byte[] toDbFormat(IdempotencyRecord idempotencyRecord)
      throws IdempotencyExampleException {
    try {
      return JsonFormat.printer().print(idempotencyRecord).getBytes();
    } catch (InvalidProtocolBufferException e) {
      log.error("Unable to parse idempotency record {}", idempotencyRecord, e);
      throw new IdempotencyExampleException(e);
    }
  }

  private static IdempotencyRecord fromDbFormat(byte[] dbIdempotencyRecord)
      throws IdempotencyExampleException {
    try {
      IdempotencyRecord.Builder idempotencyRecordBuilder = IdempotencyRecord.newBuilder();
      JsonFormat.parser().merge(new String(dbIdempotencyRecord), idempotencyRecordBuilder);
      return idempotencyRecordBuilder.build();
    } catch (InvalidProtocolBufferException e) {
      log.error("Unable to parse idempotency record {}", dbIdempotencyRecord, e);
      throw new IdempotencyExampleException(e);
    }
  }
}
