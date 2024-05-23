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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.graalvm.polyglot.HostAccess;

@XmlRootElement(name = "actor")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Actor {

  private GroupMandate groupMandate;
  private String pseudonymKey;
  private String worldRole;

  public Actor () {

  }

  public Actor (String pseudonymKey, GroupMandate groupMandate, String worldRole) {

    this.pseudonymKey = pseudonymKey;
    this.groupMandate = groupMandate;
    this.worldRole = worldRole;
  }

  @HostAccess.Export
  @XmlElement(name = "pseudonym_key", required = true)
  public String getPseudonymKey () {

    return pseudonymKey;
  }

  public void setPseudonymKey (String pseudonymKey) {

    this.pseudonymKey = pseudonymKey;
  }

  @HostAccess.Export
  @XmlElement(name = "group_mandate")
  @XmlJavaTypeAdapter(GroupMandateEnumXmlAdapter.class)
  public GroupMandate getGroupMandate () {

    return groupMandate;
  }

  public void setGroupMandate (GroupMandate groupMandate) {

    this.groupMandate = groupMandate;
  }

  @HostAccess.Export
  @XmlElement(name = "world_role")
  public String getWorldRole () {

    return worldRole;
  }

  public void setWorldRole (String worldRole) {

    this.worldRole = worldRole;
  }
}
