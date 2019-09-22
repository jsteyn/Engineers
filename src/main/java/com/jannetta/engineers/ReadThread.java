package com.jannetta.engineers;

import com.fazecast.jSerialComm.SerialPort;

public class ReadThread implements Runnable {
    SerialPort ubxPort;

    public ReadThread(SerialPort ubxPort) {
        this.ubxPort = ubxPort;
    }

    @Override
    public void run() {
        byte[] readBuffer = new byte[2048];
        ubxPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        while (true) {
            try {
                if (ubxPort.bytesAvailable() > 0) {
//                    System.out.println("Available: " + ubxPort.bytesAvailable());
                    int numRead = ubxPort.readBytes(readBuffer, readBuffer.length);
//                    System.out.println("Read " + numRead + " bytes.");
                    System.out.println(new String(readBuffer));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
