package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class GEShopOfferResponse extends Response {
  private boolean success;

  public GEShopOfferResponse(Request request, boolean success) {
    super(request);
    this.success = success;
  }

  public boolean getSuccess() {
    return success;
  }
}
