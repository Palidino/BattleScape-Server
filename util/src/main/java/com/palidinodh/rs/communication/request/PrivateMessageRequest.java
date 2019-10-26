package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class PrivateMessageRequest extends Request {
  private String senderUsername;
  private int icon;
  private String receiverUsername;
  private String message;

  public PrivateMessageRequest(ServerSession session, int key, String senderUsername, int icon,
      String receiverUsername, String message) {
    super(session, key);
    this.senderUsername = senderUsername.toLowerCase();
    this.icon = icon;
    this.receiverUsername = receiverUsername.toLowerCase();
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
