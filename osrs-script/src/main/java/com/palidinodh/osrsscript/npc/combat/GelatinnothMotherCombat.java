package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class GelatinnothMotherCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private int changeDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.GELATINNOTH_MOTHER_130).id(NpcId.GELATINNOTH_MOTHER_130_4885)
        .id(NpcId.GELATINNOTH_MOTHER_130_4886).id(NpcId.GELATINNOTH_MOTHER_130_4887)
        .id(NpcId.GELATINNOTH_MOTHER_130_4888).id(NpcId.GELATINNOTH_MOTHER_130_4889);
    combat.hitpoints(NpcCombatHitpoints.total(240));
    combat.stats(NpcCombatStats.builder().attackLevel(78).rangedLevel(50).defenceLevel(81)
        .bonus(CombatBonus.MELEE_DEFENCE, 150).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.deathAnimation(1342).blockAnimation(1340);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(1341).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(20));
    style.animation(1343).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.attackCount(2);
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void restoreHook() {
    setChangeDelay();
  }

  @Override
  public void tickStartHook() {
    if (npc.isLocked()) {
      return;
    }
    if (changeDelay > 0) {
      changeDelay--;
      if (changeDelay == 0) {
        setChangeDelay();
        if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130) {
          npc.setTransformationId(NpcId.GELATINNOTH_MOTHER_130_4886);
        } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4886) {
          npc.setTransformationId(NpcId.GELATINNOTH_MOTHER_130_4885);
        } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4885) {
          npc.setTransformationId(NpcId.GELATINNOTH_MOTHER_130_4889);
        } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4889) {
          npc.setTransformationId(NpcId.GELATINNOTH_MOTHER_130_4887);
        } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4887) {
          npc.setTransformationId(NpcId.GELATINNOTH_MOTHER_130_4888);
        } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4888) {
          npc.setTransformationId(NpcId.GELATINNOTH_MOTHER_130);
        }
      }
    }
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (!(opponent instanceof Player)) {
      return damage;
    }
    var player = (Player) opponent;
    var spell = player.getMagic().getActiveSpell();
    if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("wind"))) {
      return 0;
    } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4885 && hitType != HitType.MELEE) {
      return 0;
    } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4886
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("water"))) {
      return 0;
    } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4887
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("fire"))) {
      return 0;
    } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4888 && hitType != HitType.RANGED) {
      return 0;
    } else if (npc.getId() == NpcId.GELATINNOTH_MOTHER_130_4889
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("earth"))) {
      return 0;
    }
    return damage;
  }

  public void setChangeDelay() {
    changeDelay = 25 + PRandom.randomI(8);
  }
}
