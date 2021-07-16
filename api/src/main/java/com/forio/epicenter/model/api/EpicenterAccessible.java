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

import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public interface EpicenterAccessible {

  Thread setContextClassLoader (Thread thread);

  ForkJoinPool constructForkJoinPool (int parallelism);

  Actor getActor ();

  void subscribe (String name, Consumer<ArrayNode> consumer);

  void publish (String name, ArrayNode arguments);

  void record (String name, JsonNode value);

  void log (LogLevel level, String message);

  void scribble (LogLevel level, String message);

  void callback (String name, ArrayNode arguments);
}
