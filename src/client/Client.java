package client;

import core.DispatchedApp;

import java.io.*;
import java.net.Socket;

public class Client implements DispatchedApp {

    protected String address = "127.0.0.1";
    protected Integer port = 5566;
    protected String command;

    public static Class[] getPrototype() {
        Class[] c = new Class[1];
        c[0] = String.class;
        return c;
    }

    /**
     * @param command
     */
    public Client(String command) {
        this.command = command;
    }

    /**
     * @param port
     * @param command
     */
    public Client(Integer port, String command) {
        this.command = command;

        this.setAddress(address);
        this.setPort(port);
    }

    /**
     * @param address
     * @param port
     * @param command
     */
    public Client(String address, Integer port, String command) {
        this.command = command;

        this.setAddress(address);
        this.setPort(port);
    }

    protected void setAddress(String address) {
        if (address.length() > 0) {
            this.address = address;
        }
    }

    protected void setPort(Integer port){
        if(port > 0) {
            this.port = port;
        }
    }

    public void run() {
        this.run(false);
    }

    public void run(Boolean verbose) {

        // No need to run the program without a command
        if (this.command.length() == 0) {
            System.exit(0);
        }

        // Opening the socket and sending data to the server
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket(this.address, this.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Could not get I/O for " + this.address);
            System.exit(0);
        }

        if (verbose == true) {
            System.out.println("Sending command '" + this.command + "' to " + this.address + " on port " + this.port);
        }

        // Sending the command to the server
        out.println(this.command);

        // Reading the response
        String response;
        String output = new String();

        try {
            do {
                response = in.readLine();

                while (response != null) {
                    output += response + "\r\n";
                    response = in.readLine();
                }

            } while (response != null && response.length() <= 0);
        } catch (IOException e) {
            output = "Could not execute command";
        }

        // Cleaning up
        out.close();

        try {
            in.close();
            socket.close();
        } catch (IOException e) {
        }

        if(output.length() == 0) {
            output = "No response received (maybe server did not respond)";
        }

        // Returning the command result to the caller
        System.out.println(output);
    }
}
