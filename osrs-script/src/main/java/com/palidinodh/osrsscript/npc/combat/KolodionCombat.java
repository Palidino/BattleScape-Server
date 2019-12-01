package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class KolodionCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat1 = NpcCombatDefinition.builder();
    combat1.id(NpcId.KOLODION_1605);
    combat1.spawn(NpcCombatSpawn.builder().lock(4).phrase("You must prove yourself... now!")
        .graphic(new Graphic(86, 100)).respawnId(NpcId.KOLODION_1606).deathDelay(8).build());
    combat1.hitpoints(NpcCombatHitpoints.total(3));
    combat1
        .stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16).build());
    combat1.aggression(NpcCombatAggression.PLAYERS);
    combat1.deathAnimation(714).blockAnimation(424);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(811).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat1.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(811).attackSpeed(4);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat1.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(811).attackSpeed(4);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    combat1.style(style.build());


    var combat2 = NpcCombatDefinition.builder();
    combat2.id(NpcId.KOLODION_1606);
    combat2.spawn(NpcCombatSpawn.builder().lock(4)
        .phrase("This is only the beginning; you can't beat me!").animation(134)
        .graphic(new Graphic(86, 100)).respawnId(NpcId.KOLODION_1607).deathDelay(8).build());
    combat2.hitpoints(NpcCombatHitpoints.total(65));
    combat2
        .stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16).build());
    combat2.aggression(NpcCombatAggression.PLAYERS);
    combat2.deathAnimation(133).blockAnimation(129);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(132).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(132).attackSpeed(4);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(132).attackSpeed(4);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    combat2.style(style.build());


    var combat3 = NpcCombatDefinition.builder();
    combat3.id(NpcId.KOLODION_1607);
    combat3.spawn(
        NpcCombatSpawn.builder().lock(4).phrase("Foolish mortal; I am unstoppable.").animation(5324)
            .graphic(new Graphic(86, 100)).respawnId(NpcId.KOLODION_1608).deathDelay(8).build());
    combat3.hitpoints(NpcCombatHitpoints.total(65));
    combat3
        .stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16).build());
    combat3.aggression(NpcCombatAggression.PLAYERS);
    combat3.deathAnimation(5323).blockAnimation(5320);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(5322).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat3.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(5322).attackSpeed(4);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat3.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(5322).attackSpeed(4);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    combat3.style(style.build());


    var combat4 = NpcCombatDefinition.builder();
    combat4.id(NpcId.KOLODION_1608);
    combat4.spawn(NpcCombatSpawn.builder().lock(4).phrase("Now you feel it... The dark energy.")
        .animation(715).graphic(new Graphic(86, 100)).respawnId(NpcId.KOLODION_112).deathDelay(8)
        .build());
    combat4.hitpoints(NpcCombatHitpoints.total(78));
    combat4
        .stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16).build());
    combat4.aggression(NpcCombatAggression.PLAYERS);
    combat4.deathAnimation(714).blockAnimation(399);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(812).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat4.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(812).attackSpeed(4);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat4.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(812).attackSpeed(4);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    combat4.style(style.build());


    var combat5 = NpcCombatDefinition.builder();
    combat5.id(NpcId.KOLODION_112);
    combat5.spawn(NpcCombatSpawn.builder().lock(4).phrase("Aaaaaaaarrgghhhh! The power!")
        .animation(4681).graphic(new Graphic(86, 100)).build());
    combat5.hitpoints(NpcCombatHitpoints.total(107));
    combat5
        .stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16).build());
    combat5.aggression(NpcCombatAggression.PLAYERS);
    combat5.deathAnimation(67).blockAnimation(65);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat5.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat5.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    combat5.style(style.build());


    return Arrays.asList(combat1.build(), combat2.build(), combat3.build(), combat4.build(),
        combat5.build());
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (npc.getId() == NpcId.KOLODION_112) {
      player.getCombat().setMageArena(true);
      player.getMovement().teleport(2541, 4717, 0);
    }
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (hitType != HitType.MAGIC) {
      damage = 0;
    }
    return damage;
  }
}
