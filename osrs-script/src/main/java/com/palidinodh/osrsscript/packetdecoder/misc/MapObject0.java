package com.palidinodh.osrsscript.packetdecoder.misc;

import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.ForceMovement;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.Region;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.map.WildernessObelisk;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.AchievementDiaryTask;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Runecrafting;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.Smithing;
import com.palidinodh.osrscore.model.player.controller.PestControlPC;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PTime;

public class MapObject0 {
  // Rocks
  public static void mapObject154(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2887 && mapObject.getY() == 9823) {
      // Taverley Dungeon
      player.getMovement().ladderUpTeleport(new Tile(2888, 9823, 1));
    }
  }

  // Ship's ladder
  public static void mapObject245(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3017 && mapObject.getY() == 3959) {
      player.getMovement().teleport(3017, 3961, player.getHeight() + 1);
    } else if (mapObject.getX() == 3019 && mapObject.getY() == 3959) {
      player.getMovement().teleport(3019, 3961, player.getHeight() + 1);
    }
  }

  // Ship's ladder
  public static void mapObject246(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3017 && mapObject.getY() == 3959) {
      player.getMovement().teleport(3017, 3958, player.getHeight() - 1);
    } else if (mapObject.getX() == 3019 && mapObject.getY() == 3959) {
      player.getMovement().teleport(3019, 3958, player.getHeight() - 1);
    }
  }

  // Ship's ladder
  public static void mapObject272(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3018 && mapObject.getY() == 3957) {
      player.getMovement().ladderUpTeleport(new Tile(3018, 3956, player.getHeight() + 1));
    }
  }

  // Ship's ladder
  public static void mapObject273(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3018 && mapObject.getY() == 3957) {
      player.getMovement().ladderDownTeleport(new Tile(3018, 3958, player.getHeight() - 1));
    }
  }

  // Port Sarim altar
  public static void mapObject409(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
    player.setAnimation(Prayer.PRAY_ANIMATION);
  }

  // Chaos altar
  public static void mapObject411(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
    player.setAnimation(Prayer.PRAY_ANIMATION);
  }

  // Crevice
  public static void mapObject534(Player player, int index, MapObject mapObject) {
    // Smoke Devil Dungeon
    Tile tile = new Tile(2413, 3059, 0);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Crevice
  public static void mapObject536(Player player, int index, MapObject mapObject) {
    // Stronghold Slayer Cave
    Tile tile = new Tile(2379, 9452, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Crevice
  public static void mapObject537(Player player, int index, MapObject mapObject) {
    player.openDialogue("bossinstance", 12);
  }

  // Crevice
  public static void mapObject538(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(2280, 10016, 0);
    player.getMovement().ladderUpTeleport(tile);
    player.getController().stopWithTeleport();
  }

  // Passage
  public static void mapObject677(Player player, int index, MapObject mapObject) {
    player.getCombat().setDamageInflicted(0);
    if (player.getX() <= 2970) {
      player.getMovement().teleport(2974, 4384, 2);
    } else {
      player.getMovement().teleport(2970, 4384, 2);
    }
    player.getController().stopWithTeleport();
  }

  // Cave
  public static void mapObject678(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (player.getMovement().getTeleportBlock() > 0) {
      player.getGameEncoder()
          .sendMessage("A teleport block has been cast on you. It should wear off in "
              + player.getMovement().getTeleportBlockRemaining() + ".");
      return;
    }
    Tile tile = new Tile(2964, 4382, 2);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Cave exit
  public static void mapObject679(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(3206, 3681, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Shaking box
  public static void mapObject721(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Web
  public static void mapObject733(Player player, int index, MapObject mapObject) {
    if ((player.getEquipment().getWeaponDef().getWeaponType() == null
        || !player.getEquipment().getWeaponDef().getWeaponType().hasSlash())
        && !player.getInventory().hasItem(946)) {
      player.getGameEncoder().sendMessage("Only a sharp blade can cut through this sticky web.");
      return;
    }
    player.setAnimation(player.getCombat().getAttackAnimation());
    if (PRandom.randomE(4) != 0) {
      player.getGameEncoder().sendMessage("You fail to cut through it.");
      return;
    }
    player.getGameEncoder().sendMessage("You slash the web apart.");
    MapObject newWeb = new MapObject(mapObject.getId() + 1, mapObject);
    player.getWorld().addEvent(new TempMapObject(100, player.getController(), newWeb));
  }

  // Dramen tree
  public static void mapObject1292(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(" axe") && !player.getEquipment().hasItem(" axe")) {
      player.getGameEncoder().sendMessage("You need an axe to chop down this tree.");
      return;
    }
    if (player.getWorld().getTargetNPC(1163, player) != null) {
      return;
    }
    Npc treeSpiritNPC =
        new Npc(player.getController(), 1163, new Tile(2860, 9736, player.getHeight()));
    treeSpiritNPC.setForceMessage("You must defeat me before touching the tree!");
    treeSpiritNPC.getCombat().setTarget(player);
  }

  // Spirit tree
  public static void mapObject1293(Player player, int index, MapObject mapObject) {
    player.openDialogue("spirittree", 0);
  }

  // Spirit tree
  public static void mapObject1294(Player player, int index, MapObject mapObject) {
    player.openDialogue("spirittree", 0);
  }

  // Spirit tree
  public static void mapObject1295(Player player, int index, MapObject mapObject) {
    player.openDialogue("spirittree", 0);
  }

  // Trapdoor
  public static void mapObject1579(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3097 && mapObject.getY() == 3468) {
      // Edgeville dungeon
      player.getMovement().ladderUpTeleport(new Tile(3096, 9867));
    }
  }

  // Trapdoor
  public static void mapObject1738(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3008 && mapObject.getY() == 3150) {
      // Asgarnian Ice Dungeon entrance
      Tile tile = new Tile(3007, 9550, 0);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Lever
  public static void mapObject1815(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (mapObject.getX() == 3153 && mapObject.getY() == 3923) {
      // Deep wild lever
      if (player.getMovement().getTeleportBlock() > 0) {
        player.getGameEncoder()
            .sendMessage("A teleport block has been cast on you. It should wear off in "
                + player.getMovement().getTeleportBlockRemaining() + ".");
        return;
      }
      Tile tile = player.getWidgetManager().getHomeTile();
      if (player.getClientHeight() == tile.getHeight()) {
        tile.setHeight(player.getHeight());
      }
      player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
          Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
      player.clearHits();
    }
  }

  // Lever
  public static void mapObject1816(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (mapObject.getX() == 3067 && mapObject.getY() == 10253) {
      // KBD entrance lever
      if (player.getMovement().getTeleportBlock() > 0) {
        player.getGameEncoder()
            .sendMessage("A teleport block has been cast on you. It should wear off in "
                + player.getMovement().getTeleportBlockRemaining() + ".");
        return;
      }
      if (index == 0) {
        Tile tile = new Tile(2271, 4680, 0);
        player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
            Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
        player.clearHits();
      } else if (index == 4) {
        player.openDialogue("bossinstance", 3);
      }
    }
  }

  // Lever
  public static void mapObject1817(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (mapObject.getX() == 2271 && mapObject.getY() == 4680) {
      // KBD exit lever
      Tile tile = new Tile(3067, 10253, 0);
      player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
          Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
      player.getController().stopWithTeleport();
      player.clearHits();
    }
  }

  // Crumbling wall
  public static void mapObject1948(Player player, int index, MapObject mapObject) {
    if (player.getY() != mapObject.getY() || player.getX() > mapObject.getX()) {
      return;
    }
    player.getGameEncoder().sendMessage("You climb the low wall...");
    player.setAnimation(840);
    Tile toTile = new Tile(mapObject.getX() + 1, mapObject.getY(), mapObject.getHeight());
    player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.EAST));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        int xp = 14;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (player.getAttributeInt("agility_stage") == 4) {
          player.putAttribute("agility_stage", 5);
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        } else if (player.getAttributeInt("agility_stage") == 5) {
          player.putAttribute("agility_stage", 6);
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        } else if (player.getAttributeInt("agility_stage") == 6) {
          player.removeAttribute("agility_stage");
          xp = 47;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          // 102 laps an hour
          if (PRandom.randomE(25) == 0) {
            int amount = 4;
            if (player.isPremiumMember()) {
              amount = 6;
            }
            player.getInventory().addOrDropItem(11849, amount);
          }
          if (PRandom.randomE(2) == 0) {
            int rewardType = PRandom.randomE(3);
            if (rewardType == 0) {
              player.getInventory().addOrDropItem(3009, 1);
            } else if (rewardType == 1) {
              player.getInventory().addOrDropItem(3017, 1);
            } else if (rewardType == 2) {
              player.getInventory().addOrDropItem(12640, 4);
            }
          }
          player.getFamiliar().rollSkillPet(Skills.AGILITY, 44376, 20659);
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Anvil
  public static void mapObject2031(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(Smithing.BRONZE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.BRONZE_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.IRON_BAR_ID)) {
      Smithing.openSmith(player, Smithing.IRON_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.STEEL_BAR_ID)) {
      Smithing.openSmith(player, Smithing.STEEL_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.MITHRIL_BAR_ID)) {
      Smithing.openSmith(player, Smithing.MITHRIL_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.ADAMANT_BAR_ID)) {
      Smithing.openSmith(player, Smithing.ADAMANT_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.RUNE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.RUNE_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.BLURITE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.BLURITE_BAR_ID);
    }
  }

  // Anvil
  public static void mapObject2097(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(Smithing.BRONZE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.BRONZE_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.IRON_BAR_ID)) {
      Smithing.openSmith(player, Smithing.IRON_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.STEEL_BAR_ID)) {
      Smithing.openSmith(player, Smithing.STEEL_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.MITHRIL_BAR_ID)) {
      Smithing.openSmith(player, Smithing.MITHRIL_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.ADAMANT_BAR_ID)) {
      Smithing.openSmith(player, Smithing.ADAMANT_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.RUNE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.RUNE_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.BLURITE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.BLURITE_BAR_ID);
    }
  }

  // Door
  public static void mapObject2102(Player player, int index, MapObject mapObject) {
    // Slayer Tower bloodvelds
    if (player.getY() >= 3556) {
      player.getMovement().teleport(player.getX(), 3555, 1);
    } else {
      player.getMovement().teleport(player.getX(), 3556, 1);
    }
  }

  // Door
  public static void mapObject2104(Player player, int index, MapObject mapObject) {
    // Slayer Tower bloodvelds
    if (player.getY() >= 3556) {
      player.getMovement().teleport(player.getX(), 3555, 1);
    } else {
      player.getMovement().teleport(player.getX(), 3556, 1);
    }
  }

  // Staircase
  public static void mapObject2114(Player player, int index, MapObject mapObject) {
    // Slayer Tower
    Tile tile = new Tile(3433, 3537, 1);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Staircase
  public static void mapObject2118(Player player, int index, MapObject mapObject) {
    // Slayer Tower
    Tile tile = new Tile(3438, 3537, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Staircase
  public static void mapObject2119(Player player, int index, MapObject mapObject) {
    // Slayer Tower
    Tile tile = new Tile(3417, 3540, 2);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Staircase
  public static void mapObject2120(Player player, int index, MapObject mapObject) {
    // Slayer Tower
    Tile tile = new Tile(3412, 3540, 1);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Loose Railing
  public static void mapObject2186(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2515 && mapObject.getY() == 3161) {
      if (player.getY() >= 3161) {
        player.getMovement().teleport(2503, 3191);
      } else {
        player.getMovement().teleport(2515, 3161);
      }
    }
  }

  // Gate
  public static void mapObject2623(Player player, int index, MapObject mapObject) {
    // Taverley Dungeon blue dragons door
    if (player.getX() >= 2924) {
      player.getMovement().teleport(2923, player.getY(), 0);
    } else {
      player.getMovement().teleport(2924, player.getY(), 0);
    }
  }

  // (Heroes Guild) Door
  public static void mapObject2624(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    }
    if (!player.getCombat().getDragonSlayer()) {
      player.getGameEncoder().sendMessage("You need to complete Dragon Slayer to enter.");
      return;
    } else if (!player.getCombat().getLostCity()) {
      player.getGameEncoder().sendMessage("You need to complete Lost City to enter.");
      return;
    }
    player.getMovement().clear();
    if (player.getX() >= 2902) {
      player.getMovement().addMovement(player.getX() - 1, player.getY());
    } else {
      player.getMovement().addMovement(player.getX() + 1, player.getY());
    }
    Region.openDoors(player, mapObject, 1, false);
  }

  // (Heroes Guild) Door
  public static void mapObject2625(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    }
    if (!player.getCombat().getDragonSlayer()) {
      player.getGameEncoder().sendMessage("You need to complete Dragon Slayer to enter.");
      return;
    } else if (!player.getCombat().getLostCity()) {
      player.getGameEncoder().sendMessage("You need to complete Lost City to enter.");
      return;
    }
    player.getMovement().clear();
    if (player.getX() >= 2902) {
      player.getMovement().addMovement(player.getX() - 1, player.getY());
    } else {
      player.getMovement().addMovement(player.getX() + 1, player.getY());
    }
    Region.openDoors(player, mapObject, 1, false);
  }

  // Legends Guild door
  public static void mapObject2896(Player player, int index, MapObject mapObject) {
    Region.openDoors(player, mapObject);
  }

  // Legends Guild door
  public static void mapObject2897(Player player, int index, MapObject mapObject) {
    Region.openDoors(player, mapObject);
  }

  // Fire Wall
  public static void mapObject2908(Player player, int index, MapObject mapObject) {
    if (!player.carryingItem(730)) {
      player.getGameEncoder().sendMessage("You need a Binding book to enter.");
      return;
    }
    player.getMovement().teleport(2792, 9328, 0);
  }

  // Fire Wall
  public static void mapObject2909(Player player, int index, MapObject mapObject) {
    if (!player.carryingItem(730)) {
      player.getGameEncoder().sendMessage("You need a Binding book to enter.");
      return;
    }
    player.getMovement().teleport(2792, 9328, 0);
  }

  // Bookcase
  public static void mapObject2911(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 50) {
      player.getGameEncoder().sendMessage("You need an Agility level of 50 to do this.");
      return;
    }
    player.getMovement().ladderDownTeleport(new Tile(2800, 9340, 0));
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Ancient Gate
  public static void mapObject2912(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    }
    if (player.getY() >= 9332) {
      player.getGameEncoder().sendMessage("You attempt to pick the lock.");
      if (!player.getInventory().hasItem(1523)) {
        player.getGameEncoder().sendMessage("You need a lockpick for this lock.");
        return;
      } else if (PRandom.randomE(4) != 0) {
        player.getGameEncoder().sendMessage("You fail to pick the lock.");
        return;
      }
      player.getGameEncoder().sendMessage("You manage to pick the lock.");
      player.getMovement().clear();
      player.getMovement().addMovement(player.getX(), player.getY() - 1);
      Region.openDoors(player, mapObject, 1, false);
    } else {
      player.getMovement().clear();
      player.getMovement().addMovement(player.getX(), player.getY() + 1);
      Region.openDoors(player, mapObject, 1, false);
    }
  }

  // Ancient Gate
  public static void mapObject2913(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    }
    if (player.getY() >= 9332) {
      player.getGameEncoder().sendMessage("You attempt to pick the lock.");
      if (!player.getInventory().hasItem(1523)) {
        player.getGameEncoder().sendMessage("You need a lockpick for this lock.");
        return;
      } else if (PRandom.randomE(4) != 0) {
        player.getGameEncoder().sendMessage("You fail to pick the lock.");
        return;
      }
      player.getGameEncoder().sendMessage("You manage to pick the lock.");
      player.getMovement().clear();
      player.getMovement().addMovement(player.getX(), player.getY() - 1);
      Region.openDoors(player, mapObject, 1, false);
    } else {
      player.getMovement().clear();
      player.getMovement().addMovement(player.getX(), player.getY() + 1);
      Region.openDoors(player, mapObject, 1, false);
    }
  }

  // Crevice
  public static void mapObject2918(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(2795, 9338, 0));
  }

  // Boulder
  public static void mapObject2919(Player player, int index, MapObject mapObject) {
    if (player.getY() >= 9329) {
      player.getMovement().teleport(player.getX(), 9326, 0);
    } else {
      player.getMovement().teleport(player.getX(), 9329, 0);
    }
  }

  // Boulder
  public static void mapObject2920(Player player, int index, MapObject mapObject) {
    if (player.getY() >= 9325) {
      player.getMovement().teleport(player.getX(), 9322, 0);
    } else {
      player.getMovement().teleport(player.getX(), 9325, 0);
    }
  }

  // Boulder
  public static void mapObject2921(Player player, int index, MapObject mapObject) {
    if (player.getY() >= 9321) {
      player.getMovement().teleport(player.getX(), 9318, 0);
    } else {
      player.getMovement().teleport(player.getX(), 9321, 0);
    }
  }

  // Ancient Gate
  public static void mapObject2922(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    } else if (player.getSkills().getLevel(Skills.STRENGTH) < 50) {
      player.getGameEncoder().sendMessage("You need a Strength level of 50 to do this.");
      return;
    }
    player.getMovement().clear();
    if (player.getY() >= 9314) {
      player.getMovement().addMovement(player.getX(), player.getY() - 1);
    } else {
      player.getMovement().addMovement(player.getX(), player.getY() + 1);
    }
    Region.openDoors(player, mapObject, 1, false);
  }

  // Ancient Gate
  public static void mapObject2923(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    } else if (player.getSkills().getLevel(Skills.STRENGTH) < 50) {
      player.getGameEncoder().sendMessage("You need a Strength level of 50 to do this.");
      return;
    }
    player.getMovement().clear();
    if (player.getY() >= 9314) {
      player.getMovement().addMovement(player.getX(), player.getY() - 1);
    } else {
      player.getMovement().addMovement(player.getX(), player.getY() + 1);
    }
    Region.openDoors(player, mapObject, 1, false);
  }

  // Jagged wall
  public static void mapObject2926(Player player, int index, MapObject mapObject) {
    if (player.getX() == 2790 && player.getY() == 9295) {
      player.getMovement().teleport(2789, 9296, 0);
    } else if (player.getX() == 2789 && player.getY() == 9296) {
      player.getMovement().teleport(2790, 9295, 0);
    }
  }

  // Marked wall
  public static void mapObject2927(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2779 && mapObject.getY() == 9305) {
      if (player.getCombat().getLegendsQuest() == 0) {
        if (player.getInventory().getId(0) != 566 || player.getInventory().getId(1) != 558
            || player.getInventory().getId(2) != 557 || player.getInventory().getId(3) != 563
            || player.getInventory().getAmount(3) < 2) {
          player.getGameEncoder().sendMessage("You need to carry the SMELL runes to pass.");
          return;
        }
        player.getInventory().deleteItem(566, 1);
        player.getInventory().deleteItem(558, 1);
        player.getInventory().deleteItem(557, 1);
        player.getInventory().deleteItem(563, 2);
      }
      player.getController().addMapItem(new Item(730, 1), new Tile(2775, 9290, 0), player);
      player.getGameEncoder()
          .sendMessage("You appear in a large cavern like room filled with pools of water.");
      player.getMovement().teleport(2773, 9301, 0);
    } else if (mapObject.getX() == 2776 && mapObject.getY() == 9303) {
      player.getMovement().teleport(2780, 9307, 0);
    }
  }

  // Ancient Gate
  public static void mapObject2930(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getLegendsQuest() == 0) {
      player.getGameEncoder().sendMessage("I don't think I should go in there...");
      return;
    }
    player.getMovement().teleport(2762, 9320, 0);
  }

  // Winch
  public static void mapObject2934(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("I need to find a way to climb down.");
  }

  // Rocky Ledge
  public static void mapObject2959(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2377 && mapObject.getY() == 4717) {
      if (player.getX() != 2378 || player.getY() != 4717) {
        return;
      }
      player.setAnimation(840);
      Tile toTile = new Tile(2376, 4717, 0);
      player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.WEST));
      player.lock();
      player.getGameEncoder().sendMessage("You start to climb the precarious rocks.");
      PEvent event = new PEvent() {
        @Override
        public void execute() {
          super.stop();
          if (!player.isVisible()) {
            return;
          }
          player.unlock();
          player.getMovement().teleport(toTile);
          player.getGameEncoder()
              .sendMessage("You climb confidently over the rocks and hold your balance well.");
        }
      };
      player.getWorld().addEvent(event);
    }
  }

  // Rocky Ledge
  public static void mapObject2960(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2377 && mapObject.getY() == 4728) {
      if (player.getX() != 2377 || player.getY() != 4727) {
        return;
      }
      player.setAnimation(840);
      Tile toTile = new Tile(2378, 4729, 0);
      player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.NORTH));
      player.lock();
      player.getGameEncoder().sendMessage("You start to climb the precarious rocks.");
      PEvent event = new PEvent() {
        @Override
        public void execute() {
          super.stop();
          if (!player.isVisible()) {
            return;
          }
          player.unlock();
          player.getMovement().teleport(toTile);
          player.getGameEncoder()
              .sendMessage("You climb confidently over the rocks and hold your balance well.");
        }
      };
      player.getWorld().addEvent(event);
    }
  }

  // Rocky Ledge
  public static void mapObject2961(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2382 && mapObject.getY() == 4729) {
      if (player.getX() != 2382 || player.getY() != 4730) {
        return;
      }
      player.setAnimation(840);
      Tile toTile = new Tile(2382, 4728, 0);
      player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.SOUTH));
      player.lock();
      player.getGameEncoder().sendMessage("You start to climb the precarious rocks.");
      PEvent event = new PEvent() {
        @Override
        public void execute() {
          super.stop();
          if (!player.isVisible()) {
            return;
          }
          player.unlock();
          player.getMovement().teleport(toTile);
          player.getGameEncoder()
              .sendMessage("You climb confidently over the rocks and hold your balance well.");
        }
      };
      player.getWorld().addEvent(event);
    }
  }

  // Rocks
  public static void mapObject2962(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2387 && mapObject.getY() == 4728) {
      if (player.getX() != 2387 || player.getY() != 4727) {
        return;
      }
      player.setAnimation(840);
      Tile toTile = new Tile(2388, 4729, 0);
      player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.NORTH));
      player.lock();
      player.getGameEncoder().sendMessage("You start to climb the precarious rocks.");
      PEvent event = new PEvent() {
        @Override
        public void execute() {
          super.stop();
          if (!player.isVisible()) {
            return;
          }
          player.unlock();
          player.getMovement().teleport(toTile);
          player.getGameEncoder()
              .sendMessage("You climb confidently over the rocks and hold your balance well.");
        }
      };
      player.getWorld().addEvent(event);
    }
  }

  // Rocks
  public static void mapObject2963(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2390 && mapObject.getY() == 4724) {
      if (player.getX() != 2390 || player.getY() != 4725) {
        return;
      }
      player.setAnimation(840);
      Tile toTile = new Tile(2390, 4723, 0);
      player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.SOUTH));
      player.lock();
      player.getGameEncoder().sendMessage("You start to climb the precarious rocks.");
      PEvent event = new PEvent() {
        @Override
        public void execute() {
          super.stop();
          if (!player.isVisible()) {
            return;
          }
          player.unlock();
          player.getMovement().teleport(toTile);
          player.getGameEncoder()
              .sendMessage("You climb confidently over the rocks and hold your balance well.");
        }
      };
      player.getWorld().addEvent(event);
    }
  }

  // Rocks
  public static void mapObject2964(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2390 && mapObject.getY() == 4718) {
      if (player.getX() != 2390 || player.getY() != 4719) {
        return;
      }
      player.setAnimation(840);
      Tile toTile = new Tile(2390, 4717, 0);
      player.setForceMovement(new ForceMovement(player, 0, toTile, 2, Tile.SOUTH));
      player.lock();
      player.getGameEncoder().sendMessage("You start to climb the precarious rocks.");
      PEvent event = new PEvent() {
        @Override
        public void execute() {
          super.stop();
          if (!player.isVisible()) {
            return;
          }
          player.unlock();
          player.getMovement().teleport(toTile);
          player.getGameEncoder()
              .sendMessage("You climb confidently over the rocks and hold your balance well.");
        }
      };
      player.getWorld().addEvent(event);
    }
  }

  // Mossy rock
  public static void mapObject2965(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("These rocks look somehow manufactured.");
  }

  // Furnace
  public static void mapObject2966(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("Perhaps I should use this to forge something...");
  }

  // Recess
  public static void mapObject2969(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getLegendsQuest() >= 2) {
      player.getGameEncoder().sendMessage("The wall appears to glow from the crystal.");
      return;
    } else if (player.getInventory().getCount(745) < 1) {
      player.getGameEncoder()
          .sendMessage("It looks like something needs to be placed into the wall.");
      return;
    }
    player.getCombat().setLegendsQuest(2);
    player.getInventory().deleteItem(745, 1);
    player.getGameEncoder().sendMessage("You carefully place the crystal into the depression.");
  }

  // Shimmering field
  public static void mapObject2971(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getLegendsQuest() < 2) {
      player.getGameEncoder()
          .sendMessage("You try to pass through the field, but something prevents you.");
      return;
    }
    if (player.getY() > mapObject.getY()) {
      player.getMovement().teleport(player.getX(), mapObject.getY());
    } else {
      player.getMovement().teleport(player.getX(), mapObject.getY() + 1);
    }
  }

  // Open chest
  public static void mapObject3194(Player player, int index, MapObject mapObject) {
    player.getBank().open();
  }

  // Cave entrance
  public static void mapObject3650(Player player, int index, MapObject mapObject) {
    // Mos Le'Harmless Cave
    Tile tile = new Tile(3748, 9373, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Rope
  public static void mapObject3832(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3508 && mapObject.getY() == 9494) {
      player.getMovement().ladderUpTeleport(new Tile(3509, 9496, 2));
      player.getController().stopWithTeleport();
    }
  }

  // Furnace
  public static void mapObject4304(Player player, int index, MapObject mapObject) {
    Smithing.openSmelt(player);
  }

  // Anvil
  public static void mapObject4306(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(Smithing.BRONZE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.BRONZE_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.IRON_BAR_ID)) {
      Smithing.openSmith(player, Smithing.IRON_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.STEEL_BAR_ID)) {
      Smithing.openSmith(player, Smithing.STEEL_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.MITHRIL_BAR_ID)) {
      Smithing.openSmith(player, Smithing.MITHRIL_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.ADAMANT_BAR_ID)) {
      Smithing.openSmith(player, Smithing.ADAMANT_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.RUNE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.RUNE_BAR_ID);
    } else if (player.getInventory().hasItem(Smithing.BLURITE_BAR_ID)) {
      Smithing.openSmith(player, Smithing.BLURITE_BAR_ID);
    }
  }

  // Iron ladder
  public static void mapObject4413(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(2515, 4629, player.getHeight()));
  }

  // Iron ladder
  public static void mapObject4485(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(2515, 4632, player.getHeight()));
  }

  // Cart tunnel
  public static void mapObject4913(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3440 && mapObject.getY() == 3232) {
      player.getMovement().ladderDownTeleport(new Tile(3436, 9637));
    }
  }

  // Cart tunnel
  public static void mapObject4914(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3430 && mapObject.getY() == 3233) {
      player.getMovement().ladderDownTeleport(new Tile(3405, 9631));
    }
  }

  // Cart tunnel
  public static void mapObject4915(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3429 && mapObject.getY() == 3225) {
      player.getMovement().ladderDownTeleport(new Tile(3409, 9623));
    }
  }

  // Mine cart
  public static void mapObject4918(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(player.getX() >= 3446 ? 3444 : 3446, 3236);
  }

  // Cart tunnel
  public static void mapObject4920(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3437 && mapObject.getY() == 9637) {
      player.getMovement().ladderUpTeleport(new Tile(3441, 3232));
    }
  }

  // Cart tunnel
  public static void mapObject4921(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3404 && mapObject.getY() == 9631) {
      player.getMovement().ladderUpTeleport(new Tile(3429, 3233));
    }
  }

  // Water Valve
  public static void mapObject4924(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("This valve seems to already be open.");
  }

  // Crystal outcrop x:2792, y: 4429
  public static void mapObject4926(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(1755)) {
      player.getGameEncoder().sendMessage("You need a chisel to do this.");
      return;
    } else if (player.getSkills().getLevel(Skills.CRAFTING) < 35) {
      player.getGameEncoder().sendMessage("You need a Crafting level of 35 to cut this.");
      return;
    }
    player.getGameEncoder().sendMessage("You cut a shard from the crystal.");
    player.getInventory().addItem(4082, 1);
    if (player.getCombat().getHauntedMine() == 3) {
      player.getCombat().setHauntedMine(4);
      player.getGameEncoder().sendMessage("<col=ff0000>You have completed Haunted Mine!");
      player.getInventory().addOrDropItem(ItemId.COINS, 50000);
    }
  }

  // Crystal outcrop x:2792, y: 4429
  public static void mapObject4927(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(1755)) {
      player.getGameEncoder().sendMessage("You need a chisel to do this.");
      return;
    } else if (player.getSkills().getLevel(Skills.CRAFTING) < 35) {
      player.getGameEncoder().sendMessage("You need a Crafting level of 35 to cut this.");
      return;
    }
    player.getGameEncoder().sendMessage("You cut a shard from the crystal.");
    player.getInventory().addItem(4082, 1);
    if (player.getCombat().getHauntedMine() == 3) {
      player.getCombat().setHauntedMine(4);
      player.getGameEncoder().sendMessage("<col=ff0000>You have completed Haunted Mine!");
      player.getInventory().addOrDropItem(ItemId.COINS, 50000);
    }
  }

  // Crystal outcrop x:2792, y: 4429
  public static void mapObject4928(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(1755)) {
      player.getGameEncoder().sendMessage("You need a chisel to do this.");
      return;
    } else if (player.getSkills().getLevel(Skills.CRAFTING) < 35) {
      player.getGameEncoder().sendMessage("You need a Crafting level of 35 to cut this.");
      return;
    }
    player.getGameEncoder().sendMessage("You cut a shard from the crystal.");
    player.getInventory().addItem(4082, 1);
    if (player.getCombat().getHauntedMine() == 3) {
      player.getCombat().setHauntedMine(4);
      player.getGameEncoder().sendMessage("<col=ff0000>You have completed Haunted Mine!");
      player.getInventory().addOrDropItem(ItemId.COINS, 50000);
    }
  }

  // Glowing fungus
  public static void mapObject4932(Player player, int index, MapObject mapObject) {
    player.getInventory().addItem(4075, 1);
    player.getGameEncoder()
        .sendMessage("You pull the fungus from the water, it is very cold to the touch.");
  }

  // Glowing fungus
  public static void mapObject4933(Player player, int index, MapObject mapObject) {
    player.getInventory().addItem(4075, 1);
    player.getGameEncoder()
        .sendMessage("You pull the fungus from the water, it is very cold to the touch.");
  }

  // Lift
  public static void mapObject4937(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getHauntedMine() < 2 || !player.getInventory().hasItem(4075)) {
      player.getGameEncoder()
          .sendMessage("You should take a glowing fungus with you before going down.");
      return;
    }
    player.getMovement().teleport(2725, 4452);
  }

  // Lift
  public static void mapObject4938(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getHauntedMine() < 2 || !player.getInventory().hasItem(4075)) {
      player.getGameEncoder()
          .sendMessage("You should take a glowing fungus with you before going down.");
      return;
    }
    player.getMovement().teleport(2725, 4452);
  }

  // Lift
  public static void mapObject4940(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getHauntedMine() < 2 || !player.getInventory().hasItem(4075)) {
      player.getGameEncoder()
          .sendMessage("You should take a glowing fungus with you before going down.");
      return;
    }
    player.getMovement().teleport(2725, 4452);
  }

  // Points settings
  public static void mapObject4949(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("Everything looks good here.");
  }

  // Lever
  public static void mapObject4950(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4951(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4952(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4953(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4954(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4955(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4956(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // Lever
  public static void mapObject4957(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("There is no reason to do that.");
  }

  // (Haunted Mine) Door
  public static void mapObject4962(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2799 && mapObject.getY() == 4453) {
      if (mapObject.isBusy() || player.getY() != mapObject.getY()) {
        return;
      }
      player.getMovement().clear();
      if (player.getX() == mapObject.getX()) {
        player.getMovement().addMovement(mapObject.getX() - 1, mapObject.getY());
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
      Region.openDoor(player, mapObject, 1, false, false);
    }
  }

  // (Haunted Mine) Large door
  public static void mapObject4963(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2773 && mapObject.getY() == 4450) {
      if (mapObject.getOriginal() != null || mapObject.getAttachment() != null
          || player.getX() != mapObject.getX()) {
        player.getGameEncoder().sendMessage("There was an issue opening this door.");
        return;
      }
      player.getMovement().clear();
      if (player.getY() == mapObject.getY()) {
        if (!player.getInventory().hasItem(4077)) {
          player.getGameEncoder().sendMessage("You need a key to open these doors.");
          return;
        }
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY() - 1);
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
      Region.openDoors(player, mapObject, 1, false);
    }
  }

  // (Haunted Mine) Large door
  public static void mapObject4964(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2772 && mapObject.getY() == 4450) {
      if (mapObject.getOriginal() != null || mapObject.getAttachment() != null
          || player.getX() != mapObject.getX()) {
        player.getGameEncoder().sendMessage("There was an issue opening this door.");
        return;
      }
      player.getMovement().clear();
      if (player.getY() == mapObject.getY()) {
        if (!player.getInventory().hasItem(4077)) {
          player.getGameEncoder().sendMessage("You need a key to open these doors.");
          return;
        }
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY() - 1);
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
      Region.openDoors(player, mapObject, 1, false);
    }
  }

  // Ladder
  public static void mapObject4965(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3422 && mapObject.getY() == 9625) {
      player.getMovement().ladderDownTeleport(new Tile(2782, 4568));
    } else if (mapObject.getX() == 3413 && mapObject.getY() == 9633) {
      player.getMovement().ladderDownTeleport(new Tile(2773, 4576));
    }
  }

  // Ladder
  public static void mapObject4966(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2782 && mapObject.getY() == 4569) {
      player.getMovement().ladderUpTeleport(new Tile(3422, 9624));
    } else if (mapObject.getX() == 2773 && mapObject.getY() == 4577) {
      player.getMovement().ladderUpTeleport(new Tile(3412, 9633));
    }
  }

  // Ladder
  public static void mapObject4967(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2725 && mapObject.getY() == 4486) {
      player.getMovement().ladderDownTeleport(new Tile(2789, 4487));
    } else if (mapObject.getX() == 2710 && mapObject.getY() == 4540) {
      player.getMovement().ladderDownTeleport(new Tile(2775, 4540));
    } else if (mapObject.getX() == 2732 && mapObject.getY() == 4529) {
      player.getMovement().ladderDownTeleport(new Tile(2797, 4529));
    }
  }

  // Ladder
  public static void mapObject4968(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2789 && mapObject.getY() == 4486) {
      player.getMovement().ladderUpTeleport(new Tile(2725, 4487));
    } else if (mapObject.getX() == 2774 && mapObject.getY() == 4540) {
      player.getMovement().ladderUpTeleport(new Tile(2711, 4540));
    } else if (mapObject.getX() == 2796 && mapObject.getY() == 4529) {
      player.getMovement().ladderUpTeleport(new Tile(2732, 4528));
    }
  }

  // Ladder
  public static void mapObject4969(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2798 && mapObject.getY() == 4567) {
      player.getMovement().ladderDownTeleport(new Tile(2734, 4504));
    } else if (mapObject.getX() == 2797 && mapObject.getY() == 4599) {
      player.getMovement().ladderDownTeleport(new Tile(2734, 4535));
    }
  }

  // Ladder
  public static void mapObject4970(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2734 && mapObject.getY() == 4503) {
      player.getMovement().ladderUpTeleport(new Tile(2798, 4568));
    } else if (mapObject.getX() == 2733 && mapObject.getY() == 4535) {
      player.getMovement().ladderUpTeleport(new Tile(2798, 4599));
    }
  }

  // Stairs
  public static void mapObject4971(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2746 && mapObject.getY() == 4436) {
      player.getMovement().ladderDownTeleport(new Tile(2811, 4453));
    } else if (mapObject.getX() == 2692 && mapObject.getY() == 4436) {
      player.getMovement().ladderDownTeleport(new Tile(2758, 4453));
    }
  }

  // Stairs
  public static void mapObject4973(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2812 && mapObject.getY() == 4452) {
      player.getMovement().ladderUpTeleport(new Tile(2750, 4436));
    } else if (mapObject.getX() == 2755 && mapObject.getY() == 4452) {
      player.getMovement().ladderUpTeleport(new Tile(2691, 4438));
    }
  }

  // Mine cart
  public static void mapObject4974(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2778 && mapObject.getY() == 4506) {
      if (player.getCombat().getHauntedMine() == 0) {
        player.getGameEncoder().sendMessage("This cart looks like it could carry something.");
      } else {
        player.getGameEncoder()
            .sendMessage("You have already pushed a mine cart deeper into the mine.");
      }
    } else if (mapObject.getX() == 2774 && mapObject.getY() == 4537) {
      if (player.getCombat().getHauntedMine() >= 1) {
        player.getInventory().addItem(4075, 1);
        player.getGameEncoder().sendMessage("You remove the glowing fungus from the mine cart.");
      }
      if (player.getCombat().getHauntedMine() == 1) {
        player.getCombat().setHauntedMine(2);
      }
    }
  }

  // Cave
  public static void mapObject5553(Player player, int index, MapObject mapObject) {
    // Mos Le'Harmless Cave
    Tile tile = new Tile(3748, 9373, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Lever
  public static void mapObject5959(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (mapObject.getX() == 3090 && mapObject.getY() == 3956) {
      // Mage Arena to bank
      if (player.getMovement().getTeleportBlock() > 0) {
        player.getGameEncoder()
            .sendMessage("A teleport block has been cast on you. It should wear off in "
                + player.getMovement().getTeleportBlockRemaining() + ".");
        return;
      }
      Tile tile = new Tile(2539, 4712, 0);
      player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
          Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
      player.clearHits();
    }
  }

  // Lever
  public static void mapObject5960(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (mapObject.getX() == 2539 && mapObject.getY() == 4712) {
      // Mage Arena bank to outside
      Tile tile = new Tile(3090, 3956, 0);
      player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
          Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
      player.clearHits();
    }
  }

  // Smokey well
  public static void mapObject6279(Player player, int index, MapObject mapObject) {
    // Smoke Dungeon entrance
    Tile tile = new Tile(3205, 9378, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Rope
  public static void mapObject6439(Player player, int index, MapObject mapObject) {
    // Smoke Dungeon exit
    Tile tile = new Tile(3310, 2961, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Poll booth
  public static void mapObject8720(Player player, int index, MapObject mapObject) {
    player.openDialogue("vote", 0);
  }

  // Steps
  public static void mapObject8729(Player player, int index, MapObject mapObject) {
    // Asgarnian Ice Dungeon
    if (player.getY() >= 9557) {
      Tile tile = new Tile(3060, 9555, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(3060, 9557, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Net trap
  public static void mapObject8731(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Young tree
  public static void mapObject8732(Player player, int index, MapObject mapObject) {
    player.getHunter().layTrap(-1, mapObject);
  }

  // Net trap
  public static void mapObject8734(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // ladder
  public static void mapObject8744(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(player).setHeight(player.getHeight() + 1));
  }

  // ladder
  public static void mapObject8745(Player player, int index, MapObject mapObject) {
    player.openDialogue("climb", 0);
  }

  // ladder
  public static void mapObject8746(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(player).setHeight(player.getHeight() - 1));
  }

  // Lletya altar
  public static void mapObject8749(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
    player.setAnimation(Prayer.PRAY_ANIMATION);
  }

  // Cave entrance
  public static void mapObject8929(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon entrance
    Tile tile = new Tile(2443, 10146, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Door
  public static void mapObject8958(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon top
    if (player.getX() <= 2490) {
      Tile tile = new Tile(player.getX() + 2, player.getY(), 0);
      player.getMovement().teleport(tile);
    } else {
      Tile tile = new Tile(player.getX() - 2, player.getY(), 0);
      player.getMovement().teleport(tile);
    }
  }

  // Door
  public static void mapObject8959(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon middle
    if (player.getX() <= 2490) {
      Tile tile = new Tile(player.getX() + 2, player.getY(), 0);
      player.getMovement().teleport(tile);
    } else {
      Tile tile = new Tile(player.getX() - 2, player.getY(), 0);
      player.getMovement().teleport(tile);
    }
  }

  // Door
  public static void mapObject8960(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon bottom
    if (player.getX() <= 2490) {
      Tile tile = new Tile(player.getX() + 2, player.getY(), 0);
      player.getMovement().teleport(tile);
    } else {
      Tile tile = new Tile(player.getX() - 2, player.getY(), 0);
      player.getMovement().teleport(tile);
    }
  }

  // Steps
  public static void mapObject8966(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon exit
    Tile tile = new Tile(2524, 3739, 0);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Net trap
  public static void mapObject8973(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Net trap
  public static void mapObject8986(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Net trap
  public static void mapObject8988(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Young tree
  public static void mapObject8989(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Young tree
  public static void mapObject8990(Player player, int index, MapObject mapObject) {
    player.getHunter().layTrap(-1, mapObject);
  }

  // Net trap
  public static void mapObject8992(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Net trap
  public static void mapObject8996(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Net trap
  public static void mapObject8998(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Young tree
  public static void mapObject8999(Player player, int index, MapObject mapObject) {
    player.getHunter().layTrap(-1, mapObject);
  }

  // Young tree
  public static void mapObject9000(Player player, int index, MapObject mapObject) {
    player.getHunter().layTrap(-1, mapObject);
  }

  // Net trap
  public static void mapObject9002(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Net trap
  public static void mapObject9004(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Young tree
  public static void mapObject9341(Player player, int index, MapObject mapObject) {
    player.getHunter().layTrap(-1, mapObject);
  }

  // Net trap
  public static void mapObject9343(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Bird snare
  public static void mapObject9344(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Bird snare
  public static void mapObject9345(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Bird snare
  public static void mapObject9348(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Private portal
  public static void mapObject9370(Player player, int index, MapObject mapObject) {
    player.openDialogue("bossinstance", 1);
  }

  // Bird snare
  public static void mapObject9373(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Bird snare
  public static void mapObject9375(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Bird snare
  public static void mapObject9377(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Bird snare
  public static void mapObject9379(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Box trap
  public static void mapObject9380(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Shaking box
  public static void mapObject9382(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Shaking box
  public static void mapObject9383(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Shaking box
  public static void mapObject9384(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Box trap
  public static void mapObject9385(Player player, int index, MapObject mapObject) {
    player.getHunter().pickupTrap(mapObject);
  }

  // Ladder
  public static void mapObject9558(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(player).setHeight(1));
  }

  // Ladder
  public static void mapObject9559(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(player).setHeight(0));
  }

  // Lever
  public static void mapObject9706(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    // Mage Arena
    if (player.getMovement().getTeleportBlock() > 0) {
      player.getGameEncoder()
          .sendMessage("A teleport block has been cast on you. It should wear off in "
              + player.getMovement().getTeleportBlockRemaining() + ".");
      return;
    }
    Tile tile = new Tile(3105, 3951, 0);
    player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
        Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
  }

  // Lever
  public static void mapObject9707(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    // Mage Arena
    if (player.getMovement().getTeleportBlock() > 0) {
      player.getGameEncoder()
          .sendMessage("A teleport block has been cast on you. It should wear off in "
              + player.getMovement().getTeleportBlockRemaining() + ".");
      return;
    }
    Tile tile = new Tile(3105, 3956, 0);
    player.getMovement().animatedTeleport(tile, 2140, Magic.NORMAL_MAGIC_ANIMATION_START,
        Magic.NORMAL_MAGIC_ANIMATION_END, null, Magic.NORMAL_MAGIC_GRAPHIC, null, 1, 2);
  }

  // Ladder
  public static void mapObject9742(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(2834, 3542, 0);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Ladder
  public static void mapObject10042(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(2907, 9968, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Door
  public static void mapObject10043(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2911 && mapObject.getY() == 9968) {
      if (player.getX() >= 2912) {
        player.getMovement().teleport(2911, 9968, player.getHeight());
      } else {
        if (player.getInventory().getCount(8851) < 100
            && !player.getEquipment().wearingAccomplishmentCape(Skills.ATTACK)) {
          player.getGameEncoder().sendMessage("You need atleast 100 tokens to enter.");
          return;
        } else if (!player.carryingItem(8850)) {
          player.getGameEncoder().sendMessage("You need a Rune defender to enter.");
          return;
        }
        player.getMovement().teleport(2912, 9968, player.getHeight());
      }
    }
  }

  // Grand Exchange booth
  public static void mapObject10060(Player player, int index, MapObject mapObject) {
    player.getBank().open();
  }

  // Grand Exchange booth
  public static void mapObject10061(Player player, int index, MapObject mapObject) {
    if (player.getHeight() != 0) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.openDialogue("grandexchange", 0);
  }

  // Sacrificial boat
  public static void mapObject10068(Player player, int index, MapObject mapObject) {
    player.openDialogue("zulrah", 0);
  }

  // Iron ladder
  public static void mapObject10177(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon
    player.openDialogue("bossinstance", 2);
  }

  // Ladder
  public static void mapObject10229(Player player, int index, MapObject mapObject) {
    // Waterbirth Dungeon
    Tile tile = new Tile(2545, 10143, 0);
    player.getMovement().ladderUpTeleport(tile);
    player.getController().stopWithTeleport();
  }

  // Ladder
  public static void mapObject10560(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3190 && mapObject.getY() == 9758) {
      player.getMovement().ladderUpTeleport(new Tile(2212, 4940));
    }
  }

  // Icy Cavern
  public static void mapObject10595(Player player, int index, MapObject mapObject) {
    // Asgarnian Ice Dungeon
    Tile tile = new Tile(3056, 9562, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Icy Cavern
  public static void mapObject10596(Player player, int index, MapObject mapObject) {
    // Asgarnian Ice Dungeon
    Tile tile = new Tile(3056, 9555, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Chest
  public static void mapObject10661(Player player, int index, MapObject mapObject) {
    player.getBank().open();
  }

  // Tall tree
  public static void mapObject10819(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 40) {
      player.getGameEncoder().sendMessage("You need an Agility level of 40 to use this course.");
      return;
    }
    Tile toTile = new Tile(3506, 3492, 2);
    player.getMovement().ladderUpTeleport(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 10;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject10820(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3505 || player.getY() != 3497) {
      return;
    }
    int direction = Tile.NORTH;
    player.setAnimation(2588);
    Tile toTile = new Tile(3503, 3504, 2);
    player.setForceMovement(new ForceMovement(player, 0, toTile, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setForceMovement(new ForceMovement(direction));
        player.setAnimation(-1);
        int xp = 8;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject10821(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3498 || player.getY() != 3504) {
      return;
    }
    int direction = Tile.WEST;
    player.setAnimation(2588);
    Tile toTile = new Tile(3492, 3504, 2);
    player.setForceMovement(new ForceMovement(player, 0, toTile, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setForceMovement(new ForceMovement(direction));
        player.setAnimation(-1);
        int xp = 8;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject10822(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3478 || player.getY() != 3493) {
      return;
    }
    int direction = Tile.SOUTH;
    player.setAnimation(2588);
    Tile toTile = new Tile(3478, 3486, 2);
    player.setForceMovement(new ForceMovement(player, 0, toTile, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setForceMovement(new ForceMovement(direction));
        player.setAnimation(-1);
        int xp = 8;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject10823(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3502 || player.getY() != 3476) {
      return;
    }
    int direction = Tile.EAST;
    player.setAnimation(2588);
    Tile toTile = new Tile(3510, 3476, 2);
    player.setForceMovement(new ForceMovement(player, 0, toTile, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setForceMovement(new ForceMovement(direction));
        player.setAnimation(-1);
        int xp = 11;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject10828(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3487 || player.getY() != 3499) {
      return;
    }
    int direction = Tile.WEST;
    player.setAnimation(2588);
    Tile toTile = new Tile(3479, 3499, 3);
    player.setForceMovement(new ForceMovement(player, 0, toTile, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setForceMovement(new ForceMovement(direction));
        player.setAnimation(-1);
        int xp = 10;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Pole-vault
  public static void mapObject10831(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3480 || player.getY() != 3484) {
      return;
    }
    player.setAnimation(7132);
    Tile toTile = new Tile(3489, 3476, 3);
    player.setForceMovement(new ForceMovement(player, 1, toTile, 4, Tile.EAST));
    player.lock();
    PEvent event = new PEvent(3) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setAnimation(-1);
        int xp = 10;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject10832(Player player, int index, MapObject mapObject) {
    if (player.getX() != 3510 || player.getY() != 3482) {
      return;
    }
    Tile toTile = new Tile(3510, 3485, 0);
    player.getMovement().animatedTeleport(toTile, 7133, null, 0);
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 175;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (PRandom.randomE(10) == 0) {
          int amount = 4;
          if (player.isPremiumMember()) {
            amount = 6;
          }
          player.getInventory().addOrDropItem(11849, amount);
        }
        if (PRandom.randomE(2) == 0) {
          int rewardType = PRandom.randomE(3);
          if (rewardType == 0) {
            player.getInventory().addOrDropItem(3009, 1);
          } else if (rewardType == 1) {
            player.getInventory().addOrDropItem(3017, 1);
          } else if (rewardType == 2) {
            player.getInventory().addOrDropItem(12640, 4);
          }
        }
        player.getFamiliar().rollSkillPet(Skills.AGILITY, 36842, 20659);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Wall
  public static void mapObject11373(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2729 || player.getY() != 3489) {
      return;
    }
    if (player.getSkills().getLevel(Skills.AGILITY) < 60) {
      player.getGameEncoder().sendMessage("You need an Agility level of 60 to use this course.");
      return;
    }
    Tile toTile1 = new Tile(2729, 3488, 1);
    Tile toTile2 = new Tile(2729, 3491, 3);
    player.setFaceTile(toTile2);
    player.getMovement().animatedTeleport(toTile1, 3063, null, 1);
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 1) {
          player.getMovement().animatedTeleport(toTile2, 1120, null, 2);
        } else if (super.getExecutions() == 4) {
          super.stop();
          int xp = 45;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject11374(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2721 || player.getY() != 3494) {
      return;
    }
    Tile toTile1 = new Tile(2719, 3495, 2);
    Tile toTile2 = new Tile(2713, 3494, 2);
    int direction = Tile.WEST;
    player.setFaceTile(toTile2);
    player.setAnimation(2588);
    player.setForceMovement(new ForceMovement(player, 0, toTile1, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 0) {
          player.getMovement().teleport(toTile1);
          player.setForceMovement(new ForceMovement(direction));
          player.setAnimation(-1);
        } else if (super.getExecutions() == 1) {
          player.setAnimation(2588);
          player.setForceMovement(new ForceMovement(player, 0, toTile2, 4, direction));
        } else if (super.getExecutions() == 2) {
          player.unlock();
          player.getMovement().teleport(toTile2);
          player.setForceMovement(new ForceMovement(direction));
          player.setAnimation(-1);
          int xp = 20;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject11375(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2710 && player.getX() != 2711 || player.getY() != 3477) {
      return;
    }
    Tile toTile1 = new Tile(2710, 3474, 1);
    Tile toTile2 = new Tile(2710, 3472, 3);
    int direction = Tile.SOUTH;
    player.setFaceTile(toTile2);
    player.setAnimation(2588);
    player.setForceMovement(new ForceMovement(player, 0, toTile1, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 0) {
          player.getMovement().teleport(toTile1);
          player.setForceMovement(new ForceMovement(direction));
          player.setAnimation(1120);
        } else if (super.getExecutions() == 2) {
          player.unlock();
          player.getMovement().teleport(toTile2);
          player.setAnimation(-1);
          int xp = 35;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject11376(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2702 || player.getY() != 3470) {
      return;
    }
    int direction = Tile.SOUTH;
    player.setAnimation(2588);
    Tile toTile = new Tile(2702, 3465, 2);
    player.setForceMovement(new ForceMovement(player, 0, toTile, 4, direction));
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        player.setForceMovement(new ForceMovement(direction));
        player.setAnimation(-1);
        int xp = 15;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Edge
  public static void mapObject11377(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2702 || player.getY() != 3464) {
      return;
    }
    Tile toTile = new Tile(2704, 3464, 0);
    player.setFaceTile(toTile);
    player.getMovement().animatedTeleport(toTile, 7133, null, 0);
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 435;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (PRandom.randomE(6) == 0) {
          int amount = 4;
          if (player.isPremiumMember()) {
            amount = 6;
          }
          player.getInventory().addOrDropItem(11849, amount);
        }
        if (PRandom.randomE(2) == 0) {
          int rewardType = PRandom.randomE(3);
          if (rewardType == 0) {
            player.getInventory().addOrDropItem(3009, 1);
          } else if (rewardType == 1) {
            player.getInventory().addOrDropItem(3017, 1);
          } else if (rewardType == 2) {
            player.getInventory().addOrDropItem(12640, 4);
          }
        }
        player.getFamiliar().rollSkillPet(Skills.AGILITY, 35205, 20659);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Tightrope
  public static void mapObject11378(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2710) {
      return;
    }
    Tile toTile = new Tile(mapObject.getX(), 3480, mapObject.getHeight());
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(762);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == toTile.getX() && player.getY() == toTile.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 20;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Rough wall
  public static void mapObject11391(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2625 || player.getY() != 3677) {
      return;
    }
    if (player.getSkills().getLevel(Skills.AGILITY) < 80) {
      player.getGameEncoder().sendMessage("You need an Agility level of 80 to use this course.");
      return;
    }
    Tile toTile = new Tile(2625, 3675, 3);
    player.getMovement().ladderUpTeleport(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 20;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject11392(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2622 || player.getY() != 3672) {
      return;
    }
    int direction = Tile.SOUTH;
    player.setAnimation(6132);
    Tile toTile = new Tile(2622, 3668, 3);
    player.setForceMovement(new ForceMovement(player, 1, toTile, 2, direction));
    player.lock();
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        int xp = 30;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Tightrope
  public static void mapObject11393(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2622 || player.getY() != 3658) {
      return;
    }
    Tile toTile = new Tile(2627, 3654, mapObject.getHeight());
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(762);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == toTile.getX() && player.getY() == toTile.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 40;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject11395(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2629 || player.getY() != 3655) {
      return;
    }
    player.setAnimation(6132);
    Tile toTile1 = new Tile(2629, 3658, 3);
    Tile toTile2 = new Tile(2635, 3658, 3);
    Tile toTile3 = new Tile(2640, 3652, 3);
    player.setForceMovement(new ForceMovement(player, 1, toTile1, 2, Tile.NORTH));
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    PEvent event = new PEvent() {
      boolean reachedRope = false;

      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 0) {
          return;
        } else if (super.getExecutions() == 1) {
          player.getMovement().teleport(toTile1);
        } else if (super.getExecutions() == 2) {
          player.getMovement().clear();
          player.getMovement().addMovement(toTile2);
          player.getAppearance().setForceMoveAnimation(754);
        } else if (!reachedRope) {
          if (player.getX() == toTile2.getX() && player.getY() == toTile2.getY()) {
            reachedRope = true;
            player.getMovement().clear();
            player.getMovement().addMovement(toTile3);
            player.getAppearance().setForceMoveAnimation(762);
          }
        } else if (player.getX() == toTile3.getX() && player.getY() == toTile3.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 85;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Gap
  public static void mapObject11396(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2643 || player.getY() != 3653) {
      return;
    }
    int direction = Tile.NORTH;
    player.setAnimation(6132);
    Tile toTile = new Tile(2643, 3657, 3);
    player.setForceMovement(new ForceMovement(player, 1, toTile, 2, direction));
    player.lock();
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.unlock();
        player.getMovement().teleport(toTile);
        int xp = 25;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Tightrope
  public static void mapObject11397(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2647 || player.getY() != 3662) {
      return;
    }
    Tile toTile = new Tile(2655, 3670, 3);
    player.getMovement().clear();
    player.getMovement().addMovement(2647, 3663);
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(762);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == toTile.getX() && player.getY() == toTile.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 105;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Our lives
  public static void mapObject11398(Player player, int index, MapObject mapObject) {
    if (index == 1) {
      player.getController().stop();
    }
  }

  // Pile of fish
  public static void mapObject11404(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2655 || player.getY() != 3676) {
      return;
    }
    Tile toTile = new Tile(2652, 3676, 0);
    player.getMovement().animatedTeleport(toTile, 7133, null, 0);
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 475;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (PRandom.randomE(4) == 0) {
          int amount = 4;
          if (player.isPremiumMember()) {
            amount = 6;
          }
          player.getInventory().addOrDropItem(11849, amount);
        }
        if (PRandom.randomE(2) == 0) {
          int rewardType = PRandom.randomE(3);
          if (rewardType == 0) {
            player.getInventory().addOrDropItem(3009, 1);
          } else if (rewardType == 1) {
            player.getInventory().addOrDropItem(3017, 1);
          } else if (rewardType == 2) {
            player.getInventory().addOrDropItem(12640, 4);
          }
        }
        player.getFamiliar().rollSkillPet(Skills.AGILITY, 31063, 20659);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Door
  public static void mapObject11726(Player player, int index, MapObject mapObject) {
    if (mapObject.isBusy()) {
      return;
    }
    if ((mapObject.getDirection() == 0 || mapObject.getDirection() == 2)
        && player.getY() != mapObject.getY()) {
      return;
    } else if ((mapObject.getDirection() == 1 || mapObject.getDirection() == 3)
        && player.getX() != mapObject.getX()) {
      return;
    }
    if (index == 1) {
      player.getGameEncoder().sendMessage("You attempt to pick the lock.");
    }
    if (index == 1 && !player.getInventory().hasItem(1523)) {
      player.getGameEncoder().sendMessage("You need a lockpick for this lock.");
      return;
    }
    if (player.getX() != mapObject.getX() || player.getY() != mapObject.getY()) {
      if (index == 1) {
        player.getGameEncoder().sendMessage("The door is already unlocked.");
        return;
      }
    } else {
      if (index == 0) {
        player.getGameEncoder().sendMessage("The door is locked.");
        return;
      } else if (PRandom.randomE(4) != 0) {
        player.getGameEncoder().sendMessage("You fail to pick the lock.");
        return;
      }
    }
    if (index == 0) {
      player.getGameEncoder().sendMessage("You go through the door.");
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("You manage to pick the lock.");
    }
    player.getMovement().clear();
    if (mapObject.getDirection() == 0) {
      if (player.getX() == mapObject.getX()) {
        player.getMovement().addMovement(mapObject.getX() - 1, mapObject.getY());
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    } else if (mapObject.getDirection() == 1) {
      if (player.getY() == mapObject.getY()) {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY() + 1);
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    } else if (mapObject.getDirection() == 2) {
      if (player.getX() == mapObject.getX()) {
        player.getMovement().addMovement(mapObject.getX() + 1, mapObject.getY());
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    } else if (mapObject.getDirection() == 3) {
      if (player.getY() == mapObject.getY()) {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY() - 1);
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    }
    Region.openDoor(player, mapObject, 1, false, false);
  }

  // Door
  public static void mapObject11727(Player player, int index, MapObject mapObject) {
    if (mapObject.isBusy()) {
      return;
    }
    if ((mapObject.getDirection() == 0 || mapObject.getDirection() == 2)
        && player.getY() != mapObject.getY()) {
      return;
    } else if ((mapObject.getDirection() == 1 || mapObject.getDirection() == 3)
        && player.getX() != mapObject.getX()) {
      return;
    }
    if (index == 1) {
      player.getGameEncoder().sendMessage("You attempt to pick the lock.");
    }
    if (index == 1 && !player.getInventory().hasItem(1523)) {
      player.getGameEncoder().sendMessage("You need a lockpick for this lock.");
      return;
    }
    if (player.getX() == mapObject.getX() && player.getY() == mapObject.getY()) {
      if (index == 1) {
        player.getGameEncoder().sendMessage("The door is already unlocked.");
        return;
      }
    } else {
      if (index == 0) {
        player.getGameEncoder().sendMessage("The door is locked.");
        return;
      } else if (PRandom.randomE(4) != 0) {
        player.getGameEncoder().sendMessage("You fail to pick the lock.");
        return;
      }
    }
    if (index == 0) {
      player.getGameEncoder().sendMessage("You go through the door.");
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("You manage to pick the lock.");
    }
    player.getMovement().clear();
    if (mapObject.getDirection() == 0) {
      if (player.getX() == mapObject.getX()) {
        player.getMovement().addMovement(mapObject.getX() - 1, mapObject.getY());
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    } else if (mapObject.getDirection() == 1) {
      if (player.getY() == mapObject.getY()) {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY() + 1);
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    } else if (mapObject.getDirection() == 2) {
      if (player.getX() == mapObject.getX()) {
        player.getMovement().addMovement(mapObject.getX() + 1, mapObject.getY());
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    } else if (mapObject.getDirection() == 3) {
      if (player.getY() == mapObject.getY()) {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY() - 1);
      } else {
        player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
      }
    }
    Region.openDoor(player, mapObject, 1, false, false);
  }

  // Cave entrance
  public static void mapObject11833(Player player, int index, MapObject mapObject) {
    player.openDialogue("tzhaar", 0);
  }

  // Cave entrance
  public static void mapObject11834(Player player, int index, MapObject mapObject) {
    player.getCombat().getTzHaar().exitFightCave();
  }

  // Mole hill
  public static void mapObject12202(Player player, int index, MapObject mapObject) {
    player.openDialogue("bossinstance", 6);
  }

  // Portal
  public static void mapObject12356(Player player, int index, MapObject mapObject) {
    player.getController().stop();
  }

  // Altar
  public static void mapObject13185(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.getPrayer().adjustPoints(player.getController().getLevelForXP(Skills.PRAYER));
    player.setAnimation(Prayer.PRAY_ANIMATION);
  }

  // Rocks
  public static void mapObject14106(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2887 && mapObject.getY() == 9823) {
      // Taverley Dungeon
      player.getMovement().ladderUpTeleport(new Tile(2886, 9823, 0));
    }
  }


  public static void mapObject14235(Player player, int index, MapObject mapObject) {
    Region.openDoors(player, mapObject);
  }

  public static void mapObject14233(Player player, int index, MapObject mapObject) {
    Region.openDoors(player, mapObject);
  }

  // Ladder
  public static void mapObject14296(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2644 && mapObject.getY() == 2601) {
      if (player.getX() == 2645 && player.getY() == 2601) {
        player.getMovement().ladderUpTeleport(new Tile(2643, 2601, 0));
      } else if (player.getX() == 2643 && player.getY() == 2601) {
        player.getMovement().ladderDownTeleport(new Tile(2645, 2601, 0));
      }
    } else if (mapObject.getX() == 2647 && mapObject.getY() == 2586) {
      if (player.getX() == 2647 && player.getY() == 2587) {
        player.getMovement().ladderUpTeleport(new Tile(2647, 2585, 0));
      } else if (player.getX() == 2647 && player.getY() == 2585) {
        player.getMovement().ladderDownTeleport(new Tile(2647, 2587, 0));
      }
    } else if (mapObject.getX() == 2666 && mapObject.getY() == 2586) {
      if (player.getX() == 2666 && player.getY() == 2587) {
        player.getMovement().ladderUpTeleport(new Tile(2666, 2585, 0));
      } else if (player.getX() == 2666 && player.getY() == 2585) {
        player.getMovement().ladderDownTeleport(new Tile(2666, 2587, 0));
      }
    } else if (mapObject.getX() == 2669 && mapObject.getY() == 2601) {
      if (player.getX() == 2668 && player.getY() == 2601) {
        player.getMovement().ladderUpTeleport(new Tile(2670, 2601, 0));
      } else if (player.getX() == 2670 && player.getY() == 2601) {
        player.getMovement().ladderDownTeleport(new Tile(2668, 2601, 0));
      }
    }
  }

  // Gangplank
  public static void mapObject14315(Player player, int index, MapObject mapObject) {
    player.setController(new PestControlPC());
  }

  // Staircase
  public static void mapObject14735(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Staircase
  public static void mapObject14736(Player player, int index, MapObject mapObject) {
    if (index == 0) {
      player.openDialogue("climb", 0);
    } else if (index == 1) {
      Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
      player.getMovement().ladderUpTeleport(tile);
    } else if (index == 2) {
      Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Staircase
  public static void mapObject14737(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Sack
  public static void mapObject14743(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3093 && mapObject.getY() == 3956) {
      if (!player.getInventory().hasItem(946)) {
        player.getGameEncoder().sendMessage("You search the sack and find a knife.");
        player.getInventory().addItem(946, 1);
      } else {
        player.getGameEncoder().sendMessage("You search the sack but find nothing.");
      }
    }
  }

  // Obelisk
  public static void mapObject14826(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (index == 0) {
      WildernessObelisk.activateObelisk(player, mapObject);
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("Teleport to destination");
    } else if (index == 2) {
      WildernessObelisk.setDestinationDialogue(player);
    }
  }

  // Obelisk
  public static void mapObject14827(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (index == 0) {
      WildernessObelisk.activateObelisk(player, mapObject);
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("Teleport to destination");
    } else if (index == 2) {
      WildernessObelisk.setDestinationDialogue(player);
    }
  }

  // Obelisk
  public static void mapObject14828(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (index == 0) {
      WildernessObelisk.activateObelisk(player, mapObject);
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("Teleport to destination");
    } else if (index == 2) {
      WildernessObelisk.setDestinationDialogue(player);
    }
  }

  // Obelisk
  public static void mapObject14829(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (index == 0) {
      WildernessObelisk.activateObelisk(player, mapObject);
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("Teleport to destination");
    } else if (index == 2) {
      WildernessObelisk.setDestinationDialogue(player);
    }
  }

  // Obelisk
  public static void mapObject14830(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (index == 0) {
      WildernessObelisk.activateObelisk(player, mapObject);
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("Teleport to destination");
    } else if (index == 2) {
      WildernessObelisk.setDestinationDialogue(player);
    }
  }

  // Obelisk
  public static void mapObject14831(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.BLOODY_KEY)
        || player.getInventory().hasItem(ItemId.BLOODIER_KEY)) {
      player.getGameEncoder().sendMessage("You can't use this right now.");
      return;
    }
    if (index == 0) {
      WildernessObelisk.activateObelisk(player, mapObject);
    } else if (index == 1) {
      player.getGameEncoder().sendMessage("Teleport to destination");
    } else if (index == 2) {
      WildernessObelisk.setDestinationDialogue(player);
    }
  }

  // Bank chest
  public static void mapObject14886(Player player, int index, MapObject mapObject) {
    if (!player.getWidgetManager().isDiaryComplete(AchievementDiary.Name.FALADOR,
        AchievementDiaryTask.Difficulty.HARD)) {
      player.getGameEncoder()
          .sendMessage("You need to complete the hard Falador achievement diary.");
      return;
    }
    player.getBank().open();
  }

  // Altar
  public static void mapObject14897(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.AIR);
  }

  // Altar
  public static void mapObject14898(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.MIND);
  }

  // Altar
  public static void mapObject14899(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.WATER);
  }

  // Altar
  public static void mapObject14900(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.EARTH);
  }

  // Altar
  public static void mapObject14901(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.FIRE);
  }

  // Altar
  public static void mapObject14902(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.BODY);
  }

  // Altar
  public static void mapObject14903(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.COSMIC);
  }

  // Altar
  public static void mapObject14904(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.LAW);
  }

  // Altar
  public static void mapObject14905(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.NATURE);
  }

  // Altar
  public static void mapObject14906(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.CHAOS);
  }

  // Altar
  public static void mapObject14907(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.DEATH);
  }

  // Guild Door
  public static void mapObject14910(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    }
    if (player.getSkills().getLevel(Skills.CRAFTING) < 40) {
      player.getGameEncoder().sendMessage("You need a Crafting level of 40 to enter.");
      return;
    }
    player.getMovement().clear();
    if (player.getY() >= 3289) {
      player.getMovement().addMovement(player.getX(), player.getY() - 1);
    } else {
      player.getMovement().addMovement(player.getX(), player.getY() + 1);
    }
    Region.openDoors(player, mapObject, 1, false);
  }

  // Stepping stone
  public static void mapObject14917(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 82) {
      player.getGameEncoder().sendMessage("You need an Agility level of 82 to use this.");
      return;
    }
    if (player.getController().isMagicBound()) {
      player.getGameEncoder()
          .sendMessage("A magical force stops you from moving for "
              + PTime
                  .tickToSec(player.getMovement().getMagicBindDelay() - Movement.MAGIC_REBIND_DELAY)
              + " more seconds.");
      return;
    }
    if (player.getY() <= 3879) {
      player.getMovement().animatedTeleport(new Tile(3091, 3882, player.getHeight()), 7133, null,
          0);
    } else {
      player.getMovement().animatedTeleport(new Tile(3093, 3879, player.getHeight()), 7133, null,
          0);
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Stepping stone
  public static void mapObject14918(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 74) {
      player.getGameEncoder().sendMessage("You need an Agility level of 74 to use this.");
      return;
    }
    if (player.getController().isMagicBound()) {
      player.getGameEncoder()
          .sendMessage("A magical force stops you from moving for "
              + PTime
                  .tickToSec(player.getMovement().getMagicBindDelay() - Movement.MAGIC_REBIND_DELAY)
              + " more seconds.");
      return;
    }
    if (player.getX() == 3201 && player.getY() == 3810) {
      player.getMovement().animatedTeleport(new Tile(3201, 3807, player.getHeight()), 7133, null,
          0);
    } else if (player.getX() == 3201 && player.getY() == 3807) {
      player.getMovement().animatedTeleport(new Tile(3201, 3810, player.getHeight()), 7133, null,
          0);
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Cart tunnel
  public static void mapObject15830(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3408 && mapObject.getY() == 9623) {
      player.getMovement().ladderUpTeleport(new Tile(3428, 3225));
    }
  }
}
