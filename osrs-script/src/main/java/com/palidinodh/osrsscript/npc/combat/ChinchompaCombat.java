package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

class ChinchompaCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CHINCHOMPA_1);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(6).build());
    combat.hitpoints(NpcCombatHitpoints.total(2));
    combat.deathAnimation(5184);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(1));
    style.animation(5181).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var carnivorousCombat = NpcCombatDefinition.builder();
    carnivorousCombat.id(NpcId.CARNIVOROUS_CHINCHOMPA_2);
    carnivorousCombat.spawn(NpcCombatSpawn.builder().respawnDelay(6).build());
    carnivorousCombat.hitpoints(NpcCombatHitpoints.total(2));
    carnivorousCombat.deathAnimation(5184);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(1));
    style.animation(5181).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    carnivorousCombat.style(style.build());


    var blackCombat = NpcCombatDefinition.builder();
    blackCombat.id(NpcId.BLACK_CHINCHOMPA_2);
    blackCombat.spawn(NpcCombatSpawn.builder().respawnDelay(6).build());
    blackCombat.hitpoints(NpcCombatHitpoints.total(2));
    blackCombat.deathAnimation(5184);

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(1));
    style.animation(5181).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    blackCombat.style(style.build());


    return Arrays.asList(combat.build(), carnivorousCombat.build(), blackCombat.build());
  }
}
