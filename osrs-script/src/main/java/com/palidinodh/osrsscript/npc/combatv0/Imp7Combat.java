package com.palidinodh.osrsscript.npc.combatv0;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import lombok.var;

public class Imp7Combat extends NpcCombat {
  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder();
    var dropTable = NpcCombatDropTable.builder().chance(4.0);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ENSOULED_IMP_HEAD_13454)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BRONZE_BOLTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLUE_WIZARD_HAT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHEFS_HAT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BREAD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CABBAGE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COOKED_MEAT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_BEAD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RED_BEAD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WHITE_BEAD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YELLOW_BEAD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BALL_OF_WOOL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EGG)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TINDERBOX)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCKET)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HAMMER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CADAVA_BERRIES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BREAD_DOUGH)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_CHICKEN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BURNT_MEAT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BURNT_BREAD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JUG)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POT_OF_FLOUR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CLAY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.POTION)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FLIER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JUG_OF_WATER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BUCKET_OF_WATER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHEARS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRAIN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.IMP_7);
    combat.hitpoints(NpcCombatHitpoints.total(10));
    combat.stats(NpcCombatStats.builder().attackLevel(5).defenceLevel(6)
        .bonus(CombatBonus.DEFENCE_MAGIC, -42).bonus(CombatBonus.DEFENCE_RANGED, -42).build());
    combat.aggression(NpcCombatAggression.PLAYERS);
    combat.combatScript("ZamorakGWDCS").type(NpcCombatType.DEMON).deathAnimation(172);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.maximum(1));
    style.animation(173).attackSpeed(4);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }
}
