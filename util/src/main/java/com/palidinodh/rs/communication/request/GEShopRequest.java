package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GEShopRequest extends Request {
  private String username;
  private int userId;

  public GEShopRequest(ServerSession session, int key, String username) {
    super(session, key);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
