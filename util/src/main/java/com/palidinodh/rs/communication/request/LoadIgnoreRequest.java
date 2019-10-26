package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class LoadIgnoreRequest extends Request {
  private String username;
  private String ignoreUsername;

  public LoadIgnoreRequest(ServerSession session, int key, String username, String ignoreUsername) {
    super(session, key);
    this.username = username.toLowerCase();
    this.ignoreUsername = ignoreUsername.toLowerCase();
  }

  public String getUsername() {
    return username;
  }

  public String getIgnoreUsername() {
    return ignoreUsername;
  }
}
