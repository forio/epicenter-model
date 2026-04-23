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
 * JAXB adapter that converts between {@link GroupMandate} enum values and XML string form.
 *
 * <p>JAXB applies this adapter automatically to the {@code groupMandate} property of {@link Actor}
 * via the {@code @XmlJavaTypeAdapter} annotation on that getter. It may also be applied explicitly
 * to any other JAXB-bound property carrying a {@link GroupMandate}.</p>
 *
 * <p>The XML representation is the exact constant name — {@code FACILITATOR}, {@code REVIEWER},
 * {@code LEADER}, or {@code PARTICIPANT} — with no case normalization. The XML value must match
 * a declared constant name exactly; otherwise {@link #unmarshal(String)} will throw
 * {@link IllegalArgumentException} from {@link GroupMandate#valueOf(String)}. For normalized
 * (case-insensitive) lookup, use {@link EnumUtility#toEnumName(String)} to pre-process the
 * string before calling {@code valueOf}.</p>
 *
 * <p>Both methods return {@code null} when passed {@code null}.</p>
 */
public class GroupMandateEnumXmlAdapter extends XmlAdapter<String, GroupMandate> {

  /**
   * No-arg constructor. JAXB instantiates adapters automatically; direct construction is rarely
   * needed.
   */
  public GroupMandateEnumXmlAdapter () {

  }

  /**
   * Converts an XML string to the corresponding {@link GroupMandate} constant.
   *
   * <p>No whitespace trimming or case folding is performed. The value must exactly match a
   * declared constant name.</p>
   *
   * @param name the XML string to convert; may be {@code null}.
   * @return the matching {@link GroupMandate}, or {@code null} if {@code name} is {@code null}.
   * @throws IllegalArgumentException if {@code name} is non-{@code null} and does not match any
   *                                  declared {@link GroupMandate} constant.
   */
  @Override
  public GroupMandate unmarshal (String name) {

    return (name == null) ? null : GroupMandate.valueOf(name);
  }

  /**
   * Converts a {@link GroupMandate} to its XML string form.
   *
   * <p>Returns {@link GroupMandate#name()}, the declared constant name in uppercase (e.g.,
   * {@code LEADER}).</p>
   *
   * @param role the mandate to convert; may be {@code null}.
   * @return the constant name, or {@code null} if {@code role} is {@code null}.
   */
  @Override
  public String marshal (GroupMandate role) {

    return (role == null) ? null : role.name();
  }
}
