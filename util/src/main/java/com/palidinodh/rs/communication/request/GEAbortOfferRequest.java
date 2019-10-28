package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class GEAbortOfferRequest extends Request {
  private int userId;
  private int slot;

  public GEAbortOfferRequest(ServerSession session, int key, int userId, int slot) {
    super(session, key);
    this.userId = userId;
    this.slot = slot;
  }

  public int getUserId() {
    return userId;
  }

  public int getSlot() {
    return slot;
  }
}
