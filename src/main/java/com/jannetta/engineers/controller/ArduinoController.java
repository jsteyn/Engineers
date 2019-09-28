package com.jannetta.engineers.controller;

import com.fazecast.jSerialComm.SerialPort;

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

    static public void writePort(int sendString) {
        byte[] readBuffer = new byte[2048];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        OutputStream outputStream = comPort.getOutputStream();
        byte[] s = new byte[2];

        s[0] = (byte) String.valueOf(sendString).charAt(0);
        s[1] = (char) '\n';
        try {
            outputStream.write(s, 0, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (comPort.bytesAvailable() > 0) {
//                    System.out.println("Available: " + ubxPort.bytesAvailable());
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
//                    System.out.println("Read " + numRead + " bytes.");
                System.out.println(new String(readBuffer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
