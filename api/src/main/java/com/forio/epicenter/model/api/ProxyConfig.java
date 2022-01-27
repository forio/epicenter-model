package com.forio.epicenter.model.api;

public class ProxyConfig {

  private String accountShortName;
  private String projectShortName;
  private String apiHostUrl;
  private String apiSharedSecret;
  private int externalPort;
  private int debugPort;

  public ProxyConfig (String accountShortName, String projectShortName, String apiHostUrl, String apiSharedSecret, int externalPort, int debugPort) {

    this.accountShortName = accountShortName;
    this.projectShortName = projectShortName;
    this.apiHostUrl = apiHostUrl;
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

  public String getApiHostUrl () {

    return apiHostUrl;
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
