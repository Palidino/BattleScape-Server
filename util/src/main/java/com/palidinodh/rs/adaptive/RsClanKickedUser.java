package com.palidinodh.rs.adaptive;

import com.palidinodh.util.PTime;
import lombok.Getter;

public class RsClanKickedUser {
  private static final int BAN_LENGTH_MINUTES = 60;

  @Getter
  private String username;
  private long kickDate = PTime.currentTimeMillis();

  public RsClanKickedUser(String username) {
    this.username = username;
  }

  public boolean isValid() {
    return PTime.milliToMin(kickDate) < BAN_LENGTH_MINUTES;
  }
}
