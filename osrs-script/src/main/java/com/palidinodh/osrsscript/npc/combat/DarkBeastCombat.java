package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.map.area.CatacombsOfKourendArea;
import com.palidinodh.random.PRandom;
import lombok.var;

public class DarkBeastCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(3.15).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable TOTEM_DROP_TABLE =
      NpcCombatDropTable.builder().chance(0.36).order(NpcCombatDropTable.Order.RANDOM_UNIQUE)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_BASE)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_MIDDLE)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_TOP))).build();
  private static final NpcCombatDropTable SHARD_DROP_TABLE =
      NpcCombatDropTable.builder().chance(0.54)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_SHARD))).build();
  private static final NpcCombatStyle SPECIAL_ATTACK =
      NpcCombatStyle.builder().type(NpcCombatStyleType.MAGIC).damage(NpcCombatDamage.maximum(25))
          .projectile(NpcCombatProjectile.builder().id(130).speedMinimumDistance(8).build())
          .animation(2731).attackSpeed(2).targetTileGraphic(new Graphic(101, 124))
          .specialAttack(NpcCombatTargetTile.builder().radius(1).radiusProjectiles(true).build())
          .build();

  @Inject
  private Npc npc;
  private boolean usingSpecialAttack;
  private int specialAttackCount;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_1200)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_BOW)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_TALISMAN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_10);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_ARROWTIPS, 5, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DART_TIP, 5, 25)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_JAVELIN_HEADS, 1, 10)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 47)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AVANTOE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KWUARM_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CADANTINE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_ORE_NOTED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ATTACK_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 1, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_SQ_SHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 64, 220)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DARK_BEAST_182);
    combat.hitpoints(NpcCombatHitpoints.total(220));
    combat.stats(NpcCombatStats.builder().attackLevel(140).magicLevel(160).defenceLevel(120)
        .bonus(CombatBonus.DEFENCE_STAB, 30).bonus(CombatBonus.DEFENCE_SLASH, 40)
        .bonus(CombatBonus.DEFENCE_CRUSH, 100).bonus(CombatBonus.DEFENCE_MAGIC, 90)
        .bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    combat.slayer(NpcCombatSlayer.builder().level(90).superiorId(NpcId.NIGHT_BEAST_374).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.focus(NpcCombatFocus.builder().meleeUnlessUnreachable(true).build());
    combat.deathAnimation(2733).blockAnimation(2732);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(2731).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(2731).attackSpeed(4);
    style.targetGraphic(new Graphic(131, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var combat2 = NpcCombatDefinition.builder();
    combat2.id(NpcId.DARK_BEAST_182_7250);
    combat2.hitpoints(NpcCombatHitpoints.total(220));
    combat2.stats(NpcCombatStats.builder().attackLevel(140).magicLevel(160).defenceLevel(120)
        .bonus(CombatBonus.DEFENCE_STAB, 30).bonus(CombatBonus.DEFENCE_SLASH, 40)
        .bonus(CombatBonus.DEFENCE_CRUSH, 100).bonus(CombatBonus.DEFENCE_MAGIC, 90)
        .bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    combat2.slayer(NpcCombatSlayer.builder().level(90).superiorId(NpcId.NIGHT_BEAST_374).build());
    combat2.aggression(NpcCombatAggression.PLAYERS);
    combat2.focus(NpcCombatFocus.builder().meleeUnlessUnreachable(true).build());
    combat2.deathAnimation(2733).blockAnimation(2732);
    combat2.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(2731).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(2731).attackSpeed(4);
    style.targetGraphic(new Graphic(101, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat2.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_DARK_BEAST_374_16009);
    cursedCombat
        .hitpoints(NpcCombatHitpoints.builder().total(550).bar(HitpointsBar.GREEN_RED_60).build());
    cursedCombat.stats(NpcCombatStats.builder().attackLevel(270).magicLevel(300).defenceLevel(220)
        .bonus(CombatBonus.DEFENCE_STAB, 75).bonus(CombatBonus.DEFENCE_SLASH, 80)
        .bonus(CombatBonus.DEFENCE_CRUSH, 200).bonus(CombatBonus.DEFENCE_MAGIC, 190)
        .bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(90).build());
    cursedCombat.focus(NpcCombatFocus.builder().meleeUnlessUnreachable(true).build());
    cursedCombat.deathAnimation(2733).blockAnimation(2732);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(32));
    style.animation(2731).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(25));
    style.animation(2731).attackSpeed(4);
    style.targetGraphic(new Graphic(131, 124));
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    var superiorCombat = NpcCombatDefinition.builder();
    superiorCombat.id(NpcId.NIGHT_BEAST_374);
    superiorCombat
        .hitpoints(NpcCombatHitpoints.builder().total(550).bar(HitpointsBar.GREEN_RED_60).build());
    superiorCombat.stats(NpcCombatStats.builder().attackLevel(270).magicLevel(300).defenceLevel(220)
        .bonus(CombatBonus.DEFENCE_STAB, 75).bonus(CombatBonus.DEFENCE_SLASH, 80)
        .bonus(CombatBonus.DEFENCE_CRUSH, 200).bonus(CombatBonus.DEFENCE_MAGIC, 190)
        .bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    superiorCombat.slayer(NpcCombatSlayer.builder().level(90).experience(6462).build());
    superiorCombat.aggression(NpcCombatAggression.PLAYERS);
    superiorCombat.focus(NpcCombatFocus.builder().meleeUnlessUnreachable(true).build());
    superiorCombat
        .killCount(NpcCombatKillCount.builder().asName("Superior Slayer Creature").build());
    superiorCombat.deathAnimation(2733).blockAnimation(2732);
    superiorCombat.drop(drop.rolls(3).build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(32));
    style.animation(2731).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    superiorCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(25));
    style.animation(2731).attackSpeed(4);
    style.targetGraphic(new Graphic(131, 124));
    style.projectile(NpcCombatProjectile.id(335));
    superiorCombat.style(style.build());


    return Arrays.asList(combat.build(), combat2.build(), cursedCombat.build(),
        superiorCombat.build());
  }

  @Override
  public void restoreHook() {
    usingSpecialAttack = false;
    specialAttackCount = 0;
  }

  @Override
  public void tickStartHook() {
    if ((npc.getId() == NpcId.NIGHT_BEAST_374 || npc.getId() == NpcId.CURSED_DARK_BEAST_374_16009)
        && npc.isAttacking() && !usingSpecialAttack && PRandom.randomE(20) == 0) {
      usingSpecialAttack = true;
    }
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    return usingSpecialAttack ? SPECIAL_ATTACK : combatStyle;
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    if (!usingSpecialAttack) {
      return;
    }
    if (++specialAttackCount >= 3) {
      usingSpecialAttack = false;
      specialAttackCount = 0;
    }
  }

  @Override
  public double accuracyHook(NpcCombatStyle combatStyle, double accuracy) {
    return usingSpecialAttack ? Integer.MAX_VALUE : accuracy;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (npc.getArea().matches(CatacombsOfKourendArea.class)) {
      if (npc.getId() == NpcId.NIGHT_BEAST_374 || TOTEM_DROP_TABLE.canDrop(npc, player)) {
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
    if (npc.getId() == NpcId.CURSED_DARK_BEAST_374_16009) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
    }
    if ((npc.getId() == NpcId.NIGHT_BEAST_374 || npc.getId() == NpcId.CURSED_DARK_BEAST_374_16009)
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_DARK_BEAST_374_16009 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
