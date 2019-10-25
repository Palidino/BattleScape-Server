package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.adaptive.GrandExchangeItem;
import com.palidinodh.rs.communication.request.Request;

public class GEHistoryResponse extends Response {
  private GrandExchangeItem[] items;

  public GEHistoryResponse(Request request, GrandExchangeItem[] items) {
    super(request);
    this.items = items;
  }

  public GrandExchangeItem[] getItems() {
    return items;
  }
}
