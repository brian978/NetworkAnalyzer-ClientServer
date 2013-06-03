package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String response;
        String fromUser = args[0];
        String output = new String();

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            socket = new Socket("127.0.0.1", 4444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Could not get I/O for 127.0.0.1");
            System.exit(-1);
        }

        out.println(fromUser);

        try {
            do {
                response = in.readLine();
                
                while(response != null) {
                    output += response + "\r\n";
                    response = in.readLine();
                }
                
            } while (response != null && response.length() <= 0);
        } catch (IOException e) {
            System.out.println("Could not execute command");
        }

        out.close();
        in.close();
        
        System.out.println(output);
    }
}
