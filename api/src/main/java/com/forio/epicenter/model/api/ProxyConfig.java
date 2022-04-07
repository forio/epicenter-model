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

public class ProxyConfig {

  private final String accountShortName;
  private final String projectShortName;
  private final String apiScheme;
  private final String apiHost;
  private final String apiSharedSecret;
  private final int externalPort;
  private final int debugPort;

  public ProxyConfig (String accountShortName, String projectShortName, String apiScheme, String apiHost, String apiSharedSecret, int externalPort, int debugPort) {

    this.accountShortName = accountShortName;
    this.projectShortName = projectShortName;
    this.apiScheme = apiScheme;
    this.apiHost = apiHost;
    this.apiSharedSecret = apiSharedSecret;
    this.externalPort = externalPort;
    this.debugPort = debugPort;
  }

  public String getAccountShortName () {

    return accountShortName;
  }

  public String getProjectShortName () {

    return projectShortName;
  }

  public String getApiScheme () {

    return apiScheme;
  }

  public String getApiHost () {

    return apiHost;
  }

  public String getApiSharedSecret () {

    return apiSharedSecret;
  }

  public int getExternalPort () {

    return externalPort;
  }

  public int getDebugPort () {

    return debugPort;
  }
}
