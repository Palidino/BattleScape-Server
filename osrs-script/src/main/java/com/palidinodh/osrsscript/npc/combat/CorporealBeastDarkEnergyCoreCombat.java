package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.random.PRandom;
import lombok.var;

public class CorporealBeastDarkEnergyCoreCombat extends NpcCombat {
  private Npc npc;
  private int jumpTime;
  private Tile jumpTile;
  private boolean stunned;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DARK_ENERGY_CORE_75);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(6000).build());
    combat.hitpoints(NpcCombatHitpoints.total(25));
    combat.stats(NpcCombatStats.builder().defenceLevel(20).bonus(CombatBonus.MELEE_DEFENCE, 10)
        .bonus(CombatBonus.DEFENCE_MAGIC, -5).bonus(CombatBonus.DEFENCE_RANGED, 10).build());
    combat.aggression(NpcCombatAggression.builder().range(16).always(true).build());
    combat.focus(
        NpcCombatFocus.builder().retaliationDisabled(true).disableFollowingOpponent(true).build());
    combat.drop(drop.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void restoreHook() {
    jumpTime = 0;
    jumpTile = null;
    stunned = false;
  }

  @Override
  public void tickStartHook() {
    if (npc.isDead() || npc.getHitDelay() > 0) {
      return;
    }
    if (jumpTime > 0) {
      jumpTime--;
      if (jumpTime == 4) {
        npc.getController().sendMapProjectile(null, npc, jumpTile, 319, 13, 1, 1, 55, 16, 64);
        npc.setVisible(false);
      } else if (jumpTime == 2) {
        npc.setTile(jumpTile);
        npc.setVisible(true);
      } else if (jumpTime == 0) {
        npc.unlock();
      }
    }
    if (!npc.isVisible() || jumpTime > 0) {
      return;
    }
    var players = npc.getController().getPlayers();
    Collections.shuffle(players);
    var playerInRange = false;
    var corporealBeast = npc.getController().getNpc(NpcId.CORPOREAL_BEAST_785);
    if (corporealBeast == null) {
      npc.getWorld().removeNpc(npc);
      return;
    }
    for (var player : players) {
      if (player.isLocked() || !npc.withinDistance(player, 1)) {
        continue;
      }
      playerInRange = true;
      var damage = 6 + PRandom.randomI(4);
      var hitEvent = new HitEvent(0, player, npc, new Hit(damage));
      player.addHit(hitEvent);
      if (corporealBeast != null && corporealBeast.isVisible() && !corporealBeast.isDead()) {
        corporealBeast.adjustHitpoints(damage, 0);
      }
    }
    npc.setHitDelay(stunned ? 8 : 2);
    if (!playerInRange) {
      stunned = false;
      npc.setAttacking(false);
      npc.setEngagingEntity(null);
      npc.getCombat().checkPlayerAggression();
      if (npc.getEngagingEntity() != null) {
        npc.lock();
        npc.setAnimation(1689);
        jumpTime = 6;
        jumpTile = new Tile(npc.getEngagingEntity());
      }
    }
  }

  @Override
  public void npcSetPoisonHook(int damage, int count, int delay) {
    stunned = true;
  }
}
