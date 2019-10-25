package com.palidinodh.util;

public class PNumber {
  public static double reduceDecimals(double value, int max) {
    if (max <= 0 || value == Math.floor(value)) {
      return value;
    }
    String asString = Double.toString(value);
    int decimalIndex = asString.indexOf(".");
    String wholeNumber = asString.substring(0, decimalIndex);
    if (asString.length() - (decimalIndex + 1) > max) {
      double factor = Math.pow(10, max);
      value = value * factor;
      value = Math.round(value) / factor;
    }
    String decimalString = asString.substring(decimalIndex + 1);
    decimalString = decimalString.substring(0, Math.min(max, decimalString.length()));
    decimalString = decimalString.replaceFirst("[a-zA-Z]+.*", "");
    if (decimalString.matches("0+")) {
      decimalString = "0";
    }
    return Double.parseDouble(wholeNumber + "." + decimalString);
  }

  public static int greatestCommonDenominator(int a, int b) {
    return b == 0 ? a : greatestCommonDenominator(b, a % b);
  }

  public static double formatDouble1(double d) {
    return formatDouble(d, 1);
  }

  public static double formatDouble2(double d) {
    return formatDouble(d, 2);
  }

  public static double formatDouble(double d, int decimalPlaces) {
    if (d == Math.floor(d)) {
      return d;
    }
    String value = String.valueOf(d);
    if (value.contains("E")) {
      return 0;
    }
    String decimal = value.substring(value.indexOf(".") + 1);
    if (decimal.length() > decimalPlaces) {
      String whole = value.substring(0, value.indexOf("."));
      decimal = decimal.substring(0, decimalPlaces);
      return Double.parseDouble(whole + "." + decimal);
    }
    return d;
  }

  public static String formatNumber(long i) {
    String s = String.valueOf(i);
    for (int k = s.length() - 3; k > 0; k -= 3) {
      s = s.substring(0, k) + "," + s.substring(k);
    }
    return s;
  }

  public static String abbreviateNumber(long i) {
    String s = String.valueOf(i);
    for (int k = s.length() - 3; k > 0; k -= 3) {
      s = s.substring(0, k) + "," + s.substring(k);
    }
    if (s.length() > 12) {
      int decimalValue = Integer.parseInt(s.substring(s.length() - 11, s.length() - 10));
      if (decimalValue > 0) {
        s = s.substring(0, s.length() - 12) + "." + decimalValue + "B";
      } else {
        s = s.substring(0, s.length() - 12) + "B";
      }
    } else if (s.length() > 8) {
      int decimalValue = Integer.parseInt(s.substring(s.length() - 7, s.length() - 6));
      if (decimalValue > 0) {
        s = s.substring(0, s.length() - 8) + "." + decimalValue + "M";
      } else {
        s = s.substring(0, s.length() - 8) + "M";
      }
    } else if (s.length() > 4) {
      s = s.substring(0, s.length() - 4) + "K";
    }
    return s;
  }

  public static int round(int value) {
    if (value < 10) {
      return value;
    }
    if (value > 100000000) {
      return (int) (Math.round(value / 10000000.0) * 10000000);
    } else if (value > 10000000) {
      return (int) (Math.round(value / 1000000.0) * 1000000);
    } else if (value > 1000000) {
      return (int) (Math.round(value / 100000.0) * 100000);
    } else if (value > 100000) {
      return (int) (Math.round(value / 10000.0) * 10000);
    } else if (value > 10000) {
      return (int) (Math.round(value / 1000.0) * 1000);
    } else if (value > 1000) {
      return (int) (Math.round(value / 100.0) * 100);
    }
    return (int) (Math.round(value / 10.0) * 10);
  }

  public static int fullRound(int value) {
    if (value > 100000000) {
      return (int) (Math.round(value / 100000000.0) * 100000000);
    } else if (value > 10000000) {
      return (int) (Math.round(value / 10000000.0) * 10000000);
    } else if (value > 1000000) {
      return (int) (Math.round(value / 1000000.0) * 1000000);
    } else if (value > 100000) {
      return (int) (Math.round(value / 100000.0) * 100000);
    } else if (value > 10000) {
      return (int) (Math.round(value / 10000.0) * 10000);
    } else if (value > 1000) {
      return (int) (Math.round(value / 1000.0) * 1000);
    } else if (value > 100) {
      return (int) (Math.round(value / 100.0) * 100);
    }
    return (int) (Math.round(value / 10.0) * 10);
  }

  public static int fullCeil(int value) {
    if (value > 100000000) {
      return (int) (Math.ceil(value / 100000000.0) * 100000000);
    } else if (value > 10000000) {
      return (int) (Math.ceil(value / 10000000.0) * 10000000);
    } else if (value > 1000000) {
      return (int) (Math.ceil(value / 1000000.0) * 1000000);
    } else if (value > 100000) {
      return (int) (Math.ceil(value / 100000.0) * 100000);
    } else if (value > 10000) {
      return (int) (Math.ceil(value / 10000.0) * 10000);
    } else if (value > 1000) {
      return (int) (Math.ceil(value / 1000.0) * 1000);
    } else if (value > 100) {
      return (int) (Math.ceil(value / 100.0) * 100);
    }
    return (int) (Math.ceil(value / 10.0) * 10);
  }

  public static double addDoubles(double... input) {
    if (input.length == 0) {
      return 0;
    } else if (input.length == 1) {
      return input[0];
    }
    double sum = input[0];
    double c = 0;
    for (int i = 1; i < input.length; i++) {
      double t = sum + input[i];
      if (Math.abs(sum) >= Math.abs(input[i])) {
        c += sum - t + input[i];
      } else {
        c += input[i] - t + sum;
      }
      sum = t;
    }
    return sum + c;
  }

  /* Ditch this method eventually */
  public static boolean canAddInt(int initial, int adding, int max) {
    if (initial >= max || adding > max || adding > max - initial) {
      return false;
    }
    int added = initial + adding;
    if (((initial ^ added) & (adding ^ added)) < 0 || added > max) {
      return false;
    }
    return true;
  }

  public static int maxAddInt(int initial, int adding, int max) {
    if (initial >= max || adding > max) {
      return 0;
    }
    if (adding > max - initial) {
      adding = max - initial;
    }
    int added = initial + adding;
    if (((initial ^ added) & (adding ^ added)) < 0 || added > max) {
      return 0;
    }
    return adding;
  }

  public static int addInt(int initial, int adding) {
    return addInt(initial, adding, Integer.MAX_VALUE);
  }

  public static int addInt(int initial, int adding, int max) {
    if (initial >= max || adding >= max) {
      return max;
    }
    if (adding > max - initial) {
      adding = max - initial;
    }
    int addedResult = initial + adding;
    if (((initial ^ addedResult) & (adding ^ addedResult)) < 0 || addedResult > max) {
      return max;
    }
    return addedResult;
  }

  public static boolean canMultiplyInt(int initial, int multiplying, int max) {
    if (initial >= max || multiplying >= max) {
      return false;
    }
    long multiplied = (long) initial * (long) multiplying;
    if (multiplied != (int) multiplied || multiplied > max) {
      return false;
    }
    return true;
  }

  public static int multiplyInt(int initial, int multiplying, int max) {
    if (initial >= max || multiplying >= max) {
      return max;
    }
    if (multiplying > max / initial) {
      multiplying = max / initial;
    }
    long multipliedResult = (long) initial * (long) multiplying;
    int returnableMultipledResult = (int) multipliedResult;
    if (multipliedResult != returnableMultipledResult || multipliedResult > max) {
      return max;
    }
    return returnableMultipledResult;
  }

  public static int filterMultiplyAmount(int amount, double multiplier) {
    return filterMultiplyAmount(amount, multiplier, Integer.MAX_VALUE);
  }

  public static int filterMultiplyAmount(int amount, double multiplier, int max) {
    if (amount > max) {
      amount = max;
    }
    if (amount > max / multiplier) {
      amount = (int) (max / multiplier);
    }
    long multipliedAmount = (long) (amount * multiplier);
    if ((int) multipliedAmount != multipliedAmount || multipliedAmount < 0
        || multipliedAmount > max) {
      amount = 0;
    }
    return amount;
  }

  public static int filterDivideAmount(int amount, double division) {
    return filterDivideAmount(amount, division, Integer.MAX_VALUE);
  }

  public static int filterDivideAmount(int amount, double division, int max) {
    if (amount > max) {
      amount = max;
    }
    long dividedAmount = (long) (amount / division);
    if ((int) dividedAmount != dividedAmount || dividedAmount < 0 || dividedAmount > max) {
      amount = 0;
    }
    return amount;
  }
}
