package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.RandomItem;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

class ZamorakGodWarsChamberCombat extends NpcCombat {
  private static final NpcCombatStyle SPECIAL_ATTACK =
      NpcCombatStyle.builder().type(NpcCombatStyleType.MELEE)
          .damage(NpcCombatDamage.builder().maximum(49).ignorePrayer(true).build())
          .effect(NpcCombatEffect.builder().poison(16).build()).phrase("YARRRRRRR!").animation(6950)
          .attackSpeed(6).build();

  @Inject
  private Npc npc;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var krilTsutsarothDrop =
        NpcCombatDrop.builder().rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
            .clue(NpcCombatDrop.ClueScroll.ELITE, NpcCombatDropTable.CHANCE_1_IN_250);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PET_KRIL_TSUTSAROTH)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_HILT)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STAFF_OF_THE_DEAD)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_254).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAKIAN_SPEAR)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEAM_BATTLESTAFF)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SCIMITAR)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DAGGER)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 80, 90)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 7, 13)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LANTADYME_SEED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_ARROW, 295, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_PLATEBODY)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_ATTACK_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_BREW_3, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 120, 124)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 19362, 20073)));
    krilTsutsarothDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    krilTsutsarothDrop.table(dropTable.build());


    var krilTsutsarothCombat = NpcCombatDefinition.builder();
    krilTsutsarothCombat.id(NpcId.KRIL_TSUTSAROTH_650);
    krilTsutsarothCombat.phrase("Attack them, you dogs!").phrase("Forward!")
        .phrase("Death to Saradomin's dogs!").phrase("Kill them, you cowards!")
        .phrase("The Dark One will have their souls!").phrase("Zamorak curse them!")
        .phrase("Rend them limb from limb!").phrase("No retreat!").phrase("Flay them all!");
    krilTsutsarothCombat.spawn(NpcCombatSpawn.builder().respawnDelay(100).build());
    krilTsutsarothCombat.hitpoints(NpcCombatHitpoints.total(255));
    krilTsutsarothCombat.stats(NpcCombatStats.builder().attackLevel(340).magicLevel(200)
        .defenceLevel(270).bonus(CombatBonus.MELEE_ATTACK, 160).bonus(CombatBonus.MELEE_DEFENCE, 80)
        .bonus(CombatBonus.DEFENCE_MAGIC, 130).bonus(CombatBonus.DEFENCE_RANGED, 80).build());
    krilTsutsarothCombat.aggression(NpcCombatAggression.builder().range(16).build());
    krilTsutsarothCombat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    krilTsutsarothCombat
        .focus(NpcCombatFocus.builder().keepWithinDistance(1).singleTargetFocus(true).build());
    krilTsutsarothCombat.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    krilTsutsarothCombat.type(NpcCombatType.DEMON).deathAnimation(6949).blockAnimation(6947);
    krilTsutsarothCombat.drop(krilTsutsarothDrop.build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(47));
    style.animation(6948).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(16).build());
    krilTsutsarothCombat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(30));
    style.animation(6950).attackSpeed(6);
    style.castGraphic(new Graphic(1224, 100));
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(16).build());
    style.multiTarget(true);
    krilTsutsarothCombat.style(style.build());


    var balfrugKreeyathDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    balfrugKreeyathDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAKIAN_SPEAR).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 40))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    balfrugKreeyathDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 98)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WINE_OF_ZAMORAK, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TUNA_POTATO, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 98)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_ATTACK_3)));
    balfrugKreeyathDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    balfrugKreeyathDrop.table(dropTable.build());


    var balfrugKreeyathCombat = NpcCombatDefinition.builder();
    balfrugKreeyathCombat.id(NpcId.BALFRUG_KREEYATH_151);
    balfrugKreeyathCombat
        .spawn(NpcCombatSpawn.builder().respawnDelay(50).respawnWithId(NpcId.KRIL_TSUTSAROTH_650)
            .respawnWithId(NpcId.TSTANON_KARLAK_145).respawnWithId(NpcId.ZAKLN_GRITCH_142).build());
    balfrugKreeyathCombat.hitpoints(NpcCombatHitpoints.total(161));
    balfrugKreeyathCombat.stats(NpcCombatStats.builder().attackLevel(115).magicLevel(150)
        .defenceLevel(153).bonus(CombatBonus.DEFENCE_MAGIC, 10).build());
    balfrugKreeyathCombat.aggression(NpcCombatAggression.builder().range(16).build());
    balfrugKreeyathCombat
        .killCount(NpcCombatKillCount.builder().asName("K'ril Tsutsaroth's bodyguard").build());
    balfrugKreeyathCombat.type(NpcCombatType.DEMON).deathAnimation(67).blockAnimation(65);
    balfrugKreeyathCombat.drop(balfrugKreeyathDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.builder().maximum(16).splashOnMiss(true).build());
    style.animation(4630).attackSpeed(5);
    style.castGraphic(new Graphic(1226)).targetGraphic(new Graphic(157, 124));
    style.projectile(NpcCombatProjectile.id(335));
    balfrugKreeyathCombat.style(style.build());


    var zaklnGritchDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    zaklnGritchDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAKIAN_SPEAR).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 40))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    zaklnGritchDrop.table(dropTable.build());
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
    zaklnGritchDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1300, 1500)));
    zaklnGritchDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    zaklnGritchDrop.table(dropTable.build());


    var zaklnGritchCombat = NpcCombatDefinition.builder();
    zaklnGritchCombat.id(NpcId.ZAKLN_GRITCH_142);
    zaklnGritchCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.KRIL_TSUTSAROTH_650).respawnWithId(NpcId.TSTANON_KARLAK_145)
        .respawnWithId(NpcId.BALFRUG_KREEYATH_151).build());
    zaklnGritchCombat.hitpoints(NpcCombatHitpoints.total(150));
    zaklnGritchCombat.stats(
        NpcCombatStats.builder().attackLevel(83).magicLevel(50).rangedLevel(150).defenceLevel(127)
            .bonus(CombatBonus.ATTACK_RANGED, 20).bonus(CombatBonus.DEFENCE_MAGIC, -5).build());
    zaklnGritchCombat.aggression(NpcCombatAggression.builder().range(16).build());
    zaklnGritchCombat
        .killCount(NpcCombatKillCount.builder().asName("K'ril Tsutsaroth's bodyguard").build());
    zaklnGritchCombat.type(NpcCombatType.DEMON).deathAnimation(67).blockAnimation(65);
    zaklnGritchCombat.drop(zaklnGritchDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.RANGED);
    style.damage(NpcCombatDamage.maximum(21));
    style.animation(7077).attackSpeed(5);
    style.castGraphic(new Graphic(1222));
    style.projectile(NpcCombatProjectile.id(335));
    zaklnGritchCombat.style(style.build());


    var tstanonKarlakDrop = NpcCombatDrop.builder().clue(NpcCombatDrop.ClueScroll.HARD,
        NpcCombatDropTable.CHANCE_1_IN_128);
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_508).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_1)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GODSWORD_SHARD_3)));
    tstanonKarlakDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_127).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAKIAN_SPEAR).weight(1)));
    dropTable
        .drop(NpcCombatDropTableDrop.builder().item(new RandomItem(ItemId.COINS, 1000, 5000, 40))
            .log(NpcCombatDropTableDrop.Log.NO).build());
    tstanonKarlakDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_DART, 98)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WINE_OF_ZAMORAK_NOTED, 7)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.STEEL_ARROW, 98)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SHARK, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TUNA_POTATO, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_STRENGTH_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_ATTACK_3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ZAMORAK_BREW_3)));
    tstanonKarlakDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 1100, 1400)));
    tstanonKarlakDrop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    tstanonKarlakDrop.table(dropTable.build());


    var tstanonKarlakCombat = NpcCombatDefinition.builder();
    tstanonKarlakCombat.id(NpcId.TSTANON_KARLAK_145);
    tstanonKarlakCombat.spawn(NpcCombatSpawn.builder().respawnDelay(50)
        .respawnWithId(NpcId.KRIL_TSUTSAROTH_650).respawnWithId(NpcId.ZAKLN_GRITCH_142)
        .respawnWithId(NpcId.BALFRUG_KREEYATH_151).build());
    tstanonKarlakCombat.hitpoints(NpcCombatHitpoints.total(142));
    tstanonKarlakCombat.stats(NpcCombatStats.builder().attackLevel(124).magicLevel(50)
        .rangedLevel(50).defenceLevel(125).bonus(CombatBonus.DEFENCE_MAGIC, -5).build());
    tstanonKarlakCombat.aggression(NpcCombatAggression.builder().range(16).build());
    tstanonKarlakCombat
        .killCount(NpcCombatKillCount.builder().asName("K'ril Tsutsaroth's bodyguard").build());
    tstanonKarlakCombat.type(NpcCombatType.DEMON).deathAnimation(67).blockAnimation(65);
    tstanonKarlakCombat.drop(tstanonKarlakDrop.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_CRUSH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(64).attackSpeed(5);
    style.projectile(NpcCombatProjectile.id(335));
    tstanonKarlakCombat.style(style.build());


    return Arrays.asList(krilTsutsarothCombat.build(), balfrugKreeyathCombat.build(),
        zaklnGritchCombat.build(), tstanonKarlakCombat.build());
  }

  @Override
  public void restoreHook() {
    if (npc.getId() != NpcId.KRIL_TSUTSAROTH_650) {
      return;
    }
    var respawns =
        new int[] { NpcId.BALFRUG_KREEYATH_151, NpcId.ZAKLN_GRITCH_142, NpcId.TSTANON_KARLAK_145 };
    for (var id : respawns) {
      var respawningNpc = npc.getController().getNpc(id);
      if (respawningNpc != null && !respawningNpc.isVisible() && respawningNpc.getRespawns()) {
        respawningNpc.restore();
      }
    }
  }

  @Override
  public NpcCombatStyle attackTickCombatStyleHook(NpcCombatStyle combatStyle, Entity opponent) {
    if (combatStyle.getType().getHitType() == HitType.MELEE && PRandom.randomI(10) == 0) {
      return SPECIAL_ATTACK;
    }
    return combatStyle;
  }

  @Override
  public void applyAttackEndHook(NpcCombatStyle combatStyle, Entity opponent,
      int applyAttackLoopCount, HitEvent hitEvent) {
    if (combatStyle == SPECIAL_ATTACK && opponent instanceof Player) {
      var player = (Player) opponent;
      player.getPrayer().adjustPoints(-(player.getPrayer().getPoints() / 2));
      player.getGameEncoder().sendMessage(
          "K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
    }
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getArea().script("increase_zamorak_killcount");
  }
}
