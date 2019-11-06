package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class LongTailedWyvern152Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.034).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WYVERN_VISAGE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.05).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_BOOTS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.29).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_LONGSWORD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS, 12, 29)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_SMALL_FOSSIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_MEDIUM_FOSSIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_LARGE_FOSSIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_RARE_FOSSIL)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 3, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_CROSSBOW_U)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_ARROW, 38, 42)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SEAWEED_SPORE, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CALCITE, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PYROPHOSPHITE, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TEAK_LOGS_NOTED, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPE_GRASS_NOTED, 10, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BAR_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_RUNE, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_RUNE, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LOBSTER, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 3000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NUMULITE, 1, 21)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WYVERN_BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.LONG_TAILED_WYVERN_152);
    combat.hitpoints(NpcCombatHitpoints.total(200));
    combat.stats(NpcCombatStats.builder().attackLevel(125).magicLevel(90).rangedLevel(90)
        .defenceLevel(120).bonus(CombatBonus.MELEE_DEFENCE, 70)
        .bonus(CombatBonus.DEFENCE_MAGIC, 140).bonus(CombatBonus.DEFENCE_RANGED, 120).build());
    combat.slayer(NpcCombatSlayer.builder().level(66).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.killCount(NpcCombatKillCount.builder().asName("Fossil Island wyvern").build());
    combat.combatScript("WyvernCS").deathAnimation(7652).blockAnimation(7659);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(13));
    style.animation(7654).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.DRAGONFIRE);
    style.damage(NpcCombatDamage.maximum(50));
    style.animation(7657).attackSpeed(6);
    style.castGraphic(new Graphic(501, 124)).targetGraphic(new Graphic(502));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
