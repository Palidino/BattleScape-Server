package com.palidinodh.rs.adaptive;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RsPlayer {
  private int id;
  private String username;
  private String password;
  private String ip;
  private int worldId;
  @Setter
  private int rights;
  @Setter
  private int icon;
  @Setter
  private int icon2;
  @Setter
  private int privateChatStatus;
  private List<RsFriend> friends = new ArrayList<>();
  private List<String> ignores = new ArrayList<>();

  public RsPlayer(int id, String username, String password, String ip, int worldId) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.ip = ip;
    this.worldId = worldId;
  }

  public void loadFriends(List<RsFriend> friends, List<String> ignores, int privateChatStatus) {
    this.friends = friends;
    this.ignores = ignores;
    this.privateChatStatus = privateChatStatus;
  }

  public void loadFriend(RsFriend friend) {
    if (getFriends().contains(friend)) {
      getFriends().remove(friend);
    } else {
      getFriends().add(friend);
    }
  }

  public void loadFriendClanRank(RsFriend friend) {
    int indexOf = getFriends().indexOf(friend);
    if (indexOf != -1) {
      RsFriend orig = getFriends().get(indexOf);
      orig.setClanRank(friend.getClanRank());
    }
  }

  public void loadIgnore(String username) {
    if (getIgnores().contains(username.toLowerCase())) {
      getIgnores().remove(username.toLowerCase());
    } else {
      getIgnores().add(username.toLowerCase());
    }
  }
}
