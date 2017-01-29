package com.nutrons.framework.producers;

import static com.nutrons.framework.util.FlowOperators.toFlow;

import edu.wpi.first.wpilibj.SerialPort;

import io.reactivex.Flowable;

/**
 * A wrapper around WPI's SerialE class which provides Flowables for data sent to the roboRIO over serial
 */
public class Serial {
    private final SerialPort serial;
    private final int bufferSize;
    private final int packetLength;
    private final Flowable<byte[]> dataStream;
    private final char terminationCharacter;

    public Serial(int bufferSize, int packetLength, char terminationCharacter) {
        this(9600, SerialPort.Port.kUSB, bufferSize, packetLength, '\n');
    }

    public Serial(SerialPort.Port port, int bufferSize, int packetLength, char terminationCharacter) {
        this(9600, port, bufferSize, packetLength, terminationCharacter);
    }

    /**
     * Create Serial streams from a WPI "Serial"
     * bufferSize represents how many bytes to cache unread before clearing buffer, packetLength represents the length of each read from the buffer
     */
    public Serial(int baudrate, SerialPort.Port port, int bufferSize, int packetLength, char terminationCharacter) {
        this.serial = new SerialPort(baudrate, port);
        this.bufferSize = bufferSize;
        this.packetLength = packetLength;
        this.terminationCharacter = terminationCharacter;

        if (terminationCharacter == '\n') {
            serial.enableTermination();
        } else {
            this.serial.enableTermination(terminationCharacter);
        }

        this.dataStream = toFlow(() -> {
            if (serial.getBytesReceived() > this.bufferSize) {
                serial.reset();
            }
            return serial.read(packetLength);
        }).filter(x -> x.length == packetLength);
    }

    /**
     * A Flowable providing data from the serial
     **/
    public Flowable<byte[]> dataStream() {
        return this.dataStream();
    }
}