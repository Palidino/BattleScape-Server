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
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class FlockleaderGeerin149Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(0.02).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_HELMET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHESTPLATE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARMADYL_CHAINSKIRT)));
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
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MANTA_RAY, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUSHROOM_POTATO, 3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SMOKE_RUNE, 13, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 95, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRUSHED_NEST, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.FLOCKLEADER_GEERIN_149);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.KREEARRA_580)
        .respawnWithId(NpcId.WINGMAN_SKREE_143).respawnWithId(NpcId.FLIGHT_KILISA_159).build());
    combat.hitpoints(NpcCombatHitpoints.total(132));
    combat.stats(NpcCombatStats.builder().attackLevel(80).magicLevel(50).rangedLevel(150)
        .defenceLevel(175).bonus(CombatBonus.ATTACK_RANGED, 60).build());
    combat.aggression(NpcCombatAggression.builder().range(16).build());
    combat.killCount(NpcCombatKillCount.builder().asName("Kree'arra's bodyguard").build());
    combat.combatScript("AviansieMinionCS").deathAnimation(6959).blockAnimation(6958);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(25));
    style.animation(6956).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
