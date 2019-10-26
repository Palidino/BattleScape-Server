package com.palidinodh.osrsscript.player.plugin.clanwars.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RuleOption {
  ALLOWED(0), DISABLED(1), LAST_TEAM_STANDING(0), KILLS_25(1), KILLS_50(2), KILLS_100(3), KILLS_200(
      4), KILLS_500(5), KILLS_1000(6), KILLS_5000(7), KILLS_10000(8), KING_80_POINTS(
          9), KING_250_POINTS(10), KING_750_POINTS(11), KING_1500_POINTS(12), KING_2500_POINTS(
              13), KING_4000_POINTS(14), KING_6000_POINTS(15), MOST_KILLS_5_MINS(
                  16), MOST_KILLS_10_MINS(17), MOST_KILLS_20_MINS(18), MOST_KILLS_60_MINS(
                      19), MOST_KILLS_120_MINS(20), ODDSKULL_100_POINTS(21), ODDSKULL_300_POINTS(
                          22), ODDSKULL_500_POINTS(23), WASTELAND(Arena.WASTELAND
                              .ordinal()), PLATEAU(Arena.PLATEAU.ordinal()), SYLVAN_GLADE(
                                  Arena.SYLVAN_GLADE.ordinal()), FORSAKEN_QUARRY(
                                      Arena.FORSAKEN_QUARRY.ordinal()), TURRETS(
                                          Arena.TURRETS.ordinal()), CLAN_CUP(
                                              Arena.CLAN_CUP.ordinal()), SOGGY_SWAMP(
                                                  Arena.SOGGY_SWAMP.ordinal()), GHASTLY_SWAMP(
                                                      Arena.GHASTLY_SWAMP
                                                          .ordinal()), NORTHLEACH_QUELL(
                                                              Arena.NORTHLEACH_QUELL
                                                                  .ordinal()), GRIDLOCK(
                                                                      Arena.GRIDLOCK
                                                                          .ordinal()), ETHREAL(
                                                                              Arena.ETHREAL
                                                                                  .ordinal()), CLASSIC(
                                                                                      Arena.CLASSIC
                                                                                          .ordinal()), LUMBRIDGE(
                                                                                              Arena.LUMBRIDGE
                                                                                                  .ordinal()), FALADOR(
                                                                                                      Arena.FALADOR
                                                                                                          .ordinal()), STANDARD_SPELLS(
                                                                                                              2), BINDING_ONLY(
                                                                                                                  3), NO_STAFF_OF_THE_DEAD(
                                                                                                                      2), NO_OVERHEADS(
                                                                                                                          2), KILL_EM_ALL(
                                                                                                                              0), IGNORE_5(
                                                                                                                                  1);

  private int index;
}
