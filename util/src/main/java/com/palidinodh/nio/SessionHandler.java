package com.palidinodh.nio;

public interface SessionHandler {
  void accept(Session session);

  void read(Session session, byte[] bytes);

  void closed(Session session);

  void error(Exception exception, Session session);
}
