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
package com.forio.epicenter.model.api.mock;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import com.forio.epicenter.model.api.Actor;
import com.forio.epicenter.model.api.EpicenterAccessible;
import com.forio.epicenter.model.api.LogLevel;
import com.forio.epicenter.model.api.ProxyConfig;

/**
 * No-op {@link EpicenterAccessible} implementation for use in tests and minimal embedding
 * scenarios where full runtime services are not needed.
 *
 * <p>Every method either returns a safe default or does nothing, so model code under test can
 * call {@link com.forio.epicenter.model.api.Epicenter} methods without a real runtime. The
 * default behavior for each method is:</p>
 * <ul>
 *   <li>{@link #setContextClassLoader(Thread)} — returns the thread unchanged.</li>
 *   <li>{@link #constructForkJoinPool(int)} — returns a plain {@link ForkJoinPool} with the
 *       requested parallelism and default settings.</li>
 *   <li>{@link #getActor()} — returns a new empty {@link Actor} (all fields {@code null}); never
 *       returns {@code null} itself, so model code calling methods on the actor won't throw
 *       {@link NullPointerException} at the actor level.</li>
 *   <li>{@link #getProxyConfig()} — returns {@code null}; no meaningful endpoint data exists in
 *       a test context.</li>
 *   <li>{@link #read(Path)} — returns {@code null}; no resource storage is available.</li>
 *   <li>{@link #subscribe}, {@link #publish}, {@link #record}, {@link #log},
 *       {@link #callback} — silent no-ops.</li>
 * </ul>
 *
 * <p>Subclass this implementation and override individual methods to inject test-specific
 * behavior — for example, returning a known actor identity from {@link #getActor()}, serving
 * classpath resources from {@link #read(Path)}, or capturing log output from {@link #log}.</p>
 *
 * <h2>Typical test usage</h2>
 * <pre>
 * Epicenter.setCurrent(new MockEpicenterAccessibleImpl());
 * try {
 *     model.run();
 * } finally {
 *     Epicenter.removeCurrent();
 * }
 * </pre>
 */
public class MockEpicenterAccessibleImpl implements EpicenterAccessible {

  /**
   * Constructs a mock bridge with inert behavior for all methods.
   */
  public MockEpicenterAccessibleImpl () {

  }

  /**
   * Returns the supplied thread unchanged without modifying its context class loader.
   *
   * @param thread the thread passed in by the caller.
   * @return the same {@code thread} instance, unmodified.
   */
  @Override
  public Thread setContextClassLoader (Thread thread) {

    return thread;
  }

  /**
   * Creates a plain {@link ForkJoinPool} with the requested parallelism and default settings.
   *
   * <p>The pool uses {@link ForkJoinPool#defaultForkJoinWorkerThreadFactory}, no uncaught-exception
   * handler, and {@code asyncMode=false}. Setting {@code asyncMode=false} means locally forked
   * subtasks are processed in LIFO order, which is well-suited to recursive divide-and-conquer
   * work. Use {@code asyncMode=true} (not done here) for predominantly independent, event-driven
   * tasks. The caller owns the returned pool and must shut it down.</p>
   *
   * @param parallelism the requested parallelism level.
   * @return a new {@link ForkJoinPool}; never {@code null}.
   */
  @Override
  public ForkJoinPool constructForkJoinPool (int parallelism) {

    return new ForkJoinPool(parallelism, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
  }

  /**
   * Returns a new empty {@link Actor} with all fields set to {@code null}.
   *
   * <p>The returned instance is never {@code null}, so model code that calls actor field getters
   * without null-checking the actor itself will not throw {@link NullPointerException} at the
   * actor reference level. All actor field getters on the returned instance return {@code null}.
   * Override this method in a subclass to supply an actor with known identity data.</p>
   *
   * @return a new {@link Actor} with no fields populated; never {@code null}.
   */
  @Override
  public Actor getActor () {

    return new Actor();
  }

  /**
   * Returns {@code null} because no proxy or API endpoint configuration is available in the mock
   * context.
   *
   * <p>Override in a subclass to supply a specific {@link ProxyConfig} for tests that exercise
   * code paths that depend on endpoint data.</p>
   *
   * @return {@code null}.
   */
  @Override
  public ProxyConfig getProxyConfig () {

    return null;
  }

  /**
   * Returns {@code null} because no resource storage is available in the mock context.
   *
   * <p>Override in a subclass to serve content from a test classpath, an in-memory map, or
   * another source.</p>
   *
   * @param path the path that a real implementation would read; ignored here.
   * @return {@code null}.
   */
  @Override
  public InputStream read (Path path) {

    return null;
  }

  /**
   * Does nothing. Subscription requests are silently discarded.
   *
   * @param name     the channel name; ignored.
   * @param consumer the consumer that would receive publications; ignored.
   */
  @Override
  public void subscribe (String name, Consumer<Object[]> consumer) {

  }

  /**
   * Does nothing. Publication requests are silently discarded.
   *
   * @param name      the channel name; ignored.
   * @param arguments the arguments that would be delivered; ignored.
   */
  @Override
  public void publish (String name, Object... arguments) {

  }

  /**
   * Does nothing. Record requests are silently discarded.
   *
   * @param name  the key under which the value would be recorded; ignored.
   * @param value the value that would be recorded; ignored.
   */
  @Override
  public void record (String name, Object value) {

  }

  /**
   * Does nothing. Log requests are silently discarded.
   *
   * @param level   the severity level of the message; ignored.
   * @param message the message text that would be emitted; ignored.
   */
  @Override
  public void log (LogLevel level, String message) {

  }

  /**
   * Does nothing. Callback invocations are silently discarded.
   *
   * @param name      the callback name that would be invoked; ignored.
   * @param arguments the arguments that would be supplied to the callback; ignored.
   */
  @Override
  public void callback (String name, Object... arguments) {

  }
}
