package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Dawn228Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DAWN_228);
    combat.spawn(NpcCombatSpawn.builder().lock(16).direction(3).animation(7766).build());
    combat.hitpoints(NpcCombatHitpoints.total(450));
    combat.stats(NpcCombatStats.builder().attackLevel(140).magicLevel(100).rangedLevel(140)
        .defenceLevel(100).bonus(CombatBonus.DEFENCE_MAGIC, 80).build());
    combat.slayer(NpcCombatSlayer.builder().level(75).build());
    combat.aggression(NpcCombatAggression.builder().range(20).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.combatScript("GrotesqueGuardianCS");

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7769).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7770).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.attackCount(2);
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
