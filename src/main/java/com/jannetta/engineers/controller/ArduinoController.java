package com.jannetta.engineers.controller;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.io.OutputStream;

public class ArduinoController {

    static ArduinoController arduinoController = null;
    static SerialPort comPort = null;

    private ArduinoController(String serialPortDescriptor) {
        // prevent instantiation
        System.out.println("Opening port " + serialPortDescriptor);
        this.comPort = SerialPort.getCommPort(serialPortDescriptor);
    }

    static public ArduinoController getInstance(String serialPortDescriptor) {
        if (arduinoController == null) {
            arduinoController = new ArduinoController(serialPortDescriptor);
        }
        return arduinoController;
    }

    static public String writePort(int sendString, int bytestoread, int timeout) {
        System.out.println("write " + sendString);
        byte[] readBuffer = new byte[2048];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 500, 0);
        OutputStream outputStream = comPort.getOutputStream();
        byte[] s = new byte[2];
        s[0] = (byte) String.valueOf(sendString).charAt(0);
        s[1] = (char) '\n';
        try {
            outputStream.write(s, 0, 2);
            int numRead = comPort.readBytes(readBuffer, bytestoread);
        } catch (IOException err) {
            err.printStackTrace();
        }
        comPort.closePort();
        return (new String(readBuffer)).trim();

    }
}
