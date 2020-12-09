package com.forio.epicenter.model.api;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GroupMandateEnumXmlAdapter extends XmlAdapter<String, GroupMandate> {

  @Override
  public GroupMandate unmarshal (String name) {

    return (name == null) ? null : GroupMandate.valueOf(name);
  }

  @Override
  public String marshal (GroupMandate role) {

    return (role == null) ? null : role.name();
  }
}
