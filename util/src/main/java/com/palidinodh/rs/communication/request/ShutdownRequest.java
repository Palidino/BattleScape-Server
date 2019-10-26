package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class ShutdownRequest extends Request {
  public ShutdownRequest(ServerSession session, int key) {
    super(session, key);
  }
}
