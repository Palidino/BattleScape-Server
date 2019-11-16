package com.palidinodh.rs.setting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.encryption.BCrypt;

public class XenforoForum implements Forum {
  private SecureSettings secureSettings;

  public XenforoForum(SecureSettings secureSettings) {
    this.secureSettings = secureSettings;
  }

  @Override
  public String getColumnName(SqlUserField sqlUserField) {
    switch (sqlUserField) {
      case ID:
        return "user_id";
      case NAME:
        return "username";
      case PASSWORD:
        return "data";
      case EMAIL:
        return "email";
      case MAIN_GROUP:
        return "user_group_id";
      case SUB_GROUPS:
        return "secondary_group_ids";
      case PENDING_BONDS:
        return secureSettings.getSqlCustomUserFields().getPendingBonds();
      case PENDING_VOTE_POINTS:
        return secureSettings.getSqlCustomUserFields().getPendingVotePoints();
      case VOTE_TIME_RUNELOCUS:
        return secureSettings.getSqlCustomUserFields().getVoteTimeRunelocus();
      default:
        return null;
    }
  }

  @Override
  public Map<SqlUserField, String> executeLoginQuery(Connection connection,
      String usernameOrEmail) {
    String query =
        "SELECT * FROM xf_user JOIN xf_user_authenticate ON (xf_user.user_id = xf_user_authenticate.user_id) WHERE ";
    if (usernameOrEmail.contains("@")) {
      query += "xf_user.email='" + usernameOrEmail + "'";
    } else {
      query += "xf_user.username='" + usernameOrEmail + "'";
    }
    Map<SqlUserField, String> results = new HashMap<>();
    Statement statement = null;
    ResultSet userResult = null;
    try {
      statement = connection.createStatement();
      userResult = statement.executeQuery(query);
      if (!userResult.next() || userResult.getString("user_id").length() == 0) {
        throw new Exception("No account!");
      }
      for (SqlUserField sqlUserField : SqlUserField.values()) {
        String columnName = getColumnName(sqlUserField);
        if (columnName == null) {
          continue;
        }
        String value = userResult.getString(columnName);
        results.put(sqlUserField, value);
      }
      String password = results.get(SqlUserField.PASSWORD);
      password = password.substring(password.indexOf("{") + 1, password.lastIndexOf("}"));
      password = password.split(";")[1];
      password = password.substring(password.indexOf("\"") + 1, password.lastIndexOf("\""));
      password = password.replace("$2y$", "$2a$");
      results.put(SqlUserField.PASSWORD, password);
      int mainGroupId = Integer.parseInt(results.get(SqlUserField.MAIN_GROUP));
      results.put(SqlUserField.MAIN_GROUP,
          Settings.getInstance().getSqlUserRank(mainGroupId).name());
      if ("1".equals(userResult.getString("is_banned"))) {
        results.put(SqlUserField.MAIN_GROUP, SqlUserRank.BANNED.name());
      }
      if (results.get(SqlUserField.SUB_GROUPS).length() > 0) {
        String[] subGroupIds = results.get(SqlUserField.SUB_GROUPS).split(",");
        String subGroupList = "";
        for (int i = 0; i < subGroupIds.length; i++) {
          subGroupList +=
              Settings.getInstance().getSqlUserRank(Integer.parseInt(subGroupIds[i])).name();
          if (i + 1 < subGroupIds.length) {
            subGroupList += ",";
          }
        }
        results.put(SqlUserField.SUB_GROUPS, subGroupList);
      }
    } catch (Exception e) {
      results.clear();
      if (!"No account!".equals(e.getMessage())) {
        e.printStackTrace();
      }
    } finally {
      try {
        if (userResult != null) {
          userResult.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (Exception e) {
      }
    }
    return results;
  }

  @Override
  public String buildUserUpdate(SqlUserField sqlUserField, String value, int userId) {
    return "UPDATE xf_user SET " + getColumnName(sqlUserField) + " = '" + value
        + "' WHERE user_id = '" + userId + "'";
  }

  @Override
  public boolean verifyPassword(String suppliedPassword, String password, String salt) {
    return BCrypt.checkpw(suppliedPassword, password);
  }
}
