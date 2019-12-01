package com.palidinodh.osrsscript.player.plugin.clanwars;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.var;

enum CompletedState {
  NONE, LOSE, WIN;

  private static final Map<ClanWarsRuleOption, String> DESCRIPTIONS;

  public static String getDescription(ClanWarsRuleOption option, boolean won) {
    var description = DESCRIPTIONS.get(option);
    if (description == null) {
      return won ? "You have won!" : "You have lost!";
    }
    return (won ? "Your" : "The other") + " team " + description;
  }

  static {
    var descriptions = new HashMap<ClanWarsRuleOption, String>();
    descriptions.put(ClanWarsRuleOption.LAST_TEAM_STANDING, "was the last standing.");
    descriptions.put(ClanWarsRuleOption.KILLS_25, "achieved 25 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_50, "achieved 50 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_100, "achieved 100 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_200, "achieved 200 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_500, "achieved 500 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_1000, "achieved 1,000 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_5000, "achieved 5,000 kills.");
    descriptions.put(ClanWarsRuleOption.KILLS_10000, "achieved 10,000 kills.");
    DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
  }
}
