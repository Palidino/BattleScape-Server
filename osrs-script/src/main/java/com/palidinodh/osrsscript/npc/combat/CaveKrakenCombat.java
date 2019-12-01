package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class CaveKrakenCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(3.15).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable CURSED_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(NpcCombatDropTable.CHANCE_1_IN_500).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KRAKEN_TENTACLE).weight(10)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TRIDENT_OF_THE_SEAS_FULL).weight(8)))
      .build();

  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true)
        .rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_1200)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_100);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1200).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KRAKEN_TENTACLE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_200).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCHARGED_TRIDENT)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_SPEAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_WATER_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BELLADONNA_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AVANTOE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KWUARM_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LANTADYME_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CADANTINE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POISON_IVY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CACTUS_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 123, 19770)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCKET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_LOBSTER_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_ORB_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OYSTER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SWORDFISH, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIDOTE_PLUS_PLUS_4_NOTED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.VIAL_OF_WATER_NOTED, 50)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STAFF_OF_WATER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEAM_RUNE, 7)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OLD_BOOT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SEAWEED_NOTED, 30)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.WHIRLPOOL_127).id(NpcId.CAVE_KRAKEN_127);
    combat.noclip(true);
    combat.hitpoints(NpcCombatHitpoints.total(125));
    combat.stats(NpcCombatStats.builder().magicLevel(120).defenceLevel(150)
        .bonus(CombatBonus.DEFENCE_MAGIC, -63).bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    combat.slayer(NpcCombatSlayer.builder().level(87).taskOnly(true).build());
    combat.immunity(NpcCombatImmunity.builder().melee(true).ranged(true).build());
    combat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    combat.deathAnimation(3993).blockAnimation(3990);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(13).splashOnMiss(true).build());
    style.animation(3991).attackSpeed(6);
    style.targetGraphic(new Graphic(163, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_WHIRLPOOL_127_16014).id(NpcId.CURSED_KRAKEN_127_16013);
    cursedCombat.noclip(true);
    cursedCombat.hitpoints(NpcCombatHitpoints.total(200));
    cursedCombat.stats(NpcCombatStats.builder().magicLevel(120).defenceLevel(150)
        .bonus(CombatBonus.DEFENCE_MAGIC, -63).bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(87).build());
    combat.immunity(NpcCombatImmunity.builder().melee(true).ranged(true).build());
    cursedCombat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    cursedCombat.deathAnimation(3993).blockAnimation(3990);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(15).splashOnMiss(true).build());
    style.animation(3991).attackSpeed(6);
    style.targetGraphic(new Graphic(163, 124));
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    return Arrays.asList(combat.build(), cursedCombat.build());
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (npc.getId() == NpcId.WHIRLPOOL_127 || npc.getId() == NpcId.CAVE_KRAKEN_127) {
      if (npc.getInCombatDelay() == 0 && !npc.isAttacking() && npc.getId() != NpcId.WHIRLPOOL_127) {
        npc.setTransformationId(NpcId.WHIRLPOOL_127);
      } else if (npc.getInCombatDelay() > 0 || npc.isAttacking()) {
        npc.getMovement().clear();
        if (npc.getId() != NpcId.CAVE_KRAKEN_127) {
          npc.setTransformationId(NpcId.CAVE_KRAKEN_127);
          npc.setAnimation(7135);
          npc.setHitDelay(4);
        }
      }
    } else if (npc.getId() == NpcId.CURSED_WHIRLPOOL_127_16014
        || npc.getId() == NpcId.CURSED_KRAKEN_127_16013) {
      if (npc.getInCombatDelay() == 0 && !npc.isAttacking()
          && npc.getId() != NpcId.CURSED_WHIRLPOOL_127_16014) {
        npc.setTransformationId(NpcId.CURSED_WHIRLPOOL_127_16014);
      } else if (npc.getInCombatDelay() > 0 || npc.isAttacking()) {
        npc.getMovement().clear();
        if (npc.getId() != NpcId.CURSED_KRAKEN_127_16013) {
          npc.setTransformationId(NpcId.CURSED_KRAKEN_127_16013);
          npc.setAnimation(7135);
          npc.setHitDelay(4);
        }
      }
    }
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_KRAKEN_127_16013) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
      if (CURSED_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
        return CURSED_DROP_TABLE;
      }
    }
    if (npc.getId() == NpcId.CURSED_KRAKEN_127_16013
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_KRAKEN_127_16013 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
