package com.palidinodh.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PString {
  @SuppressWarnings("rawtypes")
  public static String[] toStringArray(List list) {
    return toStringArray(list, false);
  }

  @SuppressWarnings("rawtypes")
  public static String[] toStringArray(List list, boolean clear) {
    String[] strings = PCollection.castList(list, String.class).toArray(new String[0]);
    if (clear) {
      list.clear();
    }
    return strings;
  }

  public static String aOrAn(String word) {
    return "aeiouAEIOU".indexOf(word.charAt(0)) != -1 ? "an" : "a";
  }

  public static String aOrAnCap(String word) {
    return "aeiouAEIOU".indexOf(word.charAt(0)) != -1 ? "An" : "A";
  }

  public static String toAndSentenceString(String string, int position, int total) {
    if (total > 2 && position + 1 < total) {
      string += ", ";
    }
    if (total > 1 && position + 2 == total) {
      if (total == 2) {
        string += " ";
      }
      string += "and ";
    }
    return string;
  }

  public static String toOrSentenceString(String string, int position, int total) {
    if (total > 2 && position + 1 < total) {
      string += ", ";
    }
    if (total > 1 && position + 2 == total) {
      if (total == 2) {
        string += " ";
      }
      string += "or ";
    }
    return string;
  }

  public static String stringsToString(String[] strings, String divider) {
    String string = "";
    for (int i = 0; i < strings.length; i++) {
      string += strings[i];
      if (i + 1 < strings.length) {
        string += "|";
      }
    }
    return string;
  }

  public static String implodeString(String key, String[] array) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      sb.append(array[i]);
      if (i != array.length - 1) {
        sb.append(key);
      }
    }
    return sb.toString();
  }

  public static long stringToLong(String s) {
    if (s.length() > 9) {
      s = s.substring(0, 9);
    }
    long l = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      l *= 37;
      if (c >= 'A' && c <= 'Z') {
        l += 1 + c - 65;
      } else if (c >= 'a' && c <= 'z') {
        l += 1 + c - 97;
      } else if (c >= '0' && c <= '9') {
        l += 27 + c - 48;
      }
    }
    for (; l % 37 == 0 && l != 0; l /= 37) {

    }
    return l;
  }

  public static String formatName(String s) {
    if (s == null) {
      return "Null";
    }
    if (s.length() > 0) {
      s = s.toLowerCase();
      char[] ac = s.toCharArray();
      for (int j = 0; j < ac.length; j++) {
        if (ac[j] == ' ') {
          if (j + 1 < ac.length && ac[j + 1] >= 'a' && ac[j + 1] <= 'z') {
            ac[j + 1] = (char) (ac[j + 1] + 65 - 97);
          }
        }
      }
      if (ac[0] >= 'a' && ac[0] <= 'z') {
        ac[0] = (char) (ac[0] + 65 - 97);
      }
      return new String(ac);
    }
    return s;
  }

  public static boolean startsWithVowel(String string) {
    return string.startsWith("A") || string.startsWith("E") || string.startsWith("I")
        || string.startsWith("O") || string.startsWith("U");
  }

  public static String cleanString(String string) {
    // Strips off all non-ASCII characters
    string = string.replaceAll("[^\\x00-\\x7F]", "");
    // Erases all the ASCII control characters
    string = string.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
    // Removes non-printable characters from Unicode
    string = string.replaceAll("\\p{C}", "");
    return string.trim();
  }

  public static String removeHtml(String string) {
    int startIndex = -1, endIndex = -1;
    while ((startIndex = string.indexOf('<')) != -1
        && (endIndex = string.indexOf('>', startIndex)) != -1) {
      string = string.substring(0, startIndex) + string.substring(endIndex + 1);
    }
    return string;
  }

  public static String cleanName(String name) {
    name = cleanString(name);
    name = name.replaceAll("[^a-zA-Z0-9 _-]", "");
    return name;
  }

  public static String getBaseIp(String ip) {
    if (ip == null) {
      return ip;
    }
    String[] num = ip.split("\\.");
    if (num.length != 4) {
      return ip;
    }
    return num[0] + "." + num[1];
  }

  public static boolean getBaseIpMatch(String ip1, String ip2) {
    return ip1 != null && ip2 != null && getBaseIp(ip1).equals(getBaseIp(ip2));
  }

  public static String wrapMultilineText(String str, int wrapLength) {
    if (str == null) {
      return null;
    }
    String[] strs = str.replace("\r", "").split("\n");
    StringBuilder builder = new StringBuilder();
    for (String aStr : strs) {
      builder.append(wrapText(aStr, wrapLength));
      builder.append("\n");
    }
    return builder.toString();
  }

  public static String wrapText(String str, int wrapLength) {
    if (str == null) {
      return null;
    }
    String noHtmlStr = str.replaceAll("\\<[^>]*>", "");
    Map<Integer, String> htmlMap = new LinkedHashMap<>();
    Pattern patternToHtml = Pattern.compile("\\<[^>]*>");
    Matcher matcher = patternToHtml.matcher(str);
    while (matcher.find()) {
      htmlMap.put(matcher.start(), matcher.group());
    }
    String newLineStr = "\n";
    boolean wrapLongWords = false;
    String wrapOn = " ";
    if (wrapLength < 1) {
      wrapLength = 1;
    }
    if (wrapOn == null || wrapOn.length() == 0) {
      wrapOn = " ";
    }
    Pattern patternToWrapOn = Pattern.compile(wrapOn);
    int inputLineLength = noHtmlStr.length();
    int offset = 0;
    StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
    while (offset < inputLineLength) {
      int spaceToWrapAt = -1;
      matcher = patternToWrapOn.matcher(noHtmlStr.substring(offset,
          Math.min((int) Math.min(Integer.MAX_VALUE, offset + wrapLength + 1L), inputLineLength)));
      if (matcher.find()) {
        if (matcher.start() == 0) {
          offset += matcher.end();
          continue;
        }
        spaceToWrapAt = matcher.start() + offset;
      }
      // only last line without leading spaces is left
      if (inputLineLength - offset <= wrapLength) {
        break;
      }
      while (matcher.find()) {
        spaceToWrapAt = matcher.start() + offset;
      }
      if (spaceToWrapAt >= offset) {
        // normal case
        wrappedLine.append(noHtmlStr, offset, spaceToWrapAt);
        for (Iterator<Map.Entry<Integer, String>> it = htmlMap.entrySet().iterator(); it
            .hasNext();) {
          Map.Entry<Integer, String> entry = it.next();
          if (entry.getKey() > wrappedLine.length()) {
            break;
          }
          wrappedLine.insert(entry.getKey(), entry.getValue());
          it.remove();
        }
        wrappedLine.append(newLineStr);
        offset = spaceToWrapAt + 1;
      } else {
        // really long word or URL
        if (wrapLongWords) {
          // wrap really long word one line at a time
          wrappedLine.append(noHtmlStr, offset, wrapLength + offset);
          for (Iterator<Map.Entry<Integer, String>> it = htmlMap.entrySet().iterator(); it
              .hasNext();) {
            Map.Entry<Integer, String> entry = it.next();
            if (entry.getKey() > wrappedLine.length()) {
              break;
            }
            wrappedLine.insert(entry.getKey(), entry.getValue());
            it.remove();
          }
          wrappedLine.append(newLineStr);
          offset += wrapLength;
        } else {
          // do not wrap really long word, just extend beyond limit
          matcher = patternToWrapOn.matcher(noHtmlStr.substring(offset + wrapLength));
          if (matcher.find()) {
            spaceToWrapAt = matcher.start() + offset + wrapLength;
          }
          if (spaceToWrapAt >= 0) {
            wrappedLine.append(noHtmlStr, offset, spaceToWrapAt);
            for (Iterator<Map.Entry<Integer, String>> it = htmlMap.entrySet().iterator(); it
                .hasNext();) {
              Map.Entry<Integer, String> entry = it.next();
              if (entry.getKey() >= wrappedLine.length()) {
                break;
              }
              wrappedLine.insert(entry.getKey(), entry.getValue());
              it.remove();
            }
            wrappedLine.append(newLineStr);
            offset = spaceToWrapAt + 1;
          } else {
            wrappedLine.append(noHtmlStr, offset, noHtmlStr.length());
            offset = inputLineLength;
          }
        }
      }
    }
    // Whatever is left in line is short enough to just pass through
    wrappedLine.append(noHtmlStr, offset, noHtmlStr.length());
    for (Iterator<Map.Entry<Integer, String>> it = htmlMap.entrySet().iterator(); it.hasNext();) {
      Map.Entry<Integer, String> entry = it.next();
      if (entry.getKey() > wrappedLine.length()) {
        break;
      }
      wrappedLine.insert(entry.getKey(), entry.getValue());
      it.remove();
    }
    return wrappedLine.toString();
  }
}
