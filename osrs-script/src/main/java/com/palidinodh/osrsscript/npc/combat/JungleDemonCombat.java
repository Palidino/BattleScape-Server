package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
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

public class JungleDemonCombat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.JUNGLE_DEMON_195);
    combat.hitpoints(NpcCombatHitpoints.total(170));
    combat.stats(NpcCombatStats.builder().attackLevel(170).magicLevel(170).defenceLevel(170)
        .bonus(CombatBonus.MELEE_ATTACK, 50).bonus(CombatBonus.DEFENCE_SLASH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.type(NpcCombatType.DEMON).deathAnimation(67).blockAnimation(65);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(32));
    style.animation(64).attackSpeed(6).attackRange(2);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(32).splashOnMiss(true).build());
    style.animation(69).attackSpeed(6);
    style.targetGraphic(new Graphic(134, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(32).splashOnMiss(true).build());
    style.animation(69).attackSpeed(6);
    style.targetGraphic(new Graphic(137, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(32));
    style.animation(69).attackSpeed(6);
    style.targetGraphic(new Graphic(140, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(32).splashOnMiss(true).build());
    style.animation(69).attackSpeed(6);
    style.targetGraphic(new Graphic(131, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getCombat().setMonkeyMadness(true);
    player.getMovement().teleport(3109, 3514);
    player.getGameEncoder().sendMessage("<col=ff0000>You have completed Monkey Madness!");
    player.getInventory().addOrDropItem(ItemId.COINS, 25_000);
  }
}
