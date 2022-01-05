package com.bablooka.idempotency.core;

import com.bablooka.idempotency.proto.IdempotencyRecord;

/** A storage medium for idempotency records */
public interface IdempotencyStore {

  /**
   * Inserts a new idempotency record keyed by {code idempotencyKey} if none exists, or updates it
   * otherwise.
   */
  void upsertIdempotencyRecord(String idempotencyKey, IdempotencyRecord idempotencyRecord);
}
