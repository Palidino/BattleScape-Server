package com.palidinodh.rs.adaptive;

import java.io.Serializable;

public class GrandExchangeCompleteItem implements Serializable {
  private static final long serialVersionUID = 12212016;

  private int amount;
  private int price;

  public GrandExchangeCompleteItem(int amount, int price) {
    this.amount = amount;
    this.price = price;
  }

  public int getAmount() {
    return amount;
  }

  public int getPrice() {
    return price;
  }
}
