package com.palidinodh.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PCollection {
  public static final boolean isSet(int[] values, int defaultValue) {
    if (values == null) {
      return false;
    }
    for (int value : values) {
      if (value != defaultValue) {
        return true;
      }
    }
    return false;
  }

  public static final boolean isSet(String[] values, String defaultValue) {
    if (values == null) {
      return false;
    }
    for (String value : values) {
      if (value == null) {
        continue;
      }
      if (defaultValue == null || !value.equals(defaultValue)) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] concatFirst(T[] first, T... second) {
    T[] result = Arrays.copyOf(second, first.length + second.length);
    System.arraycopy(first, 0, result, second.length, first.length);
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] concatLast(T[] first, T... second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> toMap(Object... entries) {
    if (entries.length % 2 != 0) {
      throw new IndexOutOfBoundsException("toMap should be an even size!");
    }
    Class<?> keyClass = entries[0].getClass();
    Class<?> valueClass = entries[1].getClass();
    Map<K, V> map = new HashMap<>(entries.length);
    for (int i = 0; i < entries.length; i += 2) {
      if (!keyClass.isInstance(entries[i]) || !valueClass.isInstance(entries[i + 1])) {
        throw new IllegalArgumentException("toMap incorrect instances! " + keyClass + ", "
            + entries[i].getClass() + ", " + valueClass + ", " + entries[i + 1].getClass());
      }
      map.put((K) entries[i], (V) entries[i + 1]);
    }
    return map;
  }

  @SafeVarargs
  public static <T> List<T> toList(T... values) {
    List<T> list = new ArrayList<>(values.length);
    for (T value : values) {
      list.add(value);
    }
    return list;
  }

  @SafeVarargs
  public static <T> List<T> toImmutableList(T... values) {
    List<T> list = new ArrayList<>(values.length);
    for (T value : values) {
      list.add(value);
    }
    return Collections.unmodifiableList(list);
  }

  @SuppressWarnings("rawtypes")
  public static <T> List<T> castList(List list, Class<T> clazz) {
    if (list == null) {
      return null;
    }
    List<T> castedList = new ArrayList<>();
    for (Object obj : list) {
      if (obj != null && clazz.isAssignableFrom(obj.getClass())) {
        castedList.add(clazz.cast(obj));
      }
    }
    return castedList;
  }

  @SuppressWarnings("rawtypes")
  public static <T> Deque<T> castDeque(Deque deque, Class<T> clazz) {
    if (deque == null) {
      return null;
    }
    Deque<T> castedList = new ArrayDeque<>();
    for (Object obj : deque) {
      if (obj != null && clazz.isAssignableFrom(obj.getClass())) {
        castedList.add(clazz.cast(obj));
      }
    }
    return castedList;
  }

  @SuppressWarnings("rawtypes")
  public static <K, V> Map<K, V> castMap(Map map, Class<K> clazz1, Class<V> clazz2) {
    if (map == null) {
      return null;
    }
    Map<K, V> castedMap = new HashMap<>();
    for (Object o : map.entrySet()) {
      if (!(o instanceof Map.Entry)) {
        continue;
      }
      Map.Entry entry = (Map.Entry) o;
      if (entry.getKey() == null || entry.getValue() == null) {
        continue;
      }
      if (clazz1.isAssignableFrom(entry.getKey().getClass())
          && clazz2.isAssignableFrom(entry.getValue().getClass())) {
        castedMap.put(clazz1.cast(entry.getKey()), clazz2.cast(entry.getValue()));
      }
    }
    return castedMap;
  }

  @SuppressWarnings("rawtypes")
  public static <K, V> Map<K, V> castTreeMap(Map map, Class<K> clazz1, Class<V> clazz2) {
    if (map == null) {
      return null;
    }
    Map<K, V> castedMap = new TreeMap<>();
    for (Object o : map.entrySet()) {
      if (!(o instanceof Map.Entry)) {
        continue;
      }
      Map.Entry entry = (Map.Entry) o;
      if (entry.getKey() == null || entry.getValue() == null) {
        continue;
      }
      if (clazz1.isAssignableFrom(entry.getKey().getClass())
          && clazz2.isAssignableFrom(entry.getValue().getClass())) {
        castedMap.put(clazz1.cast(entry.getKey()), clazz2.cast(entry.getValue()));
      }
    }
    return castedMap;
  }
}
