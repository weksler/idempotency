package com.bablooka.idempotency.core;

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
}
