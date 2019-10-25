package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

public class Spinolyp76Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SPINOLYP_76);
    combat.noclip(true);
    combat.hitpoints(NpcCombatHitpoints.total(100));
    combat.stats(NpcCombatStats.builder().attackLevel(10).rangedLevel(2).defenceLevel(10)
        .bonus(CombatBonus.MELEE_DEFENCE, 100).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.aggression(NpcCombatAggression.builder().range(8).build());
    combat.focus(NpcCombatFocus.builder().disableFollowingOpponent(true).build());
    combat.combatScript("SpinolypCS").deathAnimation(2866).blockAnimation(2869);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(10));
    style.animation(2868).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.maximum(10));
    style.animation(2868).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().statDrain(Skills.PRAYER, 1).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
