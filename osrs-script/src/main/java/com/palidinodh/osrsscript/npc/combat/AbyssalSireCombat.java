package com.palidinodh.osrsscript.npc.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitMark;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.HitpointsBar;
import com.palidinodh.osrscore.model.Tile;
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
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatType;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatEffect;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.rs.setting.SqlUserRank;
import lombok.var;

public class AbyssalSireCombat extends NpcCombat {
  private Npc npc;
  private int phase;
  private Npc[] respiratorySystems;
  private Npc[] vents;
  private Npc[] tentacles;
  private List<Npc> spawns = new ArrayList<>();
  private Player combatWith;
  private int countdown;
  private int status;
  private Tile moveTo;
  private int fumeDelay;
  private Tile fumeTile;
  private int disorientingState;
  private int disorientingDelay;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var drop = NpcCombatDrop.builder().underKiller(true)
        .rareDropTableRate(NpcCombatDropTable.CHANCE_1_IN_256)
        .clue(NpcCombatDrop.ClueScroll.ELITE, 0.56);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_100);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNSIRED)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.YEW_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SPIRIT_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATER_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.FIRE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BODY_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NATURE_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MIND_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_TALISMAN)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_TALISMAN)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATEBODY_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_SWORD_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM_NOTED, 3)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_KITESHIELD_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BATTLESTAFF_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.AIR_BATTLESTAFF_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_AIR_STAFF_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MYSTIC_LAVA_STAFF_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.EARTH_ORB_NOTED, 47, 53)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR_NOTED, 5)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_KWUARM_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_LANTADYME_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_CADANTINE_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_DWARF_WEED_NOTED, 25)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WATERMELON_SEED, 30)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SNAPDRAGON_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.TORSTOL_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RANARR_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PAPAYA_TREE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PALM_TREE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.WILLOW_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAPLE_SEED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_BREW_3_NOTED, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_RESTORE_4, 4)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.LAW_RUNE, 250)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 300, 370)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SOUL_RUNE, 225, 275)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLOOD_RUNE, 160, 210)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.UNCUT_DIAMOND_NOTED, 15)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BINDING_NECKLACE_NOTED, 25)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JUG_OF_WATER_NOTED, 254, 350)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CANNONBALL, 300)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRIMY_TORSTOL_NOTED, 1, 5)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PURE_ESSENCE_NOTED, 600)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 380, 420)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MAGIC_LOGS_NOTED, 51, 70)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHILLI_POTATO_NOTED, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COSMIC_RUNE, 350)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 9000, 51989)));
    drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ASHES)));
    drop.table(dropTable.build());


    var combat1 = NpcCombatDefinition.builder();
    combat1.id(NpcId.ABYSSAL_SIRE_350).id(NpcId.ABYSSAL_SIRE_350_5887)
        .id(NpcId.ABYSSAL_SIRE_350_5888);
    combat1
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_60).build());
    combat1.stats(NpcCombatStats.builder().attackLevel(180).magicLevel(200).defenceLevel(250)
        .bonus(CombatBonus.MELEE_ATTACK, 65).bonus(CombatBonus.DEFENCE_STAB, 40)
        .bonus(CombatBonus.DEFENCE_SLASH, 60).bonus(CombatBonus.DEFENCE_CRUSH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 20).bonus(CombatBonus.DEFENCE_RANGED, 60).build());
    combat1.slayer(NpcCombatSlayer.builder().level(85).build());
    combat1.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat1
        .focus(NpcCombatFocus.builder().retaliationDisabled(true).bypassMapObjects(true).build());
    combat1.type(NpcCombatType.DEMON);


    var combat2 = NpcCombatDefinition.builder();
    combat2.id(NpcId.ABYSSAL_SIRE_350_5889).id(NpcId.ABYSSAL_SIRE_350_5890);
    combat2
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_60).build());
    combat2.stats(NpcCombatStats.builder().attackLevel(180).magicLevel(200).defenceLevel(250)
        .bonus(CombatBonus.MELEE_ATTACK, 65).bonus(CombatBonus.DEFENCE_STAB, 40)
        .bonus(CombatBonus.DEFENCE_SLASH, 60).bonus(CombatBonus.DEFENCE_CRUSH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 20).bonus(CombatBonus.DEFENCE_RANGED, 60).build());
    combat2.slayer(NpcCombatSlayer.builder().level(85).build());
    combat2.aggression(NpcCombatAggression.PLAYERS);
    combat2.immunity(NpcCombatImmunity.builder().poison(true).venom(true).bind(true).build());
    combat2.focus(
        NpcCombatFocus.builder().bypassMapObjects(true).disableFollowingOpponent(true).build());
    combat2.type(NpcCombatType.DEMON);

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.builder().maximum(8).prayerEffectiveness(0.4).build());
    style.animation(5751).attackSpeed(7);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(8).build());
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.builder().maximum(18).prayerEffectiveness(0.4).build());
    style.animation(5369).attackSpeed(7);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(8).build());
    combat2.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_STAB);
    style.damage(NpcCombatDamage.builder().maximum(36).prayerEffectiveness(0.4).build());
    style.animation(5755).attackSpeed(7);
    style.projectile(NpcCombatProjectile.id(335));
    style.effect(NpcCombatEffect.builder().poison(8).build());
    combat2.style(style.build());


    var combat3 = NpcCombatDefinition.builder();
    combat3.id(NpcId.ABYSSAL_SIRE_350_5891).id(NpcId.ABYSSAL_SIRE_350_5908);
    combat3
        .hitpoints(NpcCombatHitpoints.builder().total(400).bar(HitpointsBar.GREEN_RED_60).build());
    combat3.stats(NpcCombatStats.builder().attackLevel(180).magicLevel(200).defenceLevel(250)
        .bonus(CombatBonus.MELEE_ATTACK, 65).bonus(CombatBonus.DEFENCE_STAB, 40)
        .bonus(CombatBonus.DEFENCE_SLASH, 60).bonus(CombatBonus.DEFENCE_CRUSH, 50)
        .bonus(CombatBonus.DEFENCE_MAGIC, 20).bonus(CombatBonus.DEFENCE_RANGED, 60).build());
    combat3.slayer(NpcCombatSlayer.builder().level(85).build());
    combat3.aggression(NpcCombatAggression.PLAYERS);
    combat3.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    combat3.focus(NpcCombatFocus.builder().retaliationDisabled(true).bypassMapObjects(true)
        .disableFollowingOpponent(true).build());
    combat3.killCount(NpcCombatKillCount.builder().sendMessage(true).build());
    combat3.type(NpcCombatType.DEMON).deathAnimation(7100);
    combat3.drop(drop.build());


    return Arrays.asList(combat1.build(), combat2.build(), combat3.build());
  }

  public static Tile[][] RESPIRATORY_SYSTEMS =
      {{new Tile(2964, 4844), new Tile(2992, 4843), new Tile(2967, 4834), new Tile(2995, 4833)},
          {new Tile(3089, 4844), new Tile(3117, 4843), new Tile(3092, 4834), new Tile(3120, 4833)},
          {new Tile(2954, 4780), new Tile(2982, 4779), new Tile(2957, 4770), new Tile(2985, 4769)},
          {new Tile(3094, 4780), new Tile(3122, 4779), new Tile(3097, 4770), new Tile(3125, 4769)}};
  public static Tile[][] TENTACLES = {
      {new Tile(2967, 4844), new Tile(2984, 4844), new Tile(2970, 4835), new Tile(2982, 4835),
          new Tile(2968, 4826), new Tile(2985, 4826)},
      {new Tile(3092, 4844), new Tile(3109, 4844), new Tile(3095, 4835), new Tile(3107, 4835),
          new Tile(3093, 4826), new Tile(3110, 4826)},
      {new Tile(2957, 4780), new Tile(2974, 4780), new Tile(2960, 4771), new Tile(2972, 4771),
          new Tile(2958, 4762), new Tile(2975, 4762)},
      {new Tile(3097, 4780), new Tile(3114, 4780), new Tile(3100, 4771), new Tile(3112, 4771),
          new Tile(3098, 4762), new Tile(3115, 4762)}};

  @Override
  public Object script(String name, Object... args) {
    if (name.equals("combat_with")) {
      return combatWith;
    }
    if (name.equals("phase")) {
      return phase;
    }
    if (name.equals("disorienting_delay")) {
      return disorientingDelay;
    }
    return null;
  }

  @Override
  public void spawnHook() {
    npc = getNpc();
  }

  @Override
  public void restoreHook() {
    combatWith = null;
    countdown = 0;
    status = 0;
    fumeDelay = 0;
    disorientingState = 0;
    disorientingDelay = 0;
    moveTo = null;
    phase = 0;
    npc.getWorld().removeNpcs(respiratorySystems);
    npc.getWorld().removeNpcs(vents);
    npc.getWorld().removeNpcs(tentacles);
    npc.getWorld().removeNpcs(spawns);
    spawns.clear();
    if (npc.isVisible()) {
      respiratorySystems = new Npc[4];
      vents = new Npc[respiratorySystems.length];
      for (var i = 0; i < respiratorySystems.length; i++) {
        var tile = RESPIRATORY_SYSTEMS[getRegionIndex()][i];
        respiratorySystems[i] = new Npc(npc.getController(), NpcId.RESPIRATORY_SYSTEM, tile);
        respiratorySystems[i].setRespawns(npc.getRespawns());
        respiratorySystems[i].getCombat2().script("abyssal_sire", npc);
        vents[i] = new Npc(npc.getController(), NpcId.VENT, tile);
        vents[i].setRespawns(npc.getRespawns());
      }
      tentacles = new Npc[6];
      for (var i = 0; i < tentacles.length; i++) {
        var id = -1;
        if (i == 0 || i == 1) {
          id = NpcId.TENTACLE_5910;
        } else if (i == 2 || i == 3) {
          id = NpcId.TENTACLE_5911;
        } else if (i == 4 || i == 5) {
          id = NpcId.TENTACLE_5909;
        }
        var tile2 = TENTACLES[getRegionIndex()][i];
        tentacles[i] = new Npc(npc.getController(), id, tile2);
        tentacles[i].setRespawns(npc.getRespawns());
      }
      for (var spawnedNpc : respiratorySystems) {
        spawnedNpc.getMovement().setImmobile(true);
      }
      for (var spawnedNpc : tentacles) {
        spawnedNpc.getMovement().setImmobile(true);
      }
    }
  }

  @Override
  public void tickStartHook() {
    if (!npc.isLocked() && phase > 0
        && (npc.getLastHitByDelay() > 500 || !npc.withinMapDistance(combatWith))) {
      npc.restore();
    }
    if (!npc.isLocked()) {
      npc.getMovement().setImmobile(true);
    }
    if (combatWith != null) {
      npc.getCombat2().setDisableAutoRetaliate(true);
    } else {
      npc.getCombat2().setDisableAutoRetaliate(false);
    }
    if (countdown > 0) {
      countdown--;
    }
    updateAttacking();
    fumeTick();
    spawnTick();
    if (phase == 1) {
      phase1Tick();
    } else if (phase == 2) {
      phase2Tick();
    } else if (phase == 3) {
      phase3Tick();
    } else if (phase == 4) {
      phase4Tick();
    }
  }

  @Override
  public void tickEndHook() {
    updateAttacking();
    if (moveTo != null) {
      npc.getMovement().clear();
      npc.getMovement().addMovement(moveTo);
      if (npc.withinDistance(moveTo, 0)) {
        moveTo = null;
      }
    }
  }

  @Override
  public void applyDeadHook() {
    combatWith = null;
    lockTentacles(Integer.MAX_VALUE);
  }

  @Override
  public boolean canBeAttackedHook(Entity entity, boolean sendMessage, HitType hitType) {
    if (!(entity instanceof Player)) {
      return false;
    }
    var player = (Player) entity;
    if (!Settings.getInstance().isSpawn() && !player.getSkills().isAnySlayerTask(npc)
        && !player.isUsergroup(SqlUserRank.YOUTUBER)) {
      if (sendMessage) {
        player.getGameEncoder()
            .sendMessage("This can only be attacked on an appropriate Slayer task.");
      }
      return false;
    }
    if (combatWith != null && player != combatWith) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("Abyssal Sire is busy with someone else.");
      }
      return false;
    }
    if (phase == 1 && disorientingDelay > 0) {
      if (sendMessage) {
        player.getGameEncoder().sendMessage("The Abyssal Sire is currently disoriented.");
      }
      return false;
    }
    return !npc.isLocked();
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (!(opponent instanceof Player)) {
      return 0;
    }
    var player = (Player) opponent;
    combatWith = player;
    if (phase == 0) {
      wakeUp();
    } else if (phase == 1) {
      if (disorientingDelay == 0 && npc.getId() != NpcId.ABYSSAL_SIRE_350 && !npc.isLocked()) {
        if (hitType == HitType.MAGIC && player.getMagic().getActiveSpell() != null
            && player.getMagic().getActiveSpell().getName().contains("shadow")) {
          if (player.getMagic().getActiveSpell().getName().contains("rush")) {
            disorientingState += 25;
          } else if (player.getMagic().getActiveSpell().getName().contains("burst")) {
            disorientingState += 50;
          } else if (player.getMagic().getActiveSpell().getName().contains("blitz")) {
            disorientingState += 75;
          } else if (player.getMagic().getActiveSpell().getName().contains("barrage")) {
            disorientingState += 100;
          }
        } else {
          disorientingState += (int) (damage * 1.33);
        }
      } else if (disorientingDelay > 0) {
        damage = 0;
      }
    }
    if (phase != 4 && damage > npc.getHitpoints()) {
      damage = npc.getHitpoints() - 1;
    }
    return damage;
  }

  private void updateAttacking() {
    if (!npc.isDead() && npc.isVisible() && npc.withinDistance(combatWith, 32)) {
      npc.setAttacking(true);
      npc.setEngagingEntity(combatWith);
      if (npc.isLocked() || !npc.getDef().getCombat().hasAttack()) {
        npc.setFaceEntity(null);
      }
      for (var spawnedNpc : tentacles) {
        spawnedNpc.setAttacking(true);
        spawnedNpc.setEngagingEntity(combatWith);
        if (spawnedNpc.isLocked() || !spawnedNpc.getDef().hasAttack()) {
          spawnedNpc.setFaceEntity(null);
        }
      }
      for (var spawnedNpc : spawns) {
        spawnedNpc.setAttacking(true);
        spawnedNpc.setEngagingEntity(combatWith);
        if (spawnedNpc.isLocked() || !spawnedNpc.getDef().hasAttack()) {
          spawnedNpc.setFaceEntity(null);
        }
      }
    } else if (combatWith != null) {
      combatWith = null;
    }
  }

  private void wakeUp() {
    if (npc.getId() != NpcId.ABYSSAL_SIRE_350) {
      return;
    }
    phase = 1;
    endDisoriented();
  }

  private void lockTentacles(int lock) {
    for (var spawnedNpc : tentacles) {
      if (spawnedNpc.getId() != NpcId.TENTACLE_5913) {
        spawnedNpc.setTransformationId(NpcId.TENTACLE_5913);
        spawnedNpc.setAnimation(7112);
        spawnedNpc.setFaceTile(new Tile(spawnedNpc).moveTile(1, -1));
        spawnedNpc.setLock(lock);
      }
    }
  }

  private void unlockTentacles() {
    for (var spawnedNpc : tentacles) {
      if (spawnedNpc.getId() != NpcId.TENTACLE_5912) {
        spawnedNpc.setTransformationId(NpcId.TENTACLE_5912);
        spawnedNpc.setAnimation(7114);
        spawnedNpc.setLock(5);
      }
    }
  }

  private void teleportPlayer() {
    if (combatWith != null && combatWith.isVisible()) {
      combatWith.getMovement().animatedTeleport(new Tile(npc).moveTile(0, -1), 1816,
          new Graphic(342, 100), 2);
    }
  }

  private void fumeTick() {
    if (fumeDelay > 0) {
      fumeDelay--;
      if (fumeTile != null && fumeDelay < 4 && combatWith != null && !combatWith.isLocked()) {
        if (fumeTile.withinDistance(combatWith, 0)) {
          var hitEvent = new HitEvent(0, combatWith, new Hit(28, HitMark.POISON));
          combatWith.addHit(hitEvent);
        } else if (fumeTile.withinDistance(combatWith, 1)) {
          var hitEvent2 = new HitEvent(0, combatWith, new Hit(14, HitMark.POISON));
          combatWith.addHit(hitEvent2);
        }
      }
    } else if (combatWith != null && !combatWith.isLocked() && fumeDelay == 0 && !npc.isLocked()
        && (phase != 1 || disorientingDelay == 0) && PRandom.randomE(5) == 0) {
      fumeDelay = 6;
      fumeTile = new Tile(combatWith);
      npc.getController().sendMapGraphic(fumeTile, new Graphic(1275));
    }
  }

  private void spawnTick() {
    if (spawns.isEmpty()) {
      return;
    }
    var iterator = spawns.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().isDead()) {
        iterator.remove();
      }
    }
  }

  private void summonSpawn(int x, int y) {
    if (npc.isDead() || !npc.isVisible()) {
      return;
    }
    var spawnedNpc = new Npc(npc.getController(), NpcId.SPAWN_60, x, y, npc.getHeight());
    spawnedNpc.setRespawns(npc.getRespawns());
    spawnedNpc.getCombat2().script("abyssal_sire", npc);
    spawns.add(spawnedNpc);
  }

  private void phase1Tick() {
    var areDead = true;
    for (int i = 0; i < respiratorySystems.length; ++i) {
      var spawnedNpc = respiratorySystems[i];
      if (spawnedNpc.isVisible()) {
        areDead = false;
        if (spawnedNpc.isDead()) {
          vents[i].setAnimation(7102);
        } else if (disorientingDelay > 0) {
          vents[i].setAnimation(7105);
        }
      } else {
        vents[i].setVisible(false);
      }
    }
    if (areDead) {
      startPhase2();
    } else if (disorientingDelay == 0 && disorientingState >= 100) {
      startDisoriented();
    } else if (disorientingDelay > 0) {
      disorientingDelay--;
      if (disorientingDelay == 0) {
        endDisoriented();
      }
    }
  }

  private void startDisoriented() {
    disorientingDelay = 40;
    npc.setTransformationId(NpcId.ABYSSAL_SIRE_350_5888);
    npc.setAnimation(4531);
    npc.setLock(4);
    lockTentacles(2);
  }

  private void endDisoriented() {
    disorientingState = 0;
    npc.setTransformationId(NpcId.ABYSSAL_SIRE_350_5887);
    npc.setAnimation(4528);
    npc.setLock(8);
    unlockTentacles();
    for (var vent : vents) {
      vent.setAnimation(-1);
    }
  }

  private void phase2Tick() {
    if (!npc.getMovement().isRouting() && combatWith != null && combatWith.isAttacking()
        && combatWith.getEngagingEntity() == npc && npc.getHitDelay() <= 0
        && !npc.withinDistance(combatWith, 1)
        && Route.canMove(combatWith, npc, new Tile(npc).moveTile(0, -1))) {
      teleportPlayer();
    }
    if (npc.getHitpoints() < npc.getMaxHitpoints() / 2) {
      startPhase3();
    }
  }

  private void startPhase2() {
    phase = 2;
    npc.clearHits();
    npc.setTransformationId(NpcId.ABYSSAL_SIRE_350_5890);
    npc.setHitpoints(npc.getDef().getCombat().getHitpoints().getTotal());
    npc.setAnimation(4532);
    npc.setLock(19);
    npc.getMovement().setImmobile(false);
    npc.setFaceEntity(null);
    npc.getMovement().setFollowing(null);
    npc.getController().setMagicBind(8, null);
    moveTo = new Tile(npc.getX(), npc.getY() - 10, npc.getHeight());
    npc.getMovement().clear();
    npc.getMovement().addMovement(moveTo);
    lockTentacles(2);
  }

  private void phase3Tick() {
    if (status == 0 && countdown == 0) {
      status = 1;
      countdown = 6;
      npc.setTransformationId(NpcId.ABYSSAL_SIRE_350_5891);
      npc.setAnimation(7096);
    } else if (status == 1 && countdown == 0) {
      status = 2;
      unlockTentacles();
      summonSpawn(npc.getX(), npc.getY());
      summonSpawn(npc.getX() + 1, npc.getY());
      summonSpawn(npc.getX(), npc.getY() + 1);
      summonSpawn(npc.getX() + 1, npc.getY() + 1);
    }
    if (npc.getHitpoints() < npc.getMaxHitpoints() * 0.35) {
      startPhase4();
    }
  }

  private void startPhase3() {
    phase = 3;
    npc.clearHits();
    countdown = 10;
    npc.setLock(countdown + 6);
    npc.setFaceEntity(null);
    npc.getMovement().setImmobile(false);
    npc.getMovement().setFollowing(null);
    moveTo = new Tile(npc.getX(), npc.getY() - 9, npc.getHeight());
    npc.getMovement().clear();
    npc.getMovement().addMovement(moveTo);
  }

  private void phase4Tick() {
    if (status == 0 && countdown == 0) {
      status = 1;
      if (combatWith != null && npc.withinDistance(combatWith, 1)) {
        var hitEvent = new HitEvent(0, combatWith, new Hit(PRandom.randomI(72)));
        combatWith.addHit(hitEvent);
      }
      summonSpawn(npc.getX(), npc.getY());
      summonSpawn(npc.getX() + 1, npc.getY());
      summonSpawn(npc.getX(), npc.getY() + 1);
      summonSpawn(npc.getX() + 1, npc.getY() + 1);
    }
  }

  private void startPhase4() {
    phase = 4;
    npc.clearHits();
    status = 0;
    countdown = 6;
    npc.setLock(6);
    npc.setTransformationId(NpcId.ABYSSAL_SIRE_350_5908);
    npc.setAnimation(7098);
    teleportPlayer();
  }

  private int getRegionIndex() {
    if (npc.getRegionId() == 12363) {
      return 1;
    }
    if (npc.getRegionId() == 11850) {
      return 2;
    }
    if (npc.getRegionId() == 12362) {
      return 3;
    }
    return 0;
  }
}
