package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;
import com.palidinodh.rs.communication.response.PlayerLoginResponse;

public class PlayerLoginRequest extends Request {
  private String username;
  private String password;
  private String ip;
  private int worldId;

  public PlayerLoginRequest(ServerSession session, int key, String username, String password,
      String ip, int worldId) {
    super(session, key);
    this.username = username;
    this.password = password;
    this.ip = ip;
    this.worldId = worldId;
  }

  @Override
  public PlayerLoginResponse getResponse() {
    return (PlayerLoginResponse) super.getResponse();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public int getWorldId() {
    return worldId;
  }

  public String getIP() {
    return ip;
  }
}
