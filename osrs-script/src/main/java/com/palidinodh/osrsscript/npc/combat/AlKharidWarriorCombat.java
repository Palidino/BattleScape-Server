package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class AlKharidWarriorCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.EASY,
        NpcCombatDropTable.CHANCE_1_IN_128);


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.AL_KHARID_WARRIOR_9);
    combat.hitpoints(NpcCombatHitpoints.total(19));
    combat.stats(
        NpcCombatStats.builder().attackLevel(7).defenceLevel(4).bonus(CombatBonus.ATTACK_STAB, 10)
            .bonus(CombatBonus.ATTACK_SLASH, 10).bonus(CombatBonus.ATTACK_CRUSH, 10)
            .bonus(CombatBonus.ATTACK_MAGIC, 10).bonus(CombatBonus.ATTACK_RANGED, 10)
            .bonus(CombatBonus.DEFENCE_STAB, 12).bonus(CombatBonus.DEFENCE_SLASH, 15)
            .bonus(CombatBonus.DEFENCE_CRUSH, 10).bonus(CombatBonus.DEFENCE_RANGED, 12).build());
    combat.deathAnimation(836).blockAnimation(1156);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(2));
    style.animation(451).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
