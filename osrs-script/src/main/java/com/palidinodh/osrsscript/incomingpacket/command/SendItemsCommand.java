package com.palidinodh.osrsscript.incomingpacket.command;

import java.util.ArrayList;
import com.palidinodh.osrscore.io.incomingpacket.CommandHandler;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.random.PRandom;
import lombok.var;

public class SendItemsCommand implements CommandHandler {
  @Override
  public String getExample() {
    return "widget_id child_id item_count (random_ids)";
  }

  @Override
  public boolean canUse(Player player) {
    return player.getRights() == Player.RIGHTS_ADMIN;
  }

  @Override
  public void execute(Player player, String message) {
    var values = message.split(" ");
    var widgetId = Integer.parseInt(values[0]);
    var childId = Integer.parseInt(values[1]);
    var itemCount = Integer.parseInt(values[2]);
    var randomIds = false;
    if (values.length == 4) {
      randomIds = true;
    }
    var items = new ArrayList<Item>();
    for (var i = 0; i < itemCount; i++) {
      items.add(new Item(randomIds ? PRandom.randomE(8192) : i, 1));
    }
    player.getGameEncoder().sendItems(widgetId, childId, 0, items);
  }
}
