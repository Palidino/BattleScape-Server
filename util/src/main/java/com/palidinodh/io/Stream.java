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

  public int getMark() {
    return mark;
  }

  public int getPosition() {
    return position;
  }

  public int getBitPosition() {
    return bitPosition;
  }

  public int getLength() {
    return length;
  }

  public byte[] getArray() {
    return buffer;
  }

  public void setPosition(int position) {
    if (position < mark) {
      mark = 0;
    }
    this.position = position;
  }

  public void setLength(int length) {
    if (length < mark) {
      mark = 0;
    }
    this.length = length;
  }

  public int available() {
    return position < length && position < buffer.length ? length - position : 0;
  }

  public byte[] toByteArray() {
    byte[] bytes = new byte[position];
    System.arraycopy(buffer, 0, bytes, 0, position);
    return bytes;
  }

  public void clear() {
    position = 0;
    length = 0;
    mark = 0;
  }

  public void mark() {
    mark = position;
  }

  public void reset() {
    position = mark;
  }

  public void skip(int length) {
    position += length;
  }

  public void checkCapacity(int length) {
    if (length >= buffer.length) {
      byte[] newBuffer = new byte[length * 2];
      System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
      buffer = newBuffer;
    }
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

  public byte getByte(int position) {
    return position >= 0 && position < length ? buffer[position] : 0;
  }

  public Stream setByte(int position, int i) {
    checkCapacity(position);
    buffer[position] = (byte) i;
    return this;
  }

  public int getBitsAvailable(int length) {
    return length * 8 - position;
  }

  public Stream writeByte(int i) {
    setByte(position++, i);
    return this;
  }

  public Stream writeByteA(int i) {
    writeByte(i + 128);
    return this;
  }

  public Stream writeByteS(int i) {
    writeByte(128 - i);
    return this;
  }

  public Stream writeByteC(int i) {
    writeByte(-i);
    return this;
  }

  public Stream writeBoolean(boolean bool) {
    writeByte(bool ? 1 : 0);
    return this;
  }

  public Stream writeBytes(byte[] b) {
    writeBytes(b, 0, b.length);
    return this;
  }

  public Stream writeBytes(byte[] b, int position, int count) {
    checkCapacity(this.position + count);
    System.arraycopy(b, position, buffer, this.position, count);
    this.position += count;
    return this;
  }

  public Stream writeBytesReversed(byte[] b) {
    writeBytesReversed(b, 0, b.length);
    return this;
  }

  public Stream writeBytesReversed(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      writeByte(b[i]);
    }
    return this;
  }

  public Stream writeBytesReversedA(byte[] b) {
    writeBytesReversedA(b, 0, b.length);
    return this;
  }

  public Stream writeBytesReversedA(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      writeByte(b[i] + 128);
    }
    return this;
  }

  public Stream writeBytesA(byte[] b) {
    writeBytesA(b, 0, b.length);
    return this;
  }

  public Stream writeBytesA(byte[] b, int position, int count) {
    for (int i = position; i < count + position; i++) {
      writeByteA(b[i]);
    }
    return this;
  }

  public Stream writeShort(int i) {
    writeByte(i >> 8);
    writeByte(i);
    return this;
  }

  public Stream writeLEShort(int i) {
    writeByte(i);
    writeByte(i >> 8);
    return this;
  }

  public Stream writeShortA(int i) {
    writeByte(i >> 8);
    writeByte(i + 128);
    return this;
  }

  public Stream writeLEShortA(int i) {
    writeByte(i + 128);
    writeByte(i >> 8);
    return this;
  }

  public Stream writeSmart(int i) {
    if (i >= 0 && i < 128) {
      writeByte(i);
    } else if (i >= 0 && i < 32768) {
      writeShort(i + 32768);
    } else {
      throw new IllegalArgumentException();
    }
    return this;
  }

  public Stream writeBigSmart(int i) {
    if (i >= Short.MAX_VALUE) {
      writeInt(i - Integer.MAX_VALUE - 1);
    } else {
      writeShort(i >= 0 ? i : 32767);
    }
    return this;
  }

  public Stream writeHugeSmart(int i) {
    if (i < 32767) {
      writeSmart(i);
    } else {
      int divisions = i / 32767;
      for (int i2 = 0; i2 < divisions; i2++) {
        writeSmart(32767);
      }
      writeSmart(i - 32767 * divisions);
    }
    return this;
  }

  public Stream writeTriByte(int i) {
    writeByte(i >> 16);
    writeByte(i >> 8);
    writeByte(i);
    return this;
  }

  public Stream writeLETriByte(int i) {
    writeByte(i);
    writeByte(i >> 8);
    writeByte(i >> 16);
    return this;
  }

  public Stream writeTriByte1(int i) {
    writeByte(i >> 8);
    writeByte(i >> 16);
    writeByte(i);
    return this;
  }

  public Stream writeInt(int i) {
    writeByte(i >> 24);
    writeByte(i >> 16);
    writeByte(i >> 8);
    writeByte(i);
    return this;
  }

  public Stream writeLEInt(int i) {
    writeByte(i);
    writeByte(i >> 8);
    writeByte(i >> 16);
    writeByte(i >> 24);
    return this;
  }

  public Stream writeInt1(int i) {
    writeByte(i >> 8);
    writeByte(i);
    writeByte(i >> 24);
    writeByte(i >> 16);
    return this;
  }

  public Stream writeInt2(int i) {
    writeByte(i >> 16);
    writeByte(i >> 24);
    writeByte(i);
    writeByte(i >> 8);
    return this;
  }

  public Stream writePentaByte(int i) {
    writeByte(i >> 32);
    writeByte(i >> 24);
    writeByte(i >> 16);
    writeByte(i >> 8);
    writeByte(i);
    return this;
  }

  public Stream writeHexaByte(int i) {
    writeByte(i >> 40);
    writeByte(i >> 32);
    writeByte(i >> 24);
    writeByte(i >> 16);
    writeByte(i >> 8);
    writeByte(i);
    return this;
  }

  public Stream writeDouble(Double d) {
    writeLong(Double.doubleToLongBits(d));
    return this;
  }

  public Stream writeLong(long l) {
    writeByte((int) (l >> 56));
    writeByte((int) (l >> 48));
    writeByte((int) (l >> 40));
    writeByte((int) (l >> 32));
    writeByte((int) (l >> 24));
    writeByte((int) (l >> 16));
    writeByte((int) (l >> 8));
    writeByte((int) l);
    return this;
  }

  public Stream writeString(String s) {
    if (s.length() > MAX_STRING_LENGTH) {
      s = s.substring(0, MAX_STRING_LENGTH);
    }
    checkCapacity(position + s.length() + 1);
    System.arraycopy(s.getBytes(), 0, buffer, position, s.length());
    position += s.length();
    writeByte(0);
    return this;
  }

  public Stream WriteJString(String s) {
    writeByte(0);
    writeString(s);
    return this;
  }

  public Stream writeOpcode(int id) {
    writeEncryptedByte(id);
    return this;
  }

  public Stream writeOpcodeVarByte(int id) {
    writeOpcode(id);
    writeByte(0);
    opcodeStart = position - 1;
    return this;
  }

  public Stream writeOpcodeVarShort(int id) {
    writeOpcode(id);
    writeShort(0);
    opcodeStart = position - 2;
    return this;
  }

  public Stream writeOpcodeVarInt(int id) {
    writeOpcode(id);
    writeInt(0);
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

  public Stream writeEncryptedByte(int id) {
    writeByte(id);
    return this;
  }

  public Stream writeEncryptedBytes(byte[] b) {
    writeEncryptedBytes(b, 0, b.length);
    return this;
  }

  public Stream writeEncryptedBytes(byte[] b, int position, int count) {
    checkCapacity(this.position + count);
    for (byte aByte : b) {
      writeEncryptedByte(aByte);
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

  public Stream writeBits(int numBits, int value) {
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

  public Stream writeScript(Map<Integer, Object> script) {
    writeByte(script.size());
    for (Map.Entry<Integer, Object> entry : script.entrySet()) {
      writeByte(entry.getValue() instanceof String ? 1 : 0);
      writeTriByte(entry.getKey());
      if (entry.getValue() instanceof String) {
        writeString((String) entry.getValue());
      } else {
        writeInt(entry.getValue() instanceof Integer ? (int) entry.getValue() : 0);
      }
    }
    return this;
  }

  public void readBytes(byte[] b) {
    readBytes(b, 0, b.length);
  }

  public void readBytes(byte[] b, int position, int count) {
    System.arraycopy(buffer, this.position, b, position, count);
    this.position += count;
  }

  public void readBytesA(byte[] b, int position, int count) {
    for (int i = position; i < count + position; i++) {
      b[i] = readByteA();
    }
  }

  public void readBytesReversed(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      b[i] = readByte();
    }
  }

  public void readBytesReversedA(byte[] b, int position, int count) {
    for (int i = count + position - 1; i >= position; i--) {
      b[i] = readByteA();
    }
  }

  public byte readByte() {
    return getByte(position++);
  }

  public int readUnsignedByte() {
    return readByte() & 255;
  }

  public byte readByteA() {
    return (byte) (readByte() - 128);
  }

  public int readUnsignedByteA() {
    return readUnsignedByte() - 128 & 255;
  }

  public byte readByteC() {
    return (byte) -readByte();
  }

  public int readUnsignedByteC() {
    return -readUnsignedByte() & 255;
  }

  public byte readByteS() {
    return (byte) (128 - readByte());
  }

  public int readUnsignedByteS() {
    return 128 - readUnsignedByte() & 255;
  }

  public boolean readBoolean() {
    return (readUnsignedByte() & 1) == 1;
  }

  public short readShort() {
    int i = (readUnsignedByte() << 8) + readUnsignedByte();
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int readUnsignedShort() {
    return (readUnsignedByte() << 8) + readUnsignedByte();
  }

  public short readLEShort() {
    int i = readUnsignedByte() + (readUnsignedByte() << 8);
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int readUnsignedLEShort() {
    return readUnsignedByte() + (readUnsignedByte() << 8);
  }

  public short readShortA() {
    int i = (readUnsignedByte() << 8) + (readByte() - 128 & 255);
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int readUnsignedShortA() {
    return (readUnsignedByte() << 8) + (readByte() - 128 & 255);
  }

  public short readLEShortA() {
    int i = (readByte() - 128 & 255) + (readUnsignedByte() << 8);
    if (i > 32767) {
      i -= 65536;
    }
    return (short) i;
  }

  public int readUnsignedLEShortA() {
    return (readByte() - 128 & 255) + (readUnsignedByte() << 8);
  }

  public int readSmart() {
    int i = available() > 0 ? buffer[position] & 255 : 0;
    return i < 128 ? readUnsignedByte() - 64 : readUnsignedShort() - 49152;
  }

  public int readUnsignedSmart() {
    int i = available() > 0 ? buffer[position] & 255 : 0;
    return i < 128 ? readUnsignedByte() : readUnsignedShort() - 32768;
  }

  public int readBigSmart() {
    int i = available() > 0 ? buffer[position] : 0;
    if (i < 0) {
      return readInt() & Integer.MAX_VALUE;
    }
    i = readUnsignedShort();
    return i == 32767 ? -1 : i;
  }

  public int readUnsignedBigSmart() {
    return (available() > 0 ? buffer[position] : 0) < 0 ? readInt() & Integer.MAX_VALUE
        : readUnsignedShort();
  }

  public int readHugeSmart() {
    int baseVal = 0;
    int lastVal = 0;
    while ((lastVal = readUnsignedSmart()) == 32767) {
      baseVal += 32767;
    }
    return baseVal + lastVal;
  }

  public int readTriByte() {
    return (readUnsignedByte() << 16) + (readUnsignedByte() << 8) + readUnsignedByte();
  }

  public int readLETriByte() {
    return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16);
  }

  public int readTriByte1() {
    return (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + readUnsignedByte();
  }

  public int readInt() {
    return (readUnsignedByte() << 24) + (readUnsignedByte() << 16) + (readUnsignedByte() << 8)
        + readUnsignedByte();
  }

  public int readLEInt() {
    return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16)
        + (readUnsignedByte() << 24);
  }

  public int readInt1() {
    return (readUnsignedByte() << 8) + readUnsignedByte() + (readUnsignedByte() << 24)
        + (readUnsignedByte() << 16);
  }

  public int readInt2() {
    return (readUnsignedByte() << 16) + (readUnsignedByte() << 24) + readUnsignedByte()
        + (readUnsignedByte() << 8);
  }

  public int readPentaByte() {
    return (readUnsignedByte() << 32) + (readUnsignedByte() << 24) + (readUnsignedByte() << 16)
        + (readUnsignedByte() << 8) + readUnsignedByte();
  }

  public int readHexaByte() {
    return (readUnsignedByte() << 40) + (readUnsignedByte() << 32) + (readUnsignedByte() << 24)
        + (readUnsignedByte() << 16) + (readUnsignedByte() << 8) + readUnsignedByte();
  }

  public long readLong() {
    long l = readInt() & 0xffffffffL;
    long l1 = readInt() & 0xffffffffL;
    return (l << 32) + l1;
  }

  public String readString() {
    int aChar;
    StringBuilder stringBuilder = new StringBuilder();
    while ((aChar = readByte()) != 0 && stringBuilder.length() < MAX_STRING_LENGTH) {
      stringBuilder.append((char) aChar);
    }
    return stringBuilder.toString();
  }

  public String readJString() {
    readByte();
    int aChar;
    StringBuilder stringBuilder = new StringBuilder();
    while ((aChar = readByte()) != 0 && stringBuilder.length() < MAX_STRING_LENGTH) {
      stringBuilder.append((char) aChar);
    }
    return stringBuilder.toString();
  }

  public String readSpecialString() {
    int start = position;
    int length = 0;
    while (readByte() != 0 && length < MAX_STRING_LENGTH) {
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

  public int readOpcode() {
    return readUnsignedByte();
  }

  public int readBits(int size) {
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

  public byte[] decodeRsa(BigInteger exponent, BigInteger modulus) {
    byte[] data = new byte[readShort()];
    readBytes(data);
    if (exponent == null || modulus == null) {
      return new BigInteger(data).toByteArray();
    }
    return new BigInteger(data).modPow(exponent, modulus).toByteArray();
  }

  public void decodeXtea(int[] keys, int start, int end) {
    int l = position;
    position = start;
    int i1 = (end - start) / 8;
    for (int j1 = 0; j1 < i1; j1++) {
      int k1 = readInt();
      int l1 = readInt();
      int sum = 0xc6ef3720;
      int delta = 0x9e3779b9;
      for (int k2 = 32; k2-- > 0;) {
        l1 -= keys[(sum & 0x1c84) >>> 11] + sum ^ (k1 >>> 5 ^ k1 << 4) + k1;
        sum -= delta;
        k1 -= (l1 >>> 5 ^ l1 << 4) + l1 ^ keys[sum & 3] + sum;
      }
      position -= 8;
      writeInt(k1);
      writeInt(l1);
    }
    position = l;
  }

  public void encodeXtea(int[] keys, int start, int end) {
    int o = position;
    int j = (end - start) / 8;
    position = start;
    for (int k = 0; k < j; k++) {
      int l = readInt();
      int i1 = readInt();
      int sum = 0;
      int delta = 0x9e3779b9;
      for (int l1 = 32; l1-- > 0;) {
        l += sum + keys[3 & sum] ^ i1 + (i1 >>> 5 ^ i1 << 4);
        sum += delta;
        i1 += l + (l >>> 5 ^ l << 4) ^ keys[(0x1eec & sum) >>> 11] + sum;
      }
      position -= 8;
      writeInt(l);
      writeInt(i1);
    }
    position = o;
  }

  public Map<Integer, Object> readScript() {
    int size = readUnsignedByte();
    Map<Integer, Object> script = new HashMap<>();
    for (int i = 0; i < size; i++) {
      boolean bool = readUnsignedByte() == 1;
      int key = readTriByte();
      Object value = bool ? readString() : readInt();
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
