package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.CoreModule;
import dagger.Component;

@Component(modules = {CoreModule.class, IdempotencyExampleModule.class})
public interface IdempotencyExampleRoot {
  IdempotencyExample idempotencyExample();
}
