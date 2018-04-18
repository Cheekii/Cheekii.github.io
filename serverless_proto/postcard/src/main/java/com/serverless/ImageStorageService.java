package com.serverless;

import com.google.inject.Inject;
import java.net.URL;
import org.apache.log4j.Logger;

public class ImageStorageService {
  private final StorageProvider provider;
  private static final Logger LOG = Logger.getLogger(ImageStorageService.class);

  @Inject
  public ImageStorageService(StorageProvider provider) {
    this.provider = provider;
  }

  Order upload(Order order, String bucketName) {
    LOG.info("Uploading image for postcard order order: " + order.getOrderId());
    return provider.upload(order, bucketName);
  }
}
