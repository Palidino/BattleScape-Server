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

public class DerangedArchaeologist276Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_SMALL_FOSSIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_MEDIUM_FOSSIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_LARGE_FOSSIL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNIDENTIFIED_RARE_FOSSIL)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_MACE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KNIFE, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_ARROW, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTATO_WITH_CHEESE, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_KEY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_LIMBS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WHITE_BERRIES_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CANNONBALL, 80)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DRAGONHIDE_NOTED, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED_NOTED, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE_NOTED, 10)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_DHIDE_BODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUD_RUNE, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PLAIN_PIZZA)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCHOVY_PIZZA, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NUMULITE, 5, 115)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DERANGED_ARCHAEOLOGIST_276);
    combat.phrase("Round and round and round and round!").phrase("The plants! They're alive!")
        .phrase("They came from the ground! They came from the ground!!!")
        .phrase("The doors won't stay closed forever!")
        .phrase("They're cheering! Why are they cheering?")
        .phrase("PTime is running out! She will rise again!");
    combat.hitpoints(NpcCombatHitpoints.total(200));
    combat.stats(NpcCombatStats.builder().attackLevel(280).rangedLevel(320).defenceLevel(280)
        .bonus(CombatBonus.MELEE_ATTACK, 280).bonus(CombatBonus.ATTACK_RANGED, 90)
        .bonus(CombatBonus.DEFENCE_STAB, 20).bonus(CombatBonus.DEFENCE_SLASH, 20)
        .bonus(CombatBonus.DEFENCE_CRUSH, 50).bonus(CombatBonus.DEFENCE_MAGIC, 300)
        .bonus(CombatBonus.DEFENCE_RANGED, 300).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(836).blockAnimation(415);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(28));
    style.animation(423).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.RANGED).weight(8).build());
    style.damage(NpcCombatDamage.maximum(28));
    style.animation(2614).attackSpeed(4);
    style.targetGraphic(new Graphic(305));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(
        NpcCombatStyleType.builder().hitType(HitType.RANGED).subHitType(HitType.TYPELESS).build());
    style.damage(NpcCombatDamage.maximum(25));
    style.animation(2614).attackSpeed(4);
    style.targetGraphic(new Graphic(157));
    style.projectile(NpcCombatProjectile.builder().id(1260).speedType(HitType.MAGIC)
        .speedMinimumDistance(8).build());
    style.phrase("Learn to Read!");
    var targetTile = NpcCombatTargetTile.builder().radius(1);
    targetTile.breakOff(NpcCombatTargetTile.BreakOff.builder().count(2).distance(2).build());
    style.specialAttack(targetTile.build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
