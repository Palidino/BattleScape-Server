package com.palidinodh.rs.adaptive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.palidinodh.util.PTime;

public class Clan implements Serializable {
  public static final Clan[] GLOBAL_CLANS = { new Clan("home", -2), new Clan("pvp", -3) };
  public static final Clan HOME_CLAN = GLOBAL_CLANS[0];
  private static final long serialVersionUID = 8112016L;
  public static final int TIME_BETWEEN_UPDATES = 10000;
  public static final int MAX_SIZE = 200, MAX_DISPLAY_SIZE = 100;
  public static final String DISABLED_NAME = "Chat disabled";
  public static final int NAME = 0, DISABLE = 1, ENTER_LIMIT = 2, TALK_LIMIT = 3, KICK_LIMIT = 4;
  public static final String LEAVE_CLAN = new String(new char[] { 'y', (char) 2, 'M' });

  private transient long lastUpdate;
  private String name;
  private String owner;
  private int ownerId = -1;
  private List<RsFriend> ownerFriends;
  private List<String> ownerIgnores;
  private transient List<RsClanActiveUser> activeUsers;
  private transient List<RsClanKickedUser> kickedUsers;
  private boolean disabled;
  private RsClanRank enterLimit = RsClanRank.ANY_FRIENDS;
  private RsClanRank talkLimit = RsClanRank.ANYONE;
  private RsClanRank kickLimit = RsClanRank.ONLY_ME;

  public Clan() {
    lastUpdate = PTime.currentTimeMillis();
    name = "";
    activeUsers = new ArrayList<>();
    kickedUsers = new ArrayList<>();
  }

  public Clan(String name, int userId) {
    this(name, name, userId, new ArrayList<RsFriend>(), new ArrayList<String>());
    enterLimit = RsClanRank.ANYONE;
  }

  public Clan(String name, String username, int userId, List<RsFriend> friends,
      List<String> ignores) {
    this();
    this.name = name;
    owner = username.toLowerCase();
    ownerId = userId;
    ownerFriends = friends;
    ownerIgnores = ignores;
  }

  public boolean canUpdate() {
    if (PTime.currentTimeMillis() - lastUpdate < TIME_BETWEEN_UPDATES) {
      return false;
    }
    lastUpdate = PTime.currentTimeMillis();
    update();
    return true;
  }

  public void update() {
    checkKickedUsers();
    for (Iterator<RsClanActiveUser> it = activeUsers.iterator(); it.hasNext();) {
      RsClanActiveUser user = it.next();
      if (!canStay(user.getUsername(), user.getRights())) {
        it.remove();
      }
    }
  }

  public void updateFriends(List<RsFriend> friends, List<String> ignores) {
    if (isGlobal(this)) {
      sortRanks();
      return;
    }
    ownerFriends = friends;
    ownerIgnores = ignores;
    sortRanks();
  }

  public void sortRanks() {
    for (RsClanActiveUser user : activeUsers) {
      sortRank(user);
    }
    Collections.sort(activeUsers);
  }

  public void sortRank(RsClanActiveUser user) {
    RsFriend friend = null;
    int indexOf = ownerFriends.indexOf(new RsFriend(user.getUsername()));
    if (indexOf != -1) {
      friend = ownerFriends.get(indexOf);
    }
    if (friend != null) {
      RsClanRank rank = friend.getClanRank();
      if (rank == RsClanRank.NOT_IN_CLAN) {
        rank = RsClanRank.FRIEND;
      }
      user.setRank(rank);
    } else {
      user.setRank(RsClanRank.NOT_IN_CLAN);
    }
    if (user.getRights() > 0) {
      user.setRank(RsClanRank.STAFF);
    }
    if (user.getUsername().equalsIgnoreCase(owner)) {
      user.setRank(RsClanRank.OWNER);
    }
  }

  public boolean canJoin(String username, int rights) {
    checkKickedUsers();
    if (disabled) {
      return false;
    }
    boolean isOwner = username.equalsIgnoreCase(owner);
    if (activeUsers.size() >= MAX_SIZE) {
      return false;
    }
    if (rights > 0) {
      return true;
    }
    if (enterLimit == RsClanRank.ONLY_ME) {
      return isOwner;
    }
    for (RsClanActiveUser user : activeUsers) {
      if (user.getUsername().equalsIgnoreCase(username)) {
        return false;
      }
    }
    for (RsClanKickedUser user : kickedUsers) {
      if (!isOwner && user.getUsername().equalsIgnoreCase(username)) {
        return false;
      }
    }
    if (ownerIgnores.contains(username)) {
      return false;
    }
    if (enterLimit != RsClanRank.ANYONE && !isOwner) {
      RsFriend friend = null;
      int indexOf = ownerFriends.indexOf(new RsFriend(username));
      if (indexOf == -1) {
        return false;
      }
      friend = ownerFriends.get(indexOf);
      RsClanRank rank = friend.getClanRank();
      if ((enterLimit == RsClanRank.RECRUIT || enterLimit == RsClanRank.CORPORAL
          || enterLimit == RsClanRank.SERGEANT || enterLimit == RsClanRank.LIEUTENANT
          || enterLimit == RsClanRank.CAPTAIN || enterLimit == RsClanRank.GENERAL)
          && rank.ordinal() < enterLimit.ordinal()) {
        return false;
      }
    }
    return true;
  }

  public boolean canStay(String username, int rights) {
    if (disabled) {
      return false;
    }
    if (rights > 0) {
      return true;
    }
    boolean isOwner = username.equalsIgnoreCase(owner);
    if (enterLimit == RsClanRank.ONLY_ME) {
      return isOwner;
    }
    for (RsClanKickedUser user : kickedUsers) {
      if (!isOwner && user.getUsername().equalsIgnoreCase(username)) {
        return false;
      }
    }
    if (ownerIgnores.contains(username)) {
      return false;
    }
    if (enterLimit != RsClanRank.ANYONE && !isOwner) {
      RsFriend friend = null;
      int indexOf = ownerFriends.indexOf(new RsFriend(username));
      if (indexOf == -1) {
        return false;
      }
      friend = ownerFriends.get(indexOf);
      RsClanRank rank = friend.getClanRank();
      if ((enterLimit == RsClanRank.RECRUIT || enterLimit == RsClanRank.CORPORAL
          || enterLimit == RsClanRank.SERGEANT || enterLimit == RsClanRank.LIEUTENANT
          || enterLimit == RsClanRank.CAPTAIN || enterLimit == RsClanRank.GENERAL)
          && rank.ordinal() < enterLimit.ordinal()) {
        return false;
      }
    }
    return true;
  }

  public boolean canMessage(String username, int rights) {
    if (rights > 0) {
      return true;
    }
    boolean isOwner = username.equalsIgnoreCase(owner);
    if (talkLimit == RsClanRank.ONLY_ME) {
      return isOwner;
    }
    if (talkLimit != RsClanRank.ANYONE && !isOwner) {
      RsFriend friend = null;
      int indexOf = ownerFriends.indexOf(new RsFriend(username));
      if (indexOf == -1) {
        return false;
      }
      friend = ownerFriends.get(indexOf);
      RsClanRank rank = friend.getClanRank();
      if ((talkLimit == RsClanRank.RECRUIT || talkLimit == RsClanRank.CORPORAL
          || talkLimit == RsClanRank.SERGEANT || talkLimit == RsClanRank.LIEUTENANT
          || talkLimit == RsClanRank.CAPTAIN || talkLimit == RsClanRank.GENERAL)
          && rank.ordinal() < talkLimit.ordinal()) {
        return false;
      }
    }
    return true;
  }

  public boolean canKick(RsClanActiveUser kicker, RsClanActiveUser kicked) {
    for (RsClanKickedUser user : kickedUsers) {
      if (user.getUsername().equalsIgnoreCase(kicked.getUsername())) {
        return false;
      }
    }
    if (kicked.getRights() > 0) {
      return false;
    }
    if (kicker.getRights() > 0) {
      return true;
    }
    boolean isOwner = kicker.getUsername().equalsIgnoreCase(owner) && !isGlobal(this);
    if (kickLimit == RsClanRank.ONLY_ME || isOwner) {
      return isOwner;
    }
    RsFriend friend = null;
    int indexOf = ownerFriends.indexOf(new RsFriend(kicker.getUsername()));
    if (indexOf == -1) {
      return false;
    }
    friend = ownerFriends.get(indexOf);
    RsClanRank rank = friend.getClanRank();
    if ((kickLimit == RsClanRank.CORPORAL || kickLimit == RsClanRank.SERGEANT
        || kickLimit == RsClanRank.LIEUTENANT || kickLimit == RsClanRank.CAPTAIN
        || kickLimit == RsClanRank.GENERAL) && rank.ordinal() < kickLimit.ordinal()) {
      return false;
    }
    return kicker.getRank().ordinal() > kicked.getRank().ordinal();
  }

  public void join(String username, int worldId, int rights) {
    RsClanActiveUser user = new RsClanActiveUser(username);
    user.setWorldId(worldId);
    user.setRights(rights);
    activeUsers.add(user);
    sortRank(user);
    Collections.sort(activeUsers);
  }

  public void remove(RsClanActiveUser user) {
    activeUsers.remove(user);
    Collections.sort(activeUsers);
  }

  public void kick(RsClanActiveUser user) {
    remove(user);
    kickedUsers.add(new RsClanKickedUser(user.getUsername()));
  }

  public void checkKickedUsers() {
    if (activeUsers.isEmpty()) {
      kickedUsers.clear();
      return;
    }
    for (Iterator<RsClanKickedUser> it = kickedUsers.iterator(); it.hasNext();) {
      RsClanKickedUser user = it.next();
      if (!user.isValid()) {
        it.remove();
      }
    }
  }

  public String getDisplayName() {
    return name != null && name.length() != 0 ? name : owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (isGlobal(this)) {
      return;
    }
    this.name = name;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner.toLowerCase();
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  public boolean getDisabled() {
    return disabled;
  }

  public RsClanRank getEnterLimit() {
    return enterLimit;
  }

  public RsClanRank getTalkLimit() {
    return talkLimit;
  }

  public RsClanRank getKickLimit() {
    return kickLimit;
  }

  public void setDisabled(boolean disabled) {
    if (isGlobal(this)) {
      return;
    }
    this.disabled = disabled;
  }

  public void setEnterLimit(int enterLimit) {
    if (isGlobal(this)) {
      return;
    }
    this.enterLimit = RsClanRank.values()[enterLimit];
  }

  public void setTalkLimit(int talkLimit) {
    if (isGlobal(this)) {
      return;
    }
    this.talkLimit = RsClanRank.values()[talkLimit];
  }

  public void setKickLimit(int kickLimit) {
    if (isGlobal(this)) {
      return;
    }
    this.kickLimit = RsClanRank.values()[kickLimit];
  }

  public List<RsClanActiveUser> getActiveUsers() {
    return activeUsers;
  }

  public static boolean isGlobal(Clan clan) {
    for (Clan global : GLOBAL_CLANS) {
      if (global == clan) {
        return true;
      }
    }
    return false;
  }

  public static boolean isGlobal(String username) {
    for (Clan global : GLOBAL_CLANS) {
      if (username.equalsIgnoreCase(global.getOwner())) {
        return true;
      }
    }
    return false;
  }

  public static Clan getGlobal(String username) {
    for (Clan global : GLOBAL_CLANS) {
      if (username.equalsIgnoreCase(global.getOwner())) {
        return global;
      }
    }
    return null;
  }
}
