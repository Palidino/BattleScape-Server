package com.palidinodh.rs.setting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.encryption.MD5;

public class Vbulletin4Forum implements Forum {
  private Settings settings;

  public Vbulletin4Forum(Settings settings) {
    this.settings = settings;
  }

  @Override
  public String getColumnName(SqlUserField sqlUserField) {
    switch (sqlUserField) {
      case ID:
        return "userid";
      case NAME:
        return "username";
      case PASSWORD:
        return "password";
      case PASSWORD_SALT:
        return "salt";
      case EMAIL:
        return "email";
      case IP_ADDRESS:
        return "ipaddress";
      case MAIN_GROUP:
        return "usergroupid";
      case SUB_GROUPS:
        return "membergroupids";
      case PREMIUM_EXPIRATION:
        return "expirydate";
      case PENDING_BONDS:
        return settings.getSqlCustomUserFields().getPendingBonds();
      case PENDING_VOTE_POINTS:
        return settings.getSqlCustomUserFields().getPendingVotePoints();
      case VOTE_TIME_RUNELOCUS:
        return settings.getSqlCustomUserFields().getVoteTimeRunelocus();
      default:
        return null;
    }
  }

  @Override
  public Map<SqlUserField, String> executeLoginQuery(Connection connection,
      String usernameOrEmail) {
    String query =
        "SELECT * FROM user LEFT JOIN subscriptionlog ON (user." + getColumnName(SqlUserField.ID)
            + " = subscriptionlog." + getColumnName(SqlUserField.ID) + ") WHERE ";
    if (usernameOrEmail.contains("@")) {
      query += "user." + getColumnName(SqlUserField.EMAIL) + "="
          + SqlConnection.escapeString(usernameOrEmail);
    } else {
      query += "user." + getColumnName(SqlUserField.NAME) + "="
          + SqlConnection.escapeString(usernameOrEmail);
    }
    Map<SqlUserField, String> results = new HashMap<>();
    Statement statement = null;
    ResultSet userResult = null;
    ResultSet voteResult = null;
    try {
      statement = connection.createStatement();
      userResult = statement.executeQuery(query);
      if (!userResult.next()
          || userResult.getString(getColumnName(SqlUserField.ID)).length() == 0) {
        throw new Exception("No account: " + usernameOrEmail);
      }
      for (SqlUserField sqlUserField : SqlUserField.values()) {
        String columnName = getColumnName(sqlUserField);
        if (columnName == null) {
          continue;
        }
        String value = userResult.getString(columnName);
        results.put(sqlUserField, value);
      }
      int mainGroupId = Integer.parseInt(results.get(SqlUserField.MAIN_GROUP));
      results.put(SqlUserField.MAIN_GROUP, settings.getSqlUserRank(mainGroupId).name());
      if (results.get(SqlUserField.SUB_GROUPS).length() > 0) {
        String[] subGroupIds = results.get(SqlUserField.SUB_GROUPS).split(",");
        String subGroupList = "";
        for (int i = 0; i < subGroupIds.length; i++) {
          subGroupList += settings.getSqlUserRank(Integer.parseInt(subGroupIds[i])).name();
          if (i + 1 < subGroupIds.length) {
            subGroupList += ",";
          }
        }
        results.put(SqlUserField.SUB_GROUPS, subGroupList);
      }
      int voteTime = results.containsKey(SqlUserField.VOTE_TIME_RUNELOCUS)
          ? Integer.parseInt(results.get(SqlUserField.VOTE_TIME_RUNELOCUS))
          : 0;
      String ipAddress = results.get(SqlUserField.IP_ADDRESS);
      if (ipAddress != null) {
        voteResult = statement.executeQuery(
            "SELECT " + getColumnName(SqlUserField.VOTE_TIME_RUNELOCUS) + " FROM user WHERE "
                + getColumnName(SqlUserField.IP_ADDRESS) + "='" + ipAddress + "'");
        while (voteResult.next()) {
          int foundVoteTime = voteResult.getInt(getColumnName(SqlUserField.VOTE_TIME_RUNELOCUS));
          if (foundVoteTime > voteTime) {
            voteTime = foundVoteTime;
          }
        }
      }
      results.put(SqlUserField.VOTE_TIME_RUNELOCUS, Integer.toString(voteTime));
    } catch (Exception e) {
      results.clear();
      System.out.println(e.getMessage());
    } finally {
      try {
        if (userResult != null) {
          userResult.close();
        }
        if (voteResult != null) {
          voteResult.close();
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
    return "UPDATE user SET " + getColumnName(sqlUserField) + "="
        + SqlConnection.escapeString(value) + " WHERE " + getColumnName(SqlUserField.ID) + "="
        + userId;
  }

  @Override
  public boolean verifyPassword(String suppliedPassword, String password, String salt) {
    return password.equals(MD5.compute(suppliedPassword, salt));
  }
}
