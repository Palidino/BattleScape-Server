package com.palidinodh.util;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public abstract class PEvent {
  public static final int ONCE = -1, MILLIS_600 = 0, MILLIS_1200 = 1, MILLIS_1800 = 2,
      MILLIS_2400 = 3, MILLIS_3000 = 4, MILLIS_3600 = 5, MILLIS_4200 = 6, MILLIS_4800 = 7,
      MILLIS_5400 = 8, MILLIS_6000 = 9, MILLIS_6600 = 10, MILLIS_7200 = 11, MILLIS_7800 = 12,
      MILLIS_8400 = 13, MILLIS_9000 = 14, MILLIS_9600 = 15, MILLIS_10200 = 16, MILLIS_10800 = 17,
      MILLIS_11400 = 18, MILLIS_12000 = 19;
  public static final int SEC_3 = MILLIS_3000, SEC_6 = MILLIS_6000, SEC_9 = MILLIS_9000,
      SEC_12 = MILLIS_12000, SEC_15 = 24, SEC_18 = 29, SEC_21 = 34, SEC_24 = 39, SEC_27 = 44,
      SEC_30 = 49;
  public static final int MIN_1 = 100, MIN_2 = 200, MIN_3 = 300, MIN_4 = 400, MIN_5 = 500,
      MIN_6 = 600, MIN_7 = 700, MIN_8 = 800, MIN_9 = 900, MIN_10 = 1000;

  @Getter
  @Setter
  private boolean running;
  @Getter
  private int tick;
  private int currentTick;
  @Getter
  private int executions;
  private Map<Integer, Object> attachments;

  public PEvent(int tick) {
    setTick(tick);
    setRunning(true);
  }

  public PEvent() {
    setTick(MILLIS_600);
    setRunning(true);
  }

  public Object script(String name, Object... args) {
    return null;
  }

  public abstract void execute();

  public void stopHook() {}

  public final void start() {
    setRunning(true);
  }

  public final boolean isReady() {
    if (!isRunning()) {
      return false;
    }
    return currentTick == ONCE || currentTick-- == 0;
  }

  public final boolean complete() {
    if (!isReady()) {
      return false;
    }
    currentTick = tick;
    execute();
    executions++;
    if (currentTick == ONCE) {
      stop();
    }
    return !running;
  }

  public final void stop() {
    setRunning(false);
    stopHook();
  }

  public final void setTick(int tick) {
    this.tick = currentTick = tick;
  }

  public Object getAttachment() {
    return getAttachment(0);
  }

  public Object getAttachment(int index) {
    return attachments != null ? attachments.get(index) : null;
  }

  public void setAttachment(Object attachment) {
    setAttachment(0, attachment);
  }

  public void setAttachment(int index, Object attachment) {
    if (attachments == null) {
      attachments = new HashMap<>();
    }
    attachments.put(index, attachment);
  }
}
