package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Scorpia225Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.05).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SCORPIAS_OFFSPRING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MALEDICTION_WARD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ODIUM_WARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(5.6);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_SCORPION_HEAD_13460)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PHOENIX_NECKLACE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SPEAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DUST_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.KWUARM_NOTED, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADMIRAL_PIE, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCHOVY_PIZZA_NOTED, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCKET_OF_SAND_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CACTUS_SPINE_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_SAPPHIRE_NOTED, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_EMERALD_NOTED, 6)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SCORPIA_225);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(15).build());
    combat.hitpoints(NpcCombatHitpoints.total(200));
    combat.stats(NpcCombatStats.builder().attackLevel(250).defenceLevel(180)
        .bonus(CombatBonus.MELEE_ATTACK, 60).bonus(CombatBonus.DEFENCE_STAB, 246)
        .bonus(CombatBonus.DEFENCE_SLASH, 284).bonus(CombatBonus.DEFENCE_CRUSH, 284)
        .bonus(CombatBonus.DEFENCE_MAGIC, 44).bonus(CombatBonus.DEFENCE_RANGED, 284).build());
    combat.aggression(NpcCombatAggression.builder().range(8).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.combatScript("wildernessdemiboss").deathAnimation(6256).blockAnimation(6255);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(16));
    style.animation(6254).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(20).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
