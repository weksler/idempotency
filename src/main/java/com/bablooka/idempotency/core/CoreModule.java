package com.bablooka.idempotency.core;

import com.bablooka.idempotency.core.IdempotentRpc.IdempotentRpcContext;
import dagger.Module;
import dagger.Provides;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;
import javax.inject.Named;

@Module
public class CoreModule {

  public static final String IDEMPOTENCY_KEY = "idempotency_key";

  @Provides
  IdempotentRpcContextFactory<String> provideIdempotencyRpcContextFactory(
      @Named(IDEMPOTENCY_KEY) Supplier<String> idempotencyKeySupplier) {
    return () -> {
      String idempotencyKey = idempotencyKeySupplier.get();
      return IdempotentRpcContext.builder().idempotencyKey(idempotencyKey).build();
    };
  }

  @Provides
  @Named(IDEMPOTENCY_KEY)
  Supplier<String> provideIdempotencyKeySupplier() {
    // For now, idempotency key is just a UUID
    return () -> UUID.randomUUID().toString();
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
