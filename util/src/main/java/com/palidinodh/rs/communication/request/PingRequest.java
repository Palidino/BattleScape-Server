package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class PingRequest extends Request {
  public PingRequest(ServerSession session, int key) {
    super(session, key);
  }
}
