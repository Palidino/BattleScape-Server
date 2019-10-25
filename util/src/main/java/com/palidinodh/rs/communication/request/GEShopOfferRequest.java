package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.adaptive.GrandExchangeItem;
import com.palidinodh.rs.adaptive.RsGameMode;
import com.palidinodh.rs.communication.ServerSession;

public class GEShopOfferRequest extends Request {
  private int userId;
  private String username;
  private String userIP;
  private int gameMode;
  private int shopUserId;
  private int slot;
  private int id;
  private int amount;
  private int price;
  private int geState;

  public GEShopOfferRequest(ServerSession session, int key, int userId, String username,
      String userIP, int gameMode, int shopUserId, int slot, int id, int amount, int price,
      int geState) {
    super(session, key);
    this.userId = userId;
    this.username = username;
    this.userIP = userIP;
    this.gameMode = gameMode;
    this.shopUserId = shopUserId;
    this.slot = slot;
    this.id = id;
    this.amount = amount;
    this.price = price;
    this.geState = geState;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getUserIP() {
    return userIP;
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

  public int getShopUserId() {
    return shopUserId;
  }

  public int getSlot() {
    return slot;
  }

  public int getId() {
    return id;
  }

  public int getAmount() {
    return amount;
  }

  public int getPrice() {
    return price;
  }

  public int getGEState() {
    return geState;
  }

  public boolean getStateBuying() {
    return geState == GrandExchangeItem.STATE_BUYING;
  }

  public boolean getStateSelling() {
    return geState == GrandExchangeItem.STATE_SELLING;
  }
}
