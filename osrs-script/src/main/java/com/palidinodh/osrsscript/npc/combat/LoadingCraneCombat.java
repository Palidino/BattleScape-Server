package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
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

public class LoadingCraneCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.LOADING_CRANE);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    combat.stats(NpcCombatStats.builder().attackLevel(70).build());
    combat.aggression(NpcCombatAggression.builder().always(true).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(
        NpcCombatFocus.builder().bypassMapObjects(true).disableFollowingOpponent(true).build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(1452).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public boolean canAttackEntityHook(NpcCombatStyle combatStyle, Entity opponent) {
    return npc.getWorld().getTargetNPC(NpcId.TREUS_DAYTH_95, opponent) != null
        && npc.withinDistance(opponent, 1);
  }
}
