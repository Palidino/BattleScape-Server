package com.palidinodh.osrsscript.npc.combat;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.CombatBonus;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.ForceMovement;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Hit;
import com.palidinodh.osrscore.model.HitEvent;
import com.palidinodh.osrscore.model.HitType;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.TileHitEvent;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.combat.NpcCombat;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatAggression;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDefinition;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTable;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatDropTableDrop;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatHitpoints;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatImmunity;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatKillCount;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSlayer;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatSpawn;
import com.palidinodh.osrscore.model.npc.combat.NpcCombatStats;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatDamage;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatProjectile;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyle;
import com.palidinodh.osrscore.model.npc.combat.style.NpcCombatStyleType;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import lombok.var;

class GrotesqueGuardianCombat extends NpcCombat {
  private static final Tile DUSK_TILE_ATTACK = new Tile(1691, 4573),
      DAWN_TILE_ATTACK = new Tile(1699, 4573);
  private static final int PHASE_1 = 0, PHASE_1_POSITION = 1, PHASE_1_SPECIAL_PREP = 2,
      PHASE_1_SPECIAL = 3, PHASE_2_INITIAL = 4, PHASE_2_INITIAL_2 = 5, PHASE_2 = 6,
      PHASE_2_POSITION_1 = 7, PHASE_2_SPECIAL_PREP = 8, PHASE_2_SPECIAL = 9, PHASE_3_INITIAL = 10,
      PHASE_3 = 11, PHASE_2_POSITION_2 = 12, PHASE_4_INITIAL_1 = 13, PHASE_4_INITIAL_2 = 14,
      PHASE_4 = 15;
  private static final int EXPLOSIVE_PHASE_START = 0, EXPLOSIVE_PHASE_END = 1;
  private static final int PRISON_PHASE_START = 0, PRISON_PHASE_END = 1;
  private static final Tile BASE_TILE = new Tile(1689, 4567);

  @Inject
  private Npc npc;
  private int phase;
  private Npc dusk;
  private Npc dawn;
  private int explosivePhase;
  private int explosiveTick;
  private int sphereTick;
  private int rocksTick;
  private int prisonPhase;
  private int prisonTick;

  @Override
  public List<NpcCombatDefinition> getCombatDefinitions() {
    var dawn1Combat = NpcCombatDefinition.builder();
    dawn1Combat.id(NpcId.DAWN_228);
    dawn1Combat.spawn(NpcCombatSpawn.builder().lock(16).direction(3).animation(7766).build());
    dawn1Combat.hitpoints(NpcCombatHitpoints.total(450));
    dawn1Combat.stats(NpcCombatStats.builder().attackLevel(140).magicLevel(100).rangedLevel(140)
        .defenceLevel(100).bonus(CombatBonus.DEFENCE_MAGIC, 80).build());
    dawn1Combat.slayer(NpcCombatSlayer.builder().level(75).build());
    dawn1Combat.aggression(NpcCombatAggression.builder().range(20).build());
    dawn1Combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());

    var style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7769).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    dawn1Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7770).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(1437));
    style.attackCount(2);
    dawn1Combat.style(style.build());


    var dawn2Combat = NpcCombatDefinition.builder();
    dawn2Combat.id(NpcId.DAWN_228_7885);
    dawn2Combat.spawn(NpcCombatSpawn.builder().lock(16).direction(3).animation(7766).build());
    dawn2Combat.hitpoints(NpcCombatHitpoints.total(450));
    dawn2Combat.stats(NpcCombatStats.builder().attackLevel(140).magicLevel(100).rangedLevel(140)
        .defenceLevel(100).bonus(CombatBonus.DEFENCE_MAGIC, 80).build());
    dawn2Combat.slayer(NpcCombatSlayer.builder().level(75).build());
    dawn2Combat.aggression(NpcCombatAggression.builder().range(20).build());
    dawn2Combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    dawn2Combat.deathAnimation(7777);


    var duskCombat1 = NpcCombatDefinition.builder();
    duskCombat1.id(NpcId.DUSK_248);
    duskCombat1.spawn(NpcCombatSpawn.builder().lock(16).direction(4).animation(7778).build());
    duskCombat1.hitpoints(NpcCombatHitpoints.total(450));
    duskCombat1.stats(NpcCombatStats.builder().attackLevel(200).magicLevel(140).rangedLevel(140)
        .defenceLevel(100).build());
    duskCombat1.slayer(NpcCombatSlayer.builder().level(75).build());
    duskCombat1.aggression(NpcCombatAggression.builder().range(20).build());
    duskCombat1.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7785).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    duskCombat1.style(style.build());


    var duskCombat2 = NpcCombatDefinition.builder();
    duskCombat2.id(NpcId.DUSK_248_7855);
    duskCombat2.spawn(NpcCombatSpawn.builder().lock(16).direction(4).animation(7778).build());
    duskCombat2.hitpoints(NpcCombatHitpoints.total(450));
    duskCombat2.stats(NpcCombatStats.builder().attackLevel(200).magicLevel(140).rangedLevel(140)
        .defenceLevel(100).build());
    duskCombat2.slayer(NpcCombatSlayer.builder().level(75).build());
    duskCombat2.aggression(NpcCombatAggression.builder().range(20).build());
    duskCombat2.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7785).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    duskCombat2.style(style.build());


    var dusk3Combat = NpcCombatDefinition.builder();
    dusk3Combat.id(NpcId.DUSK_248_7882);
    dusk3Combat.spawn(NpcCombatSpawn.builder().lock(16).direction(4).animation(7778).build());
    dusk3Combat.hitpoints(NpcCombatHitpoints.total(450));
    dusk3Combat.stats(NpcCombatStats.builder().attackLevel(200).magicLevel(140).rangedLevel(140)
        .defenceLevel(100).build());
    dusk3Combat.slayer(NpcCombatSlayer.builder().level(75).build());
    dusk3Combat.aggression(NpcCombatAggression.builder().range(20).build());
    dusk3Combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(15));
    style.animation(7786).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    dusk3Combat.style(style.build());


    var dusk4Combat = NpcCombatDefinition.builder();
    dusk4Combat.id(NpcId.DUSK_328_7888);
    dusk4Combat.spawn(NpcCombatSpawn.builder().lock(16).direction(4).animation(7778).build());
    dusk4Combat.hitpoints(NpcCombatHitpoints.total(450));
    dusk4Combat.stats(NpcCombatStats.builder().attackLevel(300).magicLevel(250).rangedLevel(250)
        .defenceLevel(150).build());
    dusk4Combat.slayer(NpcCombatSlayer.builder().level(75).build());
    dusk4Combat.aggression(NpcCombatAggression.builder().range(20).build());
    dusk4Combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MELEE_SLASH);
    style.damage(NpcCombatDamage.maximum(26));
    style.animation(7800).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(335));
    dusk4Combat.style(style.build());

    style = NpcCombatStyle.builder();
    style.type(NpcCombatStyleType.MAGIC);
    style.damage(NpcCombatDamage.maximum(26));
    style.animation(7801).attackSpeed(6);
    style.projectile(NpcCombatProjectile.id(1437));
    style.attackCount(2);
    dusk4Combat.style(style.build());


    var dusk5Drop = NpcCombatDrop.builder().rolls(2).clue(NpcCombatDrop.ClueScroll.ELITE,
        NpcCombatDropTable.CHANCE_1_IN_230);
    var dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_5000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.JAR_OF_STONE)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_3000)
        .order(NpcCombatDropTable.Order.RANDOM_UNIQUE).broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.NOON)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_1000)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.BLACK_TOURMALINE_CORE)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_750)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_HAMMER)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_1_IN_250)
        .broadcast(true).log(true);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_GLOVES)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_RING)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_RARE);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_MAUL)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANT_BOOTS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_MED_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ONYX_BOLT_TIPS, 5, 10)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_ARROWTIPS, 50, 150)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_UNCOMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PICKAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_FULL_HELM)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_BATTLEAXE)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_PLATELEGS)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNE_2H_SWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_LONGSWORD)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COAL_NOTED, 185, 245)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_ORE_NOTED, 40, 50)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_ORE_NOTED, 3, 6)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GOLD_BAR_NOTED, 42, 49)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MITHRIL_BAR_NOTED, 36, 45)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.ADAMANTITE_BAR_NOTED, 25, 58)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.RUNITE_BAR_NOTED, 3, 9)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGON_DART_TIP, 15, 25)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DIAMOND_BOLT_TIPS, 105, 147)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DRAGONSTONE_BOLT_TIPS, 19, 28)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SUPER_COMBAT_POTION_2_NOTED),
        new RandomItem(ItemId.RANGING_POTION_2_NOTED),
        new RandomItem(ItemId.MAGIC_POTION_2_NOTED)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.PRAYER_POTION_4_NOTED, 1, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.SARADOMIN_BREW_4_NOTED, 2)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CRYSTAL_KEY)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_COMMON);
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.CHAOS_RUNE, 100, 150)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.DEATH_RUNE, 71, 99)));
    dropTable.drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.COINS, 10003, 25000)));
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.MUSHROOM_POTATO_NOTED, 4, 6)));
    dusk5Drop.table(dropTable.build());
    dropTable = NpcCombatDropTable.builder().chance(NpcCombatDropTable.CHANCE_ALWAYS);
    dropTable
        .drop(NpcCombatDropTableDrop.items(new RandomItem(ItemId.GRANITE_CANNONBALL, 50, 100)));
    dusk5Drop.table(dropTable.build());


    var dusk5Combat = NpcCombatDefinition.builder();
    dusk5Combat.id(NpcId.DUSK_328_7889);
    dusk5Combat.spawn(NpcCombatSpawn.builder().lock(16).direction(4).animation(7778).build());
    dusk5Combat.hitpoints(NpcCombatHitpoints.total(450));
    dusk5Combat.stats(NpcCombatStats.builder().attackLevel(200).magicLevel(140).rangedLevel(140)
        .defenceLevel(100).build());
    dusk5Combat.slayer(NpcCombatSlayer.builder().level(75).build());
    dusk5Combat.aggression(NpcCombatAggression.builder().range(20).build());
    dusk5Combat.immunity(NpcCombatImmunity.builder().poison(true).venom(true).build());
    dusk5Combat.killCount(
        NpcCombatKillCount.builder().asName("Grotesque Guardians").sendMessage(true).build());
    dusk5Combat.deathAnimation(7809);
    dusk5Combat.drop(dusk5Drop.build());


    return Arrays.asList(dawn1Combat.build(), dawn2Combat.build(), duskCombat1.build(),
        duskCombat2.build(), dusk3Combat.build(), dusk4Combat.build(), dusk5Combat.build());
  }

  @Override
  public Object script(String name, Object... args) {
    if (name.equals("phase")) {
      phase = (int) args[0];
    }
    return null;
  }

  @Override
  public void restoreHook() {
    phase = PHASE_1;
    dusk = null;
    dawn = null;
    explosivePhase = EXPLOSIVE_PHASE_START;
    explosiveTick = 0;
    prisonPhase = PRISON_PHASE_END;
    prisonTick = 0;
    sphereTick = 0;
    rocksTick = 8;
  }

  @Override
  public void despawnHook() {
    npc.getWorld().removeNpc(dawn);
  }

  @Override
  public void tickStartHook() {
    if (dusk == null) {
      dusk = npc.getController().getNpc(NpcId.DUSK_248);
    }
    if (dawn == null) {
      dawn = npc.getController().getNpc(NpcId.DAWN_228);
    }
    if (npc.getId() == NpcId.DUSK_248 && npc.getTotalTicks() == 10) {
      npc.getMovement().clear();
      npc.getMovement().addMovement(npc.getX() + 4, npc.getY());
      npc.setAnimation(7789);
    } else if (npc.getId() == NpcId.DAWN_228 && npc.getTotalTicks() == 10) {
      npc.getMovement().clear();
      npc.getMovement().addMovement(npc.getX() - 4, npc.getY());
      npc.setAnimation(7767);
    }
    if (explosiveTick > 0) {
      explosiveTick--;
    }
    if (prisonTick > 0) {
      prisonTick--;
    }
    if (sphereTick > 0) {
      sphereTick--;
    }
    if (npc.isDead() && npc.getRespawnDelay() == npc.getDef().getDeathDelay() - 2) {
      if (npc.getId() == NpcId.DAWN_228) {
        npc.setTransformationId(NpcId.DAWN_228_7885);
      } else if (npc.getId() == NpcId.DUSK_248 || npc.getId() == NpcId.DUSK_248_7882
          || npc.getId() == NpcId.DUSK_328_7888) {
        npc.setTransformationId(NpcId.DUSK_328_7889);
      }
    }
    if (npc.isLocked() || dusk == null || dawn == null) {
      return;
    }
    if (phase1()) {
    } else if (phase2()) {
    } else if (phase3()) {
    } else if (phase4()) {
    }
  }

  @Override
  public Graphic.Projectile mapProjectileHook(Graphic.Projectile projectile) {
    if (projectile.getId() == 1437) {
      var speed1 = new Graphic.ProjectileSpeed(projectile.getEventDelay(),
          projectile.getDelay() + 4, projectile.getSpeed());
      var speed2 = new Graphic.ProjectileSpeed(projectile.getEventDelay(),
          projectile.getDelay() - 12, projectile.getSpeed());
      npc.getController().sendMapProjectile(projectile.rebuilder().projectileSpeed(speed1).build());
      npc.getController().sendMapProjectile(projectile.rebuilder().projectileSpeed(speed2).build());
      return null;
    }
    return projectile;
  }

  @Override
  public boolean canAttackEntityHook(NpcCombatStyle combatStyle, Entity opponent) {
    return !npc.isLocked();
  }

  @Override
  public void deathDropItemsHook(Player player, int additionalPlayerLoopCount, Tile dropTile) {
    player.getGameEncoder().setVarp(1667, 0);
  }

  @Override
  public boolean canBeAttackedHook(Entity opponent, boolean sendMessage, HitType hitType) {
    if (!(opponent instanceof Player)) {
      return false;
    }
    var player = (Player) opponent;
    if (npc.getId() == NpcId.DAWN_228 && hitType == HitType.MELEE) {
      if (sendMessage) {
        player.getGameEncoder()
            .sendMessage("Dawn is flying too high for you to attack using melee.");
      }
      return false;
    }
    return !npc.isLocked();
  }

  @Override
  public double damageReceivedHook(Entity opponent, double damage, HitType hitType,
      HitType defenceType) {
    if (npc.getId() == NpcId.DUSK_248) {
      damage = 0;
      if (opponent instanceof Player) {
        var player = (Player) opponent;
        player.getGameEncoder()
            .sendMessage("Dusk is defending himself with his wing! He absorbs the attack.");
      }
    } else if ((npc.getId() == NpcId.DUSK_248_7882 || npc.getId() == NpcId.DUSK_328_7888)
        && hitType != HitType.MELEE) {
      damage = 0;
      if (opponent instanceof Player) {
        var player = (Player) opponent;
        player.getGameEncoder()
            .sendMessage("Dusk's stone armour absorbs all magic and ranged based attacks.");
      }
    } else if (npc.getId() == NpcId.DAWN_228) {
      if (hitType == HitType.MELEE) {
        damage = 0;
      } else if (hitType == HitType.MAGIC) {
        damage *= 0.6;
      }
    }
    return damage;
  }

  private boolean phase1() {
    if (phase == PHASE_1 && dawn.getHitpoints() < dawn.getMaxHitpoints() * 0.5) {
      dusk.getCombat2().clear();
      dusk.getMovement().clear();
      dusk.getMovement().addMovement(DUSK_TILE_ATTACK);
      dawn.getCombat2().clear();
      dawn.getMovement().clear();
      dawn.getMovement().addMovement(DAWN_TILE_ATTACK);
      var lock =
          Math.max(dusk.getMovement().getMoveListSize(), dawn.getMovement().getMoveListSize()) + 2;
      dusk.setLock(lock);
      dawn.setLock(lock);
      dusk.getCombat2().script("phase", PHASE_1_POSITION);
      dawn.getCombat2().script("phase", PHASE_1_POSITION);
      return true;
    } else if (phase == PHASE_1_POSITION) {
      dusk.setFaceTile(new Tile(dusk.getX() + 4, dusk.getY() + 1));
      dusk.setAnimation(7790);
      dusk.setTransformationId(NpcId.DUSK_248_7855);
      dawn.setFaceTile(new Tile(dawn.getX() - 4, dawn.getY()));
      dusk.setLock(4);
      dawn.setLock(4);
      dusk.getCombat2().script("phase", PHASE_1_SPECIAL_PREP);
      dawn.getCombat2().script("phase", PHASE_1_SPECIAL_PREP);
      return true;
    } else if (phase == PHASE_1_SPECIAL_PREP) {
      dusk.setAnimation(7792);
      dawn.setAnimation(7772);
      dusk.setLock(1);
      dawn.setLock(1);
      dusk.getCombat2().script("phase", PHASE_1_SPECIAL);
      dawn.getCombat2().script("phase", PHASE_1_SPECIAL);
      return true;
    } else if (phase == PHASE_1_SPECIAL) {
      tileAttack();
      dusk.getCombat2().script("phase", PHASE_2_INITIAL);
      dawn.getCombat2().script("phase", PHASE_2_INITIAL);
      return true;
    }
    return false;
  }

  private boolean phase2() {
    if (phase == PHASE_2_INITIAL) {
      dusk.setAnimation(7789);
      dusk.setTransformationId(NpcId.DUSK_248_7882);
      dawn.setAnimation(7773);
      dusk.setLock(2);
      dawn.setLock(2);
      dusk.getCombat2().script("phase", PHASE_2_INITIAL_2);
      dawn.getCombat2().script("phase", PHASE_2_INITIAL_2);
      return true;
    } else if (phase == PHASE_2_INITIAL_2) {
      dawn.setVisible(false);
      dawn.lock();
      dusk.getCombat2().script("phase", PHASE_2);
      dawn.getCombat2().script("phase", PHASE_2);
      dusk.setHitDelay(6);
      explosivePhase = EXPLOSIVE_PHASE_START;
      explosiveTick = 0;
      return true;
    } else if (phase == PHASE_2 && npc.getId() == NpcId.DUSK_248_7882) {
      if (npc.getHitDelay() == 0 && explosivePhase == EXPLOSIVE_PHASE_START && explosiveTick == 0) {
        explosivePhase = EXPLOSIVE_PHASE_END;
        explosiveTick = 3;
        npc.setLock(3);
        npc.setAnimation(7802);
      } else if (explosivePhase == EXPLOSIVE_PHASE_END && explosiveTick == 0) {
        explosivePhase = EXPLOSIVE_PHASE_START;
        explosiveTick = 50 + PRandom.randomI(25);
        npc.setHitDelay(6);
        throwPlayers(20, 35);
      }
      if (rocksTick-- == 0) {
        rocksTick = 15 + PRandom.randomI(15);
        for (var i = 0; i < 7; i++) {
          var tile = new Tile(BASE_TILE);
          tile.moveX(PRandom.randomI(15));
          tile.moveY(PRandom.randomI(15));
          npc.getController().sendMapProjectile(null, new Tile(tile).moveY(1), tile, 1435, 255, 10,
              0, 51 + (i + 4) * 10, 0, 0);
          npc.getController().sendMapGraphic(tile, 1436, 0, 51 + (i + 2) * 10);
          npc.getController().sendMapGraphic(tile, 1446 + i / 3, 0, 0);
          var the =
              new TileHitEvent(i < 4 ? 3 : 4, npc.getController(), tile, 10, HitType.TYPELESS);
          the.setRadius(1);
          the.setBind(8);
          the.setGraphic(new Graphic(80, 100));
          the.setMessage("You have been knocked out!");
          npc.getWorld().addEvent(the);
        }
      }
      if (npc.getHitpoints() < npc.getMaxHitpoints() * 0.5) {
        dusk.getCombat2().clear();
        dusk.getMovement().clear();
        dusk.getMovement().addMovement(DUSK_TILE_ATTACK);
        dawn.getCombat2().clear();
        dawn.getMovement().clear();
        dawn.getMovement().teleport(DAWN_TILE_ATTACK);
        var lock = dusk.getMovement().getMoveListSize() + 2;
        dusk.setLock(lock);
        dawn.setLock(lock);
        dusk.getCombat2().script("phase", PHASE_2_POSITION_1);
        dawn.getCombat2().script("phase", PHASE_2_POSITION_1);
      }
      return true;
    } else if (phase == PHASE_2_POSITION_1) {
      dawn.setVisible(true);
      dawn.unlock();
      dawn.setFaceTile(new Tile(dawn.getX() - 4, dawn.getY()));
      dawn.setAnimation(7774);
      dusk.setLock(2);
      dawn.setLock(2);
      dusk.getCombat2().script("phase", PHASE_2_POSITION_2);
      dawn.getCombat2().script("phase", PHASE_2_POSITION_2);
      return true;
    } else if (phase == PHASE_2_POSITION_2) {
      dusk.setFaceTile(new Tile(dusk.getX() + 4, dusk.getY() + 1));
      dusk.setAnimation(7790);
      dusk.setTransformationId(NpcId.DUSK_248_7855);
      dusk.setLock(4);
      dawn.setLock(4);
      dusk.getCombat2().script("phase", PHASE_2_SPECIAL_PREP);
      dawn.getCombat2().script("phase", PHASE_2_SPECIAL_PREP);
      return true;
    } else if (phase == PHASE_2_SPECIAL_PREP) {
      dusk.setAnimation(7792);
      dawn.setAnimation(7772);
      dusk.setLock(1);
      dawn.setLock(1);
      dusk.getCombat2().script("phase", PHASE_2_SPECIAL);
      dawn.getCombat2().script("phase", PHASE_2_SPECIAL);
      return true;
    } else if (phase == PHASE_2_SPECIAL) {
      tileAttack();
      dusk.getCombat2().script("phase", PHASE_3_INITIAL);
      dawn.getCombat2().script("phase", PHASE_3_INITIAL);
      return true;
    }
    return false;
  }

  private boolean phase3() {
    if (phase == PHASE_3_INITIAL) {
      dusk.setAnimation(7789);
      dusk.setTransformationId(NpcId.DUSK_248);
      dusk.setLock(2);
      dawn.setLock(2);
      dusk.getCombat2().script("phase", PHASE_3);
      dawn.getCombat2().script("phase", PHASE_3);
      dawn.setHitDelay(6);
      sphereTick = 0;
      return true;
    } else if (phase == PHASE_3) {
      if ((npc.getId() == NpcId.DUSK_248 || npc.getId() == NpcId.DUSK_248_7882) && dawn.isDead()) {
        script("phase", PHASE_4_INITIAL_1);
      }
      if (npc.getId() == NpcId.DAWN_228 && npc.getHitDelay() == 0 && sphereTick == 0) {
        sphereTick = 50 + PRandom.randomI(25);
        npc.setAnimation(7776);
        npc.setLock(3);
        npc.setHitDelay(6);
        for (int i = 0; i < 3; i++) {
          var tile = new Tile(BASE_TILE);
          tile.moveX(PRandom.randomI(15));
          tile.moveY(PRandom.randomI(15));
          npc.getController().sendMapProjectile(null, npc, tile, 1437, 43, 1, 51, 5 + 5 * 10, 16,
              64);
          var sphereObject = new MapObject(31678, tile, 10, MapObject.getRandomDirection());
          var event = new PEvent(2) {
            @Override
            public void execute() {
              setTick(0);
              var sphereObject = (MapObject) getAttachment(0);
              if (!sphereObject.isVisible()) {
                stop();
                return;
              }
              var dawn = (Npc) getAttachment(1);
              if (getExecutions() == 0) {
                dawn.getController().addMapObject(sphereObject);
              } else if (getExecutions() == 10) {
                sphereObject.setId(sphereObject.getId() + 1);
                dawn.getController().sendMapObject(sphereObject);
              } else if (getExecutions() == 20) {
                sphereObject.setId(sphereObject.getId() + 1);
                dawn.getController().sendMapObject(sphereObject);
              } else if (getExecutions() == 30) {
                if (!dawn.isLocked()) {
                  dawn.getController().sendMapProjectile(dawn, sphereObject, dawn, 1441, 1, 43, 0,
                      51 + 5 + 5 * 10, 16, 64);
                  npc.adjustHitpoints(90, 0);
                }
                stop();
              }
            }

            @Override
            public void stopHook() {
              var sphereObject = (MapObject) getAttachment(0);
              var dawn = (Npc) getAttachment(1);
              var player = (Player) getAttachment(2);
              if (player != null && player.isVisible()) {
                if (sphereObject.getId() == 31678) {
                  player.getController().sendMapProjectile(player, sphereObject, player, 1437, 1,
                      43, 0, 51 + 5 + 1 * 10, 16, 64);
                  player.addHit(new HitEvent(2, player, new Hit(2)));
                } else if (sphereObject.getId() == 31679) {
                  player.getController().sendMapProjectile(player, sphereObject, player, 1439, 1,
                      43, 0, 51 + 5 + 1 * 10, 16, 64);
                  player.addHit(new HitEvent(2, player, new Hit(10)));
                } else if (sphereObject.getId() == 31680) {
                  player.getController().sendMapProjectile(player, sphereObject, player, 1441, 1,
                      43, 0, 51 + 5 + 1 * 10, 16, 64);
                  player.addHit(new HitEvent(2, player, new Hit(20)));
                }
              }
              dawn.getController().addMapObject(new MapObject(-1, sphereObject));
            }
          };
          npc.getWorld().addEvent(event);
          event.setAttachment(0, sphereObject);
          event.setAttachment(1, dawn);
          sphereObject.setAttachment(event);
        }
      }
    }
    return false;
  }

  private boolean phase4() {
    if (phase == PHASE_4_INITIAL_1) {
      npc.setLock(4);
      npc.getMovement().clear();
      npc.setTransformationId(NpcId.DUSK_328_7888);
      npc.setAnimation(7795);
      script("phase", PHASE_4_INITIAL_2);
      npc.getController().sendMapGraphic(new Tile(npc), 1433, 0, 0);
      npc.getController().sendMapGraphic(new Tile(npc).moveX(3), 1433, 0, 0);
      npc.getController().sendMapGraphic(new Tile(npc).moveY(3), 1433, 0, 0);
      npc.getController().sendMapGraphic(new Tile(npc).moveTile(3, 3), 1433, 0, 0);
      npc.getController().sendMapGraphic(new Tile(dawn), 1432, 0, 0);
      npc.getController().sendMapGraphic(new Tile(dawn).moveX(3), 1432, 0, 0);
      npc.getController().sendMapGraphic(new Tile(dawn).moveY(3), 1432, 0, 0);
      npc.getController().sendMapGraphic(new Tile(dawn).moveTile(3, 3), 1432, 0, 0);
      return true;
    } else if (phase == PHASE_4_INITIAL_2) {
      prisonPhase = PRISON_PHASE_END;
      prisonTick = 8;
      npc.setLock(8);
      npc.getMovement().clear();
      npc.setAnimation(7796);
      script("phase", PHASE_4);
      return true;
    } else if (phase == PHASE_4) {
      if (npc.getHitDelay() == 0 && prisonPhase == PRISON_PHASE_START && prisonTick == 0) {
        prisonPhase = PRISON_PHASE_END;
        prisonTick = 6;
        npc.setLock(6);
        npc.setAnimation(7799);
      } else if (prisonPhase == PRISON_PHASE_END && prisonTick == 0) {
        prisonPhase = PRISON_PHASE_START;
        prisonTick = 50 + PRandom.randomI(25);
        npc.setLock(7);
        npc.setHitDelay(7);
        makePrison();
      }
    }
    return false;
  }

  private void tileAttack() {
    for (var i = 0; i < 7; i++) {
      var tile = new Tile(BASE_TILE);
      tile.moveTile(PRandom.randomI(15), PRandom.randomI(15));
      npc.getController().sendMapGraphic(tile, 1416 + i, 0, i * 30);
      var the = new TileHitEvent(2, npc.getController(), tile, 10, HitType.TYPELESS);
      the.setDuration(8 - i);
      the.setRadius(1);
      npc.getWorld().addEvent(the);
      tile = new Tile(BASE_TILE);
      tile.moveX(PRandom.randomI(15));
      tile.moveY(PRandom.randomI(15));
      npc.getController().sendMapGraphic(tile, 1424 + i, 0, i * 30);
      the = new TileHitEvent(2 + i, npc.getController(), tile, 10, HitType.TYPELESS);
      the.setDuration(8 - i);
      the.setRadius(1);
      npc.getWorld().addEvent(the);
    }
    dusk.setLock(10);
    dawn.setLock(10);
  }

  private void throwPlayers(int minDamage, int maxDamage) {
    for (var player : npc.getController().getPlayers()) {
      if (!npc.withinDistance(player, 1) || player.isLocked()) {
        continue;
      }
      var distance = 4;
      Tile tile;
      int direction;
      if (player.getY() < npc.getY()) {
        tile = new Tile(player.getX(), player.getY() - distance);
        while (!Route.canMove(player, tile) && distance > 0) {
          tile = new Tile(player.getX(), player.getY() - (--distance));
        }
        direction = Tile.NORTH;
      } else {
        tile = new Tile(player.getX(), player.getY() + distance);
        while (!Route.canMove(player, tile) && distance > 0) {
          tile = new Tile(player.getX(), player.getY() + --distance);
        }
        direction = Tile.SOUTH;
      }
      var fm = new ForceMovement(tile, 1, direction);
      player.setLock(4);
      player.setForceMovementTeleport(fm, 734, 1, null);
      player.applyHit(new Hit(minDamage + PRandom.randomI(maxDamage - minDamage)));
      player.getGameEncoder().sendMessage("You have been knocked out!");
    }
  }

  private void makePrison() {
    var tile = new Tile(BASE_TILE);
    tile.moveTile(1 + PRandom.randomI(9), 1 + PRandom.randomI(9));
    var borderTiles = new Tile[] { new Tile(tile), new Tile(tile).moveX(1), new Tile(tile).moveX(2),
        new Tile(tile).moveX(3), new Tile(tile).moveX(4), new Tile(tile).moveTile(4, 1),
        new Tile(tile).moveTile(4, 2), new Tile(tile).moveTile(4, 3), new Tile(tile).moveTile(4, 4),
        new Tile(tile).moveTile(3, 4), new Tile(tile).moveTile(2, 4), new Tile(tile).moveTile(1, 4),
        new Tile(tile).moveY(4), new Tile(tile).moveY(4), new Tile(tile).moveY(3),
        new Tile(tile).moveY(2), new Tile(tile).moveY(1) };
    var openTile = PRandom.arrayRandom(1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15);
    for (var i = 0; i < borderTiles.length; i++) {
      if (i == openTile) {
        continue;
      }
      npc.getController().sendMapGraphic(borderTiles[i], 1434, 0, 106);
      var invisibleBarrier = new MapObject(6900, borderTiles[i], 10, 0);
      npc.getWorld().addEvent(new TempMapObject(3 + 6, npc.getController(), invisibleBarrier));
    }
    var innerTiles = new Tile[] { new Tile(tile).moveTile(1, 1), new Tile(tile).moveTile(2, 1),
        new Tile(tile).moveTile(3, 1), new Tile(tile).moveTile(1, 2), new Tile(tile).moveTile(2, 2),
        new Tile(tile).moveTile(3, 2), new Tile(tile).moveTile(1, 3), new Tile(tile).moveTile(2, 3),
        new Tile(tile).moveTile(3, 3) };
    for (var innerTile : innerTiles) {
      npc.getController().sendMapGraphic(innerTile, 1434, 0, 206);
      var the = new TileHitEvent(6, npc.getController(), innerTile, 60, HitType.TYPELESS);
      the.setFullDamage(true);
      npc.getWorld().addEvent(the);
    }
    tile.moveTile(2, 2);
    for (var player : npc.getController().getPlayers()) {
      if (player.isLocked()) {
        continue;
      }
      var fm = new ForceMovement(tile, 1, Tile.WEST);
      player.setLock(4);
      player.setForceMovementTeleport(fm, 734, 1, null);
      player.setForceMessage("Arghhh!");
    }
  }
}
