package server;

import core.DispatchedApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class Server implements DispatchedApp {

    protected Integer port = 5566;

    public static Class[] getPrototype()
    {
        Class[] c = new Class[1];
        c[0] = int.class;
        return c;
    }

    public Server() {
    }

    public Server(Integer port) {
        if (port > 0) {
            this.port = port;
        }
    }

    public void run() {

        // Opening the socket for listening
        Boolean listening = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(this.port);
        } catch (SocketException e) {
            System.out.println("The application is already running");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Could not open a socket on port " + this.port);
            System.exit(-1);
        }

        System.out.println("Server started listening on port " + this.port);

        // Started to listing for incoming connections
        while (listening) {
            try {
                new Listener(serverSocket.accept(), listening).start();
            } catch (IOException e) {
                System.out.println("Accept failed on port " + + this.port);
                break;
            }
        }

        System.out.println("Server is shutting down...");

        // Cleaning up
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Could not close the serverSocket.");
            System.exit(-2);
        }
    }
}
