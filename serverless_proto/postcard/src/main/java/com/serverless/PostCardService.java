package com.serverless;

import com.google.inject.Inject;
import com.serverless.exceptions.PostcardCreationException;
import org.apache.log4j.Logger;

public class PostCardService {
  private static final Logger LOG = Logger.getLogger(PostCardService.class);
  private PostCardProvider provider;

  @Inject
  public PostCardService(PostCardProvider provider) {
    this.provider = provider;
  }

  Order submit(Order order) throws PostcardCreationException {
    LOG.info("Submitting postcard for order: " + order.getOrderId());
    return provider.submit(order);
  }
}
