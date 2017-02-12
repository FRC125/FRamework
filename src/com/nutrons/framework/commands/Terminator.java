package com.nutrons.framework.commands;

/**
 * A runnable which cleans-up a completed or interrupted command.
 * Terminators' run method must be idempotent.
 */
public interface Terminator extends Runnable {

}
