package com.palidinodh.io;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Stream {
  private static final int[] BIT_MASK = new int[32];
  public static final int MAX_STRING_LENGTH = 4096;
  public static final char[] SPECIAL_CHARACTERS = {'\u20ac', '\u0000', '\u201a', '\u0192', '\u201e',
      '\u2026', '\u2020', '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\u0000',
      '\u017d', '\u0000', '\u0000', '\u2018', '\u2019', '\u201c', '\u201d', '\u2022', '\u2013',
      '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\u0000', '\u017e', '\u0178'};

  private int position;
  private int length;
  private byte[] buffer;
  private int bitPosition;
  private int opcodeStart;
  private int mark;

  public Stream(int initialCapacity) {
    buffer = new byte[initialCapacity];
  }

  public Stream(byte[] buffer) {
    this.buffer = buffer;
    length = buffer.length;
  }

  public void mark() {
    mark = position;
  }

  public int getMark() {
    return mark;
  }

  public void reset() {
    position = mark;
  }

  public void checkCapacity(int length) {
    if (length >= buffer.length) {
      byte[] newBuffer = new byte[length * 2];
      System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
      buffer = newBuffer;
    }
  }

  public void skip(int length) {
    position += length;
  }

  public byte[] toByteArray() {
    byte[] bytes = new byte[position];
    System.arraycopy(buffer, 0, bytes, 0, position);
    return bytes;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    if (position < mark) {
      mark = 0;
    }
    this.position = position;
  }

  public int getBitPosition() {
    return bitPosition;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    if (length < mark) {
      mark = 0;
    }
    this.length = length;
  }

  public byte[] getArray() {
    return buffer;
  }

  public Stream addByte(int i) {
    setByte(position++, i);
    return this;
  }

  public Stream setByte(int position, int i) {
    checkCapacity(position);
    buffer[position] = (byte) i;
    return this;
  }

  public Stream addByte128(int i) {
    addByte(i + 128);
    return this;
  }

  public Stream add128Byte(int i) {
    addByte(128 - i);
    return this;
  }

  public Stream addReversedByte(int i) {
    addByte(-i);
    return this;
  }

  public Stream addBoolean(boolean bool) {
    addByte(bool ? 1 : 0);
    return this;
  }

  public Stream addBytes(byte[] b) {
    addBytes(b, 0, b.length);
    return this;
  }

  public Stream addBytes(byte[] b, int position, int count) {
    checkCapacity(this.position + count);
    System.arraycopy(b, position, buffer, this.position, count);
    this.position += count;
    return this;
  }

  public Stream addReversedBytes(byte[] b) {
    addReversedBytes(b, 0, b.length);
    return this;
  }

  public Stream addReversedBytes(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      addByte(b[i]);
    }
    return this;
  }

  public Stream addReversedBytes128(byte[] b) {
    addReversedBytes128(b, 0, b.length);
    return this;
  }

  public Stream addReversedBytes128(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      addByte(b[i] + 128);
    }
    return this;
  }

  public Stream addBytes128(byte[] b) {
    addBytes128(b, 0, b.length);
    return this;
  }

  public Stream addBytes128(byte[] b, int position, int count) {
    for (int i = position; i < count + position; i++) {
      addByte128(b[i]);
    }
    return this;
  }

  public Stream addShort(int i) {
    addByte(i >> 8);
    addByte(i);
    return this;
  }

  public Stream addShortLE(int i) {
    addByte(i);
    addByte(i >> 8);
    return this;
  }

  public Stream addShort128(int i) {
    addByte(i >> 8);
    addByte(i + 128);
    return this;
  }

  public Stream addShortLE128(int i) {
    addByte(i + 128);
    addByte(i >> 8);
    return this;
  }

  public Stream addSmart(int i) {
    if (i >= 0 && i < 128) {
      addByte(i);
    } else if (i >= 0 && i < 32768) {
      addShort(i + 32768);
    } else {
      throw new IllegalArgumentException();
    }
    return this;
  }

  public Stream addBigSmart(int i) {
    if (i >= Short.MAX_VALUE) {
      addInt(i - Integer.MAX_VALUE - 1);
    } else {
      addShort(i >= 0 ? i : 32767);
    }
    return this;
  }

  public Stream addHugeSmart(int i) {
    if (i < 32767) {
      addSmart(i);
    } else {
      int divisions = i / 32767;
      for (int i2 = 0; i2 < divisions; i2++) {
        addSmart(32767);
      }
      addSmart(i - 32767 * divisions);
    }
    return this;
  }

  public Stream add24Int(int i) {
    addByte(i >> 16);
    addByte(i >> 8);
    addByte(i);
    return this;
  }

  public Stream addInt(int i) {
    addByte(i >> 24);
    addByte(i >> 16);
    addByte(i >> 8);
    addByte(i);
    return this;
  }

  public Stream addIntLE(int i) {
    addByte(i);
    addByte(i >> 8);
    addByte(i >> 16);
    addByte(i >> 24);
    return this;
  }

  public Stream addIntV2(int i) {
    addByte(i >> 8);
    addByte(i);
    addByte(i >> 24);
    addByte(i >> 16);
    return this;
  }

  public Stream addIntV3(int i) {
    addByte(i >> 16);
    addByte(i >> 24);
    addByte(i);
    addByte(i >> 8);
    return this;
  }

  public Stream add40Int(int i) {
    addByte(i >> 32);
    addByte(i >> 24);
    addByte(i >> 16);
    addByte(i >> 8);
    addByte(i);
    return this;
  }

  public Stream add48Int(int i) {
    addByte(i >> 40);
    addByte(i >> 32);
    addByte(i >> 24);
    addByte(i >> 16);
    addByte(i >> 8);
    addByte(i);
    return this;
  }

  public Stream addDouble(Double d) {
    addLong(Double.doubleToLongBits(d));
    return this;
  }

  public Stream addLong(long l) {
    addByte((int) (l >> 56));
    addByte((int) (l >> 48));
    addByte((int) (l >> 40));
    addByte((int) (l >> 32));
    addByte((int) (l >> 24));
    addByte((int) (l >> 16));
    addByte((int) (l >> 8));
    addByte((int) l);
    return this;
  }

  public Stream addString(String s) {
    if (s.length() > MAX_STRING_LENGTH) {
      s = s.substring(0, MAX_STRING_LENGTH);
    }
    checkCapacity(position + s.length() + 1);
    System.arraycopy(s.getBytes(), 0, buffer, position, s.length());
    position += s.length();
    addByte(0);
    return this;
  }

  public Stream addJString(String s) {
    addByte(0);
    addString(s);
    return this;
  }

  public Stream addOpcode(int id) {
    addEncryptedByte(id);
    return this;
  }

  public Stream addOpcodeVarByte(int id) {
    addOpcode(id);
    addByte(0);
    opcodeStart = position - 1;
    return this;
  }

  public Stream addOpcodeVarShort(int id) {
    addOpcode(id);
    addShort(0);
    opcodeStart = position - 2;
    return this;
  }

  public Stream addOpcodeVarInt(int id) {
    addOpcode(id);
    addInt(0);
    opcodeStart = position - 4;
    return this;
  }

  public Stream endOpcodeVarByte() {
    int size = position - (opcodeStart + 1);
    if (size > 255) {
      throw new IndexOutOfBoundsException();
    }
    setByte(opcodeStart, size);
    return this;
  }

  public Stream endOpcodeVarShort() {
    int size = position - (opcodeStart + 2);
    if (size > 65535) {
      throw new IndexOutOfBoundsException();
    }
    setByte(opcodeStart++, size >> 8);
    setByte(opcodeStart, size);
    return this;
  }

  public Stream endOpcodeVarInt() {
    int size = position - (opcodeStart + 4);
    if (size < 0) {
      throw new IndexOutOfBoundsException();
    }
    setByte(opcodeStart++, size >> 24);
    setByte(opcodeStart++, size >> 16);
    setByte(opcodeStart++, size >> 8);
    setByte(opcodeStart, size);
    return this;
  }

  public Stream addEncryptedByte(int id) {
    addByte(id);
    return this;
  }

  public Stream addEncryptedBytes(byte[] b) {
    addEncryptedBytes(b, 0, b.length);
    return this;
  }

  public Stream addEncryptedBytes(byte[] b, int position, int count) {
    checkCapacity(this.position + count);
    for (byte aByte : b) {
      addEncryptedByte(aByte);
    }
    return this;
  }

  public Stream startBitAccess() {
    bitPosition = position * 8;
    return this;
  }

  public Stream endBitAccess() {
    position = (bitPosition + 7) / 8;
    return this;
  }

  public Stream addBits(int numBits, int value) {
    int bytePos = bitPosition >> 3;
    int bitposition = 8 - (bitPosition & 7);
    bitPosition += numBits;
    for (; numBits > bitposition; bitposition = 8) {
      checkCapacity(bytePos);
      buffer[bytePos] &= ~BIT_MASK[bitposition];
      buffer[bytePos++] |= value >> numBits - bitposition & BIT_MASK[bitposition];
      numBits -= bitposition;
    }
    checkCapacity(bytePos);
    if (numBits == bitposition) {
      buffer[bytePos] &= ~BIT_MASK[bitposition];
      buffer[bytePos] |= value & BIT_MASK[bitposition];
    } else {
      buffer[bytePos] &= ~(BIT_MASK[numBits] << bitposition - numBits);
      buffer[bytePos] |= (value & BIT_MASK[numBits]) << bitposition - numBits;
    }
    return this;
  }

  public Stream addScript(Map<Integer, Object> script) {
    addByte(script.size());
    for (Map.Entry<Integer, Object> entry : script.entrySet()) {
      addByte(entry.getValue() instanceof String ? 1 : 0);
      add24Int(entry.getKey());
      if (entry.getValue() instanceof String) {
        addString((String) entry.getValue());
      } else {
        addInt(entry.getValue() instanceof Integer ? (int) entry.getValue() : 0);
      }
    }
    return this;
  }

  public int available() {
    return position < length && position < buffer.length ? length - position : 0;
  }

  public void clear() {
    position = 0;
    length = 0;
    mark = 0;
  }

  public void appendBytes(byte[] b) {
    appendBytes(b, 0, b.length);
  }

  public void appendBytes(byte[] b, int position, int count) {
    checkCapacity(length + count);
    System.arraycopy(b, position, buffer, length, count);
    length += count;
  }

  public void appendBytes(int index, byte[] b) {
    appendBytes(index, b, 0, b.length);
  }

  public void appendBytes(int index, byte[] b, int position, int count) {
    if (index < 0 || index > length) {
      return;
    }
    checkCapacity(length + count);
    int numMoved = length - index;
    if (numMoved > 0) {
      System.arraycopy(buffer, index, buffer, index + count, numMoved);
    }
    System.arraycopy(b, position, buffer, index, count);
    length += count;
  }


  public void getBytes(byte[] b) {
    getBytes(b, 0, b.length);
  }

  public void getBytes(byte[] b, int position, int count) {
    System.arraycopy(buffer, this.position, b, position, count);
    this.position += count;
  }

  public void getReversedBytes(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      b[i] = getByte();
    }
  }

  public void getReversedBytes128(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      b[i] = getByte128();
    }
  }

  public void getBytes128(byte[] b, int position, int count) {
    for (int i = position; i < count + position; i++) {
      b[i] = getByte128();
    }
  }

  public byte getByte(int position) {
    return position >= 0 && position < length ? buffer[position] : 0;
  }

  public byte getByte() {
    return getByte(position++);
  }

  public int getUByte() {
    return getByte() & 255;
  }

  public byte getByte128() {
    return (byte) (getByte() - 128);
  }

  public int getUByte128() {
    return getUByte() - 128 & 255;
  }

  public byte getReversedByte() {
    return (byte) -getByte();
  }

  public int getUReversedByte() {
    return -getUByte() & 255;
  }

  public byte get128Byte() {
    return (byte) (128 - getByte());
  }

  public int getU128Byte() {
    return 128 - getUByte() & 255;
  }

  public boolean getBoolean() {
    return (getUByte() & 1) == 1;
  }

  public short getShort() {
    int i = (getUByte() << 8) + getUByte();
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int getUShort() {
    return (getUByte() << 8) + getUByte();
  }

  public short getShortLE() {
    int i = getUByte() + (getUByte() << 8);
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int getUShortLE() {
    return getUByte() + (getUByte() << 8);
  }

  public short getShort128() {
    int i = (getUByte() << 8) + (getByte() - 128 & 255);
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int getUShort128() {
    return (getUByte() << 8) + (getByte() - 128 & 255);
  }

  public short getShortLE128() {
    int i = (getByte() - 128 & 255) + (getUByte() << 8);
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int getUShortLE128() {
    return (getByte() - 128 & 255) + (getUByte() << 8);
  }

  public int getSmart() {
    int i = available() > 0 ? buffer[position] & 255 : 0;
    if (i < 128) {
      return getUByte() - 64;
    }
    return getUShort() - 49152;
  }

  public int getUSmart() {
    int i = available() > 0 ? buffer[position] & 255 : 0;
    if (i < 128) {
      return getUByte();
    } else {
      return getUShort() - 32768;
    }
  }

  public int getBigSmart() {
    int i = available() > 0 ? buffer[position] : 0;
    if (i < 0) {
      return getInt() & 0x7fffffff;
    }
    i = getUShort();
    if (i == 32767) {
      return -1;
    }
    return i;
  }

  public int getUBigSmart() {
    return (available() > 0 ? buffer[position] : 0) < 0 ? getInt() & Integer.MAX_VALUE
        : getUShort();
  }

  public int getHugeSmart() {
    int baseVal = 0;
    int lastVal = 0;
    while ((lastVal = getUSmart()) == 32767) {
      baseVal += 32767;
    }
    return baseVal + lastVal;
  }

  public int get24Int() {
    return (getUByte() << 16) + (getUByte() << 8) + getUByte();
  }

  public int get24IntLE() {
    return getUByte() + (getUByte() << 8) + (getUByte() << 16);
  }

  public int getInt() {
    return (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8) + getUByte();
  }

  public int getIntLE() {
    return getUByte() + (getUByte() << 8) + (getUByte() << 16) + (getUByte() << 24);
  }

  public int getIntV2() {
    return (getUByte() << 8) + getUByte() + (getUByte() << 24) + (getUByte() << 16);
  }

  public int getIntV3() {
    return (getUByte() << 16) + (getUByte() << 24) + getUByte() + (getUByte() << 8);
  }

  public int get40Int() {
    return (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8)
        + getUByte();
  }

  public int get48Int() {
    return (getUByte() << 40) + (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16)
        + (getUByte() << 8) + getUByte();
  }

  public long getLong() {
    long l = getInt() & 0xffffffffL;
    long l1 = getInt() & 0xffffffffL;
    return (l << 32) + l1;
  }

  public String getString() {
    int aChar;
    StringBuilder stringBuilder = new StringBuilder();
    while ((aChar = getByte()) != 0 && stringBuilder.length() < MAX_STRING_LENGTH) {
      stringBuilder.append((char) aChar);
    }
    return stringBuilder.toString();
  }

  public String getJString() {
    getByte();
    int aChar;
    StringBuilder stringBuilder = new StringBuilder();
    while ((aChar = getByte()) != 0 && stringBuilder.length() < MAX_STRING_LENGTH) {
      stringBuilder.append((char) aChar);
    }
    return stringBuilder.toString();
  }

  public String getSpecialString() {
    int start = position;
    int length = 0;
    while (getByte() != 0 && length < MAX_STRING_LENGTH) {
      length++;
    }
    if (length == 0) {
      return "";
    }
    char[] chars = new char[length];
    int charPos = 0;
    for (int i = 0; i < length; i++) {
      int charByte = buffer[start + i] & 255;
      if (charByte != 0) {
        if (charByte >= 128 && charByte < 160) {
          char specialChar = SPECIAL_CHARACTERS[charByte - 128];
          if (specialChar == 0) {
            specialChar = 63;
          }
          charByte = specialChar;
        }
        chars[charPos++] = (char) charByte;
      }
    }
    return new String(chars, 0, charPos);
  }

  public int getOpcode() {
    return getUByte();
  }

  public int getBitsAvailable(int length) {
    return length * 8 - position;
  }

  public int getBits(int size) {
    int bytePos = position >> 3;
    int bitposition = 8 - (position & 7);
    int value = 0;
    for (position += size; size > bitposition; bitposition = 8) {
      value += (getByte(bytePos++) & BIT_MASK[bitposition]) << size - bitposition;
      size -= bitposition;
    }
    if (bitposition == size) {
      value += getByte(bytePos) & BIT_MASK[bitposition];
    } else {
      value += getByte(bytePos) >> bitposition - size & BIT_MASK[size];
    }
    return value;
  }

  public byte[] decodeRSA(BigInteger exponent, BigInteger modulus) {
    byte[] data = new byte[getShort()];
    getBytes(data);
    if (exponent == null || modulus == null) {
      return new BigInteger(data).toByteArray();
    }
    return new BigInteger(data).modPow(exponent, modulus).toByteArray();
  }

  public void decodeXTEA(int[] keys, int start, int end) {
    int l = position;
    position = start;
    int i1 = (end - start) / 8;
    for (int j1 = 0; j1 < i1; j1++) {
      int k1 = getInt();
      int l1 = getInt();
      int sum = 0xc6ef3720;
      int delta = 0x9e3779b9;
      for (int k2 = 32; k2-- > 0;) {
        l1 -= keys[(sum & 0x1c84) >>> 11] + sum ^ (k1 >>> 5 ^ k1 << 4) + k1;
        sum -= delta;
        k1 -= (l1 >>> 5 ^ l1 << 4) + l1 ^ keys[sum & 3] + sum;
      }
      position -= 8;
      addInt(k1);
      addInt(l1);
    }
    position = l;
  }

  public Map<Integer, Object> getScript() {
    int size = getUByte();
    Map<Integer, Object> script = new HashMap<>();
    for (int i = 0; i < size; i++) {
      boolean bool = getUByte() == 1;
      int key = get24Int();
      Object value = bool ? getString() : getInt();
      script.put(key, value);
    }
    return script;
  }

  static {
    for (int i = 0; i < 32; i++) {
      BIT_MASK[i] = (1 << i) - 1;
    }
  }
}
