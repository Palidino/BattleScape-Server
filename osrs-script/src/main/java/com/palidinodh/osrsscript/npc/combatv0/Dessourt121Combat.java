package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Dessourt121Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DESSOURT_121);
    combat.hitpoints(NpcCombatHitpoints.total(130));
    combat.stats(NpcCombatStats.builder().attackLevel(99).defenceLevel(99)
        .bonus(CombatBonus.MELEE_ATTACK, 50).bonus(CombatBonus.DEFENCE_STAB, 10)
        .bonus(CombatBonus.DEFENCE_SLASH, 150).bonus(CombatBonus.DEFENCE_CRUSH, 150).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    combat.deathAnimation(3509).blockAnimation(3505);

    var style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MELEE).subHitType(HitType.MAGIC).build());
    style.damage(NpcCombatDamage.maximum(19));
    style.animation(3508).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(19));
    style.animation(3508).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    style.phrase("Hssssssssssss");
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
