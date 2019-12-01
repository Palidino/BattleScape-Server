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
      { "", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
  public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  public static final SimpleDateFormat FULL_DATE_FILENAME =
      new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
  public static final SimpleDateFormat YEAR_MONTH_DAY = new SimpleDateFormat("yyyy-MM-dd");
  public static final SimpleDateFormat TIME_OF_DAY = new SimpleDateFormat("hh:mm a");
  public static final Calendar CALENDAR = new GregorianCalendar(DEFAULT_TIMEZONE);

  private static String fullDate;
  private static String fullDateFilename;
  private static String yearMonthDay;
  private static String timeOfDay;
  private static int month;
  private static int dayOfWeek;
  private static int hour;
  private static int hour24;
  private static int minute;

  public static synchronized void update() {
    CALENDAR.setTimeInMillis(currentTimeMillis());
    Date date = CALENDAR.getTime();
    fullDate = FULL_DATE.format(date);
    fullDateFilename = FULL_DATE_FILENAME.format(date);
    yearMonthDay = YEAR_MONTH_DAY.format(date);
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

  public static long betweenNanoToMilli(long start) {
    return TimeUnit.NANOSECONDS.toMillis(nanoTime() - start);
  }

  public static long betweenNanoToSec(long start) {
    return TimeUnit.NANOSECONDS.toSeconds(nanoTime() - start);
  }

  public static long betweenNanoToMin(long start) {
    return TimeUnit.NANOSECONDS.toMinutes(nanoTime() - start);
  }

  public static long betweenMilliToSec(long start) {
    return TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis() - start);
  }

  public static long betweenMilliToMin(long start) {
    return TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis() - start);
  }

  public static long betweenMilliToHour(long start) {
    return TimeUnit.MILLISECONDS.toHours(currentTimeMillis() - start);
  }

  public static long betweenMilliToDay(long start) {
    return TimeUnit.MILLISECONDS.toDays(currentTimeMillis() - start);
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

  public static long secToMilli(long seconds) {
    return TimeUnit.SECONDS.toMillis(seconds);
  }

  public static long secToMin(long seconds) {
    return TimeUnit.SECONDS.toMinutes(seconds);
  }

  public static long secToHour(long seconds) {
    return TimeUnit.SECONDS.toHours(seconds);
  }

  public static long betweenSecToHour(long start) {
    return TimeUnit.SECONDS.toHours(currentTimeMillis() / 1000 - start);
  }

  public static long secToDay(long seconds) {
    return TimeUnit.SECONDS.toDays(seconds);
  }

  public static long minToMilli(long minutes) {
    return TimeUnit.MINUTES.toMillis(minutes);
  }

  public static long minToSec(long minutes) {
    return TimeUnit.MINUTES.toSeconds(minutes);
  }

  public static long minToHour(long minutes) {
    return TimeUnit.MINUTES.toHours(minutes);
  }

  public static long minToDay(long minutes) {
    return TimeUnit.MINUTES.toDays(minutes);
  }

  public static long hourToMilli(long hours) {
    return TimeUnit.HOURS.toMillis(hours);
  }

  public static long hourToMin(long hours) {
    return TimeUnit.HOURS.toMinutes(hours);
  }

  public static long hourToDay(long hours) {
    return TimeUnit.HOURS.toDays(hours);
  }

  public static long dayToMilli(long days) {
    return TimeUnit.DAYS.toMillis(days);
  }

  public static long dayToSec(long days) {
    return TimeUnit.DAYS.toSeconds(days);
  }

  public static long dayToMin(long days) {
    return TimeUnit.DAYS.toMinutes(days);
  }

  public static long dayToHour(long days) {
    return TimeUnit.DAYS.toHours(days);
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

  public static String getFullDate() {
    return fullDate;
  }

  public static String getFullDateFilename() {
    return fullDateFilename;
  }

  public static String getYearMonthDay() {
    return yearMonthDay;
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
    FULL_DATE.setTimeZone(DEFAULT_TIMEZONE);
    YEAR_MONTH_DAY.setTimeZone(DEFAULT_TIMEZONE);
    TIME_OF_DAY.setTimeZone(DEFAULT_TIMEZONE);
    update();
  }
}
