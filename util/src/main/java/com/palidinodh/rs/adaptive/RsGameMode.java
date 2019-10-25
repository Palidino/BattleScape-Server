package com.palidinodh.rs.adaptive;

import com.palidinodh.util.PString;

public enum RsGameMode {
  UNSET, NORMAL, IRONMAN, HARD, HARDCORE_IRONMAN, GROUP_IRONMAN;

  public String getFormattedName() {
    return PString.formatName(name().toLowerCase().replace("_", " "));
  }
}
