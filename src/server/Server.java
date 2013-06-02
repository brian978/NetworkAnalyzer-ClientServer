package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 *
 * @author Brian
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }

    public void run() throws IOException {

        Boolean listening = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (SocketException e) {
            System.out.println("The application is already running");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Could not open a socket on port 4444");
            System.exit(-1);
        }
        
        System.out.println("Server up");

        while (listening) {
            try {
                new Listener(serverSocket.accept(), listening).start();
            } catch (IOException e) {
                System.out.println("Accept failed on port 4444");
                break;
            }
        }
        
        serverSocket.close();
    }
}
