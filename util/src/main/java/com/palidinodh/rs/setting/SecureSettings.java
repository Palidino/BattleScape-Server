package com.palidinodh.rs.setting;

import lombok.Getter;

@Getter
public class SecureSettings {
  private String password;
  private String communicationIp;
  private SqlForum sqlForum;
  private SqlConnection sqlConnection;
  private SqlCustomUserFields sqlCustomUserFields;
  private String discordToken;

  public String getCommunicationIp() {
    if (communicationIp == null) {
      return "0.0.0.0";
    }
    String[] ipData = communicationIp.split(":");
    return ipData[0];
  }

  public int getCommunicationPort() {
    if (communicationIp == null) {
      return 43596;
    }
    String[] ipData = communicationIp.split(":");
    return Integer.parseInt(ipData[1]);
  }

  public Forum getForum() {
    if (sqlForum == null) {
      return null;
    }
    switch (sqlForum) {
      case VBULLETIN_4:
        return new Vbulletin4Forum(this);
      case XENFORO:
        return new XenforoForum(this);
    }
    return null;
  }
}
