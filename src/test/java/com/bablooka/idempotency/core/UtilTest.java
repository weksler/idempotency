package com.bablooka.idempotency.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.time.Clock;
import java.time.Instant;
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
  }

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
  public void testAssertOk() {
    assertTrue(1 == 1);
  }

  @Test
  public void testSomethingWithClock() {
    when(clock.instant()).thenReturn(Instant.ofEpochSecond(TIMESTAMPS_SECONDS));
    assertEquals(util.now().getEpochSecond(), TIMESTAMPS_SECONDS);
  }
}
