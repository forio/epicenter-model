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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import org.graalvm.polyglot.HostAccess;

/**
 * Static facade through which model code accesses Epicenter runtime services.
 *
 * <p>Every call is forwarded to the {@link EpicenterAccessible} installed for the calling thread.
 * Model code never holds a reference to the bridge directly; it calls only the static methods on
 * this class. The embedding runtime controls the bridge lifecycle via {@link #setCurrent} and
 * {@link #removeCurrent}.</p>
 *
 * <h2>Bridge lifecycle</h2>
 * <p>The runtime installs a bridge before invoking model code and removes it afterward:</p>
 * <pre>
 * Epicenter.setCurrent(myBridge);
 * try {
 *     model.run();
 * } finally {
 *     Epicenter.removeCurrent();
 * }
 * </pre>
 *
 * <h2>Thread inheritance</h2>
 * <p>The bridge is stored in an {@link InheritableThreadLocal}. Child threads created after
 * {@link #setCurrent} is called inherit the same bridge value; threads created before the call
 * do not. {@link #removeCurrent} clears only the calling thread's slot and does not affect
 * threads that inherited the value.</p>
 *
 * <h2>No-bridge behavior</h2>
 * <p>When no bridge is installed, methods that return a value ({@link #constructForkJoinPool},
 * {@link #getActor}, {@link #getProxyConfig}, {@link #read}) return {@code null}. Methods with
 * side effects ({@link #subscribe}, {@link #publish}, {@link #record}, {@link #log},
 * {@link #callback}) are silent no-ops. This is part of the contract, not an error condition.</p>
 *
 * <h2>GraalVM host access</h2>
 * <p>All bridge-forwarding methods carry {@link HostAccess.Export} so that GraalVM guest-language
 * code can invoke them without extra host-access configuration. {@link #setCurrent} and
 * {@link #removeCurrent} are not exported; they are reserved for the Java host.</p>
 */
public class Epicenter {

  private static final InheritableThreadLocal<EpicenterAccessible> epicenterAccessibleLocal = new InheritableThreadLocal<>();

  /**
   * No-arg constructor. This class exposes only static methods; instances serve no purpose.
   */
  public Epicenter () {

  }

  /**
   * Installs a runtime bridge for the current thread.
   *
   * <p>Child threads created after this call inherit the installed bridge. Passing {@code null}
   * clears the slot without requiring a subsequent {@link #removeCurrent()} call.</p>
   *
   * @param epicenterAccessible the bridge to install, or {@code null} to clear the slot.
   */
  public static void setCurrent (EpicenterAccessible epicenterAccessible) {

    epicenterAccessibleLocal.set(epicenterAccessible);
  }

  /**
   * Removes the runtime bridge from the current thread.
   *
   * <p>After this call the current thread follows the no-bridge behavior described in the class
   * documentation. Other threads that inherited the bridge value are not affected.</p>
   */
  public static void removeCurrent () {

    epicenterAccessibleLocal.remove();
  }

  /**
   * Requests a {@link ForkJoinPool} from the installed runtime bridge.
   *
   * <p>The caller owns the returned pool and is responsible for shutting it down. The bridge may
   * apply a custom thread factory, naming, or monitoring policy that a plain
   * {@code new ForkJoinPool()} call would not provide.</p>
   *
   * @param parallelism the requested parallelism level.
   * @return a new pool configured by the bridge, or {@code null} if no bridge is installed.
   */
  @HostAccess.Export
  public static ForkJoinPool constructForkJoinPool (int parallelism) {

    EpicenterAccessible epicenterAccessible;

    return ((epicenterAccessible = epicenterAccessibleLocal.get()) == null) ? null : epicenterAccessible.constructForkJoinPool(parallelism);
  }

  /**
   * Returns the actor context for the current execution from the installed bridge.
   *
   * <p>The returned {@link Actor} carries the identity and role of the actor whose request
   * triggered this execution. Any field on the actor may be {@code null} when the deployment
   * context does not provide it.</p>
   *
   * @return the current actor, or {@code null} if no bridge is installed.
   */
  @HostAccess.Export
  public static Actor getActor () {

    EpicenterAccessible epicenterAccessible;

    return ((epicenterAccessible = epicenterAccessibleLocal.get()) == null) ? null : epicenterAccessible.getActor();
  }

  /**
   * Returns proxy and API endpoint configuration from the installed bridge.
   *
   * <p>The returned {@link ProxyConfig} is an immutable snapshot. Use it to build base URLs for
   * Epicenter API calls or to obtain the shared secret for request authentication.</p>
   *
   * @return the current proxy configuration, or {@code null} if no bridge is installed.
   */
  @HostAccess.Export
  public static ProxyConfig getProxyConfig () {

    EpicenterAccessible epicenterAccessible;

    return ((epicenterAccessible = epicenterAccessibleLocal.get()) == null) ? null : epicenterAccessible.getProxyConfig();
  }

  /**
   * Opens a resource at the given path through the installed bridge.
   *
   * <p>The bridge owns path interpretation; it may resolve against a classpath, a storage
   * backend, or a filesystem mount. The caller owns the returned stream and must close it.</p>
   *
   * @param path the path to the resource to read.
   * @return an open input stream for the resource; {@code null} if no bridge is installed, or if
   * the installed bridge has no resource at the given path.
   * @throws IOException if the installed bridge encounters an error attempting to open the resource.
   */
  @HostAccess.Export
  public static InputStream read (Path path)
    throws IOException {

    EpicenterAccessible epicenterAccessible;

    return ((epicenterAccessible = epicenterAccessibleLocal.get()) == null) ? null : epicenterAccessible.read(path);
  }

  /**
   * Registers a consumer for a named publication channel through the installed bridge.
   *
   * <p>When {@link #publish(String, Object...)} is called with the same name, the consumer
   * receives the argument array. Whether multiple consumers registered for the same name all
   * receive the event is bridge-defined; see {@link EpicenterAccessible#subscribe} for details.
   * Routing and delivery semantics are owned by the bridge.</p>
   *
   * <p>No-op when no bridge is installed.</p>
   *
   * @param name     the channel name to subscribe to.
   * @param consumer the consumer to invoke on publication; receives the arguments as an array.
   */
  @HostAccess.Export
  public static void subscribe (String name, Consumer<Object[]> consumer) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.subscribe(name, consumer);
    }
  }

  /**
   * Publishes a named event with arguments through the installed bridge.
   *
   * <p>The bridge delivers the event to all consumers registered for {@code name} via
   * {@link #subscribe}. Delivery order and error handling are owned by the bridge.</p>
   *
   * <p>No-op when no bridge is installed.</p>
   *
   * @param name      the publication channel name.
   * @param arguments the arguments to deliver with the event.
   */
  @HostAccess.Export
  public static void publish (String name, Object... arguments) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.publish(name, arguments);
    }
  }

  /**
   * Records a named output value through the installed bridge.
   *
   * <p>The bridge defines what recording means — a database write, time-series entry, event
   * stream entry, or other persistence. Call this method to report any output value the
   * surrounding system should capture.</p>
   *
   * <p>No-op when no bridge is installed.</p>
   *
   * @param name  the key under which to record the value.
   * @param value the value to record.
   */
  @HostAccess.Export
  public static void record (String name, Object value) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.record(name, value);
    }
  }

  /**
   * Emits a log message at the given level through the installed bridge.
   *
   * <p>No level filtering is applied in this facade; the bridge is responsible for applying any
   * configured minimum level before forwarding to its logging backend. Pass only messages the
   * caller has already decided should be emitted.</p>
   *
   * <p>No-op when no bridge is installed.</p>
   *
   * @param level   the severity level for the message.
   * @param message the message text to emit.
   */
  @HostAccess.Export
  public static void log (LogLevel level, String message) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.log(level, message);
    }
  }

  /**
   * Invokes a named callback in the installed bridge.
   *
   * <p>Use callbacks to signal lifecycle events or trigger runtime-owned side effects by name.
   * The bridge's callback registry determines what, if anything, executes for each name.</p>
   *
   * <p>No-op when no bridge is installed.</p>
   *
   * @param name      the callback name to invoke.
   * @param arguments the arguments to pass to the callback.
   */
  @HostAccess.Export
  public static void callback (String name, Object... arguments) {

    EpicenterAccessible epicenterAccessible;

    if ((epicenterAccessible = epicenterAccessibleLocal.get()) != null) {
      epicenterAccessible.callback(name, arguments);
    }
  }
}
