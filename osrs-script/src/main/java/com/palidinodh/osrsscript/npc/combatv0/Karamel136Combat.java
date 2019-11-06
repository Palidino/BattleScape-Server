package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

public class Karamel136Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.KARAMEL_136);
    combat.hitpoints(NpcCombatHitpoints.total(250));
    combat.stats(NpcCombatStats.builder().rangedLevel(100).defenceLevel(100)
        .bonus(CombatBonus.ATTACK_STAB, 50).bonus(CombatBonus.ATTACK_SLASH, 50)
        .bonus(CombatBonus.ATTACK_CRUSH, 50).bonus(CombatBonus.ATTACK_RANGED, 134)
        .bonus(CombatBonus.MELEE_DEFENCE, 150).bonus(CombatBonus.DEFENCE_MAGIC, 150)
        .bonus(CombatBonus.DEFENCE_RANGED, 150).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.combatScript("KaramelCS").deathAnimation(836).blockAnimation(424);

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(2075).attackSpeed(3);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().statDrain(Skills.ATTACK, 1).statDrain(Skills.DEFENCE, 1)
        .statDrain(Skills.STRENGTH, 1).statDrain(Skills.RANGED, 1).statDrain(Skills.MAGIC, 1)
        .build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(7).alwaysMaximum(true).build());
    style.animation(1979).attackSpeed(3);
    style.targetGraphic(new Graphic(369));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().magicBind(34).statDrain(Skills.ATTACK, 1)
        .statDrain(Skills.DEFENCE, 1).statDrain(Skills.STRENGTH, 1).statDrain(Skills.RANGED, 1)
        .statDrain(Skills.MAGIC, 1).build());
    style.phrase("Semolina-Go!").attackCount(2);
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
