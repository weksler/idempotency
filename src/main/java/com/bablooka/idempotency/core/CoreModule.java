package com.bablooka.idempotency.core;

import com.google.protobuf.util.JsonFormat;
import dagger.Module;
import dagger.Provides;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class CoreModule {

  public static final String IDEMPOTENCY_KEY = "idempotency_key";

  @Singleton
  @Provides
  @Named(IDEMPOTENCY_KEY)
  Supplier<String> provideIdempotencyKeySupplier() {
    // For now, idempotency key is just a UUID
    return () -> UUID.randomUUID().toString();
  }

  @Singleton
  @Provides
  Clock provideClock() {
    // This is the clock used by Java's default Instant implementation.
    return Clock.systemUTC();
  }

  @Singleton
  @Provides
  Duration provideDefaultIdempotencyDureation() {
    // TODO(weksler): Allow this to be configured
    // Default idempotency least duration is 1 minute.
    return Duration.ofMinutes(1);
  }

  @Provides
  @Singleton
  JsonFormat.Printer getJsonFormatPrinter() {
    return JsonFormat.printer().includingDefaultValueFields();
  }

  @Provides
  @Singleton
  JsonFormat.Parser getJsonFormatParser() {
    return JsonFormat.parser();
  }
}
