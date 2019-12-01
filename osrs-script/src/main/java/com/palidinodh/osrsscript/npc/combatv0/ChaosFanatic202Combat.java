package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitType;
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
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import lombok.var;

class ChaosFanatic202Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(0.1).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_CHAOS_ELEMENTAL)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_256).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MALEDICTION_WARD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ODIUM_WARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_MONK_BOTTOM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_MONK_BOTTOM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RING_OF_LIFE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPLITBARK_BODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPLITBARK_LEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_RUNE, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 175)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOKE_RUNE, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCHOVY_PIZZA_NOTED, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MONKFISH, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 600, 4000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_SAPPHIRE_NOTED, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_EMERALD_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WINE_OF_ZAMORAK_NOTED, 10)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CHAOS_FANATIC_202);
    combat.phrase("Burn!").phrase("WEUGH!").phrase("Develish Oxen Roll!")
        .phrase("All your wilderness are belong to them!").phrase("AhehHeheuhHhahueHuUEehEahAH")
        .phrase("I shall call him squidgy and he shall be my squidgy!");
    combat.hitpoints(NpcCombatHitpoints.total(225));
    combat.stats(NpcCombatStats.builder().magicLevel(200).defenceLevel(220)
        .bonus(CombatBonus.ATTACK_RANGED, 75).bonus(CombatBonus.DEFENCE_STAB, 260)
        .bonus(CombatBonus.DEFENCE_SLASH, 260).bonus(CombatBonus.DEFENCE_CRUSH, 250)
        .bonus(CombatBonus.DEFENCE_MAGIC, 280).bonus(CombatBonus.DEFENCE_RANGED, 80).build());
    combat.aggression(NpcCombatAggression.builder().range(8).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.combatScript("wildernessdemiboss").deathAnimation(836).blockAnimation(415);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.MAGIC).weight(8).build());
    style.damage(NpcCombatDamage.builder().maximum(31).splashOnMiss(true).build());
    style.animation(811).attackSpeed(2);
    style.targetGraphic(new Graphic(305));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.MAGIC).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.maximum(31));
    style.animation(811).attackSpeed(2);
    style.targetGraphic(new Graphic(157));
    style.projectile(NpcCombatProjectile.builder().id(551).speedMinimumDistance(8).build());
    var targetTile = NpcCombatTargetTile.builder();
    targetTile.breakOff(NpcCombatTargetTile.BreakOff.builder().count(2).distance(2).build());
    style.specialAttack(targetTile.build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
