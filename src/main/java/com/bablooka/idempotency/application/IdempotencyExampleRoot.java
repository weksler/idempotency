package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.IdempotencyHandler;
import dagger.Component;

@Component(modules = {IdempotencyExampleModule.class})
interface IdempotencyExampleRoot {
  IdempotencyHandler<String> idempotencyHandler();
}
