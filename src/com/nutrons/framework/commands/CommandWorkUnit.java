package com.nutrons.framework.commands;

import io.reactivex.Flowable;

public interface CommandWorkUnit {
  Flowable<Terminator> execute(boolean selfTerminating);
}
