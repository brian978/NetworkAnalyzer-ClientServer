package client;

import core.DispatchedApp;

import java.io.*;
import java.net.Socket;

public class Client implements DispatchedApp
{
    protected String address = "127.0.0.1";

    protected int port = 5566;

    protected String command;

    protected Socket socket;

//    /**
//     * @param command The command to be executed by the server
//     */
//    public Client(String command)
//    {
//        this.command = command;
//    }
//
//    /**
//     * @param port    Port for the socket
//     * @param command The command to be executed by the server
//     */
//    public Client(Integer port, String command)
//    {
//        this.command = command;
//
//        this.setAddress(address);
//        this.setPort(port);
//    }

    /**
     * @param address IP address for the socket
     * @param port    Port for the socket
     * @param command The command to be executed by the server
     */
    public Client(String address, Integer port, String command)
    {
        this.command = command;

        this.setAddress(address);
        this.setPort(port);
    }

    /**
     * This is used when wanting to check for a connection
     *
     * @param address IP address for the socket
     * @param port    Port for the socket
     */
    public Client(String address, Integer port)
    {
        this.setAddress(address);
        this.setPort(port);
    }

    protected void setAddress(String address)
    {
        if (address.length() > 0) {
            this.address = address;
        }
    }

    protected void setPort(int port)
    {
        if (port > 0) {
            this.port = port;
        }
    }

    /**
     * Checks if a connection can be made to the application server
     *
     * @param outputResult When this parameter is passed a string will be outputted besides a value
     * @return boolean
     */
    public boolean isServerAvailable(boolean outputResult)
    {
        boolean status = false;

        if (this.connect()) {
            if (outputResult) {
                System.out.println("Connected to server");
            }

            status = true;

            this.disconnect();
        } else if (outputResult) {
            System.out.println("Failed to connect to the server");
        }

        return status;
    }

    /**
     * Connects to the server
     *
     * @return boolean
     */
    protected boolean connect()
    {
        boolean connected = false;

        try {
            this.socket = new Socket(this.address, this.port);
            connected = true;
        } catch (IOException e) {
            // Don't care if connection was successful - it is handled elsewhere
        }

        return connected;
    }

    /**
     * Disconnects from the server
     */
    protected void disconnect()
    {
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                // Don't care if disconnect was successful or not (for now)
            }
        }
    }

    /**
     * @param in If this is passed as null, a buffer reader is created
     * @return string
     */
    protected String readCommandResponse(BufferedReader in)
    {
        boolean internalBuffer = false;
        String response = "";
        String output;

        // Checking the param
        if (in == null) {
            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                internalBuffer = true;
            } catch (IOException e) {
                System.out.println("Could not get input stream for server at " + this.address);
                System.exit(-1);
            }
        }

        // Reading the command output
        try {
            output = in.readLine();

            while (output != null) {
                response += output + "\r\n";
                output = in.readLine();
            }
        } catch (IOException e) {
            response = "Could not execute command";
        }

        // Some info in case no response is given
        if (response.length() == 0) {
            response = "No response received";
        }

        // If the BufferedReader was created internally it will be closed internally
        if (internalBuffer) {
            try {
                in.close();
            } catch (IOException e) {
                // Doesn't matter if it's not closed (for now)
            }
        }

        return response;
    }

    /**
     * Run in non-verbose mode
     */
    public void run()
    {
        this.run(false);
    }

    /**
     * Run depending on verbose param
     *
     * @param verbose When this is set to "true" additional information will be outputted
     */
    public void run(boolean verbose)
    {
        // No need to run the program without a command
        if (this.command.length() == 0) {
            System.exit(0);
        }

        if (!this.connect()) {
            System.out.println("Cannot connect to the server");
            System.exit(-1);
        }

        // Creating the I/O streams
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Could not get I/O for server at " + this.address);
            System.exit(-1);
        }

        if (verbose) {
            System.out.println("Sending command '" + this.command + "' to " + this.address + " on port " + this.port);
        }

        // Sending the command to the server
        out.println(this.command);

        String output;
        output = this.readCommandResponse(in);

        // Cleaning up
        out.close();

        try {
            in.close();
        } catch (IOException e) {
            // Don't care for now
        }

        this.disconnect();

        // Returning the command result to the caller
        System.out.println(output);
    }
}
