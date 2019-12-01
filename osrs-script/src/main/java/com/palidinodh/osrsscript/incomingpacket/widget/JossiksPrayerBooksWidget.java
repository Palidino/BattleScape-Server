package com.palidinodh.osrsscript.incomingpacket.widget;

import com.palidinodh.osrscore.io.incomingpacket.WidgetHandler;
import com.palidinodh.osrscore.io.cache.id.ItemId;
import com.palidinodh.osrscore.io.cache.id.WidgetId;
import com.palidinodh.osrscore.model.item.ItemDef;
import com.palidinodh.osrscore.model.player.Player;
import com.palidinodh.rs.ReferenceId;
import com.palidinodh.util.PNumber;

@ReferenceId(WidgetId.JOSSIKS_SALVAGED_PRAYERBOOKS)
class JossiksPrayerBooksWidget implements WidgetHandler {
  @Override
  public void execute(Player player, int option, int widgetId, int childId, int slot, int itemId) {
    int bookId = -1;
    int price = 1;
    switch (childId) {
      case 3: // Holy book
        bookId = ItemId.HOLY_BOOK;
        price = 100000;
        break;
      case 4: // Book of balance
        bookId = ItemId.BOOK_OF_BALANCE;
        price = 100000;
        break;
      case 5: // Unholy book
        bookId = ItemId.UNHOLY_BOOK;
        price = 100000;
        break;
      case 6: // Book of war
        bookId = ItemId.BOOK_OF_WAR;
        price = 500000;
        break;
      case 7: // Book of law
        bookId = ItemId.BOOK_OF_LAW;
        price = 500000;
        break;
      case 8: // Book of darkness
        bookId = ItemId.BOOK_OF_DARKNESS;
        price = 500000;
        break;
    }
    if (bookId == -1) {
      return;
    }
    if (player.getInventory().getCount(ItemId.COINS) < price) {
      player.getGameEncoder().sendMessage(
          ItemDef.getName(bookId) + ": currently costs " + PNumber.formatNumber(price) + " coins.");
      return;
    } else if (!player.getInventory().canAddItem(bookId, 1)) {
      player.getInventory().notEnoughSpace();
      return;
    }
    player.getInventory().deleteItem(ItemId.COINS, price);
    player.getInventory().addItem(bookId, 1);
  }
}
