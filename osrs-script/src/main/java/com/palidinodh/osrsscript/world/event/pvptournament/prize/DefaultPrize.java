package com.palidinodh.osrsscript.world.event.pvptournament.prize;

import java.util.List;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.model.item.Item;
import com.palidinodh.util.PCollection;

public class DefaultPrize implements Prize {
  private boolean rewardBonds;

  public DefaultPrize(boolean rewardBonds) {
    this.rewardBonds = rewardBonds;
  }

  @Override
  public List<Item> getItems(int position) {
    List<Item> items = null;
    switch (position) {
      case 0:
        items = PCollection.toList(new Item(ItemId.COINS, 8_000_000));
        break;
      case 1:
        items = PCollection.toList(new Item(ItemId.COINS, 4_000_000));
        break;
      case 2:
        items = PCollection.toList(new Item(ItemId.COINS, 2_000_000));
        break;
      case 3:
        items = PCollection.toList(new Item(ItemId.COINS, 1_000_000));
        break;
    }
    if (items != null && rewardBonds) {
      items.add(new Item(ItemId.BOND_32318, 50 / (position + 1)));
    }
    return items;
  }

  @Override
  public String getMessage() {
    return "Prizes include 15M" + (rewardBonds ? " and 100 bonds" : "") + ".";
  }
}
