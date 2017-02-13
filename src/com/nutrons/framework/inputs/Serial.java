package com.nutrons.framework.inputs;

import com.nutrons.framework.Subsystem;
import com.nutrons.framework.util.IntervalCache;
import edu.wpi.first.wpilibj.SerialPort;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

import java.util.function.Supplier;

import static com.nutrons.framework.util.FlowOperators.toFlow;

/**
 * A wrapper around WPI's SerialPort class which provides
 * Flowables for data sent to the roboRIO over serial.
 */
public class Serial implements Subsystem {

  private static final int DEFAULT_BAUD_RATE = 9600;
  private static final char DEFAULT_TERMINATION_CHARACTER = '\n';
  private SerialPort serial;
  private int bufferSize;
  private int packetLength;
  private Flowable<byte[]> dataStream;
  private char terminationCharacter;
  private PublishProcessor<byte[]> wait;

  /**
   * Create Serial streams from a WPI Serial.
   *
   * @param bufferSize   represents how many bytes to cache unread before clearing buffer
   * @param packetLength represents the length of each read from the buffer
   */
  public Serial(int bufferSize, int packetLength) throws ExceptionInInitializerError {
    this(DEFAULT_BAUD_RATE, SerialPort.Port.kUSB, bufferSize, packetLength,
        DEFAULT_TERMINATION_CHARACTER);
  }

  /**
   * Create Serial streams from a WPI Serial.
   *
   * @param bufferSize           represents how many bytes to cache unread before clearing buffer
   * @param packetLength         represents the length of each read from the buffer
   * @param terminationCharacter allows users to set a custom termination character - default is the
   *                             newline character '\n'
   */
  public Serial(SerialPort.Port port,
                int bufferSize,
                int packetLength,
                char terminationCharacter) throws ExceptionInInitializerError {
    this(DEFAULT_BAUD_RATE, port, bufferSize, packetLength, terminationCharacter);
  }

  /**
   * Create Serial streams from a WPI Serial
   *
   * @param bufferSize           represents how many bytes to cache unread before clearing buffer
   * @param packetLength         represents the length of each read from the buffer
   * @param terminationCharacter allows users to set a custom termination character - default is the
   *                             newline character '\n'
   * @param baudrate             See <a href="https://en.wikipedia.org/wiki/Symbol_rate">Symbol Rate</a>
   */
  public Serial(int baudrate,
                SerialPort.Port port,
                int bufferSize,
                int packetLength,
                char terminationCharacter) throws ExceptionInInitializerError {

    try {
      this.serial = new SerialPort(baudrate, port);
      this.bufferSize = bufferSize;
      this.packetLength = packetLength;
      this.terminationCharacter = terminationCharacter;

      this.serial.enableTermination(terminationCharacter);
      this.wait = PublishProcessor.create();

      Supplier<byte[]> suppler = (Supplier<byte[]>) () -> {
        if (serial.getBytesReceived() > this.bufferSize) { //Clear out old values
          serial.reset();
        }
        return serial.read(packetLength);
      };
      this.dataStream = toFlow(new IntervalCache<byte[]>(100, suppler))
          .filter(x -> x.length == packetLength);
    } catch (ExceptionInInitializerError e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * A Flowable providing data from the serial.
   **/
  public Flowable<byte[]> getDataStream() {
    return this.dataStream;
  }

  @Override
  public void registerSubscriptions() {
    wait.onComplete();
  }
}
