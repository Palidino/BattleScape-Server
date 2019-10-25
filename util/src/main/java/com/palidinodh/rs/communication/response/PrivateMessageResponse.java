package com.palidinodh.rs.communication.response;

import com.palidinodh.rs.communication.request.Request;

public class PrivateMessageResponse extends Response {
  private String senderUsername;
  private int icon;
  private String receiverUsername;
  private String message;

  public PrivateMessageResponse(Request request, String senderUsername, int icon,
      String receiverUsername, String message) {
    super(request);
    this.senderUsername = senderUsername;
    this.icon = icon;
    this.receiverUsername = receiverUsername;
    this.message = message;
  }

  public String getSenderUsername() {
    return senderUsername;
  }

  public int getIcon() {
    return icon;
  }

  public String getReceiverUsername() {
    return receiverUsername;
  }

  public String getMessage() {
    return message;
  }
}
