package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class KickClanUserRequest extends Request {
  private String username;
  private String clanUsername;
  private String kickUsername;

  public KickClanUserRequest(ServerSession session, int key, String username, String clanUsername,
      String kickUsername) {
    super(session, key);
    this.username = username.toLowerCase();
    this.clanUsername = clanUsername.toLowerCase();
    this.kickUsername = kickUsername.toLowerCase();
  }

  public String getUsername() {
    return username;
  }

  public String getClanUsername() {
    return clanUsername;
  }

  public String getKickUsername() {
    return kickUsername;
  }
}
