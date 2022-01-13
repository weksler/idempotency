package com.bablooka.idempotency.core;

import dagger.Component;

@Component(modules = {CoreModule.class})
public interface CoreRoot {
  IdempotencyHandler<String> idempotencyHandler();
}
