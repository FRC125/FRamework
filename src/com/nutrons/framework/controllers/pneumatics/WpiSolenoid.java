package com.nutrons.framework.controllers.pneumatics;

import edu.wpi.first.wpilibj.Solenoid;
import io.reactivex.functions.Consumer;

public class WpiSolenoid implements Consumer<PneumaticEvent> {

  private final Solenoid solenoid;

  /**
   * Creates a solenoid from given port.
   *
   * @param port PCM device ID.
   */
  public WpiSolenoid(int port) {
    this.solenoid = new Solenoid(port);
  }

  public void set(boolean on) {
    solenoid.set(on);
  }

  @Override
  public void accept(PneumaticEvent event) {
    event.actOn(this);
  }
}
