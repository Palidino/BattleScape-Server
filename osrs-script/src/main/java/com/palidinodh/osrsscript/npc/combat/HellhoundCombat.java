package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
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
import com.palidinodh.osrsscript.map.area.CatacombsOfKourendArea;
import lombok.var;

public class HellhoundCombat extends NpcCombat {
  private static final NpcCombatDropTable SUPERIOR_DROP_TABLE = NpcCombatDropTable.builder()
      .chance(2.32).log(true)
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IMBUED_HEART, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_GEM, 1, 1, 1)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_BATTLESTAFF, 1, 1, 3)))
      .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_BATTLESTAFF, 1, 1, 3))).build();
  private static final NpcCombatDropTable TOTEM_DROP_TABLE =
      NpcCombatDropTable.builder().chance(0.27).order(NpcCombatDropTable.Order.RANDOM_UNIQUE)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_BASE)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_MIDDLE)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_TOP))).build();
  private static final NpcCombatDropTable SHARD_DROP_TABLE =
      NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_SHARD))).build();
  private static final NpcCombatDropTable CURSED_DROP_TABLE =
      NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_512).log(true)
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRIMORDIAL_CRYSTAL)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PEGASIAN_CRYSTAL)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ETERNAL_CRYSTAL)))
          .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOULDERING_STONE))).build();

  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_64);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_32768);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOULDERING_STONE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.HELLHOUND_136).id(NpcId.HELLHOUND_122).id(NpcId.HELLHOUND_122_7256);
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(NpcCombatStats.builder().attackLevel(105).defenceLevel(102).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(6576).blockAnimation(6578);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(6579).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    var cursedCombat = NpcCombatDefinition.builder();
    cursedCombat.id(NpcId.CURSED_HELLHOUND_260_16000);
    cursedCombat
        .hitpoints(NpcCombatHitpoints.builder().total(300).bar(HitpointsBar.GREEN_RED_60).build());
    cursedCombat.stats(NpcCombatStats.builder().attackLevel(110).magicLevel(110).rangedLevel(110)
        .defenceLevel(50).bonus(CombatBonus.ATTACK_SLASH, 25).bonus(CombatBonus.ATTACK_MAGIC, 25)
        .bonus(CombatBonus.ATTACK_RANGED, 25).bonus(CombatBonus.DEFENCE_STAB, 25)
        .bonus(CombatBonus.DEFENCE_SLASH, 50).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    cursedCombat.slayer(NpcCombatSlayer.builder().level(91).build());
    cursedCombat.deathAnimation(6564).blockAnimation(6563);
    cursedCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(6562).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(6562).attackSpeed(6);
    style.targetGraphic(new Graphic(1244, 100));
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(12));
    style.animation(6562).attackSpeed(6);
    style.targetGraphic(new Graphic(1243, 100));
    style.projectile(NpcCombatProjectile.id(335));
    cursedCombat.style(style.build());


    return Arrays.asList(combat.build(), cursedCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    if (npc.getArea().matches(CatacombsOfKourendArea.class)) {
      if (TOTEM_DROP_TABLE.canDrop(npc, player)) {
        TOTEM_DROP_TABLE.dropItems(npc, player, dropTile);
      }
      if (SHARD_DROP_TABLE.canDrop(npc, player)) {
        SHARD_DROP_TABLE.dropItems(npc, player, dropTile);
      }
    }
  }

  @Override
  public NpcCombatDropTable deathDropItemsTableHook(Npc npc, Player player, int dropRateDivider,
      int roll, NpcCombatDropTable table) {
    if (npc.getId() == NpcId.CURSED_HELLHOUND_260_16000) {
      if (!player.getSkills().isWildernessSlayerTask(npc)) {
        player.getGameEncoder().sendMessage("Without an assigned task, the loot turns to dust...");
        return null;
      }
      if (CURSED_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
        return CURSED_DROP_TABLE;
      }
    }
    if (npc.getId() == NpcId.CURSED_HELLHOUND_260_16000
        && SUPERIOR_DROP_TABLE.canDrop(npc, player, dropRateDivider, roll)) {
      return SUPERIOR_DROP_TABLE;
    }
    return table;
  }

  @Override
  public double dropTableChanceHook(Player player, int dropRateDivider, int roll,
      NpcCombatDropTable table) {
    var chance = table.getChance();
    if (npc.getId() == NpcId.CURSED_HELLHOUND_260_16000 && table == SUPERIOR_DROP_TABLE) {
      chance /= 32;
    }
    return chance;
  }
}
