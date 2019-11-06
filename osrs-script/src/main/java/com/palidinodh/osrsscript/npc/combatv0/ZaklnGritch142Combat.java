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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class ZaklnGritch142Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(0.04).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAKIAN_SPEAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STAFF_OF_THE_DEAD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.29).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 96, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 5, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 5, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TUNA_POTATO, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_3)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WINE_OF_ZAMORAK_NOTED, 5, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 2)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.ZAKLN_GRITCH_142);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.KRIL_TSUTSAROTH_650)
        .respawnWithId(NpcId.TSTANON_KARLAK_145).respawnWithId(NpcId.BALFRUG_KREEYATH_151).build());
    combat.hitpoints(NpcCombatHitpoints.total(150));
    combat.stats(
        NpcCombatStats.builder().attackLevel(83).magicLevel(50).rangedLevel(150).defenceLevel(127)
            .bonus(CombatBonus.ATTACK_RANGED, 20).bonus(CombatBonus.DEFENCE_MAGIC, -5).build());
    combat.aggression(NpcCombatAggression.builder().range(16).build());
    combat.killCount(NpcCombatKillCount.builder().asName("K'ril Tsutsaroth's bodyguard").build());
    combat.type(NpcCombatType.DEMON).deathAnimation(67).blockAnimation(65);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(21));
    style.animation(7077).attackSpeed(5);
    style.castGraphic(new Graphic(1222));
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
