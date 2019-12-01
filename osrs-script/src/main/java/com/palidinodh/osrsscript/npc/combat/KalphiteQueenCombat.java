package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import lombok.var;

class KalphiteQueenCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPPLIES_DROP_TABLE = NpcCombatDropTable.builder()
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MONKFISH, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_COMBAT_POTION_2)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANGING_POTION_3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPERANTIPOISON_2)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_CRAB, 2)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_BREW_4)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_4)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4, 2))).build();

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_100);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_3000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KALPHITE_PRINCESS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_2000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_SAND)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_2H_SWORD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_CHAINBODY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KQ_HEAD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KNIFE_P_PLUS_PLUS, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(5);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_KALPHITE_HEAD_13490)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAVA_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RED_DHIDE_BODY)));
    dropTable.drop(
        NpcCombatDropTableDrop.items(new RandomItem(ItemId.WEAPON_POISON_PLUS_PLUS_NOTED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TOADFLAX_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_WEED_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_ARROW, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_RUBY_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_EMERALD_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 15000, 20000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_LOGS_NOTED, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRAPES_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCKET_OF_SAND_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CACTUS_SPINE_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE_NOTED, 250)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_ARROW, 500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WINE_OF_ZAMORAK_NOTED, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTATO_CACTUS_NOTED, 100)));
    drop.table(dropTable.build());


    var combat1 = NpcCombatDefinition.builder();
    combat1.id(NpcId.KALPHITE_QUEEN_333);
    combat1.spawn(
        NpcCombatSpawn.builder().respawnId(NpcId.KALPHITE_QUEEN_333_965).deathDelay(6).build());
    combat1.hitpoints(NpcCombatHitpoints.total(255));
    combat1.stats(NpcCombatStats.builder().attackLevel(300).magicLevel(150).defenceLevel(300)
        .bonus(CombatBonus.DEFENCE_STAB, 50).bonus(CombatBonus.DEFENCE_SLASH, 50)
        .bonus(CombatBonus.DEFENCE_CRUSH, 10).bonus(CombatBonus.DEFENCE_MAGIC, 100)
        .bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    combat1.aggression(NpcCombatAggression.builder().range(8).build());
    combat1.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat1.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    combat1.deathAnimation(6242).blockAnimation(6232);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(6241).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat1.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().maximum(31).ignoreDefence(true).build());
    style.animation(6231).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().statDrain(Skills.PRAYER, 1).includeMiss(true).build());
    style.multiNearTarget(1);
    combat1.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(31).ignoreDefence(true).build());
    style.animation(6231).attackSpeed(4);
    style.castGraphic(new Graphic(278)).targetGraphic(new Graphic(281));
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    combat1.style(style.build());


    var combat2 = NpcCombatDefinition.builder();
    combat2.id(NpcId.KALPHITE_QUEEN_333_965);
    combat2.spawn(
        NpcCombatSpawn.builder().lock(12).animation(6270).graphic(new Graphic(1055)).build());
    combat2.hitpoints(NpcCombatHitpoints.total(255));
    combat2.stats(NpcCombatStats.builder().attackLevel(300).magicLevel(150).defenceLevel(300)
        .bonus(CombatBonus.MELEE_DEFENCE, 100).bonus(CombatBonus.DEFENCE_MAGIC, 10)
        .bonus(CombatBonus.DEFENCE_RANGED, 10).build());
    combat2.aggression(NpcCombatAggression.PLAYERS);
    combat2.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat2.focus(NpcCombatFocus.builder().keepWithinDistance(1).build());
    combat2.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat2.deathAnimation(6233).blockAnimation(6237);
    combat2.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(6235).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().maximum(31).ignoreDefence(true).build());
    style.animation(6234).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().statDrain(Skills.PRAYER, 1).includeMiss(true).build());
    style.multiNearTarget(1);
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(31).ignoreDefence(true).build());
    style.animation(6234).attackSpeed(4);
    style.castGraphic(new Graphic(279)).targetGraphic(new Graphic(281));
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    combat2.style(style.build());


    return Arrays.asList(combat1.build(), combat2.build());
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    SUPPLIES_DROP_TABLE.dropItems(getNpc(), player, dropTile);
  }
}
