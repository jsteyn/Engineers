package com.jannetta.engineers;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class Engineers {


    static public void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SerialPort port = readPort();
        Runnable action = new ReadThread(port);
        Thread t = new Thread(action);
        t.start();
        int input = 0;
        while (input != 999) {
            System.out.print("Input: ");
            input = getInt();
            if (input != 999)
            writePort(port, input);
        }
        port.closePort();
        System.exit(0);

    }

    private void readSerial() {
    }

    static SerialPort readPort() {

        System.out.println("\nUsing Library Version v" + SerialPort.getVersion());
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("\nAvailable Ports:\n");
        for (int i = 0; i < ports.length; ++i)
            System.out.println("   [" + i + "] " + ports[i].getSystemPortName() + ": " + ports[i].getDescriptivePortName() + " - " + ports[i].getPortDescription());
        SerialPort ubxPort;
        System.out.print("\nChoose your desired serial port or enter -1 to specify a port directly: ");
        int serialPortChoice = getInt();

        if (serialPortChoice == -1) {
            String serialPortDescriptor = "";
            System.out.print("\nSpecify your desired serial port descriptor: ");
            try {
                Scanner inputScanner = new Scanner(System.in);
                serialPortDescriptor = inputScanner.nextLine();
            } catch (Exception e) {
            }
            ubxPort = SerialPort.getCommPort(serialPortDescriptor);
        } else
            ubxPort = ports[serialPortChoice];
        return ubxPort;
    }

    static private void writePort(SerialPort comPort, int sendString) {
        System.out.println("Writing to comPort: " + comPort);
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
    }

    static private int getInt() {
        int serialPortChoice = -1;
        try {
            Scanner inputScanner = new Scanner(System.in);
            serialPortChoice = inputScanner.nextInt();
        } catch (Exception e) {
        }
        return serialPortChoice;
    }
}
