package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.util.PEvent;
import lombok.var;

public class MageArena2DemonCombat extends NpcCombat {
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var justiciarCombat = NpcCombatDefinition.builder();
    justiciarCombat.id(NpcId.JUSTICIAR_ZACHARIAH_348_7858);
    justiciarCombat.spawn(NpcCombatSpawn.builder().lock(4).animation(7964).build());
    justiciarCombat.hitpoints(NpcCombatHitpoints.total(320));
    justiciarCombat.stats(NpcCombatStats.builder().attackLevel(500).magicLevel(180)
        .defenceLevel(100).bonus(CombatBonus.ATTACK_MAGIC, 80).bonus(CombatBonus.MELEE_DEFENCE, 200)
        .bonus(CombatBonus.DEFENCE_MAGIC, -60).bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    justiciarCombat.aggression(NpcCombatAggression.PLAYERS);
    justiciarCombat.immunity(NpcCombatImmunity.builder().venom(true).build());
    justiciarCombat.deathAnimation(7854).blockAnimation(7965);

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.builder().maximum(43).prayerEffectiveness(0.5).build());
    style.animation(7963).attackSpeed(3);
    style.targetGraphic(new Graphic(1518, 100));
    style.projectile(NpcCombatProjectile.id(335));
    justiciarCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(6).build());
    style.damage(
        NpcCombatDamage.builder().maximum(43).prayerEffectiveness(0.5).splashOnMiss(true).build());
    style.animation(7962).attackSpeed(6);
    style.targetGraphic(new Graphic(1518, 100));
    style.projectile(NpcCombatProjectile.id(335));
    justiciarCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.builder().maximum(43).ignorePrayer(true).build());
    style.animation(7962).attackSpeed(6);
    style.targetGraphic(new Graphic(137));
    style.projectile(NpcCombatProjectile.builder().id(1515).speedMinimumDistance(8).build());
    var targetTile = NpcCombatTargetTile.builder().teleportOpponent(true);
    style.specialAttack(targetTile.build());
    justiciarCombat.style(style.build());


    var derwenCombat = NpcCombatDefinition.builder();
    derwenCombat.id(NpcId.DERWEN_235_7859);
    derwenCombat.spawn(NpcCombatSpawn.builder().lock(6).animation(7844).build());
    derwenCombat.hitpoints(NpcCombatHitpoints.total(320));
    derwenCombat.stats(NpcCombatStats.builder().attackLevel(250).magicLevel(80).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_MAGIC, 80).bonus(CombatBonus.MELEE_DEFENCE, 200)
        .bonus(CombatBonus.DEFENCE_MAGIC, -60).bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    derwenCombat.aggression(NpcCombatAggression.PLAYERS);
    derwenCombat.immunity(NpcCombatImmunity.builder().venom(true).build());
    derwenCombat.deathAnimation(7850).blockAnimation(7846);

    style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.builder().maximum(43).prayerEffectiveness(0.5).build());
    style.animation(7848).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    derwenCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(6).build());
    style.damage(
        NpcCombatDamage.builder().maximum(43).prayerEffectiveness(0.5).splashOnMiss(true).build());
    style.animation(7849).attackSpeed(6);
    style.targetGraphic(new Graphic(1511, 100));
    style.projectile(NpcCombatProjectile.id(335));
    derwenCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.builder().maximum(43).ignorePrayer(true).build());
    style.animation(7849).attackSpeed(6);
    style.projectile(NpcCombatProjectile.builder().id(1512).speedMinimumDistance(8).build());
    targetTile = NpcCombatTargetTile.builder();
    style.specialAttack(targetTile.build());
    derwenCombat.style(style.build());


    var porazdirCombat = NpcCombatDefinition.builder();
    porazdirCombat.id(NpcId.PORAZDIR_235_7860);
    porazdirCombat.spawn(NpcCombatSpawn.builder().lock(8).animation(7842).build());
    porazdirCombat.hitpoints(NpcCombatHitpoints.total(320));
    porazdirCombat.stats(NpcCombatStats.builder().attackLevel(250).magicLevel(80).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_MAGIC, 80).bonus(CombatBonus.MELEE_DEFENCE, 200)
        .bonus(CombatBonus.DEFENCE_MAGIC, -60).bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    porazdirCombat.aggression(NpcCombatAggression.PLAYERS);
    porazdirCombat.immunity(NpcCombatImmunity.builder().venom(true).build());
    porazdirCombat.deathAnimation(7843).blockAnimation(7838);

    style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.builder().maximum(43).prayerEffectiveness(0.5).build());
    style.animation(7840).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    porazdirCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(
        NpcCombatDamage.builder().maximum(43).prayerEffectiveness(0.5).splashOnMiss(true).build());
    style.animation(7841).attackSpeed(6);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    porazdirCombat.style(style.build());


    return Arrays.asList(justiciarCombat.build(), derwenCombat.build(), porazdirCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void targetTileHitEventHook(NpcCombatStyle combatStyle, Entity opponent,
      TileHitEvent tileHitEvent, Graphic.Projectile projectile) {
    if (npc.getId() == NpcId.DERWEN_235_7859) {
      var event = new PEvent(tileHitEvent.getTick()) {
        @Override
        public void execute() {
          stop();
          if (npc.isLocked()) {
            return;
          }
          new Npc(npc.getController(), NpcId.ENERGY_BALL, tileHitEvent.getTile());
        }
      };
      npc.getWorld().addEvent(event);
    }
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    var spell = player.getMagic().getActiveSpell();
    if (hitType != HitType.MAGIC || spell == null) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("This demon can only be affected by magic.");
      }
      return false;
    }
    if (npc.getId() == NpcId.JUSTICIAR_ZACHARIAH_348_7858
        && !spell.getName().equals("saradomin strike")) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("This demon can only be affected by Saradomin Strike.");
      }
      return false;
    } else if (npc.getId() == NpcId.DERWEN_235_7859 && !spell.getName().equals("claws of guthix")) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("This demon can only be affected by Claws of Guthix.");
      }
      return false;
    } else if (npc.getId() == NpcId.PORAZDIR_235_7860
        && !spell.getName().equals("flames of zamorak")) {
      if (sendMessage) {
        player.getGameEncoder()
            .sendMessage("This demon can only be affected by Flames of Zamorak.");
      }
      return false;
    }
    return true;
  }
}
