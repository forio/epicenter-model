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
import org.graalvm.polyglot.HostAccess;

public class Epicenter {

  private static final InheritableThreadLocal<EpicenterAccessible> epicenterAccessibleLocal = new InheritableThreadLocal<>();

  public static void setCurrent (EpicenterAccessible epicenterAccessible) {

    epicenterAccessibleLocal.set(epicenterAccessible);
  }

  // For example, Epicenter.constructForkJoinPool(2).submit(() -> LongStream.rangeClosed(0, 5).parallel().forEach(number -> System.out.println(number))).get();
  @HostAccess.Export
  public static ForkJoinPool constructForkJoinPool (int parallelism) {

    return epicenterAccessibleLocal.get().constructForkJoinPool(parallelism);
  }

  @HostAccess.Export
  public static Actor getActor () {

    return epicenterAccessibleLocal.get().getActor();
  }

  @HostAccess.Export
  public static void subscribe (String name, Consumer<Object[]> consumer) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.subscribe(name, consumer);
    }
  }

  @HostAccess.Export
  public static void publish (String name, Object... arguments) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.publish(name, arguments);
    }
  }

  @HostAccess.Export
  public static void record (String name, Object value) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.record(name, value);
    }
  }

  @HostAccess.Export
  public static void log (LogLevel level, String message) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.log(level, message);
    }
  }

  @HostAccess.Export
  public static void scribble (LogLevel level, String message) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.scribble(level, message);
    }
  }

  @HostAccess.Export
  public static void callback (String name, Object... arguments) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.callback(name, arguments);
    }
  }
}