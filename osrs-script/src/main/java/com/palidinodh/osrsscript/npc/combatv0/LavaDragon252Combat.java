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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class LavaDragon252Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.015).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRACONIC_VISAGE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.4);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KNIFE, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_JAVELIN_HEADS, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAVA_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_ORB_NOTED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHOCOLATE_CAKE, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DART, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_JAVELIN, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAVA_RUNE, 15, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 66, 3000)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAVA_DRAGON_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DRAGONHIDE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAVA_SCALE, 1, 10)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.LAVA_DRAGON_252);
    combat.hitpoints(NpcCombatHitpoints.total(230));
    combat.stats(NpcCombatStats.builder().attackLevel(240).defenceLevel(220)
        .bonus(CombatBonus.DEFENCE_STAB, 70).bonus(CombatBonus.DEFENCE_SLASH, 90)
        .bonus(CombatBonus.DEFENCE_CRUSH, 90).bonus(CombatBonus.DEFENCE_MAGIC, 80)
        .bonus(CombatBonus.DEFENCE_RANGED, 70).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.killCount(NpcCombatKillCount.builder().asName("Chromatic dragon").build());
    combat.deathAnimation(92).blockAnimation(89);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(23));
    style.animation(80).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.DRAGONFIRE);
    style.damage(NpcCombatDamage.maximum(60));
    style.animation(81).attackSpeed(4).attackRange(1);
    style.castGraphic(new Graphic(1, 100));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
