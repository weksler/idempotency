package com.bablooka.idempotency.core;

import static org.junit.runners.Suite.SuiteClasses;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@SuiteClasses({
  UtilTest.class,
  IdempotencyHandlerTest.class,
})
public class CoreTestSuite {}
