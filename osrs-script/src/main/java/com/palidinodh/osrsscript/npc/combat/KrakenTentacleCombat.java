package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
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
import lombok.var;

class KrakenTentacleCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private Npc kraken;
  private Npc[] tentacles = new Npc[4];

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ENORMOUS_TENTACLE_112);
    combat.noclip(true);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    combat.hitpoints(NpcCombatHitpoints.total(120));
    combat.stats(NpcCombatStats.builder().rangedLevel(150).bonus(CombatBonus.DEFENCE_MAGIC, -15)
        .bonus(CombatBonus.DEFENCE_RANGED, 270).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    combat.deathAnimation(3620).blockAnimation(3619);

    var style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.RANGED).subHitType(HitType.MAGIC).build());
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(3618).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public Object script(String name, Object... args) {
    if (name.startsWith("kraken=")) {
      kraken = npc.getWorld().getNpcByIndex(Integer.parseInt(name.substring(7)));
    } else if (name.startsWith("tentacle0=")) {
      tentacles[0] = npc.getWorld().getNpcByIndex(Integer.parseInt(name.substring(10)));
    } else if (name.startsWith("tentacle1=")) {
      tentacles[1] = npc.getWorld().getNpcByIndex(Integer.parseInt(name.substring(10)));
    } else if (name.startsWith("tentacle2=")) {
      tentacles[2] = npc.getWorld().getNpcByIndex(Integer.parseInt(name.substring(10)));
    } else if (name.startsWith("tentacle3=")) {
      tentacles[3] = npc.getWorld().getNpcByIndex(Integer.parseInt(name.substring(10)));
    }
    return null;
  }

  @Override
  public void restoreHook() {
    npc.setTransformationId(NpcId.WHIRLPOOL_5534);
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (npc.getInCombatDelay() == 0 && !npc.isAttacking() && npc.getId() != NpcId.WHIRLPOOL_5534) {
      npc.setTransformationId(NpcId.WHIRLPOOL_5534);
    } else if (npc.getInCombatDelay() > 0 || npc.isAttacking()) {
      npc.getMovement().clear();
      if (npc.getId() != NpcId.ENORMOUS_TENTACLE_112) {
        npc.setTransformationId(NpcId.ENORMOUS_TENTACLE_112);
        npc.setLock(1);
        npc.setAnimation(3860);
        npc.setHitDelay(4);
      }
    }
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    if (hitType == HitType.MELEE) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("Melee doesn't seem effective against these...");
      }
      return false;
    }
    if (npc.isAttacking() && npc.getLastHitByEntity() != null
        && player != npc.getLastHitByEntity()) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("The Kraken is busy attacking someone else.");
      }
      return false;
    }
    if (kraken != null && kraken.isAttacking() && kraken.getLastHitByEntity() != null
        && player != kraken.getLastHitByEntity()) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("The Kraken is busy attacking someone else.");
      }
      return false;
    }
    for (var tentacle : tentacles) {
      if (tentacle != null && tentacle.isAttacking() && tentacle.getLastHitByEntity() != null
          && player != tentacle.getLastHitByEntity()) {
        if (sendMessage) {
          player.getGameEncoder().sendMessage("The Kraken is busy attacking someone else.");
        }
        return false;
      }
    }
    return true;
  }
}
