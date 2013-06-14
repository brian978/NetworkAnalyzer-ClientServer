package core;

import client.Client;
import server.Server;

public class Dispatcher
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String mode = null;
        int port = 0;

        for (int i = 0; i < args.length; i++) {

            // Mode
            if (args[i].equals("-mode")) {
                mode = args[++i];
            }

            // Port
            if (args[i].equals("-port") && i + 1 <= args.length) {
                port = Integer.parseInt(args[++i]);
            }
        }

        if (mode != null) {
            if (mode.equals("server")) {
                Server server = new Server(port);
                server.run();
            } else if (mode.equals("client")) {

                // Creating the client with the given command
                if (mode.equals("client")) {
                    boolean verbose = false;
                    boolean checkConnection = false;
                    String address = "";
                    String command = "";

                    for (int i = 0; i < args.length; i++) {

                        // Command to run
                        if (args[i].equals("-command") && i + 1 <= args.length) {
                            command = args[++i];
                        }
                        // Address
                        else if (args[i].equals("-address") && i + 1 <= args.length) {
                            address = args[++i];
                        }
                        // Address
                        else if (args[i].equals("-check") && i + 1 <= args.length) {
                            checkConnection = true;
                        }
                        // Verbose
                        else if (args[i].equals("-v")) {
                            verbose = true;
                        }
                    }

                    if (!checkConnection) {
                        Client client = new Client(address, port, command);
                        client.run(verbose);
                    } else {
                        Client client = new Client(address, port);
                        client.isServerAvailable(true);
                    }
                }
            } else {
                System.out.println("Invalid mode");
            }
        } else {
            System.out.println("Mode argument is required");
        }
    }
}
