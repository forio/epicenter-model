package com.forio.epicenter.model.api;

import java.util.function.Consumer;

public interface EpicenterAccessible {

  Actor getActor ();

  void subscribe (String name, Consumer<Object[]> consumer);

  void publish (String name, Object... arguments);

  void record (String name, Object value);

  void log (LogLevel level, String message);

  void scribble (LogLevel level, String message);

  void callback (String name, Object... arguments);
}
