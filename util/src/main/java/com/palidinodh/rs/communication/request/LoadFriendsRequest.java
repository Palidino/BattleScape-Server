package com.palidinodh.rs.communication.request;

import java.util.List;
import com.palidinodh.rs.adaptive.RsFriend;
import com.palidinodh.rs.communication.ServerSession;

public class LoadFriendsRequest extends Request {
  private String username;
  private int icon;
  private int icon2;
  private List<RsFriend> friends;
  private List<String> ignores;
  private int privateChatStatus;

  public LoadFriendsRequest(ServerSession session, int key, String username, int icon, int icon2,
      List<RsFriend> friends, List<String> ignores, int privateChatStatus) {
    super(session, key);
    this.username = username.toLowerCase();
    this.icon = icon;
    this.icon2 = icon2;
    this.friends = friends;
    this.ignores = ignores;
    this.privateChatStatus = privateChatStatus;
  }

  public String getUsername() {
    return username;
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

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public int getIcon2() {
    return icon2;
  }

  public void setIcon2(int icon2) {
    this.icon2 = icon2;
  }
}
