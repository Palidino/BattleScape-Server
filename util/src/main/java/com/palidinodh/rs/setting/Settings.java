package com.palidinodh.rs.setting;

import java.io.File;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Settings {
  @Getter
  private static Settings instance;
  @Getter
  private static SecureSettings secure;
  private static Boolean beta;

  private String name;
  private int revision;
  private String cacheDirectory;
  private String savesDirectory;
  private String logsDirectory;
  private String scriptPackage;
  private WorldStyle worldStyle;
  @Setter
  private boolean staffOnly;
  private boolean local;
  private boolean withResponseServer;
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

  public String getDiscordChannel(DiscordChannel discordChannel) {
    return discordChannels != null ? discordChannels.get(discordChannel) : null;
  }

  public static void setInstance(Settings settings) {
    if (instance != null) {
      throw new IllegalStateException("Settings can't be overwritten!");
    }
    instance = settings;
  }

  public static void setSecure(SecureSettings settings) {
    if (secure != null) {
      throw new IllegalStateException("Settings can't be overwritten!");
    }
    secure = settings;
  }

  public static boolean isBeta() {
    return beta != null ? beta : false;
  }

  public static void setBeta(boolean _beta) {
    if (beta != null) {
      throw new IllegalStateException("Settings can't be overwritten!");
    }
    beta = _beta;
  }
}
