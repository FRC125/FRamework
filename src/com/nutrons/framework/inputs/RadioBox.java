package com.nutrons.framework.inputs;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import com.nutrons.framework.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.Map;

public class RadioBox<T> implements Subsystem {
  private final SendableChooser<T> chooser;
  private final Map<String, T> options;
  private final String name;
  private Map.Entry<String, T> defaultOption;

  public RadioBox(String name, Map<String, T> options, String defaultOption) {
    this.name = name;
    this.chooser = new SendableChooser<>();
    this.options = new HashMap<>();
    Observable<Map.Entry<String, T>> optionStream = Observable.fromIterable(options.entrySet());
    optionStream.filter(x -> x.getKey().equals(defaultOption))
        .blockingSubscribe(x -> this.defaultOption = x);
    optionStream.filter(x -> !x.getKey().equals(defaultOption))
        .blockingSubscribe(x -> this.options.put(x.getKey(), x.getValue()));
  }

  @Override
  public void registerSubscriptions() {
    if (defaultOption != null) {
      chooser.addDefault(defaultOption.getKey(), defaultOption.getValue());
    }
    Observable.fromIterable(options.entrySet()).blockingSubscribe(x ->
        chooser.addObject(x.getKey(), x.getValue()));
    SmartDashboard.putData(name, chooser);
  }

  public Flowable<T> selected() {
    return toFlow(chooser::getSelected).distinctUntilChanged();
  }
}
