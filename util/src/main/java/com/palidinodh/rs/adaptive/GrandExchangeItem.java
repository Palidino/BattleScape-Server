package com.palidinodh.rs.adaptive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PTime;

public class GrandExchangeItem implements Serializable {
  private static final long serialVersionUID = 12202016;
  public static final int MAX_F2P_ITEMS = 3, MAX_P2P_ITEMS = 8;
  public static final int STATE_BUYING = 1, STATE_SELLING = 2;
  public static final int EXPIRATION = 30;
  public static final int BOND = 13190;
  private static final Map<Integer, Boolean> BLOCKED_HARD_MODE_ITEMS = PCollection.toMap(373, true, // Swordfish
      7946, true, // Monkfish
      385, true, // Shark
      11936, true, // Dark crab
      3144, true, // Cooked karambwan
      2436, true, // Super attack(4)
      145, true, // Super attack(3)
      147, true, // Super attack(2)
      149, true, // Super attack(1)
      2440, true, // Super strength(4)
      157, true, // Super strength(3)
      159, true, // Super strength(2)
      161, true, // Super strength(1)
      2442, true, // Super defence(4)
      163, true, // Super defence(3)
      165, true, // Super defence(2)
      167, true, // Super defence(1)
      2444, true, // Ranging potion(4)
      169, true, // Ranging potion(3)
      171, true, // Ranging potion(2)
      173, true, // Ranging potion(1)
      3040, true, // Magic potion(4)
      3042, true, // Magic potion(3)
      3044, true, // Magic potion(2)
      3046, true, // Magic potion(1)
      12695, true, // Super combat potion(4)
      12697, true, // Super combat potion(3)
      12699, true, // Super combat potion(2)
      12701, true, // Super combat potion(1)
      22461, true, // Bastion potion(4)
      22464, true, // Bastion potion(3)
      22467, true, // Bastion potion(2)
      22470, true, // Bastion potion(1)
      22449, true, // Battlemage potion(4)
      22452, true, // Battlemage potion(3)
      22455, true, // Battlemage potion(2)
      22458, true, // Battlemage potion(1)
      12625, true, // Stamina potion(4)
      12627, true, // Stamina potion(3)
      12629, true, // Stamina potion(2)
      12631, true, // Stamina potion(1)
      2448, true, // Superantipoison(4)
      181, true, // Superantipoison(3)
      183, true, // Superantipoison(2)
      185, true, // Superantipoison(1)
      5952, true, // Antidote++(4)
      5954, true, // Antidote++(3)
      5956, true, // Antidote++(2)
      5958, true, // Antidote++(1)
      3024, true, // Super restore(4)
      3026, true, // Super restore(3)
      3028, true, // Super restore(2)
      3030, true, // Super restore(1)
      12905, true, // Anti-venom(4)
      12907, true, // Anti-venom(3)
      12909, true, // Anti-venom(2)
      12911, true, // Anti-venom(1)
      12913, true, // Anti-venom+(4)
      12915, true, // Anti-venom+(3)
      12917, true, // Anti-venom+(2)
      12919, true, // Anti-venom+(1)
      10925, true, // Sanfew serum(4)
      10927, true, // Sanfew serum(3)
      10929, true, // Sanfew serum(2)
      10931, true, // Sanfew serum(1)
      6685, true, // Saradomin brew(4)
      6687, true, // Saradomin brew(3)
      6689, true, // Saradomin brew(2)
      6691, true, // Saradomin brew(1)
      2452, true, // Antifire potion(4)
      2454, true, // Antifire potion(3)
      2456, true, // Antifire potion(2)
      2458, true, // Antifire potion(1)
      21978, true, // Super antifire potion(4)
      21981, true, // Super antifire potion(3)
      21984, true, // Super antifire potion(2)
      21987, true // Super antifire potion(1)
  );
  private static List<Integer> BLOCKED_HARD_MODE_ITEM_IDS = new ArrayList<>();

  public enum TaxBracket {
    LOW(10000000, 0.0025), MEDIUM(100000000, 0.005), HIGH(-1, 0.01);

    public final int value;
    public final double tax;

    private TaxBracket(int value, double tax) {
      this.value = value;
      this.tax = tax;
    }

    public static double getTax(int value) {
      for (TaxBracket bracket : values()) {
        if (value < bracket.value || bracket.value == -1) {
          return bracket.tax;
        }
      }
      return 0;
    }
  }

  private long creation;
  private int state;
  private String ip;
  private int id;
  private String name;
  private int amount;
  private int price;
  private int exchangedAmount;
  private int exchangedPrice;
  private int collectedAmount;
  private int collectedPrice;
  private boolean aborted;

  public GrandExchangeItem(int state, int id, int amount, int price) {
    this(state, "", id, "", amount, price);
  }

  public GrandExchangeItem(int state, String ip, int id, String name, int amount, int price) {
    creation = PTime.currentTimeMillis();
    this.state = state;
    this.ip = ip;
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.price = price;
  }

  public GrandExchangeItem(GrandExchangeItem grandExchangeItem) {
    creation = grandExchangeItem.getCreation();
    state = grandExchangeItem.getState();
    id = grandExchangeItem.getId();
    amount = grandExchangeItem.getAmount();
    price = grandExchangeItem.getPrice();
    exchangedAmount = grandExchangeItem.getExchangedAmount();
    exchangedPrice = grandExchangeItem.getExchangedPrice();
    aborted = grandExchangeItem.getAborted();
    collectedAmount = grandExchangeItem.getCollectedAmount();
    collectedPrice = grandExchangeItem.getCollectedPrice();
  }

  public int getRemainingAmount() {
    return Math.max(0, amount - exchangedAmount);
  }

  public int getReturnAmount() {
    if (getStateSelling() && !aborted) {
      return 0;
    } else if (getStateBuying()) {
      return Math.max(0, exchangedAmount - collectedAmount);
    } else if (getStateSelling()) {
      return Math.max(0, getRemainingAmount() - collectedAmount);
    }
    return 0;
  }

  public int getReturnCoins() {
    if (getStateBuying() && !aborted) {
      return 0;
    } else if (getStateBuying()) {
      return Math.max(0, amount * price - exchangedPrice - collectedPrice);
    } else if (getStateSelling()) {
      double tax = TaxBracket.getTax(exchangedPrice);
      int taxedAmount = (int) ((exchangedPrice - collectedPrice) * tax);
      return Math.max(0, exchangedPrice - collectedPrice - taxedAmount);
    }
    return 0;
  }

  public int getTotalCoins() {
    if (getStateBuying() && !aborted) {
      return 0;
    } else if (getStateBuying()) {
      return Math.max(0, amount * price - exchangedPrice - collectedPrice);
    } else if (getStateSelling()) {
      return Math.max(0, exchangedPrice - collectedPrice);
    }
    return 0;
  }

  public long getCreation() {
    return creation;
  }

  public boolean isExpired() {
    return PTime.betweenMilliToDay(creation) > EXPIRATION;
  }

  public int getState() {
    return state;
  }

  public boolean getStateBuying() {
    return state == STATE_BUYING;
  }

  public boolean getStateSelling() {
    return state == STATE_SELLING;
  }

  public String getIP() {
    return ip;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public int getAmount() {
    return amount;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getExchangedAmount() {
    return exchangedAmount;
  }

  public int getExchangedPrice() {
    return exchangedPrice;
  }

  public void setExchangedPrice(int exchangedPrice) {
    this.exchangedPrice = exchangedPrice;
    inspect();
  }

  public void increaseExchanged(int amount, int price) {
    exchangedAmount += amount;
    exchangedPrice += price * amount;
    inspect();
    if (getRemainingAmount() == 0) {
      aborted = true;
    }
  }

  public void setExchanged(int amount, int price) {
    exchangedAmount += amount;
    exchangedPrice += price;
    inspect();
    if (getRemainingAmount() == 0) {
      aborted = true;
    }
  }

  public void collected(int amount, int price) {
    collectedAmount += amount;
    collectedPrice += price;
    inspect();
  }

  public int getCollectedAmount() {
    return collectedAmount;
  }

  public void setCollectedAmount(int collectedAmount) {
    this.collectedAmount = collectedAmount;
    inspect();
  }

  public int getCollectedPrice() {
    return collectedPrice;
  }

  public void setCollectedPrice(int collectedPrice) {
    this.collectedPrice = collectedPrice;
    inspect();
  }

  public boolean getAborted() {
    return aborted;
  }

  public void setAborted(boolean aborted) {
    this.aborted = aborted;
  }

  public boolean canRemove() {
    return aborted && getReturnAmount() == 0 && getReturnCoins() == 0;
  }

  public void inspect() {
    if (exchangedAmount < 0) {
      exchangedAmount = 0;
    }
    if (exchangedPrice < 0) {
      exchangedPrice = 0;
    }
    if (collectedAmount < 0) {
      collectedAmount = 0;
    }
    if (collectedPrice < 0) {
      collectedPrice = 0;
    }
  }

  public boolean matches(GrandExchangeItem grandExchangeItem) {
    return grandExchangeItem != null && state == grandExchangeItem.getState()
        && id == grandExchangeItem.getId() && amount == grandExchangeItem.getAmount()
        && exchangedAmount == grandExchangeItem.getExchangedAmount()
        && exchangedPrice == grandExchangeItem.getExchangedPrice()
        && collectedAmount == grandExchangeItem.getCollectedAmount()
        && collectedPrice == grandExchangeItem.getCollectedPrice()
        && aborted == grandExchangeItem.getAborted();
  }

  public static boolean isBlockedHardModeItem(int id) {
    return BLOCKED_HARD_MODE_ITEMS.containsKey(id) && BLOCKED_HARD_MODE_ITEMS.get(id);
  }

  public static List<Integer> getBlockedHardModeItemIds() {
    return BLOCKED_HARD_MODE_ITEM_IDS;
  }

  static {
    for (int id : BLOCKED_HARD_MODE_ITEMS.keySet()) {
      BLOCKED_HARD_MODE_ITEM_IDS.add(id);
    }
  }
}
