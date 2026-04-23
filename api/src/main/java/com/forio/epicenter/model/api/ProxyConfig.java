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

/**
 * Immutable snapshot of the proxy and API endpoint coordinates for the current deployment.
 *
 * <p>The runtime supplies a populated instance via {@link EpicenterAccessible#getProxyConfig()};
 * model code retrieves it through {@link Epicenter#getProxyConfig()}. All fields are set at
 * construction time; the class has no setters and no default constructor.</p>
 *
 * <p>A typical Epicenter API base URL has the form:
 * {@code {apiScheme}://{apiHost}:{externalPort}/api/v2/{accountShortName}/{projectShortName}}.</p>
 *
 * <p>{@code apiSharedSecret} is an authentication credential. Do not log it, include it in
 * recorded output values, or expose it to untrusted code or guest-language scripts.</p>
 *
 * <p>This class carries no JAXB or GraalVM annotations; it is a plain Java value type for
 * programmatic use within model code and bridge implementations.</p>
 */
public class ProxyConfig {

  private final String accountShortName;
  private final String projectShortName;
  private final String apiScheme;
  private final String apiHost;
  private final String apiSharedSecret;
  private final int externalPort;
  private final int debugPort;

  /**
   * Constructs an immutable proxy configuration snapshot.
   *
   * <p>No validation is performed. String parameters may be {@code null} if the caller does not
   * have a value for them; port parameters accept any {@code int} value with no range check. Each
   * getter returns exactly what was passed here.</p>
   *
   * @param accountShortName the Forio account identifier for this deployment.
   * @param projectShortName the project or model identifier within the account.
   * @param apiScheme        the URL scheme for API calls, typically {@code http} or {@code https}.
   * @param apiHost          the hostname for API calls.
   * @param apiSharedSecret  the shared secret for authenticating Epicenter API requests;
   *                         treat as a credential — do not log or expose.
   * @param externalPort     the port number for external proxy traffic; no range validation is applied.
   * @param debugPort        the port number for debug access to the runtime; no range validation is applied.
   */
  public ProxyConfig (String accountShortName, String projectShortName, String apiScheme, String apiHost, String apiSharedSecret, int externalPort, int debugPort) {

    this.accountShortName = accountShortName;
    this.projectShortName = projectShortName;
    this.apiScheme = apiScheme;
    this.apiHost = apiHost;
    this.apiSharedSecret = apiSharedSecret;
    this.externalPort = externalPort;
    this.debugPort = debugPort;
  }

  /**
   * Returns the Forio account short name for this deployment.
   *
   * <p>Used as a path segment when constructing Epicenter API URLs.</p>
   *
   * @return the account short name supplied at construction time; {@code null} only if
   * {@code null} was passed to the constructor.
   */
  public String getAccountShortName () {

    return accountShortName;
  }

  /**
   * Returns the project short name for this deployment.
   *
   * <p>Used as a path segment in Epicenter API URLs, following the account short name.</p>
   *
   * @return the project short name supplied at construction time; {@code null} only if
   * {@code null} was passed to the constructor.
   */
  public String getProjectShortName () {

    return projectShortName;
  }

  /**
   * Returns the URL scheme used for Epicenter API calls, typically {@code http} or {@code https}.
   *
   * @return the API URL scheme supplied at construction time; {@code null} only if {@code null}
   * was passed to the constructor.
   */
  public String getApiScheme () {

    return apiScheme;
  }

  /**
   * Returns the hostname used for Epicenter API calls.
   *
   * @return the API hostname supplied at construction time; {@code null} only if {@code null}
   * was passed to the constructor.
   */
  public String getApiHost () {

    return apiHost;
  }

  /**
   * Returns the shared secret for authenticating requests to the Epicenter API.
   *
   * <p>Treat this value as a credential. Do not log it, include it in recorded output, or pass
   * it to untrusted code or guest-language scripts.</p>
   *
   * @return the shared secret supplied at construction time; {@code null} only if {@code null}
   * was passed to the constructor.
   */
  public String getApiSharedSecret () {

    return apiSharedSecret;
  }

  /**
   * Returns the port number for external proxy traffic.
   *
   * <p>Use this as the port component when building Epicenter API base URLs reachable from
   * outside the deployment's internal network.</p>
   *
   * @return the external proxy port.
   */
  public int getExternalPort () {

    return externalPort;
  }

  /**
   * Returns the port number for debug access to the runtime.
   *
   * <p>This port is distinct from the external proxy port and is intended for development and
   * troubleshooting tooling rather than production API calls.</p>
   *
   * @return the debug port.
   */
  public int getDebugPort () {

    return debugPort;
  }
}
