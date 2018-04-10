package com.serverless;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.junitextensions.MockitoExtension;
import com.lob.model.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class PostCardRequestTest {
  @Test
  void testConstructor(@Mock Address toAddress, @Mock Address fromAddress) {
    PostCardRequest postCardRequest = new PostCardRequest(
        "name",
        "message",
        "stripeToken",
        toAddress,
        fromAddress,
        "base64",
        "code"
    );
    assertAll("Post card request constructor",
        () -> assertEquals("name", postCardRequest.getName()),
        () -> assertEquals("message", postCardRequest.getMessage()),
        () -> assertEquals("stripeToken", postCardRequest.getStripe()),
        () -> assertEquals(toAddress, postCardRequest.getToAddress()),
        () -> assertEquals(fromAddress, postCardRequest.getFromAddress()),
        () -> assertEquals("base64", postCardRequest.getBase64image()),
        () -> assertEquals("code", postCardRequest.getCode())
    );
  }
}