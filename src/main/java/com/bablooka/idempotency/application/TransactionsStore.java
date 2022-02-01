package com.bablooka.idempotency.application;

import static com.bablooka.idempotency.dao.tables.Transactions.TRANSACTIONS;

import com.bablooka.idempotency.application.FakePaymentProcessor.FakePaymentData;
import com.bablooka.idempotency.dao.tables.records.TransactionsRecord;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;

@Log4j2
public class TransactionsStore {

  private enum PaymentStatus {
    PENDING,
  };

  private final DSLContext dslContext;

  @Inject
  TransactionsStore(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  void countRows() {
    log.info("Counting rows in the transactions table...");
    int count = dslContext.selectCount().from(TRANSACTIONS).fetchOne(0, int.class);
    log.info("Tables has {} rows.", count);
  }

  void insertPendingPayment(FakePaymentData fakePaymentData) {
    TransactionsRecord transactionsRecord = dslContext.newRecord(TRANSACTIONS);
    transactionsRecord.setAmountmicros(fakePaymentData.getAmountMicros());
    transactionsRecord.setIsocurrencycode(fakePaymentData.getIsoCurrencyCode());
    transactionsRecord.setDescription(fakePaymentData.getPaymentId());
    transactionsRecord.setStatus(String.valueOf(PaymentStatus.PENDING));
    log.info("About to store newly created record.");
    transactionsRecord.store();
    log.info("Stored record for payment {}, id {}", fakePaymentData, transactionsRecord.getId());
  }
}
