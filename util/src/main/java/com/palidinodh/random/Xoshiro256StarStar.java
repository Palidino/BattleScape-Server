/*
 * To the extent possible under law, the author has dedicated all copyright and related and
 * neighboring rights to this software to the public domain worldwide. This software is distributed
 * without any warranty.
 *
 * See <http://creativecommons.org/publicdomain/zero/1.0/>
 */

package com.palidinodh.random;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of Random based on the xoshiro256** RNG. No-dependencies Java port of the
 * <a href="http://xoshiro.di.unimi.it/xoshiro256starstar.c">original C code</a>, which is public
 * domain. This Java port is similarly dedicated to the public domain.
 * <p>
 * Individual instances are not thread-safe. Each thread must have its own instance which is not
 * shared.
 *
 * @see <a href="http://xoshiro.di.unimi.it/">http://xoshiro.di.unimi.it/</a>
 * @author David Blackman and Sebastiano Vigna &lt;vigna@acm.org> (original C code)
 * @author Una Thompson &lt;una@unascribed.com> (Java port)
 */

public class Xoshiro256StarStar extends Random {
  private static final long serialVersionUID = -2837799889588687855L;
  private static final AtomicLong uniq = new AtomicLong(System.nanoTime());

  private final long[] s = new long[4];

  public Xoshiro256StarStar() {
    this(System.nanoTime() ^ nextUniq());
  }

  public Xoshiro256StarStar(long seed) {
    super(seed);
    setSeed(seed);
  }

  @Override
  public void setSeed(long seed) {
    if (s == null) {
      return;
    }
    super.setSeed(seed);
    long sms = splitmix64_1(seed);
    s[0] = splitmix64_2(sms);
    sms = splitmix64_1(sms);
    s[1] = splitmix64_2(sms);
    sms = splitmix64_1(sms);
    s[2] = splitmix64_2(sms);
    sms = splitmix64_1(sms);
    s[3] = splitmix64_2(sms);
  }

  @Override
  protected int next(int bits) {
    return (int) (nextLong() & (1L << bits) - 1);
  }

  @Override
  public int nextInt() {
    return (int) nextLong();
  }

  @Override
  public int nextInt(int bound) {
    return (int) nextLong(bound);
  }

  public long nextLong(long bound) {
    if (bound <= 0) {
      throw new IllegalArgumentException("bound must be positive: " + bound);
    }
    return (nextLong() & Long.MAX_VALUE) % bound;
  }

  @Override
  public double nextDouble() {
    return (nextLong() >>> 11) * 0x1.0P-53;
  }

  @Override
  public float nextFloat() {
    return (nextLong() >>> 40) * 0x1.0P-24F;
  }

  @Override
  public boolean nextBoolean() {
    return (nextLong() & 1) != 0;
  }

  @Override
  public void nextBytes(byte[] bytes) {
    int j = 8;
    long l = 0;
    for (int i = 0; i < bytes.length; i++) {
      if (j >= 8) {
        l = nextLong();
        j = 0;
      }
      bytes[i] = (byte) (l & 0xFF);
      l = l >>> 8L;
      j++;
    }
  }

  private static long rotl(long x, int k) {
    return x << k | x >>> 64 - k;
  }

  @Override
  public long nextLong() {
    long result_starstar = rotl(s[1] * 5, 7) * 9;
    long t = s[1] << 17;
    s[2] ^= s[0];
    s[3] ^= s[1];
    s[1] ^= s[2];
    s[0] ^= s[3];
    s[2] ^= t;
    s[3] = rotl(s[3], 45);
    return result_starstar;
  }

  private static final long nextUniq() {
    return splitmix64_2(uniq.addAndGet(0x9E3779B97F4A7C15L));
  }

  private static long splitmix64_1(long x) {
    return x + 0x9E3779B97F4A7C15L;
  }

  private static long splitmix64_2(long z) {
    z = (z ^ z >> 30) * 0xBF58476D1CE4E5B9L;
    z = (z ^ z >> 27) * 0x94D049BB133111EBL;
    return z ^ z >> 31;
  }
}
