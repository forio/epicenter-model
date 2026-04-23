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

/**
 * Log severity levels ordered from least to most severe, plus a sentinel for disabling output.
 *
 * <p>Declaration order encodes severity: {@link #TRACE} (ordinal 0) is the lowest and
 * {@link #OFF} (ordinal 6) is the highest. The comparison helpers ({@link #atLeast},
 * {@link #noGreater}) and {@link #allowedValues()} all depend on this ordering, so inserting or
 * reordering constants is a breaking change.</p>
 *
 * <h2>Model code usage</h2>
 * <p>Pass a level constant to {@link Epicenter#log(LogLevel, String)} to emit a message at that
 * severity. The bridge decides whether to forward the message.</p>
 *
 * <h2>Bridge implementation usage</h2>
 * <p>Use {@link #atLeast(LogLevel)} to gate forwarding on a configured minimum level. Use
 * {@link #allowedValues()} to produce a user-facing list of valid level names when exposing a
 * minimum-level configuration option.</p>
 *
 * <p>The seven enum constants carry {@link HostAccess.Export} so that GraalVM guest-language
 * code can read them without extra host-access configuration. The helper methods
 * ({@link #atLeast}, {@link #noGreater}, {@link #allowedValues}) do <em>not</em> carry that
 * annotation; whether guest code can invoke them depends on the host-access policy applied by
 * the embedding runtime.</p>
 *
 * <p>JAXB serializes and deserializes this enum via {@link LogLevelEnumXmlAdapter}, which
 * normalizes the input, so {@code "warn"}, {@code "Warn"}, and {@code "WARN"} all unmarshal
 * to {@link #WARN}.</p>
 */
public enum LogLevel {

  /**
   * Trace level (ordinal 0). Fine-grained execution tracing; too verbose for routine debug use.
   */
  @HostAccess.Export
  TRACE,

  /**
   * Debug level (ordinal 1). Diagnostic details useful when investigating a specific problem but
   * not needed during normal operation.
   */
  @HostAccess.Export
  DEBUG,

  /**
   * Informational level (ordinal 2). Normal operational events confirming expected progress.
   */
  @HostAccess.Export
  INFO,

  /**
   * Warning level (ordinal 3). Unexpected conditions that do not prevent execution from
   * continuing.
   */
  @HostAccess.Export
  WARN,

  /**
   * Error level (ordinal 4). Failures that may affect results but do not halt execution entirely.
   */
  @HostAccess.Export
  ERROR,

  /**
   * Fatal level (ordinal 5). Conditions from which execution cannot continue meaningfully.
   */
  @HostAccess.Export
  FATAL,

  /**
   * Sentinel (ordinal 6) used to disable all logging. No message is ever emitted at this level;
   * using it as a minimum threshold via {@link #atLeast(LogLevel)} suppresses all output.
   * Appears as the final entry in arrays returned by {@link #allowedValues()}.
   */
  @HostAccess.Export
  OFF;

  /**
   * Returns {@code true} if this level is at least as severe as {@code logLevel}.
   *
   * <p>Bridge implementations use this to apply a minimum threshold:
   * {@code messageLevel.atLeast(configuredMinimum)} is {@code true} when the message should be
   * forwarded.</p>
   *
   * @param logLevel the threshold to compare against; must not be {@code null}.
   * @return {@code true} when this level's ordinal is greater than or equal to
   * {@code logLevel}'s ordinal.
   * @throws NullPointerException if {@code logLevel} is {@code null}.
   */
  public boolean atLeast (LogLevel logLevel) {

    return this.ordinal() >= logLevel.ordinal();
  }

  /**
   * Returns {@code true} if this level is no more severe than {@code logLevel}.
   *
   * <p>Use this to check whether a level falls at or below a configured ceiling:
   * {@code level.noGreater(ceiling)} is {@code true} when the level does not exceed it.</p>
   *
   * @param logLevel the ceiling to compare against; must not be {@code null}.
   * @return {@code true} when this level's ordinal is less than or equal to
   * {@code logLevel}'s ordinal.
   * @throws NullPointerException if {@code logLevel} is {@code null}.
   */
  public boolean noGreater (LogLevel logLevel) {

    return this.ordinal() <= logLevel.ordinal();
  }

  /**
   * Returns the lowercase constant names for this level and every level above it through
   * {@link #OFF}.
   *
   * <p>Use this to enumerate valid values for a user-facing minimum-level configuration option.
   * For example, calling this on {@link #INFO} returns
   * {@code ["info", "warn", "error", "fatal", "off"]}.</p>
   *
   * @return a new array of lowercase level names in declaration order from this level through
   * {@link #OFF}; never {@code null} and never empty.
   */
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
