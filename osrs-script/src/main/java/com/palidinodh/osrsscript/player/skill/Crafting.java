package com.palidinodh.osrsscript.player.skill;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.ValueEnteredEvent;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.model.player.skill.SkillEntry;
import com.palidinodh.util.PNumber;
import lombok.var;

public class Crafting extends SkillContainer {
  private static final int LEATHER_COST = 100;
  private static final int HARD_LEATHER_COST = 300;
  private static final int SNAKESKIN_LEATHER_COST = 1500;
  private static final int GREEN_DRAGON_LEATHER_COST = 2000;
  private static final int BLUE_DRAGON_LEATHER_COST = 2000;
  private static final int RED_DRAGON_LEATHER_COST = 2000;
  private static final int BLACK_DRAGON_LEATHER_COST = 2000;

  private static List<SkillEntry> entries = new ArrayList<>();

  @Override
  public int getSkillId() {
    return Skills.CRAFTING;
  }

  public List<SkillEntry> getEntries() {
    return entries;
  }

  @Override
  public boolean widgetHook(Player player, int index, int widgetId, int childId, int slot,
      int itemId) {
    if (widgetId == WidgetId.TANNING) {
      var amount = 1;
      if (childId >= 124 && childId <= 130) {
        amount = -1;
      } else if (childId >= 132 && childId <= 138) {
        amount = -2;
      } else if (childId >= 140 && childId <= 146) {
        amount = 5;
      }
      int makeItemId;
      if (childId == 124 || childId == 132 || childId == 140 || childId == 148) {
        makeItemId = ItemId.LEATHER;
      } else if (childId == 125 || childId == 133 || childId == 141 || childId == 149) {
        makeItemId = ItemId.HARD_LEATHER;
      } else if (childId == 126 || childId == 134 || childId == 142 || childId == 150) {
        makeItemId = ItemId.GREEN_DRAGON_LEATHER;
      } else if (childId == 127 || childId == 135 || childId == 143 || childId == 151) {
        makeItemId = ItemId.BLUE_DRAGON_LEATHER;
      } else if (childId == 128 || childId == 136 || childId == 144 || childId == 152) {
        makeItemId = ItemId.RED_DRAGON_LEATHER;
      } else if (childId == 129 || childId == 137 || childId == 145 || childId == 153) {
        makeItemId = ItemId.BLACK_DRAGON_LEATHER;
      } else if (childId == 130 || childId == 138 || childId == 146 || childId == 154) {
        makeItemId = ItemId.SNAKESKIN;
      } else {
        makeItemId = -1;
      }
      if (makeItemId == -1) {
        return true;
      }
      if (amount == -2) {
        player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
          @Override
          public void execute(int value) {
            tanHide(player, makeItemId, value);
          }
        });
      } else {
        tanHide(player, makeItemId, amount);
      }
      return true;
    } else if (widgetId == WidgetId.MAKE_JEWELRY) {
      if (itemId == ItemId.GOLD_BRACELET_11068) {
        itemId = ItemId.GOLD_BRACELET;
      } else if (itemId == ItemId.SAPPHIRE_BRACELET_11071) {
        itemId = ItemId.SAPPHIRE_BRACELET;
      } else if (itemId == ItemId.EMERALD_BRACELET_11078) {
        itemId = ItemId.EMERALD_BRACELET;
      } else if (itemId == ItemId.RUBY_BRACELET_11087) {
        itemId = ItemId.RUBY_BRACELET;
      } else if (itemId == ItemId.DIAMOND_BRACELET_11094) {
        itemId = ItemId.DIAMOND_BRACELET;
      } else if (itemId == ItemId.DRAGON_BRACELET) {
        itemId = ItemId.DRAGONSTONE_BRACELET;
      } else if (itemId == ItemId.ONYX_BRACELET_11132) {
        itemId = ItemId.ONYX_BRACELET;
      } else if (itemId == ItemId.ZENYTE_BRACELET_19492) {
        itemId = ItemId.ZENYTE_BRACELET;
      }
      var entry = findEntryFromCreate(itemId);
      if (entry == null) {
        return true;
      }
      if (index == 0) {
        startEvent(player, entry, 1);
      } else if (index == 1) {
        startEvent(player, entry, 5);
      } else if (index == 2) {
        startEvent(player, entry, 10);
      } else if (index == 3) {
        player.getGameEncoder().sendEnterAmount(new ValueEnteredEvent.IntegerEvent() {
          @Override
          public void execute(int value) {
            startEvent(player, entry, value);
          }
        });
      } else if (index == 4) {
        startEvent(player, entry, 28);
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean widgetOnMapObjectHook(Player player, int widgetId, int childId, int slot,
      int itemId, MapObject mapObject) {
    if (widgetId == WidgetId.INVENTORY && mapObject.getDef().hasOption("smelt")) {
      if (itemId == ItemId.RING_MOULD || itemId == ItemId.NECKLACE_MOULD
          || itemId == ItemId.BRACELET_MOULD || itemId == ItemId.AMULET_MOULD
          || itemId == ItemId.GOLD_BAR || itemId == ItemId.SAPPHIRE || itemId == ItemId.EMERALD
          || itemId == ItemId.RUBY || itemId == ItemId.DIAMOND || itemId == ItemId.DRAGONSTONE
          || itemId == ItemId.ONYX || itemId == ItemId.ZENYTE) {
        player.getWidgetManager().sendInteractiveOverlay(WidgetId.MAKE_JEWELRY);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean npcOptionHook(Player player, int index, Npc npc) {
    if (npc.getId() == NpcId.TANNER) {
      openTanning(player);
      return true;
    }
    return false;
  }

  private void tanHide(Player player, int craftItemId, int amount) {
    var baseItemId = -1;
    var cost = -1;
    if (craftItemId == ItemId.LEATHER) {
      baseItemId = ItemId.COWHIDE;
      cost = LEATHER_COST;
    } else if (craftItemId == ItemId.HARD_LEATHER) {
      baseItemId = ItemId.COWHIDE;
      cost = HARD_LEATHER_COST;
    } else if (craftItemId == ItemId.GREEN_DRAGON_LEATHER) {
      baseItemId = ItemId.GREEN_DRAGONHIDE;
      cost = GREEN_DRAGON_LEATHER_COST;
    } else if (craftItemId == ItemId.BLUE_DRAGON_LEATHER) {
      baseItemId = ItemId.BLUE_DRAGONHIDE;
      cost = BLUE_DRAGON_LEATHER_COST;
    } else if (craftItemId == ItemId.RED_DRAGON_LEATHER) {
      baseItemId = ItemId.RED_DRAGONHIDE;
      cost = RED_DRAGON_LEATHER_COST;
    } else if (craftItemId == ItemId.BLACK_DRAGON_LEATHER) {
      baseItemId = ItemId.BLACK_DRAGONHIDE;
      cost = BLACK_DRAGON_LEATHER_COST;
    } else if (craftItemId == ItemId.SNAKESKIN) {
      baseItemId = ItemId.SNAKE_HIDE;
      cost = SNAKESKIN_LEATHER_COST;
    }
    if (baseItemId == -1 || cost == -1) {
      return;
    }
    if (player.getInventory().getCount(baseItemId) == 0) {
      baseItemId = ItemDef.getNotedId(baseItemId);
      craftItemId = ItemDef.getNotedId(craftItemId);
    }
    if (amount == -1) {
      amount = player.getInventory().getCount(baseItemId);
    }
    if (amount > player.getInventory().getCount(baseItemId)) {
      amount = player.getInventory().getCount(baseItemId);
    }
    if (baseItemId == -1 || craftItemId == -1 || amount == 0) {
      player.getGameEncoder().sendMessage("You don't have raw hide to do this.");
      return;
    }
    if (player.getInventory().getCount(ItemId.COINS) < amount * cost) {
      player.getGameEncoder().sendMessage("You don't have enough coins to do this.");
      return;
    }
    player.getInventory().deleteItem(ItemId.COINS, amount * cost);
    player.getInventory().deleteItem(baseItemId, amount);
    player.getInventory().addItem(craftItemId, amount);
    AchievementDiary.makeItemUpdate(player, getSkillId(), new RandomItem(craftItemId, amount), null,
        null);
  }

  private void openTanning(Player player) {
    player.getWidgetManager().sendInteractiveOverlay(WidgetId.TANNING);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 100, ItemId.LEATHER, 200);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 101, ItemId.HARD_LEATHER, 200);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 102, ItemId.GREEN_DRAGON_LEATHER,
        200);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 103, ItemId.BLUE_DRAGON_LEATHER,
        200);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 104, ItemId.RED_DRAGON_LEATHER,
        200);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 105, ItemId.BLACK_DRAGON_LEATHER,
        200);
    player.getGameEncoder().sendWidgetItemModel(WidgetId.TANNING, 106, ItemId.SNAKESKIN, 200);
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 108, "Leather");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 116,
        PNumber.formatNumber(LEATHER_COST) + " Coins");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 109, "Hard Leather");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 117,
        PNumber.formatNumber(HARD_LEATHER_COST) + " Coins");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 110, "Green Leather");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 118,
        PNumber.formatNumber(GREEN_DRAGON_LEATHER_COST) + " Coins");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 111, "Blue Leather");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 119,
        PNumber.formatNumber(BLUE_DRAGON_LEATHER_COST) + " Coins");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 112, "Red Leather");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 120,
        PNumber.formatNumber(RED_DRAGON_LEATHER_COST) + " Coins");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 113, "Black Leather");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 121,
        PNumber.formatNumber(BLACK_DRAGON_LEATHER_COST) + " Coins");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 114, "Snakeskin");
    player.getGameEncoder().sendWidgetText(WidgetId.TANNING, 122,
        PNumber.formatNumber(SNAKESKIN_LEATHER_COST) + " Coins");
  }

  static {
    entries.add(SkillEntry.builder().level(41).experience(70).animation(7386)
        .tool(new Item(ItemId.HAMMER)).consume(new RandomItem(ItemId.OAK_SHIELD))
        .consume(new RandomItem(ItemId.HARD_LEATHER, 2))
        .consume(new RandomItem(ItemId.BRONZE_NAILS, 15))
        .create(new RandomItem(ItemId.HARD_LEATHER_SHIELD)).build());
    entries.add(SkillEntry.builder().level(56).experience(100).animation(7387)
        .tool(new Item(ItemId.HAMMER)).consume(new RandomItem(ItemId.WILLOW_SHIELD))
        .consume(new RandomItem(ItemId.SNAKESKIN, 2)).consume(new RandomItem(ItemId.IRON_NAILS, 15))
        .create(new RandomItem(ItemId.SNAKESKIN_SHIELD)).build());
    entries.add(SkillEntry.builder().level(62).experience(124).animation(7831)
        .tool(new Item(ItemId.HAMMER)).consume(new RandomItem(ItemId.MAPLE_SHIELD))
        .consume(new RandomItem(ItemId.GREEN_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.STEEL_NAILS, 15))
        .create(new RandomItem(ItemId.GREEN_DHIDE_SHIELD)).build());
    entries.add(SkillEntry.builder().level(69).experience(140).animation(7832)
        .tool(new Item(ItemId.HAMMER)).consume(new RandomItem(ItemId.YEW_SHIELD))
        .consume(new RandomItem(ItemId.BLUE_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.MITHRIL_NAILS, 15))
        .create(new RandomItem(ItemId.BLUE_DHIDE_SHIELD)).build());
    entries.add(SkillEntry.builder().level(76).experience(156).animation(7833)
        .tool(new Item(ItemId.HAMMER)).consume(new RandomItem(ItemId.MAGIC_SHIELD))
        .consume(new RandomItem(ItemId.RED_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.ADAMANTITE_NAILS, 15))
        .create(new RandomItem(ItemId.RED_DHIDE_SHIELD)).build());
    entries.add(SkillEntry.builder().level(83).experience(172).animation(7834)
        .tool(new Item(ItemId.HAMMER)).consume(new RandomItem(ItemId.REDWOOD_SHIELD))
        .consume(new RandomItem(ItemId.BLACK_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.RUNE_NAILS, 15))
        .create(new RandomItem(ItemId.BLACK_DHIDE_SHIELD)).build());
    entries.add(
        SkillEntry.builder().level(1).experience(14).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.LEATHER_GLOVES)).build());
    entries.add(
        SkillEntry.builder().level(7).experience(16).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.LEATHER_BOOTS)).build());
    entries.add(
        SkillEntry.builder().level(9).experience(19).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.LEATHER_COWL)).build());
    entries.add(
        SkillEntry.builder().level(11).experience(22).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.LEATHER_VAMBRACES)).build());
    entries.add(
        SkillEntry.builder().level(14).experience(25).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.LEATHER_BODY)).build());
    entries.add(
        SkillEntry.builder().level(18).experience(27).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.LEATHER_CHAPS)).build());
    entries.add(SkillEntry.builder().level(38).experience(37).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.LEATHER))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.COIF)).build());
    entries.add(
        SkillEntry.builder().level(28).experience(35).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.HARD_LEATHER)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.HARDLEATHER_BODY)).build());
    entries.add(SkillEntry.builder().level(57).experience(62).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.GREEN_DRAGON_LEATHER))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.GREEN_DHIDE_VAMB))
        .build());
    entries.add(SkillEntry.builder().level(60).experience(124).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.GREEN_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.GREEN_DHIDE_CHAPS))
        .build());
    entries.add(SkillEntry.builder().level(63).experience(186).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.GREEN_DRAGON_LEATHER, 3))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.GREEN_DHIDE_BODY))
        .build());
    entries.add(SkillEntry.builder().level(66).experience(70).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.BLUE_DRAGON_LEATHER))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.BLUE_DHIDE_VAMB))
        .build());
    entries.add(SkillEntry.builder().level(68).experience(140).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.BLUE_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.BLUE_DHIDE_CHAPS))
        .build());
    entries.add(SkillEntry.builder().level(71).experience(210).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.BLUE_DRAGON_LEATHER, 3))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.BLUE_DHIDE_BODY))
        .build());
    entries.add(SkillEntry.builder().level(73).experience(78).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.RED_DRAGON_LEATHER))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.RED_DHIDE_VAMB))
        .build());
    entries.add(SkillEntry.builder().level(75).experience(156).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.RED_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.RED_DHIDE_CHAPS))
        .build());
    entries.add(SkillEntry.builder().level(77).experience(234).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.RED_DRAGON_LEATHER, 3))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.RED_DHIDE_BODY))
        .build());
    entries.add(SkillEntry.builder().level(79).experience(86).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.BLACK_DRAGON_LEATHER))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.BLACK_DHIDE_VAMB))
        .build());
    entries.add(SkillEntry.builder().level(82).experience(172).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.BLACK_DRAGON_LEATHER, 2))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.BLACK_DHIDE_CHAPS))
        .build());
    entries.add(SkillEntry.builder().level(84).experience(258).animation(1249)
        .tool(new Item(ItemId.NEEDLE)).consume(new RandomItem(ItemId.BLACK_DRAGON_LEATHER, 3))
        .consume(new RandomItem(ItemId.THREAD)).create(new RandomItem(ItemId.BLACK_DHIDE_BODY))
        .build());
    entries.add(
        SkillEntry.builder().level(45).experience(30).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.SNAKESKIN, 6)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.SNAKESKIN_BOOTS)).build());
    entries.add(
        SkillEntry.builder().level(47).experience(35).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.SNAKESKIN, 8)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.SNAKESKIN_VAMBRACES)).build());
    entries.add(
        SkillEntry.builder().level(48).experience(45).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.SNAKESKIN, 5)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.SNAKESKIN_BANDANA)).build());
    entries.add(
        SkillEntry.builder().level(51).experience(50).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.SNAKESKIN, 12)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.SNAKESKIN_CHAPS)).build());
    entries.add(
        SkillEntry.builder().level(53).experience(55).animation(1249).tool(new Item(ItemId.NEEDLE))
            .consume(new RandomItem(ItemId.SNAKESKIN, 15)).consume(new RandomItem(ItemId.THREAD))
            .create(new RandomItem(ItemId.SNAKESKIN_BODY)).build());
    entries.add(SkillEntry.builder().level(1).experience(15).animation(886)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_OPAL))
        .create(new RandomItem(ItemId.OPAL)).build());
    entries.add(SkillEntry.builder().level(13).experience(20).animation(889)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_JADE))
        .create(new RandomItem(ItemId.JADE)).build());
    entries.add(SkillEntry.builder().level(16).experience(25).animation(887)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_RED_TOPAZ))
        .create(new RandomItem(ItemId.RED_TOPAZ)).build());
    entries.add(SkillEntry.builder().level(20).experience(50).animation(888)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_SAPPHIRE))
        .create(new RandomItem(ItemId.SAPPHIRE)).build());
    entries.add(SkillEntry.builder().level(27).experience(68).animation(889)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_EMERALD))
        .create(new RandomItem(ItemId.EMERALD)).build());
    entries.add(SkillEntry.builder().level(34).experience(85).animation(887)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_RUBY))
        .create(new RandomItem(ItemId.RUBY)).build());
    entries.add(SkillEntry.builder().level(43).experience(108).animation(886)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_DIAMOND))
        .create(new RandomItem(ItemId.DIAMOND)).build());
    entries.add(SkillEntry.builder().level(55).experience(138).animation(885)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_DRAGONSTONE))
        .create(new RandomItem(ItemId.DRAGONSTONE)).build());
    entries.add(SkillEntry.builder().level(67).experience(168).animation(2717)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_ONYX))
        .create(new RandomItem(ItemId.ONYX)).build());
    entries.add(SkillEntry.builder().level(89).experience(200).animation(2717)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.UNCUT_ZENYTE))
        .create(new RandomItem(ItemId.ZENYTE)).build());
    entries.add(SkillEntry.builder().level(83).experience(60).animation(887)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.AMETHYST))
        .create(new RandomItem(ItemId.AMETHYST_BOLT_TIPS, 15)).build());
    entries.add(SkillEntry.builder().level(85).experience(60).animation(887)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.AMETHYST))
        .create(new RandomItem(ItemId.AMETHYST_ARROWTIPS, 15)).build());
    entries.add(SkillEntry.builder().level(87).experience(60).animation(887)
        .tool(new Item(ItemId.CHISEL)).consume(new RandomItem(ItemId.AMETHYST))
        .create(new RandomItem(ItemId.AMETHYST_JAVELIN_HEADS, 5)).build());
    entries.add(SkillEntry.builder().level(5).experience(15).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).create(new RandomItem(ItemId.GOLD_RING)).build());
    entries.add(
        SkillEntry.builder().level(6).experience(20).animation(899).widgetId(WidgetId.MAKE_JEWELRY)
            .tool(new Item(ItemId.NECKLACE_MOULD)).consume(new RandomItem(ItemId.GOLD_BAR))
            .create(new RandomItem(ItemId.GOLD_NECKLACE)).build());
    entries.add(
        SkillEntry.builder().level(7).experience(25).animation(899).widgetId(WidgetId.MAKE_JEWELRY)
            .tool(new Item(ItemId.BRACELET_MOULD)).consume(new RandomItem(ItemId.GOLD_BAR))
            .create(new RandomItem(ItemId.GOLD_BRACELET)).build());
    entries.add(
        SkillEntry.builder().level(8).experience(30).animation(899).widgetId(WidgetId.MAKE_JEWELRY)
            .tool(new Item(ItemId.AMULET_MOULD)).consume(new RandomItem(ItemId.GOLD_BAR))
            .create(new RandomItem(ItemId.GOLD_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(20).experience(40).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.SAPPHIRE))
        .create(new RandomItem(ItemId.SAPPHIRE_RING)).build());
    entries.add(SkillEntry.builder().level(22).experience(55).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.SAPPHIRE))
        .create(new RandomItem(ItemId.SAPPHIRE_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(23).experience(60).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.SAPPHIRE))
        .create(new RandomItem(ItemId.SAPPHIRE_BRACELET)).build());
    entries.add(SkillEntry.builder().level(24).experience(65).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.SAPPHIRE))
        .create(new RandomItem(ItemId.SAPPHIRE_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(27).experience(55).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.EMERALD))
        .create(new RandomItem(ItemId.EMERALD_RING)).build());
    entries.add(SkillEntry.builder().level(29).experience(60).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.EMERALD))
        .create(new RandomItem(ItemId.EMERALD_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(30).experience(65).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.EMERALD))
        .create(new RandomItem(ItemId.EMERALD_BRACELET)).build());
    entries.add(SkillEntry.builder().level(31).experience(70).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.EMERALD))
        .create(new RandomItem(ItemId.EMERALD_AMULET_U)).build());
    entries.add(
        SkillEntry.builder().level(34).experience(70).animation(899).widgetId(WidgetId.MAKE_JEWELRY)
            .tool(new Item(ItemId.RING_MOULD)).consume(new RandomItem(ItemId.GOLD_BAR))
            .consume(new RandomItem(ItemId.RUBY)).create(new RandomItem(ItemId.RUBY_RING)).build());
    entries.add(SkillEntry.builder().level(40).experience(75).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.RUBY))
        .create(new RandomItem(ItemId.RUBY_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(42).experience(80).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.RUBY))
        .create(new RandomItem(ItemId.RUBY_BRACELET)).build());
    entries.add(SkillEntry.builder().level(50).experience(85).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.RUBY))
        .create(new RandomItem(ItemId.RUBY_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(43).experience(85).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DIAMOND))
        .create(new RandomItem(ItemId.DIAMOND_RING)).build());
    entries.add(SkillEntry.builder().level(56).experience(90).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DIAMOND))
        .create(new RandomItem(ItemId.DIAMOND_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(58).experience(95).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DIAMOND))
        .create(new RandomItem(ItemId.DIAMOND_BRACELET)).build());
    entries.add(SkillEntry.builder().level(70).experience(100).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DIAMOND))
        .create(new RandomItem(ItemId.DIAMOND_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(55).experience(100).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DRAGONSTONE))
        .create(new RandomItem(ItemId.DRAGONSTONE_RING)).build());
    entries.add(SkillEntry.builder().level(72).experience(105).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DRAGONSTONE))
        .create(new RandomItem(ItemId.DRAGON_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(74).experience(110).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DRAGONSTONE))
        .create(new RandomItem(ItemId.DRAGONSTONE_BRACELET)).build());
    entries.add(SkillEntry.builder().level(80).experience(150).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.DRAGONSTONE))
        .create(new RandomItem(ItemId.DRAGONSTONE_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(67).experience(115).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ONYX))
        .create(new RandomItem(ItemId.ONYX_RING)).build());
    entries.add(SkillEntry.builder().level(82).experience(120).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ONYX))
        .create(new RandomItem(ItemId.ONYX_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(84).experience(125).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ONYX))
        .create(new RandomItem(ItemId.ONYX_BRACELET)).build());
    entries.add(SkillEntry.builder().level(90).experience(165).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ONYX))
        .create(new RandomItem(ItemId.ONYX_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(89).experience(150).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.RING_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ZENYTE))
        .create(new RandomItem(ItemId.ZENYTE_RING)).build());
    entries.add(SkillEntry.builder().level(92).experience(165).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.NECKLACE_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ZENYTE))
        .create(new RandomItem(ItemId.ZENYTE_NECKLACE)).build());
    entries.add(SkillEntry.builder().level(95).experience(180).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.BRACELET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ZENYTE))
        .create(new RandomItem(ItemId.ZENYTE_BRACELET)).build());
    entries.add(SkillEntry.builder().level(98).experience(200).animation(899)
        .widgetId(WidgetId.MAKE_JEWELRY).tool(new Item(ItemId.AMULET_MOULD))
        .consume(new RandomItem(ItemId.GOLD_BAR)).consume(new RandomItem(ItemId.ZENYTE))
        .create(new RandomItem(ItemId.ZENYTE_AMULET_U)).build());
    entries.add(SkillEntry.builder().level(8).experience(4)
        .consume(new RandomItem(ItemId.BALL_OF_WOOL)).consume(new RandomItem(ItemId.GOLD_AMULET_U))
        .create(new RandomItem(ItemId.GOLD_AMULET)).build());
    entries.add(
        SkillEntry.builder().level(24).experience(4).consume(new RandomItem(ItemId.BALL_OF_WOOL))
            .consume(new RandomItem(ItemId.SAPPHIRE_AMULET_U))
            .create(new RandomItem(ItemId.SAPPHIRE_AMULET)).build());
    entries.add(
        SkillEntry.builder().level(31).experience(4).consume(new RandomItem(ItemId.BALL_OF_WOOL))
            .consume(new RandomItem(ItemId.EMERALD_AMULET_U))
            .create(new RandomItem(ItemId.EMERALD_AMULET)).build());
    entries.add(SkillEntry.builder().level(50).experience(4)
        .consume(new RandomItem(ItemId.BALL_OF_WOOL)).consume(new RandomItem(ItemId.RUBY_AMULET_U))
        .create(new RandomItem(ItemId.RUBY_AMULET)).build());
    entries.add(
        SkillEntry.builder().level(70).experience(4).consume(new RandomItem(ItemId.BALL_OF_WOOL))
            .consume(new RandomItem(ItemId.DIAMOND_AMULET_U))
            .create(new RandomItem(ItemId.DIAMOND_AMULET)).build());
    entries.add(
        SkillEntry.builder().level(80).experience(4).consume(new RandomItem(ItemId.BALL_OF_WOOL))
            .consume(new RandomItem(ItemId.DRAGONSTONE_AMULET_U))
            .create(new RandomItem(ItemId.DRAGONSTONE_AMULET)).build());
    entries.add(SkillEntry.builder().level(90).experience(4)
        .consume(new RandomItem(ItemId.BALL_OF_WOOL)).consume(new RandomItem(ItemId.ONYX_AMULET_U))
        .create(new RandomItem(ItemId.ONYX_AMULET)).build());
    entries.add(
        SkillEntry.builder().level(98).experience(4).consume(new RandomItem(ItemId.BALL_OF_WOOL))
            .consume(new RandomItem(ItemId.ZENYTE_AMULET_U))
            .create(new RandomItem(ItemId.ZENYTE_AMULET)).build());
    entries.add(SkillEntry.builder().level(35).experience(4)
        .consume(new RandomItem(ItemId.BALL_OF_WOOL)).consume(new RandomItem(ItemId.SALVE_SHARD))
        .create(new RandomItem(ItemId.SALVE_AMULET)).build());
    entries.add(SkillEntry.builder().level(54).experience(100)
        .consume(new RandomItem(ItemId.BATTLESTAFF)).consume(new RandomItem(ItemId.WATER_ORB))
        .create(new RandomItem(ItemId.WATER_BATTLESTAFF)).build());
    entries.add(SkillEntry.builder().level(58).experience(113)
        .consume(new RandomItem(ItemId.BATTLESTAFF)).consume(new RandomItem(ItemId.EARTH_ORB))
        .create(new RandomItem(ItemId.EARTH_BATTLESTAFF)).build());
    entries.add(SkillEntry.builder().level(62).experience(125)
        .consume(new RandomItem(ItemId.BATTLESTAFF)).consume(new RandomItem(ItemId.FIRE_ORB))
        .create(new RandomItem(ItemId.FIRE_BATTLESTAFF)).build());
    entries.add(SkillEntry.builder().level(66).experience(138)
        .consume(new RandomItem(ItemId.BATTLESTAFF)).consume(new RandomItem(ItemId.AIR_ORB))
        .create(new RandomItem(ItemId.AIR_BATTLESTAFF)).build());
    entries.add(SkillEntry.builder().level(1).experience(17).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.BEER_GLASS)).build());
    entries.add(SkillEntry.builder().level(4).experience(19).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.EMPTY_CANDLE_LANTERN)).build());
    entries.add(SkillEntry.builder().level(12).experience(25).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.EMPTY_OIL_LAMP)).build());
    entries.add(SkillEntry.builder().level(33).experience(35).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.VIAL)).build());
    entries.add(SkillEntry.builder().level(42).experience(42).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.EMPTY_FISHBOWL)).build());
    entries.add(SkillEntry.builder().level(46).experience(52).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.UNPOWERED_ORB)).build());
    entries.add(SkillEntry.builder().level(49).experience(55).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.LANTERN_LENS)).build());
    entries.add(SkillEntry.builder().level(87).experience(70).animation(884)
        .tool(new Item(ItemId.GLASSBLOWING_PIPE)).consume(new RandomItem(ItemId.MOLTEN_GLASS))
        .create(new RandomItem(ItemId.EMPTY_LIGHT_ORB)).build());
    entries.add(SkillEntry.builder().level(1).consume(new RandomItem(ItemId.BULLSEYE_LANTERN_UNF))
        .consume(new RandomItem(ItemId.LANTERN_LENS))
        .create(new RandomItem(ItemId.BULLSEYE_LANTERN_EMPTY)).build());
    entries.add(SkillEntry.builder().level(1).widgetOnMapObject(4026)
        .consume(new RandomItem(ItemId.BULLSEYE_LANTERN_EMPTY))
        .consume(new RandomItem(ItemId.SWAMP_TAR)).create(new RandomItem(ItemId.BULLSEYE_LANTERN))
        .build());
    entries.add(SkillEntry.builder().level(52).experience(120).tool(new Item(ItemId.CHISEL))
        .consume(new RandomItem(ItemId.SERPENTINE_VISAGE))
        .create(new RandomItem(ItemId.SERPENTINE_HELM_UNCHARGED)).build());
    entries.add(SkillEntry.builder().level(59).tool(new Item(ItemId.CHISEL))
        .consume(new RandomItem(ItemId.MAGIC_FANG))
        .consume(new RandomItem(ItemId.STAFF_OF_THE_DEAD))
        .create(new RandomItem(ItemId.TOXIC_STAFF_UNCHARGED)).build());
    entries.add(SkillEntry.builder().level(59).tool(new Item(ItemId.CHISEL))
        .consume(new RandomItem(ItemId.MAGIC_FANG))
        .consume(new RandomItem(ItemId.UNCHARGED_TRIDENT))
        .create(new RandomItem(ItemId.UNCHARGED_TOXIC_TRIDENT)).build());
    entries.add(SkillEntry.builder().level(59).tool(new Item(ItemId.CHISEL))
        .consume(new RandomItem(ItemId.MAGIC_FANG))
        .consume(new RandomItem(ItemId.UNCHARGED_TRIDENT_E))
        .create(new RandomItem(ItemId.UNCHARGED_TOXIC_TRIDENT_E)).build());
    entries.add(SkillEntry.builder().level(1).consume(new RandomItem(ItemId.ONYX))
        .consume(new RandomItem(ItemId.ZENYTE_SHARD)).create(new RandomItem(ItemId.UNCUT_ZENYTE))
        .build());
  }
}
