package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class FriendStatusSingleRequest extends Request {
  private String username;
  private String usernameAffected;

  public FriendStatusSingleRequest(ServerSession session, int key, String username,
      String usernameAffected) {
    super(session, key);
    this.username = username.toLowerCase();
    this.usernameAffected = usernameAffected.toLowerCase();
  }

  public String getUsername() {
    return username;
  }

  public String getUsernameAffected() {
    return usernameAffected;
  }
}
