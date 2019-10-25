package com.palidinodh.random;

import java.util.Random;

public class NumericalRecipesRandom extends Random {
  public static final long serialVersionUID = 4294957665L;

  private long u;
  private long v = 4101842887655102017L;
  private long w = 1;

  public NumericalRecipesRandom() {
    this(System.nanoTime());
  }

  public NumericalRecipesRandom(long seed) {
    u = seed ^ v;
    nextLong();
    v = u;
    nextLong();
    w = v;
    nextLong();
  }

  @Override
  public long nextLong() {
    u = u * 2862933555777941757L + 7046029254386353087L;
    v ^= v >>> 17;
    v ^= v << 31;
    v ^= v >>> 8;
    w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
    long x = u ^ u << 21;
    x ^= x >>> 35;
    x ^= x << 4;
    long ret = x + v ^ w;
    return ret;
  }

  @Override
  protected int next(int bits) {
    return (int) (nextLong() >>> 64 - bits);
  }
}
