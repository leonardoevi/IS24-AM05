package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Handles the connection with one client via the socket it is provided
 */
public class SocketServerClientHandler implements Runnable {
    /**
     * Socket connected to the client
     */
    private final Socket socket;

    /**
     * Pointer to the main server class, needed for broadcast communication
     */
    private final SocketServer parent;

    /**
     * Output channel
      */
    private PrintWriter out;

    public SocketServerClientHandler(Socket socket, SocketServer parent) {
        this.socket = socket;
        this.parent = parent;
    }

    @Override
    public void run() {
        try {
            // Initialize input and output channels
            final Scanner in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true );

            // Handle communication with client
            while (true) {
                // Read next line
                final String line = in.nextLine();

                // Check if the client wants to close the connection
                if (line.equals("quit")) {
                    break;
                } else {
                    handleClientInput(line);
                }
            }
            in.close();
            out.close();

            // Unsubscribing from broadcast list
            parent.removeClient(this.socket);

            socket.close();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handles the input from the Client
     * @param inputLine client Input
     */
    private void handleClientInput(String inputLine) {
        // TODO: fill this function according to the protocol

        // Used only for debug
        //System.out.println("Received: " + inputLine + " from: " + socket.getRemoteSocketAddress());

        /*
        // Sending messages must be sync to avoid interference with Server sendBroadcast()
        synchronized (this.socket) {
            out.println("Received: " + inputLine + " from you <3");
        }

        parent.sendBroadcast("Received: " + inputLine + " from " + socket.getRemoteSocketAddress(), this.socket);
         */

        Scanner scanner = new Scanner(inputLine);
        scanner.useDelimiter(",");

        String answer = null;

        if(!scanner.hasNext())
            synchronized (this.socket) {
                out.println("Empty Input");
            }

        switch (scanner.next()) {
            case "newConnection":
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                String name = scanner.next();
                if(scanner.hasNext()) {
                    try {
                        parent.controller.newConnection(name, Integer.parseInt(scanner.next()));
                    } catch (FirstConnectionException | InvalidNumUsersException e) {
                        answer = e.getMessage();
                    }
                } else {
                    try {
                        parent.controller.newConnection(name);
                    } catch (ConnectionRefusedException | FirstConnectionException e) {
                        answer = e.getMessage();
                    }
                }
                break;

            default:
                answer = "Invalid input";
        }

        if(answer != null)
            synchronized (this.socket) {
                out.println(answer);
            }

    }
}
