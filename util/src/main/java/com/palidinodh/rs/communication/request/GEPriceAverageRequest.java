package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.adaptive.RsGameMode;
import com.palidinodh.rs.communication.ServerSession;

public class GEPriceAverageRequest extends Request {
  private int userId;
  private int gameMode;
  private int itemId;

  public GEPriceAverageRequest(ServerSession session, int key, int userId, int gameMode,
      int itemId) {
    super(session, key);
    this.userId = userId;
    this.gameMode = gameMode;
    this.itemId = itemId;
  }

  public int getUserId() {
    return userId;
  }

  public int getItemId() {
    return itemId;
  }

  public int getGameMode() {
    return gameMode;
  }

  public boolean isGameModeNormal() {
    return gameMode == RsGameMode.NORMAL.ordinal();
  }

  public boolean isGameModeHard() {
    return gameMode == RsGameMode.HARD.ordinal();
  }
}
