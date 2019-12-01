package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
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
import lombok.var;

class JalAkCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private HitType attackStyle;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JAL_AK_165);
    combat.hitpoints(NpcCombatHitpoints.total(40));
    combat.stats(NpcCombatStats.builder().attackLevel(160).magicLevel(160).rangedLevel(160)
        .defenceLevel(95).bonus(CombatBonus.ATTACK_STAB, 45).bonus(CombatBonus.ATTACK_SLASH, 45)
        .bonus(CombatBonus.ATTACK_CRUSH, 45).bonus(CombatBonus.ATTACK_MAGIC, 45)
        .bonus(CombatBonus.ATTACK_RANGED, 45).bonus(CombatBonus.MELEE_DEFENCE, 25)
        .bonus(CombatBonus.DEFENCE_MAGIC, 25).bonus(CombatBonus.DEFENCE_RANGED, 25).build());
    combat.aggression(NpcCombatAggression.builder().range(8).always(true).sameTarget(true).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.deathAnimation(7584).blockAnimation(7585);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(29));
    style.animation(7587).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(29));
    style.animation(7587).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(29));
    style.animation(7587).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void restoreHook() {
    attackStyle = HitType.NONE;
  }

  @Override
  public void tickStartHook() {
    if (npc.getHitDelay() == 3) {
      var withinOneTile = npc.withinDistance(npc.getEngagingEntity(), 1);
      attackStyle =
          withinOneTile ? HitType.getRandomType(HitType.MELEE, HitType.RANGED, HitType.MAGIC)
              : HitType.getRandomType(HitType.RANGED, HitType.MAGIC);
      if (!withinOneTile && npc.getEngagingEntity() instanceof Player) {
        var player = (Player) npc.getEngagingEntity();
        if (player.getPrayer().hasActive("protect from missiles")) {
          attackStyle = HitType.MAGIC;
        } else if (player.getPrayer().hasActive("protect from magic")) {
          attackStyle = HitType.RANGED;
        }
      }
    }
  }

  @Override
  public HitType attackTickHitTypeHook(HitType hitType, Entity opponent) {
    return attackStyle != HitType.NONE ? attackStyle : hitType;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    var mej = new Npc(npc.getController(), NpcId.JAL_AKREK_KET_70, npc);
    mej.getMovement().setClipNPCs(true);
    mej.getCombat().setTarget(player);
    player.getCombat().getTzHaar().addMonster(mej);
    var xil = new Npc(npc.getController(), NpcId.JAL_AKREK_XIL_70, npc.getX() + 1, npc.getY(),
        npc.getHeight());
    xil.getMovement().setClipNPCs(true);
    xil.getCombat().setTarget(player);
    player.getCombat().getTzHaar().addMonster(xil);
    var ket = new Npc(npc.getController(), NpcId.JAL_AKREK_MEJ_70, npc.getX() + 2, npc.getY() + 1,
        npc.getHeight());
    ket.getMovement().setClipNPCs(true);
    ket.getCombat().setTarget(player);
    player.getCombat().getTzHaar().addMonster(ket);
  }
}
