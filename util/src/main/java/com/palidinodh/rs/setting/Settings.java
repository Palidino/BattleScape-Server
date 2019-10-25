package com.palidinodh.rs.setting;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Settings {
  public static final Settings DEFAULT_ECONOMY = new Settings("BattleScape", 178, 1,
      WorldStyle.ECONOMY, false, false, false, true, true, "0.0.0.0", "0.0.0.0:43596", null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  public static final Settings DEFAULT_SPAWN = new Settings("BattleWild", 178, 1, WorldStyle.SPAWN,
      false, false, false, true, true, "0.0.0.0", "0.0.0.0:43596", null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null);

  private String name;
  private int revision;
  private int id;
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

