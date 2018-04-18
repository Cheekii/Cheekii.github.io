package com.serverless;

import com.google.inject.AbstractModule;

public class PostCardModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PostCardProvider.class).to(LobPostCardProvider.class);
  }
}
