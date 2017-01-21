package com.nutrons.FRamework.consumers;

public class LoopPropertiesEvent implements LoopControllerEvent {

  private final double p;
  private final double i;
  private final double d;
  private final double f;

  public LoopPropertiesEvent(double p, double i, double d, double f) {
    this.p = p;
    this.i = i;
    this.d = d;
    this.f = f;
  }

  @Override
  public void actOn(LoopSpeedController controller) {
    controller.setLoopProperties(p, i, d, f);
  }
}
