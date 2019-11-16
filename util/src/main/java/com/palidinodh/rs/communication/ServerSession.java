package com.palidinodh.rs.communication;

import com.palidinodh.io.Stream;
import com.palidinodh.nio.Session;
import com.palidinodh.nio.WriteEventHandler;
import com.palidinodh.rs.setting.Settings;

public class ServerSession {
  private Session session;
  private Stream input = new Stream(32768);
  private Stream output = new Stream(32768);
  private String password;
  private int worldId;

  public ServerSession(Session session) {
    this.session = session;
  }

  public void write() {
    write(null);
  }

  public void write(WriteEventHandler handler) {
    if (session != null) {
      if (output.getPosition() > 0 && session.isOpen()) {
        session.write(output.toByteArray(), handler);
      }
    }
    output.clear();
  }

  public boolean passwordMatches() {
    return Settings.getSecure().getPassword().equals(password);
  }

  public void close() {
    if (session != null) {
      session.close();
    }
  }

  public boolean isOpen() {
    return session != null ? session.isOpen() : true;
  }

  public String getRemoteAddress() {
    return session != null ? session.getRemoteAddress() : "127.0.0.1";
  }

  public Session getSession() {
    return session;
  }

  public Stream getInput() {
    return input;
  }

  public Stream getOutput() {
    return output;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getWorldId() {
    return worldId;
  }

  public void setWorldId(int worldId) {
    this.worldId = worldId;
  }
}
