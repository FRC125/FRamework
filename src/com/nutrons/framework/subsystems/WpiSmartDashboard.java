package com.nutrons.framework.subsystems;

import com.nutrons.framework.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.reactivex.functions.Consumer;

import java.util.HashMap;
import java.util.Map;

public class WpiSmartDashboard implements Subsystem {
  private final Map<String, Consumer<Double>> doubleFields;
  private final Map<String, Consumer<String>> stringFields;
  private final Map<String, Consumer<Boolean>> booleanFields;

  public WpiSmartDashboard() {
    this.doubleFields = new HashMap<>();
    this.stringFields = new HashMap<>();
    this.booleanFields = new HashMap<>();
  }

  public Consumer<Double> getTextFieldDouble(String key) {
    if (!doubleFields.containsKey(key)) {
      synchronized (doubleFields) {
        if (!doubleFields.containsKey(key)) {
          this.doubleFields.put(key, x -> SmartDashboard.putNumber(key, x));
        }
      }
    }
    return this.doubleFields.get(key);
  }

  public Consumer<String> getTextFieldString(String key) {
    if (!stringFields.containsKey(key)) {
      synchronized (stringFields) {
        if (!stringFields.containsKey(key)) {
          this.stringFields.put(key, x -> SmartDashboard.putString(key, x));
        }
      }
    }
    return this.stringFields.get(key);
  }

  public Consumer<Boolean> getTextFieldBoolean(String key) {
    if (!booleanFields.containsKey(key)) {
      synchronized (booleanFields) {
        if (!booleanFields.containsKey(key)) {
          this.booleanFields.put(key, x -> SmartDashboard.putBoolean(key, x));
        }
      }
    }
    return this.booleanFields.get(key);
  }

  @Override
  public void registerSubscriptions() {
    //Intentionally left empty
  }
}
