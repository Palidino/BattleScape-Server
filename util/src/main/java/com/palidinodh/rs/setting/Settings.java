package com.palidinodh.rs.setting;

import java.io.File;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Settings {
  @Getter
  @Setter
  private static Settings instance;

  private String name;
  private int revision;
  private int id;
  private String cacheDirectory;
  private String savesDirectory;
  private String logsDirectory;
  private String scriptPackage;
  private String password;
  private WorldStyle worldStyle;
  private boolean staffOnly;
  private boolean beta;
  private boolean betaSaving;
  private boolean local;
  private boolean withResponseServer;
  private String worldIP;
  private String responseIP;
  private SqlForum sqlForum;
  private SqlConnection sqlConnection;
  private SqlCustomUserFields sqlCustomUserFields;
  private Map<String, SqlUserRank> sqlUserRanks;
  private SqlUserPacketLog sqlUserPacketLog;
  private String discordUrl;
  private String websiteUrl;
  private String updatesUrl;
  private String voteUrl;
  private String storeUrl;
  private String rulesUrl;
  private String supportUrl;
  private String reportPlayerUrl;
  private String threadUrl;
  private String discordToken;
  private Map<DiscordChannel, String> discordChannels;

  public File getCacheDirectory() {
    return new File(cacheDirectory);
  }

  public File getSavesDirectory() {
    return new File(savesDirectory);
  }

  public File getLogsDirectory() {
    return new File(logsDirectory);
  }

  public File getWorldDirectory() {
    return new File(getSavesDirectory(), "world");
  }

  public File getPlayerDirectory() {
    return new File(savesDirectory, "player");
  }

  public File getPlayerMapDirectory() {
    return new File(getPlayerDirectory(), "map");
  }

  public File getPlayerMapOnlineDirectory() {
    return new File(getPlayerDirectory(), "maponline");
  }

  public File getPlayerMapErrorDirectory() {
    return new File(getPlayerDirectory(), "maperror");
  }

  public File getPlayerMapBackupDirectory() {
    return new File(getPlayerDirectory(), "mapbackup");
  }

  public File getPlayerClanDirectory() {
    return new File(getPlayerDirectory(), "clan");
  }

  public File getPlayerExchangeDirectory() {
    return new File(getPlayerDirectory(), "exchange");
  }

  public File getPlayerLogsDirectory() {
    return new File(getLogsDirectory(), "player");
  }


  public boolean isEconomy() {
    return worldStyle == WorldStyle.ECONOMY;
  }

  public boolean isSpawn() {
    return worldStyle == WorldStyle.SPAWN;
  }

  public String getStyleName() {
    return worldStyle.toString();
  }

  public SqlUserRank getSqlUserRank(int forumGroupId) {
    SqlUserRank sqlUserRank =
        sqlUserRanks != null ? sqlUserRanks.get(Integer.toString(forumGroupId)) : null;
    return sqlUserRank != null ? sqlUserRank : SqlUserRank.UNKNOWN;
  }

  public boolean hasSqlUserRank(SqlUserRank rank) {
    return sqlUserRanks != null && sqlUserRanks.containsValue(rank);
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

  public String getDiscordChannel(DiscordChannel discordChannel) {
    return discordChannels != null ? discordChannels.get(discordChannel) : null;
  }
}
