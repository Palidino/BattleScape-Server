package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class NezikchenedCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.NEZIKCHENED_187);
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(NpcCombatStats.builder().attackLevel(165).magicLevel(160).rangedLevel(160)
        .defenceLevel(167).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.type(NpcCombatType.DEMON).deathAnimation(67).blockAnimation(65);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(18));
    style.animation(64).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(18).splashOnMiss(true).build());
    style.animation(69).attackSpeed(4);
    style.targetGraphic(new Graphic(131, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (player.getCombat().getLegendsQuest() == 0) {
      player.getGameEncoder()
          .sendMessage("The demon's body falls to the floor in a pile of ashes.");
      player.getGameEncoder().sendMessage("It's time to move deeper into the dungeon...");
      player.getMovement().teleport(2792, 9337);
    }
    if (player.getCombat().getLegendsQuest() == 2) {
      player.getGameEncoder().sendMessage("The demon falls one last time, realizing its defeat.");
    }
    if (player.getCombat().getLegendsQuest() == 0 || player.getCombat().getLegendsQuest() == 2) {
      player.getCombat().setLegendsQuest(player.getCombat().getLegendsQuest() + 1);
      if (player.getCombat().isLegendsQuestComplete()) {
        player.getGameEncoder().sendMessage("<col=ff0000>You have completed Legends Quest!");
        player.getInventory().addOrDropItem(ItemId.COINS, 100_000);
      }
    }
  }
}
