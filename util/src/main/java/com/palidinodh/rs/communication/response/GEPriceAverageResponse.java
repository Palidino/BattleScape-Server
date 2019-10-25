package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class GEPriceAverageResponse extends Response {
  private int totalBuy;
  private int totalSell;
  private int averageBuy;
  private int averageSell;
  private int cheapestSell;

  public GEPriceAverageResponse(Request request, int totalBuy, int averageBuy, int totalSell,
      int averageSell, int cheapestSell) {
    super(request);
    this.totalBuy = totalBuy;
    this.averageBuy = averageBuy;
    this.totalSell = totalSell;
    this.averageSell = averageSell;
    this.cheapestSell = cheapestSell;
  }

  public int getTotalBuy() {
    return totalBuy;
  }

  public int getTotalSell() {
    return totalSell;
  }

  public int getAverageBuy() {
    return averageBuy;
  }

  public int getAverageSell() {
    return averageSell;
  }

  public int getCheapestSell() {
    return cheapestSell;
  }
}
