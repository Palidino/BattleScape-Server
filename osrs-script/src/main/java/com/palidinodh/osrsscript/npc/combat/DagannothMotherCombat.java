package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
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
import com.palidinodh.random.PRandom;
import lombok.var;

class DagannothMotherCombat extends NpcCombat {
  @Inject
  private Npc npc;
  private int changeDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DAGANNOTH_MOTHER_100_983).id(NpcId.DAGANNOTH_MOTHER_100_984)
        .id(NpcId.DAGANNOTH_MOTHER_100_985).id(NpcId.DAGANNOTH_MOTHER_100_986)
        .id(NpcId.DAGANNOTH_MOTHER_100_987).id(NpcId.DAGANNOTH_MOTHER_100_988);
    combat.hitpoints(NpcCombatHitpoints.total(120));
    combat.stats(NpcCombatStats.builder().attackLevel(78).rangedLevel(50).defenceLevel(81)
        .bonus(CombatBonus.MELEE_DEFENCE, 150).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.deathAnimation(1342).blockAnimation(1340);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(1341).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(12));
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
        if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_983) {
          npc.setTransformationId(NpcId.DAGANNOTH_MOTHER_100_984);
          npc.setForceMessage("Krrrrrrk");
        } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_984) {
          npc.setTransformationId(NpcId.DAGANNOTH_MOTHER_100_988);
          npc.setForceMessage("Chkhkhkhkhk");
        } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_988) {
          npc.setTransformationId(NpcId.DAGANNOTH_MOTHER_100_986);
          npc.setForceMessage("Krrrrrrssssssss");
        } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_986) {
          npc.setTransformationId(NpcId.DAGANNOTH_MOTHER_100_985);
          npc.setForceMessage("Sssssrrrkkkkkk");
        } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_985) {
          npc.setTransformationId(NpcId.DAGANNOTH_MOTHER_100_987);
          npc.setForceMessage("Krkrkrkrkrkrkrkr");
        } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_987) {
          npc.setTransformationId(NpcId.DAGANNOTH_MOTHER_100_983);
          npc.setForceMessage("Tktktktktktkt");
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
    if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_983
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("wind"))) {
      return 0;
    } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_988 && hitType != HitType.MELEE) {
      return 0;
    } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_984
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("water"))) {
      return 0;
    } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_985
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("fire"))) {
      return 0;
    } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_987 && hitType != HitType.RANGED) {
      return 0;
    } else if (npc.getId() == NpcId.DAGANNOTH_MOTHER_100_986
        && (hitType != HitType.MAGIC || spell == null || !spell.getName().startsWith("earth"))) {
      return 0;
    }
    return damage;
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getGameEncoder().sendMessage("You have defeated the Dagannoth Mother!");
    player.getCombat().setHorrorFromTheDeep(true);
    player.getGameEncoder().sendMessage("<col=ff0000>You have completed Horror from the Deep!");
    player.getInventory().addOrDropItem(ItemId.COINS, 25_000);
  }

  private void setChangeDelay() {
    changeDelay = 25 + PRandom.randomI(8);
  }
}
