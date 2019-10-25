package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatFocus;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.npc.combat.style.special.NpcCombatTargetTile;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.random.PRandom;
import lombok.var;

public class CorporealBeastCombat extends NpcCombat {
  private Npc npc;
  private Npc darkCore;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_200);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_DARK_CORE)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_585)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ELYSIAN_SIGIL).weight(1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPECTRAL_SIGIL).weight(3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ARCANE_SIGIL).weight(3)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(0.59).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.HOLY_ELIXIR)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(1.57).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SHIELD)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(3.91);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLTS_E, 175)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CANNONBALL, 2000)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_SAPPHIRE_NOTED, 1, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_EMERALD_NOTED, 1, 8)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_ARROW, 750)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BOLTS, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_AIR_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_WATER_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_EARTH_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_FIRE_STAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_TOP)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_ROBE_BOTTOM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 2500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 500)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_ORE_NOTED, 125)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 35)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_LOGS_NOTED, 75)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GREEN_DRAGONHIDE_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RAW_SHARK_NOTED, 70)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DESERT_GOAT_HORN_NOTED, 120)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 24)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TUNA_POTATO_NOTED, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_RUBY_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 20000, 50000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_TALISMAN_NOTED, 1, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 20)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TEAK_PLANK_NOTED, 100)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WHITE_BERRIES_NOTED, 120)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ANTIDOTE_PLUS_PLUS_4_NOTED, 40)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHIELD_LEFT_HALF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_SPEAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());


    var combat = NpcCombatDefinition.builder();
    combat.id(NpcId.CORPOREAL_BEAST_785);
    combat.hitpoints(
        NpcCombatHitpoints.builder().total(2000).bar(HitpointsBar.GREEN_RED_160).build());
    combat.stats(NpcCombatStats.builder().attackLevel(320).magicLevel(350).rangedLevel(150)
        .defenceLevel(310).bonus(CombatBonus.MELEE_ATTACK, 50).bonus(CombatBonus.DEFENCE_STAB, 25)
        .bonus(CombatBonus.DEFENCE_SLASH, 200).bonus(CombatBonus.DEFENCE_CRUSH, 100)
        .bonus(CombatBonus.DEFENCE_MAGIC, 150).bonus(CombatBonus.DEFENCE_RANGED, 230).build());
    combat.aggression(NpcCombatAggression.builder().range(16).build());
    combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat.focus(NpcCombatFocus.builder().singleTargetFocus(true).build());
    combat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat.deathAnimation(1676).blockAnimation(1677);
    combat.drop(drop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(51));
    style.animation(1682).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(51));
    style.animation(1683).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(65).prayerEffectiveness(0.66).build());
    style.animation(1679).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(51).prayerEffectiveness(0.66).build());
    style.animation(1679).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(
        NpcCombatEffect.builder().statDrain(Skills.PRAYER, 1).statDrain(Skills.MAGIC, 1).build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(27).ignorePrayer(true).build());
    style.animation(1679).attackSpeed(6);
    style.targetGraphic(new Graphic(317));
    style.projectile(NpcCombatProjectile.builder().id(315).speedMinimumDistance(8).build());
    var targetTile = NpcCombatTargetTile.builder();
    targetTile.breakOff(NpcCombatTargetTile.BreakOff.builder().count(4).distance(3)
        .maximumDamage(14).afterTargettedTile(true).build());
    style.specialAttack(targetTile.build());
    combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.builder().hitType(HitType.UNDERNEATH).build());
    style.damage(NpcCombatDamage.maximum(50));
    style.animation(1686).attackSpeed(8);
    style.projectile(NpcCombatProjectile.id(335));
    combat.style(style.build());


    return Arrays.asList(combat.build());
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    npc.getWorld().removeNpc(darkCore);
    darkCore = null;
    for (var player : npc.getController().getPlayers()) {
      if (player.getHeight() == npc.getHeight()) {
        player.getCombat().setDamageInflicted(0);
      }
    }
  }

  @Override
  public void tickStartHook() {
    if (!npc.isVisible() || npc.isDead()) {
      return;
    }
    if (npc.getHitpoints() < npc.getMaxHitpoints() && npc.getController().getPlayers().isEmpty()) {
      npc.setHitpoints(npc.getMaxHitpoints());
    }
    if (darkCore != null && darkCore.isDead()) {
      npc.getWorld().removeNpc(darkCore);
      darkCore = null;
    }
    if (!npc.isLocked() && npc.getX() > 2998) { // Why does this happen?
      npc.getMovement().teleport(npc.getSpawnTile());
    }
  }

  @Override
  public void applyDeadHook() {
    if (darkCore != null) {
      npc.getWorld().removeNpc(darkCore);
      darkCore = null;
    }
  }

  @Override
  public void npcApplyHitStartHook(Hit hit) {
    if (hit.getDamage() > 100) {
      hit.setDamage(100);
    }
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (opponent instanceof Player) {
      var player = (Player) opponent;
      if (!player.getEquipment().getWeaponName().contains("spear")
          && !player.getEquipment().getWeaponName().contains("halberd")
          || player.getMagic().getActiveSpell() != null) {
        damage *= 0.5;
      }
    }
    if (damage > 32) {
      if (npc.getCombat().getPlayerAggressionDelay() == 0) {
        npc.setFaceEntity(opponent);
        npc.setEngagingEntity(opponent);
        npc.getCombat().setFollowing(opponent, 0);
        npc.getCombat().setPlayerAggressionDelay(2);
      }
      if (darkCore == null && PRandom.randomE(8) == 0) {
        if (Route.canMove(npc, npc.getX() - 1, npc.getY())) {
          darkCore = new Npc(npc.getController(), NpcId.DARK_ENERGY_CORE_75, npc.getX() - 1,
              npc.getY(), npc.getHeight());
        } else {
          darkCore = new Npc(npc.getController(), NpcId.DARK_ENERGY_CORE_75,
              npc.getX() + npc.getSizeX(), npc.getY(), npc.getHeight());
        }
        darkCore.setRespawns(npc.getRespawns());
      }
    }
    return damage;
  }
}
