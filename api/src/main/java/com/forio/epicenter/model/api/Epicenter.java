package com.forio.epicenter.model.api;

import java.util.function.Consumer;

public class Epicenter {

  private static final InheritableThreadLocal<EpicenterAccessible> epicenterAccessibleLocal = new InheritableThreadLocal<>();

  public static void setCurrent (EpicenterAccessible epicenterAccessible) {

    epicenterAccessibleLocal.set(epicenterAccessible);
  }

  public static void subscribe (String name, Consumer<Object[]> consumer) {

    EpicenterAccessible epicenterUtility;

    if ((epicenterUtility = epicenterAccessibleLocal.get()) != null) {
      epicenterUtility.subscribe(name, consumer);
    }
  }

  public static void publish (String name, Object... arguments) {

    EpicenterAccessible epicenterUtility;

    if ((epicenterUtility = epicenterAccessibleLocal.get()) != null) {
      epicenterUtility.publish(name, arguments);
    }
  }

  public static void record (String name, Object value) {

    EpicenterAccessible epicenterUtility;

    if ((epicenterUtility = epicenterAccessibleLocal.get()) != null) {
      epicenterUtility.record(name, value);
    }
  }

  public static void log (LogLevel level, String message) {

    EpicenterAccessible epicenterUtility;

    if ((epicenterUtility = epicenterAccessibleLocal.get()) != null) {
      epicenterUtility.log(level, message);
    }
  }

  public static void scribble (LogLevel level, String message) {

    EpicenterAccessible epicenterUtility;

    if ((epicenterUtility = epicenterAccessibleLocal.get()) != null) {
      epicenterUtility.scribble(level, message);
    }
  }

  public static void callback (String name, Object... arguments) {

    EpicenterAccessible epicenterUtility;

    if ((epicenterUtility = epicenterAccessibleLocal.get()) != null) {
      epicenterUtility.callback(name, arguments);
    }
  }
}