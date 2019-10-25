package com.palidinodh.random;

import java.util.Random;

public class Xoroshiro128Plus extends Random {
  public static final long serialVersionUID = 20180505L;
  private static final long[] JUMP_VALUES = {0xbeac0467eba5facbL, 0xd86b048b86aa9922L};
  private static final long DOUBLE_MASK = (1L << 53) - 1;
  private static final double NORM_53 = 1D / (1L << 53);
  private static final long FLOAT_MASK = (1L << 24) - 1;
  private static final double NORM_24 = 1D / (1L << 24);

  private long seed1;
  private long seed2;

  @Override
  public void setSeed(long seed) {
    long x = seed;
    long z = x += 0x9E3779B97F4A7C15L;
    z = (z ^ z >>> 30) * 0xBF58476D1CE4E5B9L;
    z = (z ^ z >>> 27) * 0x94D049BB133111EBL;
    seed1 = z ^ z >>> 31;
    z = x + 0x9E3779B97F4A7C15L;
    z = (z ^ z >>> 30) * 0xBF58476D1CE4E5B9L;
    z = (z ^ z >>> 27) * 0x94D049BB133111EBL;
    seed2 = z ^ z >>> 31;
  }

  @Override
  public long nextLong() {
    long s0 = seed1;
    long s1 = seed2;
    long result = s0 + s1;
    s1 ^= s0;
    seed1 = Long.rotateLeft(s0, 55) ^ s1 ^ s1 << 14;
    seed2 = Long.rotateLeft(s1, 36);
    return result;
  }

  @Override
  public int nextInt() {
    return (int) nextLong();
  }

  @Override
  public int nextInt(int bound) {
    if (bound <= 0) {
      return 0;
    }
    int threshold = (0x7fffffff - bound + 1) % bound;
    while (true) {
      int bits = (int) (nextLong() & 0x7fffffff);
      if (bits >= threshold) {
        return bits % bound;
      }
    }
  }

  @Override
  public double nextDouble() {
    return (nextLong() & DOUBLE_MASK) * NORM_53;
  }

  @Override
  public float nextFloat() {
    return (float) ((nextLong() & FLOAT_MASK) * NORM_24);
  }

  @Override
  public boolean nextBoolean() {
    return (nextLong() & 1) != 0L;
  }

  @Override
  public void nextBytes(byte[] bytes) {
    int i = bytes.length;
    int n = 0;
    while (i != 0) {
      n = Math.min(i, 8);
      for (long bits = nextLong(); n-- != 0; bits >>>= 8) {
        bytes[--i] = (byte) bits;
      }
    }
  }

  @Override
  protected int next(int bits) {
    return (int) (nextLong() & (1L << bits) - 1);
  }

  public int nextInt(int lower, int upper) {
    if (upper - lower <= 0) {
      throw new IllegalArgumentException("Upper bound must be greater than lower bound");
    }
    return lower + nextInt(upper - lower);
  }

  public long nextLong(long bound) {
    if (bound <= 0) {
      return 0;
    }
    long threshold = (0x7fffffffffffffffL - bound + 1) % bound;
    while (true) {
      long bits = nextLong() & 0x7fffffffffffffffL;
      if (bits >= threshold) {
        return bits % bound;
      }
    }
  }

  public void jump() {
    long s0 = 0;
    long s1 = 0;
    for (int i = 0; i < 2; i++) {
      for (int b = 0; b < 64; b++) {
        if ((JUMP_VALUES[i] & 1L << b) > 0) {
          s0 ^= seed1;
          s1 ^= seed2;
        }
        nextLong();
      }
    }
    seed1 = s0;
    seed2 = s1;
  }
}
