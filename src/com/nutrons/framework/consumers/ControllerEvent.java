package com.nutrons.framework.consumers;

public interface ControllerEvent {
  default void actOn(LoopSpeedController controller) {
    throw new EventUnimplementedException(controller.getClass().toString(), this.getClass().toString());
  }

  class EventUnimplementedException extends RuntimeException {
    EventUnimplementedException(String controller, String event) {
      super("Controller \"" + controller +
          "\" is not supported by the event \""
          + event + "\"");
    }
  }
}
