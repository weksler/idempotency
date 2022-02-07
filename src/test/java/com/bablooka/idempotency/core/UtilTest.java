package com.bablooka.idempotency.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.time.Clock;
import java.time.Instant;
import javax.inject.Singleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

  static final long TIMESTAMPS_SECONDS = 1642192717;

  @Module
  class UtilTestModule {
    @Provides
    Clock provideClock() {
      return clock;
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

  @Singleton
  @Component(modules = {UtilTestModule.class})
  public interface UtilTestRoot {
    Util util();
  }

  @Mock Clock clock;
  Util util;

  @Before
  public void setUp() {
    util =
        DaggerUtilTest_UtilTestRoot.builder().utilTestModule(new UtilTestModule()).build().util();
  }

  @Test
  public void testNow() {
    when(clock.instant()).thenReturn(Instant.ofEpochSecond(TIMESTAMPS_SECONDS));
    assertEquals(util.now().getEpochSecond(), TIMESTAMPS_SECONDS);
  }

  @Test
  public void testTimestampFromInstant() {
    Instant instant = Instant.parse("2019-10-01T08:25:24.08Z");
    Timestamp timestamp = util.timestampFromInstant(instant);
    assertEquals(1_569_918_324, timestamp.getSeconds());
    assertEquals(80_000_000, timestamp.getNanos());
  }
}
