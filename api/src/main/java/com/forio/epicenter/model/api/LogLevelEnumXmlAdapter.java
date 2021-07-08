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

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LogLevelEnumXmlAdapter extends XmlAdapter<String, LogLevel> {

  @Override
  public LogLevel unmarshal (String name) {

    return (name == null) ? null : LogLevel.valueOf(EnumUtility.toEnumName(name));
  }

  @Override
  public String marshal (LogLevel level) {

    return (level == null) ? null : level.name();
  }
}