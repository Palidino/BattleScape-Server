package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

class GargoyleCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(1.55).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable CURSED_DROP_TABLE =
      NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_500).log(true)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_GLOVES).weight(6)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_RING).weight(6)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_HAMMER).weight(3)))
          .drop(
              NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_TOURMALINE_CORE).weight(2)))
          .build();
  private static final NpcCombatStyle SPECIAL_ATTACK = NpcCombatStyle.builder()
      .type(
          NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build())
      .damage(NpcCombatDamage.builder().maximum(38).ignorePrayer(true).build()).animation(7815)
      .attackSpeed(4)
      .projectile(NpcCombatProjectile.builder().id(1453).speedMinimumDistance(8).build())
      .effect(NpcCombatEffect.builder().magicBind(6).build())
      .specialAttack(NpcCombatTargetTile.builder().build()).build();

  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_TOP_DARK)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_MAUL)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_150)
        .slayerTask(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BRITTLE_KEY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BAR_NOTED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 75, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 400, 1000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 10000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE_NOTED, 10, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_BAR_NOTED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_BAR_NOTED, 10, 15)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.GARGOYLE_111);
    combat.hitpoints(NpcCombatHitpoints.total(105));
    combat.stats(NpcCombatStats.builder().attackLevel(75).defenceLevel(107)
        .bonus(CombatBonus.DEFENCE_STAB, 20).bonus(CombatBonus.DEFENCE_SLASH, 20)
        .bonus(CombatBonus.DEFENCE_MAGIC, 20).bonus(CombatBonus.DEFENCE_RANGED, 20).build());
    combat
        .slayer(NpcCombatSlayer.builder().level(75).superiorId(NpcId.MARBLE_GARGOYLE_349).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.blockAnimation(1517);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(11));
    style.animation(1519).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_GARGOYLE_349_16007);
    cursedCombat
        .hitpoints(NpcCombatHitpoints.builder().total(270).bar(HitpointsBar.GREEN_RED_60).build());
    cursedCombat.stats(NpcCombatStats.builder().attackLevel(230).defenceLevel(190)
        .bonus(CombatBonus.DEFENCE_STAB, 50).bonus(CombatBonus.DEFENCE_SLASH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(75).build());
    cursedCombat.deathAnimation(7812).blockAnimation(7814);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7811).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7811).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    var superiorCombat = NpcCombatDefinition.builder();
    superiorCombat.id(NpcId.MARBLE_GARGOYLE_349);
    superiorCombat
        .hitpoints(NpcCombatHitpoints.builder().total(270).bar(HitpointsBar.GREEN_RED_60).build());
    superiorCombat.stats(NpcCombatStats.builder().attackLevel(230).defenceLevel(190)
        .bonus(CombatBonus.DEFENCE_STAB, 50).bonus(CombatBonus.DEFENCE_SLASH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    superiorCombat.slayer(NpcCombatSlayer.builder().level(75).experience(3044).build());
    superiorCombat.aggression(NpcCombatAggression.PLAYERS);
    superiorCombat
        .killCount(NpcCombatKillCount.builder().asName("Superior Slayer Creature").build());
    superiorCombat.deathAnimation(7812).blockAnimation(7814);
    superiorCombat.drop(drop.rolls(3).build());

    style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7811).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    superiorCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7811).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    superiorCombat.style(style.build());


    return Arrays.asList(combat.build(), cursedCombat.build(), superiorCombat.build());
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    return (npc.getId() == NpcId.MARBLE_GARGOYLE_349
        || npc.getId() == NpcId.CURSED_GARGOYLE_349_16007) && PRandom.randomE(8) == 0
            ? SPECIAL_ATTACK
            : combatStyle;
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_GARGOYLE_349_16007) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
      if (CURSED_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
        return CURSED_DROP_TABLE;
      }
    }
    if ((npc.getId() == NpcId.MARBLE_GARGOYLE_349 || npc.getId() == NpcId.CURSED_GARGOYLE_349_16007)
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_GARGOYLE_349_16007 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
