package com.serverless;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.junitextensions.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class PostCardRequestTest {
  @Test
  void testConstructor(@Mock PostCard mockPostCard, @Mock Payment mockPayment) {
    PostCardRequest postCardRequest = new PostCardRequest(mockPayment, mockPostCard);
    assertAll("Post card request constructor",
        () -> assertEquals(mockPayment, postCardRequest.getPaymentInfo()),
        () -> assertEquals(mockPostCard, postCardRequest.getPostCard())
    );
  }
}