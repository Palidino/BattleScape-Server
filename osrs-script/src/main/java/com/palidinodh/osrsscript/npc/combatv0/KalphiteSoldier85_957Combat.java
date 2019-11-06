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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import lombok.var;

public class KalphiteSoldier85_957Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256);
    var dropTable = NpcCombatDropTable.builder().chance(1.1);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_KALPHITE_HEAD_13490)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_RUNE, 1, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TARROMIN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_MARRENTILL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_GUAM_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_AVANTOE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_IRIT_LEAF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HARDLEATHER_BODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_SQ_SHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_RUNE, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BODY_RUNE, 5, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 3, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 1, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTATO_CACTUS_NOTED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LOBSTER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERSKIN_4)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_HARRALANDER)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.KALPHITE_SOLDIER_85_957);
    combat.hitpoints(NpcCombatHitpoints.total(90));
    combat.stats(NpcCombatStats.builder().attackLevel(70).defenceLevel(70)
        .bonus(CombatBonus.DEFENCE_STAB, 25).bonus(CombatBonus.DEFENCE_SLASH, 25)
        .bonus(CombatBonus.DEFENCE_CRUSH, 5).bonus(CombatBonus.DEFENCE_MAGIC, 50)
        .bonus(CombatBonus.DEFENCE_RANGED, 50).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.deathAnimation(6228).blockAnimation(6227);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.damage(NpcCombatDamage.maximum(8));
    style.animation(6223).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(4).build());
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
