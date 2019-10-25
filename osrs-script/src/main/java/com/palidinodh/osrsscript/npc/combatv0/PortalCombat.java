package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import lombok.var;

public class PortalCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.PORTAL);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(500).build());
    combat.hitpoints(NpcCombatHitpoints.total(250));
    combat.immunity(NpcCombatImmunity.builder().bind(true).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat.combatScript("PestControlCS");


    return Arrays.asList(combat.build());
  }
}
