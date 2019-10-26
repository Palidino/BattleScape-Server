package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GECollectOfferRequest extends Request {
  private int userId;
  private int slot;
  private int collectedAmount;
  private int collectedPrice;

  public GECollectOfferRequest(ServerSession session, int key, int userId, int slot,
      int collectedAmount, int collectedPrice) {
    super(session, key);
    this.userId = userId;
    this.slot = slot;
    this.collectedAmount = collectedAmount;
    this.collectedPrice = collectedPrice;
  }

  public int getUserId() {
    return userId;
  }

  public int getSlot() {
    return slot;
  }

  public int getCollectedAmount() {
    return collectedAmount;
  }

  public int getCollectedPrice() {
    return collectedPrice;
  }
}
