package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class LoadClanRequest extends Request {
  private String username;

  public LoadClanRequest(ServerSession session, int key, String username) {
    super(session, key);
    this.username = username.toLowerCase();
  }

  public String getUsername() {
    return username;
  }
}
