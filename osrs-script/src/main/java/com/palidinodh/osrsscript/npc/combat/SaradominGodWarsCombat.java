package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class SaradominGodWarsCombat extends NpcCombat {
  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var knightOfSaradominDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    knightOfSaradominDrop.table(dropTable.build());


    var knightOfSaradomin101Combat = NpcCombatDefinition.builder();
    knightOfSaradomin101Combat.id(NpcId.KNIGHT_OF_SARADOMIN_101);
    knightOfSaradomin101Combat.hitpoints(NpcCombatHitpoints.total(108));
    knightOfSaradomin101Combat.stats(NpcCombatStats.builder().attackLevel(75).magicLevel(60)
        .defenceLevel(82).bonus(CombatBonus.MELEE_ATTACK, 13).bonus(CombatBonus.DEFENCE_STAB, 12)
        .bonus(CombatBonus.DEFENCE_SLASH, 14).bonus(CombatBonus.DEFENCE_CRUSH, 13)
        .bonus(CombatBonus.DEFENCE_RANGED, 13).build());
    knightOfSaradomin101Combat.aggression(NpcCombatAggression.PLAYERS);
    knightOfSaradomin101Combat.deathAnimation(836).blockAnimation(410);
    knightOfSaradomin101Combat.drop(knightOfSaradominDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(406).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    knightOfSaradomin101Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(407).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    knightOfSaradomin101Combat.style(style.build());


    var knightOfSaradomin103Combat = NpcCombatDefinition.builder();
    knightOfSaradomin103Combat.id(NpcId.KNIGHT_OF_SARADOMIN_103);
    knightOfSaradomin103Combat.hitpoints(NpcCombatHitpoints.total(135));
    knightOfSaradomin103Combat.stats(NpcCombatStats.builder().attackLevel(70).magicLevel(60)
        .defenceLevel(70).bonus(CombatBonus.MELEE_ATTACK, 8).bonus(CombatBonus.DEFENCE_STAB, 10)
        .bonus(CombatBonus.DEFENCE_SLASH, 10).bonus(CombatBonus.DEFENCE_CRUSH, 7)
        .bonus(CombatBonus.DEFENCE_RANGED, 13).build());
    knightOfSaradomin103Combat.aggression(NpcCombatAggression.PLAYERS);
    knightOfSaradomin103Combat.deathAnimation(836).blockAnimation(410);
    knightOfSaradomin103Combat.drop(knightOfSaradominDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(11));
    style.animation(406).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    knightOfSaradomin103Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(11));
    style.animation(407).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    knightOfSaradomin103Combat.style(style.build());


    var saradominPriestDrop = NpcCombatDrop.builder();
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    saradominPriestDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    saradominPriestDrop.table(dropTable.build());


    var saradominPriestCombat = NpcCombatDefinition.builder();
    saradominPriestCombat.id(NpcId.SARADOMIN_PRIEST_113);
    saradominPriestCombat.hitpoints(NpcCombatHitpoints.total(89));
    saradominPriestCombat.stats(NpcCombatStats.builder().attackLevel(120).magicLevel(125)
        .defenceLevel(120).bonus(CombatBonus.MELEE_ATTACK, 9).bonus(CombatBonus.DEFENCE_STAB, 12)
        .bonus(CombatBonus.DEFENCE_SLASH, 14).bonus(CombatBonus.DEFENCE_CRUSH, 13)
        .bonus(CombatBonus.DEFENCE_MAGIC, 5).bonus(CombatBonus.DEFENCE_RANGED, 13).build());
    saradominPriestCombat.aggression(NpcCombatAggression.PLAYERS);
    saradominPriestCombat.deathAnimation(836).blockAnimation(404);
    saradominPriestCombat.drop(saradominPriestDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(811).attackSpeed(5);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    saradominPriestCombat.style(style.build());


    var spiritualMageDrop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
            .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BOOTS)));
    spiritualMageDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASTRAL_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUD_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_RUNE, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIPOISON_3_NOTED, 5)));
    spiritualMageDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 90)));
    spiritualMageDrop.table(dropTable.build());


    var spiritualMageCombat = NpcCombatDefinition.builder();
    spiritualMageCombat.id(NpcId.SPIRITUAL_MAGE_120);
    spiritualMageCombat.hitpoints(NpcCombatHitpoints.total(85));
    spiritualMageCombat.stats(
        NpcCombatStats.builder().magicLevel(160).defenceLevel(86).bonus(CombatBonus.DEFENCE_STAB, 8)
            .bonus(CombatBonus.DEFENCE_SLASH, 7).bonus(CombatBonus.DEFENCE_CRUSH, 3)
            .bonus(CombatBonus.DEFENCE_MAGIC, 16).bonus(CombatBonus.DEFENCE_RANGED, 2).build());
    spiritualMageCombat.slayer(NpcCombatSlayer.builder().level(83).build());
    spiritualMageCombat.aggression(NpcCombatAggression.PLAYERS);
    spiritualMageCombat.deathAnimation(836).blockAnimation(404);
    spiritualMageCombat.drop(spiritualMageDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(17));
    style.animation(811).attackSpeed(4);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    spiritualMageCombat.style(style.build());


    return Arrays.asList(knightOfSaradomin101Combat.build(), knightOfSaradomin103Combat.build(),
        saradominPriestCombat.build(), spiritualMageCombat.build());
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getArea().script("increase_saradomin_killcount");
    if (npc.getController().inWilderness() && PRandom.randomE(60) == 0) {
      npc.getController().addMapItem(new Item(ItemId.ECUMENICAL_KEY, 1), dropTile, player);
    }
  }

  @Override
  public boolean canBeAggressiveHook(Entity opponent) {
    if (!(opponent instanceof Player)) {
      return true;
    }
    var player = (Player) opponent;
    if (player.getEquipment().hasItemIC("Saradomin")
        || player.getEquipment().getShieldId() == ItemId.HOLY_BOOK
        || player.getEquipment().getAmmoId() == ItemId.HOLY_BLESSING
        || player.getEquipment().getNeckId() == ItemId.HOLY_SYMBOL
        || player.getEquipment().hasItemIC("Monk's robe")
        || player.getEquipment().getFootId() == ItemId.HOLY_SANDALS) {
      return false;
    }
    return true;
  }
}
