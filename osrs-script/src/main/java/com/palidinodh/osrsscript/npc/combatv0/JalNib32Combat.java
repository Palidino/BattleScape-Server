package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
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
import lombok.var;

class JalNib32Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JAL_NIB_32);
    combat.hitpoints(NpcCombatHitpoints.total(15));
    combat.stats(NpcCombatStats.builder().magicLevel(15).defenceLevel(15)
        .bonus(CombatBonus.MELEE_DEFENCE, -20).bonus(CombatBonus.DEFENCE_MAGIC, -20)
        .bonus(CombatBonus.DEFENCE_RANGED, -20).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.deathAnimation(7576).blockAnimation(7575);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(7574).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
