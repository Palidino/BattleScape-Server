package com.palidinodh.rs.adaptive;

import com.palidinodh.util.PString;

public enum RsClanRank {
  NOT_IN_CLAN, ANYONE, ANY_FRIENDS, FRIEND, RECRUIT, CORPORAL, SERGEANT, LIEUTENANT, CAPTAIN, GENERAL, ONLY_ME, OWNER, STAFF;

  public String getFormattedName() {
    return PString.formatName(name().toLowerCase().replace("_", " "));
  }

  public String getFormattedLimitName() {
    String name = getFormattedName();
    if (this == RECRUIT || this == CORPORAL || this == SERGEANT || this == LIEUTENANT
        || this == CAPTAIN || this == GENERAL) {
      name += "+";
    }
    return name;
  }
}
