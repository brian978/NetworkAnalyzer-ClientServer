package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Listener extends Thread
{
    protected Socket socket;

    public Listener(Socket socket)
    {
        super("Listener");

        this.socket = socket;
    }

    public void run()
    {

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

    protected void processRequest(BufferedReader in)
    {
        try {
            this.executeCommand(in.readLine());
        } catch (Exception e) {
            System.out.println("Cannot read line from socket");
        }
    }

    protected void executeCommand(String s)
    {
        Process process;
        PrintWriter out;
        String line;

        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            return;
        }

        // Executing the command
        try {
            process = Runtime.getRuntime().exec(s);
        } catch (IOException e) {
            out.println(e.getMessage());
            return;
        }

        // Creating the readers the will, well, read the command output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);

        // Outputting the response to the client
        try {
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Could not read line");
        }

        // Clean up
        try {
            reader.close();
        } catch (IOException e) {
            return;
        }

        out.close();
    }
}
