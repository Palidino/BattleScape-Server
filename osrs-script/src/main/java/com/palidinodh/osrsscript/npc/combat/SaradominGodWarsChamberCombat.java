package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

class SaradominGodWarsChamberCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var zilyanaDrop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_250);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_ZILYANA)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_HILT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CROSSBOW)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMINS_LIGHT)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_SWORD)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_BREW_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_4, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_DEFENCE_4, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_POTION_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DART, 35, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 95, 105)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATESKIRT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 19362, 20300)));
    zilyanaDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    zilyanaDrop.table(dropTable.build());


    var zilyanaCombat = NpcCombatDefinition.builder();
    zilyanaCombat.id(NpcId.COMMANDER_ZILYANA_596);
    zilyanaCombat.phrase("Death to the enemies of the light!").phrase("Slay the evil ones!")
        .phrase("Saradomin lend me strength!").phrase("By the power of Saradomin!")
        .phrase("May Saradomin be my sword.").phrase("Good will always triumph!")
        .phrase("Forward! Our allies are with us!").phrase("Saradomin is with us!")
        .phrase("In the name of Saradomin!").phrase("All praise Saradomin!");
    zilyanaCombat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    zilyanaCombat.hitpoints(NpcCombatHitpoints.total(255));
    zilyanaCombat.stats(NpcCombatStats.builder().attackLevel(280).magicLevel(300).rangedLevel(250)
        .defenceLevel(300).bonus(CombatBonus.MELEE_ATTACK, 195).bonus(CombatBonus.ATTACK_MAGIC, 200)
        .bonus(CombatBonus.MELEE_DEFENCE, 100).bonus(CombatBonus.DEFENCE_MAGIC, 100)
        .bonus(CombatBonus.DEFENCE_RANGED, 100).build());
    zilyanaCombat.aggression(NpcCombatAggression.builder().range(16).build());
    zilyanaCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    zilyanaCombat
        .focus(NpcCombatFocus.builder().keepWithinDistance(1).singleTargetFocus(true).build());
    zilyanaCombat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    zilyanaCombat.deathAnimation(6968).blockAnimation(6969);
    zilyanaCombat.drop(zilyanaDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(6967).attackSpeed(2);
    style.projectile(NpcCombatProjectile.id(335));
    zilyanaCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(6970).attackSpeed(2).attackRange(1);
    style.targetGraphic(new Graphic(1221));
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    zilyanaCombat.style(style.build());


    var breeDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    breeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_SWORD).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 40))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    breeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNICORN_HORN_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPE_GRASS, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUMMER_PIE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MONKFISH, 3)));
    breeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    breeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    breeDrop.table(dropTable.build());


    var breeCombat = NpcCombatDefinition.builder();
    breeCombat.id(NpcId.BREE_146);
    breeCombat
        .spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.COMMANDER_ZILYANA_596)
            .respawnWithId(NpcId.STARLIGHT_149).respawnWithId(NpcId.GROWLER_139).build());
    breeCombat.hitpoints(NpcCombatHitpoints.total(162));
    breeCombat.stats(NpcCombatStats.builder().attackLevel(162).magicLevel(80).rangedLevel(150)
        .defenceLevel(130).bonus(CombatBonus.MELEE_ATTACK, 10).bonus(CombatBonus.DEFENCE_STAB, 12)
        .bonus(CombatBonus.DEFENCE_SLASH, 14).bonus(CombatBonus.DEFENCE_CRUSH, 14)
        .bonus(CombatBonus.DEFENCE_MAGIC, 18).bonus(CombatBonus.DEFENCE_RANGED, 5).build());
    breeCombat.aggression(NpcCombatAggression.builder().range(16).build());
    breeCombat
        .killCount(NpcCombatKillCount.builder().asName("Commander Zilyana's bodyguard").build());
    breeCombat.deathAnimation(7028).blockAnimation(7027);
    breeCombat.drop(breeDrop.build());


    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(7026).attackSpeed(5);
    style.castGraphic(new Graphic(1185));
    style.projectile(NpcCombatProjectile.id(335));
    breeCombat.style(style.build());


    var growlerDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    growlerDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_SWORD).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 40))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    growlerDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUMMER_PIE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNICORN_HORN_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MONKFISH, 3)));
    growlerDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 2000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPE_GRASS_NOTED, 5)));
    growlerDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    growlerDrop.table(dropTable.build());


    var growlerCombat = NpcCombatDefinition.builder();
    growlerCombat.id(NpcId.GROWLER_139);
    growlerCombat
        .spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.COMMANDER_ZILYANA_596)
            .respawnWithId(NpcId.STARLIGHT_149).respawnWithId(NpcId.BREE_146).build());
    growlerCombat.hitpoints(NpcCombatHitpoints.total(146));
    growlerCombat.stats(NpcCombatStats.builder().attackLevel(100).magicLevel(150).defenceLevel(120)
        .bonus(CombatBonus.MELEE_ATTACK, 10).bonus(CombatBonus.DEFENCE_STAB, 12)
        .bonus(CombatBonus.DEFENCE_SLASH, 14).bonus(CombatBonus.DEFENCE_CRUSH, 14)
        .bonus(CombatBonus.DEFENCE_MAGIC, 18).bonus(CombatBonus.DEFENCE_RANGED, 5).build());
    growlerCombat.aggression(NpcCombatAggression.builder().range(16).build());
    growlerCombat
        .killCount(NpcCombatKillCount.builder().asName("Commander Zilyana's bodyguard").build());
    growlerCombat.deathAnimation(7034).blockAnimation(7035);
    growlerCombat.drop(growlerDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(16).splashOnMiss(true).build());
    style.animation(7037).attackSpeed(5);
    style.castGraphic(new Graphic(1182)).targetGraphic(new Graphic(1184));
    style.projectile(NpcCombatProjectile.id(335));
    growlerCombat.style(style.build());


    var starlightDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    starlightDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_SWORD).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 40))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    starlightDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUMMER_PIE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNICORN_HORN_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MONKFISH, 3)));
    starlightDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 7, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 97, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 2000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPE_GRASS_NOTED, 5)));
    starlightDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    starlightDrop.table(dropTable.build());


    var starlightCombat = NpcCombatDefinition.builder();
    starlightCombat.id(NpcId.STARLIGHT_149);
    starlightCombat
        .spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.COMMANDER_ZILYANA_596)
            .respawnWithId(NpcId.GROWLER_139).respawnWithId(NpcId.BREE_146).build());
    starlightCombat.hitpoints(NpcCombatHitpoints.total(160));
    starlightCombat.stats(NpcCombatStats.builder().attackLevel(120).magicLevel(125)
        .defenceLevel(120).bonus(CombatBonus.MELEE_ATTACK, 60).bonus(CombatBonus.DEFENCE_STAB, 12)
        .bonus(CombatBonus.DEFENCE_SLASH, 14).bonus(CombatBonus.DEFENCE_CRUSH, 13)
        .bonus(CombatBonus.DEFENCE_MAGIC, 5).bonus(CombatBonus.DEFENCE_RANGED, 13).build());
    starlightCombat.aggression(NpcCombatAggression.builder().range(16).build());
    starlightCombat
        .killCount(NpcCombatKillCount.builder().asName("Commander Zilyana's bodyguard").build());
    starlightCombat.deathAnimation(6377).blockAnimation(6375);
    starlightCombat.drop(starlightDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(6376).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    starlightCombat.style(style.build());


    return Arrays.asList(zilyanaCombat.build(), breeCombat.build(), growlerCombat.build(),
        starlightCombat.build());
  }

  @Override
  public void restoreHook() {
    if (npc.getId() != NpcId.COMMANDER_ZILYANA_596) {
      return;
    }
    var respawns = new int[] { NpcId.STARLIGHT_149, NpcId.GROWLER_139, NpcId.BREE_146 };
    for (var id : respawns) {
      var respawningNpc = npc.getController().getNpc(id);
      if (respawningNpc != null && !respawningNpc.isVisible() && respawningNpc.getRespawns()) {
        respawningNpc.restore();
      }
    }
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getArea().script("increase_saradomin_killcount");
  }
}
