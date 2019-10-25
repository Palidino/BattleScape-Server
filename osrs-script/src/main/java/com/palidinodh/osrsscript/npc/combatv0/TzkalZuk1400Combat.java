package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class TzkalZuk1400Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.TZKAL_ZUK_1400);
    combat.spawn(NpcCombatSpawn.builder().lock(8).animation(7563).build());
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(1200).bar(HitpointsBar.GREEN_RED_160).build());
    combat.stats(NpcCombatStats.builder().attackLevel(350).magicLevel(150).rangedLevel(400)
        .defenceLevel(260).bonus(CombatBonus.ATTACK_CRUSH, 300).bonus(CombatBonus.ATTACK_MAGIC, 550)
        .bonus(CombatBonus.ATTACK_RANGED, 550).bonus(CombatBonus.DEFENCE_MAGIC, 350)
        .bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().disableFacingOpponent(true).disableFollowingOpponent(true)
        .build());
    combat.combatScript("TzKalZukCS");

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(251).ignorePrayer(true).build());
    style.animation(7566).attackSpeed(10).attackRange(40);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
