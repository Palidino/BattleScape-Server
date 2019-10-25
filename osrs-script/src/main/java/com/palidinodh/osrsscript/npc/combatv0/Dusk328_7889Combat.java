package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import lombok.var;

public class Dusk328_7889Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rolls(2);
    var dropTable = NpcCombatDropTable.builder().chance(0.02).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_STONE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.033)
        .order(NpcCombatDropTable.Order.RANDOM_UNIQUE).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NOON)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.15).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_TOURMALINE_CORE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.2).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_HAMMER)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.6).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_GLOVES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_RING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_MAUL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 5, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_ARROWTIPS, 50, 150)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 185, 245)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE_NOTED, 40, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 3, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_BAR_NOTED, 42, 49)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BAR_NOTED, 36, 45)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 25, 58)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR_NOTED, 3, 9)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DART_TIP, 15, 25)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_BOLT_TIPS, 105, 147)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_BOLT_TIPS, 19, 28)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_COMBAT_POTION_2_NOTED),
        new RandomItem(ItemId.RANGING_POTION_2_NOTED),
        new RandomItem(ItemId.MAGIC_POTION_2_NOTED)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4_NOTED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_BREW_4_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_KEY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 100, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 71, 99)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 10003, 25000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUSHROOM_POTATO_NOTED, 4, 6)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_CANNONBALL, 50, 100)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DUSK_328_7889);
    combat.spawn(NpcCombatSpawn.builder().lock(16).direction(4).animation(7778).build());
    combat.hitpoints(NpcCombatHitpoints.total(450));
    combat.stats(NpcCombatStats.builder().attackLevel(200).magicLevel(140).rangedLevel(140)
        .defenceLevel(100).build());
    combat.slayer(NpcCombatSlayer.builder().level(75).build());
    combat.aggression(NpcCombatAggression.builder().range(20).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.killCount(
        NpcCombatKillCount.builder().asName("Grotesque Guardians").sendMessage(true).build());
    combat.combatScript("GrotesqueGuardianCS").deathAnimation(7809);
    combat.drop(drop.build());


    return Arrays.asList(combat.build());
  }
}
