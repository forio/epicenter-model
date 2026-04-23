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
 * Enumerated roles describing an actor's participation mandate within a group.
 *
 * <p>Declaration order is contractual: {@link #FACILITATOR} has ordinal 0, {@link #REVIEWER} 1,
 * {@link #LEADER} 2, {@link #PARTICIPANT} 3. The comparison helpers ({@link #lt}, {@link #lte},
 * {@link #gte}, {@link #gt}) operate on these ordinals, so inserting or reordering constants is
 * a breaking change.</p>
 *
 * <p>"Greater" in the ordering means later in the declaration sequence, not higher authority or
 * seniority. Callers assign semantic meaning to the ordering for their own access-control rules.</p>
 *
 * <p>The four enum constants carry {@link HostAccess.Export} so that GraalVM guest-language code
 * can read them without extra host-access configuration. The comparison methods ({@link #lt},
 * {@link #lte}, {@link #gte}, {@link #gt}) do <em>not</em> carry that annotation; whether guest
 * code can invoke them depends on the host-access policy applied by the embedding runtime.</p>
 *
 * <p>JAXB serializes and deserializes this enum as a plain string constant name (e.g.,
 * {@code LEADER}) via {@link GroupMandateEnumXmlAdapter}.</p>
 */
public enum GroupMandate {

  /**
   * Facilitator mandate (ordinal 0). Assigned to actors who coordinate or guide the group session.
   */
  @HostAccess.Export
  FACILITATOR,

  /**
   * Reviewer mandate (ordinal 1). Assigned to actors who observe and evaluate a session without
   * leading it.
   */
  @HostAccess.Export
  REVIEWER,

  /**
   * Leader mandate (ordinal 2). Assigned to the designated lead actor within a group.
   */
  @HostAccess.Export
  LEADER,

  /**
   * Participant mandate (ordinal 3). The default role for group members without a specialized
   * function.
   */
  @HostAccess.Export
  PARTICIPANT;

  /**
   * Returns {@code true} if this mandate precedes {@code mandate} in the declaration order.
   *
   * @param mandate the mandate to compare against; must not be {@code null}.
   * @return {@code true} when this mandate's ordinal is strictly less than {@code mandate}'s.
   * @throws NullPointerException if {@code mandate} is {@code null}.
   */
  public boolean lt (GroupMandate mandate) {

    return ordinal() < mandate.ordinal();
  }

  /**
   * Returns {@code true} if this mandate precedes or equals {@code mandate} in declaration order.
   *
   * @param mandate the mandate to compare against; must not be {@code null}.
   * @return {@code true} when this mandate's ordinal is less than or equal to {@code mandate}'s.
   * @throws NullPointerException if {@code mandate} is {@code null}.
   */
  public boolean lte (GroupMandate mandate) {

    return ordinal() <= mandate.ordinal();
  }

  /**
   * Returns {@code true} if this mandate equals or follows {@code mandate} in declaration order.
   *
   * @param mandate the mandate to compare against; must not be {@code null}.
   * @return {@code true} when this mandate's ordinal is greater than or equal to {@code mandate}'s.
   * @throws NullPointerException if {@code mandate} is {@code null}.
   */
  public boolean gte (GroupMandate mandate) {

    return ordinal() >= mandate.ordinal();
  }

  /**
   * Returns {@code true} if this mandate follows {@code mandate} in the declaration order.
   *
   * @param mandate the mandate to compare against; must not be {@code null}.
   * @return {@code true} when this mandate's ordinal is strictly greater than {@code mandate}'s.
   * @throws NullPointerException if {@code mandate} is {@code null}.
   */
  public boolean gt (GroupMandate mandate) {

    return ordinal() > mandate.ordinal();
  }
}
