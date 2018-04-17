package com.serverless;

import com.google.inject.AbstractModule;

public class ImageStorageModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(StorageProvider.class).to(S3StorageProvider.class);
  }
}
