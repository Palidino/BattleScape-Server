package com.palidinodh.encryption;

import java.security.MessageDigest;
import java.util.concurrent.ThreadLocalRandom;

public class MD5 {
  private String inStr;
  private MessageDigest md5;

  public MD5(String inStr) {
    this.inStr = inStr;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String compute() {
    try {
      char[] charArray = inStr.toCharArray();
      byte[] byteArray = new byte[charArray.length];
      for (int i = 0; i < charArray.length; i++) {
        byteArray[i] = (byte) charArray[i];
      }
      byte[] md5Bytes = md5.digest(byteArray);
      StringBuilder hexValue = new StringBuilder();
      for (byte md5Byte : md5Bytes) {
        int val = md5Byte & 255;
        if (val < 16) {
          hexValue.append("0");
        }
        hexValue.append(Integer.toHexString(val));
      }
      return hexValue.toString();
    } catch (Exception e) {
      return "null";
    }
  }

  public static String createSalt() {
    String salt = "";
    for (int i = 0; i < 30; i++) {
      int value = ThreadLocalRandom.current().nextInt(33, 126);
      salt += (char) value;
    }
    return salt;
  }

  public static String compute(String in, String salt) {
    String a = new MD5(in).compute();
    String b = new MD5(a + salt).compute();
    return b;
  }
}
