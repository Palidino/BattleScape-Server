package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.Region;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

class PestControlCombat extends NpcCombat {
  private static final List<Integer> KNIGHTS = Arrays.asList(NpcId.VOID_KNIGHT_2950,
      NpcId.VOID_KNIGHT_2951, NpcId.VOID_KNIGHT_2952, NpcId.VOID_KNIGHT_2953);
  private static final List<Integer> SHIFTERS = Arrays.asList(NpcId.SHIFTER_38, NpcId.SHIFTER_57,
      NpcId.SHIFTER_76, NpcId.SHIFTER_90, NpcId.SHIFTER_104);
  private static final List<Integer> RAVAGERS = Arrays.asList(NpcId.RAVAGER_36, NpcId.RAVAGER_53,
      NpcId.RAVAGER_71, NpcId.RAVAGER_89, NpcId.RAVAGER_106);
  private static final List<Integer> SPINNERS =
      Arrays.asList(NpcId.SPINNER_36, NpcId.SPINNER_55, NpcId.SPINNER_74, NpcId.SPINNER_92);
  private static final List<Integer> TORCHERS = Arrays.asList(NpcId.TORCHER_33, NpcId.TORCHER_49,
      NpcId.TORCHER_66, NpcId.TORCHER_79, NpcId.TORCHER_92);
  private static final List<Integer> DEFILERS = Arrays.asList(NpcId.DEFILER_33, NpcId.DEFILER_50,
      NpcId.DEFILER_66, NpcId.DEFILER_80, NpcId.DEFILER_97);
  private static final List<Integer> BRAWLERS =
      Arrays.asList(NpcId.BRAWLER_51, NpcId.BRAWLER_76, NpcId.BRAWLER_101, NpcId.BRAWLER_129);
  private static final Graphic SHIFTER_TELEPORT = new Graphic(654);
  private static final Tile[] RAVAGER_DOORS =
      { new Tile(2652, 2592), new Tile(2656, 2584), new Tile(2671, 2592) };

  @Inject
  private Npc npc;
  private Npc knight;
  private Npc[] portals = new Npc[4];

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var knightCombat = NpcCombatDefinition.builder();
    knightCombat.id(NpcId.VOID_KNIGHT_2950).id(NpcId.VOID_KNIGHT_2951).id(NpcId.VOID_KNIGHT_2952)
        .id(NpcId.VOID_KNIGHT_2953);
    knightCombat.spawn(NpcCombatSpawn.builder().respawnDelay(500).build());
    knightCombat.hitpoints(NpcCombatHitpoints.total(200));
    knightCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    knightCombat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    knightCombat.deathAnimation(836);


    var portalCombat = NpcCombatDefinition.builder();
    portalCombat.id(NpcId.PORTAL).id(NpcId.PORTAL_1740).id(NpcId.PORTAL_1741).id(NpcId.PORTAL_1742);
    portalCombat.spawn(NpcCombatSpawn.builder().respawnDelay(500).build());
    portalCombat.hitpoints(NpcCombatHitpoints.total(250));
    portalCombat.immunity(NpcCombatImmunity.builder().bind(true).build());
    portalCombat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());


    var shifter38Combat = NpcCombatDefinition.builder();
    shifter38Combat.id(NpcId.SHIFTER_38);
    shifter38Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    shifter38Combat.hitpoints(NpcCombatHitpoints.total(46));
    shifter38Combat.stats(NpcCombatStats.builder().attackLevel(38).defenceLevel(19).build());
    shifter38Combat.aggression(NpcCombatAggression.PLAYERS);
    shifter38Combat.deathAnimation(3903).blockAnimation(3902);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(5));
    style.animation(3901).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    shifter38Combat.style(style.build());


    var shifter57Combat = NpcCombatDefinition.builder();
    shifter57Combat.id(NpcId.SHIFTER_57);
    shifter57Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    shifter57Combat.hitpoints(NpcCombatHitpoints.total(76));
    shifter57Combat.stats(NpcCombatStats.builder().attackLevel(57).defenceLevel(28).build());
    shifter57Combat.aggression(NpcCombatAggression.PLAYERS);
    shifter57Combat.deathAnimation(3903).blockAnimation(3902);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(6));
    style.animation(3901).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    shifter57Combat.style(style.build());


    var shifter76Combat = NpcCombatDefinition.builder();
    shifter76Combat.id(NpcId.SHIFTER_76);
    shifter76Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    shifter76Combat.hitpoints(NpcCombatHitpoints.total(106));
    shifter76Combat.stats(NpcCombatStats.builder().attackLevel(76).defenceLevel(38).build());
    shifter76Combat.aggression(NpcCombatAggression.PLAYERS);
    shifter76Combat.deathAnimation(3903).blockAnimation(3902);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(3901).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    shifter76Combat.style(style.build());


    var shifter104Combat = NpcCombatDefinition.builder();
    shifter104Combat.id(NpcId.SHIFTER_104);
    shifter104Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    shifter104Combat.hitpoints(NpcCombatHitpoints.total(166));
    shifter104Combat.stats(NpcCombatStats.builder().attackLevel(104).defenceLevel(52).build());
    shifter104Combat.aggression(NpcCombatAggression.PLAYERS);
    shifter104Combat.deathAnimation(3903).blockAnimation(3902);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(3901).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    shifter104Combat.style(style.build());


    var ravager36Combat = NpcCombatDefinition.builder();
    ravager36Combat.id(NpcId.RAVAGER_36);
    ravager36Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    ravager36Combat.hitpoints(NpcCombatHitpoints.total(46));
    ravager36Combat.stats(NpcCombatStats.builder().attackLevel(18).defenceLevel(18).build());
    ravager36Combat.deathAnimation(3917).blockAnimation(3916);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(7));
    style.animation(3915).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    ravager36Combat.style(style.build());


    var ravager53Combat = NpcCombatDefinition.builder();
    ravager53Combat.id(NpcId.RAVAGER_53);
    ravager53Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    ravager53Combat.hitpoints(NpcCombatHitpoints.total(76));
    ravager53Combat.stats(NpcCombatStats.builder().attackLevel(26).defenceLevel(26).build());
    ravager53Combat.deathAnimation(3917).blockAnimation(3916);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(3915).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    ravager53Combat.style(style.build());


    var ravager71Combat = NpcCombatDefinition.builder();
    ravager71Combat.id(NpcId.RAVAGER_71);
    ravager71Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    ravager71Combat.hitpoints(NpcCombatHitpoints.total(106));
    ravager71Combat.stats(NpcCombatStats.builder().attackLevel(35).defenceLevel(35).build());
    ravager71Combat.deathAnimation(3917).blockAnimation(3916);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(3915).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    ravager71Combat.style(style.build());


    var ravager89Combat = NpcCombatDefinition.builder();
    ravager89Combat.id(NpcId.RAVAGER_89);
    ravager89Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    ravager89Combat.hitpoints(NpcCombatHitpoints.total(136));
    ravager89Combat.stats(NpcCombatStats.builder().attackLevel(44).defenceLevel(44).build());
    ravager89Combat.deathAnimation(3917).blockAnimation(3916);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(14));
    style.animation(3915).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    ravager89Combat.style(style.build());


    var ravager106Combat = NpcCombatDefinition.builder();
    ravager106Combat.id(NpcId.RAVAGER_106);
    ravager106Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    ravager106Combat.hitpoints(NpcCombatHitpoints.total(166));
    ravager106Combat.stats(NpcCombatStats.builder().attackLevel(53).defenceLevel(53).build());
    ravager106Combat.deathAnimation(3917).blockAnimation(3916);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(3915).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    ravager106Combat.style(style.build());


    var spinner36Combat = NpcCombatDefinition.builder();
    spinner36Combat.id(NpcId.SPINNER_36);
    spinner36Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    spinner36Combat.hitpoints(NpcCombatHitpoints.total(66));
    spinner36Combat.stats(NpcCombatStats.builder().attackLevel(18).defenceLevel(18).build());
    spinner36Combat.deathAnimation(3910).blockAnimation(3909);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(5));
    style.animation(3908).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    spinner36Combat.style(style.build());


    var spinner55Combat = NpcCombatDefinition.builder();
    spinner55Combat.id(NpcId.SPINNER_55);
    spinner55Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    spinner55Combat.hitpoints(NpcCombatHitpoints.total(106));
    spinner55Combat.stats(NpcCombatStats.builder().attackLevel(27).defenceLevel(27).build());
    spinner55Combat.deathAnimation(3910).blockAnimation(3909);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(6));
    style.animation(3908).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    spinner55Combat.style(style.build());


    var spinner74Combat = NpcCombatDefinition.builder();
    spinner74Combat.id(NpcId.SPINNER_74);
    spinner74Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    spinner74Combat.hitpoints(NpcCombatHitpoints.total(146));
    spinner74Combat.stats(NpcCombatStats.builder().attackLevel(37).defenceLevel(37).build());
    spinner74Combat.deathAnimation(3910).blockAnimation(3909);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(3908).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    spinner74Combat.style(style.build());


    var spinner92Combat = NpcCombatDefinition.builder();
    spinner92Combat.id(NpcId.SPINNER_92);
    spinner92Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    spinner92Combat.hitpoints(NpcCombatHitpoints.total(202));
    spinner92Combat.stats(NpcCombatStats.builder().attackLevel(46).defenceLevel(46).build());
    spinner92Combat.deathAnimation(3910).blockAnimation(3909);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(3908).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    spinner92Combat.style(style.build());


    var torcher33Combat = NpcCombatDefinition.builder();
    torcher33Combat.id(NpcId.TORCHER_33);
    torcher33Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    torcher33Combat.hitpoints(NpcCombatHitpoints.total(36));
    torcher33Combat.stats(NpcCombatStats.builder().magicLevel(33).defenceLevel(16).build());
    torcher33Combat.aggression(NpcCombatAggression.PLAYERS);
    torcher33Combat.deathAnimation(3881).blockAnimation(3880);

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.builder().maximum(6).splashOnMiss(true).build());
    style.animation(3882).attackSpeed(4);
    style.castGraphic(new Graphic(646)).targetGraphic(new Graphic(648, 124));
    style.projectile(NpcCombatProjectile.id(335));
    torcher33Combat.style(style.build());


    var torcher49Combat = NpcCombatDefinition.builder();
    torcher49Combat.id(NpcId.TORCHER_49);
    torcher49Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    torcher49Combat.hitpoints(NpcCombatHitpoints.total(60));
    torcher49Combat.stats(NpcCombatStats.builder().magicLevel(49).defenceLevel(24).build());
    torcher49Combat.aggression(NpcCombatAggression.PLAYERS);
    torcher49Combat.deathAnimation(3881).blockAnimation(3880);

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.builder().maximum(8).splashOnMiss(true).build());
    style.animation(3882).attackSpeed(4);
    style.castGraphic(new Graphic(646)).targetGraphic(new Graphic(648, 124));
    style.projectile(NpcCombatProjectile.id(335));
    torcher49Combat.style(style.build());


    var torcher66Combat = NpcCombatDefinition.builder();
    torcher66Combat.id(NpcId.TORCHER_66);
    torcher66Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    torcher66Combat.hitpoints(NpcCombatHitpoints.total(90));
    torcher66Combat.stats(NpcCombatStats.builder().magicLevel(66).defenceLevel(33).build());
    torcher66Combat.aggression(NpcCombatAggression.PLAYERS);
    torcher66Combat.deathAnimation(3881).blockAnimation(3880);

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.builder().maximum(9).splashOnMiss(true).build());
    style.animation(3882).attackSpeed(4);
    style.castGraphic(new Graphic(646)).targetGraphic(new Graphic(648, 124));
    style.projectile(NpcCombatProjectile.id(335));
    torcher66Combat.style(style.build());


    var torcher79Combat = NpcCombatDefinition.builder();
    torcher79Combat.id(NpcId.TORCHER_79);
    torcher79Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    torcher79Combat.hitpoints(NpcCombatHitpoints.total(114));
    torcher79Combat.stats(NpcCombatStats.builder().magicLevel(79).defenceLevel(39).build());
    torcher79Combat.aggression(NpcCombatAggression.PLAYERS);
    torcher79Combat.deathAnimation(3881).blockAnimation(3880);

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.builder().maximum(11).splashOnMiss(true).build());
    style.animation(3882).attackSpeed(4);
    style.castGraphic(new Graphic(646)).targetGraphic(new Graphic(648, 124));
    style.projectile(NpcCombatProjectile.id(335));
    torcher79Combat.style(style.build());


    var torcher92Combat = NpcCombatDefinition.builder();
    torcher92Combat.id(NpcId.TORCHER_92);
    torcher92Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    torcher92Combat.hitpoints(NpcCombatHitpoints.total(142));
    torcher92Combat.stats(NpcCombatStats.builder().magicLevel(91).defenceLevel(46).build());
    torcher92Combat.aggression(NpcCombatAggression.PLAYERS);
    torcher92Combat.deathAnimation(3881).blockAnimation(3880);

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.builder().maximum(12).splashOnMiss(true).build());
    style.animation(3882).attackSpeed(4);
    style.castGraphic(new Graphic(646)).targetGraphic(new Graphic(648, 124));
    style.projectile(NpcCombatProjectile.id(335));
    torcher92Combat.style(style.build());


    var defiler33Combat = NpcCombatDefinition.builder();
    defiler33Combat.id(NpcId.DEFILER_33);
    defiler33Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    defiler33Combat.hitpoints(NpcCombatHitpoints.total(54));
    defiler33Combat.stats(NpcCombatStats.builder().rangedLevel(33).defenceLevel(16).build());
    defiler33Combat.aggression(NpcCombatAggression.PLAYERS);
    defiler33Combat.deathAnimation(3922).blockAnimation(3921);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(6));
    style.animation(3920).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    defiler33Combat.style(style.build());


    var defiler50Combat = NpcCombatDefinition.builder();
    defiler50Combat.id(NpcId.DEFILER_50);
    defiler50Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    defiler50Combat.hitpoints(NpcCombatHitpoints.total(90));
    defiler50Combat.stats(NpcCombatStats.builder().rangedLevel(50).defenceLevel(25).build());
    defiler50Combat.aggression(NpcCombatAggression.PLAYERS);
    defiler50Combat.deathAnimation(3922).blockAnimation(3921);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(3920).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    defiler50Combat.style(style.build());


    var defiler66Combat = NpcCombatDefinition.builder();
    defiler66Combat.id(NpcId.DEFILER_66);
    defiler66Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    defiler66Combat.hitpoints(NpcCombatHitpoints.total(122));
    defiler66Combat.stats(NpcCombatStats.builder().rangedLevel(66).defenceLevel(33).build());
    defiler66Combat.aggression(NpcCombatAggression.PLAYERS);
    defiler66Combat.deathAnimation(3922).blockAnimation(3921);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(3920).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    defiler66Combat.style(style.build());


    var defiler80Combat = NpcCombatDefinition.builder();
    defiler80Combat.id(NpcId.DEFILER_80);
    defiler80Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    defiler80Combat.hitpoints(NpcCombatHitpoints.total(156));
    defiler80Combat.stats(NpcCombatStats.builder().rangedLevel(80).defenceLevel(40).build());
    defiler80Combat.aggression(NpcCombatAggression.PLAYERS);
    defiler80Combat.deathAnimation(3922).blockAnimation(3921);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(11));
    style.animation(3920).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    defiler80Combat.style(style.build());


    var defiler97Combat = NpcCombatDefinition.builder();
    defiler97Combat.id(NpcId.DEFILER_97);
    defiler97Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    defiler97Combat.hitpoints(NpcCombatHitpoints.total(194));
    defiler97Combat.stats(NpcCombatStats.builder().rangedLevel(97).defenceLevel(48).build());
    defiler97Combat.aggression(NpcCombatAggression.PLAYERS);
    defiler97Combat.deathAnimation(3922).blockAnimation(3921);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(3920).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    defiler97Combat.style(style.build());


    var brawler51Combat = NpcCombatDefinition.builder();
    brawler51Combat.id(NpcId.BRAWLER_51);
    brawler51Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    brawler51Combat.hitpoints(NpcCombatHitpoints.total(106));
    brawler51Combat.stats(NpcCombatStats.builder().attackLevel(51).defenceLevel(25).build());
    brawler51Combat.aggression(NpcCombatAggression.PLAYERS);
    brawler51Combat.deathAnimation(3894).blockAnimation(3895);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(7));
    style.animation(3897).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler51Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(7));
    style.animation(3896).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler51Combat.style(style.build());


    var brawler76Combat = NpcCombatDefinition.builder();
    brawler76Combat.id(NpcId.BRAWLER_76);
    brawler76Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    brawler76Combat.hitpoints(NpcCombatHitpoints.total(166));
    brawler76Combat.stats(NpcCombatStats.builder().attackLevel(76).defenceLevel(38).build());
    brawler76Combat.aggression(NpcCombatAggression.PLAYERS);
    brawler76Combat.deathAnimation(3894).blockAnimation(3895);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(3897).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler76Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(9));
    style.animation(3896).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler76Combat.style(style.build());


    var brawler101Combat = NpcCombatDefinition.builder();
    brawler101Combat.id(NpcId.BRAWLER_101);
    brawler101Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    brawler101Combat.hitpoints(NpcCombatHitpoints.total(226));
    brawler101Combat.stats(NpcCombatStats.builder().attackLevel(101).defenceLevel(50).build());
    brawler101Combat.aggression(NpcCombatAggression.PLAYERS);
    brawler101Combat.deathAnimation(3894).blockAnimation(3895);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(3897).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler101Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(3896).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler101Combat.style(style.build());


    var brawler129Combat = NpcCombatDefinition.builder();
    brawler129Combat.id(NpcId.BRAWLER_129);
    brawler129Combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    brawler129Combat.hitpoints(NpcCombatHitpoints.total(286));
    brawler129Combat.stats(NpcCombatStats.builder().attackLevel(129).defenceLevel(64).build());
    brawler129Combat.aggression(NpcCombatAggression.PLAYERS);
    brawler129Combat.deathAnimation(3894).blockAnimation(3895);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(14));
    style.animation(3897).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler129Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(14));
    style.animation(3896).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    brawler129Combat.style(style.build());


    return Arrays.asList(knightCombat.build(), portalCombat.build(), shifter38Combat.build(),
        shifter57Combat.build(), shifter76Combat.build(), shifter104Combat.build(),
        ravager36Combat.build(), ravager53Combat.build(), ravager71Combat.build(),
        ravager89Combat.build(), ravager106Combat.build(), spinner36Combat.build(),
        spinner55Combat.build(), spinner74Combat.build(), spinner92Combat.build(),
        torcher33Combat.build(), torcher49Combat.build(), torcher66Combat.build(),
        torcher79Combat.build(), torcher92Combat.build(), defiler33Combat.build(),
        defiler50Combat.build(), defiler66Combat.build(), defiler80Combat.build(),
        defiler97Combat.build(), brawler51Combat.build(), brawler76Combat.build(),
        brawler101Combat.build(), brawler129Combat.build());
  }

  @Override
  public void restoreHook() {
    if (KNIGHTS.contains(npc.getId())) {
      npc.setTransformationId(KNIGHTS.get(PRandom.randomE(KNIGHTS.size())));
    }
    for (int id : KNIGHTS) {
      knight = npc.getWorld().getNPC(id, npc);
      if (knight != null) {
        break;
      }
    }
    portals[0] = npc.getWorld().getNPC(NpcId.PORTAL, npc);
    portals[1] = npc.getWorld().getNPC(NpcId.PORTAL_1740, npc);
    portals[2] = npc.getWorld().getNPC(NpcId.PORTAL_1741, npc);
    portals[3] = npc.getWorld().getNPC(NpcId.PORTAL_1742, npc);
    if (SHIFTERS.contains(npc.getId())) {
      npc.setTransformationId(SHIFTERS.get(PRandom.randomE(SHIFTERS.size())));
    } else if (RAVAGERS.contains(npc.getId())) {
      npc.setTransformationId(RAVAGERS.get(PRandom.randomE(RAVAGERS.size())));
    } else if (SPINNERS.contains(npc.getId())) {
      npc.setTransformationId(SPINNERS.get(PRandom.randomE(SPINNERS.size())));
    } else if (TORCHERS.contains(npc.getId())) {
      npc.setTransformationId(TORCHERS.get(PRandom.randomE(TORCHERS.size())));
    } else if (DEFILERS.contains(npc.getId())) {
      npc.setTransformationId(DEFILERS.get(PRandom.randomE(DEFILERS.size())));
    } else if (BRAWLERS.contains(npc.getId())) {
      npc.setTransformationId(BRAWLERS.get(PRandom.randomE(BRAWLERS.size())));
    }
  }

  @Override
  public void tickStartHook() {
    if (npc.isDead() || !npc.getController().getRegion(npc.getRegionId(), false).isLoaded()) {
      return;
    }
    if (npc.getId() >= NpcId.SHIFTER_38 && npc.getId() <= NpcId.SHIFTER_104_1703) {
      if (knight.isVisible() && PRandom.randomE(256) == 0) {
        npc.getController().sendMapGraphic(npc, SHIFTER_TELEPORT);
        var spot = PRandom.randomE(4);
        Tile tile = null;
        if (spot == 0) {
          tile = new Tile(2656, 2593);
        } else if (spot == 1) {
          tile = new Tile(2657, 2592);
        } else if (spot == 2) {
          tile = new Tile(2656, 2591);
        } else if (spot == 3) {
          tile = new Tile(2655, 2592);
        }
        if (tile != null) {
          npc.getController().sendMapGraphic(tile, SHIFTER_TELEPORT);
          npc.getMovement().teleport(tile);
          npc.getCombat().startAttacking(knight);
        }
      }
    } else if (npc.getId() >= NpcId.RAVAGER_36 && npc.getId() <= NpcId.RAVAGER_106) {
      for (var x = npc.getX() - 1; x <= npc.getX() + 1; x++) {
        for (var y = npc.getY() - 1; y <= npc.getY() + 1; y++) {
          var object = npc.getController().getDirectionalMapObject(x, y, npc.getHeight());
          if (object != null && object.getAttachment() == null && object.getId() >= 14233
              && object.getY() <= 14248 && object.getDef().getName().equals("Gate")) {
            npc.setAnimation(3915);
            Region.openDoors(npc, object);
          }
        }
      }
      if (PRandom.randomE(256) == 0) {
        Tile closestTile = null;
        var closest = Integer.MAX_VALUE;
        for (var door : RAVAGER_DOORS) {
          var distance = npc.getDistance(door);
          if (distance < closest) {
            closestTile = door;
            closest = distance;
          }
        }
        npc.getMovement().quickRoute(closestTile.getX(), closestTile.getY());
      }
    } else if (npc.getId() >= NpcId.SPINNER_36 && npc.getId() <= NpcId.SPINNER_88) {
      if (npc.getHitDelay() <= 0) {
        for (var portal : portals) {
          if (npc.withinDistance(portal, 4) && !portal.isDead()
              && portal.getHitpoints() < portal.getMaxHitpoints()) {
            npc.setFaceEntity(portal);
            npc.setHitDelay(4);
            npc.setAnimation(3911);
            npc.setGraphic(658);
            portal.adjustHitpoints(1 + PRandom.randomE(19), 0);
          }
        }
      }
    }
    if (npc != knight && npc.getDef().getAggressive() && !npc.isAttacking()
        && (npc.getId() >= NpcId.SHIFTER_38 && npc.getId() <= NpcId.SHIFTER_104_1703
            || npc.getId() >= NpcId.TORCHER_33 && npc.getId() <= NpcId.DEFILER_97_1733)
        && npc.withinDistance(knight, 10) && PRandom.randomE(8) == 0) {
      npc.getCombat().startAttacking(knight);
    }
  }

  @Override
  public void npcApplyHitEndHook(Hit hit) {
    if (hit.getDamage() > 0 && hit.getSource() instanceof Player) {
      var player = (Player) hit.getSource();
      var multiplier = 1.0;
      if (knight != null && !knight.isDead()) {
        multiplier += 0.10;
      }
      for (var portal : portals) {
        if (portal != null && portal.isDead()) {
          multiplier += 0.10;
        }
      }
      if (player.isPremiumMember()) {
        multiplier += 0.25;
      }
      var points = (int) (hit.getDamage() * multiplier);
      player.getCombat().setPestControlPoints(player.getCombat().getPestControlPoints() + points);
    }
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    return npc != knight;
  }
}
