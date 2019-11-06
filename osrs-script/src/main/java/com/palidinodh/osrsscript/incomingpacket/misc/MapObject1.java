package com.palidinodh.osrsscript.incomingpacket.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.model.ForceMovement;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.Scroll;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.Region;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Runecrafting;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.Smithing;
import com.palidinodh.osrscore.model.player.controller.BossInstancePC;
import com.palidinodh.osrscore.model.player.controller.ClanWarsFreeForAllPC;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PNumber;
import com.palidinodh.util.PTime;
import lombok.var;

public class MapObject1 {
  /* 16384 - 16639 */

  // Obstacle pipe
  public static void mapObject16509(Player player, int index, MapObject mapObject) {
    // Taverley Dungeon
    if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
      player.getGameEncoder().sendMessage("You need an Agility level of 70 to do this.");
      return;
    }
    if (player.getX() <= 2887) {
      Tile tile = new Tile(2892, 9799, 0);
      player.getMovement().animatedTeleport(tile, 746, 748, null, null, 0);
    } else {
      Tile tile = new Tile(2886, 9799, 0);
      player.getMovement().animatedTeleport(tile, 746, 748, null, null, 0);
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Strange floor
  public static void mapObject16510(Player player, int index, MapObject mapObject) {
    // Taverley Dungeon
    if (player.getSkills().getLevel(Skills.AGILITY) < 80) {
      player.getGameEncoder().sendMessage("You need an Agility level of 80 to do this.");
      return;
    }
    if (player.getX() <= 2878) {
      Tile tile = new Tile(2880, 9813, 0);
      player.getMovement().animatedTeleport(tile, 3067, null, 1);
    } else {
      Tile tile = new Tile(2878, 9813, 0);
      player.getMovement().animatedTeleport(tile, 3067, null, 1);
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Spikey chain
  public static void mapObject16537(Player player, int index, MapObject mapObject) {
    // Slayer Tower
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
    player.getMovement().animatedTeleport(tile, 834, null, 1);
  }

  // Spikey chain
  public static void mapObject16538(Player player, int index, MapObject mapObject) {
    // Slayer Tower
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
    player.getMovement().animatedTeleport(tile, 834, null, 1);
  }

  // Crevice
  public static void mapObject16539(Player player, int index, MapObject mapObject) {
    // Fremennik Dungeon
    if (mapObject.getX() == 2734 && mapObject.getY() == 10008) {
      Tile tile = new Tile(2730, 10008, 0);
      player.getMovement().ladderDownTeleport(tile);
    } else if (mapObject.getX() == 2731 && mapObject.getY() == 10008) {
      Tile tile = new Tile(2735, 10008, 0);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Crevice
  public static void mapObject16543(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
      player.getGameEncoder().sendMessage("You need an Agility level of 42 to do this.");
      return;
    }
    if (mapObject.getX() == 3029 && mapObject.getY() == 9806) {
      player.getMovement().teleport(3035, 9806);
    } else if (mapObject.getX() == 3034 && mapObject.getY() == 9806) {
      player.getMovement().teleport(3028, 9806);
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Strange floor
  public static void mapObject16544(Player player, int index, MapObject mapObject) {
    // Fremennik Dungeon
    if (player.getX() >= mapObject.getX()) {
      Tile tile = new Tile(mapObject.getX() - 1, mapObject.getY(), 0);
      player.getMovement().animatedTeleport(tile, 3067, null, 1);
    } else {
      Tile tile = new Tile(mapObject.getX() + 1, mapObject.getY(), 0);
      player.getMovement().animatedTeleport(tile, 3067, null, 1);
    }
  }

  // Staircase
  public static void mapObject16664(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3044 && mapObject.getY() == 3924 && player.getHeight() == 0) {
      player.getMovement().teleport(3044, 10322);
    }
  }

  // Staircase
  public static void mapObject16665(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3044 && mapObject.getY() == 10324 && player.getHeight() == 0) {
      player.getMovement().teleport(3044, 3927);
    }
  }

  // Staircase
  public static void mapObject16671(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Staircase
  public static void mapObject16672(Player player, int index, MapObject mapObject) {
    if (index == 0) {
      player.openDialogue("climb", 0);
    } else if (index == 1) {
      Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
      player.getMovement().ladderUpTeleport(tile);
    } else if (index == 2) {
      Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Staircase
  public static void mapObject16675(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2444 && mapObject.getY() == 3414) {
      player.getMovement().ladderUpTeleport(new Tile(2445, 3416, 1));
    } else if (mapObject.getX() == 2445 && mapObject.getY() == 3434) {
      player.getMovement().ladderUpTeleport(new Tile(2445, 3433, 1));
    }
  }

  // Staircase
  public static void mapObject16677(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2445 && mapObject.getY() == 3415) {
      player.getMovement().ladderDownTeleport(new Tile(2445, 3416));
    } else if (mapObject.getX() == 2445 && mapObject.getY() == 3434) {
      player.getMovement().ladderDownTeleport(new Tile(2445, 3433));
    }
  }

  // Ladder
  public static void mapObject16679(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Ladder
  public static void mapObject16680(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2824 && mapObject.getY() == 3507) {
      // Ice Queen
      player.getMovement().ladderDownTeleport(new Tile(2823, 9907));
    } else if (mapObject.getX() == 2827 && mapObject.getY() == 3510) {
      // Ice Queen
      player.getMovement().ladderDownTeleport(new Tile(2826, 9910));
    } else if (mapObject.getX() == 2859 && mapObject.getY() == 3519) {
      // Ice Queen
      player.getMovement().ladderDownTeleport(new Tile(2858, 9919));
    } else if (mapObject.getX() == 2857 && mapObject.getY() == 3517) {
      // Ice Queen
      player.getMovement().ladderDownTeleport(new Tile(2858, 9917));
    } else if (mapObject.getX() == 3088 && mapObject.getY() == 3571) {
      // Air obelisk
      player.getMovement().ladderDownTeleport(new Tile(3087, 9971));
    } else if (mapObject.getX() == 2884 && mapObject.getY() == 3397) {
      // Taverley Dungeon entrance
      Tile tile = new Tile(2884, 9798, 0);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Ladder
  public static void mapObject16682(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Ladder
  public static void mapObject16683(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() + 1);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Ladder
  public static void mapObject17028(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("This ladder doesn't seem to reach the floor above it.");
  }

  // Ladder
  public static void mapObject17384(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2892 && mapObject.getY() == 3507) {
      // Heros' Guild entrance
      Tile tile = new Tile(2893, 9907);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Ladder
  public static void mapObject17385(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2884 && mapObject.getY() == 9797) {
      // Taverley Dungeon exit
      Tile tile = new Tile(2884, 3396, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else if (mapObject.getX() == 3008 && mapObject.getY() == 9550) {
      // Asgarnian Ice Dungeon exit
      Tile tile = new Tile(3009, 3150, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else if (mapObject.getX() == 2824 && mapObject.getY() == 9907) {
      // Ice Queen
      player.getMovement().ladderUpTeleport(new Tile(2823, 3507));
    } else if (mapObject.getX() == 2857 && mapObject.getY() == 9917) {
      // Ice Queen
      player.getMovement().ladderUpTeleport(new Tile(2857, 3516));
    } else if (mapObject.getX() == 2859 && mapObject.getY() == 9919) {
      // Ice Queen
      player.getMovement().ladderUpTeleport(new Tile(2858, 3519));
    } else if (mapObject.getX() == 2827 && mapObject.getY() == 9910) {
      // Ice Queen
      player.getMovement().ladderUpTeleport(new Tile(2826, 3510));
    } else if (mapObject.getX() == 3088 && mapObject.getY() == 9971) {
      // Air obelisk
      player.getMovement().ladderUpTeleport(new Tile(3087, 3571));
    }
  }

  // Ladder
  public static void mapObject18987(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3017 && mapObject.getY() == 3849) {
      if (player.getX() != 3017 || player.getY() != 3850) {
        return;
      }
      player.getMovement().teleport(3069, 10255);
    }
  }

  // Ladder
  public static void mapObject18988(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3069 && mapObject.getY() == 10256) {
      player.getMovement().teleport(3016, 3849);
    }
  }

  // Ladder
  public static void mapObject18989(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3069 && mapObject.getY() == 3856) {
      Tile tile = new Tile(3017, 10250, 0);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Ladder
  public static void mapObject18990(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3017 && mapObject.getY() == 10249) {
      Tile tile = new Tile(3069, 3857, 0);
      player.getMovement().ladderDownTeleport(tile);
    }
  }

  // Crevice
  public static void mapObject19043(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3046 && mapObject.getY() == 10327) {
      player.getMovement().teleport(3048, 10337);
    } else if (mapObject.getX() == 3048 && mapObject.getY() == 10335) {
      player.getMovement().teleport(3046, 10326);
    }
  }

  // Obstacle pipe
  public static void mapObject20210(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2552 && mapObject.getY() == 3559) {
      if (player.getY() >= 3560) {
        player.getMovement().teleport(2552, 3558);
      } else {
        player.getMovement().teleport(2552, 3561);
      }
    }
  }

  // Obstacle net
  public static void mapObject20211(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2539) {
      return;
    }
    player.getGameEncoder().sendMessage("You climb the netting.");
    Tile toTile = new Tile(mapObject.getX() - 1, player.getY(), 1);
    player.getMovement().ladderUpTeleport(toTile);
    player.setFaceTile(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 8;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (player.getAttributeInt("agility_stage") == 2) {
          player.putAttribute("agility_stage", 3);
        }
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Staircase
  public static void mapObject20667(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().exitCrypt(NpcId.AHRIM_THE_BLIGHTED_98);
  }

  // Staircase
  public static void mapObject20668(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().exitCrypt(NpcId.DHAROK_THE_WRETCHED_115);
  }

  // Staircase
  public static void mapObject20669(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().exitCrypt(NpcId.GUTHAN_THE_INFESTED_115);
  }

  // Staircase
  public static void mapObject20670(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().exitCrypt(NpcId.KARIL_THE_TAINTED_98);
  }

  // Staircase
  public static void mapObject20671(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().exitCrypt(NpcId.TORAG_THE_CORRUPTED_115);
  }

  // Staircase
  public static void mapObject20672(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().exitCrypt(NpcId.VERAC_THE_DEFILED_115);
  }

  // Sarcophagus
  public static void mapObject20720(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().searchSarcophagus(NpcId.DHAROK_THE_WRETCHED_115);
  }

  // Sarcophagus
  public static void mapObject20721(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().searchSarcophagus(NpcId.TORAG_THE_CORRUPTED_115);
  }

  // Sarcophagus
  public static void mapObject20722(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().searchSarcophagus(NpcId.GUTHAN_THE_INFESTED_115);
  }

  // Sarcophagus
  public static void mapObject20770(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().searchSarcophagus(NpcId.AHRIM_THE_BLIGHTED_98);
  }

  // Sarcophagus
  public static void mapObject20771(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().searchSarcophagus(NpcId.KARIL_THE_TAINTED_98);
  }

  // Sarcophagus
  public static void mapObject20772(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().searchSarcophagus(NpcId.VERAC_THE_DEFILED_115);
  }

  // Cage
  public static void mapObject20873(Player player, int index, MapObject mapObject) {
    player.getRandomEvent().sendWidget();
  }

  // Dungeon entrance
  public static void mapObject20877(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon entrance
    Tile tile = new Tile(2712, 9564, 0);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Exit
  public static void mapObject20878(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon exit
    Tile tile = new Tile(2744, 3151, 0);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Log balance
  public static void mapObject20882(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2687, 9506, 0);
    player.getMovement().teleport(tile);
  }

  // Log balance
  public static void mapObject20884(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2682, 9506, 0);
    player.getMovement().teleport(tile);
  }

  // (Fishing Guild) Door
  public static void mapObject20925(Player player, int index, MapObject mapObject) {
    if (mapObject.getOriginal() != null || mapObject.getAttachment() != null) {
      return;
    }
    if (player.getY() >= 3394) {
      player.getMovement().clear();
      player.getMovement().addMovement(player.getX(), player.getY() - 1);
      Region.openDoor(player, mapObject, 1, false, false);
    } else {
      player.getMovement().clear();
      player.getMovement().addMovement(player.getX(), player.getY() + 1);
      Region.openDoor(player, mapObject, 1, false, false);
    }
  }

  // Chest
  public static void mapObject20973(Player player, int index, MapObject mapObject) {
    player.getCombat().getBarrows().openChest(mapObject.getX() != 3551 || mapObject.getY() != 9695);
  }

  // Clay forge
  public static void mapObject21303(Player player, int index, MapObject mapObject) {
    Smithing.openSmelt(player);
  }

  // Rope bridge
  public static void mapObject21306(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2317, 3831);
  }

  // Rope bridge
  public static void mapObject21307(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2317, 3824);
  }

  // Rope bridge
  public static void mapObject21308(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2343, 3828);
  }

  // Rope bridge
  public static void mapObject21309(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2343, 3821);
  }

  // Rope bridge
  public static void mapObject21310(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2314, 3847);
  }

  // Rope bridge
  public static void mapObject21311(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2343, 3840);
  }

  // Rope bridge
  public static void mapObject21312(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2355, 3847);
  }

  // Rope bridge
  public static void mapObject21313(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2355, 3840);
  }

  // Rope bridge
  public static void mapObject21314(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2378, 3847);
  }

  // Rope bridge
  public static void mapObject21315(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2378, 3840);
  }

  // Staircase
  public static void mapObject21455(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2407, 10187);
  }

  // Staircase
  public static void mapObject21578(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2398, 3811);
  }

  // Stairs
  public static void mapObject21722(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2643, 9594, 2);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Stairs
  public static void mapObject21724(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2649, 9591, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Stairs
  public static void mapObject21725(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2636, 9510, 2);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Stairs
  public static void mapObject21726(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2636, 9517, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Pipe
  public static void mapObject21727(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (mapObject.getX() == 2698 && mapObject.getY() == 9498) {
      Tile tile = new Tile(2698, 9492, 0);
      player.getMovement().animatedTeleport(tile, 746, 748, null, null, 0);
    } else if (mapObject.getX() == 2698 && mapObject.getY() == 9493) {
      Tile tile = new Tile(2698, 9500, 0);
      player.getMovement().animatedTeleport(tile, 746, 748, null, null, 0);
    }
  }

  // Pipe
  public static void mapObject21728(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (mapObject.getX() == 2655 && mapObject.getY() == 9567) {
      Tile tile = new Tile(2655, 9573, 0);
      player.getMovement().animatedTeleport(tile, 746, 748, null, null, 0);
    } else if (mapObject.getX() == 2655 && mapObject.getY() == 9571) {
      Tile tile = new Tile(2655, 9566, 0);
      player.getMovement().animatedTeleport(tile, 746, 748, null, null, 0);
    }
  }

  // Vines
  public static void mapObject21731(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (player.getX() >= 2691) {
      Tile tile = new Tile(2689, 9564, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(2691, 9564, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Vines
  public static void mapObject21732(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (player.getY() <= 9568) {
      Tile tile = new Tile(2683, 9570, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(2683, 9568, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Vines
  public static void mapObject21733(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (player.getX() <= 2672) {
      Tile tile = new Tile(2674, 9499, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(2672, 9499, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Vines
  public static void mapObject21734(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (player.getX() <= 2674) {
      Tile tile = new Tile(2676, 9479, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(2674, 9479, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Vines
  public static void mapObject21735(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    if (player.getX() <= 2693) {
      Tile tile = new Tile(2695, 9482, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(2693, 9482, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Stepping stone
  public static void mapObject21738(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2647, 9557, 0);
    player.getMovement().animatedTeleport(tile, 3067, null, 1);
  }

  // Stepping stone
  public static void mapObject21739(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2649, 9562, 0);
    player.getMovement().animatedTeleport(tile, 3067, null, 1);
  }

  // Portcullis
  public static void mapObject21772(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1239 && mapObject.getY() == 1225) {
      player.getMovement().teleport(1290, 1252);
    } else if (mapObject.getX() == 1367 && mapObject.getY() == 1225) {
      player.getMovement().teleport(1332, 1252);
    } else if (mapObject.getX() == 1303 && mapObject.getY() == 1289) {
      player.getMovement().teleport(1310, 1273);
    }
    player.getController().stopWithTeleport();
  }

  // Marv (ghastly!)
  public static void mapObject22499(Player player, int index, MapObject mapObject) {}

  // Marv (ghastly!)
  public static void mapObject22502(Player player, int index, MapObject mapObject) {}

  // Hank (ghastly!)
  public static void mapObject22506(Player player, int index, MapObject mapObject) {}

  // Hank (ghastly!)
  public static void mapObject22509(Player player, int index, MapObject mapObject) {}

  // Wilf (ghastly!)
  public static void mapObject22513(Player player, int index, MapObject mapObject) {}

  // Wilf (ghastly!)
  public static void mapObject22516(Player player, int index, MapObject mapObject) {}

  // Sarah (sick!)
  public static void mapObject22518(Player player, int index, MapObject mapObject) {}

  // Sarah (very sick!)
  public static void mapObject22519(Player player, int index, MapObject mapObject) {}

  // Sarah (ghastly!)
  public static void mapObject22520(Player player, int index, MapObject mapObject) {}

  // Sarah (sick!)
  public static void mapObject22521(Player player, int index, MapObject mapObject) {}

  // Sarah (very sick!)
  public static void mapObject22522(Player player, int index, MapObject mapObject) {}

  // Sarah (ghastly!)
  public static void mapObject22523(Player player, int index, MapObject mapObject) {}

  // Rachel (ghastly!)
  public static void mapObject22527(Player player, int index, MapObject mapObject) {}

  // Rachel (ghastly!)
  public static void mapObject22530(Player player, int index, MapObject mapObject) {}

  // Bone door
  public static void mapObject22945(Player player, int index, MapObject mapObject) {
    int entryRequirement = 0;
    if (mapObject.getX() == 2652 && mapObject.getY() == 5449) {
      entryRequirement = 1000000;
    } else if (mapObject.getX() == 2658 && mapObject.getY() == 5449) {
      entryRequirement = 5000000;
    } else if (mapObject.getX() == 2652 && mapObject.getY() == 5463) {
      entryRequirement = 10000000;
    } else if (mapObject.getX() == 2658 && mapObject.getY() == 5463) {
      entryRequirement = 25000000;
    } else if (mapObject.getX() == 2652 && mapObject.getY() == 5478) {
      entryRequirement = 50000000;
    } else if (mapObject.getX() == 2658 && mapObject.getY() == 5478) {
      entryRequirement = 100000000;
    } else if (mapObject.getX() == 2652 && mapObject.getY() == 5493) {
      entryRequirement = 250000000;
    } else if (mapObject.getX() == 2658 && mapObject.getY() == 5493) {
      entryRequirement = 500000000;
    }
    if (entryRequirement == 0) {
      return;
    }
    if (player.getX() > mapObject.getX() && mapObject.getDirection() == 3
        || player.getX() < mapObject.getX() && mapObject.getDirection() == 1) {
      if (player.getCombat().getRiskedValue() < entryRequirement) {
        player.getGameEncoder().sendMessage(
            "You need to risk at least " + PNumber.formatNumber(entryRequirement) + " to enter.");
        return;
      } else if (player.getInCombatDelay() > 0) {
        player.getGameEncoder().sendMessage("You can't use this yet.");
        return;
      }
      player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
      if (mapObject.getDirection() == 1) {
        player.getMovement().teleport(player.getX() + 2, player.getY());
      } else if (mapObject.getDirection() == 3) {
        player.getMovement().teleport(player.getX() - 2, player.getY());
      }
    } else {
      if (mapObject.getDirection() == 1) {
        player.getMovement().teleport(player.getX() - 2, player.getY());
      } else if (mapObject.getDirection() == 3) {
        player.getMovement().teleport(player.getX() + 2, player.getY());
      }
    }
  }

  // Ropeswing
  public static void mapObject23131(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 35) {
      player.getGameEncoder().sendMessage("You need an Agility level of 35 to use this course.");
      return;
    }
    if (player.getY() != 3554) {
      player.getGameEncoder().sendMessage("You'll need to get closer to make this jump.");
      return;
    }
    Tile startTile = new Tile(mapObject.getX(), 3554, mapObject.getHeight());
    Tile toTile = new Tile(mapObject.getX(), 3549, mapObject.getHeight());
    player.getMovement().clear();
    player.getMovement().addMovement(startTile);
    player.lock();
    PEvent event = new PEvent() {
      boolean reachedStartTile = false;
      int reachedStartTileTries = 0;

      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (!reachedStartTile) {
          if (player.getX() == startTile.getX() && player.getY() == startTile.getY()) {
            reachedStartTile = true;
            player.setAnimation(751);
            player.setForceMovement(new ForceMovement(player, 1, toTile, 2, Tile.SOUTH));
            super.setTick(1);
          } else if (reachedStartTileTries++ > 32) {
            player.unlock();
            super.stop();
          }
        } else {
          super.stop();
          player.getGameEncoder().sendMessage("You skillfully swing across.");
          player.getMovement().teleport(toTile);
          int xp = 22;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.putAttribute("agility_stage", 1);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Obstacle net
  public static void mapObject23134(Player player, int index, MapObject mapObject) {
    if (player.getY() != 3426) {
      return;
    }
    player.getGameEncoder().sendMessage("You climb the netting.");
    Tile toTile = new Tile(player.getX(), 3423, 1);
    player.getMovement().ladderUpTeleport(toTile);
    player.setFaceTile(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        int xp = 8;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (player.getAttributeInt("agility_stage") == 1) {
          player.putAttribute("agility_stage", 2);
        }
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Obstacle net
  public static void mapObject23135(Player player, int index, MapObject mapObject) {
    if (player.getY() != 3425) {
      return;
    }
    player.getGameEncoder().sendMessage("You climb the netting.");
    Tile currentTile = new Tile(player);
    Tile toTile = new Tile(player.getX(), player.getY() == 3425 ? 3427 : 3425, 0);
    player.setLock(5);
    player.getMovement().animatedTeleport(toTile, 3063, -2, null, null, 2);
    player.setFaceTile(toTile);
    PEvent event = new PEvent(PEvent.MILLIS_600) {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 2) {
          player.setFaceTile(currentTile);
        } else if (super.getExecutions() == 5) {
          super.stop();
          int xp = 8;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          if (player.getAttributeInt("agility_stage") == 5) {
            player.putAttribute("agility_stage", 6);
          }
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Obstacle pipe
  public static void mapObject23138(Player player, int index, MapObject mapObject) {
    if (player.getX() != mapObject.getX()) {
      return;
    }
    Tile toTile =
        new Tile(mapObject.getX(), mapObject.getY() == 3431 ? 3437 : 3430, mapObject.getHeight());
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(749);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    PEvent event = new PEvent(PEvent.MILLIS_600) {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 1) {
          player.getAppearance().setForceMoveAnimation(747);
        }
        if (player.getDistance(toTile) == 1) {
          player.getAppearance().setForceMoveAnimation(748);
        }
        if (player.getX() == toTile.getX() && player.getY() == toTile.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 8 + (player.getAttributeInt("agility_stage") == 6 ? 39 : 0);
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          if (player.getAttributeInt("agility_stage") == 6) {
            player.removeAttribute("agility_stage");
            // 102 laps an hour
            if (PRandom.randomE(50) == 0) {
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
            player.getFamiliar().rollSkillPet(Skills.AGILITY, 35609, 20659);
          }
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Obstacle pipe
  public static void mapObject23139(Player player, int index, MapObject mapObject) {
    if (player.getX() != mapObject.getX()) {
      return;
    }
    Tile toTile =
        new Tile(mapObject.getX(), mapObject.getY() == 3431 ? 3437 : 3430, mapObject.getHeight());
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(749);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    PEvent event = new PEvent(PEvent.MILLIS_600) {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 1) {
          player.getAppearance().setForceMoveAnimation(747);
        }
        if (player.getDistance(toTile) == 1) {
          player.getAppearance().setForceMoveAnimation(748);
        }
        if (player.getX() == toTile.getX() && player.getY() == toTile.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 8;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          if (player.getAttributeInt("agility_stage") == 6) {
            player.removeAttribute("agility_stage");
            xp = 39;
            if (player.getEquipment().wearingMinimumGraceful()) {
              xp *= 1.1;
            }
            player.getSkills().addXp(Skills.AGILITY, xp);
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
          }
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Log balance
  public static void mapObject23144(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 35) {
      player.getGameEncoder().sendMessage("You need an Agility level of 35 to use this course.");
      return;
    }
    player.lock();
    Tile startTile = new Tile(2551, mapObject.getY(), mapObject.getHeight());
    Tile toTile = new Tile(2541, mapObject.getY(), mapObject.getHeight());
    player.getMovement().clear();
    player.getMovement().addMovement(startTile);
    boolean running = player.getMovement().getRunning();
    final Tile finalTileStart = startTile;
    final Tile finalTileTo = toTile;
    PEvent event = new PEvent() {
      boolean reachedStartTile = false;

      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (!reachedStartTile) {
          if (player.getX() == finalTileStart.getX() && player.getY() == finalTileStart.getY()) {
            reachedStartTile = true;
            player.getGameEncoder().sendMessage("You walk carefully across the slippery log...");
            player.getAppearance().setForceMoveAnimation(762);
            player.getMovement().setRunning(false);
            player.getMovement().addMovement(toTile);
            super.setTick(1);
          }
        } else if (player.getX() == finalTileTo.getX() && player.getY() == finalTileTo.getY()) {
          super.stop();
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 14;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.getGameEncoder().sendMessage("... and make it safely to the other side.");
          player.putAttribute("agility_stage", 2);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Log balance
  public static void mapObject23145(Player player, int index, MapObject mapObject) {
    if (player.getX() != mapObject.getX()) {
      return;
    }
    player.getGameEncoder().sendMessage("You walk carefully across the slippery log...");
    Tile toTile = new Tile(mapObject.getX(), 3429, mapObject.getHeight());
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
          int xp = 8;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.getGameEncoder().sendMessage("... and make it safely to the other side.");
          player.putAttribute("agility_stage", 1);
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Cave
  public static void mapObject23157(Player player, int index, MapObject mapObject) {
    // Brine Rat Cavern exit
    Tile tile = new Tile(2729, 3734, 0);
    player.getMovement().ladderDownTeleport(tile);
  }

  // Wilderness Ditch
  public static void mapObject23271(Player player, int index, MapObject mapObject) {
    Tile tile;
    if (player.getY() < mapObject.getY()) {
      tile = new Tile(player.getX(), mapObject.getY() + 2, player.getHeight());
      Tile compareTile = new Tile(tile);
      compareTile.setY(compareTile.getY() + 8);
      if (!player.getController().canTeleport(compareTile, true)) {
        return;
      }
    } else {
      tile = new Tile(player.getX(), mapObject.getY() - 1, player.getHeight());
    }
    ForceMovement forceMovement = new ForceMovement(new Tile(player), 1, tile, 2,
        player.getY() < mapObject.getY() ? Tile.NORTH : Tile.SOUTH);
    player.setForceMovementTeleport(forceMovement, 6132, 1, null);
    player.clearHits();
    player.getMovement().setTeleportBlock(0);
  }

  // Balancing ledge
  public static void mapObject23547(Player player, int index, MapObject mapObject) {
    if (player.getY() != mapObject.getY()) {
      return;
    }
    player.getGameEncoder().sendMessage("You put your foot on the ledge and try to edge across...");
    Tile toTile = new Tile(2532, mapObject.getY(), mapObject.getHeight());
    boolean running = player.getMovement().getRunning();
    player.setAnimation(753);
    player.lock();
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (super.getExecutions() == 0) {
          player.getMovement().clear();
          player.getMovement().addMovement(toTile);
          player.getMovement().setRunning(false);
          player.getAppearance().setForceMoveAnimation(756);
        }
        if (player.getX() == toTile.getX() && player.getY() == toTile.getY()) {
          super.stop();
          player.setAnimation(759);
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          int xp = 22;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.getGameEncoder().sendMessage("You skillfully edge across the gap.");
          if (player.getAttributeInt("agility_stage") == 3) {
            player.putAttribute("agility_stage", 4);
          }
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Balancing rope
  public static void mapObject23557(Player player, int index, MapObject mapObject) {
    if (player.getX() != 2477 || player.getY() != 3420) {
      return;
    }
    player.getGameEncoder().sendMessage("You walk carefully across the rope...");
    Tile toTile = new Tile(2483, mapObject.getY(), mapObject.getHeight());
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
          int xp = 8;
          if (player.getEquipment().wearingMinimumGraceful()) {
            xp *= 1.1;
          }
          player.getSkills().addXp(Skills.AGILITY, xp);
          player.getGameEncoder().sendMessage("... and make it safely to the other side.");
          if (player.getAttributeInt("agility_stage") == 3) {
            player.putAttribute("agility_stage", 4);
          }
          player.unlock();
          AchievementDiary.agilityObstacleUpdate(player, mapObject);
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Tree branch
  public static void mapObject23559(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("You climb the tree...");
    Tile toTile = new Tile(2473, 3420, 2);
    player.getMovement().ladderUpTeleport(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.getGameEncoder().sendMessage("... to the platform above.");
        int xp = 5;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (player.getAttributeInt("agility_stage") == 2) {
          player.putAttribute("agility_stage", 3);
        }
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Tree branch
  public static void mapObject23560(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("You climb down the tree...");
    Tile toTile = new Tile(2487, 3421, 0);
    player.getMovement().ladderUpTeleport(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.getGameEncoder().sendMessage("... and land on the ground.");
        int xp = 5;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (player.getAttributeInt("agility_stage") == 4) {
          player.putAttribute("agility_stage", 5);
        }
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Tree branch
  public static void mapObject23561(Player player, int index, MapObject mapObject) {
    player.getGameEncoder().sendMessage("You climb down the tree...");
    Tile toTile = new Tile(2487, 3421, 0);
    player.getMovement().ladderUpTeleport(toTile);
    PEvent event = new PEvent(1) {
      @Override
      public void execute() {
        super.stop();
        if (!player.isVisible()) {
          return;
        }
        player.getGameEncoder().sendMessage("... and land on the ground.");
        int xp = 5;
        if (player.getEquipment().wearingMinimumGraceful()) {
          xp *= 1.1;
        }
        player.getSkills().addXp(Skills.AGILITY, xp);
        if (player.getAttributeInt("agility_stage") == 4) {
          player.putAttribute("agility_stage", 5);
        }
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Monkeybars
  public static void mapObject23566(Player player, int index, MapObject mapObject) {
    if (player.getController().isMagicBound()) {
      player.getGameEncoder()
          .sendMessage("A magical force stops you from moving for "
              + PTime
                  .tickToSec(player.getMovement().getMagicBindDelay() - Movement.MAGIC_REBIND_DELAY)
              + " more seconds.");
      return;
    }
    if (mapObject.getX() == 3120 && mapObject.getY() == 9964) {
      player.getMovement().teleport(3121, 9970);
    } else if (mapObject.getX() == 3120 && mapObject.getY() == 9969) {
      player.getMovement().teleport(3121, 9963);
    }
  }

  // Tunnel entrance
  public static void mapObject23609(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3509 && mapObject.getY() == 9497) {
      player.openDialogue("bossinstance", 13);
    }
  }

  // Box of Health
  public static void mapObject23709(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.setGraphic(436);
    player.getGameEncoder().sendMessage("The pool restores you.");
    player.rejuvenate();
  }

  // Furnace
  public static void mapObject24009(Player player, int index, MapObject mapObject) {
    Smithing.openSmelt(player);
  }

  // Staircase
  public static void mapObject24067(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2968 && mapObject.getY() == 3347) {
      player.getMovement().ladderUpTeleport(new Tile(2968, 3348, 1));
    }
  }

  // Staircase
  public static void mapObject24068(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2968 && mapObject.getY() == 3347) {
      player.getMovement().ladderDownTeleport(new Tile(2971, 3347));
    }
  }

  // Ladder
  public static void mapObject24070(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2996 && mapObject.getY() == 3341) {
      player.getMovement().ladderUpTeleport(new Tile(2995, 3341, 2));
    } else {
      player.getMovement()
          .ladderUpTeleport(new Tile(player.getX(), player.getY(), player.getHeight() + 1));
    }
  }

  // Ladder
  public static void mapObject24071(Player player, int index, MapObject mapObject) {
    player.getMovement()
        .ladderDownTeleport(new Tile(player.getX(), player.getY(), player.getHeight() - 1));
  }

  // Staircase
  public static void mapObject24072(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2954 && mapObject.getY() == 3338) {
      player.getMovement().ladderUpTeleport(new Tile(2956, 3338, 1));
    } else if (mapObject.getX() == 2960 && mapObject.getY() == 3338) {
      player.getMovement().ladderUpTeleport(new Tile(2959, 3339, 2));
    }
  }

  // Staircase
  public static void mapObject24074(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2955 && mapObject.getY() == 3338) {
      player.getMovement().ladderDownTeleport(new Tile(2956, 3338));
    } else if (mapObject.getX() == 2960 && mapObject.getY() == 3339) {
      player.getMovement().ladderDownTeleport(new Tile(2959, 3339, 1));
    }
  }

  // Staircase
  public static void mapObject24077(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2984 && mapObject.getY() == 3337) {
      player.getMovement().ladderUpTeleport(new Tile(2984, 3340, 2));
    }
  }

  // Staircase
  public static void mapObject24078(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2984 && mapObject.getY() == 3338) {
      player.getMovement().ladderDownTeleport(new Tile(2984, 3336, 1));
    }
  }

  // Crumbling wall
  public static void mapObject24222(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 5) {
      player.getGameEncoder().sendMessage("You need an Agility level of 5 to use this.");
      return;
    }
    if (player.getX() >= 2936) {
      player.getMovement().animatedTeleport(new Tile(2935, 3355), 3067, null, 1);
    } else {
      player.getMovement().animatedTeleport(new Tile(2936, 3355), 3067, null, 1);
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Staircase
  public static void mapObject24303(Player player, int index, MapObject mapObject) {
    Tile tile = new Tile(player.getX(), player.getY(), player.getHeight() - 1);
    player.getMovement().ladderUpTeleport(tile);
  }

  // (Warriors Guild) Door
  public static void mapObject24306(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2847 && (mapObject.getY() == 3540 || mapObject.getY() == 3541)) {
      if (player.getX() >= 2847) {
        player.getMovement().teleport(2846, 3540, player.getHeight());
      } else {
        if (player.getInventory().getCount(8851) < 100
            && !player.getEquipment().wearingAccomplishmentCape(Skills.ATTACK)) {
          player.getGameEncoder().sendMessage("You need atleast 100 tokens to enter.");
          return;
        }
        player.getMovement().teleport(2847, 3540, player.getHeight());
      }
    } else {
      Region.openDoors(player, mapObject);
    }
  }

  // (Warriors Guild) Door
  public static void mapObject24309(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2847 && (mapObject.getY() == 3540 || mapObject.getY() == 3541)) {
      if (player.getX() >= 2847) {
        player.getMovement().teleport(2846, 3540, player.getHeight());
      } else {
        if (player.getInventory().getCount(8851) < 100
            && !player.getEquipment().wearingAccomplishmentCape(Skills.ATTACK)) {
          player.getGameEncoder().sendMessage("You need atleast 100 tokens to enter.");
          return;
        }
        player.getMovement().teleport(2847, 3540, player.getHeight());
      }
    } else {
      Region.openDoors(player, mapObject);
    }
  }

  // Door
  public static void mapObject24318(Player player, int index, MapObject mapObject) {
    if (player.getController().getLevelForXP(Skills.ATTACK) == 99
        || player.getController().getLevelForXP(Skills.STRENGTH) == 99
        || player.getController().getLevelForXP(Skills.ATTACK)
            + player.getController().getLevelForXP(Skills.STRENGTH) >= 130) {
      player.getMovement().teleport(2876, 3546, 0);
    } else {
      player.getGameEncoder().sendMessage("You do not meet the requirements to enter.");
    }
  }

  // Fire rift
  public static void mapObject24971(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.FIRE);
  }

  // Earth rift
  public static void mapObject24972(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.EARTH);
  }

  // Body rift
  public static void mapObject24973(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.BODY);
  }

  // Cosmic rift
  public static void mapObject24974(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.COSMIC);
  }

  // Nature rift
  public static void mapObject24975(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.NATURE);
  }

  // Chaos rift
  public static void mapObject24976(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.CHAOS);
  }

  // Magical wheat
  public static void mapObject25016(Player player, int index, MapObject mapObject) {
    Tile toTile = null;
    if (mapObject.getDirection() == 0 || mapObject.getDirection() == 2) {
      if (player.getX() != mapObject.getX()) {
        return;
      }
      if (player.getY() > mapObject.getY()) {
        toTile = new Tile(mapObject.getX(), mapObject.getY() - 1, mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX(), mapObject.getY() + 1, mapObject.getHeight());
      }
    } else {
      if (player.getY() != mapObject.getY()) {
        return;
      }
      if (player.getX() > mapObject.getX()) {
        toTile = new Tile(mapObject.getX() - 1, mapObject.getY(), mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX() + 1, mapObject.getY(), mapObject.getHeight());
      }
    }
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(6593);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    final Tile finalTile = toTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == finalTile.getX() && player.getY() == finalTile.getY()) {
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          player.unlock();
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Magical wheat
  public static void mapObject25019(Player player, int index, MapObject mapObject) {
    Tile toTile = null;
    if (mapObject.getDirection() == 1 || mapObject.getDirection() == 3) {
      if (player.getX() != mapObject.getX()) {
        return;
      }
      if (player.getY() > mapObject.getY()) {
        toTile = new Tile(mapObject.getX(), mapObject.getY() - 1, mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX(), mapObject.getY() + 1, mapObject.getHeight());
      }
    } else {
      if (player.getY() != mapObject.getY()) {
        return;
      }
      if (player.getX() > mapObject.getX()) {
        toTile = new Tile(mapObject.getX() - 1, mapObject.getY(), mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX() + 1, mapObject.getY(), mapObject.getHeight());
      }
    }
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(6593);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    final Tile finalTile = toTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == finalTile.getX() && player.getY() == finalTile.getY()) {
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          player.unlock();
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Magical wheat
  public static void mapObject25020(Player player, int index, MapObject mapObject) {
    Tile toTile = null;
    if (mapObject.getDirection() == 1 || mapObject.getDirection() == 3) {
      if (player.getX() != mapObject.getX()) {
        return;
      }
      if (player.getY() > mapObject.getY()) {
        toTile = new Tile(mapObject.getX(), mapObject.getY() - 1, mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX(), mapObject.getY() + 1, mapObject.getHeight());
      }
    } else {
      if (player.getY() != mapObject.getY()) {
        return;
      }
      if (player.getX() > mapObject.getX()) {
        toTile = new Tile(mapObject.getX() - 1, mapObject.getY(), mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX() + 1, mapObject.getY(), mapObject.getHeight());
      }
    }
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(6593);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    final Tile finalTile = toTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == finalTile.getX() && player.getY() == finalTile.getY()) {
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          player.unlock();
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Magical wheat
  public static void mapObject25021(Player player, int index, MapObject mapObject) {
    Tile toTile = null;
    if (mapObject.getDirection() == 1 || mapObject.getDirection() == 3) {
      if (player.getX() != mapObject.getX()) {
        return;
      }
      if (player.getY() > mapObject.getY()) {
        toTile = new Tile(mapObject.getX(), mapObject.getY() - 1, mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX(), mapObject.getY() + 1, mapObject.getHeight());
      }
    } else {
      if (player.getY() != mapObject.getY()) {
        return;
      }
      if (player.getX() > mapObject.getX()) {
        toTile = new Tile(mapObject.getX() - 1, mapObject.getY(), mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX() + 1, mapObject.getY(), mapObject.getHeight());
      }
    }
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(6593);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    final Tile finalTile = toTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == finalTile.getX() && player.getY() == finalTile.getY()) {
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          player.unlock();
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Magical wheat
  public static void mapObject25029(Player player, int index, MapObject mapObject) {
    Tile toTile = null;
    if (player.getX() != mapObject.getX() && player.getY() != mapObject.getY()) {
      return;
    }
    if (player.getX() == mapObject.getX()) {
      if (player.getY() > mapObject.getY()) {
        toTile = new Tile(mapObject.getX(), mapObject.getY() - 1, mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX(), mapObject.getY() + 1, mapObject.getHeight());
      }
    } else if (player.getY() == mapObject.getY()) {
      if (player.getX() > mapObject.getX()) {
        toTile = new Tile(mapObject.getX() - 1, mapObject.getY(), mapObject.getHeight());
      } else {
        toTile = new Tile(mapObject.getX() + 1, mapObject.getY(), mapObject.getHeight());
      }
    } else {
      return;
    }
    player.getMovement().clear();
    player.getMovement().addMovement(toTile);
    player.getAppearance().setForceMoveAnimation(6593);
    boolean running = player.getMovement().getRunning();
    player.getMovement().setRunning(false);
    player.lock();
    final Tile finalTile = toTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.isVisible()) {
          super.stop();
          return;
        }
        if (player.getX() == finalTile.getX() && player.getY() == finalTile.getY()) {
          player.getAppearance().setForceMoveAnimation(-1);
          player.getMovement().setRunning(running);
          player.unlock();
          super.stop();
        }
      }
    };
    player.getWorld().addEvent(event);
  }

  // Law rift
  public static void mapObject25034(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.LAW);
  }

  // Death rift
  public static void mapObject25035(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.DEATH);
  }

  // Wall
  public static void mapObject25161(Player player, int index, MapObject mapObject) {
    if (player.getX() <= 2845) {
      player.getMovement().teleport(2847, 9636);
    } else {
      player.getMovement().teleport(2845, 9636);
    }
  }

  // Pyre site
  public static void mapObject25286(Player player, int index, MapObject mapObject) {
    if (player.getInventory().hasItem(11338)) {
      player.getInventory().deleteItem(11338, 1);
      if (PRandom.inRange(player.getCombat().getDropRate(11335, 0.8))) {
        player.getInventory().addItem(11335, 1);
        player.getGameEncoder()
            .sendMessage("You burn the chewed bones... And find a dragon full helm in the ashes!");
      } else {
        player.getGameEncoder()
            .sendMessage("You burn the chewed bones... All that remains is ash.");
      }
    }
  }

  // Stairs
  public static void mapObject25336(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(1768, 5366, 1));
  }

  // Stairs
  public static void mapObject25338(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(1772, 5366, 0));
  }

  // Stairs
  public static void mapObject25339(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(1778, 5343, 1));
  }

  // Stairs
  public static void mapObject25340(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(1778, 5346, 0));
  }

  // Water rift
  public static void mapObject25376(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.WATER);
  }

  // Soul rift
  public static void mapObject25377(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.SOUL);
  }

  // Air rift
  public static void mapObject25378(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.AIR);
  }

  // Mind rift
  public static void mapObject25379(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.MIND);
  }

  // Blood rift
  public static void mapObject25380(Player player, int index, MapObject mapObject) {
    Runecrafting.abyssTeleport(player, Runecrafting.Altar.BLOOD);
  }

  // Rock
  public static void mapObject26188(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(" pickaxe")) {
      player.getGameEncoder().sendMessage("You need a pickaxe to do this.");
      return;
    } else if (!PRandom.inRange(player.getController().getLevelForXP(Skills.MINING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to mine the rock.");
      return;
    }
    if (mapObject.getX() == 3026 && mapObject.getY() == 4813) {
      player.getMovement().teleport(3030, 4822);
    }
  }

  // Tendrils
  public static void mapObject26189(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(" axe")) {
      player.getGameEncoder().sendMessage("You need an axe to do this.");
      return;
    } else if (!PRandom.inRange(player.getController().getLevelForXP(Skills.WOODCUTTING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to chop the tendrils.");
      return;
    }
    if (mapObject.getX() == 3018 && mapObject.getY() == 4821) {
      player.getMovement().teleport(3029, 4824);
    }
  }

  // Boil
  public static void mapObject26190(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(590)) {
      player.getGameEncoder().sendMessage("You need a tinderbox to do this.");
      return;
    } else if (!PRandom.inRange(player.getController().getLevelForXP(Skills.FIREMAKING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to burn down the boil.");
      return;
    }
    if (mapObject.getX() == 3018 && mapObject.getY() == 4833) {
      player.getMovement().teleport(3025, 4833);
    }
  }

  // Eyes
  public static void mapObject26191(Player player, int index, MapObject mapObject) {
    if (!PRandom.inRange(player.getController().getLevelForXP(Skills.THIEVING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to distract the eyes.");
      return;
    }
    if (mapObject.getX() == 3021 && mapObject.getY() == 4842) {
      player.getMovement().teleport(3028, 4840);
    }
  }

  // Gap
  public static void mapObject26192(Player player, int index, MapObject mapObject) {
    if (!PRandom.inRange(player.getController().getLevelForXP(Skills.AGILITY) + 1)) {
      player.getGameEncoder().sendMessage("You fail to squeeze through the gap.");
      return;
    }
    if (mapObject.getX() == 3028 && mapObject.getY() == 4849) {
      player.getMovement().teleport(3032, 4843);
    }
  }

  // Passage
  public static void mapObject26208(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3038 && mapObject.getY() == 4853) {
      player.getMovement().teleport(3039, 4845);
    }
  }

  // Gap
  public static void mapObject26250(Player player, int index, MapObject mapObject) {
    if (!PRandom.inRange(player.getController().getLevelForXP(Skills.AGILITY) + 1)) {
      player.getGameEncoder().sendMessage("You fail to squeeze through the gap.");
      return;
    }
    if (mapObject.getX() == 3049 && mapObject.getY() == 4849) {
      player.getMovement().teleport(3047, 4843);
    }
  }

  // Eyes
  public static void mapObject26251(Player player, int index, MapObject mapObject) {
    if (!PRandom.inRange(player.getController().getLevelForXP(Skills.THIEVING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to distract the eyes.");
      return;
    }
    if (mapObject.getX() == 3058 && mapObject.getY() == 4839) {
      player.getMovement().teleport(3051, 4838);
    }
  }

  // Boil
  public static void mapObject26252(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(590)) {
      player.getGameEncoder().sendMessage("You need a tinderbox to do this.");
      return;
    } else if (!PRandom.inRange(player.getController().getLevelForXP(Skills.FIREMAKING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to burn down the boil.");
      return;
    }
    if (mapObject.getX() == 3060 && mapObject.getY() == 4830) {
      player.getMovement().teleport(3053, 4831);
    }
  }

  // Tendrils
  public static void mapObject26253(Player player, int index, MapObject mapObject) {
    if (!player.getInventory().hasItem(" axe")) {
      player.getGameEncoder().sendMessage("You need an axe to do this.");
      return;
    } else if (!PRandom.inRange(player.getController().getLevelForXP(Skills.WOODCUTTING) + 1)) {
      player.getGameEncoder().sendMessage("You fail to chop the tendrils.");
      return;
    }
    if (mapObject.getX() == 3057 && mapObject.getY() == 4821) {
      player.getMovement().teleport(3050, 4823);
    }
  }

  // Furnace
  public static void mapObject26300(Player player, int index, MapObject mapObject) {
    Smithing.openSmelt(player);
  }

  // Free-for-all portal
  public static void mapObject26645(Player player, int index, MapObject mapObject) {
    // player.openDialogue("freeforall", 0);
    player.getMovement().teleport(3327, 4752, 0);
    player.setController(new ClanWarsFreeForAllPC());
  }

  // Cave
  public static void mapObject26709(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(2429, 9824));
  }

  // Roots
  public static void mapObject26723(Player player, int index, MapObject mapObject) {
    // Stronghold Slayer Cave
    if (player.getX() >= 2393) {
      Tile tile = new Tile(2391, 9788, 0);
      player.getMovement().ladderUpTeleport(tile);
    } else {
      Tile tile = new Tile(2393, 9788, 0);
      player.getMovement().ladderUpTeleport(tile);
    }
  }

  // Duel arena viewing orb
  public static void mapObject26745(Player player, int index, MapObject mapObject) {
    player.getDuel().viewingDialogue(player);
  }

  // Wilderness Statistics
  public static void mapObject26756(Player player, int index, MapObject mapObject) {
    List<Player> players = new ArrayList<>(player.getWorld().getPlayers());
    Collections.sort(players, (p1, p2) -> {
      int i = Double.compare(p2.getCombat().getKDR(), p1.getCombat().getKDR());
      if (i != 0) {
        return i;
      }
      i = Integer.compare(p2.getCombat().getKillingSpree(), p1.getCombat().getKillingSpree());
      if (i != 0) {
        return i;
      }
      i = Integer.compare(p2.getCombat().getKills(), p1.getCombat().getKills());
      if (i != 0) {
        return i;
      }
      return Integer.compare(p1.getCombat().getDeaths(), p2.getCombat().getDeaths());
    });
    List<String> lines = new ArrayList<>();
    int rank = 1;
    for (int i = 0; i < players.size() && i < 50; i++) {
      Player p = players.get(i);
      if (p.getCombat().getKDR() == 0) {
        continue;
      }
      lines.add("[#" + rank + "] " + p.getUsername() + ": KDR: " + p.getCombat().getKDR()
          + "; Spree: " + p.getCombat().getKillingSpree());
      rank++;
    }
    Scroll.open(player, "Top Online PKers", lines);
  }

  // (Wilderness Resource Area) Gate
  public static void mapObject26760(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3184 && mapObject.getY() == 3944) {
      if (index == 0) {
        if (player.getY() == 3945) {
          player.openDialogue("wilderness", 0);
        } else {
          if (mapObject.isBusy() || player.getX() != mapObject.getX()
              || mapObject.getDirection() != 1) {
            return;
          }
          player.getMovement().clear();
          if (player.getY() == mapObject.getY()) {
            player.getMovement().addMovement(mapObject.getX(), mapObject.getY() + 1);
          } else {
            player.getMovement().addMovement(mapObject.getX(), mapObject.getY());
          }
          Region.openDoor(player, mapObject, 1, false, false);
        }
      } else if (index == 1) {
        int count = 0;
        for (Player player2 : player.getController().getPlayers()) {
          if (player2.inWildernessResourceArea()) {
            count++;
          }
        }
        if (count == 1) {
          player.getGameEncoder().sendMessage(
              "You peek inside the gate and see " + count + " adventurer inside the arena.");
        } else {
          player.getGameEncoder().sendMessage(
              "You peek inside the gate and see " + count + " adventurers inside the arena.");
        }
      }
    }
  }

  // Cavern
  public static void mapObject26762(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3231 && mapObject.getY() == 3951) {
      player.getMovement().teleport(3232, 10351);
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3241 && mapObject.getY() == 3949) {
      player.getMovement().teleport(3243, 10351);
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3231 && mapObject.getY() == 3936) {
      player.getMovement().teleport(3233, 10332);
      player.setObjectOptionDelay(4);
    }
  }

  // Crevice
  public static void mapObject26763(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3232 && mapObject.getY() == 10352) {
      player.getMovement().teleport(3232, 3950);
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3243 && mapObject.getY() == 10352) {
      player.getMovement().teleport(3242, 3948);
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3233 && mapObject.getY() == 10331) {
      player.getMovement().teleport(3232, 3938);
      player.setObjectOptionDelay(4);
    }
  }

  // Cave
  public static void mapObject26766(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3016 && mapObject.getY() == 3738) {
      player.getMovement().ladderDownTeleport(new Tile(3065, 10159, 3));
      player.setObjectOptionDelay(4);
    }
  }

  // Crevice
  public static void mapObject26767(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3065 && mapObject.getY() == 10160) {
      player.getMovement().ladderDownTeleport(new Tile(3017, 3740, 0));
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3049 && mapObject.getY() == 10165) {
      player.getMovement().ladderDownTeleport(new Tile(3034, 10158, 0));
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3066 && mapObject.getY() == 10142) {
      player.getMovement().ladderDownTeleport(new Tile(3062, 10130, 0));
      player.setObjectOptionDelay(4);
    }
  }

  // Jutting wall
  public static void mapObject26768(Player player, int index, MapObject mapObject) {
    if (player.getController().isMagicBound()) {
      player.getGameEncoder()
          .sendMessage("A magical force stops you from moving for "
              + PTime
                  .tickToSec(player.getMovement().getMagicBindDelay() - Movement.MAGIC_REBIND_DELAY)
              + " more seconds.");
      return;
    }
    if (mapObject.getX() == 3066 && mapObject.getY() == 10148) {
      if (player.getY() >= 10149) {
        player.getMovement().teleport(3066, 10147, 3);
      } else {
        player.getMovement().teleport(3066, 10149, 3);
      }
      player.setObjectOptionDelay(4);
    }
  }

  // Crevice
  public static void mapObject26769(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3035 && mapObject.getY() == 10158) {
      player.getMovement().ladderDownTeleport(new Tile(3050, 10165, 3));
      player.setObjectOptionDelay(4);
    } else if (mapObject.getX() == 3062 && mapObject.getY() == 10131) {
      player.getMovement().ladderDownTeleport(new Tile(3066, 10143, 3));
      player.setObjectOptionDelay(4);
    }
  }

  // Vine
  public static void mapObject26880(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2670, 9583, 2);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Vine
  public static void mapObject26882(Player player, int index, MapObject mapObject) {
    // Brimhaven Dungeon
    Tile tile = new Tile(2673, 9583, 0);
    player.getMovement().ladderUpTeleport(tile);
  }

  // Passage
  public static void mapObject27054(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(3039, 4800);
  }

  // Passage
  public static void mapObject27055(Player player, int index, MapObject mapObject) {
    if (player.getEquipment().getHandId() == 11095 || player.getEquipment().getHandId() == 11097
        || player.getEquipment().getHandId() == 11099 || player.getEquipment().getHandId() == 11101
        || player.getEquipment().getHandId() == 11103) {
      player.getEquipment().setItem(Equipment.Slot.HAND,
          player.getEquipment().getHandId() == 11103 ? null
              : new Item(player.getEquipment().getHandId() + 2, 1));
      player.getAppearance().setUpdate(true);
    } else {
      player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
    }
    player.getMovement().teleport(3039, 4805);
  }

  // <col=ffff00>The Overseer</col>
  public static void mapObject27057(Player player, int index, MapObject mapObject) {
    player.openDialogue("bossinstance", 11);
  }

  // Handholds
  public static void mapObject27362(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1459 && mapObject.getY() == 3690) {
      player.getMovement().ladderDownTeleport(new Tile(1454, 3690, 0));
    } else if (mapObject.getX() == 1455 && mapObject.getY() == 3690) {
      player.getMovement().ladderUpTeleport(new Tile(1460, 3690, 0));
    } else if (mapObject.getX() == 1471 && mapObject.getY() == 3687) {
      player.getMovement().ladderDownTeleport(new Tile(1476, 3687, 0));
    } else if (mapObject.getX() == 1475 && mapObject.getY() == 3687) {
      player.getMovement().ladderUpTeleport(new Tile(1470, 3687, 0));
    }
  }

  // Blood Altar
  public static void mapObject27978(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.BLOOD);
  }

  // Soul Altar
  public static void mapObject27980(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.SOUL);
  }

  // Rocks
  public static void mapObject27984(Player player, int index, MapObject mapObject) {
    Tile endTile = null;
    if (mapObject.getX() == 1743 && mapObject.getY() == 3854
        || mapObject.getX() == 1751 && mapObject.getY() == 3854) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 73) {
        player.getGameEncoder().sendMessage("You need an Agility level of 73 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1752 ? 1742 : 1752, 3854);
    } else if (mapObject.getX() == 1761 && mapObject.getY() == 3872
        || mapObject.getX() == 1761 && mapObject.getY() == 3873) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 69) {
        player.getGameEncoder().sendMessage("You need an Agility level of 69 to do this.");
        return;
      }
      endTile = new Tile(1761, player.getY() == 3871 ? 3874 : 3871);
    } else if (mapObject.getX() == 1770 && mapObject.getY() == 3849
        || mapObject.getX() == 1773 && mapObject.getY() == 3849) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 52) {
        player.getGameEncoder().sendMessage("You need an Agility level of 52 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1769 ? 1774 : 1769, 3849);
    }
    if (endTile == null) {
      return;
    }
    player.lock();
    player.getAppearance().setForceMoveAnimation(844);
    player.getMovement().clear();
    player.getMovement().addMovement(endTile);
    player.getMovement().setForceRunning(false);
    final Tile finalTile = endTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.matchesTile(finalTile)) {
          return;
        }
        super.stop();
        player.unlock();
        player.getAppearance().setForceMoveAnimation(-1);
        player.getMovement().setForceRunning(null);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Rocks
  public static void mapObject27985(Player player, int index, MapObject mapObject) {
    Tile endTile = null;
    if (mapObject.getX() == 1743 && mapObject.getY() == 3854
        || mapObject.getX() == 1751 && mapObject.getY() == 3854) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 73) {
        player.getGameEncoder().sendMessage("You need an Agility level of 73 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1752 ? 1742 : 1752, 3854);
    } else if (mapObject.getX() == 1761 && mapObject.getY() == 3872
        || mapObject.getX() == 1761 && mapObject.getY() == 3873) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 69) {
        player.getGameEncoder().sendMessage("You need an Agility level of 69 to do this.");
        return;
      }
      endTile = new Tile(1761, player.getY() == 3871 ? 3874 : 3871);
    } else if (mapObject.getX() == 1770 && mapObject.getY() == 3849
        || mapObject.getX() == 1773 && mapObject.getY() == 3849) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 52) {
        player.getGameEncoder().sendMessage("You need an Agility level of 52 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1769 ? 1774 : 1769, 3849);
    }
    if (endTile == null) {
      return;
    }
    player.lock();
    player.getAppearance().setForceMoveAnimation(844);
    player.getMovement().clear();
    player.getMovement().addMovement(endTile);
    player.getMovement().setForceRunning(false);
    final Tile finalTile = endTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.matchesTile(finalTile)) {
          return;
        }
        super.stop();
        player.unlock();
        player.getAppearance().setForceMoveAnimation(-1);
        player.getMovement().setForceRunning(null);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Rocks
  public static void mapObject27987(Player player, int index, MapObject mapObject) {
    Tile endTile = null;
    if (mapObject.getX() == 1743 && mapObject.getY() == 3854
        || mapObject.getX() == 1751 && mapObject.getY() == 3854) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 73) {
        player.getGameEncoder().sendMessage("You need an Agility level of 73 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1752 ? 1742 : 1752, 3854);
    } else if (mapObject.getX() == 1761 && mapObject.getY() == 3872
        || mapObject.getX() == 1761 && mapObject.getY() == 3873) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 69) {
        player.getGameEncoder().sendMessage("You need an Agility level of 69 to do this.");
        return;
      }
      endTile = new Tile(1761, player.getY() == 3871 ? 3874 : 3871);
    } else if (mapObject.getX() == 1770 && mapObject.getY() == 3849
        || mapObject.getX() == 1773 && mapObject.getY() == 3849) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 52) {
        player.getGameEncoder().sendMessage("You need an Agility level of 52 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1769 ? 1774 : 1769, 3849);
    }
    if (endTile == null) {
      return;
    }
    player.lock();
    player.getAppearance().setForceMoveAnimation(844);
    player.getMovement().clear();
    player.getMovement().addMovement(endTile);
    player.getMovement().setForceRunning(false);
    final Tile finalTile = endTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.matchesTile(finalTile)) {
          return;
        }
        super.stop();
        player.unlock();
        player.getAppearance().setForceMoveAnimation(-1);
        player.getMovement().setForceRunning(null);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Rocks
  public static void mapObject27988(Player player, int index, MapObject mapObject) {
    Tile endTile = null;
    if (mapObject.getX() == 1743 && mapObject.getY() == 3854
        || mapObject.getX() == 1751 && mapObject.getY() == 3854) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 73) {
        player.getGameEncoder().sendMessage("You need an Agility level of 73 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1752 ? 1742 : 1752, 3854);
    } else if (mapObject.getX() == 1761 && mapObject.getY() == 3872
        || mapObject.getX() == 1761 && mapObject.getY() == 3873) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 69) {
        player.getGameEncoder().sendMessage("You need an Agility level of 69 to do this.");
        return;
      }
      endTile = new Tile(1761, player.getY() == 3871 ? 3874 : 3871);
    } else if (mapObject.getX() == 1770 && mapObject.getY() == 3849
        || mapObject.getX() == 1773 && mapObject.getY() == 3849) {
      if (player.getSkills().getLevel(Skills.AGILITY) < 52) {
        player.getGameEncoder().sendMessage("You need an Agility level of 52 to do this.");
        return;
      }
      endTile = new Tile(player.getX() == 1769 ? 1774 : 1769, 3849);
    }
    if (endTile == null) {
      return;
    }
    player.lock();
    player.getAppearance().setForceMoveAnimation(844);
    player.getMovement().clear();
    player.getMovement().addMovement(endTile);
    player.getMovement().setForceRunning(false);
    final Tile finalTile = endTile;
    PEvent event = new PEvent() {
      @Override
      public void execute() {
        if (!player.matchesTile(finalTile)) {
          return;
        }
        super.stop();
        player.unlock();
        player.getAppearance().setForceMoveAnimation(-1);
        player.getMovement().setForceRunning(null);
        AchievementDiary.agilityObstacleUpdate(player, mapObject);
      }
    };
    player.getWorld().addEvent(event);
  }

  // Rope ladder
  public static void mapObject28857(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1575 && mapObject.getY() == 3483) {
      player.getMovement().ladderUpTeleport(new Tile(1574, 3483, 1));
    } else if (mapObject.getX() == 1575 && mapObject.getY() == 3493) {
      player.getMovement().ladderUpTeleport(new Tile(1574, 3493, 1));
    } else if (mapObject.getX() == 1566 && mapObject.getY() == 3483) {
      player.getMovement().ladderUpTeleport(new Tile(1567, 3483, 1));
    } else if (mapObject.getX() == 1566 && mapObject.getY() == 3493) {
      player.getMovement().ladderUpTeleport(new Tile(1567, 3493, 1));
    }
  }

  // Rope ladder
  public static void mapObject28858(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1575 && mapObject.getY() == 3483) {
      player.getMovement().ladderDownTeleport(new Tile(1576, 3483, 0));
    } else if (mapObject.getX() == 1575 && mapObject.getY() == 3493) {
      player.getMovement().ladderDownTeleport(new Tile(1576, 3493, 0));
    } else if (mapObject.getX() == 1566 && mapObject.getY() == 3483) {
      player.getMovement().ladderDownTeleport(new Tile(1565, 3483, 0));
    } else if (mapObject.getX() == 1566 && mapObject.getY() == 3493) {
      player.getMovement().ladderDownTeleport(new Tile(1565, 3493, 0));
    }
  }

  // Rocks
  public static void mapObject28890(Player player, int index, MapObject mapObject) {
    if (player.getY() >= 10040) {
      player.getMovement().teleport(1640, 10037);
    } else {
      player.getMovement().teleport(1640, 10040);
    }
  }

  // Crack
  public static void mapObject28892(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1706 && mapObject.getY() == 10077) {
      player.getMovement().teleport(1716, 10056);
    } else if (mapObject.getX() == 1716 && mapObject.getY() == 10057) {
      player.getMovement().teleport(1706, 10078);
    } else if (mapObject.getX() == 1648 && mapObject.getY() == 10008) {
      player.getMovement().teleport(1646, 10000);
    } else if (mapObject.getX() == 1646 && mapObject.getY() == 10001) {
      player.getMovement().teleport(1648, 10009);
    }
  }

  // Stone
  public static void mapObject28893(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1613 && mapObject.getY() == 10068) {
      player.getMovement().teleport(1610, 10062);
    } else if (mapObject.getX() == 1610 && mapObject.getY() == 10063) {
      player.getMovement().teleport(1613, 10069);
    }
  }

  // Altar
  public static void mapObject28900(Player player, int index, MapObject mapObject) {
    player.openDialogue("catacombsofkourend", 0);
  }

  // Ancient Altar
  public static void mapObject29147(Player player, int index, MapObject mapObject) {
    player.setAnimation(Prayer.PRAY_ANIMATION);
    if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC) {
      player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
      player.getGameEncoder().sendMessage("Your spellbook is now Ancients.");
    } else {
      player.getMagic().setSpellbook(Magic.STANDARD_MAGIC);
      player.getGameEncoder().sendMessage("Your spellbook is now normal.");
    }
  }

  // Lunar Altar
  public static void mapObject29148(Player player, int index, MapObject mapObject) {
    player.setAnimation(Prayer.PRAY_ANIMATION);
    if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC) {
      player.getMagic().setSpellbook(Magic.LUNAR_MAGIC);
      player.getGameEncoder().sendMessage("Your spellbook is now Lunars.");
    } else {
      player.getMagic().setSpellbook(Magic.STANDARD_MAGIC);
      player.getGameEncoder().sendMessage("Your spellbook is now normal.");
    }
  }

  // Altar of the Occult
  public static void mapObject29150(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    if (index == 0) {
      player.openDialogue("spellbooks", 0);
    } else if (index == 1) {
      if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC) {
        player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Ancient magic.");
      } else if (player.getMagic().getSpellbook() == Magic.ANCIENT_MAGIC
          || player.getMagic().getSpellbook() == Magic.LUNAR_MAGIC) {
        player.getMagic().setSpellbook(Magic.STANDARD_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Normal magic.");
      }
    } else if (index == 2) {
      if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC
          || player.getMagic().getSpellbook() == Magic.ANCIENT_MAGIC) {
        player.getMagic().setSpellbook(Magic.LUNAR_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Lunar magic.");
      } else if (player.getMagic().getSpellbook() == Magic.LUNAR_MAGIC) {
        player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
        player.getGameEncoder().sendMessage("You switch your spellbook to Ancient magic.");
      }
    }
  }

  // Ornate rejuvenation pool
  public static void mapObject29241(Player player, int index, MapObject mapObject) {
    if (player.getController().inPvPWorldCombat()) {
      player.getGameEncoder().sendMessage("You can't use this here.");
      return;
    }
    player.setGraphic(436);
    player.getGameEncoder().sendMessage("The pool restores you.");
    player.rejuvenate();
  }

  // crack
  public static void mapObject29626(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(3055, 5585);
  }

  // crack
  public static void mapObject29627(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(3052, 5587);
  }

  // runecrafting altar
  public static void mapObject29631(Player player, int index, MapObject mapObject) {
    Runecrafting.ouraniaCraft(player);
  }

  // ladder
  public static void mapObject29635(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2452 && mapObject.getY() == 3231) {
      player.getMovement().ladderDownTeleport(new Tile(3015, 5629));
    }
  }

  // Steps
  public static void mapObject29993(Player player, int index, MapObject mapObject) {
    if (player.getY() >= 9991) {
      player.getMovement().teleport(2703, 9987);
    } else {
      player.getMovement().teleport(2703, 9992);
    }
  }

  // Chambers of Xeric
  public static void mapObject29777(Player player, int index, MapObject mapObject) {
    player.openDialogue("raids", 0);
  }

  // Tunnel
  public static void mapObject30174(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 72) {
      player.getGameEncoder().sendMessage("You need an Agility level of 72 to use this.");
      return;
    }
    if (mapObject.getX() == 2430 && mapObject.getY() == 9806) {
      player.getMovement().ladderUpTeleport(new Tile(2435, 9806, 0));
    } else if (mapObject.getX() == 2434 && mapObject.getY() == 9806) {
      player.getMovement().ladderUpTeleport(new Tile(2429, 9806, 0));
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Tunnel
  public static void mapObject30175(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 72) {
      player.getGameEncoder().sendMessage("You need an Agility level of 72 to use this.");
      return;
    }
    if (mapObject.getX() == 2430 && mapObject.getY() == 9807) {
      player.getMovement().ladderUpTeleport(new Tile(2435, 9807, 0));
    } else if (mapObject.getX() == 2434 && mapObject.getY() == 9807) {
      player.getMovement().ladderUpTeleport(new Tile(2429, 9807, 0));
    }
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // Steps
  public static void mapObject30189(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2881 && mapObject.getY() == 9825) {
      // Taverley Dungeon
      player.getMovement().ladderUpTeleport(new Tile(2880, 9825, 1));
    } else if (mapObject.getX() == 2904 && mapObject.getY() == 9813) {
      // Taverley Dungeon
      player.getMovement().ladderUpTeleport(new Tile(2903, 9813, 1));
    }
  }

  // Steps
  public static void mapObject30190(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2881 && mapObject.getY() == 9825) {
      // Taverley Dungeon
      player.getMovement().ladderDownTeleport(new Tile(2883, 9825, 0));
    } else if (mapObject.getX() == 2904 && mapObject.getY() == 9813) {
      // Taverley Dungeon
      player.getMovement().ladderDownTeleport(new Tile(2906, 9813, 0));
    }
  }

  // Ladder
  public static void mapObject30191(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(3412, 9932, 3));
  }

  // Ladder
  public static void mapObject30192(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(3417, 3536, 0));
  }

  // Crevice
  public static void mapObject30198(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 2696 && mapObject.getY() == 9436) {
      // Brimhaven Dungeon
      player.getMovement().ladderDownTeleport(new Tile(2684, 9436, 0));
    } else if (mapObject.getX() == 2685 && mapObject.getY() == 9436) {
      // Brimhaven Dungeon
      player.getMovement().ladderDownTeleport(new Tile(2697, 9436, 0));
    }
  }

  // Lift
  public static void mapObject30258(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1437 && mapObject.getY() == 10094) {
      player.getMovement().ladderUpTeleport(new Tile(1437, 10093, 2));
    } else if (mapObject.getX() == 1452 && mapObject.getY() == 10068) {
      player.getMovement().ladderUpTeleport(new Tile(1451, 10069, 2));
    } else if (mapObject.getX() == 1458 && mapObject.getY() == 10095) {
      player.getMovement().ladderUpTeleport(new Tile(1457, 10095, 1));
    }
  }

  // Lift
  public static void mapObject30259(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1437 && mapObject.getY() == 10094) {
      player.getMovement().ladderUpTeleport(new Tile(1437, 10093, 3));
    } else if (mapObject.getX() == 1452 && mapObject.getY() == 10068) {
      player.getMovement().ladderUpTeleport(new Tile(1451, 10069, 3));
    } else if (mapObject.getX() == 1458 && mapObject.getY() == 10095) {
      player.getMovement().ladderUpTeleport(new Tile(1457, 10095, 2));
    }
  }

  // cave exit
  public static void mapObject30283(Player player, int index, MapObject mapObject) {
    player.getCombat().getTzHaar().exitInferno();
  }

  // the inferno
  public static void mapObject30352(Player player, int index, MapObject mapObject) {
    if (player.getCombat().getTzHaar().getInfernoSacrificedCape()) {
      player.openDialogue("tzhaar", 4);
    } else {
      player.openDialogue("tzhaar", 5);
    }
  }

  // row boat
  public static void mapObject30376(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2614, 3440);
  }

  // row boat
  public static void mapObject30377(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(2600, 3425);
  }

  // lizardman lair
  public static void mapObject30380(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderDownTeleport(new Tile(1305, 9973, 0));
  }

  // crevice
  public static void mapObject30381(Player player, int index, MapObject mapObject) {
    player.getMovement().ladderUpTeleport(new Tile(1309, 3574, 0));
  }

  // crevice
  public static void mapObject30382(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1305 && mapObject.getY() == 9954) {
      player.getMovement().teleport(1305, 9957);
    } else if (mapObject.getX() == 1318 && mapObject.getY() == 9957) {
      player.getMovement().teleport(1318, 9960);
    }
  }

  // crevice
  public static void mapObject30383(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1305 && mapObject.getY() == 9956) {
      player.getMovement().teleport(1305, 9953);
    } else if (mapObject.getX() == 1318 && mapObject.getY() == 9959) {
      player.getMovement().teleport(1318, 9956);
    }
  }

  // crevice
  public static void mapObject30384(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1296 && mapObject.getY() == 9959) {
      player.getMovement().teleport(1299, 9959);
    } else if (mapObject.getX() == 1320 && mapObject.getY() == 9966) {
      player.getMovement().teleport(1323, 9966);
    }
  }

  // crevice
  public static void mapObject30385(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 1298 && mapObject.getY() == 9959) {
      player.getMovement().teleport(1295, 9959);
    } else if (mapObject.getX() == 1322 && mapObject.getY() == 9966) {
      player.getMovement().teleport(1319, 9966);
    }
  }

  // thick vine
  public static void mapObject30646(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3678 && mapObject.getY() == 3743) {
      if (player.getX() <= 3677) {
        player.getMovement().teleport(3680, 3743);
      } else {
        player.getMovement().teleport(3677, 3743);
      }
    }
  }

  // thick vines
  public static void mapObject30648(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3669 && mapObject.getY() == 3746) {
      if (player.getY() >= 3748) {
        player.getMovement().teleport(3671, 3745);
      } else {
        player.getMovement().teleport(3669, 3748);
      }
    } else if (mapObject.getX() == 3671 && mapObject.getY() == 3760) {
      if (player.getY() <= 3760) {
        player.getMovement().teleport(3672, 3762);
      } else {
        player.getMovement().teleport(3670, 3760);
      }
    } else if (mapObject.getX() == 3672 && mapObject.getY() == 3764) {
      if (player.getY() <= 3763) {
        player.getMovement().teleport(3674, 3765);
      } else {
        player.getMovement().teleport(3672, 3763);
      }
    }
  }

  // stairs
  public static void mapObject30847(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(3633, 10260);
  }

  // stairs
  public static void mapObject30849(Player player, int index, MapObject mapObject) {
    player.getMovement().teleport(3633, 10264);
  }

  // rope anchor
  public static void mapObject30916(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 64) {
      player.getGameEncoder().sendMessage("You need an Agility level of 64 to do this.");
      return;
    }
    player.getMovement().teleport(3784, 3821);
  }

  // rope anchor
  public static void mapObject30917(Player player, int index, MapObject mapObject) {
    if (player.getSkills().getLevel(Skills.AGILITY) < 64) {
      player.getGameEncoder().sendMessage("You need an Agility level of 64 to do this.");
      return;
    }
    player.getMovement().teleport(3778, 3821);
  }

  // chest pieces
  public static void mapObject31427(Player player, int index, MapObject mapObject) {
    player.getBank().open();
  }

  // cavern
  public static void mapObject31555(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3073 && mapObject.getY() == 3654) {
      player.getMovement().teleport(3196, 10056);
      player.setObjectOptionDelay(2);
    }
  }

  // cavern
  public static void mapObject31556(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3124 && mapObject.getY() == 3831) {
      player.getMovement().teleport(3241, 10234);
      player.setObjectOptionDelay(2);
    }
  }

  // opening
  public static void mapObject31557(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3194 && mapObject.getY() == 10055) {
      player.getMovement().teleport(3075, 3653);
      player.setObjectOptionDelay(2);
    }
  }

  // opening
  public static void mapObject31558(Player player, int index, MapObject mapObject) {
    if (mapObject.getX() == 3240 && mapObject.getY() == 10235) {
      player.getMovement().teleport(3126, 3832);
      player.setObjectOptionDelay(2);
    }
  }

  // pillar
  public static void mapObject31561(Player player, int index, MapObject mapObject) {
    Tile tile = null;
    int direction = Tile.NORTH;
    int level = 1;
    if (mapObject.getX() == 3241 && mapObject.getY() == 10145) {
      tile = player.getX() >= 3243 ? new Tile(3239, 10145) : new Tile(3243, 10145);
      direction = player.getX() >= 3243 ? Tile.WEST : Tile.EAST;
      level = 89;
    } else if (mapObject.getX() == 3200 && mapObject.getY() == 10136) {
      tile = player.getX() >= 3202 ? new Tile(3198, 10136) : new Tile(3202, 10136);
      direction = player.getX() >= 3202 ? Tile.WEST : Tile.EAST;
      level = 75;
    } else if (mapObject.getX() == 3220 && mapObject.getY() == 10086) {
      tile = player.getY() >= 10088 ? new Tile(3220, 10084) : new Tile(3220, 10088);
      direction = player.getY() >= 10088 ? Tile.SOUTH : Tile.NORTH;
      level = 65;
    } else if (mapObject.getX() == 3202 && mapObject.getY() == 10196) {
      tile = player.getX() >= 3204 ? new Tile(3200, 10196) : new Tile(3204, 10196);
      direction = player.getX() >= 3204 ? Tile.WEST : Tile.EAST;
      level = 75;
    } else if (mapObject.getX() == 3180 && mapObject.getY() == 10209) {
      tile = player.getY() <= 10207 ? new Tile(3180, 10211) : new Tile(3180, 10207);
      direction = player.getY() <= 10207 ? Tile.NORTH : Tile.SOUTH;
      level = 75;
    }
    if (tile == null) {
      return;
    } else if (player.getSkills().getLevel(Skills.AGILITY) < level) {
      player.getGameEncoder()
          .sendMessage("You need an Agility level of " + level + " to jump this.");
      return;
    }
    player.setObjectOptionDelay(2);
    tile.setHeight(player.getHeight());
    ForceMovement forceMovement = new ForceMovement(new Tile(player), 1, tile, 2, direction);
    player.setForceMovementTeleport(forceMovement, 6132, 1, null);
    AchievementDiary.agilityObstacleUpdate(player, mapObject);
  }

  // roof exit
  public static void mapObject31674(Player player, int index, MapObject mapObject) {
    player.getController().stopWithTeleport();
    player.getMovement().teleport(3426, 3540, 2);
  }

  // energy sphere
  public static void mapObject31678(Player player, int index, MapObject mapObject) {
    if (mapObject.getAttachment() != null) {
      var tmo = (PEvent) mapObject.getAttachment();
      tmo.setAttachment(2, player);
      tmo.stop();
    }
  }

  // energy sphere
  public static void mapObject31679(Player player, int index, MapObject mapObject) {
    if (mapObject.getAttachment() != null) {
      var tmo = (PEvent) mapObject.getAttachment();
      tmo.setAttachment(2, player);
      tmo.stop();
    }
  }

  // energy sphere
  public static void mapObject31680(Player player, int index, MapObject mapObject) {
    if (mapObject.getAttachment() != null) {
      var tmo = (PEvent) mapObject.getAttachment();
      tmo.setAttachment(2, player);
      tmo.stop();
    }
  }

  // ice chunks
  public static void mapObject31990(Player player, int index, MapObject mapObject) {
    if (player.getY() <= 4052) {
      player.setController(new BossInstancePC());
      player.getController().instance();
      player.getMovement().teleport(2272, 4054);
      player.getController().getVariable("boss_instance_vorkath");
    } else {
      player.getController().stop();
    }
  }

  // barrier
  public static void mapObject32153(Player player, int index, MapObject mapObject) {
    if (mapObject.getDirection() == 1) {
      if (player.getX() > mapObject.getX()) {
        player.openDialogue("lithkrenvault", 0);
      } else {
        player.getMovement().teleport(1562, 5074);
      }
    } else if (mapObject.getDirection() == 3) {
      if (player.getX() < mapObject.getX()) {
        player.openDialogue("lithkrenvault", 1);
      } else {
        player.getMovement().teleport(1573, 5074);
      }
    }
  }

  // altar
  public static void mapObject32492(Player player, int index, MapObject mapObject) {
    Runecrafting.craftRunes(player, Runecrafting.Altar.WRATH);
  }
}
