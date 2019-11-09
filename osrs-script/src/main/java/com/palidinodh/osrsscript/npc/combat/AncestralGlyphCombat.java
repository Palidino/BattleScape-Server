package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import lombok.var;

public class AncestralGlyphCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private int moveDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ANCESTRAL_GLYPH);
    combat.hitpoints(NpcCombatHitpoints.total(600));
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat.focus(NpcCombatFocus.builder().disableFacingOpponent(true).disableFollowingOpponent(true)
        .build());
    combat.deathAnimation(7569).blockAnimation(7568);


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    moveDelay = 2;
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (npc.getMovement().isRouting()) {
      return;
    }
    if (moveDelay > 0) {
      moveDelay--;
    }
    if (moveDelay != 0) {
      return;
    }
    moveDelay = 4;
    npc.getMovement().clear();
    if (npc.getY() == npc.getSpawnTile().getY()) {
      npc.getMovement().addMovement(npc.getSpawnTile().getX(), npc.getSpawnTile().getY() - 2);
    } else {
      if (npc.getX() < npc.getSpawnTile().getX()) {
        npc.getMovement().addMovement(npc.getSpawnTile().getX() + 13,
            npc.getSpawnTile().getY() - 2);
      } else {
        npc.getMovement().addMovement(npc.getSpawnTile().getX() - 13,
            npc.getSpawnTile().getY() - 2);
      }
    }
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    return opponent instanceof Npc && opponent.getId() == NpcId.TZKAL_ZUK_1400;
  }
}
