/*
 * Copyright (c) 2020 Forio
 *
 * This file is part of the Forio Epicenter project.
 *
 * These files from the Forio Epicenter project are free software, you can
 * redistribute and/or modify them under the terms of the Apache License, Version 2.0.
 *
 * These files from the Forio Epicenter project are distributed in the hope that
 * they will be useful, but are WITHOUT ANY WARRANTY; without even an implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Apache License
 * for more details.
 *
 * You should have received a copy of the Apache License along with the these Forio
 * Epicenter project files. If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.forio.epicenter.model.api;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public class Epicenter {

  private static final InheritableThreadLocal<EpicenterAccessible> epicenterAccessibleLocal = new InheritableThreadLocal<>();

  public static void setCurrent (EpicenterAccessible epicenterAccessible) {

    epicenterAccessibleLocal.set(epicenterAccessible);
  }

  public static Thread setContextClassLoader (Thread thread) {

    return epicenterAccessibleLocal.get().setContextClassLoader(thread);
  }

  public static ForkJoinPool constructForkJoinPool (int parallelism) {

    return epicenterAccessibleLocal.get().constructForkJoinPool(parallelism);
  }

  public static Actor getActor () {

    return epicenterAccessibleLocal.get().getActor();
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