package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.random.PRandom;
import lombok.var;

class JalImkotCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JAL_IMKOT_240);
    combat.hitpoints(NpcCombatHitpoints.total(75));
    combat.stats(NpcCombatStats.builder().attackLevel(210).magicLevel(120).rangedLevel(220)
        .defenceLevel(120).bonus(CombatBonus.ATTACK_STAB, 40).bonus(CombatBonus.ATTACK_SLASH, 40)
        .bonus(CombatBonus.ATTACK_CRUSH, 40).bonus(CombatBonus.MELEE_DEFENCE, 65)
        .bonus(CombatBonus.DEFENCE_MAGIC, 30).bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.deathAnimation(7599).blockAnimation(7598);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(49));
    style.animation(7597).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked() || npc.getHitDelay() > -34 || npc.getEngagingEntity() == null
        || PRandom.randomE(4) != 0) {
      return;
    }
    Tile teleportTile = null;
    if (PRandom.randomI(1) == 0 && Route.canMove(npc, npc.getEngagingEntity(),
        new Tile(npc.getEngagingEntity()).moveY(-npc.getSizeY()))) {
      teleportTile = new Tile(npc.getEngagingEntity()).moveY(-npc.getSizeY());
    } else if (PRandom.randomI(1) == 0 && Route.canMove(npc, npc.getEngagingEntity(),
        new Tile(npc.getEngagingEntity()).moveX(-npc.getSizeX()))) {
      teleportTile = new Tile(npc.getEngagingEntity()).moveX(-npc.getSizeX());
    } else if (PRandom.randomI(1) == 0 && Route.canMove(npc, npc.getEngagingEntity(),
        new Tile(npc.getEngagingEntity()).moveY(1))) {
      teleportTile = new Tile(npc.getEngagingEntity()).moveY(1);
    } else if (PRandom.randomI(1) == 0 && Route.canMove(npc, npc.getEngagingEntity(),
        new Tile(npc.getEngagingEntity()).moveX(1))) {
      teleportTile = new Tile(npc.getEngagingEntity()).moveX(1);
    }
    npc.setHitDelay(2);
    if (teleportTile == null) {
      return;
    }
    npc.setLock(8);
    npc.getMovement().animatedTeleport(teleportTile, 7600, 7601, null, null, 4);
  }
}
