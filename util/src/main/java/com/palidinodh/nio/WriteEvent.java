package com.palidinodh.nio;

import java.nio.ByteBuffer;

public class WriteEvent {
  private ByteBuffer buffer;
  private WriteEventHandler handler;

  public WriteEvent(byte[] original, int offset, int length, WriteEventHandler handler) {
    if (original == null) {
      throw new NullPointerException("original can't be null");
    }
    if (offset < 0 || length < 0) {
      throw new NegativeArraySizeException("offset and length must be greater than 0");
    }
    if (offset > original.length || length + offset > original.length) {
      throw new ArrayIndexOutOfBoundsException(
          "length + offset can't be greater than original.length");
    }
    if (original.length == 0 || length == 0) {
      throw new IllegalArgumentException("length must be greater than 0");
    }
    buffer = ByteBuffer.allocate(length);
    buffer.put(original, offset, length);
    buffer.flip();
    buffer = buffer.asReadOnlyBuffer();
    this.handler = handler;
  }

  public WriteEvent(ByteBuffer original, WriteEventHandler handler) {
    buffer = ByteBuffer.allocate(original.capacity());
    ByteBuffer readOnlyCopy = original.asReadOnlyBuffer();
    readOnlyCopy.flip();
    buffer.put(readOnlyCopy);
    buffer.flip();
    buffer = buffer.asReadOnlyBuffer();
    this.handler = handler;
  }

  ByteBuffer getBuffer() {
    return buffer;
  }

  WriteEventHandler getHandler() {
    return handler;
  }
}
