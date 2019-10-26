package com.palidinodh.rs.adaptive;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RsFriend {
  private String username;
  @Setter
  private RsClanRank clanRank = RsClanRank.NOT_IN_CLAN;
  @Setter
  private transient int worldId = -1;

  public RsFriend(String username) {
    this(username, -1, RsClanRank.NOT_IN_CLAN);
  }

  public RsFriend(String username, int worldId) {
    this(username, worldId, RsClanRank.NOT_IN_CLAN);
  }

  public RsFriend(RsFriend fromFriend) {
    this(fromFriend.getUsername(), fromFriend.getWorldId(), fromFriend.getClanRank());
  }

  public RsFriend(String username, int worldId, RsClanRank clanRank) {
    this.username = username;
    this.worldId = worldId;
    this.clanRank = clanRank;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof RsFriend && ((RsFriend) o).getUsername().equalsIgnoreCase(username)
        || o instanceof String && ((String) o).equalsIgnoreCase(username);
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

  public static boolean canRegister(String senderUsername, int senderPrivateChatStatus,
      String receiverUsername, int receiverPrivateChatStatus, List<RsFriend> senderFriends,
      List<RsFriend> receiverFriends, List<String> receiverIgnores) {
    senderUsername = senderUsername.toLowerCase();
    receiverUsername = receiverUsername.toLowerCase();
    if (senderFriends == null || receiverFriends == null || receiverIgnores == null) {
      return false;
    }
    if (receiverPrivateChatStatus == RsPrivateChat.OFF.ordinal()) {
      return false;
    }
    if (senderPrivateChatStatus == RsPrivateChat.FRIENDS.ordinal()
        && !senderFriends.contains(new RsFriend(receiverUsername))) {
      return false;
    }
    if (receiverPrivateChatStatus == RsPrivateChat.FRIENDS.ordinal()
        && !receiverFriends.contains(new RsFriend(senderUsername))) {
      return false;
    }
    if (receiverIgnores.contains(senderUsername)) {
      return false;
    }
    return true;
  }
}
