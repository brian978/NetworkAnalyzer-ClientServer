package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Listener extends Thread {

    protected Socket socket;
    protected Boolean listening;

    public Listener(Socket socket, Boolean listening) {
        super("Listener");

        this.socket = socket;
        this.listening = listening;
    }

    public void run() {

        try {
            InputStreamReader stream = new InputStreamReader(this.socket.getInputStream());

            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(stream);

            this.processRequest(in);

            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void executeCommand(String s) {
        Process proc;
        PrintWriter out = null;
        String line;

        try {
            proc = Runtime.getRuntime().exec(s);
        } catch (IOException e) {
            System.out.println("Could not execute command");
            return;
        }

        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            return;
        }

        InputStream is = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);

        try {
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Could not read line");
        }

        if (out != null) {
            out.close();
        }

    }

    protected void processRequest(BufferedReader in) {
        try {
            this.executeCommand(in.readLine());
        } catch (SocketException e) {
        } catch (IOException e) {
            System.out.println("Cannot read line from socket");
        }
    }
}
