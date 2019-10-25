package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
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

public class Torcher49Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.TORCHER_49);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    combat.hitpoints(NpcCombatHitpoints.total(60));
    combat.stats(NpcCombatStats.builder().magicLevel(49).defenceLevel(24).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.combatScript("PestControlCS").deathAnimation(3881).blockAnimation(3880);

    var style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.RANGED).build());
    style.damage(NpcCombatDamage.builder().maximum(8).splashOnMiss(true).build());
    style.animation(3882).attackSpeed(4);
    style.castGraphic(new Graphic(646)).targetGraphic(new Graphic(648, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
