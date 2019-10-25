package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.random.PRandom;
import lombok.var;

public class MaidenBloodSpawnCombat extends NpcCombat {
  private Npc npc;
  private List<TempMapObject> tempMapObjects = new ArrayList<>();

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.BLOOD_SPAWN_55);
    combat.hitpoints(NpcCombatHitpoints.total(120));
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat.deathAnimation(8103);


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    for (var tempMapObject : tempMapObjects) {
      tempMapObject.stop();
    }
  }

  @Override
  public void tickStartHook() {
    if (!npc.isLocked() && !npc.getController().hasSolidMapObject(npc)) {
      var tmo = new TempMapObject(30, npc.getController(),
          new MapObject(32984, npc, 10, MapObject.getRandomDirection()));
      tempMapObjects.add(tmo);
      npc.getWorld().addEvent(tmo);
    }
    if (!npc.isLocked() && (PRandom.randomE(4) == 0 || !npc.getMovement().isRouting())) {
      npc.getMovement().clear();
      npc.getMovement().generateRandomPath();
    }
  }
}
