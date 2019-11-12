package com.palidinodh.osrsscript.player.plugin.slayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.id.VarbitId;
import com.palidinodh.osrscore.io.cache.id.VarpId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.npc.NpcDef;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.PlayerPlugin;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.controller.BossInstancePC;
import com.palidinodh.osrscore.model.player.slayer.AssignedSlayerTask;
import com.palidinodh.osrscore.model.player.slayer.SlayerMaster;
import com.palidinodh.osrscore.model.player.slayer.SlayerTask;
import com.palidinodh.osrscore.model.player.slayer.SlayerTaskIdentifier;
import com.palidinodh.osrscore.model.player.slayer.SlayerUnlock;
import com.palidinodh.osrsscript.incomingpacket.UseWidgetDecoder;
import com.palidinodh.osrsscript.player.plugin.slayer.dialogue.MasterMenuDialogue;
import com.palidinodh.osrsscript.player.plugin.slayer.dialogue.RewardsDialogue;
import com.palidinodh.osrsscript.player.plugin.slayer.dialogue.SlayerRingDialogue;
import com.palidinodh.osrsscript.player.plugin.slayer.dialogue.WildernessMasterMenuDialogue;
import com.palidinodh.random.PRandom;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PNumber;
import com.palidinodh.util.PTime;
import lombok.Getter;
import lombok.var;

public class SlayerPlugin extends PlayerPlugin {
  private static final int[] BLOCKED_TASK_VARBITS =
      {VarbitId.SLAYER_BLOCKED_TASK_1, VarbitId.SLAYER_BLOCKED_TASK_2,
          VarbitId.SLAYER_BLOCKED_TASK_3, VarbitId.SLAYER_BLOCKED_TASK_4,
          VarbitId.SLAYER_BLOCKED_TASK_5, VarbitId.SLAYER_BLOCKED_TASK_6};
  private static final List<RandomItem> BRIMSTONE_CHEST_ITEMS =
      RandomItem.buildList(new RandomItem(ItemId.UNCUT_DIAMOND, 25, 35).weight(1024 - 12),
          new RandomItem(ItemId.UNCUT_RUBY, 25, 35).weight(1024 - 12),
          new RandomItem(ItemId.COAL, 300, 500).weight(1024 - 12),
          new RandomItem(ItemId.COINS, 50_000, 150_000).weight(1024 - 12),
          new RandomItem(ItemId.GOLD_ORE, 100, 200).weight(1024 - 15),
          new RandomItem(ItemId.DRAGON_ARROWTIPS, 50, 200).weight(1024 - 15),
          new RandomItem(ItemId.IRON_ORE, 350, 500).weight(1024 - 20),
          new RandomItem(ItemId.RUNE_FULL_HELM, 2, 4).weight(1024 - 20),
          new RandomItem(ItemId.RUNE_PLATEBODY, 1, 2).weight(1024 - 20),
          new RandomItem(ItemId.RUNE_PLATELEGS, 1, 2).weight(1024 - 20),
          new RandomItem(ItemId.RAW_TUNA, 100, 350).weight(1024 - 20),
          new RandomItem(ItemId.RAW_LOBSTER, 100, 350).weight(1024 - 20),
          new RandomItem(ItemId.RAW_SWORDFISH, 100, 350).weight(1024 - 20),
          new RandomItem(ItemId.RAW_MONKFISH, 100, 300).weight(1024 - 20),
          new RandomItem(ItemId.RAW_SHARK, 100, 250).weight(1024 - 20),
          new RandomItem(ItemId.RAW_SEA_TURTLE, 80, 200).weight(1024 - 20),
          new RandomItem(ItemId.RAW_MANTA_RAY, 80, 160).weight(1024 - 20),
          new RandomItem(ItemId.RUNITE_ORE, 10, 15).weight(1024 - 30),
          new RandomItem(ItemId.STEEL_BAR, 300, 500).weight(1024 - 30),
          new RandomItem(ItemId.MAGIC_LOGS, 120, 160).weight(1024 - 30),
          new RandomItem(ItemId.DRAGON_DART_TIP, 40, 160).weight(1024 - 30),
          new RandomItem(ItemId.PALM_TREE_SEED, 2, 4).weight(1024 - 60),
          new RandomItem(ItemId.MAGIC_SEED, 2, 4).weight(1024 - 60),
          new RandomItem(ItemId.CELASTRUS_SEED, 2, 4).weight(1024 - 60),
          new RandomItem(ItemId.DRAGONFRUIT_TREE_SEED, 2, 4).weight(1024 - 60),
          new RandomItem(ItemId.REDWOOD_TREE_SEED).weight(1024 - 60),
          new RandomItem(ItemId.TORSTOL_SEED, 3, 5).weight(1024 - 60),
          new RandomItem(ItemId.SNAPDRAGON_SEED, 3, 5).weight(1024 - 60),
          new RandomItem(ItemId.RANARR_SEED, 3, 5).weight(1024 - 60),
          new RandomItem(ItemId.PURE_ESSENCE, 3000, 6000).weight(1024 - 60),
          new RandomItem(ItemId.DRAGON_HASTA).weight(1024 - 200),
          new RandomItem(ItemId.MYSTIC_HAT_DUSK).weight(1024 - 1000),
          new RandomItem(ItemId.MYSTIC_ROBE_TOP_DUSK).weight(1024 - 1000),
          new RandomItem(ItemId.MYSTIC_ROBE_BOTTOM_DUSK).weight(1024 - 1000),
          new RandomItem(ItemId.MYSTIC_GLOVES_DUSK).weight(1024 - 1000),
          new RandomItem(ItemId.MYSTIC_BOOTS_DUSK).weight(1024 - 1000));

  private transient Player player;

  @Getter
  private AssignedSlayerTask task = new AssignedSlayerTask();
  private AssignedSlayerTask wildernessTask = new AssignedSlayerTask();
  @Getter
  private AssignedSlayerTask bossTask = new AssignedSlayerTask();
  private int consecutiveTasks;
  private int consecutiveWildernessTasks;
  private boolean wildernessTaskEmblemUpgrade = true;
  private int points;
  private int bossPoints;
  private int totalTasks;
  private int totalBossTasks;
  private int brimstoneKeys;
  private List<SlayerTaskIdentifier> blockedTasks;
  private List<SlayerUnlock> unlocks;

  @Override
  @SuppressWarnings("rawtypes")
  public void loadLegacy(Map<String, Object> map) {
    if (!map.containsKey("slayer.wildernessOnly") && map.containsKey("slayer.taskAmount")) {
      task = new AssignedSlayerTask((String) map.get("slayer.taskMaster"),
          SlayerTaskIdentifier.get((int) map.get("slayer.taskConfig")),
          (String) map.get("slayer.taskName"), (int) map.get("slayer.taskAmount"),
          PTime.getYearMonthDay());
    }
    if (map.containsKey("slayer.wildernessTaskAmount")) {
      wildernessTask = new AssignedSlayerTask((String) map.get("slayer.wildernessTaskMaster"),
          SlayerTaskIdentifier.get((int) map.get("slayer.wildernessTaskConfig")),
          (String) map.get("slayer.wildernessTaskName"),
          (int) map.get("slayer.wildernessTaskAmount"),
          (String) map.get("slayer.wildernessTaskDate"));
    }
    if (map.containsKey("slayer.bossTaskAmount")) {
      bossTask = new AssignedSlayerTask(SlayerMaster.BOSS_MASTER, null,
          (String) map.get("slayer.bossTaskNamr"), (int) map.get("slayer.bossTaskAmount"),
          (String) map.get("slayer.bossTaskDate"));
    }
    if (map.containsKey("slayer.consecutiveTasks")) {
      consecutiveTasks = (int) map.get("slayer.consecutiveTasks");
    }
    if (map.containsKey("slayer.consecutiveWildernessTasks")) {
      consecutiveWildernessTasks = (int) map.get("slayer.consecutiveWildernessTasks");
    }
    if (map.containsKey("slayer.totalTasks")) {
      totalTasks = (int) map.get("slayer.totalTasks");
    }
    if (map.containsKey("slayer.points")) {
      points = (int) map.get("slayer.points");
    }
    if (map.containsKey("slayer.blockedTasks")) {
      List<Integer> oldBlockedTasks =
          PCollection.castList((List) map.get("slayer.blockedTasks"), Integer.class);
      blockedTasks = new ArrayList<>();
      for (int blockedTask : oldBlockedTasks) {
        SlayerTaskIdentifier identifier = SlayerTaskIdentifier.get(blockedTask);
        if (identifier == null || blockedTasks.contains(identifier)) {
          continue;
        }
        blockedTasks.add(identifier);
      }
    }
    if (map.containsKey("slayer.bossPoints")) {
      bossPoints = (int) map.get("slayer.bossPoints");
    }
    if (map.containsKey("slayer.totalBossTasks")) {
      totalBossTasks = (int) map.get("slayer.totalBossTasks");
    }
    if (map.containsKey("slayer.unlocks")) {
      List<Integer> oldUnlocks =
          PCollection.castList((List) map.get("slayer.unlocks"), Integer.class);
      unlocks = new ArrayList<>();
      for (int unlock : oldUnlocks) {
        SlayerUnlock slayerUnlock = SlayerUnlock.get(unlock);
        if (slayerUnlock == null || unlocks.contains(slayerUnlock)) {
          continue;
        }
        unlocks.add(slayerUnlock);
      }
    }
    if (map.containsKey("slayer.wildernessTaskEmblemUpgrade")) {
      wildernessTaskEmblemUpgrade = (boolean) map.get("slayer.wildernessTaskEmblemUpgrade");
    }
    if (map.containsKey("slayer.brimstoneKeys")) {
      brimstoneKeys = (int) map.get("slayer.brimstoneKeys");
    }
  }

  @Override
  public void login() {
    player = getPlayer();
    sendVarps();
  }

  @Override
  public Object script(String name, Object... args) {
    if (name.equals("slayer_is_any_task")) {
      var npcId = args[0] instanceof Npc ? ((Npc) args[0]).getId() : (int) args[0];
      return isAnyTask(npcId, NpcDef.getName(npcId));
    } else if (name.equals("slayer_is_task")) {
      var npc = (Npc) args[0];
      return isTask(task, npc.getId(), npc.getName());
    } else if (name.equals("slayer_is_wilderness_task")) {
      var npc = (Npc) args[0];
      return isTask(wildernessTask, npc.getId(), npc.getName());
    } else if (name.equals("slayer_is_boss_task")) {
      var npc = (Npc) args[0];
      return isTask(bossTask, npc.getId(), npc.getName());
    } else if (name.equals("slayer_is_unlocked")) {
      return isUnlocked((SlayerUnlock) args[0]);
    } else if (name.equals("slayer_get_task")) {
      var plural = task.getQuantity() > 1
          ? (task.getName().endsWith("ss") ? "es" : (task.getName().endsWith("s") ? "'" : "s"))
          : "";
      return !task.isComplete() ? task.getQuantity() + " " + task.getName() + plural : "none";
    } else if (name.equals("slayer_get_wilderness_task")) {
      var plural =
          wildernessTask.getQuantity() > 1
              ? (wildernessTask.getName().endsWith("ss") ? "es"
                  : (wildernessTask.getName().endsWith("s") ? "'" : "s"))
              : "";
      return !wildernessTask.isComplete()
          ? wildernessTask.getQuantity() + " " + wildernessTask.getName() + plural
          : "none";
    } else if (name.equals("slayer_get_boss_task")) {
      var plural =
          bossTask.getQuantity() > 1
              ? (bossTask.getName().endsWith("ss") ? "es"
                  : (bossTask.getName().endsWith("s") ? "'" : "s"))
              : "";
      return !bossTask.isComplete() ? bossTask.getQuantity() + " " + bossTask.getName() + plural
          : "none";
    } else if (name.equals("slayer_send_information")) {
      player.getGameEncoder().sendMessage(
          "Slayer Task Streaks: " + consecutiveTasks + " / " + consecutiveWildernessTasks);
      player.getGameEncoder().sendMessage("Slayer Points: " + PNumber.formatNumber(points)
          + "; Boss Slayer Points: " + PNumber.formatNumber(bossPoints));
      player.getGameEncoder().sendMessage("Total Slayer Tasks: " + PNumber.formatNumber(totalTasks)
          + "; Total Boss Slayer Tasks: " + PNumber.formatNumber(totalBossTasks));
      player.getGameEncoder().sendMessage("Brimstone keys: " + brimstoneKeys);
      sendTask();
    } else if (name.equals("slayer_reset_task")) {
      task.cancel();
    } else if (name.equals("slayer_reset_wilderness_task")) {
      wildernessTask.cancel();
    }
    return null;
  }

  @Override
  public int getCurrency(String identifier) {
    if (identifier.equals("slayer points")) {
      return points;
    }
    if (identifier.equals("boss slayer points")) {
      return bossPoints;
    }
    return 0;
  }

  @Override
  public void changeCurrency(String identifier, int amount) {
    if (identifier.equals("slayer points")) {
      points += amount;
    } else if (identifier.equals("boss slayer points")) {
      bossPoints += amount;
    }
  }

  @Override
  public void npcKilled(Npc npc) {
    taskKillCheck(task, npc);
    taskKillCheck(wildernessTask, npc);
    taskKillCheck(bossTask, npc);
  }

  @Override
  public boolean widgetHook(int index, int widgetId, int childId, int slot, int itemId) {
    var isEquipment = widgetId == WidgetId.EQUIPMENT || widgetId == WidgetId.EQUIPMENT_BONUSES;
    if (widgetId == WidgetId.INVENTORY || isEquipment) {
      switch (itemId) {
        case ItemId.SLAYER_HELMET:
        case ItemId.SLAYER_HELMET_I:
        case ItemId.BLACK_SLAYER_HELMET:
        case ItemId.BLACK_SLAYER_HELMET_I:
        case ItemId.GREEN_SLAYER_HELMET:
        case ItemId.GREEN_SLAYER_HELMET_I:
        case ItemId.RED_SLAYER_HELMET:
        case ItemId.RED_SLAYER_HELMET_I:
        case ItemId.PURPLE_SLAYER_HELMET:
        case ItemId.PURPLE_SLAYER_HELMET_I:
        case ItemId.TURQUOISE_SLAYER_HELMET:
        case ItemId.TURQUOISE_SLAYER_HELMET_I:
        case ItemId.HYDRA_SLAYER_HELMET:
        case ItemId.HYDRA_SLAYER_HELMET_I:
          if (isEquipment && index == 3 || !isEquipment && index == 2) {
            sendTask();
            return true;
          }
          break;
        case ItemId.CORPOREAL_BEAST_TASKS_32301:
          if (isUnlocked(SlayerUnlock.CORPOREAL_BEAST)) {
            lock(SlayerUnlock.CORPOREAL_BEAST);
            player.getGameEncoder()
                .sendMessage("You can no longer be assigned Corporeal Beast boss tasks.");
          } else {
            unlock(SlayerUnlock.CORPOREAL_BEAST);
            player.getGameEncoder()
                .sendMessage("You can now be assigned Corporeal Beast boss tasks.");
          }
          return true;
        case ItemId.RAIDS_TASKS_32329:
          if (isUnlocked(SlayerUnlock.RAIDS)) {
            lock(SlayerUnlock.RAIDS);
            player.getGameEncoder().sendMessage("You can no longer be assigned raids boss tasks.");
          } else {
            unlock(SlayerUnlock.RAIDS);
            player.getGameEncoder().sendMessage("You can now be assigned raids boss tasks.");
          }
          return true;
        case ItemId.WILDERNESS_BOSS_TASKS_32336:
          if (isUnlocked(SlayerUnlock.WILDERNESS_BOSS)) {
            lock(SlayerUnlock.WILDERNESS_BOSS);
            player.getGameEncoder().sendMessage("You can now be assigned wilderness boss tasks.");
          } else {
            unlock(SlayerUnlock.WILDERNESS_BOSS);
            player.getGameEncoder()
                .sendMessage("You can no longer be assigned wilderness boss tasks.");
          }
          return true;
        case ItemId.SLAYER_RING_8:
        case ItemId.SLAYER_RING_7:
        case ItemId.SLAYER_RING_6:
        case ItemId.SLAYER_RING_5:
        case ItemId.SLAYER_RING_4:
        case ItemId.SLAYER_RING_3:
        case ItemId.SLAYER_RING_2:
        case ItemId.SLAYER_RING_1:
        case ItemId.SLAYER_RING_ETERNAL:
          if (index == 2) {
            if (isEquipment) {
              slot += 65536;
            }
            player.putAttribute("slayer_ring_slot", slot);
            openSlayerRingDialogue();
            return true;
          }
          if (isEquipment && index == 1 || !isEquipment && index == 3) {
            sendTask();
            return true;
          }
      }
    } else if (widgetId == WidgetId.SLAYER_REWARDS) {
      switch (childId) {
        case 8:
          switch (slot) {
            case 4:
              unlock(SlayerUnlock.NEED_MORE_DARKNESS);
              break;
            case 5:
              unlock(SlayerUnlock.MALEVOLENT_MASQUERADE);
              break;
            case 7:
              unlock(SlayerUnlock.BROADER_FLETCHING);
              break;
            case 8:
              unlock(SlayerUnlock.ANKOU_VERY_MUCH);
              break;
            case 10:
              unlock(SlayerUnlock.FIRE_AND_DARKNESS);
              break;
            case 11:
              unlock(SlayerUnlock.PEDAL_TO_THE_METALS);
              break;
            case 13:
              unlock(SlayerUnlock.AUGMENT_MY_ABBIES);
              break;
            case 14:
              unlock(SlayerUnlock.ITS_DARK_IN_HERE);
              break;
            case 15:
              unlock(SlayerUnlock.GREATER_CHALLENGE);
              break;
            case 16:
              unlock(SlayerUnlock.I_HOPE_YOU_MITH_ME);
              break;
            case 17:
              unlock(SlayerUnlock.WATCH_THE_BIRDIE);
              break;
            case 18:
              unlock(SlayerUnlock.HOT_STUFF);
              break;
            case 19:
              unlock(SlayerUnlock.LIKE_A_BOSS);
              break;
            case 20:
              unlock(SlayerUnlock.BLEED_ME_DRY);
              break;
            case 21:
              unlock(SlayerUnlock.SMELL_YA_LATER);
              break;
            case 22:
              unlock(SlayerUnlock.BIRDS_OF_A_FEATHER);
              break;
            case 23:
              unlock(SlayerUnlock.I_REALLY_MITH_YOU);
              break;
            case 24:
              unlock(SlayerUnlock.HORRORIFIC);
              break;
            case 25:
              unlock(SlayerUnlock.TO_DUST_YOU_SHALL_RETURN);
              break;
            case 26:
              unlock(SlayerUnlock.WYVERNOTHER_ONE);
              break;
            case 27:
              unlock(SlayerUnlock.GET_SMASHED);
              break;
            case 28:
              unlock(SlayerUnlock.NECHS_PLEASE);
              break;
            case 29:
              unlock(SlayerUnlock.KRACK_ON);
              break;
            case 30:
              unlock(SlayerUnlock.REPTILE_GOT_RIPPED);
              break;
            case 31:
              unlock(SlayerUnlock.KING_BLACK_BONNET);
              break;
            case 32:
              unlock(SlayerUnlock.KALPHITE_KHAT);
              break;
            case 33:
              unlock(SlayerUnlock.UNHOLY_HELMET);
              break;
            case 34:
              unlock(SlayerUnlock.SEEING_RED);
              break;
            case 35:
              unlock(SlayerUnlock.BIGGER_BADDER);
              break;
            case 37:
              unlock(SlayerUnlock.DULY_NOTED);
              break;
            case 38:
              unlock(SlayerUnlock.DARK_MANTLE);
              break;
            case 39:
              unlock(SlayerUnlock.WYVER_NOTHER_TWO);
              break;
            case 40:
              unlock(SlayerUnlock.ADAMIND_SOME_MORE);
              break;
            case 41:
              unlock(SlayerUnlock.RUUUUUNE);
              break;
            case 42:
              unlock(SlayerUnlock.UNDEAD_HEAD);
              break;
            case 44:
              unlock(SlayerUnlock.DOUBLE_TROUBLE);
              break;
            case 45:
              unlock(SlayerUnlock.USE_MORE_HEAD);
              break;
            case 46:
              cancelTask();
              break;
            case 47:
              blockTask();
              break;
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
              unblockTask(slot - 48);
              break;
          }
          break;
        case 23:
          if (itemId == ItemId.BROAD_BOLTS) {
            if (index == 1) {
              buy(new Item(itemId, 250), 35);
            } else if (index == 2) {
              buy(new Item(itemId, 1250), 175);
            } else if (index == 3) {
              buy(new Item(itemId, 2500), 350);
            }
          } else if (itemId == ItemId.RUNE_POUCH) {
            if (index == 1) {
              buy(new Item(itemId, 1), 1250);
            }
          } else {
            player.getGameEncoder().sendMessage("This item can't be purchased.");
          }
          break;
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean widgetOnWidgetHook(int useWidgetId, int useChildId, int onWidgetId, int onChildId,
      int useSlot, int useItemId, int onSlot, int onItemId) {
    if (useWidgetId == WidgetId.INVENTORY && onWidgetId == WidgetId.INVENTORY) {
      for (var slayerHelmet : ColoredSlayerHelmet.values()) {
        if (!UseWidgetDecoder.used(useItemId, onItemId, slayerHelmet.getFromHelmetId(),
            slayerHelmet.getFromAttachmentId())) {
          continue;
        }
        if (!isUnlocked(slayerHelmet.getUnlock())) {
          player.getGameEncoder().sendMessage("You need to unlock this feature first.");
          return true;
        }
        player.getInventory().deleteItem(useItemId, 1, useSlot);
        player.getInventory().deleteItem(onItemId, 1, onSlot);
        player.getInventory().addItem(slayerHelmet.getToHelmetId(), 1, onSlot);
        return true;
      }
      var blackMaskId = UseWidgetDecoder.getMatch(useItemId, onItemId, ItemId.BLACK_MASK,
          ItemId.BLACK_MASK_1, ItemId.BLACK_MASK_2, ItemId.BLACK_MASK_3, ItemId.BLACK_MASK_4,
          ItemId.BLACK_MASK_5, ItemId.BLACK_MASK_6, ItemId.BLACK_MASK_7, ItemId.BLACK_MASK_8,
          ItemId.BLACK_MASK_9, ItemId.BLACK_MASK_10);
      if (blackMaskId != -1) {
        if (!isUnlocked(SlayerUnlock.MALEVOLENT_MASQUERADE)) {
          player.getGameEncoder().sendMessage("You need to unlock this feature first.");
          return true;
        }
        if (!player.getInventory().hasItem(ItemId.EARMUFFS)
            || !player.getInventory().hasItem(ItemId.FACEMASK)
            || !player.getInventory().hasItem(ItemId.NOSE_PEG)
            || !player.getInventory().hasItem(ItemId.SPINY_HELMET)
            || !player.getInventory().hasItem(ItemId.ENCHANTED_GEM)) {
          player.getGameEncoder()
              .sendMessage("You don't have all the required items to put these together.");
          return true;
        }
        player.getInventory().deleteItem(blackMaskId);
        player.getInventory().deleteItem(ItemId.EARMUFFS);
        player.getInventory().deleteItem(ItemId.FACEMASK);
        player.getInventory().deleteItem(ItemId.NOSE_PEG);
        player.getInventory().deleteItem(ItemId.SPINY_HELMET);
        player.getInventory().deleteItem(ItemId.ENCHANTED_GEM);
        player.getInventory().addItem(ItemId.SLAYER_HELMET, 1, onSlot);
        return true;
      }
      var blackMaskIId =
          UseWidgetDecoder.getMatch(useItemId, onItemId, ItemId.BLACK_MASK_I, ItemId.BLACK_MASK_1_I,
              ItemId.BLACK_MASK_2_I, ItemId.BLACK_MASK_3_I, ItemId.BLACK_MASK_4_I,
              ItemId.BLACK_MASK_5_I, ItemId.BLACK_MASK_6_I, ItemId.BLACK_MASK_7_I,
              ItemId.BLACK_MASK_8_I, ItemId.BLACK_MASK_9_I, ItemId.BLACK_MASK_10_I);
      if (blackMaskIId != -1) {
        if (!isUnlocked(SlayerUnlock.MALEVOLENT_MASQUERADE)) {
          player.getGameEncoder().sendMessage("You need to unlock this feature first.");
          return true;
        }
        if (!player.getInventory().hasItem(ItemId.EARMUFFS)
            || !player.getInventory().hasItem(ItemId.FACEMASK)
            || !player.getInventory().hasItem(ItemId.NOSE_PEG)
            || !player.getInventory().hasItem(ItemId.SPINY_HELMET)
            || !player.getInventory().hasItem(ItemId.ENCHANTED_GEM)) {
          player.getGameEncoder()
              .sendMessage("You don't have all the required items to put these together.");
          return true;
        }
        player.getInventory().deleteItem(blackMaskIId);
        player.getInventory().deleteItem(ItemId.EARMUFFS);
        player.getInventory().deleteItem(ItemId.FACEMASK);
        player.getInventory().deleteItem(ItemId.NOSE_PEG);
        player.getInventory().deleteItem(ItemId.SPINY_HELMET);
        player.getInventory().deleteItem(ItemId.ENCHANTED_GEM);
        player.getInventory().addItem(ItemId.SLAYER_HELMET_I, 1, onSlot);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean mapObjectOptionHook(int index, MapObject mapObject) {
    switch (mapObject.getId()) {
      case 172: // Closed chest: crystal chest
        if (!player.getInventory().hasItem(ItemId.BRIMSTONE_KEY)) {
          return false;
        }
        player.getInventory().deleteItem(ItemId.BRIMSTONE_KEY);
        var item = RandomItem.getItem(BRIMSTONE_CHEST_ITEMS);
        player.getInventory().addOrDropItem(item.getNotedId(), item.getAmount());
        player.getGameEncoder().sendMessage("You find some treasure in the chest!");
        brimstoneKeys++;
        return true;
      case 535: // crevice
        if (!Settings.getInstance().isSpawn() && !isAnyTask(NpcId.THERMONUCLEAR_SMOKE_DEVIL_301,
            NpcDef.getName(NpcId.THERMONUCLEAR_SMOKE_DEVIL_301))) {
          player.getGameEncoder().sendMessage("You need an appropriate task to enter.");
          return true;
        }
        player.openDialogue("bossinstance", 5);
        return true;
      case 23104: // iron winch
        if (!Settings.getInstance().isSpawn()
            && !isAnyTask(NpcId.CERBERUS_318, NpcDef.getName(NpcId.CERBERUS_318))) {
          player.getGameEncoder().sendMessage("You need an appropriate task to enter.");
          return true;
        }
        if (mapObject.getX() == 1291 && mapObject.getY() == 1254) {
          player.getMovement().teleport(1240, 1227);
        } else if (mapObject.getX() == 1328 && mapObject.getY() == 1254) {
          player.getMovement().teleport(1368, 1227);
        } else if (mapObject.getX() == 1307 && mapObject.getY() == 1269) {
          player.openDialogue("bossinstance", 4);
        }
        return true;
      case 31669: // the cloister bell
        if (!Settings.getInstance().isSpawn()
            && !isAnyTask(NpcId.DUSK_248, NpcDef.getName(NpcId.DUSK_248))) {
          player.getGameEncoder().sendMessage("You need an appropriate task to do this.");
          return true;
        }
        if (player.getController().getNpc(NpcId.DUSK_248) != null
            || player.getController().getNpc(NpcId.DUSK_248_7882) != null
            || player.getController().getNpc(NpcId.DUSK_328_7888) != null
            || player.getController().getNpc(NpcId.DAWN_228) != null
            || player.getController().getNpc(NpcId.DAWN_228_7885) != null
            || player.getController().getNpc(NpcId.DUSK_328_7889) != null) {
          player.getGameEncoder().sendMessage("Nothing interesting happens.");
          return true;
        }
        player.getGameEncoder().setVarp(1667, 3);
        player.getGameEncoder().sendMapObjectAnimation(mapObject, 7748);
        var dusk = new Npc(player.getController(), NpcId.DUSK_248, new Tile(1685, 4573));
        dusk.setLargeVisibility();
        var dawn = new Npc(player.getController(), NpcId.DAWN_228, new Tile(1705, 4573));
        dawn.setLargeVisibility();
        return true;
      case 31681: // roof entrance
        if (!Settings.getInstance().isSpawn() && !isUnlocked(SlayerUnlock.GROTESQUE_GUARDIANS)) {
          if (!player.getInventory().hasItem(ItemId.BRITTLE_KEY)) {
            player.getGameEncoder().sendMessage("You need a brittle key to unlock this.");
            return true;
          }
          player.getInventory().deleteItem(ItemId.BRITTLE_KEY);
          unlock(SlayerUnlock.GROTESQUE_GUARDIANS);
          return true;
        }
        if (!Settings.getInstance().isSpawn()
            && !isAnyTask(NpcId.DUSK_248, NpcDef.getName(NpcId.DUSK_248))) {
          player.getGameEncoder().sendMessage("You need an appropriate task to enter.");
          return true;
        }
        player.setController(new BossInstancePC());
        player.getController().instance();
        player.getMovement().teleport(1696, 4574);
        player.getController().getVariable("boss_instance_grotesque_guardians");
        return true;
    }
    return false;
  }

  @Override
  public boolean npcOptionHook(int index, Npc npc) {
    switch (npc.getId()) {
      case NpcId.NIEVE:
        if (index == 0) {
          player.openDialogue(new MasterMenuDialogue(player, this));
        } else if (index == 2) {
          getAssignment();
        } else if (index == 3) {
          player.openShop("slayer");
        } else if (index == 4) {
          player.openDialogue(new RewardsDialogue(player, this));
        }
        return true;
      case NpcId.KRYSTILIA:
        if (index == 0) {
          player.openDialogue(new WildernessMasterMenuDialogue(player, this));
        } else if (index == 2) {
          getAssignment(SlayerMaster.WILDERNESS_MASTER);
        } else if (index == 3) {
          player.openShop("slayer");
        } else if (index == 4) {
          openRewards();
        }
        return true;
    }
    return false;
  }

  public void getAssignment() {
    if (!task.isComplete()) {
      player.getGameEncoder().sendMessage("You already have a task.");
      return;
    }
    for (int i = SlayerMaster.MASTERS.length - 1; i >= 0; i--) {
      SlayerMaster master = SlayerMaster.get(SlayerMaster.MASTERS[i]);
      if (player.getSkills().getCombatLevel() >= master.getCombatLevel()
          && player.getController().getLevelForXP(Skills.SLAYER) >= master.getSlayerLevel()
          && master.getTasks() != null) {
        getAssignment(SlayerMaster.MASTERS[i]);
        return;
      }
    }
    player.getGameEncoder().sendMessage("Couldn't find a suitable Slayer master.");

  }

  public void getAssignment(String name) {
    var master = SlayerMaster.get(name);
    if (master == null) {
      return;
    }
    var isWilderness = name.equals(SlayerMaster.WILDERNESS_MASTER);
    var isBoss = name.equals(SlayerMaster.BOSS_MASTER);
    var assignedTask = task;
    var assignedSlayerTask = task.getSlayerTask();
    if (isWilderness) {
      assignedTask = wildernessTask;
    } else if (isBoss) {
      assignedTask = bossTask;
    }
    if (!assignedTask.isComplete()
        && (!isBoss || PTime.getYearMonthDay().equals(assignedTask.getDate()))) {
      player.getGameEncoder().sendMessage("You already have a task.");
      return;
    }
    if (player.getSkills().getCombatLevel() < master.getCombatLevel()) {
      player.getGameEncoder().sendMessage(
          "You need a combat level of " + master.getCombatLevel() + " to use this Slayer master.");
      return;
    }
    if (player.getController().getLevelForXP(Skills.SLAYER) < master.getSlayerLevel()) {
      player.getGameEncoder().sendMessage(
          "You need a Slayer level of " + master.getSlayerLevel() + " to get one of these tasks.");
      return;
    }
    if (isBoss && !Settings.getInstance().isLocal() && !isUnlocked(SlayerUnlock.LIKE_A_BOSS)) {
      player.getGameEncoder().sendMessage("You need to unlock this feature first.");
      return;
    }
    SlayerTask selectedTask = null;
    if (assignedSlayerTask != null && player.getEquipment().wearingAccomplishmentCape(Skills.SLAYER)
        && PRandom.randomE(10) == 0) {
      selectedTask = assignedSlayerTask;
    }
    for (var i = 0; i < 128 && selectedTask == null; i++) {
      var aTask = PRandom.listRandom(master.getTasks());
      if (player.getController().getLevelForXP(Skills.SLAYER) < aTask.getSlayerLevel()) {
        continue;
      }
      if (!isWilderness && isBlockedTask(aTask.getIdentifier())) {
        continue;
      }
      if (aTask.getIdentifier() == SlayerTaskIdentifier.RED_DRAGON
          && !isUnlocked(SlayerUnlock.SEEING_RED)) {
        continue;
      }
      if (aTask.getIdentifier() == SlayerTaskIdentifier.MITHRIL_DRAGON
          && !isUnlocked(SlayerUnlock.I_HOPE_YOU_MITH_ME)) {
        continue;
      }
      if (aTask.getIdentifier() == SlayerTaskIdentifier.AVIANSIE
          && !isUnlocked(SlayerUnlock.WATCH_THE_BIRDIE)) {
        continue;
      }
      if (aTask.getIdentifier() == SlayerTaskIdentifier.TZHAAR
          && !isUnlocked(SlayerUnlock.HOT_STUFF)) {
        continue;
      }
      if (aTask.getIdentifier() == SlayerTaskIdentifier.LIZARDMAN
          && !isUnlocked(SlayerUnlock.REPTILE_GOT_RIPPED)) {
        continue;
      }
      if (aTask.getIdentifier() == SlayerTaskIdentifier.FOSSIL_ISLAND_WYVERN
          && isUnlocked(SlayerUnlock.STOP_THE_WYVERN)) {
        continue;
      }
      if (aTask.getName().equals("Corporeal Beast") && !isUnlocked(SlayerUnlock.CORPOREAL_BEAST)) {
        continue;
      }
      if (aTask.getName().equals("Raids Boss") && !isUnlocked(SlayerUnlock.RAIDS)) {
        continue;
      }
      if (aTask.getName().equals("Grotesque Guardians")
          && !isUnlocked(SlayerUnlock.GROTESQUE_GUARDIANS)) {
        continue;
      }
      if (isBoss && aTask.isWilderness() && isUnlocked(SlayerUnlock.WILDERNESS_BOSS)) {
        continue;
      }
      if (assignedSlayerTask != null
          && (assignedSlayerTask.getIdentifier() != null || aTask.getIdentifier() != null)
          && assignedSlayerTask.getIdentifier() == aTask.getIdentifier()) {
        continue;
      }
      if (assignedSlayerTask != null
          && (assignedSlayerTask.getName() != null || aTask.getName() != null)
          && assignedSlayerTask.getName().equals(aTask.getName())) {
        continue;
      }
      if (isBoss && assignedSlayerTask != null && assignedSlayerTask.isWilderness()
          && aTask.isWilderness()) {
        continue;
      }
      selectedTask = aTask;
      break;
    }
    if (selectedTask == null) {
      player.getGameEncoder().sendMessage("Failed to assign a task!");
      return;
    }
    int quantity = selectedTask.getRandomQuantity();
    if (selectedTask.getIdentifier() == SlayerTaskIdentifier.DARK_BEAST
        && isUnlocked(SlayerUnlock.NEED_MORE_DARKNESS)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.ANKOU
            && isUnlocked(SlayerUnlock.ANKOU_VERY_MUCH)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.BLACK_DRAGON
            && isUnlocked(SlayerUnlock.FIRE_AND_DARKNESS)
        || (selectedTask.getIdentifier() == SlayerTaskIdentifier.BRONZE_DRAGON
            || selectedTask.getIdentifier() == SlayerTaskIdentifier.IRON_DRAGON
            || selectedTask.getIdentifier() == SlayerTaskIdentifier.STEEL_DRAGON)
            && isUnlocked(SlayerUnlock.PEDAL_TO_THE_METALS)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.ABYSSAL_DEMON
            && isUnlocked(SlayerUnlock.AUGMENT_MY_ABBIES)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.BLACK_DEMON
            && isUnlocked(SlayerUnlock.ITS_DARK_IN_HERE)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.GREATER_DEMON
            && isUnlocked(SlayerUnlock.GREATER_CHALLENGE)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.BLOODVELD
            && isUnlocked(SlayerUnlock.BLEED_ME_DRY)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.ABERRANT_SPECTRE
            && isUnlocked(SlayerUnlock.SMELL_YA_LATER)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.AVIANSIE
            && isUnlocked(SlayerUnlock.BIRDS_OF_A_FEATHER)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.MITHRIL_DRAGON
            && isUnlocked(SlayerUnlock.I_REALLY_MITH_YOU)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.CAVE_HORROR
            && isUnlocked(SlayerUnlock.HORRORIFIC)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.DUST_DEVIL
            && isUnlocked(SlayerUnlock.TO_DUST_YOU_SHALL_RETURN)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.SKELETAL_WYVERN
            && isUnlocked(SlayerUnlock.WYVERNOTHER_ONE)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.FOSSIL_ISLAND_WYVERN
            && isUnlocked(SlayerUnlock.WYVER_NOTHER_TWO)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.GARGOYLE
            && isUnlocked(SlayerUnlock.GET_SMASHED)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.NECHRYAEL
            && isUnlocked(SlayerUnlock.NECHS_PLEASE)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.CAVE_KRAKEN
            && isUnlocked(SlayerUnlock.KRACK_ON)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.ADAMANT_DRAGON
            && isUnlocked(SlayerUnlock.ADAMIND_SOME_MORE)
        || selectedTask.getIdentifier() == SlayerTaskIdentifier.RUNE_DRAGON
            && isUnlocked(SlayerUnlock.RUUUUUNE)) {
      quantity *= 1.5;
    }
    if (!isWilderness && !isBoss && !name.equalsIgnoreCase(assignedTask.getMaster())) {
      consecutiveTasks = 0;
    }
    assignedTask = new AssignedSlayerTask(master.getName(), selectedTask.getIdentifier(),
        selectedTask.getName(), quantity, PTime.getYearMonthDay());
    if (isWilderness) {
      wildernessTask = assignedTask;
    } else if (isBoss) {
      bossTask = assignedTask;
    } else {
      task = assignedTask;
    }
    var plural =
        assignedTask.getQuantity() > 1
            ? (assignedTask.getName().endsWith("ss") ? "es"
                : (assignedTask.getName().endsWith("s") ? "'" : "s"))
            : "";
    if (isWilderness) {
      player.getGameEncoder().sendMessage("Your new wilderness task is to kill " + quantity + " "
          + selectedTask.getName() + plural + ".");
      player.getGameEncoder().sendMessage(
          "Carrying a mysterious emblem during your task will reward you with blood money for every kill.");
    } else {
      player.getGameEncoder().sendMessage(
          "Your new task is to kill " + quantity + " " + selectedTask.getName() + plural + ".");
    }
    if (selectedTask.getLocation() != null) {
      player.getGameEncoder()
          .sendMessage("They are located at " + selectedTask.getLocation() + ".");
    }
    AchievementDiary.slayerAssignmentUpdate(player, master, selectedTask, quantity);
  }

  private void taskKillCheck(AssignedSlayerTask assignedTask, Npc npc) {
    if (!countsTowardTaskQuantity(assignedTask, npc.getId())) {
      return;
    }
    if (assignedTask.isComplete() || !isTask(assignedTask, npc.getId(), npc.getName())) {
      return;
    }
    var experience = npc.getMaxHitpoints();
    if (npc.getDef().getSlayerXP() > 0) {
      experience = npc.getDef().getSlayerXP();
    }
    player.getSkills().addXp(Skills.SLAYER, experience);
    if (npc.getDef().getSuperiorSlayerId() != -1 && isUnlocked(SlayerUnlock.BIGGER_BADDER)
        && PRandom.randomE(100) == 0) {
      player.getGameEncoder().sendMessage("<col=ff0000>A superior foe has appeared...");
      var superiorNpc = new Npc(player.getController(), npc.getDef().getSuperiorSlayerId(), npc);
      superiorNpc.getCombat().setTarget(player);
    }
    int brimstoneKeyChance;
    if (npc.getDef().getCombatLevel() >= 100) {
      brimstoneKeyChance = (int) (-0.2 * Math.min(npc.getDef().getCombatLevel(), 350) + 120);
    } else {
      brimstoneKeyChance = (int) (0.2 * Math.pow(npc.getDef().getCombatLevel() - 100, 2) + 100);
    }
    var brimstoneKeyPercent = 100.0 / brimstoneKeyChance;
    boolean hasRoWICharge = player.getCharges().hasRoWICharge(0);
    if (brimstoneKeyChance > 1 && assignedTask != bossTask && PRandom
        .inRange(player.getCombat().getDropRate(ItemId.BRIMSTONE_KEY, brimstoneKeyPercent))) {
      Tile tile = npc;
      if (npc.getDef().getDropUnderKiller()) {
        tile = player;
      }

      if (hasRoWICharge && player.getInventory().canAddItem(ItemId.BRIMSTONE_KEY, 1)) {
        player.getInventory().addItem(ItemId.BRIMSTONE_KEY, 1);
      } else {
        player.getController().addMapItem(new Item(ItemId.BRIMSTONE_KEY), tile, player);
      }
    }
    assignedTask.decreaseQuantity();
    if (!assignedTask.isComplete()) {
      return;
    }
    var tasksCompleted = 0;
    if (assignedTask == task) {
      totalTasks++;
      tasksCompleted = ++consecutiveTasks;
    } else if (assignedTask == wildernessTask) {
      totalTasks++;
      tasksCompleted = ++consecutiveWildernessTasks;
    } else if (assignedTask == bossTask) {
      tasksCompleted = ++totalBossTasks;
    }
    var slayerMaster = SlayerMaster.get(assignedTask.getMaster());
    var slayerTask = assignedTask.getSlayerTask();
    var rewardPoints = slayerMaster.getPoints();
    if (player.isPremiumMember()) {
      rewardPoints *= 1.25;
    }
    if (slayerTask.isWilderness() && !Settings.getInstance().isSpawn()) {
      if (assignedTask == wildernessTask) {
        if (wildernessTaskEmblemUpgrade) {
          player.getCombat().getBountyHunter().upgradeEmblem();
          player.getInventory().addOrDropItem(ItemId.BLOOD_MONEY, 10_000);
        }
        wildernessTaskEmblemUpgrade = true;
      } else if (assignedTask == bossTask) {
        rewardPoints *= 1.25;
        player.getCombat().getBountyHunter().upgradeEmblem();
        player.getInventory().addOrDropItem(ItemId.BLOOD_MONEY, 20_000);
      }
    }
    if (tasksCompleted % 1000 == 0) {
      rewardPoints *= 50;
    } else if (tasksCompleted % 250 == 0) {
      rewardPoints *= 35;
    } else if (tasksCompleted % 100 == 0) {
      rewardPoints *= 25;
    } else if (tasksCompleted % 50 == 0) {
      rewardPoints *= 15;
    } else if (tasksCompleted % 25 == 0) {
      rewardPoints *= 10;
    } else if (tasksCompleted % 10 == 0) {
      rewardPoints *= 5;
    } else if (tasksCompleted % 5 == 0) {
      rewardPoints *= 2.5;
    }
    if (assignedTask == bossTask) {
      bossPoints += rewardPoints;
    } else {
      points += rewardPoints;
    }
    if (assignedTask == bossTask) {
      player.getGameEncoder().sendMessage("<col=8F4808>You've completed " + tasksCompleted
          + " boss tasks and received " + rewardPoints + " points; return to a Slayer master.");
    } else if (rewardPoints > 0) {
      player.getGameEncoder().sendMessage("<col=8F4808>You've completed " + tasksCompleted
          + " tasks in a row and received " + rewardPoints + " points; return to a Slayer master.");
    } else {
      player.getGameEncoder().sendMessage(
          "<col=8F4808>You've completed " + tasksCompleted + " tasks; return to a Slayer master.");
    }
    AchievementDiary.slayerAssignmentCompleteUpdate(player, slayerMaster, slayerTask);
  }

  private boolean countsTowardTaskQuantity(AssignedSlayerTask assignedTask, int id) {
    if (id == NpcId.RESPIRATORY_SYSTEM) {
      return false;
    }
    if ((id == NpcId.DAWN_228 || id == NpcId.DAWN_228_7885)
        && !isUnlocked(SlayerUnlock.DOUBLE_TROUBLE)) {
      return false;
    }
    if (assignedTask == bossTask) {
      if (id == NpcId.WINGMAN_SKREE_143 || id == NpcId.FLOCKLEADER_GEERIN_149
          || id == NpcId.FLIGHT_KILISA_159) {
        return false;
      } else if (id == NpcId.STARLIGHT_149 || id == NpcId.GROWLER_139 || id == NpcId.BREE_146) {
        return false;
      } else if (id == NpcId.SERGEANT_STRONGSTACK_141 || id == NpcId.SERGEANT_STEELWILL_142
          || id == NpcId.SERGEANT_GRIMSPIKE_142) {
        return false;
      } else if (id == NpcId.TSTANON_KARLAK_145 || id == NpcId.ZAKLN_GRITCH_142
          || id == NpcId.BALFRUG_KREEYATH_151) {
        return false;
      } else if (id == NpcId.GREAT_OLM_RIGHT_CLAW || id == NpcId.GREAT_OLM_LEFT_CLAW
          || id == NpcId.GREAT_OLM_RIGHT_CLAW_549 || id == NpcId.GREAT_OLM_LEFT_CLAW_750) {
        return false;
      }
    }
    return true;
  }

  private boolean isAnyTask(int id, String name) {
    return isTask(task, id, name) || isTask(wildernessTask, id, name) || isTask(bossTask, id, name);
  }

  private boolean isTask(AssignedSlayerTask assignedTask, int id, String name) {
    if (assignedTask.isComplete()) {
      return false;
    }
    if (assignedTask == wildernessTask && !player.getController().inWilderness()) {
      return false;
    }
    return assignedTask.containsId(id)
        || name != null && name.matches(".*(?i)(" + assignedTask.getName() + ")\\b.*");
  }

  public void sendTask() {
    if (task.isComplete() && wildernessTask.isComplete()) {
      player.getGameEncoder().sendMessage("You need something new to hunt.");
      return;
    }
    if (!task.isComplete()) {
      player.getGameEncoder().sendMessage("You're assigned to kill " + task.getName() + "s; only "
          + task.getQuantity() + " more to go.");
      if (task.getSlayerTask().getLocation() != null) {
        player.getGameEncoder()
            .sendMessage("They are located at " + task.getSlayerTask().getLocation() + ".");
      }
    }
    if (!wildernessTask.isComplete()) {
      player.getGameEncoder().sendMessage("You're assigned to kill " + wildernessTask.getName()
          + "s in the wilderness; only " + wildernessTask.getQuantity() + " more to go.");
      if (wildernessTask.getSlayerTask().getLocation() != null) {
        player.getGameEncoder().sendMessage(
            "They are located at " + wildernessTask.getSlayerTask().getLocation() + ".");
      }
    }
  }

  public boolean isUnlocked(SlayerUnlock unlock) {
    return unlocks != null && unlocks.contains(unlock);
  }

  public void unlock(SlayerUnlock unlock) {
    if (isUnlocked(unlock)) {
      lock(unlock);
      return;
    }
    var cost = unlock.getPrice();
    if (points < cost) {
      player.getGameEncoder().sendMessage("You need " + cost + " points to unlock this.");
      return;
    }
    if (unlocks == null) {
      unlocks = new ArrayList<>();
    }
    if (!unlocks.contains(unlock)) {
      unlocks.add(unlock);
    }
    points -= cost;
    sendRewardsVarps();
  }

  public void lock(SlayerUnlock unlock) {
    if (unlocks == null || !unlocks.contains(unlock)) {
      return;
    }
    unlocks.remove(unlock);
    sendRewardsVarps();
  }

  public void openRewards() {
    player.getWidgetManager().sendInteractiveOverlay(426);
    player.getGameEncoder().sendWidgetSettings(426, 8, 0, 100, 2);
    player.getGameEncoder().sendWidgetSettings(426, 12, 0, 10, 2);
    player.getGameEncoder().sendWidgetSettings(426, 23, 0, 10, 1086);
    sendRewardsVarps();
  }

  private void sendVarps() {
    player.getGameEncoder().setVarp(VarpId.SLAYER_QUANTITY, task.getQuantity());
    player.getGameEncoder().setVarp(VarpId.SLAYER_TASK_IDENTIFIER,
        !task.isComplete() ? task.getIdentifier() != null ? task.getIdentifier().ordinal() : 156
            : 0);
    player.getGameEncoder().setVarbit(VarbitId.SLAYER_GROTESQUE_GUARDIANS_DOOR,
        isUnlocked(SlayerUnlock.GROTESQUE_GUARDIANS) ? 1 : 0);
  }

  public void sendRewardsVarps() {
    sendVarps();
    player.getGameEncoder().setVarbit(VarbitId.SLAYER_POINTS, points);
    if (unlocks != null) {
      for (SlayerUnlock unlock : unlocks) {
        if (unlock.getVarbit() == -1) {
          continue;
        }
        player.getGameEncoder().setVarbit(unlock.getVarbit(), 1);
      }
    }
    if (blockedTasks != null) {
      for (int i = 0; i < blockedTasks.size(); i++) {
        player.getGameEncoder().setVarbit(BLOCKED_TASK_VARBITS[i], blockedTasks.get(i).ordinal());
      }
    }
  }

  public void openSlayerRingDialogue() {
    if (task.isComplete()) {
      player.getGameEncoder().sendMessage("You need a task to do this.");
      return;
    }
    if (task.getSlayerTask().getTeleports() == null
        || task.getSlayerTask().getTeleports().isEmpty()) {
      player.getGameEncoder().sendMessage("There are no teleports associated with this task.");
      return;
    }
    player.openDialogue(new SlayerRingDialogue(player, this));
  }

  public boolean isBlockedTask(SlayerTaskIdentifier identifier) {
    return blockedTasks != null && blockedTasks.contains(identifier);
  }

  public void cancelTask() {
    if (task.isComplete()) {
      player.getGameEncoder().sendMessage("You need something new to hunt.");
      return;
    }
    if (points < 30) {
      player.getGameEncoder().sendMessage("You need 30 points to cancel a task.");
      return;
    }
    player.getGameEncoder().sendMessage("Your task has been cancelled.");
    task.cancel();
    points -= 30;
    sendRewardsVarps();
  }

  public void cancelWildernessTask() {
    if (wildernessTask.isComplete()) {
      player.getGameEncoder().sendMessage("You need something new to hunt.");
      return;
    }
    if (points < 30) {
      player.getGameEncoder().sendMessage("You need 30 points to cancel a wilderness task.");
      return;
    }
    player.getGameEncoder().sendMessage("Your wilderness task has been cancelled.");
    player.getGameEncoder().sendMessage("Completion of your next task won't upgrade your emblem.");
    wildernessTask.cancel();
    points -= 30;
    wildernessTaskEmblemUpgrade = false;
  }

  public void blockTask() {
    if (task.isComplete()) {
      player.getGameEncoder().sendMessage("You need something new to hunt.");
      return;
    }
    if (task.getIdentifier() == null) {
      player.getGameEncoder().sendMessage("This task can't be blocked.");
      return;
    }
    if (blockedTasks != null && blockedTasks.contains(task.getIdentifier())) {
      player.getGameEncoder().sendMessage("This task is already blocked.");
      return;
    }
    if (points < 100) {
      player.getGameEncoder().sendMessage("You need 100 points to block a task.");
      return;
    }
    if (blockedTasks != null && blockedTasks.size() >= 6) {
      player.getGameEncoder().sendMessage("You can't block any more tasks.");
      return;
    }
    if (blockedTasks == null) {
      blockedTasks = new ArrayList<>();
    }
    blockedTasks.add(task.getIdentifier());
    points -= 100;
    task.cancel();
    sendRewardsVarps();
  }

  public void unblockTask(int index) {
    if (blockedTasks == null || index >= blockedTasks.size()) {
      return;
    }
    blockedTasks.remove(index);
    if (blockedTasks.size() == 0) {
      blockedTasks = null;
    }
    sendRewardsVarps();
  }

  public void buy(Item item, int cost) {
    if (points < cost) {
      player.getGameEncoder()
          .sendMessage("You need " + PNumber.formatNumber(cost) + " points to buy this.");
      return;
    }
    if (!player.getInventory().canAddItem(item)) {
      player.getInventory().notEnoughSpace();
      return;
    }
    player.getInventory().addItem(item);
    points -= cost;
    sendRewardsVarps();
  }
}
