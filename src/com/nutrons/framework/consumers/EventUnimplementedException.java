package com.nutrons.framework.consumers;

public class EventUnimplementedException extends RuntimeException {
  public EventUnimplementedException(String controller, String event) {
    super(String.format("Controller \"%s\" is not supported by the event \"%s\"",
        controller, event));
  }
}
