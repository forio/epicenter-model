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

public enum GroupMandate {

  @HostAccess.Export
  FACILITATOR,
  @HostAccess.Export
  REVIEWER,
  @HostAccess.Export
  LEADER,
  @HostAccess.Export
  PARTICIPANT;

  public boolean lt (GroupMandate mandate) {

    return ordinal() < mandate.ordinal();
  }

  public boolean lte (GroupMandate mandate) {

    return ordinal() <= mandate.ordinal();
  }

  public boolean gte (GroupMandate mandate) {

    return ordinal() >= mandate.ordinal();
  }

  public boolean gt (GroupMandate mandate) {

    return ordinal() > mandate.ordinal();
  }
}
