package com.palidinodh.nio;

public class WriteEventHandler {
  private Object attachment;

  public WriteEventHandler() {}

  public WriteEventHandler(Object attachment) {
    this.attachment = attachment;
  }

  public void complete(Session session, boolean success) {}

  public Object getAttachment() {
    return attachment;
  }

  public void setAttachment(Object attachment) {
    this.attachment = attachment;
  }
}
