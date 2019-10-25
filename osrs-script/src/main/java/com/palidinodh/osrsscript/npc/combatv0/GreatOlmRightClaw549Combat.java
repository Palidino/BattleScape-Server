package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import lombok.var;

public class GreatOlmRightClaw549Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.GREAT_OLM_RIGHT_CLAW_549);
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(500).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().attackLevel(250).magicLevel(87).rangedLevel(250)
        .defenceLevel(175).bonus(CombatBonus.ATTACK_MAGIC, 60).bonus(CombatBonus.ATTACK_RANGED, 60)
        .bonus(CombatBonus.MELEE_DEFENCE, 200).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 200).build());
    combat.focus(NpcCombatFocus.builder().retaliationDisabled(true).build());
    combat.combatScript("greatolm");


    return Arrays.asList(combat.build());
  }
}
