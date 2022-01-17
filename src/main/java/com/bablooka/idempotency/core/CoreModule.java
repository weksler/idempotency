package com.bablooka.idempotency.core;

import dagger.Module;
import dagger.Provides;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;

@Module
public class CoreModule {
  @Provides
  static IdempotentRpcContextFactory<String> provideIdempotencyRpcContextFactory() {
    return () -> {
      String idempotencyKey = UUID.randomUUID().toString();
      return new IdempotentRpc.IdempotentRpcContext(idempotencyKey, null, null);
    };
  }

  @Provides
  Clock provideClock() {
    // This is the clock used by Java's default Instant implementation.
    return Clock.systemUTC();
  }

  @Provides
  Duration provideDefaultIdempotencyDureation() {
    // TODO(weksler): Allow this to be configured
    // Default idempotency least duration is 1 minute.
    return Duration.ofMinutes(1);
  }
}
