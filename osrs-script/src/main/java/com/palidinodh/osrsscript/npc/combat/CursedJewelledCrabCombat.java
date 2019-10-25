package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class CursedJewelledCrabCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(3.15).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable CURSED_DROP_TABLE =
      NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BERSERKER_RING).weight(1)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WARRIOR_RING).weight(1)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARCHERS_RING).weight(1)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SEERS_RING).weight(1)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_AXE).weight(4))).build();

  private Npc npc;
  private int changeDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.MEDIUM, NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_RUNE, 1, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_RUNE, 1, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BODY_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRIT_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AVANTOE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CADANTINE_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LANTADYME_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DWARF_WEED_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_SPLINTER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_CHUNK)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_SHARD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_CLAWS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_GLOVES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POISON_IVY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BELLADONNA_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GUAM_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MARRENTILL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TARROMIN_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HARRALANDER_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_LOBSTER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_BASS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TINDERBOX)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CURSED_JEWELLED_CRAB_180_16001);
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(255).bar(HitpointsBar.GREEN_RED_60).build());
    combat.stats(NpcCombatStats.builder().attackLevel(127).defenceLevel(127)
        .bonus(CombatBonus.MELEE_DEFENCE, 127).bonus(CombatBonus.DEFENCE_MAGIC, 5)
        .bonus(CombatBonus.DEFENCE_RANGED, 127).build());
    combat.killCount(NpcCombatKillCount.builder().asName("Cursed jewelled crab").build());
    combat.deathAnimation(1314).blockAnimation(1313);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(1312).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var redCombat = NpcCombatDefinition.builder();
    redCombat.id(NpcId.CURSED_JEWELLED_CRAB_RED_180_16002);
    redCombat
        .hitpoints(NpcCombatHitpoints.builder().total(255).bar(HitpointsBar.GREEN_RED_60).build());
    redCombat.stats(NpcCombatStats.builder().attackLevel(127).defenceLevel(127)
        .bonus(CombatBonus.MELEE_DEFENCE, 127).bonus(CombatBonus.DEFENCE_MAGIC, 5)
        .bonus(CombatBonus.DEFENCE_RANGED, 127).build());
    redCombat.killCount(NpcCombatKillCount.builder().asName("Cursed jewelled crab").build());
    redCombat.deathAnimation(1314).blockAnimation(1313);
    redCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(1312).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    redCombat.style(style.build());


    var greenCombat = NpcCombatDefinition.builder();
    greenCombat.id(NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003);
    greenCombat
        .hitpoints(NpcCombatHitpoints.builder().total(255).bar(HitpointsBar.GREEN_RED_60).build());
    greenCombat.stats(NpcCombatStats.builder().attackLevel(127).defenceLevel(127)
        .bonus(CombatBonus.MELEE_DEFENCE, 127).bonus(CombatBonus.DEFENCE_MAGIC, 5)
        .bonus(CombatBonus.DEFENCE_RANGED, 127).build());
    greenCombat.killCount(NpcCombatKillCount.builder().asName("Cursed jewelled crab").build());
    greenCombat.deathAnimation(1314).blockAnimation(1313);
    greenCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(1312).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    greenCombat.style(style.build());


    var blueCombat = NpcCombatDefinition.builder();
    blueCombat.id(NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004);
    blueCombat
        .hitpoints(NpcCombatHitpoints.builder().total(255).bar(HitpointsBar.GREEN_RED_60).build());
    blueCombat.stats(NpcCombatStats.builder().attackLevel(127).defenceLevel(127)
        .bonus(CombatBonus.MELEE_DEFENCE, 127).bonus(CombatBonus.DEFENCE_MAGIC, 5)
        .bonus(CombatBonus.DEFENCE_RANGED, 127).build());
    blueCombat.killCount(NpcCombatKillCount.builder().asName("Cursed jewelled crab").build());
    blueCombat.deathAnimation(1314).blockAnimation(1313);
    blueCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(1312).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    blueCombat.style(style.build());


    return Arrays.asList(combat.build(), redCombat.build(), greenCombat.build(),
        blueCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public Object script(String name, Object... args) {
    if (name.equals("smash")) {
      if (npc.getId() != NpcId.CURSED_JEWELLED_CRAB_180_16001) {
        npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_180_16001);
        setChangeDelay();
      }
    }
    return null;
  }

  @Override
  public void restoreHook() {
    changeDelay = 1;
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (changeDelay > 0) {
      changeDelay--;
      if (changeDelay == 0) {
        setChangeDelay();
        npc.setTransformationId(PRandom.arrayRandom(NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004,
            NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003, NpcId.CURSED_JEWELLED_CRAB_RED_180_16002));
      }
    }
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (npc.getId() == NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004 && hitType == HitType.MAGIC) {
      return 0;
    }
    if (npc.getId() == NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003 && hitType == HitType.RANGED) {
      return 0;
    }
    if (npc.getId() == NpcId.CURSED_JEWELLED_CRAB_RED_180_16002 && hitType == HitType.MELEE) {
      return 0;
    }
    if (npc.getId() != NpcId.CURSED_JEWELLED_CRAB_180_16001) {
      if (hitType == HitType.MAGIC) {
        npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004);
        setChangeDelay();
      } else if (hitType == HitType.RANGED) {
        npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003);
        setChangeDelay();
      } else if (hitType == HitType.MELEE) {
        npc.setTransformationId(NpcId.CURSED_JEWELLED_CRAB_RED_180_16002);
        setChangeDelay();
      }
    }
    return damage;
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (isCursed()) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
      if (CURSED_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
        return CURSED_DROP_TABLE;
      }
    }
    if (isCursed() && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (isCursed() && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }

  private void setChangeDelay() {
    changeDelay = 25 + PRandom.randomI(4);
  }

  private boolean isCursed() {
    return npc.getId() == NpcId.CURSED_JEWELLED_CRAB_180_16001
        || npc.getId() == NpcId.CURSED_JEWELLED_CRAB_RED_180_16002
        || npc.getId() == NpcId.CURSED_JEWELLED_CRAB_GREEN_180_16003
        || npc.getId() == NpcId.CURSED_JEWELLED_CRAB_BLUE_180_16004;
  }
}
