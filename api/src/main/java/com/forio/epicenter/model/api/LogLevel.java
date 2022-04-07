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

import org.graalvm.polyglot.HostAccess;

public enum LogLevel {

  @HostAccess.Export
  TRACE,
  @HostAccess.Export
  DEBUG,
  @HostAccess.Export
  INFO,
  @HostAccess.Export
  WARN,
  @HostAccess.Export
  ERROR,
  @HostAccess.Export
  FATAL,
  @HostAccess.Export
  OFF;

  public boolean atLeast (LogLevel logLevel) {

    return this.ordinal() >= logLevel.ordinal();
  }

  public boolean noGreater (LogLevel logLevel) {

    return this.ordinal() <= logLevel.ordinal();
  }

  public String[] allowedValues () {

    String[] values = new String[LogLevel.values().length - this.ordinal()];
    int index = 0;

    for (LogLevel level : LogLevel.values()) {
      if (level.ordinal() >= this.ordinal()) {
        values[index++] = level.name().toLowerCase();
      }
    }

    return values;
  }
}