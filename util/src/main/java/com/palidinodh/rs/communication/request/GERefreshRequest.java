package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GERefreshRequest extends Request {
  private int userId;
  private String username;
  private int gameMode;
  private long time;

  public GERefreshRequest(ServerSession session, int key, int userId, String username, int gameMode,
      long time) {
    super(session, key);
    this.userId = userId;
    this.username = username;
    this.time = time;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public int getGameMode() {
    return gameMode;
  }

  public long getTime() {
    return time;
  }
}
