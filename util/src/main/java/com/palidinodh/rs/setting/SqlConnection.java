package com.palidinodh.rs.setting;

import lombok.Getter;

@Getter
public class SqlConnection {
  private String connectionIp;
  private int connectionPort;
  private String username;
  private String password;
  private String databaseName;

  public static String escapeString(String s) {
    int stringLength = s.length();
    if (!isEscapeNeededForString(s, stringLength)) {
      return '\'' + s + '\'';
    }
    StringBuilder buf = new StringBuilder((int) (s.length() * 1.1D));
    buf.append('\'');
    for (int i = 0; i < stringLength; i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\000':
          buf.append('\\');
          buf.append('0');
          break;
        case '\n':
          buf.append('\\');
          buf.append('n');
          break;
        case '\r':
          buf.append('\\');
          buf.append('r');
          break;
        case '\\':
          buf.append('\\');
          buf.append('\\');
          break;
        case '\'':
          buf.append('\\');
          buf.append('\'');
          break;
        case '"':
          buf.append('\\');
          buf.append('"');
          break;
        case '\032':
          buf.append('\\');
          buf.append('Z');
          break;
        case '¥':
        case '₩':
          break;
        default:
          buf.append(c);
      }
    }
    buf.append('\'');
    s = buf.toString();
    return s;
  }

  private static boolean isEscapeNeededForString(String x, int stringLength) {
    for (int i = 0; i < stringLength; i++) {
      char c = x.charAt(i);
      switch (c) {
        case '\000':
        case '\n':
        case '\r':
        case '\032':
        case '"':
        case '\'':
        case '\\':
          return true;
      }
    }
    return false;
  }
}
