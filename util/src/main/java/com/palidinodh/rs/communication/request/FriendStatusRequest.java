package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class FriendStatusRequest extends Request {
  private String username;
  private int privateChatStatus;

  public FriendStatusRequest(ServerSession session, int key, String username,
      int privateChatStatus) {
    super(session, key);
    this.username = username.toLowerCase();
    this.privateChatStatus = privateChatStatus;
  }

  public String getUsername() {
    return username;
  }

  public int getPrivateChatStatus() {
    return privateChatStatus;
  }
}
