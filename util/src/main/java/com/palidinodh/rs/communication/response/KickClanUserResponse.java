package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class KickClanUserResponse extends Response {
  private String username;
  private String clanUsername;

  public KickClanUserResponse(Request request, String username, String clanUsername) {
    super(request);
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
