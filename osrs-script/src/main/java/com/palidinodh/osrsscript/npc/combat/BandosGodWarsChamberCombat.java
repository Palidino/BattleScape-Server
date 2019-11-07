package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
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

public class BandosGodWarsChamberCombat extends NpcCombat {
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var graardorDrop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_250);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_GENERAL_GRAARDOR)));
    graardorDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_HILT)));
    graardorDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    graardorDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_CHESTPLATE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_TASSETS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_BOOTS)));
    graardorDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 60, 70)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_LOGS_NOTED, 15, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_4, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_SNAPDRAGON_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    graardorDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATEBODY)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_ORE_NOTED, 15, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 115, 120)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 19581, 21000)));
    graardorDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    graardorDrop.table(dropTable.build());


    var graardorCombat = NpcCombatDefinition.builder();
    graardorCombat.id(NpcId.GENERAL_GRAARDOR_624);
    graardorCombat.phrase("Death to our enemies!").phrase("Brargh!").phrase("Break their bones!")
        .phrase("For the glory of the Big High War God!").phrase("Split their skulls!")
        .phrase("We feast on the bones of our enemies tonight!").phrase("CHAAARGE!")
        .phrase("Crush them underfoot!");
    graardorCombat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    graardorCombat.hitpoints(NpcCombatHitpoints.total(255));
    graardorCombat.stats(NpcCombatStats.builder().attackLevel(280).magicLevel(80).rangedLevel(350)
        .defenceLevel(250).bonus(CombatBonus.MELEE_ATTACK, 120)
        .bonus(CombatBonus.ATTACK_RANGED, 100).bonus(CombatBonus.MELEE_DEFENCE, 90)
        .bonus(CombatBonus.DEFENCE_MAGIC, 298).bonus(CombatBonus.DEFENCE_RANGED, 90).build());
    graardorCombat.aggression(NpcCombatAggression.builder().range(16).build());
    graardorCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    graardorCombat
        .focus(NpcCombatFocus.builder().keepWithinDistance(1).singleTargetFocus(true).build());
    graardorCombat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    graardorCombat.deathAnimation(7020).blockAnimation(7019);
    graardorCombat.drop(graardorDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(60));
    style.animation(7018).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    graardorCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.builder().minimum(15).maximum(35).build());
    style.animation(7021).attackSpeed(6).attackRange(1);
    style.projectile(NpcCombatProjectile.id(335));
    style.multiTarget(true);
    graardorCombat.style(style.build());


    var grimspikeDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    grimspikeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_CHESTPLATE).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_TASSETS).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_BOOTS).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 120))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    grimspikeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 25, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 15, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COMBAT_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RIGHT_EYE_PATCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHILLI_POTATO, 3)));
    grimspikeDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    grimspikeDrop.table(dropTable.build());


    var grimspikeCombat = NpcCombatDefinition.builder();
    grimspikeCombat.id(NpcId.SERGEANT_GRIMSPIKE_142);
    grimspikeCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.GENERAL_GRAARDOR_624).respawnWithId(NpcId.SERGEANT_STRONGSTACK_141)
        .respawnWithId(NpcId.SERGEANT_STEELWILL_142).build());
    grimspikeCombat.hitpoints(NpcCombatHitpoints.total(146));
    grimspikeCombat.stats(NpcCombatStats.builder().attackLevel(80).magicLevel(50).rangedLevel(150)
        .defenceLevel(132).bonus(CombatBonus.ATTACK_RANGED, 20).build());
    grimspikeCombat.aggression(NpcCombatAggression.builder().range(16).build());
    grimspikeCombat
        .killCount(NpcCombatKillCount.builder().asName("General Graardor's bodyguard").build());
    grimspikeCombat.deathAnimation(6156).blockAnimation(6155);
    grimspikeCombat.drop(grimspikeDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(21));
    style.animation(6154).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    grimspikeCombat.style(style.build());


    var steelwillDrop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
            .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    steelwillDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_CHESTPLATE).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_TASSETS).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_BOOTS).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 120))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    steelwillDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 25, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LIMPWURT_ROOT_NOTED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BEER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KEBAB)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COMBAT_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RIGHT_EYE_PATCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHILLI_POTATO, 3)));
    steelwillDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    steelwillDrop.table(dropTable.build());


    var steelwillCombat = NpcCombatDefinition.builder();
    steelwillCombat.id(NpcId.SERGEANT_STEELWILL_142);
    steelwillCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.GENERAL_GRAARDOR_624).respawnWithId(NpcId.SERGEANT_STRONGSTACK_141)
        .respawnWithId(NpcId.SERGEANT_GRIMSPIKE_142).build());
    steelwillCombat.hitpoints(NpcCombatHitpoints.total(127));
    steelwillCombat
        .stats(NpcCombatStats.builder().attackLevel(80).magicLevel(150).defenceLevel(150).build());
    steelwillCombat.aggression(NpcCombatAggression.builder().range(16).build());
    steelwillCombat
        .killCount(NpcCombatKillCount.builder().asName("General Graardor's bodyguard").build());
    steelwillCombat.deathAnimation(6156).blockAnimation(6155);
    steelwillCombat.drop(steelwillDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(16).splashOnMiss(true).build());
    style.animation(6154).attackSpeed(5);
    style.castGraphic(new Graphic(1216)).targetGraphic(new Graphic(166, 124));
    style.projectile(NpcCombatProjectile.id(335));
    steelwillCombat.style(style.build());


    var strongstackDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    strongstackDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_CHESTPLATE).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_TASSETS).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BANDOS_BOOTS).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 120))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    strongstackDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 25, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 19, 28)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1450)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COMBAT_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RIGHT_EYE_PATCH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHILLI_POTATO, 3)));
    strongstackDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    strongstackDrop.table(dropTable.build());


    var strongstackCombat = NpcCombatDefinition.builder();
    strongstackCombat.id(NpcId.SERGEANT_STRONGSTACK_141);
    strongstackCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.GENERAL_GRAARDOR_624).respawnWithId(NpcId.SERGEANT_STEELWILL_142)
        .respawnWithId(NpcId.SERGEANT_GRIMSPIKE_142).build());
    strongstackCombat.hitpoints(NpcCombatHitpoints.total(128));
    strongstackCombat.stats(NpcCombatStats.builder().attackLevel(124).magicLevel(50).rangedLevel(50)
        .defenceLevel(125).build());
    strongstackCombat.aggression(NpcCombatAggression.builder().range(16).build());
    strongstackCombat
        .killCount(NpcCombatKillCount.builder().asName("General Graardor's bodyguard").build());
    strongstackCombat.deathAnimation(6156).blockAnimation(6155);
    strongstackCombat.drop(strongstackDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(6154).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    strongstackCombat.style(style.build());


    return Arrays.asList(graardorCombat.build(), grimspikeCombat.build(), steelwillCombat.build(),
        strongstackCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    if (npc.getId() != NpcId.GENERAL_GRAARDOR_624) {
      return;
    }
    var respawns = new int[] {NpcId.SERGEANT_GRIMSPIKE_142, NpcId.SERGEANT_STEELWILL_142,
        NpcId.SERGEANT_STRONGSTACK_141};
    for (var id : respawns) {
      var respawningNpc = npc.getController().getNpc(id);
      if (respawningNpc != null && !respawningNpc.isVisible() && respawningNpc.getRespawns()) {
        respawningNpc.restore();
      }
    }
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getArea().script("increase_bandos_killcount");
  }
}
