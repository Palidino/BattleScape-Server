package com.palidinodh.osrsscript.player.plugin.clanwars;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.palidinodh.osrscore.io.cache.id.VarbitId;
import com.palidinodh.util.PCollection;
import lombok.Getter;
import lombok.var;

@Getter
public enum ClanWarsRule {
  GAME_END(VarbitId.CLAN_WARS_GAME_END, ClanWarsRuleOption.LAST_TEAM_STANDING,
      ClanWarsRuleOption.KILLS_25, ClanWarsRuleOption.KILLS_50, ClanWarsRuleOption.KILLS_100,
      ClanWarsRuleOption.KILLS_200, ClanWarsRuleOption.KILLS_500, ClanWarsRuleOption.KILLS_1000,
      ClanWarsRuleOption.KILLS_5000, ClanWarsRuleOption.KILLS_10000,
      ClanWarsRuleOption.KING_80_POINTS, ClanWarsRuleOption.KING_250_POINTS,
      ClanWarsRuleOption.KING_750_POINTS, ClanWarsRuleOption.KING_1500_POINTS,
      ClanWarsRuleOption.KING_2500_POINTS, ClanWarsRuleOption.KING_4000_POINTS,
      ClanWarsRuleOption.KING_6000_POINTS, ClanWarsRuleOption.MOST_KILLS_5_MINS,
      ClanWarsRuleOption.MOST_KILLS_10_MINS, ClanWarsRuleOption.MOST_KILLS_20_MINS,
      ClanWarsRuleOption.MOST_KILLS_60_MINS, ClanWarsRuleOption.MOST_KILLS_120_MINS,
      ClanWarsRuleOption.ODDSKULL_100_POINTS, ClanWarsRuleOption.ODDSKULL_300_POINTS,
      ClanWarsRuleOption.ODDSKULL_500_POINTS),
  MELEE(VarbitId.CLAN_WARS_MELEE, ClanWarsRuleOption.ALLOWED, ClanWarsRuleOption.DISABLED),
  RANGING(VarbitId.CLAN_WARS_RANGING, ClanWarsRuleOption.ALLOWED, ClanWarsRuleOption.DISABLED),
  MAGIC(VarbitId.CLAN_WARS_MAGIC, ClanWarsRuleOption.ALLOWED, ClanWarsRuleOption.STANDARD_SPELLS,
      ClanWarsRuleOption.BINDING_ONLY, ClanWarsRuleOption.DISABLED),
  PRAYER(VarbitId.CLAN_WARS_PRAYER, ClanWarsRuleOption.ALLOWED, ClanWarsRuleOption.NO_OVERHEADS,
      ClanWarsRuleOption.DISABLED),
  FOOD(VarbitId.CLAN_WARS_FOOD, ClanWarsRuleOption.ALLOWED, ClanWarsRuleOption.DISABLED),
  DRINKS(VarbitId.CLAN_WARS_DRINKS, ClanWarsRuleOption.ALLOWED, ClanWarsRuleOption.DISABLED),
  SPECIAL_ATTACKS(VarbitId.CLAN_WARS_SPECIAL_ATTACKS, ClanWarsRuleOption.ALLOWED,
      ClanWarsRuleOption.NO_STAFF_OF_THE_DEAD, ClanWarsRuleOption.DISABLED),
  STRAGGLERS(VarbitId.CLAN_WARS_STRAGGLERS, ClanWarsRuleOption.KILL_EM_ALL,
      ClanWarsRuleOption.IGNORE_5),
  IGNORE_FREEZING(VarbitId.CLAN_WARS_IGNORE_FREEZING, ClanWarsRuleOption.DISABLED,
      ClanWarsRuleOption.ALLOWED),
  PJ_TIMER(VarbitId.CLAN_WARS_PJ_TIMER, ClanWarsRuleOption.DISABLED, ClanWarsRuleOption.ALLOWED),
  ALLOW_TRIDENT_IN_PVP(VarbitId.CLAN_WARS_ALLOW_TRIDENT_IN_PVP, ClanWarsRuleOption.DISABLED,
      ClanWarsRuleOption.ALLOWED),
  SINGLE_SPELLS(VarbitId.CLAN_WARS_SINGLE_SPELLS, ClanWarsRuleOption.DISABLED,
      ClanWarsRuleOption.ALLOWED),
  ARENA(VarbitId.CLAN_WARS_ARENA, ClanWarsRuleOption.WASTELAND, ClanWarsRuleOption.PLATEAU,
      ClanWarsRuleOption.SYLVAN_GLADE, ClanWarsRuleOption.FORSAKEN_QUARRY,
      ClanWarsRuleOption.TURRETS, ClanWarsRuleOption.CLAN_CUP, ClanWarsRuleOption.SOGGY_SWAMP,
      ClanWarsRuleOption.GHASTLY_SWAMP, ClanWarsRuleOption.NORTHLEACH_QUELL,
      ClanWarsRuleOption.GRIDLOCK, ClanWarsRuleOption.ETHREAL, ClanWarsRuleOption.CLASSIC,
      ClanWarsRuleOption.LUMBRIDGE, ClanWarsRuleOption.FALADOR);

  public static final int TOTAL = 14;
  private static final int[] DEFAULT = new int[TOTAL];
  private static final Map<Integer, String> DESCRIPTIONS;

  private int varbit;
  private List<ClanWarsRuleOption> options;

  private ClanWarsRule(int varbit, ClanWarsRuleOption... options) {
    this.varbit = varbit;
    this.options = PCollection.toList(options);
  }

  public boolean hasOption(ClanWarsRuleOption option) {
    return options.contains(option);
  }

  public static int[] getDefault() {
    var rules = new int[TOTAL];
    System.arraycopy(DEFAULT, 0, rules, 0, TOTAL);
    return rules;
  }

  public static String getDescriptions(int[] rules) {
    var builder = new StringBuilder();
    builder.append("<u=FFFFFF><col=FFFFFF>The Game:</col></u><br><br>");
    builder.append(DESCRIPTIONS.get(GAME_END.ordinal() << 16 | rules[GAME_END.ordinal()]));
    builder.append("<u=FFFFFF><col=FFFFFF>The Arena:</col></u> "
        + Arena.get(rules[ARENA.ordinal()]).getName() + "<br><br>");
    builder.append("<u=FFFFFF><col=FFFFFF>The Combat:</col></u><br><br>");
    builder.append(DESCRIPTIONS.get(MELEE.ordinal() << 16 | rules[MELEE.ordinal()]));
    builder.append(DESCRIPTIONS.get(RANGING.ordinal() << 16 | rules[RANGING.ordinal()]));
    builder.append(DESCRIPTIONS.get(MAGIC.ordinal() << 16 | rules[MAGIC.ordinal()]));
    builder.append(DESCRIPTIONS.get(PRAYER.ordinal() << 16 | rules[PRAYER.ordinal()]));
    builder.append(DESCRIPTIONS.get(FOOD.ordinal() << 16 | rules[FOOD.ordinal()]));
    builder.append(DESCRIPTIONS.get(DRINKS.ordinal() << 16 | rules[DRINKS.ordinal()]));
    builder.append("<col=EEEEEE>Special attacks:</col><br>");
    builder.append(
        DESCRIPTIONS.get(SPECIAL_ATTACKS.ordinal() << 16 | rules[SPECIAL_ATTACKS.ordinal()]));
    builder.append("<u=FFFFFF><col=FFFFFF>Other settings:</col></u><br><br>");
    builder.append(
        DESCRIPTIONS.get(IGNORE_FREEZING.ordinal() << 16 | rules[IGNORE_FREEZING.ordinal()]));
    builder.append(DESCRIPTIONS.get(PJ_TIMER.ordinal() << 16 | rules[PJ_TIMER.ordinal()]));
    builder
        .append(DESCRIPTIONS.get(SINGLE_SPELLS.ordinal() << 16 | rules[SINGLE_SPELLS.ordinal()]));
    builder.append(DESCRIPTIONS
        .get(ALLOW_TRIDENT_IN_PVP.ordinal() << 16 | rules[ALLOW_TRIDENT_IN_PVP.ordinal()]));
    return builder.toString();
  }

  static {
    for (var rule : values()) {
      DEFAULT[rule.ordinal()] = rule.options.get(0).getIndex();
    }

    var descriptions = new HashMap<Integer, String>();
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.LAST_TEAM_STANDING.getIndex(),
        "The battle ends when all members of a team have been defeated.<br>"
            + "Fighters may not join or re-join the battle after it has begun.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_25.getIndex(),
        "The first team to score 25 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_50.getIndex(),
        "The first team to score 50 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_100.getIndex(),
        "The first team to score 100 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_200.getIndex(),
        "The first team to score 200 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_500.getIndex(),
        "The first team to score 500 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_1000.getIndex(),
        "The first team to score 1,000 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_5000.getIndex(),
        "The first team to score 5,000 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(GAME_END.ordinal() << 16 | ClanWarsRuleOption.KILLS_10000.getIndex(),
        "The first team to score 10,000 kills will win.<br>"
            + "Fighters may enter the battle at any time.<br><br>");
    descriptions.put(MELEE.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "<col=EEEEEE>Melee:</col> Melee combat is allowed.<br>");
    descriptions.put(MELEE.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "<col=EEEEEE>Melee:</col> Melee combat is disabled.<br>");
    descriptions.put(RANGING.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "<col=EEEEEE>Ranging:</col> Ranging is allowed.<br>");
    descriptions.put(RANGING.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "<col=EEEEEE>Ranging:</col> Ranging is disabled.<br>");
    descriptions.put(MAGIC.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "<col=EEEEEE>Magic:</col> All spellbooks are allowed.<br><br>");
    descriptions.put(MAGIC.ordinal() << 16 | ClanWarsRuleOption.STANDARD_SPELLS.getIndex(),
        "<col=EEEEEE>Magic:</col> Only the standard spellbook is allowed.<br><br>");
    descriptions.put(MAGIC.ordinal() << 16 | ClanWarsRuleOption.BINDING_ONLY.getIndex(),
        "<col=EEEEEE>Magic:</col> Only the Bind, Snare and Entangle spells are allowed.<br><br>");
    descriptions.put(MAGIC.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "<col=EEEEEE>Magic:</col> No magical combat is allowed.<br><br>");
    descriptions.put(PRAYER.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "<col=EEEEEE>Prayer:</col> All prayers are allowed.<br><br>");
    descriptions.put(PRAYER.ordinal() << 16 | ClanWarsRuleOption.NO_OVERHEADS.getIndex(),
        "<col=EEEEEE>Prayer:</col> Prayers that use an overhead icon are forbidden. "
            + "Other prayers are allowed.<br><br>");
    descriptions.put(PRAYER.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "<col=EEEEEE>Prayer:</col> No prayers are allowed.<br><br>");
    descriptions.put(FOOD.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "<col=EEEEEE>Food:</col> Food may be eaten during the battle.<br>");
    descriptions.put(FOOD.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "<col=EEEEEE>Food:</col> No food may be eaten during the battle.<br>");
    descriptions.put(DRINKS.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "<col=EEEEEE>Drinks:</col> Drinks, such as potions, may be used during the battle.<br><br>");
    descriptions.put(DRINKS.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "<col=EEEEEE>Drinks:</col> No drinks may be consumed during the battle.<br><br>");
    descriptions.put(SPECIAL_ATTACKS.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "Special attacks are allowed.<br><br>");
    descriptions.put(
        SPECIAL_ATTACKS.ordinal() << 16 | ClanWarsRuleOption.NO_STAFF_OF_THE_DEAD.getIndex(),
        "The Staff of the Dead cannot use its special attack, but all other special attacks are allowed.<br><br>");
    descriptions.put(SPECIAL_ATTACKS.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "Special attacks are forbidden.<br><br>");
    descriptions.put(IGNORE_FREEZING.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        // "Spells such as Entangle and Ice Barrage will not prevent their targets from
        // moving.<br>");
        "Only F2P content is allowed in this battle.<br>");
    descriptions.put(IGNORE_FREEZING.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(), "");
    descriptions.put(PJ_TIMER.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        // "In single-way combat areas, players are prevented from being attacked for 10 secs
        // after "
        // + "they have been attacking someone else.<br>");
        "Only players with a Defence level of 1 can participate in this battle.<br>");
    descriptions.put(PJ_TIMER.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(), "");
    descriptions.put(SINGLE_SPELLS.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "Multi-target attacks (such as chinchompas) will hit only one target, even in multi-way combat areas.<br>");
    descriptions.put(SINGLE_SPELLS.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(), "");
    descriptions.put(ALLOW_TRIDENT_IN_PVP.ordinal() << 16 | ClanWarsRuleOption.ALLOWED.getIndex(),
        "The Trident of the Seas is able to cast its spell on players.<br>");
    descriptions.put(ALLOW_TRIDENT_IN_PVP.ordinal() << 16 | ClanWarsRuleOption.DISABLED.getIndex(),
        "");
    DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
  }
}
