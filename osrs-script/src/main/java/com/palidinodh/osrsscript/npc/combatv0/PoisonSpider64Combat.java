package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class PoisonSpider64Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.POISON_SPIDER_64);
    combat.hitpoints(NpcCombatHitpoints.total(64));
    combat.stats(NpcCombatStats.builder().attackLevel(50).defenceLevel(58)
        .bonus(CombatBonus.DEFENCE_STAB, 20).bonus(CombatBonus.DEFENCE_SLASH, 17)
        .bonus(CombatBonus.DEFENCE_CRUSH, 10).bonus(CombatBonus.DEFENCE_MAGIC, 14)
        .bonus(CombatBonus.DEFENCE_RANGED, 14).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(5329).blockAnimation(5328);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(7));
    style.animation(5327).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(6).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
