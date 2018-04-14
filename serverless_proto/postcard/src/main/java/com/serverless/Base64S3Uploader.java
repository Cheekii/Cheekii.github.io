package com.serverless;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import org.apache.log4j.Logger;

public class Base64S3Uploader {
  private AmazonS3 amazonS3;
  private String bucketName;

  private static final Logger LOG = Logger.getLogger(Base64S3Uploader.class);

  /**
   * Create a base64 uploader from a given bucket name.
   * @param bucketName the bucket name
   */
  public Base64S3Uploader(String bucketName) {
    this.amazonS3 = AmazonS3ClientBuilder.defaultClient();
    this.bucketName = bucketName;
  }

  /**
   * Create a base64 uploader from a given bucket name and S3 client.
   * @param bucketName the bucket name
   * @param client the client
   */
  public Base64S3Uploader(String bucketName, AmazonS3 client) {
    this.amazonS3 = client;
    this.bucketName = bucketName;
  }

  /**
   * Uploads a base64 file to s3, returning the URL.
   * @param base64 the base64 data
   * @param fileName the name of the file
   * @return
   */
  public URL upload(String base64, String fileName) {
    byte[] decodedImage = Base64.getDecoder().decode(base64.split(",")[1]);
    InputStream inputStream = new ByteArrayInputStream(decodedImage);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(decodedImage.length);
    String fileExt = ".jpg";
    String fullFileName = fileName + fileExt;
    PutObjectRequest putObjectRequest = new PutObjectRequest(
        bucketName,
        fullFileName,
        inputStream, metadata);
    PutObjectResult result = amazonS3.putObject(putObjectRequest);
    LOG.info("PutObjectResult:" + result);
    return amazonS3.getUrl(bucketName, fullFileName);
  }
}
