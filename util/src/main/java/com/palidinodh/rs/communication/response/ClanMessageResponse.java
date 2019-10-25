package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class ClanMessageResponse extends Response {
  private String clanUsername;
  private String username;
  private int icon;
  private String message;

  public ClanMessageResponse(Request request, String clanUsername, String username, int icon,
      String message) {
    super(request);
    this.clanUsername = clanUsername.toLowerCase();
    this.username = username;
    this.icon = icon;
    this.message = message;
  }

  public String getClanUsername() {
    return clanUsername;
  }

  public String getUsername() {
    return username;
  }

  public int getIcon() {
    return icon;
  }

  public String getMessage() {
    return message;
  }
}
