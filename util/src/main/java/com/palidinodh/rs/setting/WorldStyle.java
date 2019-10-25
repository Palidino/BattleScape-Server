package com.palidinodh.rs.setting;

public enum WorldStyle {
  ECONOMY, SPAWN;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
