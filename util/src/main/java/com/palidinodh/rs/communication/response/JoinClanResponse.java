package com.palidinodh.rs.communication.response;

import java.util.List;
import com.palidinodh.rs.adaptive.RsClanActiveUser;
import com.palidinodh.rs.communication.request.Request;

public class JoinClanResponse extends Response {
  private String name;
  private int userId;
  private int kickLimit;
  private List<RsClanActiveUser> users;

  public JoinClanResponse(Request request, String name, int userId, int kickLimit,
      List<RsClanActiveUser> users) {
    super(request);
    this.name = name;
    this.userId = userId;
    this.kickLimit = kickLimit;
    this.users = users;
  }

  public String getName() {
    return name;
  }

  public int getUserId() {
    return userId;
  }

  public int getKickLimit() {
    return kickLimit;
  }

  public List<RsClanActiveUser> getUsers() {
    return users;
  }
}
