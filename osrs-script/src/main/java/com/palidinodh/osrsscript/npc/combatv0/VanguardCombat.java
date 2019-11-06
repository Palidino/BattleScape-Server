package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import lombok.var;

public class VanguardCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.VANGUARD);
    combat.hitpoints(NpcCombatHitpoints.total(133));
    combat.stats(NpcCombatStats.builder().attackLevel(150).magicLevel(150).rangedLevel(150)
        .defenceLevel(210).build());
    combat.aggression(NpcCombatAggression.builder().range(6).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat.combatScript("VanguardCS").deathAnimation(7432);


    return Arrays.asList(combat.build());
  }
}
