package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GEHistoryRequest extends Request {
  private int userId;
  private int type;

  public GEHistoryRequest(ServerSession session, int key, int userId, int type) {
    super(session, key);
    this.userId = userId;
    this.type = type;
  }

  public int getUserId() {
    return userId;
  }

  public int getType() {
    return type;
  }
}
