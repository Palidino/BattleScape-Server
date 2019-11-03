package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class ClanSettingRequest extends Request {
  private String username;
  private int type;
  private String string;
  private int value;

  public ClanSettingRequest(ServerSession session, int key, String username, int type,
      String string) {
    super(session, key);
    this.username = username.toLowerCase();
    this.type = type;
    this.string = string;
  }

  public ClanSettingRequest(ServerSession session, int key, String username, int type, int value) {
    super(session, key);
    this.username = username.toLowerCase();
    this.type = type;
    this.value = value;
  }

  public String getUsername() {
    return username;
  }

  public int getType() {
    return type;
  }

  public String readString() {
    return string;
  }

  public int getValue() {
    return value;
  }
}
