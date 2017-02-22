package com.nutrons.framework.subsystems;

import com.nutrons.framework.Subsystem;
import com.nutrons.framework.util.FlowOperators;
import edu.wpi.first.wpilibj.Preferences;
import io.reactivex.Flowable;

public class Settings implements Subsystem {

  /**
   * Provides a Flowable for a specific property on the WpiSmartDashboard's settings table.
   */
  public Flowable<Double> getProperty(String key, double backup) {
    return FlowOperators.toFlow(() ->
        Preferences.getInstance().getDouble(key, backup))
        .distinctUntilChanged();
  }

  @Override
  public void registerSubscriptions() {
    //Intentionally left empty
  }
}