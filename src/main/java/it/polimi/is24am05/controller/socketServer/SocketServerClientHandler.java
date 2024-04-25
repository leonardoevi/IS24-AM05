package it.polimi.is24am05.controller.socketServer;

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

    private final SocketServer parent;

    public SocketServerClientHandler(Socket socket, SocketServer parent) {
        this.socket = socket;
        this.parent = parent;
    }

    @Override
    public void run() {
        try {
            // Initialize input and output channels
            final Scanner in = new Scanner(socket.getInputStream());
            final PrintWriter out = new PrintWriter(socket.getOutputStream(), true );

            // Handle communication with client
            while (true) {
                final String line = in.nextLine();

                // Check if the client wants to close the connection
                if (line.equals("quit")) {
                    break;
                } else {
                    // Used only for debug
                    System.out.println("Received: " + line + " from: " + socket.getRemoteSocketAddress());

                    // Sending messages must be sync to avoid interference with Server sendBroadcast()
                    synchronized (this.socket) {
                        out.println("Received: " + line + " from you <3");
                    }

                    parent.sendBroadcast("Received: " + line + " from " + socket.getRemoteSocketAddress(), this.socket);
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
}
