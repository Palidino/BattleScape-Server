package com.palidinodh.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PTime {
  public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("America/New_York");
  public static final String[] CALENDAR_DAYS =
      {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
  public static final SimpleDateFormat DETAILED_DATE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  public static final SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat("yyyy-MM-dd");
  public static final SimpleDateFormat TIME_OF_DAY = new SimpleDateFormat("hh:mm a");
  public static final Calendar CALENDAR = new GregorianCalendar(DEFAULT_TIMEZONE);

  private static String detailedDate;
  private static String simpleDate;
  private static String timeOfDay;
  private static int month;
  private static int dayOfWeek;
  private static int hour;
  private static int hour24;
  private static int minute;

  public static synchronized void update() {
    CALENDAR.setTimeInMillis(currentTimeMillis());
    Date date = CALENDAR.getTime();
    detailedDate = DETAILED_DATE.format(date);
    simpleDate = SIMPLE_DATE.format(date);
    timeOfDay = TIME_OF_DAY.format(date);
    month = CALENDAR.get(Calendar.MONTH);
    dayOfWeek = CALENDAR.get(Calendar.DAY_OF_WEEK);
    hour = CALENDAR.get(Calendar.HOUR);
    hour24 = CALENDAR.get(Calendar.HOUR_OF_DAY);
    minute = CALENDAR.get(Calendar.MINUTE);
  }

  public static long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  public static long nanoTime() {
    return System.nanoTime();
  }

  public static long nanoToMilli(long start, long end) {
    return TimeUnit.NANOSECONDS.toMillis(end - start);
  }

  public static long nanoToSec(long start, long end) {
    return TimeUnit.NANOSECONDS.toSeconds(end - start);
  }

  public static long nanoToMin(long start, long end) {
    return TimeUnit.NANOSECONDS.toMinutes(end - start);
  }

  public static long nanoToMilli(long start) {
    return TimeUnit.NANOSECONDS.toMillis(nanoTime() - start);
  }

  public static long nanoToSec(long start) {
    return TimeUnit.NANOSECONDS.toSeconds(nanoTime() - start);
  }

  public static long nanoToMin(long start) {
    return TimeUnit.NANOSECONDS.toMinutes(nanoTime() - start);
  }

  public static long milliToSec(long start) {
    return TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis() - start);
  }

  public static long milliToMin(long start) {
    return TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis() - start);
  }

  public static long milliToHour(long start) {
    return TimeUnit.MILLISECONDS.toHours(currentTimeMillis() - start);
  }

  public static long milliToDay(long start) {
    return TimeUnit.MILLISECONDS.toDays(currentTimeMillis() - start);
  }

  public static long secToDay(long seconds) {
    return TimeUnit.SECONDS.toDays(seconds);
  }

  public static long minToSec(long minutes) {
    return TimeUnit.MINUTES.toSeconds(minutes);
  }

  public static long minToHour(long minutes) {
    return TimeUnit.MINUTES.toHours(minutes);
  }

  public static long hourToMin(long hours) {
    return TimeUnit.HOURS.toMinutes(hours);
  }

  public static long dayToMilli(long days) {
    return TimeUnit.DAYS.toMillis(days);
  }

  public static long tickToMilli(long ticks) {
    return ticks * 600;
  }

  public static long tickToSec(long ticks) {
    return tickToMilli(ticks) / 1000;
  }

  public static long tickToMin(long ticks) {
    return tickToSec(ticks) / 60;
  }

  public static long tickToHour(long ticks) {
    return tickToMin(ticks) / 60;
  }

  public static long tickToDay(long ticks) {
    return tickToHour(ticks) / 24;
  }

  public static long milliToTick(long milliseconds) {
    return milliseconds / 600;
  }

  public static long secToTick(long seconds) {
    return milliToTick(seconds * 1000);
  }

  public static long minToTick(long mins) {
    return secToTick(mins * 60);
  }

  public static long hourToTick(long hours) {
    return minToTick(hours * 60);
  }

  public static long dayToTick(long days) {
    return hourToTick(days * 24);
  }

  public static long getExactTime() {
    update();
    return CALENDAR.getTime().getTime();
  }

  public static String getHour(int hour) {
    String hourOf;
    if (hour < 12) {
      hourOf = "AM";
    } else {
      hourOf = "PM";
      hour -= 12;
    }
    if (hour == 0) {
      hour = 12;
    }
    return hour + hourOf;
  }

  public static int getRemainingMinutes(int fromDayMinutes, int toDayMinutes) {
    if (fromDayMinutes > toDayMinutes) {
      long fromHour = minToHour(fromDayMinutes);
      toDayMinutes += hourToMin(fromHour + 24 - fromHour);
    }
    return toDayMinutes - fromDayMinutes;
  }

  public static String getDetailedDate() {
    return detailedDate;
  }

  public static String getSimpleDate() {
    return simpleDate;
  }

  public static String getTimeOfDay() {
    return timeOfDay;
  }

  public static Calendar getCalendar() {
    return CALENDAR;
  }

  public static int getMonth() {
    return month;
  }

  public static int getDayOfWeek() {
    return dayOfWeek;
  }

  public static int getHour() {
    return hour;
  }

  public static int getHour24() {
    return hour24;
  }

  public static int getMinute() {
    return minute;
  }

  public static String ticksToDuration(long ticks) {
    long seconds = tickToSec(ticks);
    long minutes = seconds / 60;
    long hours = minutes / 60;
    if (minutes > 0) {
      seconds -= minutes * 60;
    }
    if (hours > 0) {
      minutes -= hours * 60;
    }
    String secondsValue = (seconds < 10 ? "0" : "") + Long.toString(seconds);
    String minutesValue = (minutes < 10 ? "0" : "") + Long.toString(minutes);
    String hoursValue = hours > 0 ? Long.toString(hours) + ":" : "";
    return hoursValue + minutesValue + ":" + secondsValue;
  }

  public static String ticksToLongDuration(long ticks) {
    long minutes = tickToMin(ticks);
    long hours = minutes / 60;
    if (hours > 0) {
      minutes -= hours * 60;
    }
    String minutesValue = (minutes < 10 ? "0" : "") + Long.toString(minutes);
    return hours + ":" + minutesValue;
  }

  public static String minutesToLongDuration(long minutes) {
    return ticksToLongDuration(minToTick(minutes));
  }

  public static String microtimePHP() {
    long mstime = PTime.currentTimeMillis();
    long seconds = mstime / 1000;
    double decimal = (mstime - seconds * 1000) / 1000.0;
    return decimal + " " + seconds;
  }

  public static String uniqidPHP(String prefix, boolean moreEntropy) {
    long time = PTime.currentTimeMillis();
    String uniqid = "";
    if (!moreEntropy) {
      uniqid = String.format("%s%08x%05x", prefix, time / 1000, time);
    } else {
      SecureRandom sec = new SecureRandom();
      byte[] sbuf = sec.generateSeed(8);
      ByteBuffer bb = ByteBuffer.wrap(sbuf);
      uniqid = String.format("%s%08x%05x", prefix, time / 1000, time);
      uniqid += "." + String.format("%.8s", "" + bb.getLong() * -1);
    }
    return uniqid;
  }

  static {
    DETAILED_DATE.setTimeZone(DEFAULT_TIMEZONE);
    SIMPLE_DATE.setTimeZone(DEFAULT_TIMEZONE);
    TIME_OF_DAY.setTimeZone(DEFAULT_TIMEZONE);
    update();
  }
}
