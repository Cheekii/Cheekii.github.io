package com.serverless;

import com.google.inject.AbstractModule;

public class EnvironmentModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Environment.class).to(SystemEnvironment.class);
  }
}
