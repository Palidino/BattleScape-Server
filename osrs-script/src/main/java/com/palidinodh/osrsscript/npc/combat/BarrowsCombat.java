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
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.PMovement;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.random.PRandom;
import lombok.var;

class BarrowsCombat extends NpcCombat {
  private static final NpcCombatStyle AHRIM_STUN = NpcCombatStyle.builder()
      .type(NpcCombatStyleType.MAGIC)
      .damage(NpcCombatDamage.builder().maximum(0).ignorePrayer(true).splashOnMiss(true).build())
      .animation(729).attackSpeed(5).castGraphic(new Graphic(173, 92))
      .targetGraphic(new Graphic(254, 124)).projectile(NpcCombatProjectile.id(174)).build();
  private static final NpcCombatStyle AHRIM_ENFEEBLE = NpcCombatStyle.builder()
      .type(NpcCombatStyleType.MAGIC)
      .damage(NpcCombatDamage.builder().maximum(0).ignorePrayer(true).splashOnMiss(true).build())
      .animation(729).attackSpeed(5).castGraphic(new Graphic(170, 92))
      .targetGraphic(new Graphic(172, 124)).projectile(NpcCombatProjectile.id(171)).build();
  private static final NpcCombatStyle AHRIM_VULNERABILITY = NpcCombatStyle.builder()
      .type(NpcCombatStyleType.MAGIC)
      .damage(NpcCombatDamage.builder().maximum(0).ignorePrayer(true).splashOnMiss(true).build())
      .animation(729).attackSpeed(5).castGraphic(new Graphic(167, 92))
      .targetGraphic(new Graphic(169, 124)).projectile(NpcCombatProjectile.id(168)).build();

  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable = NpcCombatDropTable.builder().chance(0.8);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SCIMITAR)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_SQ_SHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_ARROW, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LOBSTER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STRENGTH_POTION_2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_BAR)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var ahrim = NpcCombatDefinition.builder();
    ahrim.id(NpcId.AHRIM_THE_BLIGHTED_98);
    ahrim.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    ahrim.hitpoints(NpcCombatHitpoints.total(100));
    ahrim.stats(NpcCombatStats.builder().magicLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_STAB, 12).bonus(CombatBonus.ATTACK_CRUSH, 65)
        .bonus(CombatBonus.ATTACK_MAGIC, 73).bonus(CombatBonus.DEFENCE_STAB, 103)
        .bonus(CombatBonus.DEFENCE_SLASH, 85).bonus(CombatBonus.DEFENCE_CRUSH, 117)
        .bonus(CombatBonus.DEFENCE_MAGIC, 73).build());
    ahrim.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    ahrim.killCount(NpcCombatKillCount.builder().asName("Barrows brother").build());
    ahrim.deathAnimation(836).blockAnimation(415);
    ahrim.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(25).splashOnMiss(true).build());
    style.animation(727).attackSpeed(5);
    style.castGraphic(new Graphic(155, 92)).targetGraphic(new Graphic(157, 124));
    style.projectile(NpcCombatProjectile.id(335));
    ahrim.style(style.build());


    var dharok = NpcCombatDefinition.builder();
    dharok.id(NpcId.DHAROK_THE_WRETCHED_115);
    dharok.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    dharok.hitpoints(NpcCombatHitpoints.total(100));
    dharok.stats(NpcCombatStats.builder().attackLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_SLASH, 103).bonus(CombatBonus.ATTACK_CRUSH, 95)
        .bonus(CombatBonus.DEFENCE_STAB, 252).bonus(CombatBonus.DEFENCE_SLASH, 250)
        .bonus(CombatBonus.DEFENCE_CRUSH, 244).bonus(CombatBonus.DEFENCE_MAGIC, -11)
        .bonus(CombatBonus.DEFENCE_RANGED, 249).build());
    dharok.killCount(NpcCombatKillCount.builder().asName("Barrows brother").build());
    dharok.deathAnimation(836).blockAnimation(424);
    dharok.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(29));
    style.animation(2066).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    dharok.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(29));
    style.animation(2067).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    dharok.style(style.build());


    var guthan = NpcCombatDefinition.builder();
    guthan.id(NpcId.GUTHAN_THE_INFESTED_115);
    guthan.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    guthan.hitpoints(NpcCombatHitpoints.total(100));
    guthan.stats(NpcCombatStats.builder().attackLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_STAB, 75).bonus(CombatBonus.ATTACK_SLASH, 75)
        .bonus(CombatBonus.ATTACK_CRUSH, 75).bonus(CombatBonus.DEFENCE_STAB, 259)
        .bonus(CombatBonus.DEFENCE_SLASH, 257).bonus(CombatBonus.DEFENCE_CRUSH, 241)
        .bonus(CombatBonus.DEFENCE_MAGIC, -11).bonus(CombatBonus.DEFENCE_RANGED, 250).build());
    guthan.killCount(NpcCombatKillCount.builder().asName("Barrows brother").build());
    guthan.deathAnimation(836).blockAnimation(435);
    guthan.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(24));
    style.animation(2080).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    guthan.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(24));
    style.animation(2081).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    guthan.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(24));
    style.animation(2082).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    guthan.style(style.build());


    var karil = NpcCombatDefinition.builder();
    karil.id(NpcId.KARIL_THE_TAINTED_98);
    karil.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    karil.hitpoints(NpcCombatHitpoints.total(100));
    karil.stats(NpcCombatStats.builder().rangedLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_RANGED, 134).bonus(CombatBonus.DEFENCE_STAB, 79)
        .bonus(CombatBonus.DEFENCE_SLASH, 71).bonus(CombatBonus.DEFENCE_CRUSH, 90)
        .bonus(CombatBonus.DEFENCE_MAGIC, 106).bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    karil.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    karil.killCount(NpcCombatKillCount.builder().asName("Barrows brother").build());
    karil.deathAnimation(836).blockAnimation(424);
    karil.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(2075).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    karil.style(style.build());


    var torag = NpcCombatDefinition.builder();
    torag.id(NpcId.TORAG_THE_CORRUPTED_115);
    torag.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    torag.hitpoints(NpcCombatHitpoints.total(100));
    torag.stats(NpcCombatStats.builder().attackLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_STAB, 68).bonus(CombatBonus.ATTACK_CRUSH, 82)
        .bonus(CombatBonus.DEFENCE_STAB, 221).bonus(CombatBonus.DEFENCE_SLASH, 235)
        .bonus(CombatBonus.DEFENCE_CRUSH, 222).bonus(CombatBonus.DEFENCE_RANGED, 221).build());
    torag.killCount(NpcCombatKillCount.builder().asName("Barrows brother").build());
    torag.deathAnimation(836).blockAnimation(424);
    torag.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(2068).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    torag.style(style.build());


    var verac = NpcCombatDefinition.builder();
    verac.id(NpcId.VERAC_THE_DEFILED_115);
    verac.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    verac.hitpoints(NpcCombatHitpoints.total(100));
    verac.stats(NpcCombatStats.builder().attackLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_STAB, 68).bonus(CombatBonus.ATTACK_CRUSH, 82)
        .bonus(CombatBonus.DEFENCE_STAB, 227).bonus(CombatBonus.DEFENCE_SLASH, 230)
        .bonus(CombatBonus.DEFENCE_CRUSH, 221).bonus(CombatBonus.DEFENCE_RANGED, 225).build());
    verac.killCount(NpcCombatKillCount.builder().asName("Barrows brother").build());
    verac.deathAnimation(836).blockAnimation(2063);
    verac.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(2062).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    verac.style(style.build());


    return Arrays.asList(ahrim.build(), dharok.build(), guthan.build(), karil.build(),
        torag.build(), verac.build());
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (!(opponent instanceof Player)) {
      return combatStyle;
    }
    var player = (Player) opponent;
    if (npc.getId() == NpcId.AHRIM_THE_BLIGHTED_98 && PRandom.randomE(5) == 0) {
      var type = PRandom.randomE(3);
      NpcCombatStyle attemptedStyle = null;
      var skillId = 0;
      if (type == 0) {
        attemptedStyle = AHRIM_STUN;
        skillId = Skills.ATTACK;
      } else if (type == 1) {
        attemptedStyle = AHRIM_ENFEEBLE;
        skillId = Skills.STRENGTH;
      } else if (type == 2) {
        attemptedStyle = AHRIM_VULNERABILITY;
        skillId = Skills.DEFENCE;
      }
      var currentLevel = player.getSkills().getLevel(skillId);
      var maxLevel = player.getSkills().getLevel(skillId);
      var maxReducedLevel = (int) (maxLevel * 0.9);
      if (currentLevel > maxReducedLevel) {
        if (attemptedStyle != null) {
          return attemptedStyle;
        }
      }
    }
    return combatStyle;
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    if (!(opponent instanceof Player)) {
      return;
    }
    var player = (Player) opponent;
    if (npc.getId() == NpcId.AHRIM_THE_BLIGHTED_98) {
      if (PRandom.randomE(4) == 0) {
        player.getSkills().changeStat(Skills.STRENGTH, -5);
        player.setGraphic(400, 100);
      }
      if (combatStyle != AHRIM_STUN && combatStyle != AHRIM_ENFEEBLE
          && combatStyle != AHRIM_VULNERABILITY || hitEvent.getDefenceBlocked()) {
        return;
      }
      var skillId = 0;
      if (combatStyle == AHRIM_STUN) {
        skillId = Skills.ATTACK;
      } else if (combatStyle == AHRIM_ENFEEBLE) {
        skillId = Skills.STRENGTH;
      } else if (combatStyle == AHRIM_VULNERABILITY) {
        skillId = Skills.DEFENCE;
      }
      var currentLevel = player.getSkills().getLevel(skillId);
      var maxLevel = player.getSkills().getLevel(skillId);
      var reducedLevel = (int) (currentLevel * 0.9);
      var maxReducedLevel = (int) (maxLevel * 0.9);
      if (reducedLevel < maxReducedLevel) {
        reducedLevel = maxReducedLevel;
      }
      if (currentLevel != reducedLevel) {
        player.getSkills().changeStat(skillId, -(currentLevel - reducedLevel));
      }
    }
  }

  @Override
  public void attackedActionsOpponentHook(Entity opponent) {
    if (!npc.getController().inWilderness() || !(opponent instanceof Player)) {
      return;
    }
    var player = (Player) opponent;
    player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (npc.getController().inWilderness()) {
      if (player.getSkills().isWildernessSlayerTask(npc)) {
        npc.getController().addMapItem(new Item(ItemId.SINISTER_KEY_NOTED, 1), dropTile, player);
      } else {
        npc.getController().addMapItem(new Item(ItemId.SINISTER_KEY, 1), dropTile, player);
      }
    } else {
      player.getCombat().getBarrows().setKilled(npc.getId());
    }
  }

  @Override
  public boolean dropTableCanDropHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    return npc.getController().inWilderness();
  }

  @Override
  public double damageInflictedHook(NpcCombatStyle combatStyle, Entity opponent, double damage) {
    if (!(opponent instanceof Player)) {
      return damage;
    }
    var player = (Player) opponent;
    if (npc.getId() == NpcId.DHAROK_THE_WRETCHED_115) {
      damage *= 1.0
          + (npc.getMaxHitpoints() - npc.getHitpoints()) / 100.0 * (npc.getMaxHitpoints() / 100.0);
    } else if (npc.getId() == NpcId.GUTHAN_THE_INFESTED_115 && PRandom.randomE(4) == 0) {
      opponent.setGraphic(398);
      if (damage > 0) {
        npc.adjustHitpoints((int) damage, 0);
      }
    } else if (npc.getId() == NpcId.KARIL_THE_TAINTED_98 && PRandom.randomE(4) == 0) {
      player.getSkills().changeStat(Skills.AGILITY,
          (int) -(player.getSkills().getLevel(Skills.AGILITY) * 0.2));
      opponent.setGraphic(401, 100);
    } else if (npc.getId() == NpcId.TORAG_THE_CORRUPTED_115 && damage > 0
        && PRandom.randomE(4) == 0) {
      player.getMovement()
          .setEnergy((int) (player.getMovement().getEnergy() - PMovement.MAX_ENERGY * 0.2));
      opponent.setGraphic(399, 100);
    } else if (npc.getId() == NpcId.VERAC_THE_DEFILED_115 && PRandom.randomE(4) == 0) {
      damage = PRandom.randomI(23) + 1;
      opponent.setGraphic(1041);
    }
    return damage;
  }
}
