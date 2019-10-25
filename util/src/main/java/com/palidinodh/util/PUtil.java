package com.palidinodh.util;

public class PUtil {
  private static final int SYSTEM_GC_MINIMUM = 10 * 60 * 1000;

  private static long lastCalledGC;

  public static void gc() {
    gc(false);
  }

  public static void gc(boolean force) {
    if (!force && PTime.currentTimeMillis() - lastCalledGC < SYSTEM_GC_MINIMUM) {
      return;
    }
    lastCalledGC = PTime.currentTimeMillis();
    System.gc();
  }

  public static int getFaceDirection(int xOffset, int yOffset) {
    return (int) (Math.atan2(-xOffset, -yOffset) * 325.949D) & 2047;
  }
}
