package com.serverless;

public class Job {
  private final String id;
  private final String url;

  public Job(String id, String url) {
    this.id = id;
    this.url = url;
  }

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }
}
