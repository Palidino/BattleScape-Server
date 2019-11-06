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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Kolodion112Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.KOLODION_112);
    combat.spawn(NpcCombatSpawn.builder().lock(4).phrase("Aaaaaaaarrgghhhh! The power!")
        .animation(4681).graphic(new Graphic(86, 100)).build());
    combat.hitpoints(NpcCombatHitpoints.total(107));
    combat
        .stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.combatScript("KolodionCS").deathAnimation(67).blockAnimation(65);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
