package com.palidinodh.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PLogger {
  private static String directory = "./";

  public static void print(Object message) {
    System.out.print(message);
  }

  public static void println(Object message) {
    System.out.println(message);
  }

  public static synchronized void log(String message) {
    println(message);
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(directory + "/info.log", true));
      out.write("[" + PTime.getDetailedDate() + "] " + message);
      out.newLine();
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static void error(String message) {
    error(message, null);
  }

  public static void error(Exception e) {
    error(e.getMessage(), e);
  }

  public static synchronized void error(String message, Exception e) {
    try {
      message = "[" + PTime.getDetailedDate() + "] " + message;
      BufferedWriter out = new BufferedWriter(new FileWriter(directory + "/error.log", true));
      println(message);
      out.write(message);
      out.newLine();
      if (e != null) {
        e.printStackTrace();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
          out.write(stackTraceElement.toString());
          out.newLine();
        }
      }
      out.newLine();
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static void emergency(String message) {
    emergency(message, null);
  }

  public static void emergency(Exception e) {
    emergency(e.getMessage(), e);
  }

  public static synchronized void emergency(String message, Exception e) {
    error(message, e);
  }

  public static void setDirectory(String dir) {
    directory = dir;
    new File(directory).mkdirs();
  }
}
