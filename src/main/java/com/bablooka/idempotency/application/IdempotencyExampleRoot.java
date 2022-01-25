package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.CoreModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {CoreModule.class, IdempotencyExampleModule.class})
public interface IdempotencyExampleRoot {
  IdempotencyExample idempotencyExample();
}
