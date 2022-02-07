package com.bablooka.idempotency.core;

import lombok.NonNull;

/** A storage medium for idempotency records */
public interface IdempotencyStore {

  /**
   * Inserts a new idempotency record keyed by {code idempotencyKey} if none exists, or updates it
   * otherwise.
   */
  void upsertIdempotencyRecord(
      @NonNull String idempotencyKey, @NonNull byte[] serializedIdempotencyRecord);
}
