package com.forio.epicenter.model.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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

  @XmlElement(name = "pseudonym_key", required = true)
  public String getPseudonymKey () {

    return pseudonymKey;
  }

  public void setPseudonymKey (String pseudonymKey) {

    this.pseudonymKey = pseudonymKey;
  }

  @XmlElement(name = "group_mandate")
  @XmlJavaTypeAdapter(GroupMandateEnumXmlAdapter.class)
  public GroupMandate getGroupMandate () {

    return groupMandate;
  }

  public void setGroupMandate (GroupMandate groupMandate) {

    this.groupMandate = groupMandate;
  }

  @XmlElement(name = "world_role")
  public String getWorldRole () {

    return worldRole;
  }

  public void setWorldRole (String worldRole) {

    this.worldRole = worldRole;
  }
}
