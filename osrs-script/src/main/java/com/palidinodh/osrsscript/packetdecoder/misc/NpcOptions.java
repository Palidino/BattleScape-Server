package com.palidinodh.osrsscript.packetdecoder.misc;

import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Movement;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Equipment;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.PCombat;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Teleports;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.adaptive.GrandExchangeUser;
import com.palidinodh.util.PTime;

public class NpcOptions {
  // Piles
  public static void npc13(Player player, int index, Npc npc) {
    player.getGameEncoder().sendMessage("Use items on Piles to note them.");
  }

  // Adam
  public static void npc311(Player player, int index, Npc npc) {
    if (index == 0) {

      if (player.isGameModeIronman() || player.isGameModeGroupIronman()) {
        player.openDialogue("ironadam", 0);
      } else {
        player.getGameEncoder().sendMessage("Adam has no reason to talk to you.");
      }

    } else if (index == 2) {
      if (player.isGameModeIronman() || player.isGameModeGroupIronman()) {
        player.openShop("ironman");
      } else if (player.isGameModeHardcoreIronman()) {
        player.openShop("hardcore_ironman");
      } else {
        player.getGameEncoder().sendMessage("Adam has no reason to trade you.");
      }
    }
  }

  // Sarah
  public static void npc501(Player player, int index, Npc npc) {
    player.openShop("skilling");
  }

  // Shop keeper
  public static void npc506(Player player, int index, Npc npc) {
    player.openShop("equipment");
  }

  // Oziach
  public static void npc822(Player player, int index, Npc npc) {
    player.openShop("skill_exchange");
  }

  // Monk of Entrana
  public static void npc1166(Player player, int index, Npc npc) {
    for (int i = 0; i < player.getInventory().size(); i++) {
      int itemId = player.getInventory().getId(i);
      if (itemId == -1 || ItemDef.getEquipSlot(itemId) == null) {
        continue;
      }
      boolean isAllowed =
          ItemDef.getName(itemId).contains("arrow") || itemId == 3840 || itemId == 3842
              || itemId == 3844 || itemId == 12608 || itemId == 12610 || itemId == 12612;
      if (isAllowed) {
        continue;
      }
      player.getGameEncoder()
          .sendMessage("You can't take " + ItemDef.getName(itemId) + " to Entrana.");
      return;
    }
    for (int i = 0; i < player.getEquipment().size(); i++) {
      int itemId = player.getEquipment().getId(i);
      if (itemId == -1 || ItemDef.getEquipSlot(itemId) == null) {
        continue;
      }
      boolean isAllowed =
          ItemDef.getName(itemId).contains("arrow") || itemId == 3840 || itemId == 3842
              || itemId == 3844 || itemId == 12608 || itemId == 12610 || itemId == 12612;
      if (isAllowed) {
        continue;
      }
      player.getGameEncoder()
          .sendMessage("You can't take " + ItemDef.getName(itemId) + " to Entrana.");
      return;
    }
    player.getMovement().teleport(2834, 3335);
  }

  // Monk of Entrana
  public static void npc1167(Player player, int index, Npc npc) {
    for (int i = 0; i < player.getInventory().size(); i++) {
      int itemId = player.getInventory().getId(i);
      if (itemId == -1 || ItemDef.getEquipSlot(itemId) == null) {
        continue;
      }
      boolean isAllowed =
          ItemDef.getName(itemId).contains("arrow") || itemId == 3840 || itemId == 3842
              || itemId == 3844 || itemId == 12608 || itemId == 12610 || itemId == 12612;
      if (isAllowed) {
        continue;
      }
      player.getGameEncoder()
          .sendMessage("You can't take " + ItemDef.getName(itemId) + " to Entrana.");
      return;
    }
    for (int i = 0; i < player.getEquipment().size(); i++) {
      int itemId = player.getEquipment().getId(i);
      if (itemId == -1 || ItemDef.getEquipSlot(itemId) == null) {
        continue;
      }
      boolean isAllowed =
          ItemDef.getName(itemId).contains("arrow") || itemId == 3840 || itemId == 3842
              || itemId == 3844 || itemId == 12608 || itemId == 12610 || itemId == 12612;
      if (isAllowed) {
        continue;
      }
      player.getGameEncoder()
          .sendMessage("You can't take " + ItemDef.getName(itemId) + " to Entrana.");
      return;
    }
    player.getMovement().teleport(2834, 3335);
  }

  // Hunting expert
  public static void npc1504(Player player, int index, Npc npc) {
    if (npc.getX() == 3508 && npc.getY() == 3479) {
      player.getMovement().animatedTeleport(new Tile(3530, 3444),
          Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
          Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
    } else if (npc.getX() == 2644 && npc.getY() == 3662) {
      player.getMovement().animatedTeleport(new Tile(2720, 3781),
          Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
          Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
    } else {
      player.openShop("skilling");
    }
  }
  /* 1536 - 1791 */

  // Gundai
  public static void npc1600(Player player, int index, Npc npc) {
    player.getBank().open();
  }

  // Lundail
  public static void npc1601(Player player, int index, Npc npc) {
    if (index == 2) {
      player.openShop("wild_runes");
    }
  }

  // Kolodion
  public static void npc1603(Player player, int index, Npc npc) {
    player.openDialogue("kolodion", 0);
  }

  // Baby impling
  public static void npc1635(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Young impling
  public static void npc1636(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Gourmet impling
  public static void npc1637(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Earth impling
  public static void npc1638(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Essence impling
  public static void npc1639(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Eclectic impling
  public static void npc1640(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Nature impling
  public static void npc1641(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Magpie impling
  public static void npc1642(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Ninja impling
  public static void npc1643(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Dragon impling
  public static void npc1644(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Baby impling
  public static void npc1645(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Young impling
  public static void npc1646(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Gourmet impling
  public static void npc1647(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Earth impling
  public static void npc1648(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Essence impling
  public static void npc1649(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Eclectic impling
  public static void npc1650(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Nature impling
  public static void npc1651(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Magpie impling
  public static void npc1652(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Ninja impling
  public static void npc1653(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Dragon impling
  public static void npc1654(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Void Knight
  public static void npc1755(Player player, int index, Npc npc) {
    player.openShop("pest_control");
  }

  // Squire
  public static void npc1763(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("bank", 1);
    } else if (index == 2) {
      player.getBank().open();
    }
  }

  // Squire
  public static void npc1764(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("bank", 1);
    } else if (index == 2) {
      player.getBank().open();
    }
  }

  // Squire
  public static void npc1765(Player player, int index, Npc npc) {
    player.openShop("equipment");
  }

  // Squire
  public static void npc1766(Player player, int index, Npc npc) {
    player.openShop("equipment");
  }

  // Squire
  public static void npc1767(Player player, int index, Npc npc) {
    player.openShop("equipment");
  }

  // Squire
  public static void npc1768(Player player, int index, Npc npc) {
    player.openShop("equipment");
  }

  // Squire
  public static void npc1769(Player player, int index, Npc npc) {
    if (player.getController().getExitTile() != null) {
      player.getController().stop();
    }
  }

  // Grand Exchange Clerk
  public static void npc2148(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("grandexchange", 0);
    } else if (index == 2) {
      player.getGrandExchange().open();
    } else if (index == 3) {
      player.getGrandExchange().openHistory(GrandExchangeUser.HISTORY);
    } else if (index == 4) {
      player.getGrandExchange().exchangeItemSets();
    }
  }

  // Grand Exchange Clerk
  public static void npc2149(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("grandexchange", 0);
    } else if (index == 2) {
      player.getGrandExchange().open();
    } else if (index == 3) {
      player.getGrandExchange().openHistory(GrandExchangeUser.HISTORY);
    } else if (index == 4) {
      player.getGrandExchange().exchangeItemSets();
    }
  }

  // TzHaar-Mej-Jal
  public static void npc2180(Player player, int index, Npc npc) {
    player.openDialogue("tzhaar", 1);
  }

  // TzHaar-Ket-Zuh
  public static void npc2182(Player player, int index, Npc npc) {
    player.getBank().open();
  }

  // TzHaar-Hur-Tel
  public static void npc2183(Player player, int index, Npc npc) {
    player.openShop("tzhaar_equipment");
  }

  // TzHaar-Hur-Lek
  public static void npc2184(Player player, int index, Npc npc) {
    player.openShop("tzhaar_rocks");
  }

  // TzHaar-Mej-Roh
  public static void npc2185(Player player, int index, Npc npc) {
    player.openShop("tzhaar_runes");
  }

  // Richard
  public static void npc2200(Player player, int index, Npc npc) {
    player.openShop("teamcapes");
  }

  // Lidio
  public static void npc2469(Player player, int index, Npc npc) {
    player.openShop("warriors_guild_food");
  }

  // Lilly
  public static void npc2470(Player player, int index, Npc npc) {
    player.openShop("warriors_guild_potions");
  }

  // Anton
  public static void npc2471(Player player, int index, Npc npc) {
    player.openShop("warriors_guild_armour");
  }
  /* 2560 - 2815 */

  // Mage of Zamorak
  public static void npc2581(Player player, int index, Npc npc) {
    if (index == 0 || index == 2) {
      player.openShop("wild_runes");
    } else if (index == 3) {
      if (!player.getController().canTeleport(true)) {
        return;
      }
      if (player.getEquipment().getHandId() == 11095 || player.getEquipment().getHandId() == 11097
          || player.getEquipment().getHandId() == 11099
          || player.getEquipment().getHandId() == 11101
          || player.getEquipment().getHandId() == 11103) {
        player.getEquipment().setItem(Equipment.Slot.HAND,
            player.getEquipment().getHandId() == 11103 ? null
                : new Item(player.getEquipment().getHandId() + 2, 1));
        player.getAppearance().setUpdate(true);
      } else {
        player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
      }
      npc.setForceMessage("Veniens! Sallakar! Rinnesset!");
      npc.setAnimation(1818);
      npc.setGraphic(343);
      Tile[] tiles = new Tile[] {new Tile(3045, 4810), new Tile(3059, 4818), new Tile(3062, 4835),
          new Tile(3054, 4850), new Tile(3043, 4854), new Tile(3027, 4851), new Tile(3017, 4840),
          new Tile(3015, 4826), new Tile(3021, 4813), new Tile(3035, 4809)};
      player.getMovement().animatedTeleport(PRandom.arrayRandom(tiles), 1816, 715, new Graphic(342),
          null, 2);
      player.getController().stopWithTeleport();
      player.clearHits();
    }
  }

  // Head chef
  public static void npc2658(Player player, int index, Npc npc) {
    player.openShop("equipment");
  }

  // Elstan
  public static void npc2663(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Dantaera
  public static void npc2664(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Kragen
  public static void npc2665(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Lyra
  public static void npc2666(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Francis
  public static void npc2667(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Garth
  public static void npc2669(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Ellena
  public static void npc2670(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Selena
  public static void npc2671(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Vasquen
  public static void npc2672(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Rhonen
  public static void npc2673(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Dreven
  public static void npc2674(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Taria
  public static void npc2675(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Rhazien
  public static void npc2676(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Torrell
  public static void npc2677(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Alain
  public static void npc2678(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Heskel
  public static void npc2679(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Treznor
  public static void npc2680(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Fayeth
  public static void npc2681(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Bolongo
  public static void npc2682(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Gileth
  public static void npc2683(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Frizzy Skernip
  public static void npc2684(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Yulf Squecks
  public static void npc2685(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Praistan Ebola
  public static void npc2686(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Prissy Scilla
  public static void npc2687(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Imiago
  public static void npc2688(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Liliwen
  public static void npc2689(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Martin Thwait
  public static void npc3193(Player player, int index, Npc npc) {
    player.openShop("thieving_stalls");
  }

  // Wyson the gardener
  public static void npc3253(Player player, int index, Npc npc) {
    int[] moleItemIds = new int[] {7416, 7417, 7418, 7419};
    for (int itemId : moleItemIds) {
      int count = Math.min(player.getInventory().getCount(itemId),
          player.getInventory().getRemainingSlots());
      player.getInventory().deleteItem(itemId, count);
      for (int i = 0; i < count; i++) {
        if (PRandom.randomE(10) == 0) {
          player.getInventory().addItem(5075, 1);
        } else if (PRandom.randomE(5) == 0) {
          player.getInventory().addItem(5074, 1);
        } else {
          player.getInventory().addItem(7413, 1);
        }
      }
    }
  }

  // Innocent-looking key
  public static void npc3619(Player player, int index, Npc npc) {
    if (player.getCombat().getHauntedMine() < 2
        || player.getWorld().getTargetNPC(3616, player) != null) {
      player.getGameEncoder().sendMessage("Nothing interesting happens.");
      return;
    } else if (player.getCombat().getHauntedMine() == 2) {
      Npc treus = new Npc(player.getController(), 3616, new Tile(2788, 4457, player.getHeight()));
      treus.getCombat().setTarget(player);
    } else if (player.getCombat().getHauntedMine() >= 3) {
      player.getInventory().addItem(4077, 1);
    }
  }

  // Ungadulu
  public static void npc3957(Player player, int index, Npc npc) {
    if (player.getCombat().getLegendsQuest() == 0 && player.carryingItem(730)
        && player.getWorld().getTargetNPC(3962, player) == null) {
      player.getGameEncoder()
          .sendMessage("You open the book and a light starts emanating, illuminating Ungadulu.");
      player.getPrayer().adjustPoints(-99);
      Npc nezikchened =
          new Npc(player.getController(), 3962, new Tile(2792, 9328, player.getHeight()));
      nezikchened.setForceMessage("Your faith will help you little here.");
      nezikchened.getCombat().setTarget(player);
    } else if (player.getCombat().getLegendsQuest() == 2 && player.carryingItem(746)) {
      player.getInventory().deleteItem(746, 1);
      player.getInventory().addItem(748, 1);
      player.getMovement().teleport(2792, 9337, 0);
      player.getGameEncoder()
          .sendMessage("Ungadulu gives you a water purifying spell in exchange for the dagger.");
    } else {
      player.getGameEncoder().sendMessage("You try to speak to Ungadulu, but he ignores you.");
    }
  }

  // Siegfried Erkle
  public static void npc3961(Player player, int index, Npc npc) {
    player.openShop("legends");
  }

  // Boulder
  public static void npc3967(Player player, int index, Npc npc) {
    if (player.getCombat().getLegendsQuest() == 2) {
      player.getGameEncoder()
          .sendMessage("You search around the rock and discover a dagger on the ground.");
      player.getGameEncoder().sendMessage("Ungadulu might be able to do something with this.");
      player.getInventory().addOrDropItem(746, 1);
    }
  }

  // Ilfeen
  public static void npc4003(Player player, int index, Npc npc) {
    player.openDialogue("elfseed", 0);
  }

  // Fisherman
  public static void npc4065(Player player, int index, Npc npc) {
    player.openDialogue("fishnoter", 0);
  }

  // Wizard
  public static void npc4399(Player player, int index, Npc npc) {
    if (index == 0) {
      Teleports.open(player);
    } else if (index == 2) {
      int[] teleportIndices = player.getWidgetManager().getLastTeleport(0);
      Teleports.destinationWidgetPressed(player, teleportIndices[0], teleportIndices[1]);
    }
  }

  // Sir Vyvin
  public static void npc4736(Player player, int index, Npc npc) {
    player.openShop("white_knight");
  }

  // Miles
  public static void npc5437(Player player, int index, Npc npc) {
    for (int i = 0; i < player.getInventory().size(); i++) {
      int itemId = player.getInventory().getId(i);
      int notedId = ItemDef.getNotedId(itemId);
      if (itemId == -1 || notedId == -1 || ItemDef.getNoted(itemId)
          || ItemDef.getStackable(itemId)) {
        continue;
      }
      player.getInventory().deleteItem(itemId, 1, i);
      player.getInventory().addItem(notedId, 1, i);
    }
  }

  // Security Guard
  public static void npc5442(Player player, int index, Npc npc) {
    player.getInventory().addOrDropItem(9003, 1);
  }

  // Le-sabr￨
  public static void npc5521(Player player, int index, Npc npc) {}

  // Black warlock
  public static void npc5553(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Snowy knight
  public static void npc5554(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Sapphire glacialis
  public static void npc5555(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Ruby harvest
  public static void npc5556(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Elnock Inquisitor
  public static void npc5734(Player player, int index, Npc npc) {
    player.openShop("skilling");
  }

  // <col=00ffff>Boulder</col>
  public static void npc6621(Player player, int index, Npc npc) {
    if (player.getController().isMagicBound()) {
      player.getGameEncoder()
          .sendMessage("A magical force stops you from moving for "
              + PTime
                  .tickToSec(player.getMovement().getMagicBindDelay() - Movement.MAGIC_REBIND_DELAY)
              + " more seconds.");
      return;
    }
    if (npc.getX() == 3053 && npc.getY() == 10165) {
      if (player.getX() >= 3055) {
        player.getMovement().teleport(3052, 10165, 3);
      } else {
        player.getMovement().teleport(3055, 10165, 3);
      }
    }
  }

  // Marisi
  public static void npc6921(Player player, int index, Npc npc) {
    if (index == 0) {
      player.getGameEncoder().sendMessage("This gardener will protect your patches for a fee.");
    } else {
      player.getFarming().gardenerProtection(npc, index - 2);
    }
  }

  // Tynan
  public static void npc6964(Player player, int index, Npc npc) {
    player.openShop("skilling");
  }

  // Tyss
  public static void npc7050(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("spellbooks", 0);
    } else if (index == 2) {
      player.getMagic().setVengeanceCast(false);
      if (player.getMagic().getSpellbook() == Magic.STANDARD_MAGIC) {
        player.getMagic().setSpellbook(Magic.ANCIENT_MAGIC);
      } else if (player.getMagic().getSpellbook() == Magic.ANCIENT_MAGIC) {
        player.getMagic().setSpellbook(Magic.LUNAR_MAGIC);
      } else {
        player.getMagic().setSpellbook(Magic.STANDARD_MAGIC);
      }
    }
  }
  /* 7168 - 7423 */

  // Lucky impling
  public static void npc7233(Player player, int index, Npc npc) {
    player.getHunter().catchNPC(npc);
  }

  // Perry
  public static void npc7240(Player player, int index, Npc npc) {
    player.openShop("skilling");
  }

  // Eniola
  public static void npc7417(Player player, int index, Npc npc) {
    player.getBank().open();
  }

  // TzHaar-Ket-Zuh
  public static void npc7677(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("bank", 1);
    } else if (index == 2) {
      player.getBank().open();
    }
  }
  /* 7680 - 7935 */

  // TzHaar-Ket-Keh
  public static void npc7690(Player player, int index, Npc npc) {
    player.openDialogue("tzhaar", 6);
  }

  // Gadrin
  public static void npc7716(Player player, int index, Npc npc) {
    if (npc.getX() == 2446 && npc.getY() == 3426) {
      player.getMovement().animatedTeleport(new Tile(2467, 9905),
          Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
          Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
    } else if (npc.getX() == 3272 && npc.getY() == 3164) {
      player.getMovement().animatedTeleport(new Tile(3294, 3282),
          Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
          Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
    } else {
      player.openShop("skilling");
    }
  }

  // Belona
  public static void npc7719(Player player, int index, Npc npc) {
    if (index == 3) {
      player.getSkills().setMiningMinerals(!player.getSkills().getMiningMinerals());
      player.getGameEncoder()
          .sendMessage("Minerals while mining: " + player.getSkills().getMiningMinerals());
    } else {
      player.openShop("skilling");
    }
  }

  // Kylie Minnow
  public static void npc7728(Player player, int index, Npc npc) {
    player.openShop("minnows");
  }
  /* 7936 - 8191 */

  // Emblem Trader
  public static void npc7942(Player player, int index, Npc npc) {
    if (index == 0) {
      player.openDialogue("emblemtrader", 1);
    } else if (index == 2) {
      player.getGameEncoder().sendMessage("You can't trade him here.");
    } else if (index == 3) {
      boolean show = !player.getCombat().showKDR();
      player.getCombat().setShowKDR(show);
      if (show) {
        player.getGameEncoder().sendMessage("Now displaying KDR.");
      } else {
        player.getGameEncoder().sendMessage("Now hiding KDR.");
      }
    } else if (index == 4) {
      player.getCombat().setPKSkullDelay(PCombat.SKULL_DELAY);
    }
  }

  // Elder Chaos druid
  public static void npc7995(Player player, int index, Npc npc) {
    player.getGameEncoder().sendMessage("The elder chaos druid will unnote bones for a fee.");
  }

  // Vorkath
  public static void npc8059(Player player, int index, Npc npc) {
    if (npc.isLocked()) {
      return;
    }
    npc.setTransformationId(8061);
    npc.setAnimation(7950);
    npc.setLock(8);
  }
  /* 8192 - 8447 */
  /* 8448 - 8703 */

  // Cursed jewelled crab (red)
  public static void npc16002(Player player, int index, Npc npc) {
    if (!player.getInventory().hasItem(ItemId.HAMMER)
        && player.getEquipment().getWeaponId() != ItemId.DRAGON_WARHAMMER
        && player.getEquipment().getWeaponId() != ItemId.ELDER_MAUL
        && player.getEquipment().getWeaponId() != ItemId.TORAGS_HAMMERS) {
      player.getGameEncoder().sendMessage("This crab can only be smashed by certain hammers.");
      return;
    }
    if (player.getInventory().hasItem(ItemId.HAMMER)) {
      player.setAnimation(1755);
    } else {
      player.setAnimation(player.getCombat().getAttackAnimation());
    }
    npc.getCombat().getCombatScript().script("smash");
  }

  // Cursed jewelled crab (green)
  public static void npc16003(Player player, int index, Npc npc) {
    if (!player.getInventory().hasItem(ItemId.HAMMER)
        && player.getEquipment().getWeaponId() != ItemId.DRAGON_WARHAMMER
        && player.getEquipment().getWeaponId() != ItemId.ELDER_MAUL
        && player.getEquipment().getWeaponId() != ItemId.TORAGS_HAMMERS) {
      player.getGameEncoder().sendMessage("This crab can only be smashed by certain hammers.");
      return;
    }
    if (player.getInventory().hasItem(ItemId.HAMMER)) {
      player.setAnimation(1755);
    } else {
      player.setAnimation(player.getCombat().getAttackAnimation());
    }
    npc.getCombat().getCombatScript().script("smash");
  }

  // Cursed jewelled crab (blue)
  public static void npc16004(Player player, int index, Npc npc) {
    if (!player.getInventory().hasItem(ItemId.HAMMER)
        && player.getEquipment().getWeaponId() != ItemId.DRAGON_WARHAMMER
        && player.getEquipment().getWeaponId() != ItemId.ELDER_MAUL
        && player.getEquipment().getWeaponId() != ItemId.TORAGS_HAMMERS) {
      player.getGameEncoder().sendMessage("This crab can only be smashed by certain hammers.");
      return;
    }
    if (player.getInventory().hasItem(ItemId.HAMMER)) {
      player.setAnimation(1755);
    } else {
      player.setAnimation(player.getCombat().getAttackAnimation());
    }
    npc.getCombat().getCombatScript().script("smash");
  }
}
