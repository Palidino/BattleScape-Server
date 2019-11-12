package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import lombok.var;

public class InfernoSupportCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ROCKY_SUPPORT_1);
    combat.hitpoints(NpcCombatHitpoints.total(175));
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat.deathAnimation(7561);


    return Arrays.asList(combat.build());
  }

  @Override
  public void applyDeadHook() {
    if (npc.getId() == NpcId.ROCKY_SUPPORT_1 && npc.getRespawnDelay() == 1) {
      npc.getController().addMapObject(new MapObject(-1, npc, 10, 0));
      npc.setTransformationId(NpcId.ROCKY_SUPPORT);
    }
    if (npc.getRespawnDelay() == 0) {
      for (var player : npc.getWorld().getPlayers()) {
        if (player.isLocked() || !npc.withinDistance(player, 1)) {
          continue;
        }
        var hitEvent = new HitEvent(0, player, new Hit(49));
        player.addHit(hitEvent);
      }
      for (var npc2 : npc.getWorld().getNPCs()) {
        if (npc2.isLocked() || !npc.withinDistance(npc2, 1) || npc2 == npc) {
          continue;
        }
        var hitEvent = new HitEvent(0, npc2, new Hit(49));
        npc2.addHit(hitEvent);
      }
    }
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    return false;
  }
}
