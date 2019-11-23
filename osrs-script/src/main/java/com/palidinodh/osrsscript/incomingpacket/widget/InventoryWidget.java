package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Graphic;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.dialogue.DialogueAction;
import com.palidinodh.osrscore.model.dialogue.OptionsDialogue;
import com.palidinodh.osrscore.model.dialogue.old.DialogueOld;
import com.palidinodh.osrscore.model.guide.Guide;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.item.MysteryBox;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Familiar;
import com.palidinodh.osrscore.model.player.Farming;
import com.palidinodh.osrscore.model.player.Herblore;
import com.palidinodh.osrscore.model.player.Hunter;
import com.palidinodh.osrscore.model.player.ItemCharges;
import com.palidinodh.osrscore.model.player.Magic;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Runecrafting;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.combat.BountyHunter;
import com.palidinodh.osrscore.model.player.combat.DropRateBoost;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.osrscore.world.WildernessEvent;
import com.palidinodh.osrscore.world.World;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.rs.setting.SqlUserRank;
import com.palidinodh.util.PNumber;
import com.palidinodh.util.PTime;
import lombok.var;

// No longer JS woohoo! Need a better solution than a 2K line file and growing.
// One item id per file would be horid
// A lot of these can be moved into appropriate skill container classes
// Option #1: group ids by relevance and then split those into sub classes.
// Option #2: group by id (maybe 1-4096, etc)
// Option #3: ???

public class InventoryWidget implements WidgetHandler {
  @Override
  public int[] getIds() {
    return new int[] {WidgetId.INVENTORY};
  }

  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    player.clearAllActions(false, false);
    if (itemId != player.getInventory().getId(slot)) {
      return;
    }
    Item item = player.getInventory().getItem(slot);
    if (option == 0 && player.getController().isFood(itemId)) {
      player.getWidgetManager().removeInteractiveWidgets();
      player.getSkills().eatFood(slot);
      return;
    }
    if (option == 0 && player.getController().isDrink(itemId)) {
      player.getWidgetManager().removeInteractiveWidgets();
      player.getSkills().drink(slot);
      return;
    }
    if (option == 0 && player.getPrayer().buryBones(slot) >= 0) {
      player.getWidgetManager().removeInteractiveWidgets();
      return;
    }
    if (option == 0 && Herblore.cleanHerb(player, itemId, slot)) {
      return;
    }
    if (item.getDef().getEquipSlot() != null
        && (item.getDef().isOption(option, "wear") || item.getDef().isOption(option, "wield")
            || item.getDef().isOption(option, "equip") || item.getDef().isOption(option, "ride"))) {
      player.getEquipment().equip(itemId, slot);
      return;
    }
    if (option == 4 && (item.getDef().getOption(option) == null
        || item.getDef().isOption(option, "drop") || item.getDef().isOption(option, "destroy")
        || item.getDef().isOption(option, "release"))) {
      player.getWidgetManager().removeInteractiveWidgets();
      if (Familiar.isPetItem(itemId)) {
        player.getFamiliar().summonPet(itemId);
        return;
      }
      if (player.inNoDroppingItemsArea()) {
        player.getGameEncoder().sendMessage("Items can't be dropped here.");
        return;
      }
      if ((itemId == ItemId.BLOOD_MONEY || BountyHunter.MysteriousEmblem.isEmblem(itemId))
          && player.getController().inWilderness()) {
        player.getGameEncoder().sendMessage("You can't drop this here.");
        return;
      }
      if (ItemDef.getUntradable(itemId) && player.getController().inWilderness()
          && itemId != ItemId.BLOODY_KEY && itemId != ItemId.BLOODIER_KEY) {
        player.getGameEncoder().sendMessage("You can't drop this right now.");
        return;
      }
      if (itemId == ItemId.BLOODY_KEY || itemId == ItemId.BLOODIER_KEY) {
        WildernessEvent.bloodyKeyToMap(item, player, MapItem.NORMAL_TIME, MapItem.ALWAYS_APPEAR);
      } else if (player.isUsergroup(SqlUserRank.YOUTUBER)) {
        player.getController().addMapItem(item, player, MapItem.NORMAL_TIME, MapItem.NEVER_APPEAR);
      } else if (player.getController().inWilderness() && !player.getController().isInstanced()
          && !ItemDef.getUntradable(itemId)) {
        if (player.getController().isFood(itemId) || player.getController().isDrink(itemId)) {
          player.getController().addMapItem(item, player, MapItem.NORMAL_TIME,
              MapItem.NEVER_APPEAR);
        } else {
          player.getController().addMapItem(item, player, MapItem.NORMAL_TIME,
              MapItem.ALWAYS_APPEAR);
        }
      } else if (player.getController().isInstanced()) {
        player.getController().addMapItem(item, player, MapItem.LONG_TIME, MapItem.NORMAL_APPEAR);
      } else if (!item.getDef().isOption(option, "release")) {
        player.getController().addMapItem(item, player, player);
      }
      player.getInventory().deleteItem(itemId, item.getAmount(), slot);
      RequestManager.addPlayerLog(player, "mapitem",
          player.getLogName() + " dropped " + item.getLogName() + " located at " + player + ".");
      return;
    }
    if (option == 0 && item.getDef().getExchangeSetIds() != null
        && item.getDef().isOption(option, "open")) {
      if (player.getInventory().getRemainingSlots() < item.getDef().getExchangeSetIds().length) {
        player.getInventory().notEnoughSpace();
        return;
      }
      player.getInventory().deleteItem(itemId, 1, slot);
      for (int exchangeSetId : item.getDef().getExchangeSetIds()) {
        player.getInventory().addItem(exchangeSetId, 1);
      }
      return;
    }
    int height = 0;
    Tile tile = null;
    int[] items = null;
    RandomItem[] randomItems = null;
    Item anItem = null;
    int[] ttLoot = null;
    switch (itemId) {
      case ItemId.MITHRIL_SEEDS:
        if (player.getController().hasSolidMapObject(player)) {
          player.getGameEncoder().sendMessage("You can't do this here.");
          break;
        }
        int[] flowerIds = new int[] {2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988};
        int flowerId = flowerIds[PRandom.randomE(flowerIds.length)];
        player.getInventory().deleteItem(itemId, 1, slot);
        var flower = new MapObject(flowerId, player, 10, MapObject.getRandomDirection());
        player.getWorld().addEvent(new TempMapObject(100, player.getController(), flower));
        Route.moveOffTile(player);
        player.setAnimation(645);
        break;
      case ItemId.ARMADYL_GODSWORD:
        if (player.getInventory().getRemainingSlots() < 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.ARMADYL_HILT, 1, slot);
        player.getInventory().addItem(ItemId.GODSWORD_BLADE);
        break;
      case ItemId.BANDOS_GODSWORD:
        if (player.getInventory().getRemainingSlots() < 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.BANDOS_HILT, 1, slot);
        player.getInventory().addItem(ItemId.GODSWORD_BLADE);
        break;
      case ItemId.SARADOMIN_GODSWORD:
        if (player.getInventory().getRemainingSlots() < 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.SARADOMIN_HILT, 1, slot);
        player.getInventory().addItem(ItemId.GODSWORD_BLADE);
        break;
      case ItemId.ZAMORAK_GODSWORD:
        if (player.getInventory().getRemainingSlots() < 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.ZAMORAK_HILT, 1, slot);
        player.getInventory().addItem(ItemId.GODSWORD_BLADE);
        break;
      case ItemId.ADAMANT_ARROW_PACK:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.ADAMANT_ARROW, 50, slot);
        break;
      case ItemId.RUNE_ARROW_PACK:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.RUNE_ARROW, 50, slot);
        break;
      case ItemId.CATALYTIC_RUNE_PACK:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addOrDropItem(ItemId.CHAOS_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.DEATH_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.BLOOD_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.WRATH_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.NATURE_RUNE, 50);
        break;
      case ItemId.ELEMENTAL_RUNE_PACK:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addOrDropItem(ItemId.AIR_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.WATER_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.EARTH_RUNE, 50);
        player.getInventory().addOrDropItem(ItemId.FIRE_RUNE, 50);
        break;
      case ItemId.COIN_POUCH:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addOrDropItem(ItemId.COINS, 100_000 + PRandom.randomI(300_000));
        break;
      case ItemId.COIN_POUCH_22522:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addOrDropItem(ItemId.COINS, 25_000 + PRandom.randomI(75_000));
        break;
      case ItemId._50_DROP_BOOST_SCROLL_1_HOUR_32314:
        if (player.getCombat().getDropRateBoost() != null) {
          player.getGameEncoder().sendMessage("You already have a drop rate boost active.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().setDropRateBoost(new DropRateBoost(1.5, (int) PTime.hourToTick(1)));
        player.getGameEncoder().sendMessage("A drop rate boost of 50% has been added for 1 hour.");
        break;
      case ItemId._50_DROP_BOOST_SCROLL_4_HOURS_32315:
        if (player.getCombat().getDropRateBoost() != null) {
          player.getGameEncoder().sendMessage("You already have a drop rate boost active.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().setDropRateBoost(new DropRateBoost(1.5, (int) PTime.hourToTick(4)));
        player.getGameEncoder().sendMessage("A drop rate boost of 50% has been added for 4 hours.");
        break;
      case ItemId._50_DROP_BOOST_SCROLL_8_HOURS_32316:
        if (player.getCombat().getDropRateBoost() != null) {
          player.getGameEncoder().sendMessage("You already have a drop rate boost active.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().setDropRateBoost(new DropRateBoost(1.5, (int) PTime.hourToTick(8)));
        player.getGameEncoder().sendMessage("A drop rate boost of 50% has been added for 8 hours.");
        break;
      case ItemId._50_DROP_BOOST_SCROLL_16_HOURS_32317:
        if (player.getCombat().getDropRateBoost() != null) {
          player.getGameEncoder().sendMessage("You already have a drop rate boost active.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().setDropRateBoost(new DropRateBoost(1.5, (int) PTime.hourToTick(16)));
        player.getGameEncoder()
            .sendMessage("A drop rate boost of 50% has been added for 16 hours.");
        break;
      case ItemId.VOID_KNIGHT_SET_32289:
        if (player.getInventory().getRemainingSlots() < 9 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.VOID_KNIGHT_TOP);
        player.getInventory().addItem(ItemId.VOID_KNIGHT_ROBE);
        player.getInventory().addItem(ItemId.VOID_KNIGHT_MACE);
        player.getInventory().addItem(ItemId.VOID_KNIGHT_GLOVES);
        player.getInventory().addItem(ItemId.VOID_MAGE_HELM);
        player.getInventory().addItem(ItemId.VOID_RANGER_HELM);
        player.getInventory().addItem(ItemId.VOID_MELEE_HELM);
        player.getInventory().addItem(ItemId.ELITE_VOID_TOP);
        player.getInventory().addItem(ItemId.ELITE_VOID_ROBE);
        break;
      case ItemId.LUMBERJACK_OUTFIT_WOODCUTTING_32291:
        if (player.getInventory().getRemainingSlots() < 4 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.LUMBERJACK_HAT);
        player.getInventory().addItem(ItemId.LUMBERJACK_TOP);
        player.getInventory().addItem(ItemId.LUMBERJACK_LEGS);
        player.getInventory().addItem(ItemId.LUMBERJACK_BOOTS);
        break;
      case ItemId.PROSPECTOR_OUTFIT_MINING_32292:
        if (player.getInventory().getRemainingSlots() < 4 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.PROSPECTOR_HELMET);
        player.getInventory().addItem(ItemId.PROSPECTOR_JACKET);
        player.getInventory().addItem(ItemId.PROSPECTOR_LEGS);
        player.getInventory().addItem(ItemId.PROSPECTOR_BOOTS);
        break;
      case ItemId.ANGLER_OUTFIT_FISHING_32293:
        if (player.getInventory().getRemainingSlots() < 4 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.ANGLER_HAT);
        player.getInventory().addItem(ItemId.ANGLER_TOP);
        player.getInventory().addItem(ItemId.ANGLER_WADERS);
        player.getInventory().addItem(ItemId.ANGLER_BOOTS);
        break;
      case ItemId.PYROMANCER_OUTFIT_FIREMAKING_32294:
        if (player.getInventory().getRemainingSlots() < 5 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.PYROMANCER_HOOD);
        player.getInventory().addItem(ItemId.PYROMANCER_GARB);
        player.getInventory().addItem(ItemId.PYROMANCER_ROBE);
        player.getInventory().addItem(ItemId.PYROMANCER_BOOTS);
        player.getInventory().addItem(ItemId.WARM_GLOVES);
        break;
      case ItemId.ROGUE_OUTFIT_THIEVING_32295:
        if (player.getInventory().getRemainingSlots() < 5 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.ROGUE_MASK);
        player.getInventory().addItem(ItemId.ROGUE_TOP);
        player.getInventory().addItem(ItemId.ROGUE_TROUSERS);
        player.getInventory().addItem(ItemId.ROGUE_BOOTS);
        player.getInventory().addItem(ItemId.ROGUE_GLOVES);
        break;
      case ItemId.LARUPIA_OUTFIT_HUNTER_32296:
        if (player.getInventory().getRemainingSlots() < 3 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.LARUPIA_HAT);
        player.getInventory().addItem(ItemId.LARUPIA_TOP);
        player.getInventory().addItem(ItemId.LARUPIA_LEGS);
        break;
      case ItemId.FARMERS_OUTFIT_FARMING_32297:
        if (player.getInventory().getRemainingSlots() < 4 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.FARMERS_STRAWHAT);
        player.getInventory().addItem(ItemId.FARMERS_JACKET);
        player.getInventory().addItem(ItemId.FARMERS_BORO_TROUSERS);
        player.getInventory().addItem(ItemId.FARMERS_BOOTS);
        break;
      case ItemId.GRACEFUL_OUTFIT_AGILITY_32298:
        if (player.getInventory().getRemainingSlots() < 6 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.GRACEFUL_HOOD);
        player.getInventory().addItem(ItemId.GRACEFUL_TOP);
        player.getInventory().addItem(ItemId.GRACEFUL_LEGS);
        player.getInventory().addItem(ItemId.GRACEFUL_GLOVES);
        player.getInventory().addItem(ItemId.GRACEFUL_BOOTS);
        player.getInventory().addItem(ItemId.GRACEFUL_CAPE);
        break;
      case ItemId.ELIDINIS_OUTFIT_RUNECRAFTING_32299:
        if (player.getInventory().getRemainingSlots() < 2 - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.ROBE_OF_ELIDINIS);
        player.getInventory().addItem(ItemId.ROBE_OF_ELIDINIS_6787);
        break;
      case ItemId.STARTER_PACK_32288:
        if (!player.hasVoted() && player.getRights() == Player.RIGHTS_NONE) {
          player.getGameEncoder().sendMessage("To open this, you first need to vote.");
          player.getGameEncoder().sendMessage("Make sure to relog after voting!");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        if (player.isGameModeNormal()) {
          player.getInventory().addOrDropItem(ItemId.DRAGON_SCIMITAR);
          player.getInventory().addOrDropItem(ItemId.RUNE_FULL_HELM);
          player.getInventory().addOrDropItem(ItemId.RUNE_PLATEBODY);
          player.getInventory().addOrDropItem(ItemId.RUNE_PLATELEGS);
          player.getInventory().addOrDropItem(ItemId.RUNE_KITESHIELD);
          player.getInventory().addOrDropItem(ItemId.RUNE_BOOTS);
          player.getInventory().addOrDropItem(ItemId.RUNE_CROSSBOW);
          player.getInventory().addOrDropItem(ItemId.DIAMOND_BOLTS_E, 100);
          player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_BODY);
          player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_CHAPS);
          player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_VAMB);
          player.getInventory().addOrDropItem(ItemId.BLACK_DHIDE_SHIELD);
          player.getInventory().addOrDropItem(ItemId.MYSTIC_AIR_STAFF);
          player.getInventory().addOrDropItem(ItemId.DEATH_RUNE, 100);
          player.getInventory().addOrDropItem(ItemId.BLOOD_RUNE, 100);
          player.getInventory().addOrDropItem(ItemId.MYSTIC_HAT);
          player.getInventory().addOrDropItem(ItemId.MYSTIC_ROBE_TOP);
          player.getInventory().addOrDropItem(ItemId.MYSTIC_ROBE_BOTTOM);
          player.getInventory().addOrDropItem(ItemId.MYSTIC_GLOVES);
          player.getInventory().addOrDropItem(ItemId.MYSTIC_BOOTS);
        }
        player.getInventory().addOrDropItem(ItemId.COINS, 250000);
        player.getInventory().addOrDropItem(ItemId.MONKFISH_NOTED, 150);
        player.getInventory().addOrDropItem(ItemId.SUPER_ATTACK_4_NOTED, 15);
        player.getInventory().addOrDropItem(ItemId.SUPER_STRENGTH_4_NOTED, 15);
        player.getInventory().addOrDropItem(ItemId.SUPER_DEFENCE_4_NOTED, 15);
        player.getInventory().addOrDropItem(ItemId.PRAYER_POTION_4_NOTED, 60);
        break;
      case ItemId.PERMANENT_TZHAAR_WAVE_BOOST_32300:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().getTzHaar().setPermanentWaveBoost(true);
        player.getGameEncoder().sendMessage("The effects of the scroll have been activated.");
        break;
      case ItemId.BIRD_NEST:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.BIRD_NEST_5075, 1, slot);
        player.getInventory().addOrDropItem(ItemId.BIRDS_EGG, 1);
        break;
      case ItemId.BIRD_NEST_5071:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.BIRD_NEST_5075, 1, slot);
        player.getInventory().addOrDropItem(ItemId.BIRDS_EGG_5078, 1);
        break;
      case ItemId.BIRD_NEST_5072:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.BIRD_NEST_5075, 1, slot);
        player.getInventory().addOrDropItem(ItemId.BIRDS_EGG_5077, 1);
        break;
      case ItemId.MAGIC_CAPE:
      case ItemId.MAGIC_CAPE_T:
        player.openDialogue("spellbooks", 1);
        break;
      case ItemId.HYDRA_LEATHER:
        if (Settings.getInstance().isSpawn()) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(ItemId.FEROCIOUS_GLOVES, 1, slot);
        } else {
          player.getGameEncoder().sendMessage(
              "This leather looks pretty tough to work with... Maybe the dragonkin had a way.");
        }
        break;
      case ItemId.RANDOM_PVP_WEAPON_32290:
        items = new int[] {ItemId.VESTAS_LONGSWORD, ItemId.STATIUSS_WARHAMMER, ItemId.VESTAS_SPEAR,
            ItemId.MORRIGANS_JAVELIN, ItemId.MORRIGANS_THROWING_AXE, ItemId.ZURIELS_STAFF};
        player.getInventory().deleteItem(itemId, 1, slot);
        anItem = new Item(items[PRandom.randomE(items.length)], 1);
        if (anItem.getId() == ItemId.MORRIGANS_JAVELIN
            || anItem.getId() == ItemId.MORRIGANS_THROWING_AXE) {
          anItem.setAmount(100);
        }
        player.getInventory().addOrDropItem(anItem);
        RequestManager.addPlayerLog(player, "lootbox", player.getLogName() + " received "
            + anItem.getLogName() + " from a random pvp weapon box.");
        break;
      case ItemId.DARK_RELIC:
        int[] raidsSkills = new int[] {Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH,
            Skills.HITPOINTS, Skills.RANGED, Skills.PRAYER, Skills.MAGIC, Skills.MINING,
            Skills.WOODCUTTING, Skills.HERBLORE, Skills.FARMING, Skills.HUNTER, Skills.COOKING,
            Skills.FISHING, Skills.THIEVING, Skills.FIREMAKING, Skills.AGILITY};
        player.getWidgetManager().sendChooseAdvanceSkill(itemId, 0, 0.5, 0);
        player.getWidgetManager().setChooseAdvanceSkillLevelMultiplier(50);
        for (int raidsSkill : raidsSkills) {
          player.getWidgetManager().setChooseAdvanceSkillLevelMultiplier(raidsSkill, 150);
        }
        break;
      case ItemId.ANTIQUE_LAMP_13145:
        player.getWidgetManager().sendChooseAdvanceSkill(itemId, 2500, 0.5, 30);
        break;
      case ItemId.ANTIQUE_LAMP_13146:
        player.getWidgetManager().sendChooseAdvanceSkill(itemId, 7500, 0.5, 40);
        break;
      case ItemId.ANTIQUE_LAMP_13147:
        player.getWidgetManager().sendChooseAdvanceSkill(itemId, 15000, 0.5, 50);
        break;
      case ItemId.ANTIQUE_LAMP_13148:
        player.getWidgetManager().sendChooseAdvanceSkill(itemId, 50000, 0.5, 70);
        break;
      case 5073: // Bird nest
        randomItems = new RandomItem[] {new RandomItem(5312, 1).weight(200), // Acorn
            new RandomItem(5283, 1).weight(150), // Apple tree seed
            new RandomItem(5313, 1).weight(150), // Willow seed
            new RandomItem(5284, 1).weight(100), // Banana tree seed
            new RandomItem(5285, 1).weight(80), // Orange tree seed
            new RandomItem(5286, 1).weight(70), // Curry tree seed
            new RandomItem(5314, 1).weight(60), // Maple seed
            new RandomItem(5287, 1).weight(50), // Pineapple seed
            new RandomItem(5288, 1).weight(40), // Papaya tree seed
            new RandomItem(5289, 1).weight(30), // Palm tree seed
            new RandomItem(5315, 1).weight(20), // Yew seed
            new RandomItem(5290, 1).weight(20), // Calquat tree seed
            new RandomItem(5316, 1).weight(10), // Magic seed
            new RandomItem(5317, 1).weight(5) // Spirit seed
        };
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(5075, 1, slot);
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 7413: // Bird nest
        randomItems = new RandomItem[] {new RandomItem(5323, 6).weight(125), // Strawberry
            new RandomItem(5320, 6).weight(125), // Sweetcorn
            new RandomItem(5312, 1).weight(100), // Acorn
            new RandomItem(5100, 2).weight(100), // Limpwurt
            new RandomItem(5321, 2).weight(50), // Watermelon
            new RandomItem(5302, 1).weight(40), // Lantadyme
            new RandomItem(5303, 1).weight(40), // Dwarf weed
            new RandomItem(5301, 1).weight(30), // Cadantine
            new RandomItem(5313, 1).weight(20), // Willow
            new RandomItem(5287, 1).weight(20), // Pineapple
            new RandomItem(5288, 1).weight(15), // Papaya
            new RandomItem(5290, 1).weight(15), // Calquat
            new RandomItem(5304, 1).weight(5), // Torstol
            new RandomItem(5317, 1).weight(5), // Spirit seed
            new RandomItem(5314, 1).weight(5), // Maple
            new RandomItem(5295, 1).weight(5), // Ranarr
            new RandomItem(5300, 1).weight(3), // Snapdragon
            new RandomItem(5315, 1).weight(3), // Yew seed
            new RandomItem(5289, 1).weight(2), // Palm
            new RandomItem(5316, 1).weight(2) // Magic
        };
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(5075, 1, slot);
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 13226: // Herb sack
        if (option == 0) {
          for (int i = player.getInventory().size(); i >= 0; i--) {
            int addingId = player.getInventory().getId(i);
            if (!Herblore.isHerb(addingId)) {
              continue;
            }
            int addingAmount = player.getInventory().getAmount(i);
            addingAmount =
                player.getWidgetManager().getHerbSack().canAddAmount(addingId, addingAmount);
            player.getWidgetManager().getHerbSack().addItem(addingId, addingAmount);
            player.getInventory().deleteItem(addingId, addingAmount, i);
          }
        } else if (option == 2) {
          for (int i = player.getWidgetManager().getHerbSack().size(); i >= 0; i--) {
            int addingId = player.getWidgetManager().getHerbSack().getId(i);
            int addingAmount = player.getWidgetManager().getHerbSack().getAmount(i);
            addingAmount = player.getInventory().canAddAmount(addingId, addingAmount);
            player.getInventory().addItem(addingId, addingAmount);
            player.getWidgetManager().getHerbSack().deleteItem(addingId, addingAmount);
          }
        } else if (option == 3) {
          player.getWidgetManager().getHerbSack().displayItemList();
        }
        break;
      case 13639: // Seed box
        if (option == 0) {
          for (int i = player.getInventory().size(); i >= 0; i--) {
            int addingId = player.getInventory().getId(i);
            if (!Farming.isSeed(addingId)) {
              continue;
            }
            int addingAmount = player.getInventory().getAmount(i);
            addingAmount =
                player.getWidgetManager().getSeedBox().canAddAmount(addingId, addingAmount);
            player.getWidgetManager().getSeedBox().addItem(addingId, addingAmount);
            player.getInventory().deleteItem(addingId, addingAmount, i);
          }
        } else if (option == 1) {
          for (int i = player.getWidgetManager().getSeedBox().size(); i >= 0; i--) {
            int addingId = player.getWidgetManager().getSeedBox().getId(i);
            int addingAmount = player.getWidgetManager().getSeedBox().getAmount(i);
            addingAmount = player.getInventory().canAddAmount(addingId, addingAmount);
            player.getInventory().addItem(addingId, addingAmount);
            player.getWidgetManager().getSeedBox().deleteItem(addingId, addingAmount);
          }
        } else if (option == 2) {
          player.getWidgetManager().getSeedBox().displayItemList();
        }
        break;
      case 20703: // Supply crate
        if (player.getInventory().getRemainingSlots() < 3) {
          player.getInventory().notEnoughSpace();
          break;
        }
        randomItems = new RandomItem[] {new RandomItem(1522, 13, 148), new RandomItem(1520, 13, 20),
            new RandomItem(1518, 10, 16), new RandomItem(6334, 14, 59),
            new RandomItem(8836, 10, 48), new RandomItem(1516, 10, 49),
            new RandomItem(1514, 10, 23), new RandomItem(1624, 1, 5), new RandomItem(1622, 1, 5),
            new RandomItem(1618, 1, 5), new RandomItem(1620, 1, 5), new RandomItem(454, 3, 12),
            new RandomItem(441, 3, 15), new RandomItem(443, 3, 12), new RandomItem(445, 3, 70),
            new RandomItem(448, 2, 7), new RandomItem(450, 2, 15), new RandomItem(452, 1, 2),
            new RandomItem(200, 3, 6), new RandomItem(202, 3, 6), new RandomItem(206, 7),
            new RandomItem(208, 1, 3), new RandomItem(212, 2, 3), new RandomItem(214, 1, 4),
            new RandomItem(216, 2, 3), new RandomItem(220, 1, 3), new RandomItem(5321, 1, 7),
            new RandomItem(5293, 1, 3), new RandomItem(5294, 1, 3), new RandomItem(5295, 1, 3),
            new RandomItem(5296, 1, 3), new RandomItem(5298, 1, 3), new RandomItem(5300, 1, 4),
            new RandomItem(5303, 1, 3), new RandomItem(5312, 1), new RandomItem(5313, 1, 2),
            new RandomItem(5284, 1, 3), new RandomItem(21486, 1, 3), new RandomItem(5314, 1, 3),
            new RandomItem(21488, 1, 3), new RandomItem(5304, 1, 3), new RandomItem(5315, 1, 3),
            new RandomItem(5316, 1, 3), new RandomItem(5317, 1), new RandomItem(322, 5, 12),
            new RandomItem(336, 5, 12), new RandomItem(332, 5, 12), new RandomItem(378, 5, 11),
            new RandomItem(360, 5, 12), new RandomItem(372, 5, 21), new RandomItem(384, 5, 21),
            new RandomItem(ItemId.COINS, 2030, 9048), new RandomItem(13422, 3, 24),
            new RandomItem(3212, 4, 7), new RandomItem(7937, 29, 391),
            new RandomItem(13574, 3, 19)};
        player.getInventory().deleteItem(itemId, 1, slot);
        int supplyCount = 2 + PRandom.randomI(2);
        for (int i = 0; i < supplyCount; i++) {
          if (PRandom.inRange(player.getCombat().getDropRate(6739, 0.01))) {
            player.getInventory().addItem(6739, 1, slot);
          } else if (PRandom.inRange(player.getCombat().getDropRate(ItemId.PHOENIX, 0.02))) {
            player.getInventory().addItem(ItemId.PHOENIX, 1, slot);
            player.getWorld().sendItemDropNews(player, ItemId.PHOENIX, "a supply crate");
          } else if (PRandom.inRange(player.getCombat().getDropRate(20716, 0.1))) {
            player.getInventory().addItem(20716, 1, slot);
          } else if (PRandom.inRange(player.getCombat().getDropRate(20720, 0.66))) {
            player.getInventory().addItem(20720, 1, slot);
          } else if (PRandom.inRange(player.getCombat().getDropRate(20718, 2.2))) {
            player.getInventory().addItem(20718, 5 + PRandom.randomE(26), slot);
          } else {
            player.getInventory().addItem(RandomItem.getItem(randomItems), slot);
          }
        }
        break;
      case 20791: // Extra supply crate
        player.getInventory().deleteItem(itemId, 1, slot);
        if (PRandom.inRange(player.getCombat().getDropRate(6739, 0.01))) {
          player.getInventory().addItem(6739, 1, slot);
        } else if (PRandom.inRange(player.getCombat().getDropRate(ItemId.PHOENIX, 0.02))) {
          player.getInventory().addItem(ItemId.PHOENIX, 1, slot);
          player.getWorld().sendItemDropNews(player, ItemId.PHOENIX, "an extra supply crate");
        } else if (PRandom.inRange(player.getCombat().getDropRate(20716, 0.1))) {
          player.getInventory().addItem(20716, 1, slot);
        } else if (PRandom.inRange(player.getCombat().getDropRate(20720, 0.66))) {
          player.getInventory().addItem(20720, 1, slot);
        } else if (PRandom.inRange(player.getCombat().getDropRate(20718, 2.2))) {
          player.getInventory().addItem(20718, 5 + PRandom.randomE(26), slot);
        } else {
          randomItems = new RandomItem[] {new RandomItem(1522, 13, 148),
              new RandomItem(1520, 13, 20), new RandomItem(1518, 10, 16),
              new RandomItem(6334, 14, 59), new RandomItem(8836, 10, 48),
              new RandomItem(1516, 10, 49), new RandomItem(1514, 10, 23),
              new RandomItem(1624, 1, 5), new RandomItem(1622, 1, 5), new RandomItem(1618, 1, 5),
              new RandomItem(1620, 1, 5), new RandomItem(454, 3, 12), new RandomItem(441, 3, 15),
              new RandomItem(443, 3, 12), new RandomItem(445, 3, 70), new RandomItem(448, 2, 7),
              new RandomItem(450, 2, 15), new RandomItem(452, 1, 2), new RandomItem(200, 3, 6),
              new RandomItem(202, 3, 6), new RandomItem(206, 7), new RandomItem(208, 1, 3),
              new RandomItem(212, 2, 3), new RandomItem(214, 1, 4), new RandomItem(216, 2, 3),
              new RandomItem(220, 1, 3), new RandomItem(5321, 1, 7), new RandomItem(5293, 1, 3),
              new RandomItem(5294, 1, 3), new RandomItem(5295, 1, 3), new RandomItem(5296, 1, 3),
              new RandomItem(5298, 1, 3), new RandomItem(5300, 1, 4), new RandomItem(5303, 1, 3),
              new RandomItem(5312, 1), new RandomItem(5313, 1, 2), new RandomItem(5284, 1, 3),
              new RandomItem(21486, 1, 3), new RandomItem(5314, 1, 3), new RandomItem(21488, 1, 3),
              new RandomItem(5304, 1, 3), new RandomItem(5315, 1, 3), new RandomItem(5316, 1, 3),
              new RandomItem(5317, 1), new RandomItem(322, 5, 12), new RandomItem(336, 5, 12),
              new RandomItem(332, 5, 12), new RandomItem(378, 5, 11), new RandomItem(360, 5, 12),
              new RandomItem(372, 5, 21), new RandomItem(384, 5, 21),
              new RandomItem(ItemId.COINS, 2030, 9048), new RandomItem(13422, 3, 24),
              new RandomItem(3212, 4, 7), new RandomItem(7937, 29, 391),
              new RandomItem(13574, 3, 19)};
          player.getInventory().addItem(RandomItem.getItem(randomItems), slot);
        }
        break;
      case ItemId.MYSTERY_BOX:
      case ItemId.SUPER_MYSTERY_BOX_32286:
      case ItemId.PET_MYSTERY_BOX_32311:
        if (option == 0) {
          MysteryBox.open(player, itemId);
        } else if (option == 1) {
          MysteryBox.quickOpen(player, itemId);
        }
        break;
      case 19564: // Royal seed pod
        if (!player.getController().canTeleport(30, true)) {
          break;
        }
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        Tile homeTile = player.getWidgetManager().getHomeTile();
        if (height != 0) {
          homeTile = new Tile(World.DEFAULT_TILE).randomize(2);
          homeTile.setHeight(height);
        }
        if (player.getController().inPvPWorld()) {
          homeTile = new Tile(3093, 3495, height);
        }
        player.getMovement().animatedTeleport(homeTile, Magic.SEEDPOD_ANIMATION_START,
            Magic.SEEDPOD_ANIMATION_MID, Magic.SEEDPOD_ANIMATION_END, Magic.SEEDPOD_START_GRAPHIC,
            null, Magic.SEEDPOD_END_GRAPHIC, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 4251: // Ectophial
        if (!player.getController().canTeleport(true)) {
          break;
        }
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        Tile ectoTile = new Tile(3603, 3528, height);
        if (height != 0) {
          ectoTile = new Tile(World.DEFAULT_TILE).randomize(2);
          ectoTile.setHeight(height);
        }
        player.getMovement().animatedTeleport(ectoTile, Magic.ECTOPHIAL_ANIMATION, -1,
            Magic.ECTOPHIAL_GRAPHIC, null, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 5509: // Small pouch
      case 5510: // Medium pouch
      case 5511: // Medium pouch
      case 5512: // Large pouch
      case 5513: // Large pouch
      case 5514: // Giant pouch
      case 5515: // Giant pouch
        if (option == 0) {
          int pureEssenceCount = player.getInventory().getCount(7936);
          int addingAmount =
              player.getWidgetManager().getRCPouch(itemId).canAddAmount(7936, pureEssenceCount);
          if (pureEssenceCount == 0) {
            player.getGameEncoder().sendMessage("You have no pure essence.");
            break;
          } else if (addingAmount == 0) {
            player.getGameEncoder().sendMessage(
                "Your " + player.getWidgetManager().getRCPouch(itemId).getName() + " is full.");
            break;
          }
          player.getInventory().deleteItem(7936, addingAmount);
          player.getWidgetManager().getRCPouch(itemId).addItem(7936, addingAmount);
        } else if (option == 1) {
          int pureEssenceCount = player.getWidgetManager().getRCPouch(itemId).getCount(7936);
          int addingAmount = player.getInventory().canAddAmount(7936, pureEssenceCount);
          player.getWidgetManager().getRCPouch(itemId).deleteItem(7936, addingAmount);
          player.getInventory().addItem(7936, addingAmount);
        } else if (option == 2) {
          int pureEssenceCount = player.getWidgetManager().getRCPouch(itemId).getCount(7936);
          player.getGameEncoder()
              .sendMessage("Your " + player.getWidgetManager().getRCPouch(itemId).getName()
                  + " contains " + PNumber.formatNumber(pureEssenceCount) + " pure essence.");
        }
        break;
      case ItemId.PURPLE_SWEETS_4561:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.PURPLE_SWEETS, 1, slot);
        break;
      case ItemId.CLUE_SCROLL_EASY_2713:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.CORPOREAL_BEAST_TASKS_32301, 1, slot);
        break;
      case 12789: // Clue box
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(2677, 1).weight(8) /* Clue scroll (easy) */,
            new RandomItem(2801, 1).weight(6) /* Clue scroll (medium) */,
            new RandomItem(2722, 1).weight(4) /* Clue scroll (hard) */,
            new RandomItem(12073, 1).weight(2) /* Clue scroll (elite) */,
            new RandomItem(19835, 1).weight(1) /* Clue scroll (master) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        if (PRandom.randomE(4) == 0) {
          player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        }
        break;
      case 21730: // Black tourmaline core
        player.getGameEncoder().sendMessage(
            "Fallen from the centre of a Grotesque Guardian. This could be attached to a pair of Bandos boots...");
        break;
      case 13256: // Saradomin's light
        if (player.getCombat().getSaradominsLight()) {
          player.getGameEncoder().sendMessage("There is no reason to consume this.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().setSaradominsLight(true);
        player.getGameEncoder().sendMessage("You consume Saradomin's light.");
        player.getController().getVariable("saradomins_light");
        break;
      case 8015: // Bones to peaches
        int bonesCount = player.getInventory().getCount(526);
        int bigBonesCount = player.getInventory().getCount(532);
        if (bonesCount == 0 && bigBonesCount == 0) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().deleteItem(526, bonesCount);
        player.getInventory().deleteItem(532, bigBonesCount);
        player.getInventory().addItem(6883, bonesCount + bigBonesCount);
        break;
      case 12846: // Bounty teleport scroll
        if (player.getCombat().getBountyHunter().getTeleportUnlocked()) {
          player.getGameEncoder().sendMessage("You already have this spell unlocked.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getCombat().getBountyHunter().setTeleportUnlocked(true);
        player.getGameEncoder()
            .sendMessage("You have unlocked the Teleport to Bounty Target spell.");
        break;
      case Hunter.BIRD_SNARE_ITEM:
      case Hunter.BOX_TRAP_ITEM:
        player.getHunter().layTrap(itemId, null);
        break;
      case 748: // Legends Quest Holy force
        if (player.getCombat().getLegendsQuest() == 2 && player.getX() >= 2387
            && player.getX() <= 2397 && player.getY() >= 4673 && player.getY() <= 4689
            && player.getWorld().getTargetNPC(3962, player) == null) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getGameEncoder().sendMessage("You cast the spell, clensing the water.");
          player.getPrayer().adjustPoints(-99);
          Npc nezikchened =
              new Npc(player.getController(), 3962, new Tile(2396, 4678, player.getHeight()));
          nezikchened.setForceMessage("Now I am revealed to you, so shall ye perish.");
          nezikchened.getCombat().setTarget(player);
        } else {
          player.getGameEncoder().sendMessage("Nothing interesting happens.");
        }
        break;
      case 12641: // Amylase pack
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(12640, 100, slot);
        break;
      case 11738: // Herb box
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(199, 1) /* Guam */,
            new RandomItem(201, 1) /* Marrentill */, new RandomItem(203, 1) /* Tarromin */,
            new RandomItem(205, 1) /* Harralander */, new RandomItem(207, 1) /* Ranaar */,
            new RandomItem(3049, 1) /* Toadflax */, new RandomItem(209, 1) /* Irit */,
            new RandomItem(211, 1) /* Avantoe */, new RandomItem(213, 1) /* Kwuarm */,
            new RandomItem(3051, 1) /* Snapdragon */, new RandomItem(215, 1) /* Cadantine */,
            new RandomItem(2485, 1) /* Lantadyme */, new RandomItem(217, 1) /* Dwarf weed */,
            new RandomItem(219, 1) /* Torstol */
        };
        for (int i = 0; i < 30; i++) {
          Item herbItem = RandomItem.getItem(randomItems);
          player.getInventory().addOrDropItem(herbItem.getNotedId(), herbItem.getAmount());
        }
        break;
      case ItemId.BAR_BOX_32302:
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(2364, 1) /* Runite bar */,
            new RandomItem(2362, 1) /* Adamantite bar */, new RandomItem(2360, 1) /* Mithril bar */,
            new RandomItem(2354, 1) /* Steel bar */, new RandomItem(2352, 1) /* Iron bar */,
            new RandomItem(2350, 1) /* Bronze bar */
        };
        for (int i = 0; i < 15; i++) {
          Item barItem = RandomItem.getItem(randomItems);
          player.getInventory().addOrDropItem(barItem.getNotedId(), barItem.getAmount());
        }
        break;
      case ItemId.BAG_FULL_OF_GEMS:
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(ItemId.UNCUT_OPAL, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_JADE, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_RED_TOPAZ, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_SAPPHIRE, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_EMERALD, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_RUBY, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_DIAMOND, 1, 4).weight(1024),
            new RandomItem(ItemId.UNCUT_DRAGONSTONE, 1).weight(32),
            new RandomItem(ItemId.UNCUT_ONYX, 1).weight(8),
            new RandomItem(ItemId.ZENYTE_SHARD, 1).weight(1)};
        for (int i = 0; i < 20; i++) {
          Item gemItem = RandomItem.getItem(randomItems);
          player.getInventory().addOrDropItem(gemItem.getNotedId(), gemItem.getAmount());
        }
        break;
      case 21047: // Torn prayer scroll
        if (player.getPrayer().getPreserveUnlocked()) {
          player.getGameEncoder().sendMessage("You have already unlocked the prayer Preserve.");
          break;
        }
        player.getPrayer().setPreserveUnlocked(true);
        player.getInventory().deleteItem(itemId, 1, slot);
        break;
      case 21034: // Dexterous prayer scroll
        if (player.getPrayer().getRigourUnlocked()) {
          player.getGameEncoder().sendMessage("You have already unlocked the prayer Rigour.");
          break;
        }
        player.getPrayer().setRigourUnlocked(true);
        player.getInventory().deleteItem(itemId, 1, slot);
        break;
      case 21079: // Arcane prayer scroll
        if (player.getPrayer().getAuguryUnlocked()) {
          player.getGameEncoder().sendMessage("You have already unlocked the prayer Augury.");
          break;
        }
        player.getPrayer().setAuguryUnlocked(true);
        player.getInventory().deleteItem(itemId, 1, slot);
        break;
      case 21043: // Kodai insignia
        player.getGameEncoder().sendMessage("Insert buttplug meme.");
        break;
      case 1856: // Guide book
        player.openDialogue("guidebook", 0);
        break;
      case 13249: // Key master teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(1310, 1250), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 8007: // Varrock teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(3213, 3423), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 8008: // Lumbridge teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(3221, 3218), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 8009: // Falador teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2965, 3379), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 8010: // Camelot teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2725, 3485), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 8011: // Ardougne teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2664, 3306), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 11742: // Taverley teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2894, 3465), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 19625: // Harmony island teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(3797, 2866), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 11744: // Rellekka teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2670, 3632), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 11747: // Trollheim teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2829, 3685), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 6099: // Teleport crystal (4)
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(6100, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2352, 3162), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 6100: // Teleport crystal (3)
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(6101, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2352, 3162), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 6101: // Teleport crystal (2)
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(6102, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2352, 3162), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 6102: // Teleport crystal (1)
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(6103, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2352, 3162), Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 8013: // Teleport to house
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        homeTile = player.getWidgetManager().getHomeTile();
        if (height != 0) {
          homeTile = new Tile(World.DEFAULT_TILE).randomize(2);
          homeTile.setHeight(height);
        }
        if (player.getController().inPvPWorld()) {
          homeTile = new Tile(3093, 3495, height);
        }
        player.getMovement().animatedTeleport(homeTile, Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12781: // Paddewwa teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        player.getMovement().animatedTeleport(new Tile(3094, 3470, height),
            Magic.TABLET_ANIMATION_START, Magic.TABLET_ANIMATION_END, -1, null,
            Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12779: // Kharyrll teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        player.getMovement().animatedTeleport(new Tile(3510, 3480, height),
            Magic.TABLET_ANIMATION_START, Magic.TABLET_ANIMATION_END, -1, null,
            Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12938: // Zul-andra teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(2212, 3056, 0), 3864, new Graphic(1039), 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 952: // Spade
        if (player.getCombat().getBarrows().ahrimMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.AHRIM_THE_BLIGHTED_98);
        } else if (player.getCombat().getBarrows().dharokMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.DHAROK_THE_WRETCHED_115);
        } else if (player.getCombat().getBarrows().guthanMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.GUTHAN_THE_INFESTED_115);
        } else if (player.getCombat().getBarrows().karilMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.KARIL_THE_TAINTED_98);
        } else if (player.getCombat().getBarrows().toragMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.TORAG_THE_CORRUPTED_115);
        } else if (player.getCombat().getBarrows().veracMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.VERAC_THE_DEFILED_115);
        }
        break;
      case 6543: // Antique lamp
        if (!player.isGameModeNormal()) {
          player.getGameEncoder().sendMessage("You can't use this.");
          break;
        }
        player.openDialogue("bloodylamp", 0);
        int herbloreXP = (int) (12.5 * (player.getController().getLevelForXP(Skills.HERBLORE)
            * player.getController().getLevelForXP(Skills.HERBLORE)
            - player.getController().getLevelForXP(Skills.HERBLORE) * 2 + 100) * 2);
        int miningXP = (int) (12.5 * (player.getController().getLevelForXP(Skills.MINING)
            * player.getController().getLevelForXP(Skills.MINING)
            - player.getController().getLevelForXP(Skills.MINING) * 2 + 100) * 2);
        int smithingXP = (int) (12.5 * (player.getController().getLevelForXP(Skills.SMITHING)
            * player.getController().getLevelForXP(Skills.SMITHING)
            - player.getController().getLevelForXP(Skills.SMITHING) * 2 + 100) * 2);
        DialogueOld.setText(player, null, PNumber.formatNumber(herbloreXP) + " Herblore XP",
            PNumber.formatNumber(miningXP) + " Mining XP",
            PNumber.formatNumber(smithingXP) + " Smithing XP");
        break;
      case 4447: // Antique lamp
        if (!player.isGameModeNormal()) {
          player.getGameEncoder().sendMessage("You can't use this.");
          break;
        }
        player.openDialogue("combatlamp", 4);
        break;
      case 10586: // Combat lamp
        if (!player.isGameModeNormal()) {
          player.getGameEncoder().sendMessage("You can't use this.");
          break;
        }
        player.openDialogue("combatlamp", 0);
        break;
      case 10889: // Blessed lamp
        if (!player.isGameModeNormal()) {
          player.getGameEncoder().sendMessage("You can't use this.");
          break;
        }
        player.openDialogue("combatlamp", 3);
        break;
      case 9656: // Tome of xp (3)
      case 9657: // Tome of xp (2)
      case 9658: // Tome of xp (1)
        if (!player.isGameModeNormal()) {
          player.getGameEncoder().sendMessage("You can't use this.");
          break;
        }
        player.openDialogue("combatlamp", 1);
        break;
      case ItemId.BOND_32318:
        player.getBonds().setPouch(player.getBonds().getPouch() + item.getAmount());
        player.getInventory().deleteItem(itemId, item.getAmount(), slot);
        player.getGameEncoder().sendMessage("Your bonds have been added to your pouch.");
        break;
      case ItemId.OLD_SCHOOL_BOND:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.BOND_32318, 50);
        break;
      case ItemId.OLD_SCHOOL_BOND_UNTRADEABLE:
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId._14_DAYS_PREMIUM_MEMBERSHIP_32303, 1, slot);
        break;
      case ItemId._14_DAYS_PREMIUM_MEMBERSHIP_32303:
        player.openDialogue("bond", 0);
        break;
      case 12791: // Rune pouch
        if (option == 0) {
          player.getMagic().openRunePouch();
        } else if (option == 3) {
          player.getMagic().removeRunesFromPouch(0, Magic.MAX_RUNE_POUCH_AMOUNT);
          player.getMagic().removeRunesFromPouch(1, Magic.MAX_RUNE_POUCH_AMOUNT);
          player.getMagic().removeRunesFromPouch(2, Magic.MAX_RUNE_POUCH_AMOUNT);
        }
        break;
      case 20164: // Large spade
        if (player.getCombat().getBarrows().ahrimMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.AHRIM_THE_BLIGHTED_98);
        } else if (player.getCombat().getBarrows().dharokMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.DHAROK_THE_WRETCHED_115);
        } else if (player.getCombat().getBarrows().guthanMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.GUTHAN_THE_INFESTED_115);
        } else if (player.getCombat().getBarrows().karilMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.KARIL_THE_TAINTED_98);
        } else if (player.getCombat().getBarrows().toragMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.TORAG_THE_CORRUPTED_115);
        } else if (player.getCombat().getBarrows().veracMound()) {
          player.getCombat().getBarrows().enterCrypt(NpcId.VERAC_THE_DEFILED_115);
        }
        break;
      case 4566: // Rubber chicken
        player.setAnimation(1835);
        break;
      case ItemId.TRIDENT_OF_THE_SEAS_FULL:
        if (option == 3) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(ItemId.UNCHARGED_TRIDENT, 1, slot);
          player.getInventory().addOrDropItem(ItemId.DEATH_RUNE, 2500);
          player.getInventory().addOrDropItem(ItemId.CHAOS_RUNE, 2500);
          player.getInventory().addOrDropItem(ItemId.FIRE_RUNE, 12500);
        }
        break;
      case ItemId.TRIDENT_OF_THE_SEAS:
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 3) {
          var charges = item.getCharges();
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(ItemId.UNCHARGED_TRIDENT, 1, slot);
          player.getInventory().addOrDropItem(ItemId.DEATH_RUNE, charges);
          player.getInventory().addOrDropItem(ItemId.CHAOS_RUNE, charges);
          player.getInventory().addOrDropItem(ItemId.FIRE_RUNE, charges * 5);
        }
        break;
      case ItemId.TRIDENT_OF_THE_SEAS_E:
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 3) {
          var charges = item.getCharges();
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(ItemId.UNCHARGED_TRIDENT_E, 1, slot);
          player.getInventory().addItem(ItemId.UNCHARGED_TRIDENT, 1, slot);
          player.getInventory().addOrDropItem(ItemId.DEATH_RUNE, charges);
          player.getInventory().addOrDropItem(ItemId.CHAOS_RUNE, charges);
          player.getInventory().addOrDropItem(ItemId.FIRE_RUNE, charges * 5);
        }
        break;
      case 11908: // Uncharged trident
      case 22290: // Uncharged trident (e)
        player.getGameEncoder().sendMessage(
            "Each charge requires 1 death rune, 1 chaos rune, 5 fire runes and 10 coins.");
        break;
      case 12900: // Uncharged toxic trident
        if (option == 2) {
          player.getGameEncoder().sendMessage(
              "Each charge requires 1 death rune, 1 chaos rune, 5 fire runes, 10 coins and 1 Zulrah's scale.");
        } else if (option == 3) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(11908, 1);
          player.getInventory().addItem(12932, 1);
        }
        break;
      case 22294: // Uncharged toxic trident (e)
        if (option == 2) {
          player.getGameEncoder().sendMessage(
              "Each charge requires 1 death rune, 1 chaos rune, 5 fire runes, 10 coins and 1 Zulrah's scale.");
        } else if (option == 3) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(22290, 1);
          player.getInventory().addItem(12932, 1);
        }
        break;
      case 9753: // Defence cape
      case 9754: // Defence cape (t)
        player.getEquipment().setDefenceCapeEffect(!player.getEquipment().getDefenceCapeEffect());
        player.getGameEncoder()
            .sendMessage("Ring of life: " + player.getEquipment().getDefenceCapeEffect());
        break;
      case 5521: // Binding necklace
        player.getGameEncoder().sendMessage("Your Binding Necklace has "
            + player.getSkills().getBindingNecklace() + " charges remaining.");
        break;
      case 11238: // Baby impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(1755, 1) /* Chisel */,
            new RandomItem(1734, 1) /* Thread */, new RandomItem(946, 1) /* Knife */,
            new RandomItem(1985, 1) /* Cheese */, new RandomItem(2347, 1) /* Hammer */,
            new RandomItem(1759, 1) /* Ball of wool */,
            new RandomItem(1927, 1) /* Bucket of milk */, new RandomItem(319, 1) /* Anchovies */,
            new RandomItem(2007, 1) /* Spice */, new RandomItem(1779, 1) /* Flax */,
            new RandomItem(7170, 1) /* Mud pie */, new RandomItem(401, 1) /* Seaweed */,
            new RandomItem(1438, 1) /* Air talisman */, new RandomItem(2355, 1) /* Silver bar */,
            new RandomItem(1607, 1) /* Sapphire */, new RandomItem(1743, 1) /* Hard leather */,
            new RandomItem(379, 1) /* Lobster */, new RandomItem(1761, 1) /* Soft clay */,
            new RandomItem(2677, 1, 1).weight(4) /* Clue scroll (easy) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11240: // Young impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(1539, 5) /* Steel nails */,
            new RandomItem(1901, 1) /* Chocolate slice */,
            new RandomItem(7936, 1) /* Pure essence */, new RandomItem(1523, 1) /* Lockpick */,
            new RandomItem(361, 1) /* Tuna */, new RandomItem(453, 1) /* Coal */,
            new RandomItem(1777, 1) /* Bow string */, new RandomItem(1353, 1) /* Steel axe */,
            new RandomItem(1157, 1) /* Steel full helm */,
            new RandomItem(1097, 1) /* Studded chaps */, new RandomItem(2293, 1) /* Meat pizza */,
            new RandomItem(247, 1) /* Jangerberries */,
            new RandomItem(2677, 1, 1).weight(4) /* Clue scroll (easy) */,
            new RandomItem(2359, 1) /* Mithril bar */, new RandomItem(231, 1) /* Snape grass */,
            new RandomItem(2432, 1) /* Defence potion(4) */,
            new RandomItem(855, 1) /* Yew longbow */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11242: // Gourmet impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(365, 1) /* Bass */,
            new RandomItem(361, 1) /* Tuna */, new RandomItem(2011, 1) /* Curry */,
            new RandomItem(2327, 1) /* Meat pie */, new RandomItem(1897, 1) /* Chocolate cake */,
            new RandomItem(5004, 1) /* Frog spawn */, new RandomItem(2007, 1) /* Spice */,
            new RandomItem(5970, 1) /* Curry leaf */,
            new RandomItem(2677, 1, 1).weight(4) /* Clue scroll (easy) */,
            new RandomItem(1883, 1) /* Ugthanki kebab */,
            new RandomItem(380, 4) /* Lobster (noted) */,
            new RandomItem(386, 3) /* Shark (noted) */, new RandomItem(7188, 1) /* Fish pie */,
            new RandomItem(7754, 1) /* Chef's delight */,
            new RandomItem(10137, 5) /* Rainbow fish (noted) */,
            new RandomItem(7179, 6) /* Garden pie (noted) */,
            new RandomItem(374, 3) /* Swordfish (noted) */,
            new RandomItem(5406, 1) /* Strawberries(5) */,
            new RandomItem(3145, 2) /* Cooked karambwan (noted) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11244: // Earth impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(1442, 1) /* Fire talisman */,
            new RandomItem(1440, 1) /* Earth talisman */, new RandomItem(5535, 1) /* Earth tiara */,
            new RandomItem(557, 32) /* Earth rune */,
            new RandomItem(448, 1, 3) /* Mithril ore (noted) */,
            new RandomItem(237, 1) /* Unicorn horn */, new RandomItem(2353, 1) /* Steel bar */,
            new RandomItem(1273, 1) /* Mithril pickaxe */,
            new RandomItem(5311, 2) /* Wildblood seed */,
            new RandomItem(5104, 2) /* Jangerberry seed */,
            new RandomItem(6033, 6) /* Compost (noted) */,
            new RandomItem(6035, 2) /* Supercompost (noted) */,
            new RandomItem(1784, 4) /* Bucket of sand (noted) */,
            new RandomItem(5294, 2) /* Harralander seed */,
            new RandomItem(454, 2) /* Coal (noted) */, new RandomItem(444, 1) /* Gold ore */,
            new RandomItem(1622, 2) /* Uncut emerald (noted) */,
            new RandomItem(1606, 2) /* Emerald (noted) */, new RandomItem(1603, 1) /* Ruby */,
            new RandomItem(2801, 1, 1).weight(4) /* Clue scroll (medium) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11246: // Essence impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(7937, 20, 35) /* Pure essence (noted) */,
            new RandomItem(555, 30) /* Water rune */, new RandomItem(556, 30) /* Air rune */,
            new RandomItem(554, 50) /* Fire rune */, new RandomItem(558, 25) /* Mind rune */,
            new RandomItem(559, 28) /* Body rune */, new RandomItem(562, 4) /* Chaos rune */,
            new RandomItem(1448, 1) /* Mind talisman */, new RandomItem(4699, 4) /* Lava rune */,
            new RandomItem(4698, 4) /* Mud rune */, new RandomItem(4697, 4) /* Smoke rune */,
            new RandomItem(4694, 4) /* Steam rune */, new RandomItem(564, 4) /* Cosmic rune */,
            new RandomItem(560, 13) /* Death rune */, new RandomItem(563, 13) /* Law rune */,
            new RandomItem(565, 7) /* Blood rune */, new RandomItem(566, 11) /* Soul rune */,
            new RandomItem(561, 13) /* Nature rune */,
            new RandomItem(2801, 1, 1).weight(4) /* Clue scroll (medium) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11248: // Eclectic impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(1273, 1) /* Mithril pickaxe */,
            new RandomItem(1199, 1) /* Adamant kiteshield */,
            new RandomItem(2493, 1) /* Blue d'hide chaps */,
            new RandomItem(10083, 1) /* Red spiky vambs */,
            new RandomItem(1213, 1) /* Rune dagger */, new RandomItem(1391, 1) /* Battlestaff */,
            new RandomItem(5970, 1) /* Curry leaf */, new RandomItem(231, 1) /* Snape grass */,
            new RandomItem(556, 30, 57) /* Air rune */,
            new RandomItem(8779, 4) /* Oak plank (noted) */,
            new RandomItem(4529, 1) /* Candle lantern (empty) */,
            new RandomItem(444, 1) /* Gold ore */, new RandomItem(2358, 5) /* Gold bar (noted) */,
            new RandomItem(237, 1) /* Unicorn horn */,
            new RandomItem(450, 5) /* Adamantite ore (noted) */,
            new RandomItem(5760, 2) /* Slayer's respite (noted) */,
            new RandomItem(7208, 1) /* Wild pie */, new RandomItem(5321, 3) /* Watermelon seed */,
            new RandomItem(2801, 1, 1).weight(4) /* Clue scroll (medium) */,
            new RandomItem(1601, 1) /* Diamond */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11250: // Nature impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(5100, 1) /* Limpwurt seed */,
            new RandomItem(5104, 1) /* Jangerberry seed */,
            new RandomItem(5281, 1) /* Belladonna seed */,
            new RandomItem(5294, 1) /* Harralander seed */,
            new RandomItem(6016, 1) /* Cactus spine */, new RandomItem(1513, 1) /* Magic logs */,
            new RandomItem(204, 4) /* Grimy tarromin (noted) */,
            new RandomItem(5286, 1) /* Curry tree seed */,
            new RandomItem(5285, 1) /* Orange tree seed */,
            new RandomItem(3051, 1) /* Grimy snapdragon */, new RandomItem(5974, 1) /* Coconut */,
            new RandomItem(5297, 1) /* Irit seed */, new RandomItem(5299, 1) /* Kwuarm seed */,
            new RandomItem(5298, 5) /* Avantoe seed */, new RandomItem(5313, 1) /* Willow seed */,
            new RandomItem(5304, 1) /* Torstol seed */, new RandomItem(5295, 1) /* Ranarr seed */,
            new RandomItem(2722, 1, 1).weight(4) /* Clue scroll (hard) */,
            new RandomItem(220, 2) /* Grimy Torstol (noted) */,
            new RandomItem(5303, 1) /* Dwarf weed seed */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11252: // Magpie impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(1701, 3) /* Diamond amulet (noted) */,
            new RandomItem(1732, 3) /* Amulet of power (noted) */,
            new RandomItem(2569, 3) /* Ring of forging (noted) */,
            new RandomItem(3391, 1) /* Splitbark gauntlets */,
            new RandomItem(4097, 1) /* Mystic boots */, new RandomItem(4095, 1) /* Mystic gloves */,
            new RandomItem(1347, 1) /* Rune warhammer */,
            new RandomItem(2571, 4) /* Ring of life (noted) */,
            new RandomItem(1185, 1) /* Rune sq shield */,
            new RandomItem(1215, 1) /* Dragon dagger */, new RandomItem(5541, 1) /* Nature tiara */,
            new RandomItem(1748, 6) /* Black dragonhide (noted) */,
            new RandomItem(2722, 1, 1).weight(4) /* Clue scroll (hard) */,
            new RandomItem(2364, 2) /* Runite bar (noted) */,
            new RandomItem(1602, 4) /* Diamond (noted) */,
            new RandomItem(5287, 1) /* Pineapple seed */,
            new RandomItem(987, 1) /* Loop half of key */,
            new RandomItem(985, 1) /* Tooth half of key */,
            new RandomItem(993, 1) /* Sinister key */, new RandomItem(5300, 1) /* Snapdragon seed */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11254: // Ninja impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(6328, 1) /* Snakeskin boots */,
            new RandomItem(3391, 1) /* Splitbark gauntlets */,
            new RandomItem(4097, 1) /* Mystic boots */,
            new RandomItem(3385, 1) /* Splitbark helm */,
            new RandomItem(1113, 1) /* Rune chainbody */,
            new RandomItem(6313, 1) /* Opal machete */, new RandomItem(3101, 1) /* Rune claws */,
            new RandomItem(1333, 1) /* Rune scimitar */,
            new RandomItem(1347, 1) /* Rune warhammer */,
            new RandomItem(1215, 1) /* Dragon dagger */, new RandomItem(892, 70) /* Rune arrow */,
            new RandomItem(811, 70) /* Rune dart */, new RandomItem(868, 40) /* Rune knife */,
            new RandomItem(805, 50) /* Rune thrownaxe */,
            new RandomItem(9245, 2) /* Onyx bolts (e) */,
            new RandomItem(1748, 10, 16) /* Black dragonhide (noted) */,
            new RandomItem(140, 4) /* Prayer potion(3) (noted) */,
            new RandomItem(5941, 4) /* Weapon poison(++) (noted) */,
            new RandomItem(6156, 3) /* Dagannoth hide (noted) */,
            new RandomItem(2722, 1, 1).weight(4) /* Clue scroll (hard) */,
            new RandomItem(2364, 4) /* Runite bar (noted) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 11256: // Dragon impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems = new RandomItem[] {new RandomItem(11232, 100, 350) /* Dragon dart tip */,
            new RandomItem(11237, 100, 500) /* Dragon arrowtips */,
            new RandomItem(9193, 10, 49) /* Dragon bolt tips */,
            new RandomItem(19582, 25, 35) /* Dragon javelin heads */,
            new RandomItem(11212, 100, 500).weight(4) /* Dragon arrow */,
            new RandomItem(9244, 3, 40).weight(4) /* Dragon bolts (e) */,
            new RandomItem(11230, 100, 350).weight(4) /* Dragon dart */,
            new RandomItem(4093, 1, 1).weight(2) /* Mystic robe bottom */,
            new RandomItem(1713, 2, 3).weight(2) /* Amulet of glory(4) (noted) */,
            new RandomItem(1703, 2, 3).weight(2) /* Dragonstone amulet (noted) */,
            new RandomItem(1305, 1, 1).weight(2) /* Dragon longsword */,
            new RandomItem(5699, 3, 3).weight(2) /* Dragon dagger(p++) (noted) */,
            new RandomItem(535, 100, 300).weight(2) /* Babydragon bones (noted) */,
            new RandomItem(5316, 1, 1).weight(2) /* Magic seed */,
            new RandomItem(537, 50, 100).weight(2) /* Dragon bones (noted) */,
            new RandomItem(1616, 3, 6).weight(2) /* Dragonstone (noted) */,
            new RandomItem(5300, 5, 5).weight(2) /* Snapdragon seed */,
            new RandomItem(7219, 15, 15).weight(2) /* Summer pie (noted) */,
            new RandomItem(12073, 1, 1).weight(2) /* Clue scroll (elite) */
        };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 19732: // Lucky impling jar
        player.getInventory().deleteItem(itemId, 1, slot);
        randomItems =
            new RandomItem[] {new RandomItem(2677, 1, 1).weight(8) /* Clue scroll (easy) */,
                new RandomItem(2801, 1, 1).weight(6) /* Clue scroll (medium) */,
                new RandomItem(2722, 1, 1).weight(4) /* Clue scroll (hard) */,
                new RandomItem(12073, 1, 1).weight(2) /* Clue scroll (elite) */,
                new RandomItem(19835, 1, 1).weight(1) /* Clue scroll (master) */
            };
        player.getInventory().addOrDropItem(RandomItem.getItem(randomItems));
        break;
      case 19675: // Arclight
      case 13241: // Infernal axe
      case 21031: // Infernal harpoon
      case 13243: // Infernal pickaxe
      case 21255: // Slayer's staff (e)
      case ItemId.VESTAS_LONGSWORD_CHARGED_32254:
      case ItemId.STATIUSS_WARHAMMER_CHARGED_32255:
      case ItemId.VESTAS_SPEAR_CHARGED_32256:
      case ItemId.ZURIELS_STAFF_CHARGED_32257:
      case ItemId.ARMADYL_GODSWORD_BEGINNER_32326:
      case ItemId.DRAGON_CLAWS_BEGINNER_32327:
      case ItemId.HEAVY_BALLISTA_BEGINNER_32328:
        player.getCharges().checkCharges(slot);
        break;
      case ItemId.VESTAS_LONGSWORD:
      case ItemId.STATIUSS_WARHAMMER:
      case ItemId.VESTAS_SPEAR:
      case ItemId.ZURIELS_STAFF:
        player.getGameEncoder().sendMessage("This item needs to be charged with coins.");
        break;
      case 22545: // Viggora's chainmace
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 4) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().addItem(21820, item.getCharges());
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(22542, 1, slot);
        }
        break;
      case 22550: // Craw's bow
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 4) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().addItem(21820, item.getCharges());
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(22547, 1, slot);
        }
        break;
      case 22555: // Thammaron's sceptre
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 4) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().addItem(21820, item.getCharges());
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(22552, 1, slot);
        }
        break;
      case 12899: // Trident of the swamp
      case 22292: // Trident of the swamp (e)
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 3) {
          player.getCharges().unloadToxicTrident(slot);
        }
        break;
      case 22323: // Sanguinesti staff
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 3) {
          player.getGameEncoder().sendMessage("This is charged with blood runes.");
        } else if (option == 4) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(22481, 1, slot);
        }
        break;
      case 22325: // Scythe of vitur
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 3) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(22486, 1, slot);
        }
        break;
      case 21816: // Bracelet of ethereum
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 3) {
          player.getCharges().setEthereumAutoAbsorb(!player.getCharges().getEthereumAutoAbsorb());
          player.getGameEncoder().sendMessage(
              "Ether automatic absorption: " + player.getCharges().getEthereumAutoAbsorb());
        } else if (option == 4) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().addItem(21820, item.getCharges());
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(21817, 1, slot);
        }
        break;
      case 21817: // Bracelet of ethereum (uncharged)
        if (option == 2) {
          player.getCharges().setEthereumAutoAbsorb(!player.getCharges().getEthereumAutoAbsorb());
          player.getGameEncoder().sendMessage(
              "Ether automatic absorption: " + player.getCharges().getEthereumAutoAbsorb());
        } else if (option == 3) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(21820, 250, slot);
        }
        break;
      case 20714: // Tome of fire
        if (option == 2) {
          player.getGameEncoder().sendMessage("This book has unlimited charges.");
        } else if (option == 3) {
          if (player.getInventory().getRemainingSlots() < 1) {
            player.getInventory().notEnoughSpace();
            break;
          }
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(20716, 1, slot);
          player.getInventory().addItem(20718, 10);
        }
        break;
      case 7510: // Dwarven rock cake
        player.putAttribute("guzzle_dwarven_rock_cake", true);
        player.getWidgetManager().removeInteractiveWidgets();
        player.getSkills().eatFood(slot);
        break;
      case 12904: // Toxic staff of the dead
        if (option == 2) {
          player.getCharges().checkToxicStaff(slot);
        } else if (option == 4) {
          player.getCharges().unloadToxicStaff(slot);
        }
        break;
      case 12926: // Toxic blowpipe
        if (option == 2) {
          player.getCharges().checkToxicBlowpipe(slot);
        } else if (option == 3) {
          player.getCharges().unloadToxicBlowpipe(slot, false);
        } else if (option == 4) {
          player.getCharges().unloadToxicBlowpipe(slot, true);
        }
        break;
      case 12931: // Serpentine helm
      case 13197: // Tanzanite helm
      case 13199: // Magma helm
        if (option == 2) {
          player.getCharges().checkSerpentineHelm(slot);
        } else if (option == 4) {
          player.getCharges().unloadSerpentineHelm(slot);
        }
        break;
      case 12785: // Ring of wealth (i)
        if (option == 2) {
          Guide.openEntry(player, "main", "bonds");
        } else if (option == 3) {
          player.openDialogue("ringwealth", 0);
        }
        break;
      case 13121: // Ardougne Cloak 1
        if (!player.getController().canTeleport(30, true)) {
          break;
        }
        Tile ardougneCloak1Teleport = null;
        if (option == 2) {
          ardougneCloak1Teleport = new Tile(2606, 3223);
        }
        if (ardougneCloak1Teleport == null) {
          break;
        }
        player.getMovement().animatedTeleport(ardougneCloak1Teleport,
            Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
            Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 13122: // Ardougne Cloak 2
      case 13123: // Ardougne Cloak 3
      case 13124: // Ardougne Cloak 4
        if (!player.getController().canTeleport(30, true)) {
          break;
        }
        Tile ardougneCloakTeleport = null;
        if (option == 2) {
          ardougneCloakTeleport = new Tile(2606, 3223);
        } else if (option == 3) {
          ardougneCloakTeleport = new Tile(2673, 3374);
        }
        if (ardougneCloakTeleport == null) {
          break;
        }
        player.getMovement().animatedTeleport(ardougneCloakTeleport,
            Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
            Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 13280: // Max cape
      case 13342: // Max cape
        if (player.getEquipment().meetsMaxCapeRequirements(true)) {
          if (!player.getController().canTeleport(30, true)) {
            break;
          }
          if (option == 2) {
            player.openDialogue(new MaxCapeDialogue(player));
          } else if (option == 3) {
            player.getGameEncoder()
                .sendMessage("There are currently no features. Feel free to suggest some!");
          }
          break;
        } else {
          break;
        }
      case 9780: // Crafting cape
      case 9781: // Crafting cape
        if (!player.getController().canTeleport(20, true)) {
          return;
        }
        Tile craftingGuildTeleport = new Tile(2935, 3283);
        player.getMovement().animatedTeleport(craftingGuildTeleport,
            Magic.NORMAL_MAGIC_ANIMATION_START, Magic.NORMAL_MAGIC_ANIMATION_END,
            Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 22481: // Sanguinesti staff (uncharged)
        player.getGameEncoder().sendMessage("This is charged with blood runes.");
        break;
      case 22486: // Scythe of vitur (uncharged)
        player.getGameEncoder().sendMessage("This is charged with vials of blood and blood runes.");
        break;
      case ItemId.TOXIC_STAFF_UNCHARGED:
        if (player.getInventory().getRemainingSlots() < 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(ItemId.STAFF_OF_THE_DEAD, 1, slot);
        player.getInventory().addItem(ItemId.MAGIC_FANG, 1);
        break;
      case ItemId.RING_OF_SUFFERING_I:
      case ItemId.SEERS_RING_I:
      case ItemId.ARCHERS_RING_I:
      case ItemId.WARRIOR_RING_I:
      case ItemId.BERSERKER_RING_I:
      case ItemId.TYRANNICAL_RING_I:
      case ItemId.TREASONOUS_RING_I:
      case ItemId.RING_OF_THE_GODS_I:
      case ItemId.BLACK_MASK_10_I:
      case ItemId.DRAGON_CHAINBODY_G:
      case ItemId.DRAGON_PLATELEGS_G:
      case ItemId.DRAGON_PLATESKIRT_G:
      case ItemId.DRAGON_SQ_SHIELD_G:
      case ItemId.DRAGON_SCIMITAR_OR:
      case ItemId.DRAGON_DEFENDER_T:
      case ItemId.DARK_INFINITY_HAT:
      case ItemId.DARK_INFINITY_TOP:
      case ItemId.DARK_INFINITY_BOTTOMS:
      case ItemId.LIGHT_INFINITY_HAT:
      case ItemId.LIGHT_INFINITY_TOP:
      case ItemId.LIGHT_INFINITY_BOTTOMS:
      case ItemId.AMULET_OF_FURY_OR:
      case ItemId.AMULET_OF_TORTURE_OR:
      case ItemId.OCCULT_NECKLACE_OR:
      case ItemId.DRAGON_FULL_HELM_G:
      case ItemId.ARMADYL_GODSWORD_OR:
      case ItemId.BANDOS_GODSWORD_OR:
      case ItemId.SARADOMIN_GODSWORD_OR:
      case ItemId.ZAMORAK_GODSWORD_OR:
      case ItemId.DRAGON_BOOTS_G:
      case ItemId.DRAGON_PLATEBODY_G:
      case ItemId.DRAGON_KITESHIELD_G:
      case ItemId.NECKLACE_OF_ANGUISH_OR:
      case ItemId.ODIUM_WARD_12807:
      case ItemId.MALEDICTION_WARD_12806:
      case ItemId.RUNE_DEFENDER_T:
      case ItemId.TZHAAR_KET_OM_T:
      case ItemId.BERSERKER_NECKLACE_OR:
      case ItemId.RUNE_SCIMITAR_23330:
      case ItemId.RUNE_SCIMITAR_23332:
      case ItemId.RUNE_SCIMITAR_23334:
      case ItemId.TORMENTED_BRACELET_OR:
        if (item.getDef().getExchangeIds() == null) {
          break;
        } else if (player.getInventory().getRemainingSlots() < item.getDef().getExchangeIds().length
            - 1) {
          player.getInventory().notEnoughSpace();
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        for (int exchangeId : item.getDef().getExchangeIds()) {
          player.getInventory().addItem(exchangeId, 1, slot);
        }
        break;
      case ItemId.AIR_TALISMAN:
      case ItemId.EARTH_TALISMAN:
      case ItemId.FIRE_TALISMAN:
      case ItemId.WATER_TALISMAN:
      case ItemId.BODY_TALISMAN:
      case ItemId.MIND_TALISMAN:
      case ItemId.CHAOS_TALISMAN:
      case ItemId.COSMIC_TALISMAN:
      case ItemId.DEATH_TALISMAN:
      case ItemId.NATURE_TALISMAN:
      case ItemId.WRATH_TALISMAN:
        Runecrafting.talismanTeleport(player, Runecrafting.Altar.getByTalisman(itemId));
        break;
      case 5516: // Elemental talisman
        player.openDialogue("runecrafting", 0);
        break;
      case 21802: // Revenant cave teleport
        if (!player.getController().canTeleport(true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(new Tile(3128, 3832, 0), 3864, new Graphic(1039), 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 10014: // Black warlock
      case 10016: // Snowy knight
      case 10018: // Sapphire glacialis
      case 10020: // Ruby harvest
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(10012, 1, slot);
        break;
      case 2550: // Ring of recoil
        player.getCharges().setRingOfRecoil(ItemCharges.RING_OF_RECOIL);
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getGameEncoder().sendMessage("Your Ring of Recoil has shattered.");
        break;
      case 12776: // Carrallangar teleport
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        tile = new Tile(3175, 3669, height);
        if (!player.getController().canTeleport(tile, true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(tile, Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12777: // Dareeyak teleport
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        tile = new Tile(2968, 3695, height);
        if (!player.getController().canTeleport(tile, true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(tile, Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12775: // Annakarl teleport
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        tile = new Tile(3290, 3886, height);
        if (!player.getController().canTeleport(tile, true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(tile, Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12778: // Ghorrock teleport
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        tile = new Tile(2974, 3873, height);
        if (!player.getController().canTeleport(tile, true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(tile, Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 12642:
        if ((player.inEdgeville() || player.getController().inWilderness())
            && player.getClientHeight() == 0) {
          height = player.getHeight();
        }
        tile = new Tile(3303, 3488, height);
        if (!player.getController().canTeleport(tile, true)) {
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getMovement().animatedTeleport(tile, Magic.TABLET_ANIMATION_START,
            Magic.TABLET_ANIMATION_END, -1, null, Magic.TABLET_GRAPHIC, null, 0, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
        break;
      case 1704: // Amulet of glory
      case 1706: // Amulet of glory(1)
      case 1708: // Amulet of glory(2)
      case 1710: // Amulet of glory(3)
      case 1712: // Amulet of glory(4)
      case 10362: // Amulet of glory (t)
      case 10360: // Amulet of glory (t1)
      case 10358: // Amulet of glory (t2)
      case 10356: // Amulet of glory (t3)
      case 10354: // Amulet of glory (t4)
      case 19707: // Amulet of eternal glory
        player.openDialogue("amuletofglory", 0);
        break;
        case ItemId.BASILISK_JAW: // Basilisk Jaw
        player.getGameEncoder().sendMessage("It's the jaw of a Basilisk Knight. Perhaps I should try to use it on a Helm of Neitiznot.");
        break;
      case 3853: // Games necklace
        player.openDialogue("gamesnecklace", 0);
        break;
      case 20716: // Tome of fire (empty)
        if (player.getInventory().getCount(20718) < 10) {
          player.getGameEncoder().sendMessage("You need 10 burnt pages to do this.");
          break;
        }
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().deleteItem(20718, 10);
        player.getInventory().addItem(20714, 1, slot);
        break;
      case ItemId.ABYSSAL_TENTACLE:
        if (option == 3) {
          player.getCharges().checkCharges(slot);
        } else if (option == 4) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(ItemId.KRAKEN_TENTACLE, 1, slot);
        }
        break;
      case ItemId.SARAS_BLESSED_SWORD_FULL:
      case ItemId.SARADOMINS_BLESSED_SWORD:
        if (option == 2) {
          player.getCharges().checkCharges(slot);
        } else if (option == 4) {
          player.getInventory().deleteItem(itemId, 1, slot);
          player.getInventory().addItem(ItemId.SARADOMINS_TEAR, 1, slot);
          if (itemId == ItemId.SARAS_BLESSED_SWORD_FULL) {
            player.getInventory().addOrDropItem(ItemId.SARADOMIN_SWORD, 1);
          }
        }
        break;
      case 12922: // Tanzanite fang
      case 12932: // Magic fang
      case 12927: // Serpentine visage
      case 12929: // Serpentine helm (uncharged)
      case 12924: // Toxic blowpipe (empty)
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addItem(12934, 20000);
        break;
      case 10146: // Orange salamander
      case 10147: // Red salamander
      case 10148: // Black salamander
      case 10149: // Swamp lizard
        player.getInventory().deleteItem(itemId, 1, slot);
        break;
        case ItemId.NEITIZNOT_FACEGUARD: // Toxic blowpipe (empty)
        player.getInventory().deleteItem(itemId, 1, slot);
        player.getInventory().addOrDropItem(ItemId.BASILISK_JAW);
        player.getInventory().addOrDropItem(ItemId.HELM_OF_NEITIZNOT);
        break;
      case ItemId.CLUE_SCROLL_EASY:
      case ItemId.CLUE_BOTTLE_EASY:
      case ItemId.CLUE_GEODE_EASY:
      case ItemId.CLUE_NEST_EASY:
        ttLoot = new int[] {ItemId.HOLY_BLESSING, ItemId.UNHOLY_BLESSING, ItemId.PEACEFUL_BLESSING,
            ItemId.HONOURABLE_BLESSING, ItemId.WAR_BLESSING, ItemId.ANCIENT_BLESSING,
            ItemId.WILLOW_COMP_BOW, ItemId.YEW_COMP_BOW, ItemId.MAGIC_COMP_BOW, ItemId.BEAR_FEET,
            ItemId.MOLE_SLIPPERS, ItemId.FROG_SLIPPERS, ItemId.DEMON_FEET, ItemId.SANDWICH_LADY_HAT,
            ItemId.SANDWICH_LADY_TOP, ItemId.SANDWICH_LADY_BOTTOM,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX, ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK, ItemId.MONKS_ROBE_TOP_T, ItemId.MONKS_ROBE_T,
            ItemId.AMULET_OF_DEFENCE_T, ItemId.JESTER_CAPE, ItemId.SHOULDER_PARROT,
            ItemId.PURPLE_SWEETS};
        int[] ttEasy = new int[] {ItemId.BRONZE_FULL_HELM_T, ItemId.BRONZE_PLATEBODY_T,
            ItemId.BRONZE_PLATELEGS_T, ItemId.BRONZE_PLATESKIRT_T, ItemId.BRONZE_KITESHIELD_T,
            ItemId.BRONZE_FULL_HELM_G, ItemId.BRONZE_PLATEBODY_G, ItemId.BRONZE_PLATELEGS_G,
            ItemId.BRONZE_PLATESKIRT_G, ItemId.BRONZE_KITESHIELD_G, ItemId.IRON_FULL_HELM_T,
            ItemId.IRON_PLATEBODY_T, ItemId.IRON_PLATELEGS_T, ItemId.IRON_PLATESKIRT_T,
            ItemId.IRON_KITESHIELD_T, ItemId.IRON_FULL_HELM_G, ItemId.IRON_PLATEBODY_G,
            ItemId.IRON_PLATELEGS_G, ItemId.IRON_PLATESKIRT_G, ItemId.IRON_KITESHIELD_G,
            ItemId.STEEL_FULL_HELM_T, ItemId.STEEL_PLATEBODY_T, ItemId.STEEL_PLATELEGS_T,
            ItemId.STEEL_PLATESKIRT_T, ItemId.STEEL_KITESHIELD_T, ItemId.STEEL_FULL_HELM_G,
            ItemId.STEEL_PLATEBODY_G, ItemId.STEEL_PLATELEGS_G, ItemId.STEEL_PLATESKIRT_G,
            ItemId.STEEL_KITESHIELD_G, ItemId.BLACK_FULL_HELM_T, ItemId.BLACK_PLATEBODY_T,
            ItemId.BLACK_PLATELEGS_T, ItemId.BLACK_PLATESKIRT_T, ItemId.BLACK_KITESHIELD_T,
            ItemId.BLACK_FULL_HELM_G, ItemId.BLACK_PLATEBODY_G, ItemId.BLACK_PLATELEGS_G,
            ItemId.BLACK_PLATESKIRT_G, ItemId.BLACK_KITESHIELD_G, ItemId.BLACK_BERET,
            ItemId.BLUE_BERET, ItemId.WHITE_BERET, ItemId.RED_BERET, ItemId.HIGHWAYMAN_MASK,
            ItemId.BEANIE, ItemId.BLUE_WIZARD_HAT_T, ItemId.BLUE_WIZARD_ROBE_T, ItemId.BLUE_SKIRT_T,
            ItemId.BLUE_WIZARD_HAT_G, ItemId.BLUE_WIZARD_ROBE_G, ItemId.BLUE_SKIRT_G,
            ItemId.BLACK_WIZARD_HAT_T, ItemId.BLACK_WIZARD_ROBE_T, ItemId.BLACK_SKIRT_T,
            ItemId.BLACK_WIZARD_HAT_G, ItemId.BLACK_WIZARD_ROBE_G, ItemId.BLACK_SKIRT_G,
            ItemId.STUDDED_BODY_T, ItemId.STUDDED_CHAPS_T, ItemId.STUDDED_BODY_G,
            ItemId.STUDDED_CHAPS_G, ItemId.BLACK_HELM_H1, ItemId.BLACK_HELM_H2,
            ItemId.BLACK_HELM_H3, ItemId.BLACK_HELM_H4, ItemId.BLACK_HELM_H5,
            ItemId.BLACK_PLATEBODY_H1, ItemId.BLACK_PLATEBODY_H2, ItemId.BLACK_PLATEBODY_H3,
            ItemId.BLACK_PLATEBODY_H4, ItemId.BLACK_PLATEBODY_H5, ItemId.BLACK_SHIELD_H1,
            ItemId.BLACK_SHIELD_H2, ItemId.BLACK_SHIELD_H3, ItemId.BLACK_SHIELD_H4,
            ItemId.BLACK_SHIELD_H5, ItemId.BLUE_ELEGANT_SHIRT, ItemId.BLUE_ELEGANT_LEGS,
            ItemId.BLUE_ELEGANT_BLOUSE, ItemId.BLUE_ELEGANT_SKIRT, ItemId.GREEN_ELEGANT_SHIRT,
            ItemId.GREEN_ELEGANT_LEGS, ItemId.GREEN_ELEGANT_BLOUSE, ItemId.GREEN_ELEGANT_SKIRT,
            ItemId.RED_ELEGANT_SHIRT, ItemId.RED_ELEGANT_LEGS, ItemId.RED_ELEGANT_BLOUSE,
            ItemId.RED_ELEGANT_SKIRT, ItemId.BOBS_RED_SHIRT, ItemId.BOBS_BLUE_SHIRT,
            ItemId.BOBS_GREEN_SHIRT, ItemId.BOBS_BLACK_SHIRT, ItemId.BOBS_PURPLE_SHIRT,
            ItemId.STAFF_OF_BOB_THE_CAT, ItemId.A_POWDERED_WIG, ItemId.FLARED_TROUSERS,
            ItemId.PANTALOONS, ItemId.SLEEPING_CAP, ItemId.AMULET_OF_MAGIC_T,
            ItemId.AMULET_OF_POWER_T, ItemId.RAIN_BOW, ItemId.HAM_JOINT, ItemId.BLACK_CANE,
            ItemId.BLACK_PICKAXE, ItemId.GUTHIX_ROBE_TOP, ItemId.GUTHIX_ROBE_LEGS,
            ItemId.SARADOMIN_ROBE_TOP, ItemId.SARADOMIN_ROBE_LEGS, ItemId.ZAMORAK_ROBE_TOP,
            ItemId.ZAMORAK_ROBE_LEGS, ItemId.ANCIENT_ROBE_TOP, ItemId.BANDOS_ROBE_LEGS,
            ItemId.ARMADYL_ROBE_TOP, ItemId.ARMADYL_ROBE_LEGS, ItemId.IMP_MASK, ItemId.GOBLIN_MASK,
            ItemId.TEAM_CAPE_I, ItemId.TEAM_CAPE_X, ItemId.TEAM_CAPE_ZERO, ItemId.CAPE_OF_SKULLS,
            ItemId.WOODEN_SHIELD_G, ItemId.GOLDEN_CHEFS_HAT, ItemId.GOLDEN_APRON,
            ItemId.MONKS_ROBE_TOP_G, ItemId.MONKS_ROBE_G, ItemId.LARGE_SPADE, ItemId.LEATHER_BODY_G,
            ItemId.LEATHER_CHAPS_G};
        player.getInventory().deleteItem(itemId, 1, slot);
        int easyItemId = ttEasy[PRandom.randomE(ttEasy.length)];
        player.getInventory().addItem(easyItemId, 1, slot);
        if (PRandom.randomE(5) == 0) {
          int extraItemId = ttLoot[PRandom.randomE(ttLoot.length)];
          if (extraItemId == ItemId.PURPLE_SWEETS) {
            player.getInventory().addOrDropItem(extraItemId, 8 + PRandom.randomI(24));
          } else {
            player.getInventory().addOrDropItem(extraItemId, 1);
          }
        }
        player.getSkills().increaseClueScrollCount(Skills.CLUE_SCROLL_EASY);
        break;
      case ItemId.CLUE_SCROLL_MEDIUM:
      case ItemId.CLUE_BOTTLE_MEDIUM:
      case ItemId.CLUE_GEODE_MEDIUM:
      case ItemId.CLUE_NEST_MEDIUM:
        ttLoot = new int[] {ItemId.HOLY_BLESSING, ItemId.UNHOLY_BLESSING, ItemId.PEACEFUL_BLESSING,
            ItemId.HONOURABLE_BLESSING, ItemId.WAR_BLESSING, ItemId.ANCIENT_BLESSING,
            ItemId.WILLOW_COMP_BOW, ItemId.YEW_COMP_BOW, ItemId.MAGIC_COMP_BOW, ItemId.BEAR_FEET,
            ItemId.MOLE_SLIPPERS, ItemId.FROG_SLIPPERS, ItemId.DEMON_FEET, ItemId.SANDWICH_LADY_HAT,
            ItemId.SANDWICH_LADY_TOP, ItemId.SANDWICH_LADY_BOTTOM,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX, ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK, ItemId.MONKS_ROBE_TOP_T, ItemId.MONKS_ROBE_T,
            ItemId.AMULET_OF_DEFENCE_T, ItemId.JESTER_CAPE, ItemId.SHOULDER_PARROT,
            ItemId.PURPLE_SWEETS};
        int[] ttMedium = new int[] {ItemId.MITHRIL_FULL_HELM_T, ItemId.MITHRIL_PLATEBODY_T,
            ItemId.MITHRIL_PLATELEGS_T, ItemId.MITHRIL_PLATESKIRT_T, ItemId.MITHRIL_KITESHIELD_T,
            ItemId.MITHRIL_FULL_HELM_G, ItemId.MITHRIL_PLATEBODY_G, ItemId.MITHRIL_PLATELEGS_G,
            ItemId.MITHRIL_PLATESKIRT_G, ItemId.MITHRIL_KITESHIELD_G, ItemId.ADAMANT_FULL_HELM_T,
            ItemId.ADAMANT_PLATEBODY_T, ItemId.ADAMANT_PLATELEGS_T, ItemId.ADAMANT_PLATESKIRT_T,
            ItemId.ADAMANT_KITESHIELD_T, ItemId.ADAMANT_FULL_HELM_G, ItemId.ADAMANT_PLATEBODY_G,
            ItemId.ADAMANT_PLATELEGS_G, ItemId.ADAMANT_PLATESKIRT_G, ItemId.ADAMANT_KITESHIELD_G,
            ItemId.CLIMBING_BOOTS_G, ItemId.SPIKED_MANACLES, ItemId.RANGER_BOOTS,
            ItemId.HOLY_SANDALS, ItemId.WIZARD_BOOTS, ItemId.BLACK_HEADBAND, ItemId.RED_HEADBAND,
            ItemId.BROWN_HEADBAND, ItemId.PINK_HEADBAND, ItemId.GREEN_HEADBAND,
            ItemId.BLUE_HEADBAND, ItemId.WHITE_HEADBAND, ItemId.GOLD_HEADBAND, ItemId.RED_BOATER,
            ItemId.ORANGE_BOATER, ItemId.GREEN_BOATER, ItemId.BLUE_BOATER, ItemId.BLACK_BOATER,
            ItemId.PINK_BOATER, ItemId.PURPLE_BOATER, ItemId.WHITE_BOATER,
            ItemId.GREEN_DHIDE_BODY_T, ItemId.GREEN_DHIDE_CHAPS_T, ItemId.GREEN_DHIDE_BODY_G,
            ItemId.GREEN_DHIDE_CHAPS_G, ItemId.ADAMANT_HELM_H1, ItemId.ADAMANT_HELM_H2,
            ItemId.ADAMANT_HELM_H3, ItemId.ADAMANT_HELM_H4, ItemId.ADAMANT_HELM_H5,
            ItemId.ADAMANT_PLATEBODY_H1, ItemId.ADAMANT_PLATEBODY_H2, ItemId.ADAMANT_PLATEBODY_H3,
            ItemId.ADAMANT_PLATEBODY_H4, ItemId.ADAMANT_PLATEBODY_H5, ItemId.ADAMANT_SHIELD_H1,
            ItemId.ADAMANT_SHIELD_H2, ItemId.ADAMANT_SHIELD_H3, ItemId.ADAMANT_SHIELD_H4,
            ItemId.ADAMANT_SHIELD_H5, ItemId.BLACK_ELEGANT_SHIRT, ItemId.BLACK_ELEGANT_LEGS,
            ItemId.WHITE_ELEGANT_BLOUSE, ItemId.WHITE_ELEGANT_SKIRT, ItemId.PURPLE_ELEGANT_SHIRT,
            ItemId.PURPLE_ELEGANT_LEGS, ItemId.PURPLE_ELEGANT_BLOUSE, ItemId.PURPLE_ELEGANT_SKIRT,
            ItemId.PINK_ELEGANT_SHIRT, ItemId.PINK_ELEGANT_LEGS, ItemId.PINK_ELEGANT_BLOUSE,
            ItemId.PINK_ELEGANT_SKIRT, ItemId.GOLD_ELEGANT_SHIRT, ItemId.GOLD_ELEGANT_LEGS,
            ItemId.GOLD_ELEGANT_BLOUSE, ItemId.GOLD_ELEGANT_SKIRT, ItemId.WOLF_MASK,
            ItemId.WOLF_CLOAK, ItemId.STRENGTH_AMULET_T, ItemId.ADAMANT_CANE, ItemId.GUTHIX_MITRE,
            ItemId.SARADOMIN_MITRE, ItemId.ZAMORAK_MITRE, ItemId.ANCIENT_MITRE, ItemId.BANDOS_MITRE,
            ItemId.ARMADYL_MITRE, ItemId.GUTHIX_CLOAK, ItemId.SARADOMIN_CLOAK, ItemId.ZAMORAK_CLOAK,
            ItemId.ANCIENT_CLOAK, ItemId.BANDOS_CLOAK, ItemId.ARMADYL_CLOAK, ItemId.ANCIENT_STOLE,
            ItemId.ARMADYL_STOLE, ItemId.BANDOS_STOLE, ItemId.ANCIENT_CROZIER,
            ItemId.ARMADYL_CROZIER, ItemId.BANDOS_CROZIER, ItemId.CAT_MASK, ItemId.PENGUIN_MASK,
            ItemId.GNOMISH_FIRELIGHTER, ItemId.CRIER_HAT, ItemId.CRIER_BELL, ItemId.CRIER_COAT,
            ItemId.LEPRECHAUN_HAT, ItemId.BLACK_LEPRECHAUN_HAT, ItemId.BLACK_UNICORN_MASK,
            ItemId.WHITE_UNICORN_MASK, ItemId.ARCEUUS_BANNER, ItemId.HOSIDIUS_BANNER,
            ItemId.LOVAKENGJ_BANNER, ItemId.PISCARILIUS_BANNER, ItemId.SHAYZIEN_BANNER,
            ItemId.CABBAGE_ROUND_SHIELD, ItemId.CLUELESS_SCROLL};
        player.getInventory().deleteItem(itemId, 1, slot);
        int mediumClueSlot = PRandom.randomE(ttMedium.length);
        int mediumItemId = ttMedium[mediumClueSlot];
        player.getInventory().addItem(mediumItemId, 1, slot);
        if (PRandom.randomE(5) == 0) {
          int extraItemId = ttLoot[PRandom.randomE(ttLoot.length)];
          if (extraItemId == ItemId.PURPLE_SWEETS) {
            player.getInventory().addOrDropItem(extraItemId, 8 + PRandom.randomI(24));
          } else {
            player.getInventory().addOrDropItem(extraItemId, 1);
          }
        }
        player.getSkills().increaseClueScrollCount(Skills.CLUE_SCROLL_MEDIUM);
        break;
      case ItemId.CLUE_SCROLL_HARD:
      case ItemId.CLUE_BOTTLE_HARD:
      case ItemId.CLUE_GEODE_HARD:
      case ItemId.CLUE_NEST_HARD:
        ttLoot = new int[] {ItemId.HOLY_BLESSING, ItemId.UNHOLY_BLESSING, ItemId.PEACEFUL_BLESSING,
            ItemId.HONOURABLE_BLESSING, ItemId.WAR_BLESSING, ItemId.ANCIENT_BLESSING,
            ItemId.WILLOW_COMP_BOW, ItemId.YEW_COMP_BOW, ItemId.MAGIC_COMP_BOW, ItemId.BEAR_FEET,
            ItemId.MOLE_SLIPPERS, ItemId.FROG_SLIPPERS, ItemId.DEMON_FEET, ItemId.SANDWICH_LADY_HAT,
            ItemId.SANDWICH_LADY_TOP, ItemId.SANDWICH_LADY_BOTTOM,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX, ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK, ItemId.MONKS_ROBE_TOP_T, ItemId.MONKS_ROBE_T,
            ItemId.AMULET_OF_DEFENCE_T, ItemId.JESTER_CAPE, ItemId.SHOULDER_PARROT,
            ItemId.PURPLE_SWEETS};
        int[] ttHard = new int[] {ItemId.RUNE_FULL_HELM_T, ItemId.RUNE_PLATEBODY_T,
            ItemId.RUNE_PLATELEGS_T, ItemId.RUNE_PLATESKIRT_T, ItemId.RUNE_KITESHIELD_T,
            ItemId.RUNE_FULL_HELM_G, ItemId.RUNE_PLATEBODY_G, ItemId.RUNE_PLATELEGS_G,
            ItemId.RUNE_PLATESKIRT_G, ItemId.RUNE_KITESHIELD_G, ItemId.GUTHIX_FULL_HELM,
            ItemId.GUTHIX_PLATEBODY, ItemId.GUTHIX_PLATELEGS, ItemId.GUTHIX_PLATESKIRT,
            ItemId.GUTHIX_KITESHIELD, ItemId.SARADOMIN_FULL_HELM, ItemId.SARADOMIN_PLATEBODY,
            ItemId.SARADOMIN_PLATELEGS, ItemId.SARADOMIN_PLATESKIRT, ItemId.SARADOMIN_KITESHIELD,
            ItemId.ZAMORAK_FULL_HELM, ItemId.ZAMORAK_PLATEBODY, ItemId.ZAMORAK_PLATELEGS,
            ItemId.ZAMORAK_PLATESKIRT, ItemId.ZAMORAK_KITESHIELD, ItemId.ANCIENT_FULL_HELM,
            ItemId.ANCIENT_PLATEBODY, ItemId.ANCIENT_PLATELEGS, ItemId.ANCIENT_PLATESKIRT,
            ItemId.ANCIENT_KITESHIELD, ItemId.BANDOS_FULL_HELM, ItemId.BANDOS_PLATEBODY,
            ItemId.BANDOS_PLATELEGS, ItemId.BANDOS_PLATESKIRT, ItemId.BANDOS_KITESHIELD,
            ItemId.ARMADYL_FULL_HELM, ItemId.ARMADYL_PLATEBODY, ItemId.ARMADYL_PLATELEGS,
            ItemId.ARMADYL_PLATESKIRT, ItemId.ARMADYL_KITESHIELD, ItemId.RUNE_HELM_H1,
            ItemId.RUNE_HELM_H2, ItemId.RUNE_HELM_H3, ItemId.RUNE_HELM_H4, ItemId.RUNE_HELM_H5,
            ItemId.RUNE_PLATEBODY_H1, ItemId.RUNE_PLATEBODY_H2, ItemId.RUNE_PLATEBODY_H3,
            ItemId.RUNE_PLATEBODY_H4, ItemId.RUNE_PLATEBODY_H5, ItemId.BLUE_DHIDE_BODY_T,
            ItemId.BLUE_DHIDE_CHAPS_T, ItemId.BLUE_DHIDE_BODY_G, ItemId.BLUE_DHIDE_CHAPS_G,
            ItemId.RED_DHIDE_BODY_T, ItemId.RED_DHIDE_CHAPS_T, ItemId.RED_DHIDE_BODY_G,
            ItemId.RED_DHIDE_CHAPS_G, ItemId.ENCHANTED_HAT, ItemId.ENCHANTED_TOP,
            ItemId.ENCHANTED_ROBE, ItemId.ROBIN_HOOD_HAT, ItemId.TAN_CAVALIER, ItemId.DARK_CAVALIER,
            ItemId.BLACK_CAVALIER, ItemId.WHITE_CAVALIER, ItemId.RED_CAVALIER, ItemId.NAVY_CAVALIER,
            ItemId.PIRATES_HAT, ItemId.AMULET_OF_GLORY_T4, ItemId.GUTHIX_COIF, ItemId.GUTHIX_DHIDE,
            ItemId.GUTHIX_CHAPS, ItemId.GUTHIX_BRACERS, ItemId.GUTHIX_DHIDE_BOOTS,
            ItemId.GUTHIX_DHIDE_SHIELD, ItemId.SARADOMIN_COIF, ItemId.SARADOMIN_DHIDE,
            ItemId.SARADOMIN_CHAPS, ItemId.SARADOMIN_BRACERS, ItemId.SARADOMIN_DHIDE_BOOTS,
            ItemId.SARADOMIN_DHIDE_SHIELD, ItemId.ZAMORAK_COIF, ItemId.ZAMORAK_DHIDE,
            ItemId.ZAMORAK_CHAPS, ItemId.ZAMORAK_BRACERS, ItemId.ZAMORAK_DHIDE_BOOTS,
            ItemId.ZAMORAK_DHIDE_SHIELD, ItemId.ARMADYL_COIF, ItemId.ARMADYL_DHIDE,
            ItemId.ARMADYL_CHAPS, ItemId.ARMADYL_BRACERS, ItemId.ARMADYL_DHIDE_BOOTS,
            ItemId.ARMADYL_DHIDE_SHIELD, ItemId.ANCIENT_COIF, ItemId.ANCIENT_DHIDE,
            ItemId.ANCIENT_CHAPS, ItemId.ANCIENT_BRACERS, ItemId.ANCIENT_DHIDE_BOOTS,
            ItemId.ANCIENT_DHIDE_SHIELD, ItemId.BANDOS_COIF, ItemId.BANDOS_DHIDE,
            ItemId.BANDOS_CHAPS, ItemId.BANDOS_BRACERS, ItemId.BANDOS_DHIDE_BOOTS,
            ItemId.BANDOS_DHIDE_SHIELD, ItemId.GUTHIX_STOLE, ItemId.SARADOMIN_STOLE,
            ItemId.ZAMORAK_STOLE, ItemId.GUTHIX_CROZIER, ItemId.SARADOMIN_CROZIER,
            ItemId.ZAMORAK_CROZIER, ItemId.GREEN_DRAGON_MASK, ItemId.BLUE_DRAGON_MASK,
            ItemId.RED_DRAGON_MASK, ItemId.BLACK_DRAGON_MASK, ItemId.PITH_HELMET,
            ItemId.EXPLORER_BACKPACK, ItemId.RUNE_CANE, ItemId.ZOMBIE_HEAD_19912,
            ItemId.CYCLOPS_HEAD, ItemId.NUNCHAKU, ItemId.DUAL_SAI, ItemId.THIEVING_BAG,
            ItemId.DRAGON_BOOTS_ORNAMENT_KIT, ItemId.RUNE_DEFENDER_ORNAMENT_KIT,
            ItemId.TZHAAR_KET_OM_ORNAMENT_KIT, ItemId.BERSERKER_NECKLACE_ORNAMENT_KIT};
        int[] gildedHard = new int[] {ItemId.GILDED_FULL_HELM, ItemId.GILDED_PLATEBODY,
            ItemId.GILDED_PLATELEGS, ItemId.GILDED_PLATESKIRT, ItemId.GILDED_KITESHIELD,
            ItemId.GILDED_MED_HELM, ItemId.GILDED_CHAINBODY, ItemId.GILDED_SQ_SHIELD,
            ItemId.GILDED_2H_SWORD, ItemId.GILDED_SPEAR, ItemId.GILDED_HASTA};
        int[] thirdageHard = new int[] {ItemId._3RD_AGE_FULL_HELMET, ItemId._3RD_AGE_PLATEBODY,
            ItemId._3RD_AGE_PLATELEGS, ItemId._3RD_AGE_KITESHIELD, ItemId._3RD_AGE_RANGE_COIF,
            ItemId._3RD_AGE_RANGE_TOP, ItemId._3RD_AGE_RANGE_LEGS, ItemId._3RD_AGE_VAMBRACES,
            ItemId._3RD_AGE_MAGE_HAT, ItemId._3RD_AGE_ROBE_TOP, ItemId._3RD_AGE_ROBE,
            ItemId._3RD_AGE_AMULET, ItemId._3RD_AGE_PLATESKIRT};
        player.getInventory().deleteItem(itemId, 1, slot);
        if (PRandom.inRange(0.1)) {
          int thirdAgeId = thirdageHard[PRandom.randomE(thirdageHard.length)];
          player.getInventory().addItem(thirdAgeId, 1, slot);
          player.getWorld().sendItemDropNews(player, thirdAgeId, "a hard clue scroll");
        } else if (PRandom.inRange(0.5)) {
          int gildedId = gildedHard[PRandom.randomE(gildedHard.length)];
          player.getInventory().addItem(gildedId, 1, slot);
          player.getWorld().sendItemDropNews(player, gildedId, "a hard clue scroll");
        } else {
          int hardItemId = ttHard[PRandom.randomE(ttHard.length)];
          player.getInventory().addItem(hardItemId, 1, slot);
        }
        if (PRandom.randomE(5) == 0) {
          int extraItemId = ttLoot[PRandom.randomE(ttLoot.length)];
          if (extraItemId == ItemId.PURPLE_SWEETS) {
            player.getInventory().addOrDropItem(extraItemId, 8 + PRandom.randomI(24));
          } else {
            player.getInventory().addOrDropItem(extraItemId, 1);
          }
        }
        player.getSkills().increaseClueScrollCount(Skills.CLUE_SCROLL_HARD);
        break;
      case ItemId.CLUE_SCROLL_ELITE:
      case ItemId.CLUE_BOTTLE_ELITE:
      case ItemId.CLUE_GEODE_ELITE:
      case ItemId.CLUE_NEST_ELITE:
        ttLoot = new int[] {ItemId.HOLY_BLESSING, ItemId.UNHOLY_BLESSING, ItemId.PEACEFUL_BLESSING,
            ItemId.HONOURABLE_BLESSING, ItemId.WAR_BLESSING, ItemId.ANCIENT_BLESSING,
            ItemId.WILLOW_COMP_BOW, ItemId.YEW_COMP_BOW, ItemId.MAGIC_COMP_BOW, ItemId.BEAR_FEET,
            ItemId.MOLE_SLIPPERS, ItemId.FROG_SLIPPERS, ItemId.DEMON_FEET, ItemId.SANDWICH_LADY_HAT,
            ItemId.SANDWICH_LADY_TOP, ItemId.SANDWICH_LADY_BOTTOM,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX, ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK, ItemId.MONKS_ROBE_TOP_T, ItemId.MONKS_ROBE_T,
            ItemId.AMULET_OF_DEFENCE_T, ItemId.JESTER_CAPE, ItemId.SHOULDER_PARROT,
            ItemId.PURPLE_SWEETS};
        int[] ttElite = new int[] {ItemId.DRAGON_FULL_HELM_ORNAMENT_KIT,
            ItemId.DRAGON_CHAINBODY_ORNAMENT_KIT, ItemId.DRAGON_LEGS_SKIRT_ORNAMENT_KIT,
            ItemId.DRAGON_SQ_SHIELD_ORNAMENT_KIT, ItemId.DRAGON_SCIMITAR_ORNAMENT_KIT,
            ItemId.LIGHT_INFINITY_COLOUR_KIT, ItemId.DARK_INFINITY_COLOUR_KIT,
            ItemId.FURY_ORNAMENT_KIT, ItemId.MUSKETEER_HAT, ItemId.MUSKETEER_TABARD,
            ItemId.MUSKETEER_PANTS, ItemId.DRAGON_CANE, ItemId.BRIEFCASE,
            ItemId.SAGACIOUS_SPECTACLES, ItemId.TOP_HAT, ItemId.MONOCLE, ItemId.BIG_PIRATE_HAT,
            ItemId.DEERSTALKER, ItemId.BRONZE_DRAGON_MASK, ItemId.IRON_DRAGON_MASK,
            ItemId.STEEL_DRAGON_MASK, ItemId.MITHRIL_DRAGON_MASK, ItemId.ADAMANT_DRAGON_MASK,
            ItemId.RUNE_DRAGON_MASK, ItemId.LAVA_DRAGON_MASK, ItemId.BLACK_DHIDE_BODY_T,
            ItemId.BLACK_DHIDE_CHAPS_T, ItemId.BLACK_DHIDE_BODY_G, ItemId.BLACK_DHIDE_CHAPS_G,
            ItemId.RANGERS_TUNIC, ItemId.AFRO, ItemId.KATANA, ItemId.ROYAL_CROWN,
            ItemId.ROYAL_GOWN_TOP, ItemId.ROYAL_GOWN_BOTTOM, ItemId.ROYAL_SCEPTRE,
            ItemId.ARCEUUS_SCARF, ItemId.HOSIDIUS_SCARF, ItemId.LOVAKENGJ_SCARF,
            ItemId.PISCARILIUS_SCARF, ItemId.SHAYZIEN_SCARF, ItemId.BLACKSMITHS_HELM,
            ItemId.BUCKET_HELM, ItemId.RANGER_GLOVES, ItemId.HOLY_WRAPS, ItemId.RING_OF_NATURE,
            ItemId.DARK_BOW_TIE, ItemId.DARK_TUXEDO_JACKET, ItemId.DARK_TROUSERS,
            ItemId.DARK_TUXEDO_CUFFS, ItemId.DARK_TUXEDO_SHOES, ItemId.LIGHT_BOW_TIE,
            ItemId.LIGHT_TUXEDO_JACKET, ItemId.LIGHT_TROUSERS, ItemId.LIGHT_TUXEDO_CUFFS,
            ItemId.LIGHT_TUXEDO_SHOES, ItemId.RANGERS_TIGHTS, ItemId.URIS_HAT,
            ItemId.FREMENNIK_KILT, ItemId.HEAVY_CASKET, ItemId.GIANT_BOOT};
        int[] gildedElite = new int[] {ItemId.GILDED_BOOTS, ItemId.GILDED_SCIMITAR,
            ItemId.GILDED_DHIDE_VAMBS, ItemId.GILDED_DHIDE_BODY, ItemId.GILDED_DHIDE_CHAPS,
            ItemId.GILDED_COIF, ItemId.GILDED_AXE, ItemId.GILDED_PICKAXE, ItemId.GILDED_SPADE};
        int[] thirdageElite = new int[] {ItemId._3RD_AGE_FULL_HELMET, ItemId._3RD_AGE_PLATEBODY,
            ItemId._3RD_AGE_PLATELEGS, ItemId._3RD_AGE_KITESHIELD, ItemId._3RD_AGE_RANGE_COIF,
            ItemId._3RD_AGE_RANGE_TOP, ItemId._3RD_AGE_RANGE_LEGS, ItemId._3RD_AGE_VAMBRACES,
            ItemId._3RD_AGE_MAGE_HAT, ItemId._3RD_AGE_ROBE_TOP, ItemId._3RD_AGE_ROBE,
            ItemId._3RD_AGE_AMULET, ItemId._3RD_AGE_CLOAK, ItemId._3RD_AGE_WAND,
            ItemId._3RD_AGE_BOW, ItemId._3RD_AGE_LONGSWORD, ItemId.RING_OF_3RD_AGE,
            ItemId._3RD_AGE_PLATESKIRT};
        player.getInventory().deleteItem(itemId, 1, slot);
        if (PRandom.inRange(0.1)) {
          int thirdAgeId = thirdageElite[PRandom.randomE(thirdageElite.length)];
          player.getInventory().addItem(thirdAgeId, 1, slot);
          player.getWorld().sendItemDropNews(player, thirdAgeId, "an elite clue scroll");
        } else if (PRandom.inRange(0.5)) {
          int gildedId = gildedElite[PRandom.randomE(gildedElite.length)];
          player.getInventory().addItem(gildedId, 1, slot);
          player.getWorld().sendItemDropNews(player, gildedId, "an elite clue scroll");
        } else {
          int eliteItemId = ttElite[PRandom.randomE(ttElite.length)];
          player.getInventory().addItem(eliteItemId, 1, slot);
        }
        if (PRandom.randomE(5) == 0) {
          int extraItemId = ttLoot[PRandom.randomE(ttLoot.length)];
          if (extraItemId == ItemId.PURPLE_SWEETS) {
            player.getInventory().addOrDropItem(extraItemId, 8 + PRandom.randomI(24));
          } else {
            player.getInventory().addOrDropItem(extraItemId, 1);
          }
        }
        player.getSkills().increaseClueScrollCount(Skills.CLUE_SCROLL_ELITE);
        break;
      case ItemId.CLUE_SCROLL_MASTER:
        ttLoot = new int[] {ItemId.HOLY_BLESSING, ItemId.UNHOLY_BLESSING, ItemId.PEACEFUL_BLESSING,
            ItemId.HONOURABLE_BLESSING, ItemId.WAR_BLESSING, ItemId.ANCIENT_BLESSING,
            ItemId.WILLOW_COMP_BOW, ItemId.YEW_COMP_BOW, ItemId.MAGIC_COMP_BOW, ItemId.BEAR_FEET,
            ItemId.MOLE_SLIPPERS, ItemId.FROG_SLIPPERS, ItemId.DEMON_FEET, ItemId.SANDWICH_LADY_HAT,
            ItemId.SANDWICH_LADY_TOP, ItemId.SANDWICH_LADY_BOTTOM,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX, ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN,
            ItemId.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK, ItemId.MONKS_ROBE_TOP_T, ItemId.MONKS_ROBE_T,
            ItemId.AMULET_OF_DEFENCE_T, ItemId.JESTER_CAPE, ItemId.SHOULDER_PARROT,
            ItemId.PURPLE_SWEETS};
        int[] ttMaster = new int[] {ItemId.DRAGON_PLATEBODY_ORNAMENT_KIT,
            ItemId.DRAGON_KITESHIELD_ORNAMENT_KIT, ItemId.DRAGON_DEFENDER_ORNAMENT_KIT,
            ItemId.ANGUISH_ORNAMENT_KIT, ItemId.TORTURE_ORNAMENT_KIT, ItemId.OCCULT_ORNAMENT_KIT,
            ItemId.ARMADYL_GODSWORD_ORNAMENT_KIT, ItemId.BANDOS_GODSWORD_ORNAMENT_KIT,
            ItemId.SARADOMIN_GODSWORD_ORNAMENT_KIT, ItemId.ZAMORAK_GODSWORD_ORNAMENT_KIT,
            ItemId.TORMENTED_ORNAMENT_KIT, ItemId.LESSER_DEMON_MASK, ItemId.GREATER_DEMON_MASK,
            ItemId.BLACK_DEMON_MASK, ItemId.JUNGLE_DEMON_MASK, ItemId.OLD_DEMON_MASK,
            ItemId.ARCEUUS_HOOD, ItemId.HOSIDIUS_HOOD, ItemId.LOVAKENGJ_HOOD,
            ItemId.PISCARILIUS_HOOD, ItemId.SHAYZIEN_HOOD, ItemId.SAMURAI_KASA,
            ItemId.SAMURAI_SHIRT, ItemId.SAMURAI_GLOVES, ItemId.SAMURAI_GREAVES,
            ItemId.SAMURAI_BOOTS, ItemId.MUMMYS_HEAD, ItemId.MUMMYS_BODY, ItemId.MUMMYS_HANDS,
            ItemId.MUMMYS_LEGS, ItemId.MUMMYS_FEET, ItemId.ANKOU_MASK, ItemId.ANKOU_TOP,
            ItemId.ANKOU_GLOVES, ItemId.ANKOUS_LEGGINGS, ItemId.ANKOU_SOCKS,
            ItemId.HOOD_OF_DARKNESS, ItemId.ROBE_TOP_OF_DARKNESS, ItemId.GLOVES_OF_DARKNESS,
            ItemId.ROBE_BOTTOM_OF_DARKNESS, ItemId.BOOTS_OF_DARKNESS, ItemId.RING_OF_COINS,
            ItemId.LEFT_EYE_PATCH, ItemId.OBSIDIAN_CAPE_R, ItemId.FANCY_TIARA,
            ItemId.HALF_MOON_SPECTACLES, ItemId.ALE_OF_THE_GODS, ItemId.BUCKET_HELM_G,
            ItemId.BOWL_WIG, ItemId.SCROLL_SACK};
        int[] gildedMaster = new int[] {ItemId.GILDED_FULL_HELM, ItemId.GILDED_PLATEBODY,
            ItemId.GILDED_PLATELEGS, ItemId.GILDED_PLATESKIRT, ItemId.GILDED_KITESHIELD,
            ItemId.GILDED_MED_HELM, ItemId.GILDED_CHAINBODY, ItemId.GILDED_SQ_SHIELD,
            ItemId.GILDED_2H_SWORD, ItemId.GILDED_SPEAR, ItemId.GILDED_HASTA, ItemId.GILDED_BOOTS,
            ItemId.GILDED_SCIMITAR, ItemId.GILDED_DHIDE_VAMBS, ItemId.GILDED_DHIDE_BODY,
            ItemId.GILDED_DHIDE_CHAPS, ItemId.GILDED_COIF, ItemId.GILDED_AXE, ItemId.GILDED_PICKAXE,
            ItemId.GILDED_SPADE};
        int[] thirdageMaster = new int[] {ItemId._3RD_AGE_FULL_HELMET, ItemId._3RD_AGE_PLATEBODY,
            ItemId._3RD_AGE_PLATELEGS, ItemId._3RD_AGE_KITESHIELD, ItemId._3RD_AGE_RANGE_COIF,
            ItemId._3RD_AGE_RANGE_TOP, ItemId._3RD_AGE_RANGE_LEGS, ItemId._3RD_AGE_VAMBRACES,
            ItemId._3RD_AGE_MAGE_HAT, ItemId._3RD_AGE_ROBE_TOP, ItemId._3RD_AGE_ROBE,
            ItemId._3RD_AGE_AMULET, ItemId._3RD_AGE_CLOAK, ItemId._3RD_AGE_WAND,
            ItemId._3RD_AGE_BOW, ItemId._3RD_AGE_LONGSWORD, ItemId._3RD_AGE_AXE,
            ItemId._3RD_AGE_PICKAXE, ItemId._3RD_AGE_DRUIDIC_ROBE_TOP,
            ItemId._3RD_AGE_DRUIDIC_ROBE_BOTTOMS, ItemId._3RD_AGE_DRUIDIC_CLOAK,
            ItemId._3RD_AGE_DRUIDIC_STAFF, ItemId.RING_OF_3RD_AGE, ItemId._3RD_AGE_PLATESKIRT};
        player.getInventory().deleteItem(itemId, 1, slot);
        if (PRandom.inRange(0.1)) {
          int thirdAgeId = thirdageMaster[PRandom.randomE(thirdageMaster.length)];
          player.getInventory().addItem(thirdAgeId, 1, slot);
          player.getWorld().sendItemDropNews(player, thirdAgeId, "a master clue scroll");
        } else if (PRandom.inRange(0.5)) {
          int gildedId = gildedMaster[PRandom.randomE(gildedMaster.length)];
          player.getInventory().addItem(gildedId, 1, slot);
          player.getWorld().sendItemDropNews(player, gildedId, "a master clue scroll");
        } else {
          int masterItemId = ttMaster[PRandom.randomE(ttMaster.length)];
          player.getInventory().addItem(masterItemId, 1, slot);
        }
        if (PRandom.randomE(5) == 0) {
          int extraItemId = ttLoot[PRandom.randomE(ttLoot.length)];
          if (extraItemId == ItemId.PURPLE_SWEETS) {
            player.getInventory().addOrDropItem(extraItemId, 8 + PRandom.randomI(24));
          } else {
            player.getInventory().addOrDropItem(extraItemId, 1);
          }
        }
        player.getFamiliar().rollPet(ItemId.BLOODHOUND, 0.1);
        player.getSkills().increaseClueScrollCount(Skills.CLUE_SCROLL_MASTER);
        break;
    }
  }

  public static class MaxCapeDialogue extends OptionsDialogue {
    public MaxCapeDialogue(Player player) {
      DialogueAction action = (childId, slot) -> {
        Tile maxCapeTele = null;
        if (slot == 0) {
          maxCapeTele = new Tile(3093, 3495);
        } else if (slot == 1) {
          maxCapeTele = new Tile(1233, 3565);
        } else if (slot == 2) {
          maxCapeTele = new Tile(1666, 10050);
        }
        if (!player.getController().canTeleport(true)) {
          return;
        }
        if (maxCapeTele == null) {
          return;
        }
        player.getMovement().animatedTeleport(maxCapeTele, Magic.NORMAL_MAGIC_ANIMATION_START,
            Magic.NORMAL_MAGIC_ANIMATION_END, Magic.NORMAL_MAGIC_GRAPHIC, null, 2);
        player.getController().stopWithTeleport();
        player.clearHits();
      };
      addOption("Edgeville", action);
      addOption("Chambers of Xeric", action);
      addOption("Catacombs of Kourend", action);
    }
  }
}
