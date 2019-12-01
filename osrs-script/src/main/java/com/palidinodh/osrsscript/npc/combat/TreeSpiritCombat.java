package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class TreeSpiritCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.TREE_SPIRIT_101);
    combat.hitpoints(NpcCombatHitpoints.total(85));
    combat.stats(NpcCombatStats.builder().attackLevel(90).defenceLevel(80).build());
    combat.deathAnimation(5534).blockAnimation(5533);

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(10));
    style.animation(5532).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getCombat().setLostCity(true);
    player.getMovement().teleport(3109, 3514);
    player.getGameEncoder().sendMessage("<col=ff0000>You have completed Lost City!");
    player.getInventory().addOrDropItem(ItemId.COINS, 25_000);
  }
}
