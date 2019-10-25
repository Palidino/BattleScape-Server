package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GEListRequest extends Request {
  private int userId;
  private int type;
  private int searchId;
  private String searchString;

  public GEListRequest(ServerSession session, int key, int userId, int type, int searchId,
      String searchString) {
    super(session, key);
    this.userId = userId;
    this.type = type;
    this.searchId = searchId;
    this.searchString = searchString;
  }

  public int getUserId() {
    return userId;
  }

  public int getType() {
    return type;
  }

  public int getSearchId() {
    return searchId;
  }

  public String getSearchString() {
    return searchString;
  }
}
