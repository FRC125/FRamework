package com.nutrons.framework.commands;

import io.reactivex.Flowable;

public interface CommandWorkUnit {
  Flowable<? extends Terminator> execute(boolean selfTerminating);
}
