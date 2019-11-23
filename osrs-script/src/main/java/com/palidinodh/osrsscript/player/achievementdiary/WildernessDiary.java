package com.palidinodh.osrsscript.player.achievementdiary;

import static com.palidinodh.osrscore.model.player.AchievementDiaryTask.Difficulty.EASY;
import static com.palidinodh.osrscore.model.player.AchievementDiaryTask.Difficulty.ELITE;
import static com.palidinodh.osrscore.model.player.AchievementDiaryTask.Difficulty.HARD;
import static com.palidinodh.osrscore.model.player.AchievementDiaryTask.Difficulty.MEDIUM;
import java.util.Arrays;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.NpcId;
import com.palidinodh.osrscore.io.cache.WidgetChild;
import com.palidinodh.osrscore.model.Entity;
import com.palidinodh.osrscore.model.Tile;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.Shop;
import com.palidinodh.osrscore.model.item.ShopItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.AchievementDiaryTask;
import com.palidinodh.osrscore.model.player.Player;

public class WildernessDiary extends AchievementDiary {
  public WildernessDiary() {
    super(AchievementDiary.Name.WILDERNESS);
  }

  @Override
  public AchievementDiaryTask[] getTasks() {
    return Arrays.stream(WildernessTask.values()).map(WildernessTask::getTask)
        .toArray(AchievementDiaryTask[]::new);
  }

  @Override
  public void animation(Player player, int id, int delay) {
    if (id == 4959 || id == 4961 || id == 4981 || id == 4971 || id == 4973 || id == 4979
        || id == 4939 || id == 4955 || id == 4957 || id == 4937 || id == 4951 || id == 4975
        || id == 4949 || id == 4943 || id == 4941 || id == 4969 || id == 4977 || id == 4965
        || id == 4967 || id == 4963 || id == 4947 || id == 5158 || id == 4953 || id == 7121) {
      if (player.getHeight() == 3 && player.within(2954, 3328, 2998, 3353)) {
        // addCompletedTask(player, WildernessTask.CAPE_EMOTE);
      }
    }
  }

  @Override
  public void teleported(Player player, Tile fromTile) {
    if (player.getRegionId() == 11316 && fromTile.getRegionId() == 12082) {
      // addCompletedTask(player, WildernessTask.ENTRANA);
    }
  }

  @Override
  public void equipItem(Player player, Item item, int slot) {
    if (item.getId() == ItemId.DWARVEN_HELMET && player.within(2962, 9699, 3061, 9852)) {
      // addCompletedTask(player, WildernessTask.DWARVEN_HELMET);
    }
  }

  @Override
  public void buyShopItem(Player player, Shop shop, ShopItem shopItem, Item item, int price) {
    if (item.getId() == ItemId.WHITE_2H_SWORD && item.getAmount() > 0
        && shopItem.getMonsterKillCount() > 0) {
      // addCompletedTask(player, WildernessTask.WHITE_2H);
    }
  }

  @Override
  public void makeItem(Player player, int skillId, Item item, Npc npc, MapObject mapObject) {
    if (item.getId() == ItemId.BUCKET_OF_WATER && player.getRegionId() == 11828) {
      // addCompletedTask(player, WildernessTask.FILL_BUCKET);
    } else if (item.getId() == ItemId.MIND_TIARA) {
      // addCompletedTask(player, WildernessTask.MIND_TIARA);
    } else if (item.getId() == ItemId.BLURITE_LIMBS) {
      // addCompletedTask(player, WildernessTask.BLURITE_LIMBS);
    } else if (item.getId() == ItemId.BULLSEYE_LANTERN_4550
        && player.within(2925, 3205, 2941, 3215)) {
      // addCompletedTask(player, WildernessTask.BULLSEYE_LANTERN);
    } else if (npc != null
        && (npc.getId() == NpcId.GUARD_21_3269 || npc.getId() == NpcId.GUARD_22_3270
            || npc.getId() == NpcId.GUARD_19 || npc.getId() == NpcId.GUARD_22_3272)
        && (player.getRegionId() == 11828 || player.getRegionId() == 12084)) {
      // addCompletedTask(player, WildernessTask.PICKPOCKET_GUARD);
    } else if (item.getId() == ItemId.MITHRIL_ORE && player.getRegionId() == 12346
        || player.getRegionId() == 12093) {
      // addCompletedTask(player, WildernessTask.MITHRIL_ORE);
    } else if (item.getId() == ItemId.IRON_ORE && player.getRegionId() == 12343) {
      // addCompletedTask(player, WildernessTask.IRON_ORE);
    } else if (item.getId() == ItemId.AIR_RUNE && item.getAmount() >= 252) {
    } else if (item.getId() == ItemId.MAGIC_ROOTS && item.getAmount() >= 3) {
    } else if (item.getId() == ItemId.SARADOMIN_BREW_3 && player.within(3009, 3355, 3019, 3358)) {
    }
  }

  @Override
  public void makeFire(Player player, Item item) {
    if (item.getId() == ItemId.WILLOW_LOGS && player.getRegionId() == 11573) {
      // addCompletedTask(player, WildernessTask.BURN_WILLOW);
    }
  }

  @Override
  public void castSpell(Player player, WidgetChild.Spellbook spellbookChild, Item item,
      Entity entity, MapObject mapObject) {
    if (spellbookChild == WidgetChild.Spellbook.HIGH_LEVEL_ALCHEMY
        && player.getRegionId() == 13372) {
      // addCompletedTask(player, WildernessTask.CAST_ALCHEMY);
    }
  }

  @Override
  public void agilityObstacle(Player player, MapObject mapObject) {
    if (mapObject.getId() == 24222 && player.getRegionId() == 11572) {
      // addCompletedTask(player, WildernessTask.CLIMB_WALL);
    } else if (mapObject.getId() == 16543 && player.getRegionId() == 12185) {
      // addCompletedTask(player, WildernessTask.DWARVERN_CREVICE);
    } else if (mapObject.getId() == 16510
        && (player.getRegionId() == 11673 || player.getRegionId() == 11417)) {
      // addCompletedTask(player, WildernessTask.TAVERLEY_FLOOR);
    }
  }

  @Override
  public void npcKilled(Player player, Npc npc) {
    if ((npc.getId() == NpcId.MAMMOTH_80) && player.getController().inWilderness()) {
      // addCompletedTask(player, WildernessTask.MAMMOTH);
    } else if ((npc.getId() == NpcId.EARTH_WARRIOR_51)
        && (player.getRegionId() == 12443 || player.getRegionId() == 12444)) {
      // addCompletedTask(player, WildernessTask.EARTH_WARRIOR);
    } else if (npc.getId() == NpcId.GIANT_MOLE_230
        && (player.getRegionId() == 6992 || player.getRegionId() == 6993)) {
      // addCompletedTask(player, WildernessTask.KILL_GIANT_MOLE);
    } else if ((npc.getId() == NpcId.SKELETAL_WYVERN_140
        || npc.getId() == NpcId.SKELETAL_WYVERN_140_466
        || npc.getId() == NpcId.SKELETAL_WYVERN_140_467
        || npc.getId() == NpcId.SKELETAL_WYVERN_140_468)
        && (player.getRegionId() == 12181 || player.getRegionId() == 12437)) {
      // addCompletedTask(player, WildernessTask.KILL_SKELETAL_WYVERN);
    } else if (npc.getId() == NpcId.BLUE_DRAGON_111 && player.getRegionId() == 11674) {
      // addCompletedTask(player, WildernessTask.KILL_BLUE_DRAGON);
    }
  }

  @Override
  public void openShop(Player player, String referenceName) {
    if (referenceName != null && referenceName.equals("skilling")
        && player.getRegionId() == 12083) {
      // addCompletedTask(player, WildernessTask.SARAHS_SHOP);
    }
  }

  @Override
  public void mapObjectOption(Player player, int option, MapObject mapObject) {
    if (mapObject.getId() == 411 && mapObject.getX() == 2947 && mapObject.getY() == 3820) {
      // addCompletedTask(player, WildernessTask.WILDERNESS_ALTAR);
    } else if (mapObject.getId() == 410
        && player.getEquipment().getHeadId() == ItemId.INITIATE_SALLET
        && player.getEquipment().getChestId() == ItemId.INITIATE_HAUBERK
        && player.getEquipment().getLegId() == ItemId.INITIATE_CUISSE) {
      // addCompletedTask(player, WildernessTask.GUTHIX_ALTAR);
    } else if (mapObject.getId() == 409
        && player.getEquipment().getHeadId() == ItemId.PROSELYTE_SALLET
        && player.getEquipment().getChestId() == ItemId.PROSELYTE_HAUBERK
        && player.getEquipment().getLegId() == ItemId.PROSELYTE_CUISSE) {
      // addCompletedTask(player, WildernessTask.SARIM_ALTAR);
    } else if (mapObject.getId() == 1816 && mapObject.getX() == 3067 && mapObject.getY() == 10253) {
      {
        // addCompletedTask(player, WildernessTask.KING_BLACK_DRAGON);
      }
    }
  }

  @Override
  public void npcOption(Player player, int option, Npc npc) {
    if (npc.getId() == NpcId.MAGE_OF_ZAMORAK_2581 && option == 3) {
      // addCompletedTask(player, WildernessTask.MAGE_OF_ZAMORAK);
    }
  }

  public void addCompletedTask(Player player, WildernessTask task) {
    super.addCompletedTask(player, task.getTask());
  }
}


enum WildernessTask {
  CAST_ALCHEMY(new AchievementDiaryTask("Cast High Alchemy at the Fountain of Rune.", MEDIUM)), // not
                                                                                                // done
  LEVER(new AchievementDiaryTask("Enter the Wilderness from the Ardougne or Edgeville lever",
      MEDIUM)),
  WILDERNESS_ALTAR(
      new AchievementDiaryTask("Pray at the Chaos Temple in level 38, Western Wilderness", MEDIUM)),
  CHAOS_TEMPLE(new AchievementDiaryTask("Enter the Chaos Runecrafting temple.", MEDIUM)), // not
                                                                                          // done
  MAMMOTH(new AchievementDiaryTask("Kill a mammoth.", EASY)),
  EARTH_WARRIOR(
      new AchievementDiaryTask("Kill an earth warrior in the Wilderness beneath Edgeville.", EASY)),
  DEMONIC_RUINS(
      new AchievementDiaryTask("Restore some Prayer points at the Demonic Ruins.", MEDIUM)), // not
                                                                                             // done
  KING_BLACK_DRAGON(new AchievementDiaryTask("Enter the King Black Dragon Lair.", MEDIUM)),
  SPIDER_EGGS(new AchievementDiaryTask("Collect 5 red spider's eggs from the Wilderness.", MEDIUM)), // not
                                                                                                     // done
  IRON_ORE(new AchievementDiaryTask("Mine some iron ore in the Wilderness.", EASY)),
  MAGE_OF_ZAMORAK(
      new AchievementDiaryTask("Have the Mage of Zamorak teleport you to the Abyss.", EASY)),
  TEAM_CAPE(new AchievementDiaryTask("Equip any team cape in the Wilderness.", MEDIUM)), // not
                                                                                         // done

  MITHRIL_ORE(new AchievementDiaryTask("Mine some mithril ore in the Wilderness.", MEDIUM)),
  ENT_LOGS(new AchievementDiaryTask("Chop some yew logs from an ent.", MEDIUM)), // not done
  WILDERNESS_GODWARS(new AchievementDiaryTask("Enter the Wilderness God Wars Dungeon", MEDIUM)), // not
                                                                                                 // done
  WILDERNESS_AGILITY(
      new AchievementDiaryTask("Complete a lap of the Wilderness Agility Course.", MEDIUM)), // not
                                                                                             // done
  GREEN_DRAGON(new AchievementDiaryTask("Kill a green dragon.", MEDIUM)), // not done
  ANKOU(new AchievementDiaryTask("Kill an ankou in the Wilderness.", MEDIUM)), // not done
  EARTH_ORB(new AchievementDiaryTask("Charge an earth orb.", MEDIUM)), // not done
  BLOODVELD(
      new AchievementDiaryTask("Kill a bloodveld in the Wilderness God Wars Dungeon.", MEDIUM)), // not
                                                                                                 // done
  MYSTERIOUS_EMBLEM(new AchievementDiaryTask(
      " Sell a mysterious emblem to the Emblem Trader in Edgeville.", MEDIUM)), // not
                                                                                // done
  GOLD_HELMET(new AchievementDiaryTask("Smith a Gold helmet in the Resource Area.", MEDIUM)), // not
                                                                                              // done
  MUDDY_CHEST(new AchievementDiaryTask("Open the muddy chest in the Lava Maze.", MEDIUM)), // not
                                                                                           // done

  GOD_SPELL(new AchievementDiaryTask(
      "Cast one of the 3 God spells against another player in the Wilderness.", HARD)),
  CHARGE_AIR_ORB(new AchievementDiaryTask("Charge an air orb.", HARD)),
  BLACK_SALAMANDER(new AchievementDiaryTask("Catch a black salamander", HARD)),
  ADAMANT_SCIMITAR(
      new AchievementDiaryTask("Smith an adamant scimitar in the Resource Area.", HARD)),
  LAVA_DRAGON(
      new AchievementDiaryTask("Kill a lava dragon and bury the bones on Lava Dragon Isle.", HARD)),
  CHAOS_ELEMENTAL(new AchievementDiaryTask("Kill the Chaos Elemental.", HARD)),
  DEMI_BOSSES(
      new AchievementDiaryTask(" Kill the Crazy archaeologist, Chaos Fanatic & Scorpia.", HARD)),
  TROLLHEIM_SHORTCUT(new AchievementDiaryTask(
      "Take the Agility Shortcut from Trollheim into the Wilderness.", HARD)),
  SPIRITUAL_WARRIOR(new AchievementDiaryTask(
      "Kill a spiritual warrior in the Wilderness God Wars Dungeon.", HARD)),
  RAW_LAVA_EEL(new AchievementDiaryTask("Fish some raw lava eel in the Wilderness.", HARD)),

  WILDY_BOSSES(new AchievementDiaryTask("Kill Callisto, Venenatis & Vet'ion.", ELITE)),
  GHORROCK_TELEPORT(new AchievementDiaryTask("Teleport to Ghorrock.", ELITE)),
  DARK_CRAB(new AchievementDiaryTask("Fish and cook a dark crab in the Resource Area.", ELITE)),
  RUNE_SCIMITAR(
      new AchievementDiaryTask(" Smith a rune scimitar from scratch in the Resource Area.", ELITE)),
  ROGUES_CASTLE_CHEST(new AchievementDiaryTask("Steal from the Chest (Rogues' Castle).", ELITE)),
  SPIRITUAL_MAGE(new AchievementDiaryTask(
      "Slay a spiritual mage inside the Wilderness God Wars Dungeon.", ELITE)),
  MAGIC_LOGS(new AchievementDiaryTask("Cut and burn some magic logs in the Resource Area.", ELITE));


  private AchievementDiaryTask task;

  private WildernessTask(AchievementDiaryTask task) {
    this.task = task;
  }

  public AchievementDiaryTask getTask() {
    return task;
  }
}
