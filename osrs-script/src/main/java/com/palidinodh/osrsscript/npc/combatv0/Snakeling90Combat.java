package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class Snakeling90Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SNAKELING_90);
    combat.hitpoints(NpcCombatHitpoints.total(1));
    combat.stats(NpcCombatStats.builder().attackLevel(140).bonus(CombatBonus.MELEE_ATTACK, 120)
        .bonus(CombatBonus.ATTACK_MAGIC, 120).bonus(CombatBonus.MELEE_DEFENCE, -40)
        .bonus(CombatBonus.DEFENCE_MAGIC, -40).bonus(CombatBonus.DEFENCE_RANGED, -40).build());
    combat.aggression(NpcCombatAggression.builder().range(20).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(
        NpcCombatFocus.builder().meleeUnlessUnreachable(true).bypassMapObjects(true).build());
    combat.deathAnimation(2408).blockAnimation(1742);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(1741).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().venom(6).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(1741).attackSpeed(4).attackRange(3);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().venom(6).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
