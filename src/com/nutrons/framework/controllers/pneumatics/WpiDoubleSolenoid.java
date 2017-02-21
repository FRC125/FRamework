package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import io.reactivex.functions.Consumer;

public class WpiDoubleSolenoid implements Consumer<PneumaticEvent> {

  private final DoubleSolenoid comp;

  /**
   * Creates a double solenoid from given port.
   */
  public WpiDoubleSolenoid(int forwardChannel, int reverseChannel) {
    this.comp = new DoubleSolenoid(forwardChannel, reverseChannel);
  }

  public void set(DoubleSolenoid.Value value) {
    comp.set(value);
  }

  @Override
  public void accept(PneumaticEvent event) {
    event.actOn(this);
  }
}
