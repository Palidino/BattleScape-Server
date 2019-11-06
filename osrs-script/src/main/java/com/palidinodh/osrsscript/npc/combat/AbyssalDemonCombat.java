package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.map.area.CatacombsOfKourendArea;
import com.palidinodh.random.PRandom;
import lombok.var;

public class AbyssalDemonCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(2.32).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable TOTEM_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(NpcCombatDropTable.CHANCE_1_IN_350).order(NpcCombatDropTable.Order.RANDOM_UNIQUE)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_BASE)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_MIDDLE)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_TOP))).build();
  private static final NpcCombatDropTable SHARD_DROP_TABLE =
      NpcCombatDropTable.builder().chance(0.43)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_SHARD))).build();
  private static final NpcCombatDropTable CURSED_DROP_TABLE =
      NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_400).log(true)
          .drop(NpcCombatDropTableDrop
              .items(new RandomItem(ItemId.ABYSSAL_DAGGER_P_PLUS_PLUS).weight(10)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ABYSSAL_BLUDGEON).weight(8)))
          .build();

  private Npc npc;
  private boolean usingSpecialAttack;
  private int specialAttackCount;
  private Tile specialAttackTile;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_1200)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_32768).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ABYSSAL_DAGGER_P_PLUS_PLUS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.016).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ABYSSAL_HEAD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ABYSSAL_WHIP)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(4);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_ABYSSAL_HEAD_13508)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEFENCE_POTION_3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 7)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LOBSTER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 60)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var normalCombat = NpcCombatDefinition.builder();
    normalCombat.id(NpcId.ABYSSAL_DEMON_124);
    normalCombat.hitpoints(NpcCombatHitpoints.total(150));
    normalCombat.stats(NpcCombatStats.builder().attackLevel(97).defenceLevel(135)
        .bonus(CombatBonus.MELEE_DEFENCE, 20).bonus(CombatBonus.DEFENCE_RANGED, 20).build());
    normalCombat.slayer(
        NpcCombatSlayer.builder().level(85).superiorId(NpcId.GREATER_ABYSSAL_DEMON_342).build());
    normalCombat.type(NpcCombatType.DEMON).deathAnimation(1538).blockAnimation(2309);
    normalCombat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(1537).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    normalCombat.style(style.build());


    var catacombsCombat = NpcCombatDefinition.builder();
    catacombsCombat.id(NpcId.ABYSSAL_DEMON_124_7241);
    catacombsCombat.hitpoints(NpcCombatHitpoints.total(150));
    catacombsCombat.stats(NpcCombatStats.builder().attackLevel(97).defenceLevel(135)
        .bonus(CombatBonus.MELEE_DEFENCE, 20).bonus(CombatBonus.DEFENCE_RANGED, 20).build());
    catacombsCombat.slayer(
        NpcCombatSlayer.builder().level(85).superiorId(NpcId.GREATER_ABYSSAL_DEMON_342).build());
    catacombsCombat.type(NpcCombatType.DEMON).deathAnimation(1538).blockAnimation(2309);
    catacombsCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(1537).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    catacombsCombat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_ABYSSAL_DEMON_342_16010);
    cursedCombat
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_60).build());
    cursedCombat.stats(NpcCombatStats.builder().attackLevel(300).defenceLevel(240)
        .bonus(CombatBonus.MELEE_DEFENCE, 50).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(85).build());
    cursedCombat.type(NpcCombatType.DEMON).deathAnimation(1538).blockAnimation(2309);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(1537).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    var superiorCombat = NpcCombatDefinition.builder();
    superiorCombat.id(NpcId.GREATER_ABYSSAL_DEMON_342);
    superiorCombat
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_60).build());
    superiorCombat.stats(NpcCombatStats.builder().attackLevel(300).defenceLevel(240)
        .bonus(CombatBonus.MELEE_DEFENCE, 50).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    superiorCombat.slayer(NpcCombatSlayer.builder().level(85).experience(4200).build());
    superiorCombat
        .killCount(NpcCombatKillCount.builder().asName("Superior Slayer Creature").build());
    superiorCombat.type(NpcCombatType.DEMON).deathAnimation(1538).blockAnimation(2309);
    superiorCombat.drop(drop.rolls(3).build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(1537).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    superiorCombat.style(style.build());


    return Arrays.asList(normalCombat.build(), catacombsCombat.build(), cursedCombat.build(),
        superiorCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    usingSpecialAttack = false;
    specialAttackCount = 0;
    specialAttackTile = null;
  }

  @Override
  public void tickStartHook() {
    if ((npc.getId() == NpcId.GREATER_ABYSSAL_DEMON_342
        || npc.getId() == NpcId.CURSED_ABYSSAL_DEMON_342_16010) && npc.isAttacking()
        && !usingSpecialAttack && PRandom.randomE(20) == 0) {
      usingSpecialAttack = true;
      specialAttackTile = new Tile(npc);
    }
  }

  @Override
  public int attackTickAttackSpeedHook(NpcCombatStyle combatStyle) {
    return usingSpecialAttack ? 2 : combatStyle.getAttackSpeed();
  }

  @Override
  public double accuracyHook(NpcCombatStyle combatStyle, double accuracy) {
    return usingSpecialAttack ? Integer.MAX_VALUE : accuracy;
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    if (!usingSpecialAttack) {
      return;
    }
    if (++specialAttackCount >= 4) {
      npc.getMovement().teleport(specialAttackTile);
      usingSpecialAttack = false;
      specialAttackCount = 0;
      specialAttackTile = null;
    } else {
      var tile = new Tile(opponent);
      var tries = 0;
      while (tries++ < 8
          && (tile.matchesTile(opponent) || tile.matchesTile(npc) || !Route.canMove(npc, tile))) {
        tile.setTile(opponent);
        tile = PRandom.randomI(1) == 0 ? tile.randomizeX(1) : tile.randomizeY(1);
      }
      if (!Route.canMove(npc, tile)) {
        tile = npc;
      }
      npc.getMovement().teleport(tile);
    }
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (npc.getArea().matches(CatacombsOfKourendArea.class)) {
      if (npc.getId() == NpcId.GREATER_ABYSSAL_DEMON_342 || TOTEM_DROP_TABLE.canDrop(npc, player)) {
        TOTEM_DROP_TABLE.dropItems(npc, player, dropTile);
      }
      if (SHARD_DROP_TABLE.canDrop(npc, player)) {
        SHARD_DROP_TABLE.dropItems(npc, player, dropTile);
      }
    }
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_ABYSSAL_DEMON_342_16010) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
      if (CURSED_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
        return CURSED_DROP_TABLE;
      }
    }
    if ((npc.getId() == NpcId.GREATER_ABYSSAL_DEMON_342
        || npc.getId() == NpcId.CURSED_ABYSSAL_DEMON_342_16010)
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_ABYSSAL_DEMON_342_16010 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
