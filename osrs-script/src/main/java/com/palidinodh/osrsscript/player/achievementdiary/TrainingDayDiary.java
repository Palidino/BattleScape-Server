package com.palidinodh.osrsscript.player.achievementdiary;

import static com.palidinodh.osrscore.model.player.AchievementDiaryTask.Difficulty.EASY;
import java.util.Arrays;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.io.cache.NpcId;
import com.palidinodh.osrscore.io.cache.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.Shop;
import com.palidinodh.osrscore.model.item.ShopItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.AchievementDiaryTask;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.slayer.SlayerMaster;
import com.palidinodh.osrscore.model.player.slayer.SlayerTask;

public class TrainingDayDiary extends AchievementDiary {
  public TrainingDayDiary() {
    super(AchievementDiary.Name.TRAINING_DAY);
  }

  @Override
  public AchievementDiaryTask[] getTasks() {
    return Arrays.stream(TrainingDayTask.values()).map(TrainingDayTask::getTask)
        .toArray(AchievementDiaryTask[]::new);
  }

  @Override
  public void updateRegionId(Player player, int oldId, int newId) {
    if (newId == 10553 || newId == 10554 || newId == 6966) {
      addCompletedTask(player, TrainingDayTask.VISIT_CRABS);
    }
  }

  @Override
  public void addXp(Player player, int skillId, int amount) {
    int totalLevel = player.getSkills().getTotalLevel();
    if (totalLevel >= 100) {
      addCompletedTask(player, TrainingDayTask.TOTAL_LEVEL_100);
    }
    if (totalLevel >= 500) {
      addCompletedTask(player, TrainingDayTask.TOTAL_LEVEL_500);
    }
  }

  @Override
  public void levelUp(Player player, int skillId, int oldLevel, int newLevel) {
    int totalLevel = player.getSkills().getTotalLevel();
    if (totalLevel >= 100) {
      addCompletedTask(player, TrainingDayTask.TOTAL_LEVEL_100);
    }
    if (totalLevel >= 500) {
      addCompletedTask(player, TrainingDayTask.TOTAL_LEVEL_500);
    }
  }

  @Override
  public void sendInteractiveOverlay(Player player, int id) {
    if (id == WidgetId.GRAND_EXCHANGE) {
      addCompletedTask(player, TrainingDayTask.VIEW_GRAND_EXCHANGE);
    }
    if (id == WidgetId.CUSTOM_BOND_POUCH) {
      addCompletedTask(player, TrainingDayTask.VIEW_BOND_POUCH);
    }
  }

  @Override
  public void sendClanChatMessage(Player player, int clanChatUserId, String clanChatName,
      String message) {
    addCompletedTask(player, TrainingDayTask.TALK_CLAN_CHAT);
  }

  @Override
  public void buyShopItem(Player player, Shop shop, ShopItem shopItem, Item item, int price) {
    addCompletedTask(player, TrainingDayTask.BUY_SHOP_ITEM);
    if (shop.getCurrency() == ItemId.VOTE_TICKET) {
      addCompletedTask(player, TrainingDayTask.SPEND_VOTE_TICKET);
    }
  }

  @Override
  public void slayerAssignment(Player player, SlayerMaster slayerMaster, SlayerTask slayerTask,
      int amount) {
    addCompletedTask(player, TrainingDayTask.GET_SLAYER_TASK);
  }

  @Override
  public void slayerAssignmentComplete(Player player, SlayerMaster slayerMaster,
      SlayerTask slayerTask) {
    addCompletedTask(player, TrainingDayTask.COMPLETE_SLAYER_TASK);
  }

  @Override
  public void npcOption(Player player, int index, Npc npc) {
    if (npc.getId() == NpcId.LOYALTY_MANAGER) {
      addCompletedTask(player, TrainingDayTask.TALK_LOYALTY_MANAGER);
    }
  }

  @Override
  public void makeItem(Player player, int skillId, Item item, Npc npc, MapObject mapObject) {
    if (skillId == Skills.WOODCUTTING && item.getId() == ItemId.WILLOW_LOGS) {
      addCompletedTask(player, TrainingDayTask.CHOP_WILLOW);
    }
    if (skillId == Skills.FISHING && item.getId() == ItemId.RAW_SALMON) {
      addCompletedTask(player, TrainingDayTask.FISH_SALMON);
    }
    if (skillId == Skills.THIEVING && mapObject != null
        && mapObject.getName().toLowerCase().endsWith("stall")) {
      addCompletedTask(player, TrainingDayTask.STEAL_FROM_STALL);
    }
    if (skillId == Skills.FLETCHING && item.getId() == ItemId.BRONZE_CROSSBOW) {
      addCompletedTask(player, TrainingDayTask.FLETCH_BRONZE_CROSSBOW);
    }
    if (skillId == Skills.MINING && item.getId() == ItemId.COAL) {
      addCompletedTask(player, TrainingDayTask.MINE_COAL);
    }
    if (skillId == Skills.CRAFTING && item.getId() == ItemId.LEATHER) {
      addCompletedTask(player, TrainingDayTask.TAN_COWHIDE);
    }
    if (skillId == Skills.HERBLORE && item.getId() == ItemId.ATTACK_POTION_3) {
      addCompletedTask(player, TrainingDayTask.MIX_ATTACK_POTION);
    }
    if (skillId == Skills.FARMING && (item.getId() == ItemId.GRIMY_GUAM_LEAF
        || item.getId() == ItemId.GRIMY_GUAM_LEAF_NOTED)) {
      addCompletedTask(player, TrainingDayTask.GROW_GUAM);
    }
    if (skillId == Skills.CRAFTING && item.getId() == ItemId.SAPPHIRE_RING) {
      addCompletedTask(player, TrainingDayTask.CRAFT_SAPPHIRE_RING);
    }
    if (skillId == Skills.SMITHING && item.getId() == ItemId.STEEL_BAR) {
      addCompletedTask(player, TrainingDayTask.SMELT_STEEL_BAR);
    }
    if (skillId == Skills.RUNECRAFTING && item.getId() == ItemId.EARTH_RUNE) {
      addCompletedTask(player, TrainingDayTask.CRAFT_EARTH_RUNES);
    }
    if (skillId == Skills.COOKING && item.getId() == ItemId.SARDINE) {
      addCompletedTask(player, TrainingDayTask.COOK_SARDINE);
    }
  }

  @Override
  public void makeFire(Player player, Item item) {
    addCompletedTask(player, TrainingDayTask.START_FIRE);
  }

  @Override
  public void npcKilled(Player player, Npc npc) {
    if (npc.getId() >= 1689 && npc.getId() <= 1754) {
      addCompletedTask(player, TrainingDayTask.KILL_PC_MONSTER);
    }
  }

  public void addCompletedTask(Player player, TrainingDayTask task) {
    super.addCompletedTask(player, task.getTask());
  }
}


enum TrainingDayTask {
  TALK_CLAN_CHAT(new AchievementDiaryTask("Talk in a Clan Chat.", EASY)), BUY_SHOP_ITEM(
      new AchievementDiaryTask("Buy an item from a shop.", EASY)), GET_SLAYER_TASK(
          new AchievementDiaryTask("Get a Slayer task.", EASY)), VIEW_GRAND_EXCHANGE(
              new AchievementDiaryTask("View the Grand Exchange.", EASY)), VISIT_CRABS(
                  new AchievementDiaryTask("Visit rock crabs or sand crabs.",
                      EASY)), VIEW_BOND_POUCH(
                          new AchievementDiaryTask("View your bond pouch.",
                              EASY)), SPEND_VOTE_TICKET(
                                  new AchievementDiaryTask("Spend a vote ticket.",
                                      EASY)), COMPLETE_SLAYER_TASK(
                                          new AchievementDiaryTask("Complete a Slayer task.",
                                              EASY)), CHOP_WILLOW(
                                                  new AchievementDiaryTask("Chop a willow tree.",
                                                      EASY)), FISH_SALMON(
                                                          new AchievementDiaryTask("Fish a salmon.",
                                                              EASY)), STEAL_FROM_STALL(
                                                                  new AchievementDiaryTask(
                                                                      "Steal from a stall.",
                                                                      EASY)), TALK_LOYALTY_MANAGER(
                                                                          new AchievementDiaryTask(
                                                                              "Talk to the loyalty manager.",
                                                                              EASY)), KILL_PC_MONSTER(
                                                                                  new AchievementDiaryTask(
                                                                                      "Kill a monster in Pest Control.",
                                                                                      EASY)), FLETCH_BRONZE_CROSSBOW(
                                                                                          new AchievementDiaryTask(
                                                                                              "Fletch a bronze crossbow.",
                                                                                              EASY)), MINE_COAL(
                                                                                                  new AchievementDiaryTask(
                                                                                                      "Mine some coal.",
                                                                                                      EASY)), TAN_COWHIDE(
                                                                                                          new AchievementDiaryTask(
                                                                                                              "Pay a tanner to tan cowhide.",
                                                                                                              EASY)), MIX_ATTACK_POTION(
                                                                                                                  new AchievementDiaryTask(
                                                                                                                      "Mix an attack potion.",
                                                                                                                      EASY)), GROW_GUAM(
                                                                                                                          new AchievementDiaryTask(
                                                                                                                              "Grow a guam plant.",
                                                                                                                              EASY)), CRAFT_SAPPHIRE_RING(
                                                                                                                                  new AchievementDiaryTask(
                                                                                                                                      "Craft a sapphire ring.",
                                                                                                                                      EASY)), SMELT_STEEL_BAR(
                                                                                                                                          new AchievementDiaryTask(
                                                                                                                                              "Smelt a steel bar.",
                                                                                                                                              EASY)), CRAFT_EARTH_RUNES(
                                                                                                                                                  new AchievementDiaryTask(
                                                                                                                                                      "Craft some earth runes.",
                                                                                                                                                      EASY)), COOK_SARDINE(
                                                                                                                                                          new AchievementDiaryTask(
                                                                                                                                                              "Cook a sardine.",
                                                                                                                                                              EASY)), START_FIRE(
                                                                                                                                                                  new AchievementDiaryTask(
                                                                                                                                                                      "Start a fire.",
                                                                                                                                                                      EASY)), TOTAL_LEVEL_100(
                                                                                                                                                                          new AchievementDiaryTask(
                                                                                                                                                                              "Get a total level of 100.",
                                                                                                                                                                              EASY)), TOTAL_LEVEL_500(
                                                                                                                                                                                  new AchievementDiaryTask(
                                                                                                                                                                                      "Get a total level of 500.",
                                                                                                                                                                                      EASY));

  private AchievementDiaryTask task;

  private TrainingDayTask(AchievementDiaryTask task) {
    this.task = task;
  }

  public AchievementDiaryTask getTask() {
    return task;
  }
}
