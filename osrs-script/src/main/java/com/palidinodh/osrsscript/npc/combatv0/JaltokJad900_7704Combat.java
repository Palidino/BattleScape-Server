package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class JaltokJad900_7704Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JALTOK_JAD_900_7704);
    combat.hitpoints(NpcCombatHitpoints.total(350));
    combat.stats(NpcCombatStats.builder().attackLevel(750).magicLevel(510).rangedLevel(1020)
        .defenceLevel(480).bonus(CombatBonus.ATTACK_STAB, 80).bonus(CombatBonus.ATTACK_SLASH, 80)
        .bonus(CombatBonus.ATTACK_CRUSH, 80).bonus(CombatBonus.ATTACK_MAGIC, 100)
        .bonus(CombatBonus.ATTACK_RANGED, 80).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    combat.combatScript("TzTokJadCS").deathAnimation(7594);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(113));
    style.animation(7590).attackSpeed(9);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(113));
    style.attackSpeed(9).attackRange(28);
    style.targetTileGraphic(new Graphic(451));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(113));
    style.attackSpeed(9).attackRange(28);
    style.projectile(NpcCombatProjectile.builder().id(448).startHeight(124).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
