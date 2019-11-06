package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import lombok.var;

public class VerzikNylocasMatomenosCombat extends NpcCombat {
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.NYLOCAS_MATOMENOS_115_8385);
    combat.spawn(NpcCombatSpawn.builder().animation(8098).build());
    combat.hitpoints(NpcCombatHitpoints.total(200));
    combat.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(100).rangedLevel(100)
        .defenceLevel(100).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat.deathAnimation(8097);


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (npc.getTotalTicks() == 20) {
      var remainingHitpoints = npc.getHitpoints();
      timedDeath();
      var verzikNpc = npc.getController().getNpc(NpcId.VERZIK_VITUR_1265);
      if (verzikNpc != null && !verzikNpc.isLocked()) {
        verzikNpc.adjustHitpoints(remainingHitpoints);
      }
    }
  }
}
