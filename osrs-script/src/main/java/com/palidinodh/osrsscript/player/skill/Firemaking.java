package com.palidinodh.osrsscript.player.skill;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.RandomItem;
import com.palidinodh.osrscore.model.map.MapItem;
import com.palidinodh.osrscore.model.map.MapObject;
import com.palidinodh.osrscore.model.map.TempMapObject;
import com.palidinodh.osrscore.model.map.route.Route;
import com.palidinodh.osrscore.model.npc.Npc;
import com.palidinodh.osrscore.model.player.AchievementDiary;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.model.player.Skills;
import com.palidinodh.osrscore.model.player.WidgetManager;
import com.palidinodh.osrscore.model.player.skill.SkillContainer;
import com.palidinodh.osrscore.model.player.skill.SkillEntry;
import com.palidinodh.osrscore.model.player.skill.SkillPet;
import com.palidinodh.random.PRandom;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PEvent;
import lombok.var;

class Firemaking extends SkillContainer {
  private static final int START_FIRE_ANIMATION = 733;
  private static final int ADD_TO_FIRE_ANIMATION = 897;
  private static final int FIRE_MAP_OBJECT = 5249;

  private static List<SkillEntry> entries = new ArrayList<>();

  @Override
  public int getSkillId() {
    return Skills.FIREMAKING;
  }

  @Override
  public List<SkillEntry> getEntries() {
    return entries;
  }

  @Override
  public void actionSuccess(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (isLog(entry.getConsume().getId())) {
      if (PRandom.randomE(160 - entry.getLevel()) == 0) {
        player.getInventory().addOrDropItem(ItemId.SUPPLY_CRATE);
      }
      if (entry.getAnimation() == START_FIRE_ANIMATION && mapObject != null) {
        player.setAnimation(ADD_TO_FIRE_ANIMATION);
      }
      if (mapObject != null) {
        player.setFaceTile(mapObject);
        if (player.withinDistance(mapObject, 0)) {
          Route.moveOffTile(player);
        }
      } else {
        player.lock();
        var logMapItem = MapItem.getForPacket(entry.getConsume(), player);
        player.getGameEncoder().sendMapItem(logMapItem);
        var tempMapObjectEvent = new PEvent(2) {
          @Override
          public void execute() {
            var fire = new MapObject(FIRE_MAP_OBJECT, player, 10, 0);
            player.getWorld().addEvent(new TempMapObject(100, player.getController(), fire));
            AchievementDiary.makeFireUpdate(player, entry.getConsume());
            Route.moveOffTile(player);
            player.getGameEncoder().sendRemoveMapItem(logMapItem);
            player.unlock();
            stop();
          }
        };
        player.getWorld().addEvent(tempMapObjectEvent);
      }
    }
  }

  @Override
  public int experienceHook(Player player, int experience, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (player.getEquipment().wearingPyromancerOutfit()) {
      experience *= 1.1;
    }
    return experience;
  }

  @Override
  public boolean canDoActionHook(Player player, PEvent event, Npc npc, MapObject mapObject,
      SkillEntry entry) {
    if (mapObject == null && isLog(entry.getConsume().getId())) {
      if (player.getHeight() != player.getClientHeight()
          || player.getController().hasSolidMapObject(player)) {
        player.getGameEncoder().sendMessage("You can't do this here.");
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean widgetOnMapObjectHook(Player player, int widgetId, int childId, int slot,
      int itemId, MapObject mapObject) {
    if (widgetId != WidgetId.INVENTORY || mapObject.getId() != FIRE_MAP_OBJECT) {
      return false;
    }
    if (!isLog(itemId)) {
      return false;
    }
    openMakeX(player, mapObject, WidgetManager.MakeX.FIRE, player.getInventory().getCount(itemId),
        PCollection.toList(findEntryFromConsume(itemId)));
    return true;
  }

  private boolean isLog(int itemId) {
    return itemId == ItemId.LOGS || itemId == ItemId.OAK_LOGS || itemId == ItemId.WILLOW_LOGS
        || itemId == ItemId.MAPLE_LOGS || itemId == ItemId.YEW_LOGS || itemId == ItemId.MAGIC_LOGS
        || itemId == ItemId.ACHEY_TREE_LOGS || itemId == ItemId.TEAK_LOGS
        || itemId == ItemId.ARCTIC_PINE_LOGS || itemId == ItemId.MAHOGANY_LOGS
        || itemId == ItemId.REDWOOD_LOGS || itemId == ItemId.KINDLING_20799;
  }

  static {
    entries.add(SkillEntry.builder().level(1).experience(40).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX)).consume(new RandomItem(ItemId.LOGS))
        .pet(new SkillPet(ItemId.PHOENIX, 461808)).build());
    entries.add(SkillEntry.builder().level(1).experience(40).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.ACHEY_TREE_LOGS)).build());
    entries.add(SkillEntry.builder().level(1).experience(40).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.KINDLING_20799)).pet(new SkillPet(ItemId.PHOENIX, 461808))
        .build());
    entries.add(SkillEntry.builder().level(15).experience(60).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX)).consume(new RandomItem(ItemId.OAK_LOGS))
        .pet(new SkillPet(ItemId.PHOENIX, 443697)).build());
    entries.add(SkillEntry.builder().level(30).experience(90).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.WILLOW_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 435165))
        .build());
    entries.add(SkillEntry.builder().level(35).experience(105).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.TEAK_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 426954))
        .build());
    entries.add(SkillEntry.builder().level(42).experience(125).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.ARCTIC_PINE_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 382609))
        .build());
    entries.add(SkillEntry.builder().level(45).experience(135).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.MAPLE_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 305792))
        .build());
    entries.add(SkillEntry.builder().level(50).experience(157).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.MAHOGANY_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 170874))
        .build());
    entries.add(SkillEntry.builder().level(60).experience(202).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX)).consume(new RandomItem(ItemId.YEW_LOGS))
        .pet(new SkillPet(ItemId.PHOENIX, 149434)).build());
    entries.add(SkillEntry.builder().level(75).experience(303).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.MAGIC_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 138583))
        .build());
    entries.add(SkillEntry.builder().level(90).experience(350).animation(START_FIRE_ANIMATION)
        .alwaysMake1(true).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.REDWOOD_LOGS)).pet(new SkillPet(ItemId.PHOENIX, 128885))
        .build());
    entries.add(SkillEntry.builder().level(49).tool(new Item(ItemId.TINDERBOX))
        .consume(new RandomItem(ItemId.BULLSEYE_LANTERN))
        .create(new RandomItem(ItemId.BULLSEYE_LANTERN_4550)).build());
  }
}
