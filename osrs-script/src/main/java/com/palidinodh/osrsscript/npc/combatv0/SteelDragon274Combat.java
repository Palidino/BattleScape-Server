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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class SteelDragon274Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.015).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRACONIC_VISAGE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.2);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable =
        NpcCombatDropTable.builder().chance(0.3).order(NpcCombatDropTable.Order.RANDOM_UNIQUE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_BASE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_MIDDLE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_TOP)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.5);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_SHARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.58).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_PLATESKIRT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_PLATELEGS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_64);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS, 2, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DART_P, 9, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_JAVELIN_HEADS, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KNIFE, 5, 7)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CURRY, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_DEFENCE_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_1)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_JAVELIN, 2, 7)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MACE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_ATTACK_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_LIMBS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_BAR, 5)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.STEEL_DRAGON_274);
    combat.hitpoints(NpcCombatHitpoints.total(250));
    combat.stats(NpcCombatStats.builder().attackLevel(235).magicLevel(130).defenceLevel(235)
        .bonus(CombatBonus.DEFENCE_STAB, 50).bonus(CombatBonus.DEFENCE_SLASH, 70)
        .bonus(CombatBonus.DEFENCE_CRUSH, 70).bonus(CombatBonus.DEFENCE_MAGIC, 30)
        .bonus(CombatBonus.DEFENCE_RANGED, 90).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.killCount(NpcCombatKillCount.builder().asName("Metal dragon").build());
    combat.deathAnimation(92).blockAnimation(89);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(22));
    style.animation(91).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.DRAGONFIRE);
    style.damage(NpcCombatDamage.maximum(60));
    style.animation(81).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
