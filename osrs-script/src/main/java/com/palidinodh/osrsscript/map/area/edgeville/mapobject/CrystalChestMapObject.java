package com.palidinodh.osrsscript.map.area.edgeville.mapobject;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.ObjectId;
import com.palidinodh.osrscore.io.incomingpacket.MapObjectHandler;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrsscript.player.plugin.slayer.SlayerPlugin;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.ReferenceId;
import lombok.var;

@ReferenceId(ObjectId.CLOSED_CHEST_172)
class CrystalChestMapObject implements MapObjectHandler {
  private static final List<RandomItem> CRYSTAL_ITEMS;
  private static final List<RandomItem> BRIMSTONE_ITEMS;

  @Override
  public void execute(Player player, int option, MapObject mapObject) {
    if (player.getInventory().hasItem(ItemId.CRYSTAL_KEY)) {
      openCrystalChest(player, mapObject);
    } else if (player.getInventory().hasItem(ItemId.BRIMSTONE_KEY)) {
      openBrimstoneChest(player, mapObject);
    } else {
      player.getGameEncoder().sendMessage("You need a key to open this.");
    }
  }

  private void openCrystalChest(Player player, MapObject mapObject) {
    if (player.getInventory().getRemainingSlots() < 4) {
      player.getInventory().notEnoughSpace();
      return;
    }
    player.getInventory().deleteItem(ItemId.CRYSTAL_KEY);
    player.getInventory().addOrDropItem(ItemId.UNCUT_DRAGONSTONE_NOTED);
    var clueItems = RandomItem.buildList(new RandomItem(ItemId.CLUE_SCROLL_EASY).weight(8),
        new RandomItem(ItemId.CLUE_SCROLL_MEDIUM).weight(6),
        new RandomItem(ItemId.CLUE_SCROLL_HARD).weight(4),
        new RandomItem(ItemId.CLUE_SCROLL_ELITE).weight(2),
        new RandomItem(ItemId.CLUE_SCROLL_MASTER).weight(1));
    if (PRandom.randomE(4) == 0) {
      player.getInventory().addOrDropItem(RandomItem.getItem(clueItems));
    }
    for (int i = 0; i < 2; i++) {
      var item = RandomItem.getItem(CRYSTAL_ITEMS);
      var amount = item.getAmount();
      if (amount > 1 && player.isGameModeIronmanRelated()) {
        amount /= 2;
      }
      player.getInventory().addOrDropItem(item.getNotedId(), amount);
    }
    player.getGameEncoder().sendMessage("You find some treasure in the chest!");
  }

  private void openBrimstoneChest(Player player, MapObject mapObject) {
    player.getInventory().deleteItem(ItemId.BRIMSTONE_KEY);
    var item = RandomItem.getItem(BRIMSTONE_ITEMS);
    player.getInventory().addOrDropItem(item.getNotedId(), item.getAmount());
    player.getGameEncoder().sendMessage("You find some treasure in the chest!");
    var plugin = player.getPlugin(SlayerPlugin.class);
    plugin.incrimentBrimstoneKeys();
  }

  static {
    var items = new ArrayList<RandomItem>();
    items.add(new RandomItem(ItemId.LOOP_HALF_OF_KEY));
    items.add(new RandomItem(ItemId.TOOTH_HALF_OF_KEY));
    items.add(new RandomItem(ItemId.BABYDRAGON_BONES, 1, 150));
    items.add(new RandomItem(ItemId.DRAGON_BONES, 1, 50));
    items.add(new RandomItem(ItemId.LAVA_DRAGON_BONES, 1, 25));
    items.add(new RandomItem(ItemId.WYRM_BONES, 1, 80));
    items.add(new RandomItem(ItemId.DRAKE_BONES, 1, 30));
    items.add(new RandomItem(ItemId.HYDRA_BONES, 1, 20));
    items.add(new RandomItem(ItemId.RAW_KARAMBWAN, 1, 200));
    items.add(new RandomItem(ItemId.RAW_SWORDFISH, 1, 650));
    items.add(new RandomItem(ItemId.RAW_MONKFISH, 1, 350));
    items.add(new RandomItem(ItemId.RAW_SHARK, 1, 150));
    items.add(new RandomItem(ItemId.RAW_ANGLERFISH, 1, 80));
    items.add(new RandomItem(ItemId.RAW_DARK_CRAB, 1, 100));
    items.add(new RandomItem(ItemId.OAK_LOGS, 1, 1200));
    items.add(new RandomItem(ItemId.WILLOW_LOGS, 1, 900));
    items.add(new RandomItem(ItemId.MAPLE_LOGS, 1, 600));
    items.add(new RandomItem(ItemId.YEW_LOGS, 1, 300));
    items.add(new RandomItem(ItemId.MAGIC_LOGS, 1, 100));
    items.add(new RandomItem(ItemId.REDWOOD_LOGS, 1, 350));
    items.add(new RandomItem(ItemId.GRIMY_RANARR_WEED, 1, 15));
    items.add(new RandomItem(ItemId.GRIMY_TOADFLAX, 1, 60));
    items.add(new RandomItem(ItemId.GRIMY_IRIT_LEAF, 1, 150));
    items.add(new RandomItem(ItemId.GRIMY_KWUARM, 1, 100));
    items.add(new RandomItem(ItemId.GRIMY_SNAPDRAGON, 1, 15));
    items.add(new RandomItem(ItemId.GRIMY_CADANTINE, 1, 80));
    items.add(new RandomItem(ItemId.GRIMY_LANTADYME, 1, 80));
    items.add(new RandomItem(ItemId.GRIMY_DWARF_WEED, 1, 200));
    items.add(new RandomItem(ItemId.GRIMY_TORSTOL, 1, 15));
    items.add(new RandomItem(ItemId.AMYLASE_CRYSTAL, 100));
    items.add(new RandomItem(ItemId.LAVA_SCALE_SHARD, 1, 250));
    items.add(new RandomItem(ItemId.MARK_OF_GRACE, 1, 20));
    items.add(new RandomItem(ItemId.COAL, 1, 700));
    items.add(new RandomItem(ItemId.GOLD_ORE, 1, 300));
    items.add(new RandomItem(ItemId.MITHRIL_ORE, 1, 600));
    items.add(new RandomItem(ItemId.ADAMANTITE_ORE, 1, 80));
    items.add(new RandomItem(ItemId.RUNITE_ORE, 1, 10));
    items.add(new RandomItem(ItemId.AMETHYST, 1, 30));
    items.add(new RandomItem(ItemId.GREEN_DRAGON_LEATHER, 1, 60));
    items.add(new RandomItem(ItemId.BLUE_DRAGON_LEATHER, 1, 50));
    items.add(new RandomItem(ItemId.RED_DRAGON_LEATHER, 1, 40));
    items.add(new RandomItem(ItemId.BLACK_DRAGON_LEATHER, 1, 30));
    items.add(new RandomItem(ItemId.UNCUT_DRAGONSTONE));
    items.add(new RandomItem(ItemId.FIRE_ORB, 1, 80));
    items.add(new RandomItem(ItemId.IRON_BAR, 1, 650));
    items.add(new RandomItem(ItemId.STEEL_BAR, 1, 250));
    items.add(new RandomItem(ItemId.GOLD_BAR, 1, 1200));
    items.add(new RandomItem(ItemId.MITHRIL_BAR, 1, 150));
    items.add(new RandomItem(ItemId.ADAMANTITE_BAR, 1, 50));
    items.add(new RandomItem(ItemId.RUNITE_BAR, 1, 10));
    items.add(new RandomItem(ItemId.DRAGON_DART_TIP, 1, 30));
    items.add(new RandomItem(ItemId.DRAGON_ARROWTIPS, 1, 30));
    items.add(new RandomItem(ItemId.DRAGON_JAVELIN_HEADS, 1, 30));
    items.add(new RandomItem(ItemId.MAGIC_STOCK));
    items.add(new RandomItem(ItemId.RUNITE_LIMBS));
    items.add(new RandomItem(ItemId.ONYX_BOLT_TIPS, 1, 12));
    items.add(new RandomItem(ItemId.PURE_ESSENCE, 3000, 6000));
    items.add(new RandomItem(ItemId.UNCUT_ONYX).weight(1));
    items.add(new RandomItem(ItemId.ZENYTE_SHARD).weight(1));
    CRYSTAL_ITEMS = RandomItem.buildList(items);

    items = new ArrayList<RandomItem>();
    items.add(new RandomItem(ItemId.UNCUT_DIAMOND, 25, 35).weight(1024 - 12));
    items.add(new RandomItem(ItemId.UNCUT_RUBY, 25, 35).weight(1024 - 12));
    items.add(new RandomItem(ItemId.COAL, 300, 500).weight(1024 - 12));
    items.add(new RandomItem(ItemId.COINS, 50_000, 150_000).weight(1024 - 12));
    items.add(new RandomItem(ItemId.GOLD_ORE, 100, 200).weight(1024 - 15));
    items.add(new RandomItem(ItemId.DRAGON_ARROWTIPS, 50, 200).weight(1024 - 15));
    items.add(new RandomItem(ItemId.IRON_ORE, 350, 500).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RUNE_FULL_HELM, 2, 4).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RUNE_PLATEBODY, 1, 2).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RUNE_PLATELEGS, 1, 2).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_TUNA, 100, 350).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_LOBSTER, 100, 350).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_SWORDFISH, 100, 350).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_MONKFISH, 100, 300).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_SHARK, 100, 250).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_SEA_TURTLE, 80, 200).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RAW_MANTA_RAY, 80, 160).weight(1024 - 20));
    items.add(new RandomItem(ItemId.RUNITE_ORE, 10, 15).weight(1024 - 30));
    items.add(new RandomItem(ItemId.STEEL_BAR, 300, 500).weight(1024 - 30));
    items.add(new RandomItem(ItemId.MAGIC_LOGS, 120, 160).weight(1024 - 30));
    items.add(new RandomItem(ItemId.DRAGON_DART_TIP, 40, 160).weight(1024 - 30));
    items.add(new RandomItem(ItemId.PALM_TREE_SEED, 2, 4).weight(1024 - 60));
    items.add(new RandomItem(ItemId.MAGIC_SEED, 2, 4).weight(1024 - 60));
    items.add(new RandomItem(ItemId.CELASTRUS_SEED, 2, 4).weight(1024 - 60));
    items.add(new RandomItem(ItemId.DRAGONFRUIT_TREE_SEED, 2, 4).weight(1024 - 60));
    items.add(new RandomItem(ItemId.REDWOOD_TREE_SEED).weight(1024 - 60));
    items.add(new RandomItem(ItemId.TORSTOL_SEED, 3, 5).weight(1024 - 60));
    items.add(new RandomItem(ItemId.SNAPDRAGON_SEED, 3, 5).weight(1024 - 60));
    items.add(new RandomItem(ItemId.RANARR_SEED, 3, 5).weight(1024 - 60));
    items.add(new RandomItem(ItemId.PURE_ESSENCE, 3000, 6000).weight(1024 - 60));
    items.add(new RandomItem(ItemId.DRAGON_HASTA).weight(1024 - 200));
    items.add(new RandomItem(ItemId.MYSTIC_HAT_DUSK).weight(1024 - 1000));
    items.add(new RandomItem(ItemId.MYSTIC_ROBE_TOP_DUSK).weight(1024 - 1000));
    items.add(new RandomItem(ItemId.MYSTIC_ROBE_BOTTOM_DUSK).weight(1024 - 1000));
    items.add(new RandomItem(ItemId.MYSTIC_GLOVES_DUSK).weight(1024 - 1000));
    items.add(new RandomItem(ItemId.MYSTIC_BOOTS_DUSK).weight(1024 - 1000));
    BRIMSTONE_ITEMS = RandomItem.buildList(items);
  }
}
