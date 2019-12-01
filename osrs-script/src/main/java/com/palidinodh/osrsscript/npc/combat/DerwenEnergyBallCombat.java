package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitMark;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import lombok.var;

class DerwenEnergyBallCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private Npc derwen;
  private int healDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ENERGY_BALL);
    combat.hitpoints(NpcCombatHitpoints.total(20));
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void restoreHook() {
    healDelay = 4;
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked() || --healDelay > 0) {
      return;
    }
    healDelay = 4;
    if (derwen == null) {
      derwen = npc.getController().getNpc(NpcId.DERWEN_235_7859);
    }
    if (derwen == null || derwen.isLocked()) {
      timedDeath();
      return;
    }
    var projectile = Graphic.Projectile.builder().id(1513).startTile(npc).entity(derwen)
        .projectileSpeed(getProjectileSpeed(derwen)).build();
    sendMapProjectile(projectile);
    derwen.addHit(new HitEvent(projectile.getEventDelay(), derwen, npc, new Hit(5, HitMark.HEAL)));
  }
}
