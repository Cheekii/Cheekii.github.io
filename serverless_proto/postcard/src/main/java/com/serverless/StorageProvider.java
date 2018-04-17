package com.serverless;

import java.net.URL;

public interface StorageProvider {

  URL upload(Order order, String bucketName);
}
