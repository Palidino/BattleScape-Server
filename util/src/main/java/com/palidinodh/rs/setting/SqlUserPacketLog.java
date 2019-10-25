package com.palidinodh.rs.setting;

public class SqlUserPacketLog {
  private String table;
  private String userIdColumn;
  private String usernameColumn;
  private String messageColumn;

  public String buildInsert(int userId, String username, String message) {
    username = SqlConnection.escapeString(username.replaceAll("[^a-zA-Z0-9 _-]", ""));
    message =
        SqlConnection.escapeString(message.replaceAll("[^a-zA-Z0-9 _\\-=;\\[\\](),.\\/]", ""));
    return "INSERT INTO " + table + " (" + userIdColumn + ", " + usernameColumn + ", "
        + messageColumn + ") VALUES (" + userId + ", " + username + ", " + message + ")";
  }
}
