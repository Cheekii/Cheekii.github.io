package com.serverless;

import com.serverless.exceptions.PostcardCreationException;

public interface PostCardProvider {

  Order submit(Order order) throws PostcardCreationException;
}
