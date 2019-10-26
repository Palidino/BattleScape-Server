package com.palidinodh.rs.communication.response;

import java.util.List;
import com.palidinodh.rs.adaptive.RsClanActiveUser;
import com.palidinodh.rs.communication.request.Request;

public class ClanUpdateResponse extends Response {
  private String clanUsername;
  private String name;
  private int kickLimit;
  private List<RsClanActiveUser> users;

  public ClanUpdateResponse(Request request, String clanUsername, String name, int kickLimit,
      List<RsClanActiveUser> users) {
    super(request);
    this.clanUsername = clanUsername;
    this.name = name;
    this.kickLimit = kickLimit;
    this.users = users;
  }

  public String getClanUsername() {
    return clanUsername;
  }

  public String getName() {
    return name;
  }

  public int getKickLimit() {
    return kickLimit;
  }

  public List<RsClanActiveUser> getUsers() {
    return users;
  }
}
