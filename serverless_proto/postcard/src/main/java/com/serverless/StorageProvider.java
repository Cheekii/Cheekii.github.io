package com.serverless;

public interface StorageProvider {

  Order upload(Order order, String bucketName);
}
