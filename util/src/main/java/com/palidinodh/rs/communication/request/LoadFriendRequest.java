package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.adaptive.RsFriend;
import com.palidinodh.rs.communication.ServerSession;

public class LoadFriendRequest extends Request {
  private String username;
  private RsFriend friend;

  public LoadFriendRequest(ServerSession session, int key, String username, RsFriend friend) {
    super(session, key);
    this.username = username.toLowerCase();
    this.friend = friend;
  }

  public String getUsername() {
    return username;
  }

  public RsFriend getFriend() {
    return friend;
  }
}
