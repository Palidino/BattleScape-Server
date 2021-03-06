package com.palidinodh.rs.communication;

public class Opcodes {
  public static final String PACKET_END_VERIFICATION = "@<W'G(&u2e[w9,fP";
  public static final int PING_DELAY = 10000;
  public static final int VERIFY = 1;
  public static final int PING = 2;
  public static final int PLAYER_LOGIN = 3;
  public static final int PLAYER_LOGOUT = 4;
  public static final int LOAD_FRIENDS = 5;
  public static final int LOAD_FRIEND = 6;
  public static final int FRIEND_STATUS = 7;
  public static final int FRIEND_STATUS_SINGLE = 8;
  public static final int LOAD_IGNORE = 9;
  public static final int PRIVATE_MESSAGE = 10;
  public static final int LOAD_CLAN = 11;
  public static final int CLAN_SETTING = 12;
  public static final int JOIN_CLAN = 13;
  public static final int CLAN_MESSAGE = 14;
  public static final int CLAN_UPDATE = 15;
  public static final int CLAN_RANK = 16;
  public static final int KICK_CLAN_USER = 17;
  public static final int LEAVE_CLAN = 18;
  public static final int SHUTDOWN = 19;
  public static final int SQL_UPDATE = 20;
  public static final int GE_REFRESH = 21;
  public static final int GE_BUY_OFFER = 22;
  public static final int GE_SELL_OFFER = 23;
  public static final int LOG = 24;
  public static final int GE_ABORT_OFFER = 25;
  public static final int GE_COLLECT_OFFER = 26;
  public static final int GE_PRICE_AVERAGE = 27;
  public static final int GE_HISTORY = 28;
  public static final int GE_SHOP = 29;
  public static final int GE_SHOP_OFFER = 30;
  public static final int GE_LIST = 31;
  public static final int WORLD_SHUTDOWN = 32;
  public static final int WORLDS_SHUTDOWN = 33;
  public static final int PLAYER_COUNT = 34;
}
