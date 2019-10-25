package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class GEListResponse extends Response {
  private String title;
  private String[] list;

  public GEListResponse(Request request, String title, String[] list) {
    super(request);
    this.title = title;
    this.list = list;
  }

  public String getTitle() {
    return title;
  }

  public String[] getList() {
    return list;
  }
}
