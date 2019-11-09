package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class VorkathZombifiedSpawnCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private int countdown1;
  private int countdown2;
  private Entity following;
  private int explosionDamage;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ZOMBIFIED_SPAWN_64);
    combat.hitpoints(NpcCombatHitpoints.total(38));
    combat.stats(NpcCombatStats.builder().attackLevel(82).defenceLevel(6)
        .bonus(CombatBonus.ATTACK_STAB, 1).bonus(CombatBonus.ATTACK_SLASH, 1)
        .bonus(CombatBonus.ATTACK_CRUSH, 1).bonus(CombatBonus.ATTACK_MAGIC, 1)
        .bonus(CombatBonus.ATTACK_RANGED, 1).bonus(CombatBonus.MELEE_DEFENCE, 3)
        .bonus(CombatBonus.DEFENCE_MAGIC, -100).bonus(CombatBonus.DEFENCE_RANGED, 3).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat.type(NpcCombatType.UNDEAD).deathAnimation(7891);


    return Arrays.asList(combat.build());
  }

  @Override
  public void restoreHook() {
    countdown1 = 2;
    countdown2 = 2;
    following = null;
    explosionDamage = 0;
  }

  @Override
  public void tickStartHook() {
    if (npc.getMovement().getFollowing() != null) {
      following = npc.getMovement().getFollowing();
    }
    if (!npc.isLocked() && !npc.withinMapDistance(following)) {
      npc.getCombat2().timedDeath();
      return;
    }
    if (!npc.withinDistance(following, 1)) {
      return;
    }
    if (--countdown1 >= 0) {
      if (countdown1 == 1) {
        npc.getMovement().setFollowing(null);
        npc.getMovement().clear();
      } else if (countdown1 == 0) {
        explosionDamage = (int) (npc.getHitpoints() * 1.4);
        npc.getCombat2().timedDeath(countdown2);
      }
    } else if (--countdown2 >= 0) {
      if (countdown2 == 0) {
        if (npc.withinDistance(following, 1)) {
          var hitEvent = new HitEvent(0, following, new Hit(explosionDamage));
          following.addHit(hitEvent);
          following.getController().setMagicBind(0);
        }
      }
    }
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (opponent instanceof Player && hitType == HitType.MAGIC && damage > 0) {
      var player = (Player) opponent;
      if (player.getMagic().getActiveSpell() != null
          && player.getMagic().getActiveSpell().getName().contains("crumble undead")) {
        return npc.getHitpoints();
      }
    }
    return damage;
  }
}
