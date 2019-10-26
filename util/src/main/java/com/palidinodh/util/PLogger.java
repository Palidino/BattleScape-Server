package com.palidinodh.util;

public class PLogger {
  public static void print(Object message) {
    message = "[" + PTime.getDetailedDate() + "] " + message;
    System.out.print(message);
  }

  public static void println(Object message) {
    message = "[" + PTime.getDetailedDate() + "] " + message;
    System.out.println(message);
  }

  public static void error(String message) {
    error(message, null);
  }

  public static void error(Exception e) {
    error(e.getMessage(), e);
  }

  public static synchronized void error(String message, Exception e) {
    message = "[" + PTime.getDetailedDate() + "] " + message;
    println(message);
  }
}
