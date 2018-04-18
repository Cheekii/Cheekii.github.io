package com.serverless;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class S3StorageProvider implements StorageProvider {

  @Override
  public Order upload(Order order, String bucketName) {
    byte[] decodedImage = Base64.getDecoder()
        .decode(order.getPostCard().getBase64image().split(",")[1]);
    InputStream inputStream = new ByteArrayInputStream(decodedImage);
    AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(decodedImage.length);
    String key = order.getOrderId().toString() + ".jpg";
    PutObjectRequest putObjectRequest = new PutObjectRequest(
        bucketName,
        key,
        inputStream, metadata);
    s3.putObject(putObjectRequest);
    return getOrderUpdatedWithUrl(order, s3.getUrl(bucketName, key));
  }

  private Order getOrderUpdatedWithUrl(Order order, URL url) {
    return new Order(
        new PostCard(
            order.getPostCard().getName(),
            order.getPostCard().getMessage(),
            order.getPostCard().getToAddress(),
            order.getPostCard().getFromAddress(),
            order.getPostCard().getBase64image(),
            url,
            order.getPostCard().getCode()),
        order.getPayment(),
        order.getOrderId(),
        order.getCharge(),
        null);
  }
}
