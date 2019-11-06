package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import lombok.var;

public class RevenantCombat extends NpcCombat {
  private static final RandomItem[] ANCIENT_WARRIOR_DROP_TABLE = {
      new RandomItem(ItemId.VESTAS_LONGSWORD, 1), new RandomItem(ItemId.STATIUSS_WARHAMMER, 1),
      new RandomItem(ItemId.VESTAS_SPEAR, 1), new RandomItem(ItemId.MORRIGANS_JAVELIN, 100),
      new RandomItem(ItemId.MORRIGANS_THROWING_AXE, 100), new RandomItem(ItemId.ZURIELS_STAFF, 1)};
  private static final RandomItem[] UNIQUE_DROP_TABLE =
      {new RandomItem(ItemId.VIGGORAS_CHAINMACE_U, 1).weight(1),
          new RandomItem(ItemId.CRAWS_BOW_U, 1).weight(1),
          new RandomItem(ItemId.THAMMARONS_SCEPTRE_U, 1).weight(1),
          new RandomItem(ItemId.AMULET_OF_AVARICE, 1).weight(2)};
  private static final RandomItem[] MEDIOCRE_DROP_TABLE =
      {new RandomItem(ItemId.DRAGON_PLATELEGS, 1).weight(1),
          new RandomItem(ItemId.DRAGON_PLATESKIRT, 1).weight(1),
          new RandomItem(ItemId.RUNE_FULL_HELM, 1).weight(2),
          new RandomItem(ItemId.RUNE_PLATEBODY, 1).weight(2),
          new RandomItem(ItemId.RUNE_PLATELEGS, 1).weight(2),
          new RandomItem(ItemId.RUNE_KITESHIELD, 1).weight(2),
          new RandomItem(ItemId.RUNE_WARHAMMER, 1).weight(2),
          new RandomItem(ItemId.DRAGON_LONGSWORD, 1).weight(1),
          new RandomItem(ItemId.DRAGON_DAGGER, 1).weight(1),
          new RandomItem(ItemId.SUPER_RESTORE_4_NOTED, 3, 5).weight(4),
          new RandomItem(ItemId.ONYX_BOLT_TIPS, 5, 10).weight(4),
          new RandomItem(ItemId.DRAGONSTONE_BOLT_TIPS, 40, 70).weight(4),
          new RandomItem(ItemId.UNCUT_DRAGONSTONE_NOTED, 5, 7).weight(1),
          new RandomItem(ItemId.DEATH_RUNE, 60, 100).weight(3),
          new RandomItem(ItemId.BLOOD_RUNE, 60, 100).weight(3),
          new RandomItem(ItemId.LAW_RUNE, 80, 120).weight(3),
          new RandomItem(ItemId.RUNITE_ORE_NOTED, 3, 6).weight(6),
          new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 8, 12).weight(6),
          new RandomItem(ItemId.COAL_NOTED, 50, 100).weight(6),
          new RandomItem(ItemId.BATTLESTAFF_NOTED, 3).weight(5),
          new RandomItem(ItemId.BLACK_DRAGONHIDE_NOTED, 10, 15).weight(6),
          new RandomItem(ItemId.MAHOGANY_PLANK_NOTED, 15, 25).weight(5),
          new RandomItem(ItemId.MAGIC_LOGS_NOTED, 15, 25).weight(2),
          new RandomItem(ItemId.YEW_LOGS_NOTED, 60, 100).weight(3),
          new RandomItem(ItemId.MANTA_RAY_NOTED, 30, 50).weight(3),
          new RandomItem(ItemId.RUNITE_BAR_NOTED, 3, 5).weight(6),
          new RandomItem(ItemId.REVENANT_CAVE_TELEPORT, 1).weight(7),
          new RandomItem(ItemId.BRACELET_OF_ETHEREUM_UNCHARGED, 1).weight(30)};

  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var impCombat = NpcCombatDefinition.builder();
    impCombat.id(NpcId.REVENANT_IMP_7);
    impCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    impCombat.hitpoints(NpcCombatHitpoints.total(10));
    impCombat
        .stats(NpcCombatStats.builder().attackLevel(5).magicLevel(9).rangedLevel(5).defenceLevel(4)
            .bonus(CombatBonus.ATTACK_MAGIC, 5).bonus(CombatBonus.DEFENCE_MAGIC, 5).build());
    impCombat.aggression(NpcCombatAggression.builder().always(true).build());
    impCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    impCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    impCombat.type(NpcCombatType.UNDEAD).deathAnimation(172).blockAnimation(170);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(169).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    impCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(173).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    impCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(2).splashOnMiss(true).build());
    style.animation(173).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    impCombat.style(style.build());


    var goblinCombat = NpcCombatDefinition.builder();
    goblinCombat.id(NpcId.REVENANT_GOBLIN_15);
    goblinCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    goblinCombat.hitpoints(NpcCombatHitpoints.total(14));
    goblinCombat.stats(NpcCombatStats.builder().attackLevel(13).magicLevel(12).rangedLevel(15)
        .defenceLevel(14).bonus(CombatBonus.MELEE_ATTACK, 6).bonus(CombatBonus.ATTACK_MAGIC, 37)
        .bonus(CombatBonus.ATTACK_RANGED, 21).bonus(CombatBonus.DEFENCE_STAB, 25)
        .bonus(CombatBonus.DEFENCE_SLASH, 28).bonus(CombatBonus.DEFENCE_CRUSH, 31)
        .bonus(CombatBonus.DEFENCE_MAGIC, 1).bonus(CombatBonus.DEFENCE_RANGED, 31).build());
    goblinCombat.aggression(NpcCombatAggression.builder().always(true).build());
    goblinCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    goblinCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    goblinCombat.type(NpcCombatType.UNDEAD).deathAnimation(6182).blockAnimation(6183);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(3));
    style.animation(6185).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    goblinCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(3));
    style.animation(6184).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    goblinCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(3).splashOnMiss(true).build());
    style.animation(6184).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    goblinCombat.style(style.build());


    var pyrefiendCombat = NpcCombatDefinition.builder();
    pyrefiendCombat.id(NpcId.REVENANT_PYREFIEND_52);
    pyrefiendCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    pyrefiendCombat.hitpoints(NpcCombatHitpoints.total(48));
    pyrefiendCombat.stats(NpcCombatStats.builder().attackLevel(60).magicLevel(67).rangedLevel(40)
        .defenceLevel(33).bonus(CombatBonus.DEFENCE_STAB, 45).bonus(CombatBonus.DEFENCE_SLASH, 40)
        .bonus(CombatBonus.DEFENCE_CRUSH, 50).bonus(CombatBonus.DEFENCE_MAGIC, 15)
        .bonus(CombatBonus.DEFENCE_RANGED, 10).build());
    pyrefiendCombat.aggression(NpcCombatAggression.builder().always(true).build());
    pyrefiendCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    pyrefiendCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    pyrefiendCombat.type(NpcCombatType.UNDEAD).deathAnimation(1580).blockAnimation(1581);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(1582).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    pyrefiendCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(1582).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    pyrefiendCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(8).splashOnMiss(true).build());
    style.animation(1582).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    pyrefiendCombat.style(style.build());


    var hobgoblinCombat = NpcCombatDefinition.builder();
    hobgoblinCombat.id(NpcId.REVENANT_HOBGOBLIN_60);
    hobgoblinCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    hobgoblinCombat.hitpoints(NpcCombatHitpoints.total(72));
    hobgoblinCombat.stats(NpcCombatStats.builder().attackLevel(50).magicLevel(55).rangedLevel(60)
        .defenceLevel(41).bonus(CombatBonus.MELEE_ATTACK, 20).bonus(CombatBonus.ATTACK_MAGIC, 5)
        .bonus(CombatBonus.ATTACK_RANGED, 25).bonus(CombatBonus.DEFENCE_STAB, 65)
        .bonus(CombatBonus.DEFENCE_SLASH, 60).bonus(CombatBonus.DEFENCE_CRUSH, 68)
        .bonus(CombatBonus.DEFENCE_MAGIC, 30).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    hobgoblinCombat.aggression(NpcCombatAggression.builder().always(true).build());
    hobgoblinCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    hobgoblinCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    hobgoblinCombat.type(NpcCombatType.UNDEAD).deathAnimation(167).blockAnimation(165);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(164).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    hobgoblinCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(163).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    hobgoblinCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(8).splashOnMiss(true).build());
    style.animation(163).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    hobgoblinCombat.style(style.build());


    var cyclopsCombat = NpcCombatDefinition.builder();
    cyclopsCombat.id(NpcId.REVENANT_CYCLOPS_82);
    cyclopsCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    cyclopsCombat.hitpoints(NpcCombatHitpoints.total(44));
    cyclopsCombat.stats(NpcCombatStats.builder().attackLevel(60).magicLevel(65).rangedLevel(70)
        .defenceLevel(49).bonus(CombatBonus.MELEE_ATTACK, 53).bonus(CombatBonus.DEFENCE_STAB, 140)
        .bonus(CombatBonus.DEFENCE_SLASH, 130).bonus(CombatBonus.DEFENCE_CRUSH, 135)
        .bonus(CombatBonus.DEFENCE_MAGIC, 10).bonus(CombatBonus.DEFENCE_RANGED, 135).build());
    cyclopsCombat.aggression(NpcCombatAggression.builder().always(true).build());
    cyclopsCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    cyclopsCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    cyclopsCombat.type(NpcCombatType.UNDEAD).deathAnimation(4653).blockAnimation(4651);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(4652).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    cyclopsCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(4652).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    cyclopsCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(16).splashOnMiss(true).build());
    style.animation(4652).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    cyclopsCombat.style(style.build());


    var hellhoundCombat = NpcCombatDefinition.builder();
    hellhoundCombat.id(NpcId.REVENANT_HELLHOUND_90);
    hellhoundCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    hellhoundCombat.hitpoints(NpcCombatHitpoints.total(80));
    hellhoundCombat.stats(NpcCombatStats.builder().attackLevel(76).magicLevel(104).rangedLevel(80)
        .defenceLevel(80).bonus(CombatBonus.MELEE_ATTACK, 38).bonus(CombatBonus.ATTACK_MAGIC, 30)
        .bonus(CombatBonus.DEFENCE_STAB, 138).bonus(CombatBonus.DEFENCE_SLASH, 140)
        .bonus(CombatBonus.DEFENCE_CRUSH, 142).bonus(CombatBonus.DEFENCE_MAGIC, 62)
        .bonus(CombatBonus.DEFENCE_RANGED, 140).build());
    hellhoundCombat.aggression(NpcCombatAggression.builder().always(true).build());
    hellhoundCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    hellhoundCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    hellhoundCombat.type(NpcCombatType.UNDEAD).deathAnimation(6576).blockAnimation(6578);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(14));
    style.animation(6581).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    hellhoundCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(14));
    style.animation(6579).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    hellhoundCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(14).splashOnMiss(true).build());
    style.animation(6579).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    hellhoundCombat.style(style.build());


    var demonCombat = NpcCombatDefinition.builder();
    demonCombat.id(NpcId.REVENANT_DEMON_98);
    demonCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    demonCombat.hitpoints(NpcCombatHitpoints.total(80));
    demonCombat.stats(NpcCombatStats.builder().attackLevel(83).magicLevel(120).rangedLevel(80)
        .defenceLevel(80).bonus(CombatBonus.MELEE_ATTACK, 30).bonus(CombatBonus.ATTACK_MAGIC, 50)
        .bonus(CombatBonus.ATTACK_RANGED, 40).bonus(CombatBonus.DEFENCE_STAB, 124)
        .bonus(CombatBonus.DEFENCE_SLASH, 118).bonus(CombatBonus.DEFENCE_CRUSH, 130)
        .bonus(CombatBonus.DEFENCE_MAGIC, 85).bonus(CombatBonus.DEFENCE_RANGED, 90).build());
    demonCombat.aggression(NpcCombatAggression.builder().always(true).build());
    demonCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    demonCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    demonCombat.type(NpcCombatType.UNDEAD).type(NpcCombatType.DEMON).deathAnimation(67)
        .blockAnimation(65);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(18));
    style.animation(64).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    demonCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(18));
    style.animation(69).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    demonCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(18).splashOnMiss(true).build());
    style.animation(69).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    demonCombat.style(style.build());


    var orkCombat = NpcCombatDefinition.builder();
    orkCombat.id(NpcId.REVENANT_ORK_105);
    orkCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    orkCombat.hitpoints(NpcCombatHitpoints.total(105));
    orkCombat.stats(NpcCombatStats.builder().attackLevel(99).magicLevel(110).rangedLevel(130)
        .defenceLevel(60).bonus(CombatBonus.MELEE_ATTACK, 60).bonus(CombatBonus.DEFENCE_STAB, 148)
        .bonus(CombatBonus.DEFENCE_SLASH, 150).bonus(CombatBonus.DEFENCE_CRUSH, 146)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).bonus(CombatBonus.DEFENCE_RANGED, 148).build());
    orkCombat.aggression(NpcCombatAggression.builder().always(true).build());
    orkCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    orkCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    orkCombat.type(NpcCombatType.UNDEAD).deathAnimation(4321).blockAnimation(4322);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(4320).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    orkCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(4320).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    orkCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(32).build());
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(4320).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    orkCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(4320).attackSpeed(5);
    style.targetGraphic(new Graphic(363));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().magicBind(16).build());
    orkCombat.style(style.build());


    var darkBeastCombat = NpcCombatDefinition.builder();
    darkBeastCombat.id(NpcId.REVENANT_DARK_BEAST_120);
    darkBeastCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    darkBeastCombat.hitpoints(NpcCombatHitpoints.total(140));
    darkBeastCombat.stats(NpcCombatStats.builder().attackLevel(93).magicLevel(130).rangedLevel(135)
        .defenceLevel(80).bonus(CombatBonus.MELEE_ATTACK, 65).bonus(CombatBonus.ATTACK_RANGED, 45)
        .bonus(CombatBonus.DEFENCE_STAB, 153).bonus(CombatBonus.DEFENCE_SLASH, 152)
        .bonus(CombatBonus.DEFENCE_CRUSH, 155).bonus(CombatBonus.DEFENCE_MAGIC, 70)
        .bonus(CombatBonus.DEFENCE_RANGED, 158).build());
    darkBeastCombat.aggression(NpcCombatAggression.builder().always(true).build());
    darkBeastCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    darkBeastCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    darkBeastCombat.type(NpcCombatType.UNDEAD).deathAnimation(2733).blockAnimation(2732);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(2731).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    darkBeastCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(2731).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    darkBeastCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(32).build());
    style.damage(NpcCombatDamage.builder().maximum(23).splashOnMiss(true).build());
    style.animation(2731).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    darkBeastCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(23).splashOnMiss(true).build());
    style.animation(2731).attackSpeed(5);
    style.targetGraphic(new Graphic(363));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().magicBind(16).build());
    darkBeastCombat.style(style.build());


    var knightCombat = NpcCombatDefinition.builder();
    knightCombat.id(NpcId.REVENANT_KNIGHT_126);
    knightCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    knightCombat.hitpoints(NpcCombatHitpoints.total(143));
    knightCombat.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(146).rangedLevel(146)
        .defenceLevel(80).bonus(CombatBonus.MELEE_ATTACK, 69).bonus(CombatBonus.ATTACK_MAGIC, 55)
        .bonus(CombatBonus.ATTACK_RANGED, 55).bonus(CombatBonus.DEFENCE_STAB, 195)
        .bonus(CombatBonus.DEFENCE_SLASH, 200).bonus(CombatBonus.DEFENCE_CRUSH, 180)
        .bonus(CombatBonus.DEFENCE_MAGIC, 95).bonus(CombatBonus.DEFENCE_RANGED, 190).build());
    knightCombat.aggression(NpcCombatAggression.builder().always(true).build());
    knightCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    knightCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    knightCombat.type(NpcCombatType.UNDEAD).deathAnimation(836).blockAnimation(404);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(27));
    style.animation(390).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    knightCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(27));
    style.animation(2614).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    knightCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(32).build());
    style.damage(NpcCombatDamage.builder().maximum(27).splashOnMiss(true).build());
    style.animation(727).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    knightCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(27).splashOnMiss(true).build());
    style.animation(1979).attackSpeed(5);
    style.targetGraphic(new Graphic(363));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().magicBind(16).build());
    knightCombat.style(style.build());


    var dragonCombat = NpcCombatDefinition.builder();
    dragonCombat.id(NpcId.REVENANT_DRAGON_135);
    dragonCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50).build());
    dragonCombat.hitpoints(NpcCombatHitpoints.total(140));
    dragonCombat.stats(NpcCombatStats.builder().attackLevel(106).magicLevel(150).rangedLevel(151)
        .defenceLevel(87).bonus(CombatBonus.MELEE_ATTACK, 72).bonus(CombatBonus.ATTACK_MAGIC, 61)
        .bonus(CombatBonus.ATTACK_RANGED, 60).bonus(CombatBonus.DEFENCE_STAB, 201)
        .bonus(CombatBonus.DEFENCE_SLASH, 206).bonus(CombatBonus.DEFENCE_CRUSH, 188)
        .bonus(CombatBonus.DEFENCE_MAGIC, 101).bonus(CombatBonus.DEFENCE_RANGED, 197).build());
    dragonCombat.aggression(NpcCombatAggression.builder().always(true).build());
    dragonCombat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    dragonCombat.killCount(NpcCombatKillCount.builder().asName("Revenant").build());
    dragonCombat.type(NpcCombatType.UNDEAD).deathAnimation(92).blockAnimation(89);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(80).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    dragonCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(6722).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    dragonCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(32).build());
    style.damage(NpcCombatDamage.builder().maximum(30).splashOnMiss(true).build());
    style.animation(6722).attackSpeed(5);
    style.targetGraphic(new Graphic(1454, 124));
    style.projectile(NpcCombatProjectile.id(335));
    dragonCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(30).splashOnMiss(true).build());
    style.animation(81).attackSpeed(5);
    style.targetGraphic(new Graphic(363));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().magicBind(16).build());
    dragonCombat.style(style.build());


    return Arrays.asList(impCombat.build(), goblinCombat.build(), pyrefiendCombat.build(),
        hobgoblinCombat.build(), cyclopsCombat.build(), hellhoundCombat.build(),
        demonCombat.build(), orkCombat.build(), darkBeastCombat.build(), knightCombat.build(),
        dragonCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void tickStartHook() {
    if (!npc.isLocked() && npc.getHitpoints() < npc.getMaxHitpoints() / 2 && npc.getHitDelay() == 0
        && PRandom.randomE(4) == 0) {
      npc.adjustHitpoints((int) (npc.getMaxHitpoints() * 0.2));
      npc.setHitDelay(npc.getHitDelay() + 2);
      npc.setGraphic(1221);
    }
  }

  @Override
  public double damageInflictedHook(NpcCombatStyle combatStyle, Entity opponent, double damage) {
    if (opponent instanceof Player) {
      var player = (Player) opponent;
      if (player.getCharges().degradeItems(false, ItemId.BRACELET_OF_ETHEREUM, false)) {
        damage = 0;
      }
    }
    return damage;
  }

  @Override
  public boolean canBeAggressiveHook(Entity opponent) {
    return !(opponent instanceof Player)
        || ((Player) opponent).getEquipment().getHandId() != ItemId.BRACELET_OF_ETHEREUM;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    var total = player.getSkills().isWildernessSlayerTask(npc) ? 2 : 1;
    for (var i = 0; i < total; i++) {
      Item item = null;
      var logDrop = false;
      var clampedLevel = Math.min(Math.max(1, npc.getDef().getCombatLevel()), 144);
      var chanceA = 2200.0 / Math.sqrt(clampedLevel);
      var chanceB = 15 + (Math.pow(npc.getDef().getCombatLevel() + 60, 2) / 200);
      var multiplier = Settings.getInstance().isSpawn() ? 8.0 : 4.0;
      var playerMultiplier = player.getCombat().getDropRateMultiplier(-1, npc.getDef());
      chanceA = chanceA / multiplier / playerMultiplier;
      var selectedChanceA = PRandom.randomE((int) chanceA);
      if (selectedChanceA == 1) {
        logDrop = true;
        var roll = PRandom.randomI(player.getCombat().getPKSkullDelay() > 0 ? 13 : 39);
        if (roll == 0) {
          item = RandomItem.getItem(UNIQUE_DROP_TABLE);
        } else if (roll == 1) {
          item = new Item(ItemId.ANCIENT_RELIC, 1);
        } else if (roll == 2) {
          item = new Item(ItemId.ANCIENT_EFFIGY, 1);
        } else if (roll >= 3 && roll <= 4) {
          item = new Item(ItemId.ANCIENT_MEDALLION, 1);
        } else if (roll >= 5 && roll <= 8) {
          item = new Item(ItemId.ANCIENT_STATUETTE, 1);
        } else if (roll >= 9 && roll <= 12) {
          item = new Item(ItemId.MAGIC_SEED, 5 + PRandom.randomI(4));
        } else if (roll >= 13 && roll <= 15) {
          item = new Item(ItemId.ANCIENT_CRYSTAL, 1);
        } else if (roll >= 16 && roll <= 20) {
          item = new Item(ItemId.ANCIENT_TOTEM, 1);
        } else if (roll >= 21 && roll <= 26) {
          item = new Item(ItemId.ANCIENT_EMBLEM, 1);
        } else if (roll >= 27 && roll <= 39) {
          item = new Item(ItemId.DRAGON_MED_HELM, 1);
        }
      } else if (selectedChanceA < chanceB) {
        item = RandomItem.getItem(MEDIOCRE_DROP_TABLE);
      } else if (selectedChanceA < 3000) {
        item = new Item(ItemId.COINS, 10000 + PRandom.randomI(90000));
      }
      if (Settings.getInstance().isSpawn()) {
        if (PRandom.inRange(1, 108 * 50)) {
          npc.getController().addMapItem(new Item(ItemId.DIAMOND_KEY_32309), dropTile, player);
        } else if (PRandom.inRange(1, 36 * 50)) {
          npc.getController().addMapItem(new Item(ItemId.GOLD_KEY_32308), dropTile, player);
        } else if (PRandom.inRange(1, 12 * 50)) {
          npc.getController().addMapItem(new Item(ItemId.SILVER_KEY_32307), dropTile, player);
        } else if (PRandom.inRange(1, 4 * 50)) {
          npc.getController().addMapItem(new Item(ItemId.BRONZE_KEY_32306), dropTile, player);
        }
      }
      if (item != null) {
        npc.getController().addMapItem(item, dropTile, player);
        if (logDrop) {
          player.getCombat().logNPCItem(npc.getDef().getCombat().getKillCountName(npc.getId()),
              item.getId(), item.getAmount());
          npc.getWorld()
              .sendRevenantCavesMessage("<col=005500>" + player.getUsername() + " received a drop: "
                  + (item.getAmount() > 1 ? item.getAmount() + " x " : "") + item.getName());
        }
      }
      if (PRandom.randomE((int) (1048576 / (player.getCombat().getPKSkullDelay() > 0 ? 4 : 1)
          / Math.sqrt(clampedLevel) / playerMultiplier)) == 0) {
        var pvpItem = RandomItem.getItem(ANCIENT_WARRIOR_DROP_TABLE);
        npc.getController().addMapItem(pvpItem, dropTile, player);
        player.getCombat().logNPCItem(npc.getDef().getCombat().getKillCountName(npc.getId()),
            pvpItem.getId(), pvpItem.getAmount());
        npc.getWorld().sendItemDropNews(player, pvpItem.getId(), " a revenant");
      }
      var etherCount = (1 + PRandom.randomE((int) Math.sqrt(clampedLevel))) * 2;
      if (player.getCharges().getEthereumAutoAbsorb()
          && (player.getEquipment().getHandId() == ItemId.BRACELET_OF_ETHEREUM
              || player.getEquipment().getHandId() == ItemId.BRACELET_OF_ETHEREUM_UNCHARGED)) {
        etherCount -= player.getCharges().charge(ItemId.BRACELET_OF_ETHEREUM,
            Equipment.Slot.HAND.ordinal() + 65536, etherCount, new Item(ItemId.REVENANT_ETHER, 1),
            1);
      }
      if (etherCount > 0) {
        npc.getController().addMapItem(new Item(ItemId.REVENANT_ETHER, etherCount), dropTile,
            player);
      }
    }
  }
}
