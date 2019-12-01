package com.palidinodh.random;

import java.util.List;
import com.palidinodh.util.PNumber;

public class PRandom {
  private static final Xoshiro256StarStar RANDOM_GEN = new Xoshiro256StarStar();

  public static <T> T listRandom(List<T> list) {
    return list.get(randomE(list.size()));
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T> T arrayRandom(T... values) {
    if (values == null || values.length == 0) {
      return null;
    }
    if (values.length == 1 && values[0] instanceof Object[]) {
      values = (T[]) values[0];
    }
    return values[randomE(values.length)];
  }

  public static int randomE(int bound) { // exclusive
    synchronized (RANDOM_GEN) {
      return RANDOM_GEN.nextInt(bound);
    }
  }

  public static int randomI(int bound) { // inclusive
    synchronized (RANDOM_GEN) {
      return RANDOM_GEN.nextInt(bound + 1);
    }
  }

  public static long nextLongE(long bound) { // exclusive
    synchronized (RANDOM_GEN) {
      return RANDOM_GEN.nextLong(bound);
    }
  }

  public static long nextLongI(long bound) { // inclusive
    synchronized (RANDOM_GEN) {
      return RANDOM_GEN.nextLong(bound + 1);
    }
  }

  public static <T> void shuffleArray(T[] array) {
    synchronized (RANDOM_GEN) {
      for (int i = 0; i < array.length; i++) {
        int randomPosition = RANDOM_GEN.nextInt(array.length);
        T temp = array[i];
        array[i] = array[randomPosition];
        array[randomPosition] = temp;
      }
    }
  }

  public static boolean inRange(int odds, int total) {
    if (odds == 1) {
      return randomE(total) == 0;
    }
    return inRange((double) odds / (double) total * 100.0);
  }

  public static boolean inRange(double percent) {
    if (percent <= 0) {
      return false;
    }
    int odds = (int) (100.0 / percent);
    if (odds >= 64) {
      return randomE(odds) == 0;
    }
    int[] range = getRange(percent);
    return range[0] == 1 ? randomE(range[1]) == 0 : randomE(range[1]) < range[0];
  }

  public static int[] getRange(double percent) {
    percent = PNumber.reduceDecimals(percent, 4);
    String asString = Double.toString(percent);
    String decimalString = asString.substring(asString.indexOf(".") + 1);
    double factor =
        Math.pow(10, decimalString.matches("0+") || decimalString.matches(".*[A-Z]+.*") ? 1
            : decimalString.length());
    percent = percent * factor;
    int range = 100 * (int) factor;
    if (percent > 1) {
      int gcd = PNumber.greatestCommonDenominator((int) percent, range);
      percent /= gcd;
      range /= gcd;
      if (range > 128) {
        range /= percent;
        percent = 1;
      }
    }
    return new int[] { (int) percent, range };
  }

  public static double chanceToPercent(int odds, int total) {
    return (double) odds / (double) total * 100.0;
  }

  public static double getPercent(double current, double max) {
    double difference = max - (max - current);
    double percent = difference / max * 100.0;
    return percent;
  }
}
