package com.palidinodh.rs.setting;

import java.sql.Connection;
import java.util.Map;

public interface Forum {
  String getColumnName(SqlUserField sqlUserField);

  Map<SqlUserField, String> executeLoginQuery(Connection connection, String usernameOrEmail);

  String buildUserUpdate(SqlUserField sqlUserField, String value, int userId);

  boolean verifyPassword(String suppliedPassword, String password, String salt);
}
