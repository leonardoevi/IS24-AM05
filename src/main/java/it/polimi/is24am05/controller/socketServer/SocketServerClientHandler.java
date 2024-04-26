package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.controller.exceptions.KoException;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.objective.Objective;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static it.polimi.is24am05.controller.socketServer.MessageDecoder.*;

/**
 * Handles the connection with one client via the socket it is provided
 */
public class SocketServerClientHandler implements Runnable {
    /**
     * Socket connected to the client
     */
    private final Socket socket;

    /**
     * Client in game Nickname
     * The Optional is empty if the client is not part of the game.
     */
    private Optional<String> clientNickname = Optional.empty();

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
            parent.removeClient(this);
            // Set disconnected status in game
            parent.controller.disconnect(this.getClientNickname());

            socket.close();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        } catch (NoSuchPlayerException e) {
            System.out.println("Player not found, this exception should not be thrown");;
        }
    }

    /**
     * Handles the input from the Client
     * If a client is logging in, saves its nickname
     * @param inputLine client Input
     */
    private void handleClientInput(String inputLine) {
        // TODO: fill this function according to the protocol

        // If the client is not logged in
        if(clientNickname.isEmpty()){
            handleLogin(inputLine);
            return;
        }

        // If the message comes from a client that is logged in
        List<Object> message;
        try {
            message = decode(inputLine);
        } catch (KoException e) {
            send(e.getMessage());
            return;
        }

        try {
            switch ((String) message.getFirst()) {
                case PLAY_STARTER_CARD:
                    this.parent.controller.playStarterCard(this.getClientNickname(), (Boolean) message.get(1));
                    break;

                case CHOOSE_OBJECTIVE:
                    this.parent.controller.chooseObjective(this.getClientNickname(), (Objective) message.get(1));
                    break;

                case PLACE_CARD:
                    this.parent.controller.placeSide(this.getClientNickname(), (Card) message.get(1), (Boolean) message.get(2), (Integer) message.get(3), (Integer) message.get(4));
                    break;

                case DRAW_DECK:
                    this.parent.controller.drawDeck(this.getClientNickname(), (Boolean) message.get(1));
            }
        } catch (Exception e) {
            send("ko," + e.getMessage());
            return;
        }

        send("ok");
        if(this.parent.controller.game != null){
            parent.sendBroadcast(this.parent.controller.game.toString());
        }

    }

    /**
     * This function is called every time a message is received from a client that is not logged in (has no name).
     * It checks whether the message is a login formatted message, according to the protocol.
     * Tries to log the player in, and stores its name.
     * If it fails, it lets the client know why.
     * @param inputLine message received
     */
    private void handleLogin(String inputLine) {
        // Initialize input string parser
        Scanner scanner = new Scanner(inputLine);
        scanner.useDelimiter(",");

        try {
            if(scanner.next().equals("newConnection")){
                String nickname = scanner.next();

                if(scanner.hasNext())
                    this.parent.controller.newConnection(nickname, scanner.nextInt());
                else
                    this.parent.controller.newConnection(nickname);

                // Set specified nickname
                this.clientNickname = Optional.of(nickname);
                // Subscribe to broadcast list
                parent.addClient(this);
                // Send confirmation to client
                send("ok," + "Hi " + getClientNickname() + "!");
            } else {throw new NoSuchElementException();}
        } catch (NoSuchElementException e) {
            // In case the message is not a login message
            send("ko," + "Identify yourself");
        } catch (ConnectionRefusedException | FirstConnectionException | InvalidNumUsersException e) {
            // In case something went wrong with the login
            send(e.getMessage());
        }
    }

    /**
     * Send a message to the correspondent client
     * @param message message to send in a string format
     */
    public synchronized void send(String message){
        out.println(message);
    }

    /**
     * @return the client Nickname if it is logged in, its address otherwise
     */
    public String getClientNickname() {
        return clientNickname.orElse(socket.getRemoteSocketAddress().toString());
    }
}
