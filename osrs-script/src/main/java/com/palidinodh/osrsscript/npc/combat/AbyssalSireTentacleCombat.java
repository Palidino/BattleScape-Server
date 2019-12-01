package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class AbyssalSireTentacleCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.TENTACLE_5912);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    combat.stats(
        NpcCombatStats.builder().attackLevel(180).bonus(CombatBonus.MELEE_ATTACK, 65).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(
        NpcCombatFocus.builder().bypassMapObjects(true).disableFollowingOpponent(true).build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.UNDERNEATH).subHitType(HitType.MELEE)
        .meleeAttackStyle(CombatBonus.ATTACK_CRUSH).build());
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(7109).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
