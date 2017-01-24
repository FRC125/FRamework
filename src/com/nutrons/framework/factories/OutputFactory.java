package com.nutrons.framework.factories;

import com.nutrons.framework.consumers.ControllerEvent;
import com.nutrons.framework.subsystems.Settings;
import io.reactivex.functions.Consumer;
import java.util.Map;

public interface OutputFactory {
  void setControllers(Map<Integer, Consumer<ControllerEvent>> controllers);

  void setSettingsInstance(Settings settings);

  Consumer<ControllerEvent> motor(int port);

  Settings settingsSubsystem();
}
