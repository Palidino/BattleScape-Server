package com.palidinodh.rs.adaptive;

import java.io.Serializable;

public class GrandExchangeUser implements Serializable {
  private static final long serialVersionUID = 12212016;
  public static final int HISTORY = 0, HISTORY_RECENT = 1, HISTORY_RECENT_BUY = 2,
      HISTORY_RECENT_SELL = 3, HISTORY_RANDOM = 4, HISTORY_RANDOM_BUY = 5, HISTORY_RANDOM_SELL = 6;
  public static final int HISTORY_SIZE = 20;
  public static final int LIST_RANDOM_PLAYERS = 0, LIST_BUY_ITEM = 1, LIST_SELL_ITEM = 2,
      LIST_ITEM = 3;

  private int userId;
  private String username;
  private int gameMode;
  private GrandExchangeItem[] items = new GrandExchangeItem[GrandExchangeItem.MAX_P2P_ITEMS];

  public GrandExchangeUser(int userId) {
    this.userId = userId;
  }

  public GrandExchangeItem getItem(int slot) {
    return slot >= 0 && slot < items.length ? items[slot] : null;
  }

  public void setItem(int slot, GrandExchangeItem item) {
    if (slot < 0 || slot >= items.length) {
      return;
    }
    items[slot] = item;
  }

  public void abortItem(int slot) {
    if (slot < 0 || slot >= items.length || items[slot] == null) {
      return;
    }
    items[slot].setAborted(true);
  }

  public void collect(int slot, int amount, int price) {
    if (slot < 0 || slot >= items.length || items[slot] == null) {
      return;
    }
    items[slot].collected(amount, price);
    if (items[slot].canRemove()) {
      items[slot] = null;
    }
  }

  public GrandExchangeItem getRecentItem(int state) {
    GrandExchangeItem grandExchangeItem = null;
    for (GrandExchangeItem grandExchangeItem2 : items) {
      if (grandExchangeItem2 != null) {
        if (state == -1 || grandExchangeItem2.getState() == state) {
          if (grandExchangeItem == null
              || grandExchangeItem.getCreation() < grandExchangeItem2.getCreation()) {
            grandExchangeItem = grandExchangeItem2;
          }
        }
      }
    }
    return grandExchangeItem;
  }

  public GrandExchangeItem getItemFromId(int itemId) {
    for (GrandExchangeItem item : items) {
      if (item != null && item.getStateSelling() && item.getId() == itemId) {
        return item;
      }
    }
    return null;
  }

  public GrandExchangeItem getItemFromId(int itemId, int state) {
    for (GrandExchangeItem item : items) {
      if (item != null && item.getState() == state && !item.getAborted()
          && item.getRemainingAmount() > 0 && item.getId() == itemId) {
        return item;
      }
    }
    return null;
  }

  public int getBuyCount() {
    int count = 0;
    for (GrandExchangeItem item : items) {
      if (item != null && !item.getAborted() && item.getRemainingAmount() > 0
          && item.getStateBuying()) {
        count++;
      }
    }
    return count;
  }

  public int getSellCount() {
    int count = 0;
    for (GrandExchangeItem item : items) {
      if (item != null && !item.getAborted() && item.getRemainingAmount() > 0
          && item.getStateSelling()) {
        count++;
      }
    }
    return count;
  }

  public void expireItems() {
    for (GrandExchangeItem item : items) {
      if (item != null && item.isExpired()) {
        item.setAborted(true);
      }
    }
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getGameMode() {
    return gameMode;
  }

  public void setGameMode(int gameMode) {
    this.gameMode = gameMode;
  }

  public boolean isGameModeNormal() {
    return gameMode == RsGameMode.NORMAL.ordinal();
  }

  public boolean isGameModeHard() {
    return gameMode == RsGameMode.HARD.ordinal();
  }

  public GrandExchangeItem[] getItems() {
    return items;
  }

  public void setItems(GrandExchangeItem[] items) {
    this.items = items;
  }
}
