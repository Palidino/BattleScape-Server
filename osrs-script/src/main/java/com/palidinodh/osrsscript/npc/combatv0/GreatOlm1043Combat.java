package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class GreatOlm1043Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.GREAT_OLM_1043);
    combat.spawn(NpcCombatSpawn.builder().lock(8).build());
    combat
        .hitpoints(NpcCombatHitpoints.builder().total(680).bar(HitpointsBar.GREEN_RED_100).build());
    combat.stats(NpcCombatStats.builder().attackLevel(250).magicLevel(250).rangedLevel(250)
        .defenceLevel(150).bonus(CombatBonus.ATTACK_MAGIC, 60).bonus(CombatBonus.ATTACK_RANGED, 60)
        .bonus(CombatBonus.MELEE_DEFENCE, 200).bonus(CombatBonus.DEFENCE_MAGIC, 200)
        .bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().disableFollowingOpponent(true).build());
    combat.killCount(
        NpcCombatKillCount.builder().asName("Chambers of Xeric").sendMessage(true).build());
    combat.combatScript("greatolm");

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().maximum(33).prayerEffectiveness(0.16).build());
    style.animation(7345).attackSpeed(4).attackRange(32);
    style.projectile(NpcCombatProjectile.id(335));
    style.multiNearTarget(3);
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(33).prayerEffectiveness(0.16).build());
    style.animation(7345).attackSpeed(4).attackRange(32);
    style.projectile(NpcCombatProjectile.id(335));
    style.multiNearTarget(3);
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
