package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class Response {
  private Request request;

  public Response(Request request) {
    this.request = request;
  }

  public Request getRequest() {
    return request;
  }

  public int getKey() {
    return request.getKey();
  }
}
