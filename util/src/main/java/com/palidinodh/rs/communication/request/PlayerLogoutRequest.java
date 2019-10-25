package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class PlayerLogoutRequest extends Request {
  private String username;
  private int userId;
  private byte[] charFile;

  public PlayerLogoutRequest(ServerSession session, int key, String username, int userId,
      byte[] charFile) {
    super(session, key);
    this.username = username.toLowerCase();
    this.userId = userId;
    this.charFile = charFile;
  }

  public PlayerLogoutRequest(ServerSession session, int key, String username, byte[] charFile) {
    super(session, key);
    this.username = username.toLowerCase();
    this.charFile = charFile;
  }

  public String getUsername() {
    return username;
  }

  public int getUserId() {
    return userId;
  }

  public byte[] getCharFile() {
    return charFile;
  }
}
