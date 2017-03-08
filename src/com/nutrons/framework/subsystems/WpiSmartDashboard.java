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

  /**
   * Constructor For WpiSmartDashboard.
   */
  public WpiSmartDashboard() {
    this.doubleFields = new HashMap<>();
    this.stringFields = new HashMap<>();
    this.booleanFields = new HashMap<>();
  }

  /**
   * Takes a double and passes it to the Smart Dashboard.
   *
   * @param key decimal passed to the Smartdash.
   */
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

  /**
   * Prints string to the Smart Dashborad.
   *
   * @param key A specific String.
   */
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

  /**
   * Prints Output of a Boolean to the Smart Dashboard.
   *
   * @param key boolean statement.
   */
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
