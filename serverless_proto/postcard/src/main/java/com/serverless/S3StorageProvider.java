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
  public URL upload(Order order, String bucketName) {
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
    return s3.getUrl(bucketName, key);
  }
}
