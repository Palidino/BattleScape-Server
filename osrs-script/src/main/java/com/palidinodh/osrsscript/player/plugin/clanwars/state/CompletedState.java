package com.palidinodh.osrsscript.player.plugin.clanwars.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.palidinodh.osrsscript.player.plugin.clanwars.rule.RuleOption;
import lombok.var;

public enum CompletedState {
  NONE, LOSE, WIN;

  private static final Map<RuleOption, String> DESCRIPTIONS;

  public static String getDescription(RuleOption option, boolean won) {
    var description = DESCRIPTIONS.get(option);
    if (description == null) {
      return won ? "You have won!" : "You have lost!";
    }
    return (won ? "Your" : "The other") + " team " + description;
  }

  static {
    var descriptions = new HashMap<RuleOption, String>();
    descriptions.put(RuleOption.LAST_TEAM_STANDING, "was the last standing.");
    descriptions.put(RuleOption.KILLS_25, "achieved 25 kills.");
    descriptions.put(RuleOption.KILLS_50, "achieved 50 kills.");
    descriptions.put(RuleOption.KILLS_100, "achieved 100 kills.");
    descriptions.put(RuleOption.KILLS_200, "achieved 200 kills.");
    descriptions.put(RuleOption.KILLS_500, "achieved 500 kills.");
    descriptions.put(RuleOption.KILLS_1000, "achieved 1,000 kills.");
    descriptions.put(RuleOption.KILLS_5000, "achieved 5,000 kills.");
    descriptions.put(RuleOption.KILLS_10000, "achieved 10,000 kills.");
    DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
  }
}
