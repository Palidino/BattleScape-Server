package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class PlayerLoginResponse extends Response {
  private int userId;
  private String username;
  private int rights;
  private byte[] charFile;
  private byte[] sqlFile;

  public PlayerLoginResponse(Request request, int userId, String username, int rights,
      byte[] charFile, byte[] sqlFile) {
    super(request);
    this.userId = userId;
    this.username = username;
    this.rights = rights;
    this.charFile = charFile;
    this.sqlFile = sqlFile;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public int getRights() {
    return rights;
  }

  public byte[] getCharFile() {
    return charFile;
  }

  public void setCharFile(byte[] charFile) {
    this.charFile = charFile;
  }

  public byte[] getSqlFile() {
    return sqlFile;
  }

  public void setSqlFile(byte[] sqlFile) {
    this.sqlFile = sqlFile;
  }
}
