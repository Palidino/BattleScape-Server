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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Skotizo321Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(0.04).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_DARKNESS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.15).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_ONYX)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(1.5).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SKOTOS)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(4.0).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_CLAW)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_BASE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM_MIDDLE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DARK_TOTEM)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(22.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 450)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 450)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATEBODY_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KITESHIELD_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATESKIRT_NOTED, 3)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED_NOTED, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_SNAPDRAGON_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 20)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DRAGONSTONE_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_ORE_NOTED, 75)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_ANGLERFISH_NOTED, 60)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAHOGANY_PLANK_NOTED, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHIELD_LEFT_HALF)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANCIENT_SHARD, 1, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.SKOTIZO_321);
    combat.hitpoints(NpcCombatHitpoints.total(450));
    combat.stats(NpcCombatStats.builder().attackLevel(240).magicLevel(280).defenceLevel(200)
        .bonus(CombatBonus.MELEE_ATTACK, 160).bonus(CombatBonus.MELEE_DEFENCE, 80)
        .bonus(CombatBonus.DEFENCE_MAGIC, 130).bonus(CombatBonus.DEFENCE_RANGED, 130).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.combatScript("SkotizoCS").type(NpcCombatType.DEMON).deathAnimation(4677)
        .blockAnimation(4676);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(38));
    style.animation(4680).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(38));
    style.animation(69).attackSpeed(6);
    style.targetGraphic(new Graphic(1243));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
