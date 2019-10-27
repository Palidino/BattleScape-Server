package com.palidinodh.osrsscript.player.skill;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.cache.ItemId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.ObjectDef;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.model.player.skill.SkillEntry;
import com.palidinodh.osrscore.model.player.skill.SkillModel;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PEvent;
import com.palidinodh.util.PNumber;
import lombok.var;

public class Cooking extends SkillContainer {
  private static final int COOKING_RANGE_ID = 114;

  private static List<SkillEntry> entries = new ArrayList<>();

  @Override
  public int getSkillId() {
    return Skills.COOKING;
  }

  @Override
  public List<SkillEntry> getEntries() {
    return entries;
  }

  @Override
  public void actionSuccess(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (entry.getConsume() != null && entry.getConsume().getId() == ItemId.SACRED_EEL) {
      var scaleCount = 3 + PRandom.randomI(6);
      player.getInventory().addOrDropItem(ItemId.ZULRAHS_SCALES, scaleCount * 2);
      player.getSkills().addXp(getSkillId(), scaleCount * 3);
    } else if (entry.getConsume() != null && entry.getConsume().getId() == ItemId.INFERNAL_EEL) {
      if (PRandom.randomE(16) == 0) {
        player.getInventory().addOrDropItem(ItemId.ONYX_BOLT_TIPS, 2);
      } else if (PRandom.randomE(12) == 0) {
        player.getInventory().addOrDropItem(ItemId.LAVA_SCALE_SHARD, 2 + PRandom.randomI(8));
      } else {
        player.getInventory().addOrDropItem(ItemId.TOKKUL, 20 + PRandom.randomI(20));
      }
    } else if (entry.getConsume() != null
        && entry.getConsume().getId() == ItemId.LEAPING_STURGEON) {
      if (PRandom.randomE(2) == 0) {
        player.getInventory().addOrDropItem(ItemId.FISH_OFFCUTS);
      } else {
        player.getInventory().addOrDropItem(ItemId.CAVIAR);
      }
      player.getSkills().addXp(getSkillId(), 15);
    }
  }

  @Override
  public Item createHook(Player player, Item item, Npc npc, MapObject mapObject, SkillEntry entry) {
    if (mapObject != null && isFire(mapObject.getName())) {
      if (item.getName().endsWith("s")) {
        player.getGameEncoder()
            .sendFilteredMessage("You successfully cook some " + item.getName() + ".");
      } else if (item.getAmount() > 1) {
        player.getGameEncoder()
            .sendFilteredMessage("You successfully cook some " + item.getName() + "s.");
      } else {
        player.getGameEncoder()
            .sendFilteredMessage("You successfully cook a " + item.getName() + ".");
      }
    }
    return item;
  }

  @Override
  public Item failedCreateHook(Player player, Item item, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (mapObject != null && isFire(mapObject.getName())) {
      var itemName = item.getName().replace("Burnt ", "");
      if (itemName.endsWith("s")) {
        player.getGameEncoder().sendFilteredMessage("You burn some " + itemName + ".");
      } else if (item.getAmount() > 1) {
        player.getGameEncoder().sendFilteredMessage("You burn some " + itemName + "s.");
      } else {
        player.getGameEncoder().sendFilteredMessage("You burn a " + itemName + ".");
      }
    }
    return item;
  }

  @Override
  public int experienceHook(Player player, int experience, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (player.getEquipment().getHandId() == ItemId.COOKING_GAUNTLETS) {
      experience *= 1.1;
    }
    return experience;
  }

  @Override
  public boolean failedActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (entry.getFailedCreate() == null) {
      return false;
    }
    var failFactor = entry.getFailFactor();
    if (player.getEquipment().getHandId() == ItemId.COOKING_GAUNTLETS
        && entry.getCreate() != null) {
      if (entry.getCreate().getId() == ItemId.RAW_TUNA) {
        failFactor--;
      } else if (entry.getCreate().getId() == ItemId.RAW_LOBSTER) {
        failFactor -= 10;
      } else if (entry.getCreate().getId() == ItemId.RAW_SWORDFISH) {
        failFactor -= 5;
      } else if (entry.getCreate().getId() == ItemId.RAW_MONKFISH) {
        failFactor -= 2;
      } else if (entry.getCreate().getId() == ItemId.RAW_SHARK) {
        failFactor -= 11;
      } else if (entry.getCreate().getId() == ItemId.RAW_ANGLERFISH) {
        failFactor -= 10;
      }
    }
    var level = player.getSkills().getLevel(getSkillId());
    if (failFactor > 0 && level >= failFactor
        || player.getEquipment().wearingAccomplishmentCape(getSkillId())) {
      return false;
    }
    var power = level + 8;
    var failure = entry.getLevel() + 2;
    if (player.inWildernessResourceArea()) {
      failure /= 2;
    }
    var chance = 0.0;
    if (power > failure) {
      chance = 1 - (failure + 2) / ((power + 1) * 2.0);
    } else {
      chance = power / ((failure + 1) * 2.0);
    }
    if (player.getEquipment().getHandId() == ItemId.COOKING_GAUNTLETS) {
      chance = Math.min(PNumber.addDoubles(chance, 0.1), 1.0);
    }
    if (player.isPremiumMember()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.05), 1.0);
    }
    if (player.hasVoted()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.05), 1.0);
    }
    if (player.getController().inWilderness()) {
      chance = Math.min(PNumber.addDoubles(chance, 0.1), 1.0);
    }
    return PRandom.randomI(100) < Math.max(0.01, 1 - chance) * 100;
  }

  @Override
  public boolean findEntriesMapObjectMatchHook(Player player, SkillModel mapObjectModel,
      int widgetOnMapObjectId, SkillEntry entry) {
    var fireName = "";
    if (widgetOnMapObjectId > 0 && entry.getWidgetOnMapObjects().contains(COOKING_RANGE_ID)) {
      fireName = ObjectDef.getObjectDef(widgetOnMapObjectId).getName().toLowerCase();
    }
    if (mapObjectModel != null && entry.getWidgetOnMapObjects().contains(COOKING_RANGE_ID)) {
      if (entry.getConsume() != null && player.getInventory().hasItem(entry.getConsume().getId())) {
        fireName = ObjectDef.getObjectDef(mapObjectModel.getId()).getName().toLowerCase();
      }
    }
    if (isFire(fireName)) {
      return true;
    }
    return false;
  }

  private boolean isFire(String name) {
    return name.equals("fire") || name.endsWith("range") || name.endsWith("stove")
        || name.endsWith("oven");
  }

  static {
    entries.add(SkillEntry.builder().level(1).failFactor(33).experience(30).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_SHRIMPS))
        .create(new RandomItem(ItemId.SHRIMPS)).failedCreate(new RandomItem(ItemId.BURNT_FISH))
        .build());
    entries.add(SkillEntry.builder().level(1).failFactor(33).experience(30).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_ANCHOVIES))
        .create(new RandomItem(ItemId.ANCHOVIES)).failedCreate(new RandomItem(ItemId.BURNT_FISH))
        .build());
    entries.add(SkillEntry.builder().level(1).failFactor(40).experience(35).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_SARDINE))
        .create(new RandomItem(ItemId.SARDINE)).failedCreate(new RandomItem(ItemId.BURNT_FISH_369))
        .build());
    entries.add(SkillEntry.builder().level(15).failFactor(50).experience(70).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_TROUT))
        .create(new RandomItem(ItemId.TROUT)).failedCreate(new RandomItem(ItemId.BURNT_FISH_343))
        .build());
    entries.add(SkillEntry.builder().level(20).failFactor(59).experience(80).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_PIKE))
        .create(new RandomItem(ItemId.PIKE)).failedCreate(new RandomItem(ItemId.BURNT_FISH_343))
        .build());
    entries.add(SkillEntry.builder().level(25).failFactor(58).experience(70).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_SALMON))
        .create(new RandomItem(ItemId.SALMON)).failedCreate(new RandomItem(ItemId.BURNT_FISH_343))
        .build());
    entries.add(SkillEntry.builder().level(30).failFactor(99).experience(190).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_KARAMBWAN))
        .create(new RandomItem(ItemId.COOKED_KARAMBWAN))
        .failedCreate(new RandomItem(ItemId.BURNT_KARAMBWAN)).build());
    entries.add(SkillEntry.builder().level(30).failFactor(64).experience(80).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_TUNA))
        .create(new RandomItem(ItemId.TUNA)).failedCreate(new RandomItem(ItemId.BURNT_FISH_367))
        .build());
    entries.add(SkillEntry.builder().level(40).failFactor(74).experience(90).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_LOBSTER))
        .create(new RandomItem(ItemId.LOBSTER)).failedCreate(new RandomItem(ItemId.BURNT_LOBSTER))
        .build());
    entries.add(SkillEntry.builder().level(43).failFactor(80).experience(130).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_BASS))
        .create(new RandomItem(ItemId.BASS)).failedCreate(new RandomItem(ItemId.BURNT_FISH_367))
        .build());
    entries.add(SkillEntry.builder().level(45).failFactor(86).experience(140).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_SWORDFISH))
        .create(new RandomItem(ItemId.SWORDFISH))
        .failedCreate(new RandomItem(ItemId.BURNT_SWORDFISH)).build());
    entries.add(SkillEntry.builder().level(62).failFactor(90).experience(150).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_MONKFISH))
        .create(new RandomItem(ItemId.MONKFISH)).failedCreate(new RandomItem(ItemId.BURNT_MONKFISH))
        .build());
    entries.add(SkillEntry.builder().level(80).failFactor(104).experience(210).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_SHARK))
        .create(new RandomItem(ItemId.SHARK)).failedCreate(new RandomItem(ItemId.BURNT_SHARK))
        .build());
    entries.add(SkillEntry.builder().level(84).failFactor(108).experience(230).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_ANGLERFISH))
        .create(new RandomItem(ItemId.ANGLERFISH))
        .failedCreate(new RandomItem(ItemId.BURNT_ANGLERFISH)).build());
    entries.add(SkillEntry.builder().level(90).failFactor(114).experience(215).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_DARK_CRAB))
        .create(new RandomItem(ItemId.DARK_CRAB))
        .failedCreate(new RandomItem(ItemId.BURNT_DARK_CRAB)).build());
    entries.add(SkillEntry.builder().level(91).failFactor(114).experience(216).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_MANTA_RAY))
        .create(new RandomItem(ItemId.MANTA_RAY))
        .failedCreate(new RandomItem(ItemId.BURNT_MANTA_RAY)).build());

    entries.add(SkillEntry.builder().level(35).failFactor(68).experience(200).animation(3283)
        .consume(new RandomItem(ItemId.GRAPES)).consume(new RandomItem(ItemId.JUG_OF_WATER))
        .create(new RandomItem(ItemId.JUG_OF_WINE))
        .failedCreate(new RandomItem(ItemId.JUG_OF_BAD_WINE)).build());
    entries.add(SkillEntry.builder().level(70).experience(80).animation(1248)
        .tool(new RandomItem(ItemId.KNIFE)).consume(new RandomItem(ItemId.LEAPING_STURGEON))
        .build());
    entries.add(SkillEntry.builder().level(72).experience(100).animation(1248)
        .tool(new RandomItem(ItemId.KNIFE)).consume(new RandomItem(ItemId.SACRED_EEL)).build());
    entries.add(SkillEntry.builder().animation(1248).tool(new RandomItem(ItemId.HAMMER))
        .consume(new RandomItem(ItemId.INFERNAL_EEL)).build());

    entries.add(SkillEntry.builder().animation(883).widgetOnMapObject(COOKING_RANGE_ID)
        .consume(new RandomItem(ItemId.SEAWEED)).create(new RandomItem(ItemId.MOLTEN_GLASS))
        .build());

    entries.add(SkillEntry.builder().level(1).experience(10).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_PYSK_FISH_0))
        .create(new RandomItem(ItemId.PYSK_FISH_0))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(15).experience(15).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_SUPHI_FISH_1))
        .create(new RandomItem(ItemId.SUPHI_FISH_1))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(30).experience(20).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_LECKISH_FISH_2))
        .create(new RandomItem(ItemId.LECKISH_FISH_2))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(45).experience(25).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_BRAWK_FISH_3))
        .create(new RandomItem(ItemId.BRAWK_FISH_3))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(60).experience(30).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_MYCIL_FISH_4))
        .create(new RandomItem(ItemId.MYCIL_FISH_4))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(75).experience(35).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_ROQED_FISH_5))
        .create(new RandomItem(ItemId.ROQED_FISH_5))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(90).experience(38).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_KYREN_FISH_6))
        .create(new RandomItem(ItemId.KYREN_FISH_6))
        .failedCreate(new RandomItem(ItemId.BURNT_FISH_20854)).build());
    entries.add(SkillEntry.builder().level(1).experience(10).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_GUANIC_BAT_0))
        .create(new RandomItem(ItemId.GUANIC_BAT_0)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
    entries.add(SkillEntry.builder().level(15).experience(15).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_PRAEL_BAT_1))
        .create(new RandomItem(ItemId.PRAEL_BAT_1)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
    entries.add(SkillEntry.builder().level(30).experience(20).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_GIRAL_BAT_2))
        .create(new RandomItem(ItemId.GIRAL_BAT_2)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
    entries.add(SkillEntry.builder().level(45).experience(25).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_PHLUXIA_BAT_3))
        .create(new RandomItem(ItemId.PHLUXIA_BAT_3)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
    entries.add(SkillEntry.builder().level(60).experience(30).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_KRYKET_BAT_4))
        .create(new RandomItem(ItemId.KRYKET_BAT_4)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
    entries.add(SkillEntry.builder().level(75).experience(35).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_MURNG_BAT_5))
        .create(new RandomItem(ItemId.MURNG_BAT_5)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
    entries.add(SkillEntry.builder().level(90).experience(38).animation(883)
        .widgetOnMapObject(COOKING_RANGE_ID).consume(new RandomItem(ItemId.RAW_PSYKK_BAT_6))
        .create(new RandomItem(ItemId.PSYKK_BAT_6)).failedCreate(new RandomItem(ItemId.BURNT_BAT))
        .build());
  }
}
