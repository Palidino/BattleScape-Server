package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class LeaveClanRequest extends Request {
  private String username;
  private String clanUsername;

  public LeaveClanRequest(ServerSession session, int key, String username, String clanUsername) {
    super(session, key);
    this.username = username.toLowerCase();
    this.clanUsername = clanUsername.toLowerCase();
  }

  public String getUsername() {
    return username;
  }

  public String getClanUsername() {
    return clanUsername;
  }
}
