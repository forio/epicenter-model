package com.forio.epicenter.model.api;

public enum LogLevel {

  TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF;

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