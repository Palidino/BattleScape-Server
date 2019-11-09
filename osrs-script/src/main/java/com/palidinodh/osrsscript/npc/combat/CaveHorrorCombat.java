package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
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
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class CaveHorrorCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(1.03).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();

  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_MASK_10)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(3.34);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_HORROR_HEAD_13487)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LANTADYME_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED, 1, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KWUARM_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POISON_IVY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CACTUS_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CADANTINE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STRAWBERRY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SWEETCORN_SEED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AVANTOE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOMATO_SEED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_LOGS_NOTED, 2, 4)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BELLADONNA_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CABBAGE_SEED, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTATO_SEED, 1, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONION_SEED, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 3, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 44, 495)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LIMPWURT_ROOT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TEAK_LOGS_NOTED, 4)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CAVE_HORROR_80);
    combat.hitpoints(NpcCombatHitpoints.total(55));
    combat.stats(NpcCombatStats.builder().attackLevel(80).magicLevel(80).defenceLevel(62).build());
    combat
        .slayer(NpcCombatSlayer.builder().level(58).superiorId(NpcId.CAVE_ABOMINATION_206).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(4233).blockAnimation(4232);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MELEE).subHitType(HitType.MAGIC).build());
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(4234).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_CAVE_HORROR_206_16005);
    cursedCombat
        .hitpoints(NpcCombatHitpoints.builder().total(130).bar(HitpointsBar.GREEN_RED_60).build());
    cursedCombat
        .stats(NpcCombatStats.builder().attackLevel(230).magicLevel(230).defenceLevel(142).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(58).build());
    cursedCombat.deathAnimation(4233).blockAnimation(4232);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MELEE).subHitType(HitType.MAGIC).build());
    style.damage(NpcCombatDamage.maximum(24));
    style.animation(4234).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    var superiorCombat = NpcCombatDefinition.builder();
    superiorCombat.id(NpcId.CAVE_ABOMINATION_206);
    superiorCombat
        .hitpoints(NpcCombatHitpoints.builder().total(130).bar(HitpointsBar.GREEN_RED_60).build());
    superiorCombat
        .stats(NpcCombatStats.builder().attackLevel(230).magicLevel(230).defenceLevel(142).build());
    superiorCombat.slayer(NpcCombatSlayer.builder().level(58).experience(1300).build());
    superiorCombat.aggression(NpcCombatAggression.PLAYERS);
    superiorCombat
        .killCount(NpcCombatKillCount.builder().asName("Superior Slayer Creature").build());
    superiorCombat.deathAnimation(4233).blockAnimation(4232);
    superiorCombat.drop(drop.rolls(3).build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MELEE).subHitType(HitType.MAGIC).build());
    style.damage(NpcCombatDamage.maximum(24));
    style.animation(4234).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    superiorCombat.style(style.build());


    return Arrays.asList(combat.build(), cursedCombat.build(), superiorCombat.build());
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_CAVE_HORROR_206_16005) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
    }
    if ((npc.getId() == NpcId.CAVE_ABOMINATION_206
        || npc.getId() == NpcId.CURSED_CAVE_HORROR_206_16005)
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_CAVE_HORROR_206_16005 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
