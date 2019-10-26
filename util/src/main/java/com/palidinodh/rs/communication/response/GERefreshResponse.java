package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.adaptive.GrandExchangeItem;
import com.palidinodh.rs.communication.request.Request;

public class GERefreshResponse extends Response {
  private int userId;
  private int gameMode;
  private GrandExchangeItem[] items;

  public GERefreshResponse(Request request, GrandExchangeItem[] items) {
    super(request);
    this.items = items;
  }

  public GERefreshResponse(Request request, int userId, int gameMode, GrandExchangeItem[] items) {
    super(request);
    this.userId = userId;
    this.gameMode = gameMode;
    this.items = items;
  }

  public int getUserId() {
    return userId;
  }

  public int getGameMode() {
    return gameMode;
  }

  public GrandExchangeItem[] getItems() {
    return items;
  }
}
