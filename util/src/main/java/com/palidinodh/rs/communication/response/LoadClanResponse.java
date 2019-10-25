package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class LoadClanResponse extends Response {
  private String name;
  private boolean disabled;
  private int enterLimit;
  private int talkLimit;
  private int kickLimit;

  public LoadClanResponse(Request request, String name, boolean disabled, int enterLimit,
      int talkLimit, int kickLimit) {
    super(request);
    this.name = name;
    this.disabled = disabled;
    this.enterLimit = enterLimit;
    this.talkLimit = talkLimit;
    this.kickLimit = kickLimit;
  }

  public String getName() {
    return name;
  }

  public boolean getDisabled() {
    return disabled;
  }

  public int getEnterLimit() {
    return enterLimit;
  }

  public int getTalkLimit() {
    return talkLimit;
  }

  public int getKickLimit() {
    return kickLimit;
  }
}
