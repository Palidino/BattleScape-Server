package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class AbyssalSireSpawnCombat extends NpcCombat {
  private Npc npc;
  private Npc abyssalSire;
  private int transformTimer;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var respiratory = NpcCombatDefinition.builder();
    respiratory.id(NpcId.RESPIRATORY_SYSTEM);
    respiratory.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    respiratory.hitpoints(NpcCombatHitpoints.total(50));
    respiratory.stats(NpcCombatStats.builder().defenceLevel(80).build());
    respiratory.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    respiratory.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    respiratory.type(NpcCombatType.DEMON);


    var spawn = NpcCombatDefinition.builder();
    spawn.id(NpcId.SPAWN_60);
    spawn.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    spawn.hitpoints(NpcCombatHitpoints.total(15));
    spawn.stats(NpcCombatStats.builder().attackLevel(120).rangedLevel(50).defenceLevel(30).build());
    spawn.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    spawn.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    spawn.type(NpcCombatType.DEMON).deathAnimation(4521).blockAnimation(4523);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.builder().maximum(6).prayerEffectiveness(0.4).build());
    style.animation(4522).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    spawn.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().maximum(6).prayerEffectiveness(0.4).build());
    style.animation(4522).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    spawn.style(style.build());


    var scion = NpcCombatDefinition.builder();
    scion.id(NpcId.SCION_100);
    scion.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    scion.hitpoints(NpcCombatHitpoints.total(50));
    scion.stats(NpcCombatStats.builder().attackLevel(120).rangedLevel(100).defenceLevel(80)
        .bonus(CombatBonus.MELEE_ATTACK, 30).bonus(CombatBonus.ATTACK_RANGED, 30).build());
    scion.aggression(NpcCombatAggression.PLAYERS);
    scion.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    scion.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    scion.type(NpcCombatType.DEMON).deathAnimation(7129).blockAnimation(7128);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.builder().maximum(15).prayerEffectiveness(0.4).build());
    style.animation(7126).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    scion.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().maximum(15).prayerEffectiveness(0.4).build());
    style.animation(7127).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    scion.style(style.build());


    return Arrays.asList(respiratory.build(), spawn.build(), scion.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    transformTimer = 0;
  }

  @Override
  public void tickStartHook() {
    if (!npc.isDead() && npc.isVisible() && npc.getId() == NpcId.SPAWN_60
        && transformTimer++ == 20) {
      npc.setTransformationId(NpcId.SCION_100);
      npc.setHitpoints(npc.getDef().getCombat().getHitpoints().getTotal());
      npc.setAnimation(7123);
      npc.setLock(5);
    }
    if (abyssalSire != null && abyssalSire.isDead() || !npc.isVisible()) {
      npc.getWorld().removeNpc(npc);
    }
  }

  @Override
  public Object script(String name, Object... args) {
    if (name.startsWith("abyssal_sire")) {
      abyssalSire = (Npc) args[0];
    }
    return null;
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    var player2 =
        abyssalSire != null ? (Player) abyssalSire.getCombat2().script("combat_with") : null;
    if (player2 != null && player != player2) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage(npc.getDef().getName() + " is busy with someone else.");
      }
      return false;
    }
    var phase = abyssalSire != null ? (int) abyssalSire.getCombat2().script("phase") : 0;
    if (phase == 0) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("Nothing intereseting happens.");
      }
      return false;
    }
    return true;
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    var delay =
        (int) (abyssalSire != null ? abyssalSire.getCombat2().script("disorienting_delay") : 0);
    if (npc.getId() == NpcId.RESPIRATORY_SYSTEM && delay == 0 && damage > 0) {
      damage = PRandom.randomI(3);
    }
    return damage;
  }
}
