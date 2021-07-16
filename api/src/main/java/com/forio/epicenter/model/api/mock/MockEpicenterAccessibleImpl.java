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

import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import com.forio.epicenter.model.api.Actor;
import com.forio.epicenter.model.api.EpicenterAccessible;
import com.forio.epicenter.model.api.LogLevel;

public class MockEpicenterAccessibleImpl implements EpicenterAccessible {

  @Override
  public Thread setContextClassLoader (Thread thread) {

    return thread;
  }

  @Override
  public ForkJoinPool constructForkJoinPool (int parallelism) {

    return new ForkJoinPool(parallelism, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
  }

  @Override
  public Actor getActor () {

    return new Actor();
  }

  @Override
  public void subscribe (String name, Consumer<Object[]> consumer) {

  }

  @Override
  public void publish (String name, Object... arguments) {

  }

  @Override
  public void record (String name, Object value) {

  }

  @Override
  public void log (LogLevel level, String message) {

  }

  @Override
  public void scribble (LogLevel level, String message) {

  }

  @Override
  public void callback (String name, Object... arguments) {

  }
}
