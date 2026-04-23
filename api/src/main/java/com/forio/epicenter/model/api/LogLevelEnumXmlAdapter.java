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

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter that converts between {@link LogLevel} enum values and XML string form, with
 * case-insensitive and whitespace-tolerant normalization during unmarshaling.
 *
 * <p>On unmarshal the XML string is first normalized by {@link EnumUtility#toEnumName(String)},
 * which converts it to the uppercase underscore-separated form expected by
 * {@link LogLevel#valueOf(String)}. This means {@code "warn"}, {@code "Warn"}, {@code "WARN"},
 * and even {@code "warn level"} all unmarshal successfully to {@link LogLevel#WARN}. This
 * differs from {@link GroupMandateEnumXmlAdapter}, which requires an exact case match.</p>
 *
 * <p>On marshal the constant name is returned unchanged (e.g., {@code WARN}).</p>
 *
 * <p>Both methods return {@code null} when passed {@code null}.</p>
 */
public class LogLevelEnumXmlAdapter extends XmlAdapter<String, LogLevel> {

  /**
   * No-arg constructor. JAXB instantiates adapters automatically; direct construction is rarely
   * needed.
   */
  public LogLevelEnumXmlAdapter () {

  }

  /**
   * Converts an XML string to the corresponding {@link LogLevel} constant.
   *
   * <p>The input is normalized via {@link EnumUtility#toEnumName(String)} before the enum lookup,
   * enabling flexible casing and whitespace in configuration values. The normalized form must
   * match a declared {@link LogLevel} constant name.</p>
   *
   * @param name the XML string to convert; may be {@code null}.
   * @return the matching {@link LogLevel}, or {@code null} if {@code name} is {@code null}.
   * @throws IllegalArgumentException if the normalized form of {@code name} does not match any
   *                                  declared {@link LogLevel} constant.
   */
  @Override
  public LogLevel unmarshal (String name) {

    return (name == null) ? null : LogLevel.valueOf(EnumUtility.toEnumName(name));
  }

  /**
   * Converts a {@link LogLevel} to its XML string form.
   *
   * <p>Returns {@link LogLevel#name()}, the declared constant name in uppercase (e.g.,
   * {@code INFO}).</p>
   *
   * @param level the log level to convert; may be {@code null}.
   * @return the constant name, or {@code null} if {@code level} is {@code null}.
   */
  @Override
  public String marshal (LogLevel level) {

    return (level == null) ? null : level.name();
  }
}
