package com.palidinodh.rs.adaptive;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RsClanActiveUser implements Comparable<RsClanActiveUser> {
  private String username;
  @Setter
  private int worldId;
  @Setter
  private int rights;
  @Setter
  private RsClanRank rank = RsClanRank.NOT_IN_CLAN;

  public RsClanActiveUser(String username) {
    this(username, 0, RsClanRank.NOT_IN_CLAN);
  }

  public RsClanActiveUser(String username, int worldId, RsClanRank rank) {
    this.username = username;
    this.worldId = worldId;
    this.rank = rank;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof RsClanActiveUser
        && ((RsClanActiveUser) o).getUsername().equalsIgnoreCase(username)
        || o instanceof String && ((String) o).equalsIgnoreCase(username);
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

  @Override
  public int compareTo(RsClanActiveUser user) {
    int compareToValue = 0;
    if (user != null) {
      compareToValue = user.getRank().ordinal();
    }
    return compareToValue - rank.ordinal();
  }
}
