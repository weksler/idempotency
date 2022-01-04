package com.bablooka.idempotency.core;

import com.bablooka.idempotency.proto.IdempotencyRecord;

public interface IdempotencyStore {
  void storeIdempotencyRecord(String idempotencyKey, IdempotencyRecord idempotencyRecord);
}
