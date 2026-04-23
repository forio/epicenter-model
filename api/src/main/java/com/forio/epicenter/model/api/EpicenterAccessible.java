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

/**
 * Service contract that an embedding runtime must implement to supply capabilities to model code.
 *
 * <p>{@link Epicenter} forwards every model-facing call to the implementation of this interface
 * installed for the current thread. The implementation owns all service concerns: class loader
 * policy, thread pool configuration, actor resolution, proxy endpoint data, resource access,
 * event routing, output persistence, logging, and the callback registry. Model code sees none of
 * those concerns directly.</p>
 *
 * <h2>Thread safety</h2>
 * <p>Because {@link Epicenter} stores the bridge in an {@link InheritableThreadLocal}, a single
 * implementation instance may be called concurrently from multiple threads that inherited it.
 * Implementations must therefore be thread-safe.</p>
 *
 * <h2>Checked exceptions</h2>
 * <p>{@link #read(Path)} is the only method that declares a checked exception. Implementations
 * of other methods may still throw runtime exceptions when inputs are invalid or underlying
 * services fail.</p>
 *
 * <h2>Ownership</h2>
 * <p>The runtime creates and manages implementations. Model code never holds a direct reference
 * to an implementation. Resources returned by {@link #read} and pools returned by
 * {@link #constructForkJoinPool} are owned by the caller once returned and must be closed or
 * shut down by the caller.</p>
 */
public interface EpicenterAccessible {

  /**
   * Applies the runtime's class loader policy to the given thread.
   *
   * <p>Called before model code runs on a new thread so the thread's context class loader
   * reflects the runtime's class loading strategy. Implementations may set a custom class
   * loader, wrap the thread, or return it unchanged.</p>
   *
   * @param thread the thread to configure.
   * @return the thread to use after class loader configuration; typically the same instance
   * that was passed in.
   */
  Thread setContextClassLoader (Thread thread);

  /**
   * Constructs a {@link ForkJoinPool} for use by model code.
   *
   * <p>The caller owns the returned pool and must shut it down. Implementations may apply a
   * custom thread factory, naming, exception handler, or monitoring policy.</p>
   *
   * @param parallelism the requested parallelism level for the pool.
   * @return a new pool configured according to the implementation's policy; never {@code null}.
   */
  ForkJoinPool constructForkJoinPool (int parallelism);

  /**
   * Returns the actor context for the current execution.
   *
   * <p>The returned {@link Actor} identifies the actor whose request triggered this execution.
   * Any field on the actor may be {@code null} when the deployment context does not provide it.</p>
   *
   * <p>Implementations must not return {@code null}. Callers may invoke methods on the returned
   * actor without a null check at the actor reference level.</p>
   *
   * @return the current actor; never {@code null}.
   */
  Actor getActor ();

  /**
   * Returns a snapshot of proxy and API endpoint configuration for the current execution.
   *
   * <p>The snapshot is immutable and contains the scheme, host, port, account, project short
   * names, and shared secret needed to construct Epicenter API URLs and authenticate requests.</p>
   *
   * @return the proxy configuration snapshot, or {@code null} if the runtime does not expose it.
   */
  ProxyConfig getProxyConfig ();

  /**
   * Opens a resource at the given path for reading.
   *
   * <p>The implementation owns path interpretation; it may resolve paths against a classpath, a
   * storage backend, a filesystem mount, or another source. The caller owns the returned stream
   * and must close it.</p>
   *
   * <p>Implementations may return {@code null} to indicate that no resource exists at the given
   * path, or may throw {@link IOException}; the choice is implementation-defined. Callers must
   * handle both outcomes.</p>
   *
   * @param path the path to the resource to open.
   * @return an open input stream positioned at the start of the resource, or {@code null} if the
   * implementation has no resource at the given path.
   * @throws IOException if the implementation encounters an error attempting to open the resource.
   */
  InputStream read (Path path)
    throws IOException;

  /**
   * Registers a consumer to receive publications on the named channel.
   *
   * <p>When {@link #publish(String, Object...)} is called with the same name, the consumer
   * receives the published arguments as an {@code Object[]}. Whether multiple consumers
   * registered for the same name all receive the event, or only the most-recently registered one
   * does, is implementation-defined. Routing, delivery order, and error handling are owned by
   * the implementation.</p>
   *
   * @param name     the publication channel name to subscribe to.
   * @param consumer the consumer to invoke on publication.
   */
  void subscribe (String name, Consumer<Object[]> consumer);

  /**
   * Delivers a named event and its arguments to registered subscribers.
   *
   * <p>The implementation routes the event to consumers registered via
   * {@link #subscribe(String, Consumer)} for the same name. Delivery threading and error
   * handling are implementation-defined.</p>
   *
   * @param name      the publication channel name.
   * @param arguments the arguments to pass to each subscriber.
   */
  void publish (String name, Object... arguments);

  /**
   * Records a named output value with the runtime.
   *
   * <p>What "recording" means is implementation-defined: a database write, a time-series entry,
   * an event emission, or other persistence. Model code calls this to report output that the
   * surrounding system should capture for later retrieval or analysis.</p>
   *
   * @param name  the key under which to store the value.
   * @param value the value to record.
   */
  void record (String name, Object value);

  /**
   * Emits a log message at the specified severity level through the runtime's logging backend.
   *
   * <p>Level filtering is the implementation's responsibility. Callers do not pre-filter before
   * calling this method. Implementations should use {@link LogLevel#atLeast(LogLevel)} to apply
   * a configured minimum threshold before forwarding to the backing logger.</p>
   *
   * @param level   the severity level for the message.
   * @param message the message text to emit.
   */
  void log (LogLevel level, String message);

  /**
   * Invokes a named callback registered in the runtime.
   *
   * <p>Callbacks let model code signal lifecycle events or trigger runtime-owned side effects by
   * name. The runtime's callback registry determines what executes for each name and argument
   * combination. Unrecognized names are silently ignored by well-formed implementations.</p>
   *
   * @param name      the callback name to invoke.
   * @param arguments the arguments to pass to the callback.
   */
  void callback (String name, Object... arguments);
}
