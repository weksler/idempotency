package com.bablooka.idempotency.core;

import static com.google.common.base.Preconditions.checkState;

import com.bablooka.idempotency.proto.IdempotencyRecord;
import com.google.protobuf.Timestamp;
import java.time.Clock;
import java.time.Instant;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Util {

  private final Clock clock;

  @Inject
  public Util(@NonNull Clock clock) {
    this.clock = clock;
  }

  Timestamp timestampFromInstant(@NonNull Instant instant) {
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  Timestamp timestampOfCurrentTime() {
    return timestampFromInstant(now());
  }

  Instant now() {
    return Instant.now(clock);
  }

  /**
   * Updates the {@code updated} record with information from {@code from}, ensuring that the {@code
   * idempotencyKey} field is not being changed, and returns the resulting proto.
   */
  public static IdempotencyRecord validateAndUpdateIdempotencyRecord(
      IdempotencyRecord updated, IdempotencyRecord from) {
    checkState(
        updated.getIdempotencyKey().equals(from.getIdempotencyKey()),
        "Can't change idempotency key while updating [%s] -> [%s]",
        updated,
        from);
    return updated
        .toBuilder()
        .setStatus(from.getStatus())
        .setLeaseExpiresAt(from.getLeaseExpiresAt())
        .setRequestFingerprint(from.getRequestFingerprint())
        .setRpcResponse(from.getRpcResponse())
        .build();
  }
}
