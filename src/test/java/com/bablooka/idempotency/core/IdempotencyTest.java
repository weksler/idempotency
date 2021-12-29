package com.bablooka.idempotency.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IdempotencyTest {

  Idempotency idempotency;

  @Before
  public void setUp() throws Exception {
    idempotency = new Idempotency();
  }

  @Test
  public void testGetName() {
    assertEquals("Idempotency!", idempotency.getName());
  }
}
