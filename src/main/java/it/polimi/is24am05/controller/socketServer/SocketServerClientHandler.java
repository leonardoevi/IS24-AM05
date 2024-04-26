package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Optional;
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

        parent.sendBroadcast(getClientNickname() + " wrote: " + inputLine, this);


        /*
        final String    NEW_CONNECTION = "nc",
                        PLAY_STARTER_CARD = "psc",
                        CHOOSE_OBJECTIVE = "co",
                        PLAY_CARD = "pc",
                        DRAW_CARD = "dc",
                        DRAW_DECK = "dd";


        // Initialize input string parser
        Scanner scanner = new Scanner(inputLine);
        scanner.useDelimiter(",");

        String answer = null;

        // Skip empty inputs
        if(!scanner.hasNext())
            synchronized (this.socket) {
                out.println("Empty Input");
                return;
            }

        String playerNickname;
        // Decode input string
        switch (scanner.next()) {
            case NEW_CONNECTION:
                // Second parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                String name = scanner.next();
                // Commands differ depending on the presence of a third parameter
                if(scanner.hasNext()) {
                    try {
                        parent.controller.newConnection(name, Integer.parseInt(scanner.next()));
                        answer = "ok";
                    } catch (FirstConnectionException | InvalidNumUsersException e) {
                        answer = "ko," + e.getMessage();
                    }
                } else {
                    try {
                        parent.controller.newConnection(name);
                        answer = "ok";
                    } catch (ConnectionRefusedException | FirstConnectionException e) {
                        answer = "ko," + e.getMessage();
                    }
                }
                break;

            case PLAY_STARTER_CARD:
                // Second parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                playerNickname = scanner.next();

                // Third parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }
                
                String sideChoice = scanner.next();
                
                try {
                    this.parent.controller.playStarterCard(playerNickname, Boolean.parseBoolean(sideChoice));
                    answer = "ok";
                } catch (NoSuchPlayerException | MoveNotAllowedException | InvalidStarterSideException | RuntimeException e) {
                    answer = "ko," + e.getMessage();
                }

                break;

            case CHOOSE_OBJECTIVE:
                // Second parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                playerNickname = scanner.next();

                // Third parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                String objectiveChoice = scanner.next();

                // Validate choice
                try {
                    Objective.valueOf(objectiveChoice);
                } catch (IllegalArgumentException e) {
                    answer = "Invalid input";
                    break;
                }

                try {
                    this.parent.controller.chooseObjective(playerNickname, objectiveChoice);
                    answer = "ok";
                } catch (NoSuchPlayerException | ObjectiveNotAllowedException | MoveNotAllowedException e) {
                    answer = "ko," + e.getMessage();
                }

                break;

            case PLAY_CARD:
                // Second parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                playerNickname = scanner.next();

                // Third parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                String cardChoice = scanner.next();
                Card card;
                // Validate choice
                try {
                    card = ResourceCard.valueOf(cardChoice);
                } catch (IllegalArgumentException e) {
                    try {
                        card = GoldCard.valueOf(cardChoice);
                    } catch (IllegalArgumentException e2) {
                        answer = "Invalid input";
                        break;
                    }
                }

                // Fourth parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                boolean frontVisible;
                try {
                    frontVisible = Boolean.parseBoolean(scanner.next());
                } catch (NumberFormatException | InputMismatchException e) {
                    answer = "Invalid input";
                    break;
                }

                // Fifth parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                int i;
                try {
                    i = scanner.nextInt();
                } catch (NumberFormatException | InputMismatchException e) {
                    answer = "Invalid input";
                    break;
                }

                // Sixth parameter expected
                if(!scanner.hasNext()){
                    answer = "Invalid input";
                    break;
                }

                int j;
                try {
                    j = scanner.nextInt();
                } catch (NumberFormatException | InputMismatchException e) {
                    answer = "Invalid input";
                    break;
                }

                try {
                    this.parent.controller.placeSide(playerNickname, card, frontVisible, i, j);
                    answer = "ok";
                } catch (PlacementNotAllowedException | NoAdjacentCardException | InvalidCardException |
                         InvalidCoordinatesException | InvalidSideException | MoveNotAllowedException |
                         NotYourTurnException | NoSuchPlayerException e) {
                    answer = "ko" + e.getMessage();
                }

                break;
            default:
                answer = "Invalid input";
        }

        synchronized (this.socket) {
            out.println(answer);
        }

        if(answer.equals("ok") && parent.controller.game != null)
                parent.sendBroadcast(parent.controller.game.toString());

        */

    }

    /**
     * This function is called every time a message is received from a client that is not logged in (has no name).
     * It checks whether the message is a login formatted message.
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
