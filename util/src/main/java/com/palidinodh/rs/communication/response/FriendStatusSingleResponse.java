package com.palidinodh.rs.communication.response;

import java.util.List;
import com.palidinodh.rs.adaptive.RsFriend;
import com.palidinodh.rs.communication.request.Request;

public class FriendStatusSingleResponse extends Response {
  private RsFriend onlineFriend;
  private List<RsFriend> friends;
  private List<String> ignores;
  private int privateChatStatus;
  private String usernameAffected;

  public FriendStatusSingleResponse(Request request, RsFriend friend, List<RsFriend> friends,
      List<String> ignores, int privateChatStatus, String usernameAffected) {
    super(request);
    onlineFriend = friend;
    this.friends = friends;
    this.ignores = ignores;
    this.privateChatStatus = privateChatStatus;
    this.usernameAffected = usernameAffected;
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

  public String getUsernameAffected() {
    return usernameAffected;
  }
}
