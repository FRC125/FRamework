package com.nutrons.framework.subsystems;

import com.nutrons.framework.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.reactivex.functions.Consumer;

import java.util.HashMap;
import java.util.Map;

public class WpiSmartDashboard implements Subsystem {
  private final Map<String, Consumer<Double>> fields;

  public WpiSmartDashboard() {
    this.fields = new HashMap<>();
  }

  public Consumer<Double> getTextField(String key) {
    if (!fields.containsKey(key)) {
      synchronized (fields) {
        if (!fields.containsKey(key)) {
          this.fields.put(key, x -> SmartDashboard.putNumber(key, x));
        }
      }
    }
    return this.fields.get(key);
  }

  @Override
  public void registerSubscriptions() {
    //Intentionally left empty
  }
}
