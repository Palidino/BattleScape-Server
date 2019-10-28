package com.palidinodh.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Session {
  private SocketChannel socketChannel;
  private SelectionKey selectionKey;
  private String remoteAddress;
  private long lastRead;
  private Queue<WriteEvent> writeEvents = new ConcurrentLinkedQueue<>();
  private Object attachment;

  public Session(SocketChannel socketChannel, String remoteAddress, SelectionKey selectionKey) {
    this.socketChannel = socketChannel;
    this.remoteAddress = remoteAddress;
    this.selectionKey = selectionKey;
    selectionKey.attach(this);
    updateLastRead();
  }

  public void write(byte[] bytes) {
    write(bytes, 0, bytes.length, null);
  }

  public void write(byte[] bytes, WriteEventHandler handler) {
    write(bytes, 0, bytes.length, handler);
  }

  public void write(byte[] bytes, int offset, int length) {
    write(bytes, 0, bytes.length, null);
  }

  public void write(byte[] bytes, int offset, int length, WriteEventHandler handler) {
    addWriteEvent(new WriteEvent(bytes, offset, length, handler));
  }

  public void write(ByteBuffer buffer) {
    write(buffer, null);
  }

  public void write(ByteBuffer buffer, WriteEventHandler handler) {
    addWriteEvent(new WriteEvent(buffer, handler));
  }

  private void addWriteEvent(WriteEvent writeEvent) {
    writeEvents.offer(writeEvent);
    if (selectionKey.isValid()) {
      selectionKey.selector().wakeup();
    }
  }

  public void close() {
    try {
      socketChannel.close();
    } catch (IOException ioe) {
    }
  }

  public boolean isOpen() {
    return socketChannel.isOpen();
  }

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public long getLastRead() {
    return lastRead;
  }

  public boolean idleTimeout(int timeoutMillis) {
    return timeoutMillis > 0 && System.currentTimeMillis() - lastRead > timeoutMillis;
  }

  public void setAttachment(Object attachment) {
    this.attachment = attachment;
  }

  public Object getAttachment() {
    return attachment;
  }

  SelectionKey getSelectionKey() {
    return selectionKey;
  }

  void updateLastRead() {
    lastRead = System.currentTimeMillis();
  }

  Queue<WriteEvent> getWriteEvents() {
    return writeEvents;
  }
}
