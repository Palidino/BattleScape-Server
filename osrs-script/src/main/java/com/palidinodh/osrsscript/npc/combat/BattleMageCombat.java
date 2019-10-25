package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import lombok.var;

public class BattleMageCombat extends NpcCombat {
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.HARD, NpcCombatDropTable.CHANCE_1_IN_128)
        .clue(NpcCombatDrop.ClueScroll.MEDIUM, NpcCombatDropTable.CHANCE_1_IN_64);
    var dropTable =
        NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_128).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MASTER_WAND)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.INFINITY_TOP)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.INFINITY_HAT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.INFINITY_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.INFINITY_GLOVES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.INFINITY_BOTTOMS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGES_BOOK)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_CHAINBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STAFF_OF_FIRE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_AXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 12)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JUG_OF_WINE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BONES)));
    drop.table(dropTable.build());


    var zamorakCombat = NpcCombatDefinition.builder();
    zamorakCombat.id(NpcId.BATTLE_MAGE_54);
    zamorakCombat.hitpoints(NpcCombatHitpoints.total(100));
    zamorakCombat.stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16)
        .bonus(CombatBonus.DEFENCE_MAGIC, 16).build());
    zamorakCombat.aggression(NpcCombatAggression.builder().range(3).build());
    zamorakCombat.deathAnimation(836).blockAnimation(415);
    zamorakCombat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(811).attackSpeed(5).attackRange(7);
    style.targetGraphic(new Graphic(78));
    style.projectile(NpcCombatProjectile.id(335));
    zamorakCombat.style(style.build());


    var saradominCombat = NpcCombatDefinition.builder();
    saradominCombat.id(NpcId.BATTLE_MAGE_54_1611);
    saradominCombat.hitpoints(NpcCombatHitpoints.total(100));
    saradominCombat.stats(NpcCombatStats.builder().magicLevel(60)
        .bonus(CombatBonus.ATTACK_MAGIC, 16).bonus(CombatBonus.DEFENCE_MAGIC, 16).build());
    saradominCombat.aggression(NpcCombatAggression.builder().range(3).build());
    saradominCombat.deathAnimation(836).blockAnimation(415);
    saradominCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(811).attackSpeed(5).attackRange(7);
    style.targetGraphic(new Graphic(76, 100));
    style.projectile(NpcCombatProjectile.id(335));
    saradominCombat.style(style.build());


    var guthixCombat = NpcCombatDefinition.builder();
    guthixCombat.id(NpcId.BATTLE_MAGE_54_1612);
    guthixCombat.hitpoints(NpcCombatHitpoints.total(100));
    guthixCombat.stats(NpcCombatStats.builder().magicLevel(60).bonus(CombatBonus.ATTACK_MAGIC, 16)
        .bonus(CombatBonus.DEFENCE_MAGIC, 16).build());
    guthixCombat.aggression(NpcCombatAggression.builder().range(3).build());
    guthixCombat.deathAnimation(196).blockAnimation(193);
    guthixCombat.drop(drop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(20).splashOnMiss(true).build());
    style.animation(198).attackSpeed(5).attackRange(7);
    style.targetGraphic(new Graphic(77, 100));
    style.projectile(NpcCombatProjectile.id(335));
    guthixCombat.style(style.build());


    return Arrays.asList(zamorakCombat.build(), saradominCombat.build(), guthixCombat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public boolean canBeAggressiveHook(Entity opponent) {
    if (!(opponent instanceof Player)) {
      return true;
    }
    var player = (Player) opponent;
    if (npc.getId() == NpcId.BATTLE_MAGE_54
        && (player.getEquipment().getWeaponId() == ItemId.ZAMORAK_STAFF
            || player.getEquipment().getCapeId() == ItemId.ZAMORAK_CAPE
            || player.getEquipment().getCapeId() == ItemId.ZAMORAK_MAX_CAPE
            || player.getEquipment().getCapeId() == ItemId.IMBUED_ZAMORAK_CAPE
            || player.getEquipment().getCapeId() == ItemId.IMBUED_ZAMORAK_MAX_CAPE)) {
      return false;
    }
    if (npc.getId() == NpcId.BATTLE_MAGE_54_1611
        && (player.getEquipment().getWeaponId() == ItemId.SARADOMIN_STAFF
            || player.getEquipment().getCapeId() == ItemId.SARADOMIN_CAPE
            || player.getEquipment().getCapeId() == ItemId.SARADOMIN_MAX_CAPE
            || player.getEquipment().getCapeId() == ItemId.IMBUED_SARADOMIN_CAPE
            || player.getEquipment().getCapeId() == ItemId.IMBUED_SARADOMIN_MAX_CAPE)) {
      return false;
    }
    if (npc.getId() == NpcId.BATTLE_MAGE_54_1612
        && (player.getEquipment().getWeaponId() == ItemId.GUTHIX_STAFF
            || player.getEquipment().getCapeId() == ItemId.GUTHIX_CAPE
            || player.getEquipment().getCapeId() == ItemId.GUTHIX_MAX_CAPE
            || player.getEquipment().getCapeId() == ItemId.IMBUED_GUTHIX_CAPE
            || player.getEquipment().getCapeId() == ItemId.IMBUED_GUTHIX_MAX_CAPE)) {
      return false;
    }
    return true;
  }
}
