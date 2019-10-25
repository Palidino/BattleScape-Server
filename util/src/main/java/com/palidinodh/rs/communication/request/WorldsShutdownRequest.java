package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class WorldsShutdownRequest extends Request {
  private int time;

  public WorldsShutdownRequest(ServerSession session, int key, int time) {
    super(session, key);
    this.time = time;
  }

  public int getTime() {
    return time;
  }
}
