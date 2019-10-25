package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GEBuyOfferRequest extends Request {
  private int userId;
  private String username;
  private String userIP;
  private int gameMode;
  private int slot;
  private int id;
  private String name;
  private int amount;
  private int price;

  public GEBuyOfferRequest(ServerSession session, int key, int userId, String username,
      String userIP, int gameMode, int slot, int id, String name, int amount, int price) {
    super(session, key);
    this.userId = userId;
    this.username = username;
    this.userIP = userIP;
    this.gameMode = gameMode;
    this.slot = slot;
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.price = price;
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

  public int getSlot() {
    return slot;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getAmount() {
    return amount;
  }

  public int getPrice() {
    return price;
  }
}
