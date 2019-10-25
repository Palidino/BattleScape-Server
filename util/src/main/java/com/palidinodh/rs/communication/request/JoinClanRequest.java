package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class JoinClanRequest extends Request {
  private String username;
  private String joinUsername;

  public JoinClanRequest(ServerSession session, int key, String username, String joinUsername) {
    super(session, key);
    this.username = username.toLowerCase();
    this.joinUsername = joinUsername.toLowerCase();
  }

  public String getUsername() {
    return username;
  }

  public String getJoinUsername() {
    return joinUsername;
  }
}
