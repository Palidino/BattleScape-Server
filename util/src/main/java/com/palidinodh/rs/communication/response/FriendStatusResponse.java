package com.palidinodh.rs.communication.response;

import java.util.List;
import com.palidinodh.rs.adaptive.RsFriend;
import com.palidinodh.rs.communication.request.Request;

public class FriendStatusResponse extends Response {
  private RsFriend onlineFriend;
  private List<RsFriend> friends;
  private List<String> ignores;
  private int privateChatStatus;

  public FriendStatusResponse(Request request, RsFriend friend, List<RsFriend> friends,
      List<String> ignores, int privateChatStatus) {
    super(request);
    onlineFriend = friend;
    this.friends = friends;
    this.ignores = ignores;
    this.privateChatStatus = privateChatStatus;
  }

  public RsFriend getOnlineFriend() {
    return onlineFriend;
  }

  public List<RsFriend> getFriends() {
    return friends;
  }

  public List<String> getIgnores() {
    return ignores;
  }

  public int getPrivateChatStatus() {
    return privateChatStatus;
  }
}
