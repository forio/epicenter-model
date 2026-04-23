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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.graalvm.polyglot.HostAccess;

/**
 * Identity and role snapshot for the actor whose request triggered a model execution.
 *
 * <p>The Epicenter runtime constructs and populates this type; model code obtains the current
 * instance via {@link Epicenter#getActor()}. The runtime sets only the fields that are meaningful
 * for the active deployment; all others remain {@code null}.</p>
 *
 * <p>Fields carried by this class:</p>
 * <ul>
 *   <li>{@code pseudonymKey} — stable unique identifier; present for any fully authenticated actor.</li>
 *   <li>{@code groupKey} — identifies the group the actor belongs to; {@code null} when not in a group.</li>
 *   <li>{@code groupMandate} — the actor's role within the group; {@code null} when no mandate applies.</li>
 *   <li>{@code worldKey} — identifies the simulation world this actor is associated with; {@code null} when absent.</li>
 *   <li>{@code worldRole} — the actor's role within that world; {@code null} when absent.</li>
 * </ul>
 *
 * <p>JAXB serializes this class as {@code <actor>} using property access. Element names use
 * snake_case: {@code pseudonym_key}, {@code group_key}, {@code group_mandate}, {@code world_key},
 * {@code world_role}. The {@code group_mandate} element is converted by
 * {@link GroupMandateEnumXmlAdapter}.</p>
 *
 * <p>All getter methods carry {@link HostAccess.Export} so that GraalVM guest-language code can
 * read actor properties without extra host-access configuration.</p>
 */
@XmlRootElement(name = "actor")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Actor {

  private GroupMandate groupMandate;
  private String groupKey;
  private String worldKey;
  private String pseudonymKey;
  private String worldRole;

  /**
   * No-arg constructor for JAXB deserialization and incremental field population via setters.
   * All fields start as {@code null}.
   */
  public Actor () {

  }

  /**
   * Constructs a fully populated actor.
   *
   * @param pseudonymKey stable unique identifier for the actor; should be non-{@code null} for a
   *                     fully identified actor.
   * @param groupKey     key of the group the actor belongs to, or {@code null} if not in a group.
   * @param groupMandate the actor's role within the group, or {@code null} if no mandate applies.
   * @param worldKey     key of the simulation world the actor is associated with, or {@code null}.
   * @param worldRole    the actor's role within that world, or {@code null} if absent.
   */
  public Actor (String pseudonymKey, String groupKey, GroupMandate groupMandate, String worldKey, String worldRole) {

    this.pseudonymKey = pseudonymKey;
    this.groupKey = groupKey;
    this.groupMandate = groupMandate;
    this.worldKey = worldKey;
    this.worldRole = worldRole;
  }

  /**
   * Returns the pseudonym key that uniquely and stably identifies this actor across executions.
   *
   * <p>This is the primary actor identifier. It may be {@code null} on instances constructed
   * with the no-arg constructor before {@link #setPseudonymKey(String)} is called.</p>
   *
   * @return the pseudonym key, or {@code null} if not set.
   */
  @HostAccess.Export
  @XmlElement(name = "pseudonym_key", required = true)
  public String getPseudonymKey () {

    return pseudonymKey;
  }

  /**
   * Sets the pseudonym key for this actor.
   *
   * @param pseudonymKey the pseudonym key to store.
   */
  public void setPseudonymKey (String pseudonymKey) {

    this.pseudonymKey = pseudonymKey;
  }

  /**
   * Returns the key of the group this actor belongs to.
   *
   * <p>When non-{@code null}, {@link #getGroupMandate()} typically also returns a non-{@code null}
   * value indicating the actor's role within that group.</p>
   *
   * @return the group key, or {@code null} if the actor has no group association.
   */
  @HostAccess.Export
  @XmlElement(name = "group_key")
  public String getGroupKey () {

    return groupKey;
  }

  /**
   * Sets the group key for this actor.
   *
   * @param groupKey the group key to store, or {@code null} to clear the group association.
   */
  public void setGroupKey (String groupKey) {

    this.groupKey = groupKey;
  }

  /**
   * Returns the actor's role within the current group.
   *
   * <p>Use {@link GroupMandate#gte(GroupMandate)} and related helpers to compare mandates
   * without relying on ordinal values directly.</p>
   *
   * @return the group mandate, or {@code null} if no mandate has been set.
   */
  @HostAccess.Export
  @XmlElement(name = "group_mandate")
  @XmlJavaTypeAdapter(GroupMandateEnumXmlAdapter.class)
  public GroupMandate getGroupMandate () {

    return groupMandate;
  }

  /**
   * Sets the group mandate for this actor.
   *
   * @param groupMandate the mandate to store, or {@code null} to indicate no mandate.
   */
  public void setGroupMandate (GroupMandate groupMandate) {

    this.groupMandate = groupMandate;
  }

  /**
   * Returns the key of the world (simulation instance) this actor is associated with.
   *
   * <p>When non-{@code null}, {@link #getWorldRole()} may also return a non-{@code null} value.</p>
   *
   * @return the world key, or {@code null} if no world context exists.
   */
  @HostAccess.Export
  @XmlElement(name = "world_key")
  public String getWorldKey () {

    return worldKey;
  }

  /**
   * Sets the world key for this actor.
   *
   * @param worldKey the world key to store, or {@code null} to clear the world association.
   */
  public void setWorldKey (String worldKey) {

    this.worldKey = worldKey;
  }

  /**
   * Returns the actor's role within the associated world.
   *
   * <p>The meaning of specific role strings is defined by the runtime and the simulation model.
   * Returns {@code null} when no world role has been set, which includes cases where
   * {@link #getWorldKey()} is also {@code null}.</p>
   *
   * @return the world role string, or {@code null} if none has been set.
   */
  @HostAccess.Export
  @XmlElement(name = "world_role")
  public String getWorldRole () {

    return worldRole;
  }

  /**
   * Sets the world role for this actor.
   *
   * @param worldRole the world role string to store, or {@code null} to clear it.
   */
  public void setWorldRole (String worldRole) {

    this.worldRole = worldRole;
  }
}
