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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class DagannothRex303Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(6.2);
    var dropTable = NpcCombatDropTable.builder().chance(0.02).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_DAGANNOTH_REX)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.14);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_ELITE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(2.4);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLUE_SCROLL_HARD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(3.5).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BERSERKER_RING)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WARRIOR_RING)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(4.0);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_DAGANNOTH_HEAD_13493)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_PLATE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ROCK_SHELL_LEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RING_OF_LIFE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FREMENNIK_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RING_OF_LIFE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.IRON_ORE_NOTED, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_BAR_NOTED, 15, 38)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BODY_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FREMENNIK_SHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIFIRE_POTION_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_ATTACK_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_DEFENCE_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_BREW_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RESTORE_POTION_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_ORE_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BASS, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SWORDFISH, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 126, 3000)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_RANARR_WEED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FREMENNIK_BLADE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_WARHAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_KITESHIELD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_PLATEBODY)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DAGANNOTH_BONES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DAGANNOTH_HIDE)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.DAGANNOTH_REX_303);
    combat.spawn(NpcCombatSpawn.builder().respawnDelay(75).build());
    combat.hitpoints(NpcCombatHitpoints.total(255));
    combat.stats(NpcCombatStats.builder().attackLevel(255).rangedLevel(255).defenceLevel(255)
        .bonus(CombatBonus.MELEE_DEFENCE, 255).bonus(CombatBonus.DEFENCE_MAGIC, 10)
        .bonus(CombatBonus.DEFENCE_RANGED, 255).build());
    combat.aggression(NpcCombatAggression.builder().range(4).build());
    combat.immunity(NpcCombatImmunity.builder().venom(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(2856).blockAnimation(2852);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(26));
    style.animation(2853).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
