package com.nutrons.framework.factories;

import com.nutrons.framework.consumers.ControllerEvent;
import com.nutrons.framework.consumers.LoopSpeedController;
import com.nutrons.framework.subsystems.Settings;
import io.reactivex.functions.Consumer;
import java.util.List;

public interface OutputFactory {
  void setControllers(List<LoopSpeedController> controllers);

  void setController(LoopSpeedController controller);

  void setSettingsInstance(Settings settings);

  Consumer<ControllerEvent> motor(int port);

  Settings settingsSubsystem();
}
