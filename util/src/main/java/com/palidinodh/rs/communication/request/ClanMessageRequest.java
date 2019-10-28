package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class ClanMessageRequest extends Request {
  private String username;
  private int icon;
  private String clanUsername;
  private String message;

  public ClanMessageRequest(ServerSession session, int key, String username, int icon,
      String clanUsername, String message) {
    super(session, key);
    this.username = username.toLowerCase();
    this.icon = icon;
    this.clanUsername = clanUsername.toLowerCase();
    this.message = message;
  }

  public String getUsername() {
    return username;
  }

  public int getIcon() {
    return icon;
  }

  public String getClanUsername() {
    return clanUsername;
  }

  public String getMessage() {
    return message;
  }
}
