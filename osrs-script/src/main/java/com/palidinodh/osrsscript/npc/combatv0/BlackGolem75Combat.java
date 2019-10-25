package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class BlackGolem75Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.BLACK_GOLEM_75);
    combat.hitpoints(NpcCombatHitpoints.total(80));
    combat.stats(NpcCombatStats.builder().attackLevel(80).defenceLevel(80)
        .bonus(CombatBonus.DEFENCE_STAB, 300).bonus(CombatBonus.DEFENCE_SLASH, 300)
        .bonus(CombatBonus.DEFENCE_CRUSH, 1).bonus(CombatBonus.DEFENCE_MAGIC, 300)
        .bonus(CombatBonus.DEFENCE_RANGED, 300).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(2919).blockAnimation(2918);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(4));
    style.animation(2917).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
