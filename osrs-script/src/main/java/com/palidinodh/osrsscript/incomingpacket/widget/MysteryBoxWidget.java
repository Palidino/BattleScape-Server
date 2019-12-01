package com.palidinodh.osrsscript.incomingpacket.widget;

import java.util.ArrayList;
import java.util.List;
import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.item.MysteryBox;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.osrscore.util.RequestManager;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.util.PEvent;
import lombok.var;

@ReferenceId(WidgetId.CUSTOM_MYSTERY_BOX)
class MysteryBoxWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    if (player.isLocked()) {
      return;
    }
    switch (childId) {
      case 60:
        var boxId = player.getAttributeInt("mystery_box");
        if (boxId <= 0) {
          break;
        }
        if (!player.getInventory().hasItem(boxId)) {
          player.getGameEncoder().sendMessage("You need a " + ItemDef.getName(boxId) + " to spin.");
          break;
        }
        player.getInventory().deleteItem(boxId);
        final List<Item> mysteryBoxItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
          mysteryBoxItems.add(MysteryBox.getRandomItem(player, boxId));
        }
        player.getGameEncoder().sendItems(WidgetId.CUSTOM_MYSTERY_BOX, 41, 0, mysteryBoxItems);
        player.getGameEncoder().sendHideWidget(WidgetId.CUSTOM_MYSTERY_BOX, 59, true);
        int totalRolls = MysteryBox.getRolls(boxId);
        var event = new PEvent(0) {
          private int rollsComplete = 0;

          @Override
          public void execute() {
            if (!player.isVisible()) {
              stop();
              return;
            }
            setTick(0);
            mysteryBoxItems.remove(0);
            mysteryBoxItems.add(MysteryBox.getRandomItem(player, boxId));
            player.getGameEncoder().sendItems(WidgetId.CUSTOM_MYSTERY_BOX, 41, 0, mysteryBoxItems);
            player.getSession().write();
            if (getExecutions() > 0 && (getExecutions() % 4) == 0) {
              boolean complete = ++rollsComplete >= totalRolls;
              if (complete) {
                stop();
                player.getGameEncoder().sendHideWidget(WidgetId.CUSTOM_MYSTERY_BOX, 59, false);
              }
              var boxItem = mysteryBoxItems.get(mysteryBoxItems.size() - 3);
              player.getInventory().addOrDropItem(boxItem);
              if (complete
                  && (boxId == ItemId.BLOODY_KEY_32304 || boxId == ItemId.BLOODIER_KEY_32305)) {
                player.getWorld().sendItemDropNews(player, boxItem.getId(),
                    "a " + ItemDef.getName(boxId));
              }
              RequestManager.addLootBoxLog(player, boxId, boxItem);
              setTick(2);
            }
          }
        };
        player.getWorld().addEvent(event);
        break;
    }
  }
}
