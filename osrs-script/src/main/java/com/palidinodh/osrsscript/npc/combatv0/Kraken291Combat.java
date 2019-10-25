package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Kraken291Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true)
        .rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.034).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_KRAKEN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.1).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_DIRT)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.2);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.29).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TRIDENT_OF_THE_SEAS_FULL)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.5).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KRAKEN_TENTACLE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(2.3);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_RING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PIRATE_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_WATER_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_TOP)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_BOTTOM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNPOWERED_ORB_NOTED, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_NOTED, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.OAK_PLANK_NOTED, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_SHARK_NOTED, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_MONKFISH_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_SNAPDRAGON_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HARPOON)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCKET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SANFEW_SERUM_4, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_KEY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUSTY_SWORD)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIDOTE_PLUS_PLUS_4_NOTED, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_RUNE, 400)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIST_RUNE, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 24)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SEAWEED_NOTED, 125)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 10000, 19999)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EDIBLE_SEAWEED, 5)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.KRAKEN_291);
    combat.noclip(true);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(14).build());
    combat.hitpoints(NpcCombatHitpoints.total(255));
    combat.stats(NpcCombatStats.builder().bonus(CombatBonus.DEFENCE_MAGIC, 130)
        .bonus(CombatBonus.DEFENCE_RANGED, 300).build());
    combat.slayer(NpcCombatSlayer.builder().level(87).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().bypassMapObjects(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.combatScript("KrakenCS").deathAnimation(3993).blockAnimation(3990);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(28).splashOnMiss(true).build());
    style.animation(3991).attackSpeed(4);
    style.targetGraphic(new Graphic(163, 124));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
