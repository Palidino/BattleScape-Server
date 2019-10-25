package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class SQLUpdateRequest extends Request {
  private String sql;

  public SQLUpdateRequest(ServerSession session, int key, String sql) {
    super(session, key);
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }
}
