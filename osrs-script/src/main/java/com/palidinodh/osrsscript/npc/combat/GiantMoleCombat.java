package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.AchievementDiaryTask;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class GiantMoleCombat extends NpcCombat {
  public static final Tile[] BURROWS =
      {new Tile(1736, 5227), new Tile(1776, 5236), new Tile(1752, 5204), new Tile(1769, 5199),
          new Tile(1778, 5207), new Tile(1740, 5187), new Tile(1745, 5170), new Tile(1774, 5173),
          new Tile(1759, 5162), new Tile(1739, 5150), new Tile(1752, 5149)};

  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_500);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_3000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BABY_MOLE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OYSTER_PEARLS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRON_ARROW, 690)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRON_ORE_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_LOGS_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 105)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 105)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 7)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AMULET_OF_STRENGTH)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BIG_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MOLE_CLAW)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MOLE_SKIN, 1, 3)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.GIANT_MOLE_230);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(15).build());
    combat.hitpoints(NpcCombatHitpoints.total(200));
    combat.stats(NpcCombatStats.builder().attackLevel(200).magicLevel(200).defenceLevel(200)
        .bonus(CombatBonus.DEFENCE_STAB, 60).bonus(CombatBonus.DEFENCE_SLASH, 80)
        .bonus(CombatBonus.DEFENCE_CRUSH, 100).bonus(CombatBonus.DEFENCE_MAGIC, 80)
        .bonus(CombatBonus.DEFENCE_RANGED, 60).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(3310).blockAnimation(3311);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(21));
    style.animation(3312).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (npc.getHitpoints() <= npc.getMaxHitpoints() / 2 && npc.getHitpoints() > 10 && damage > 0
        && PRandom.randomE(4) == 0) {
      opponent.setAttacking(false);
      burrow();
    }
    return damage;
  }

  @Override
  public Item dropTableDropGetItemHook(Player player, Tile tile, int dropRateDivider, int roll,
      NpcCombatDropTable table, NpcCombatDropTableDrop drop, Item item) {
    if ((item.getId() == ItemId.MOLE_CLAW || item.getId() == ItemId.MOLE_SKIN)
        && player.getWidgetManager().isDiaryComplete(AchievementDiary.Name.FALADOR,
            AchievementDiaryTask.Difficulty.HARD)) {
      item = new Item(item.getNotedId(), item);
    }
    return item;
  }

  public void burrow() {
    var tile = new Tile(npc.getX() + 1, npc.getY() + 1, npc.getHeight());
    npc.getController().sendMapGraphic(tile, 572, 0, 0);
    var graphic = new Graphic(571);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(0, -1), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(0, 1), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(-1, -1), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(-1, 1), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(1, -1), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(1, 1), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(-1, 0), graphic);
    npc.getController().sendMapGraphic(new Tile(tile).moveTile(1, 0), graphic);
    var teleportTile = new Tile(BURROWS[PRandom.randomE(BURROWS.length)]);
    teleportTile.setHeight(npc.getHeight());
    npc.getMovement().animatedTeleport(teleportTile, 3314, null, 2);
  }
}
