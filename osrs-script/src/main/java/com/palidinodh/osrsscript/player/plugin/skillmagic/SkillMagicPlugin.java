package com.palidinodh.osrsscript.player.plugin.skillmagic;

import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.WidgetChild;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.Smithing;

public class SkillMagicPlugin extends PlayerPlugin {
  private transient Player player;

  @Override
  public void login() {
    player = getPlayer();
  }

  @Override
  public boolean widgetOnWidgetHook(int useWidgetId, int useChildId, int onWidgetId, int onChildId,
      int useSlot, int useItemId, int onSlot, int onItemId) {
    if (useWidgetId == WidgetId.SPELLBOOK && onWidgetId == WidgetId.INVENTORY) {
      if (onItemId != player.getInventory().getId(onSlot)) {
        return true;
      }
      if (player.getHeight() != player.getClientHeight()) {
        player.getGameEncoder().sendMessage("You can't do this here.");
        return true;
      }
      switch (WidgetChild.Spellbook.get(useChildId)) {
        case LVL_1_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 7) {
            player.getGameEncoder().sendMessage("You need a Magic level of 7 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.WATER_RUNE, 1)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.SAPPHIRE_RING && onItemId != ItemId.SAPPHIRE_NECKLACE
              && onItemId != ItemId.SAPPHIRE_AMULET && onItemId != ItemId.SAPPHIRE_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(719);
          player.setGraphic(114, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.SAPPHIRE_RING) {
            player.getInventory().addItem(ItemId.RING_OF_RECOIL, 1, onSlot);
          } else if (onItemId == ItemId.SAPPHIRE_NECKLACE) {
            player.getInventory().addItem(ItemId.GAMES_NECKLACE_8, 1, onSlot);
          } else if (onItemId == ItemId.SAPPHIRE_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_MAGIC, 1, onSlot);
          } else if (onItemId == ItemId.SAPPHIRE_BRACELET) {
            player.getInventory().addItem(ItemId.BRACELET_OF_CLAY, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.WATER_RUNE, 1);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 18);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case LVL_2_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 27) {
            player.getGameEncoder().sendMessage("You need a Magic level of 27 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.AIR_RUNE, 3)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.EMERALD_RING && onItemId != ItemId.EMERALD_NECKLACE
              && onItemId != ItemId.EMERALD_AMULET && onItemId != ItemId.EMERALD_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(719);
          player.setGraphic(114, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.EMERALD_RING) {
            player.getInventory().addItem(ItemId.RING_OF_DUELING_8, 1, onSlot);
          } else if (onItemId == ItemId.EMERALD_NECKLACE) {
            player.getInventory().addItem(ItemId.BINDING_NECKLACE, 1, onSlot);
          } else if (onItemId == ItemId.EMERALD_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_DEFENCE, 1, onSlot);
          } else if (onItemId == ItemId.EMERALD_BRACELET) {
            player.getInventory().addItem(ItemId.CASTLE_WARS_BRACELET_3, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.AIR_RUNE, 3);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 37);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case SUPERHEAT_ITEM:
          if (player.getSkills().getLevel(Skills.MAGIC) < 43) {
            player.getGameEncoder().sendMessage("You need a Magic level of 43 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.FIRE_RUNE, 4)
              || !player.getMagic().hasRunes(ItemId.NATURE_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          int makeId = -1;
          if (onItemId == ItemId.COPPER_ORE || onItemId == ItemId.TIN_ORE) {
            makeId = ItemId.BRONZE_BAR;
          } else if (onItemId == ItemId.BLURITE_ORE) {
            makeId = ItemId.BLURITE_BAR;
          } else if (onItemId == ItemId.IRON_ORE
              && player.getInventory().getCount(ItemId.COAL) >= 2) {
            makeId = ItemId.STEEL_BAR;
          } else if (onItemId == ItemId.IRON_ORE) {
            makeId = ItemId.IRON_BAR;
          } else if (onItemId == ItemId.SILVER_ORE) {
            makeId = ItemId.SILVER_BAR;
          } else if (onItemId == ItemId.GOLD_ORE) {
            makeId = ItemId.GOLD_BAR;
          } else if (onItemId == ItemId.MITHRIL_ORE) {
            makeId = ItemId.MITHRIL_BAR;
          } else if (onItemId == ItemId.ADAMANTITE_ORE) {
            makeId = ItemId.ADAMANTITE_BAR;
          } else if (onItemId == ItemId.RUNITE_ORE) {
            makeId = ItemId.RUNITE_BAR;
          } else {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          if (!Smithing.make1(player, makeId)) {
            return true;
          }
          player.setAnimation(725);
          player.setGraphic(148, 92);
          player.getMagic().deleteRunes(ItemId.FIRE_RUNE, 5);
          player.getMagic().deleteRunes(ItemId.NATURE_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 53);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case LVL_3_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 49) {
            player.getGameEncoder().sendMessage("You need a Magic level of 49 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.FIRE_RUNE, 5)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.RUBY_RING && onItemId != ItemId.RUBY_NECKLACE
              && onItemId != ItemId.RUBY_AMULET && onItemId != ItemId.RUBY_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(720);
          player.setGraphic(115, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.RUBY_RING) {
            player.getInventory().addItem(ItemId.RING_OF_FORGING, 1, onSlot);
          } else if (onItemId == ItemId.RUBY_NECKLACE) {
            player.getInventory().addItem(ItemId.DIGSITE_PENDANT_5, 1, onSlot);
          } else if (onItemId == ItemId.RUBY_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_STRENGTH, 1, onSlot);
          } else if (onItemId == ItemId.RUBY_BRACELET) {
            player.getInventory().addItem(ItemId.INOCULATION_BRACELET, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.FIRE_RUNE, 5);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 59);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case HIGH_LEVEL_ALCHEMY:
          if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
            player.getGameEncoder().sendMessage("You need a Magic level of 55 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.NATURE_RUNE, 1)
              || !player.getMagic().hasRunes(ItemId.FIRE_RUNE, 5)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId == ItemId.COINS) {
            player.getGameEncoder().sendMessage("You can't alch coins.");
            return true;
          }
          if (onItemId == ItemId.BLOOD_MONEY) {
            player.getGameEncoder().sendMessage("You can't alch blood money.");
            return true;
          }
          if (onItemId == ItemId.OLD_SCHOOL_BOND || onItemId == ItemId.OLD_SCHOOL_BOND_UNTRADEABLE
              || onItemId == ItemId._14_DAYS_PREMIUM_MEMBERSHIP_32303
              || onItemId == ItemId.BOND_32318) {
            player.getGameEncoder().sendMessage("You can't alch bonds.");
            return true;
          }
          if (ItemDef.getUntradable(onItemId)) {
            player.getGameEncoder().sendMessage("You can't alch this item.");
            return true;
          }
          if (ItemDef.isFree(onItemId)) {
            player.getGameEncoder().sendMessage("You can't alch this item.");
            return true;
          }
          int value = ItemDef.getHighAlch(onItemId);
          if (player.getInventory().getEmptySlot() == -1 && ItemDef.getStackOrNote(onItemId)
              && !player.getInventory().canAddItem(ItemId.COINS, value)) {
            player.getInventory().notEnoughSpace();
            return true;
          }
          player.setAnimation(713);
          player.setGraphic(113, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          player.getInventory().addItem(ItemId.COINS, value, onSlot);
          player.getMagic().deleteRunes(ItemId.NATURE_RUNE, 1);
          player.getMagic().deleteRunes(ItemId.FIRE_RUNE, 5);
          player.getSkills().addXp(Skills.MAGIC, 65);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case LVL_4_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 57) {
            player.getGameEncoder().sendMessage("You need a Magic level of 57 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.EARTH_RUNE, 10)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.DIAMOND_RING && onItemId != ItemId.DIAMOND_NECKLACE
              && onItemId != ItemId.DIAMOND_AMULET && onItemId != ItemId.DIAMOND_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(720);
          player.setGraphic(115, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.DIAMOND_RING) {
            player.getInventory().addItem(ItemId.RING_OF_LIFE, 1, onSlot);
          } else if (onItemId == ItemId.DIAMOND_NECKLACE) {
            player.getInventory().addItem(ItemId.PHOENIX_NECKLACE, 1, onSlot);
          } else if (onItemId == ItemId.DIAMOND_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_POWER, 1, onSlot);
          } else if (onItemId == ItemId.DIAMOND_BRACELET) {
            player.getInventory().addItem(ItemId.ABYSSAL_BRACELET_5, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.EARTH_RUNE, 10);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 67);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case LVL_5_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 68) {
            player.getGameEncoder().sendMessage("You need a Magic level of 68 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.WATER_RUNE, 15)
              || !player.getMagic().hasRunes(ItemId.EARTH_RUNE, 15)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.DRAGON_NECKLACE && onItemId != ItemId.DRAGONSTONE_AMULET
              && onItemId != ItemId.DRAGONSTONE_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(721);
          player.setGraphic(116, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.DRAGON_NECKLACE) {
            player.getInventory().addItem(ItemId.SKILLS_NECKLACE_4, 1, onSlot);
          } else if (onItemId == ItemId.DRAGONSTONE_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_GLORY_4, 1, onSlot);
          } else if (onItemId == ItemId.DRAGONSTONE_BRACELET) {
            player.getInventory().addItem(ItemId.COMBAT_BRACELET_4, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.WATER_RUNE, 15);
          player.getMagic().deleteRunes(ItemId.EARTH_RUNE, 15);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 78);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case LVL_6_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 87) {
            player.getGameEncoder().sendMessage("You need a Magic level of 87 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.EARTH_RUNE, 20)
              || !player.getMagic().hasRunes(ItemId.FIRE_RUNE, 20)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.ONYX_RING && onItemId != ItemId.ONYX_NECKLACE
              && onItemId != ItemId.ONYX_AMULET && onItemId != ItemId.ONYX_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(721);
          player.setGraphic(452, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.ONYX_RING) {
            player.getInventory().addItem(ItemId.RING_OF_STONE, 1, onSlot);
          } else if (onItemId == ItemId.ONYX_NECKLACE) {
            player.getInventory().addItem(ItemId.BERSERKER_NECKLACE, 1, onSlot);
          } else if (onItemId == ItemId.ONYX_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_FURY, 1, onSlot);
          } else if (onItemId == ItemId.ONYX_BRACELET) {
            player.getInventory().addItem(ItemId.REGEN_BRACELET, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.EARTH_RUNE, 20);
          player.getMagic().deleteRunes(ItemId.FIRE_RUNE, 20);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 97);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        case LVL_7_ENCHANT:
          if (player.getSkills().getLevel(Skills.MAGIC) < 93) {
            player.getGameEncoder().sendMessage("You need a Magic level of 93 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.BLOOD_RUNE, 20)
              || !player.getMagic().hasRunes(566, 20)
              || !player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 1)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          if (onItemId != ItemId.ZENYTE_RING && onItemId != ItemId.ZENYTE_NECKLACE
              && onItemId != ItemId.ZENYTE_AMULET && onItemId != ItemId.ZENYTE_BRACELET) {
            player.getGameEncoder().sendMessage("You can't use this spell on this item.");
            return true;
          }
          player.setAnimation(721);
          player.setGraphic(452, 92);
          player.getInventory().deleteItem(onItemId, 1, onSlot);
          if (onItemId == ItemId.ZENYTE_RING) {
            player.getInventory().addItem(ItemId.RING_OF_SUFFERING, 1, onSlot);
          } else if (onItemId == ItemId.ZENYTE_NECKLACE) {
            player.getInventory().addItem(ItemId.NECKLACE_OF_ANGUISH, 1, onSlot);
          } else if (onItemId == ItemId.ZENYTE_AMULET) {
            player.getInventory().addItem(ItemId.AMULET_OF_TORTURE, 1, onSlot);
          } else if (onItemId == ItemId.ZENYTE_BRACELET) {
            player.getInventory().addItem(ItemId.TORMENTED_BRACELET, 1, onSlot);
          }
          player.getMagic().deleteRunes(ItemId.BLOOD_RUNE, 20);
          player.getMagic().deleteRunes(566, 20);
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 1);
          player.getSkills().addXp(Skills.MAGIC, 110);
          player.getGameEncoder().sendViewingIcon(WidgetChild.ViewportIcon.MAGIC);
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  @Override
  public boolean widgetOnMapObjectHook(int widgetId, int childId, int slot, int itemId,
      MapObject mapObject) {
    if (widgetId == WidgetId.SPELLBOOK) {
      switch (WidgetChild.Spellbook.get(childId)) {
        case CHARGE_WATER_ORB:
          if (mapObject.getId() != 2151) {
            player.getGameEncoder().sendMessage("This spell needs to be cast on a water obelisk.");
            return true;
          }
          if (!player.getInventory().hasItem(ItemId.UNPOWERED_ORB)) {
            player.getGameEncoder().sendMessage("You need an unpowered orb to charge.");
            return true;
          }
          if (player.getSkills().getLevel(Skills.MAGIC) < 56) {
            player.getGameEncoder().sendMessage("You need a Magic level of 56 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 3)
              || !player.getMagic().hasRunes(ItemId.WATER_RUNE, 30)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 3);
          player.getMagic().deleteRunes(ItemId.WATER_RUNE, 30);
          player.setAnimation(726);
          player.setGraphic(149, 92);
          player.getSkills().addXp(Skills.MAGIC, 66);
          player.getInventory().deleteItem(ItemId.UNPOWERED_ORB);
          player.getInventory().addItem(ItemId.WATER_ORB);
          return true;
        case CHARGE_EARTH_ORB:
          if (mapObject.getId() != 2150) {
            player.getGameEncoder().sendMessage("This spell needs to be cast on an earth obelisk.");
            return true;
          }
          if (!player.getInventory().hasItem(ItemId.UNPOWERED_ORB)) {
            player.getGameEncoder().sendMessage("You need an unpowered orb to charge.");
            return true;
          }
          if (player.getSkills().getLevel(Skills.MAGIC) < 60) {
            player.getGameEncoder().sendMessage("You need a Magic level of 60 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 3)
              || !player.getMagic().hasRunes(ItemId.EARTH_RUNE, 30)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 3);
          player.getMagic().deleteRunes(ItemId.EARTH_RUNE, 30);
          player.setAnimation(726);
          player.setGraphic(149, 92);
          player.getSkills().addXp(Skills.MAGIC, 70);
          player.getInventory().deleteItem(ItemId.UNPOWERED_ORB);
          player.getInventory().addItem(ItemId.EARTH_ORB);
          return true;
        case CHARGE_FIRE_ORB:
          if (mapObject.getId() != 2153) {
            player.getGameEncoder().sendMessage("This spell needs to be cast on a fire obelisk.");
            return true;
          }
          if (!player.getInventory().hasItem(ItemId.UNPOWERED_ORB)) {
            player.getGameEncoder().sendMessage("You need an unpowered orb to charge.");
            return true;
          }
          if (player.getSkills().getLevel(Skills.MAGIC) < 63) {
            player.getGameEncoder().sendMessage("You need a Magic level of 63 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 3)
              || !player.getMagic().hasRunes(ItemId.FIRE_RUNE, 30)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 3);
          player.getMagic().deleteRunes(ItemId.FIRE_RUNE, 30);
          player.setAnimation(726);
          player.setGraphic(150, 92);
          player.getSkills().addXp(Skills.MAGIC, 73);
          player.getInventory().deleteItem(ItemId.UNPOWERED_ORB);
          player.getInventory().addItem(ItemId.FIRE_ORB);
          return true;
        case CHARGE_AIR_ORB:
          if (mapObject.getId() != 2152) {
            player.getGameEncoder().sendMessage("This spell needs to be cast on an air obelisk.");
            return true;
          }
          if (!player.getInventory().hasItem(ItemId.UNPOWERED_ORB)) {
            player.getGameEncoder().sendMessage("You need an unpowered orb to charge.");
            return true;
          }
          if (player.getSkills().getLevel(Skills.MAGIC) < 66) {
            player.getGameEncoder().sendMessage("You need a Magic level of 66 to cast this spell.");
            return true;
          }
          if (!player.getMagic().hasRunes(ItemId.COSMIC_RUNE, 3)
              || !player.getMagic().hasRunes(ItemId.AIR_RUNE, 30)) {
            player.getMagic().notEnoughRunes();
            return true;
          }
          player.getMagic().deleteRunes(ItemId.COSMIC_RUNE, 3);
          player.getMagic().deleteRunes(ItemId.AIR_RUNE, 30);
          player.setAnimation(726);
          player.setGraphic(150, 92);
          player.getSkills().addXp(Skills.MAGIC, 76);
          player.getInventory().deleteItem(ItemId.UNPOWERED_ORB);
          player.getInventory().addItem(ItemId.AIR_ORB);
          return true;
        default:
          return false;
      }
    }
    return false;
  }
}
