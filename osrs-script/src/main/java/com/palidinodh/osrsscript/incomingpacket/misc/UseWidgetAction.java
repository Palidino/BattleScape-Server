package com.palidinodh.osrsscript.incomingpacket.misc;

import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Farming;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Prayer;
import com.palidinodh.osrscore.model.player.Smithing;
import com.palidinodh.osrscore.model.player.WidgetManager;
import com.palidinodh.osrscore.model.player.combat.WarriorsGuild;
import com.palidinodh.osrscore.world.WishingWell;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PNumber;

public class UseWidgetAction {
  public static void doActionNpc(Player player, int widgetId, int childId, int slot, Npc npc) {
    if (widgetId == WidgetId.INVENTORY) {
      Item item = player.getInventory().getItem(slot);
      if (item == null) {
        return;
      }
      int itemId = item.getId();
      switch (npc.getId()) {
        case 13: // Piles
          int inventoryCount = player.getInventory().getCount(itemId);
          player.openDialogue("piles", 0);
          DialogueOld.setText(player,
              "Banknote " + inventoryCount + " x " + ItemDef.getName(itemId),
              "Yes - " + PNumber.formatNumber(inventoryCount * 50) + " gp", "Cancel");
          player.putAttribute("use_item_id", itemId);
          player.getWidgetManager().addChatboxCloseEvent(new WidgetManager.CloseEvent() {
            @Override
            public void execute() {
              player.removeAttribute("use_item_id");
            }
          });
          break;
        case 7995: // Elder Chaos druid
          if (!Prayer.isBone(ItemDef.getUnnotedId(itemId))) {
            break;
          }
          int exchangeCount = player.getInventory().getCount(itemId);
          exchangeCount = Math.min(exchangeCount, player.getInventory().getRemainingSlots());
          exchangeCount = Math.min(exchangeCount, Item.MAX_AMOUNT / 50);
          player.openDialogue("elderchaosdruid", 0);
          DialogueOld.setText(player, null,
              "Exchange '" + ItemDef.getName(ItemDef.getUnnotedId(itemId)) + "': 50 coins",
              "Exchange 5: 250 coins",
              "Exchange All: " + PNumber.formatNumber(exchangeCount * 50) + " coins", "Exchange X",
              "Cancel");
          player.putAttribute("use_item_id", itemId);
          player.getWidgetManager().addChatboxCloseEvent(new WidgetManager.CloseEvent() {
            @Override
            public void execute() {
              player.removeAttribute("use_item_id");
            }
          });
          break;
        case 0: // Tool Leprechaun
        case 757: // Tool Leprechaun on vacation
          int notedId = ItemDef.getNotedId(itemId);
          if (!Farming.isCollectable(itemId) || notedId == -1) {
            break;
          }
          int exchangeCount2 = player.getInventory().getCount(itemId);
          player.getInventory().deleteItem(itemId, exchangeCount2);
          player.getInventory().addItem(notedId, exchangeCount2);
          break;
      }
    }
  }

  public static void doActionWidgetOnWidget(Player player, int fromWidgetId, int fromChildId,
      int toWidgetId, int toChildId, int useSlot, int useItemId, int onSlot, int onItemId) {}

  public static void doActionMapObject(Player player, int widgetId, int childId, int itemSlot,
      MapObject object) {
    if (widgetId == WidgetId.INVENTORY) {
      Item item = player.getInventory().getItem(itemSlot);
      if (item == null) {
        return;
      }
      int itemId = player.getInventory().getId(itemSlot);
      switch (object.getId()) {
        case 34628: // Machinery
          if (!player.getInventory().hasItem(ItemId.HAMMER)) {
            player.getGameEncoder().sendMessage("You need a hammer to use this machinery.");
            break;
          }
          if (itemId == ItemId.HYDRA_LEATHER) {
            player.getInventory().deleteItem(itemId, 1, itemSlot);
            player.getInventory().addItem(ItemId.FEROCIOUS_GLOVES, 1, itemSlot);
            player.getGameEncoder().sendMessage(
                "By feeding the tough to work leather through the machine, you manage to form a pair of gloves.");
          } else if (itemId == ItemId.FEROCIOUS_GLOVES) {
            player.getInventory().deleteItem(itemId, 1, itemSlot);
            player.getInventory().addItem(ItemId.HYDRA_LEATHER, 1, itemSlot);
            player.getGameEncoder().sendMessage(
                "By feeding the gloves through the machine, you manage to revert them into leather.");
          }
          break;
        case 24004: // Waterpump
          if (itemId == 1925) {
            player.getInventory().deleteItem(itemId, 1, itemSlot);
            player.getInventory().addItem(1929, 1, itemSlot);
            AchievementDiary.makeItemUpdate(player, -1, new Item(1929, 1), null, null);
          }
          break;
        case 733: // Web
          if ((ItemDef.getWeaponType(itemId) == null || !ItemDef.getWeaponType(itemId).hasSlash())
              && itemId != 946) {
            player.getGameEncoder()
                .sendMessage("Only a sharp blade can cut through this sticky web.");
            return;
          }
          player.setAnimation(player.getCombat().getAttackAnimation());
          if (PRandom.randomE(4) != 0) {
            player.getGameEncoder().sendMessage("You fail to cut through it.");
            return;
          }
          player.getGameEncoder().sendMessage("You slash the web apart.");
          MapObject newWeb = new MapObject(object.getId() + 1, object);
          player.getWorld().addEvent(new TempMapObject(100, player.getController(), newWeb));
          break;
        case 4974: // Mine cart
          if (object.getX() == 2778 && object.getY() == 4506) {
            if (player.getCombat().getHauntedMine() == 0) {
              if (!player.getInventory().hasItem(4075)) {
                player.getGameEncoder()
                    .sendMessage("You don't see a reason to add this to the mine cart.");
                break;
              }
              player.getCombat().setHauntedMine(1);
              player.getInventory().deleteItem(4075, 1);
              player.getGameEncoder().sendMessage("You place the glowing fungus in the mine cart.");
              player.getGameEncoder()
                  .sendMessage("You push the mine cart and it travels deeper into the mine.");
              player.setAnimation(4343);
              player.getGameEncoder().sendRemoveMapObject(object);
            } else {
              player.getGameEncoder()
                  .sendMessage("You have already pushed a mine cart deeper into the mine.");
            }
          } else {
            player.getGameEncoder().sendMessage("Nothing interesting happens.");
          }
          break;
        case 2638: // Fountain of Heroes
          if (itemId != 1704 && itemId != 1705) {
            player.getGameEncoder().sendMessage("Nothing interesting happens.");
            break;
          }
          int gloryCount = player.getInventory().getCount(itemId);
          player.getInventory().deleteItem(itemId, gloryCount);
          player.getInventory().addItem(ItemDef.getNoted(itemId) ? 1713 : 1712, gloryCount);
          break;
        case 884: // Wishing well
          if (itemId == ItemId.COINS) {
            player.openDialogue("wishingwell", 2);
            return;
          } else if (!WishingWell.canDonateItem(itemId)) {
            player.getGameEncoder().sendMessage("The well won't take this item.");
            break;
          }
          player.openDialogue("wishingwell", 4);
          if (itemId == ItemId.BOND_32318) {
            DialogueOld.setText(player, item.getName() + " x"
                + PNumber.formatNumber(item.getAmount()) + ": "
                + PNumber.formatNumber(
                    PNumber.multiplyInt(WishingWell.BOND_VALUE, item.getAmount(), Item.MAX_AMOUNT)),
                (String[]) null);
          } else {
            DialogueOld.setText(player,
                item.getName() + " x" + PNumber.formatNumber(item.getAmount()) + ": "
                    + PNumber.formatNumber(
                        PNumber.multiplyInt(item.getDef().getConfiguredExchangePrice() * 2,
                            item.getAmount(), Item.MAX_AMOUNT)),
                (String[]) null);
          }
          player.putAttribute("wishing_well_item_id", itemId);
          player.getWidgetManager().addChatboxCloseEvent(new WidgetManager.CloseEvent() {
            @Override
            public void execute() {
              player.removeAttribute("wishing_well_item_id");
            }
          });
          break;
        case 28900: // Catacombs of Kourend Altar
          if (itemId == 6746 || itemId == 19675) {
            player.getCharges().chargeFromInventory(19675, itemSlot, 1, new Item(19677, 3), 1000);
          } else if (itemId == 19685) {
            player.openDialogue("catacombsofkourend", 1);
          }
          break;
        case 2965: // Legends Quest Mossy rock
          if (itemId == 744) {
            player.getInventory().deleteItem(744, 1, itemSlot);
            player.getInventory().addItem(745, 1, itemSlot);
            player.getGameEncoder()
                .sendMessage("The rocks vibrate and hum and the crystal starts to glow.");
          } else {
            player.getGameEncoder()
                .sendMessage("Nothing interesting happens. Maybe I should try something else...");
          }
          break;
        case 2966: // Legends Quest Furnace
          if (itemId == 741 || itemId == 742 || itemId == 743) {
            if (player.getInventory().getCount(741) < 1 || player.getInventory().getCount(742) < 1
                || player.getInventory().getCount(743) < 1) {
              player.getGameEncoder().sendMessage("Nothing interesting happens.");
              player.getGameEncoder().sendMessage("Maybe there is more to this crystal....");
              break;
            }
            player.getInventory().deleteItem(741, 1);
            player.getInventory().deleteItem(742, 1);
            player.getInventory().deleteItem(743, 1);
            player.getInventory().addItem(744, 1, itemSlot);
            player.getGameEncoder()
                .sendMessage("The heat in the furnace rises and fuses the parts together.");
          } else {
            player.getGameEncoder().sendMessage("Perhaps I should use this to forge something...");
          }
          break;
        case 2934: // Legends Quest Winch
          if (itemId == 954) {
            player.getGameEncoder().sendMessage("You shimmy down the rope into the darkness.");
            player.getMovement().ladderUpTeleport(new Tile(2377, 4712, 0));
          } else {
            player.getGameEncoder().sendMessage("Nothing interesting happens.");
          }
          break;
        case 23955: // Magical animator
          switch (itemId) {
            case 1155:
            case 1117:
            case 1075:
              player.getCombat().getWarriorsGuild().spawnArmour(object,
                  WarriorsGuild.BRONZE_ARMOUR);
              break;
            case 1153:
            case 1115:
            case 1067:
              player.getCombat().getWarriorsGuild().spawnArmour(object, WarriorsGuild.IRON_ARMOUR);
              break;
            case 1157:
            case 1119:
            case 1069:
              player.getCombat().getWarriorsGuild().spawnArmour(object, WarriorsGuild.STEEL_ARMOUR);
              break;
            case 1165:
            case 1125:
            case 1077:
              player.getCombat().getWarriorsGuild().spawnArmour(object, WarriorsGuild.BLACK_ARMOUR);
              break;
            case 1159:
            case 1121:
            case 1071:
              player.getCombat().getWarriorsGuild().spawnArmour(object,
                  WarriorsGuild.MITHRIL_ARMOUR);
              break;
            case 1161:
            case 1123:
            case 1073:
              player.getCombat().getWarriorsGuild().spawnArmour(object,
                  WarriorsGuild.ADAMANT_ARMOUR);
              break;
            case 1163:
            case 1127:
            case 1079:
              player.getCombat().getWarriorsGuild().spawnArmour(object, WarriorsGuild.RUNE_ARMOUR);
              break;
          }
          break;
        case 13179:
        case 13180:
        case 13181:
        case 13182:
        case 13183:
        case 13184:
        case 13185:
        case 13186:
        case 13187:
        case 13188:
        case 13189:
        case 13190:
        case 13191:
        case 13192:
        case 13193:
        case 13194:
        case 13195:
        case 13196:
        case 13197:
        case 13198:
        case 13199:
        case 14860:
        case 411: // Altar
          if (!Prayer.isBone(itemId)) {
            break;
          }
          player.openDialogue("prayer", 0);
          player.getWidgetManager().sendChatboxOverlay(WidgetId.MAKE_X,
              new WidgetManager.CloseEvent() {
                @Override
                public void execute() {
                  player.removeAttribute("map_object");
                  player.removeAttribute("item_id");
                }
              });
          player.getGameEncoder().sendMakeX("How many would you like to use?", 14,
              player.getInventory().getCount(itemId), itemId);
          player.putAttribute("item_id", itemId);
          player.putAttribute("map_object", object);
          break;
        case 16469:
        case 26300:
        case 21303:
        case 4304:
        case 24009: // Furnace
          if (itemId == Smithing.COPPER_ORE_ID || itemId == Smithing.TIN_ORE_ID
              || itemId == Smithing.IRON_ORE_ID || itemId == Smithing.SILVER_ORE_ID
              || itemId == Smithing.GOLD_ORE_ID || itemId == Smithing.MITHRIL_ORE_ID
              || itemId == Smithing.ADAMANT_ORE_ID || itemId == Smithing.RUNE_ORE_ID
              || itemId == Smithing.COAL_ID || itemId == ItemId.BLURITE_ORE) {
            Smithing.openSmelt(player);
          }
          break;
        case 2031:
        case 2097:
        case 4306: // Anvil
          if (itemId == Smithing.BRONZE_BAR_ID || itemId == Smithing.IRON_BAR_ID
              || itemId == Smithing.STEEL_BAR_ID || itemId == Smithing.MITHRIL_BAR_ID
              || itemId == Smithing.ADAMANT_BAR_ID || itemId == Smithing.RUNE_BAR_ID
              || itemId == Smithing.BLURITE_BAR_ID) {
            Smithing.openSmith(player, itemId);
          }
          break;
        case 27029:
          if (itemId != 13273) {
            player.getGameEncoder().sendMessage("Nothing interesting happens.");
            break;
          }
          player.setAnimation(Prayer.PRAY_ANIMATION);
          player.getWorld().sendMapGraphic(player.getController(), new Tile(3039, 4774), 1276, 0,
              0);
          int droppedId = -1;
          if (PRandom.randomE(128) < 5) {
            droppedId = 13262; // Abyssal orphan
          } else if (PRandom.randomE(128) < 10) {
            droppedId = 7979; // Abyssal head
          } else if (PRandom.randomE(128) < 12) {
            droppedId = 4151; // Abyssal whip
          } else if (PRandom.randomE(128) < 13) {
            droppedId = 13277; // Jar of miasma
          } else if (PRandom.randomE(128) < 26) {
            droppedId = 13265; // Abyssal dagger
          } else {
            if (!player.hasItem(13274)) {
              droppedId = 13274; // Bludgeon spine
            } else if (!player.hasItem(13275)) {
              droppedId = 13275; // Bludgeon claw
            } else if (!player.hasItem(13276)) {
              droppedId = 13276; // Bludgeon axon
            } else {
              droppedId = 13274 + PRandom.randomI(2);
            }
          }
          int unsiredKillCount = player.getInventory().getAttachment(itemSlot) != null
              ? (int) player.getInventory().getAttachment(itemSlot)
              : 0;
          if (unsiredKillCount > 0) {
            player.getCombat().logNPCItem("Abyssal Sire", droppedId, 1, unsiredKillCount);
          } else {
            player.getCombat().logNPCItem("Abyssal Sire", droppedId, 1);
          }
          if (droppedId == 13262 || droppedId == 13265
              || droppedId >= 13274 && droppedId <= 13276) {
            player.getWorld().sendItemDropNews(player, droppedId, "the Abyssal Sire");
          }
          player.getInventory().deleteItem(itemId, 1, itemSlot);
          player.getInventory().addItem(droppedId, 1, itemSlot);
          break;
        default:
          player.getGameEncoder().sendMessage("Nothing interesting happens.");
          break;
      }
    }
  }
}
