package com.bablooka.idempotency.application;

import com.bablooka.idempotency.core.CoreModule;
import com.bablooka.idempotency.core.IdempotencyHandler;
import dagger.Component;

// TODO(wekselr): Make all core injection come from the CoreModule.class.
@Component(modules = {IdempotencyExampleModule.class, CoreModule.class})
interface IdempotencyExampleRoot {
  IdempotencyHandler<String> idempotencyHandler();
}
