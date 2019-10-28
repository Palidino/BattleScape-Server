package com.palidinodh.util;

public class PLogger {
  private static final Object LOCK = new Object();

  private static boolean isPrinting;

  public static void print(Object message) {
    synchronized (LOCK) {
      if (!isPrinting) {
        message = "[" + PTime.getFullDate() + "] " + message;
      }
      System.out.print(message);
      isPrinting = true;
    }
  }

  public static void println(Object message) {
    synchronized (LOCK) {
      if (!isPrinting) {
        message = "[" + PTime.getFullDate() + "] " + message;
      }
      System.out.println(message);
      isPrinting = false;
    }
  }

  public static void error(String message) {
    error(message, null);
  }

  public static void error(Exception e) {
    error(e.getMessage(), e);
  }

  public static void error(String message, Exception e) {
    synchronized (LOCK) {
      message = "[" + PTime.getFullDate() + "] " + message;
      println(message);
      e.printStackTrace();
    }
  }
}
