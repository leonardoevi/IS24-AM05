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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.is24am05.controller.socketServer.MessageDecoder.*;

/**
 * Handles the connection with one client via the socket it is provided
 */
public class SocketClientHandler implements Runnable {
    /**
     * Socket connected to the client
     */
    protected final Socket socket;

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
     * Input channels
      */
    private Scanner in;

    /**
     * Output channel
     */
    private PrintWriter out;

    /**
     * Used to check if connection is still up
     */
    private volatile String lastKeepAliveSent = "empty", lastKeepAliveReceived = "empty";
    private final ScheduledExecutorService connectionCheckerDemon = Executors.newSingleThreadScheduledExecutor();
    /**
     * Seconds to wait before checking again if client is still connected
     */
    private static final int checkingInterval = 4;

    /**
     * Flag to know whether the client is still connected
     */
    volatile boolean clientConnected = true;

    /**
     * These objects are used to allow a thread to wait() instead of sleep(),
     * avoiding busy waiting inside loops
     */
    private final Object uselessLock_1 = new Object(), uselessLock_2 = new Object();

    /**
     * Constructor
     * @param socket to listen to
     * @param parent Main server accepting connections
     */
    public SocketClientHandler(Socket socket, SocketServer parent) {
        this.socket = socket;
        this.parent = parent;
    }

    /**
     * This class asynchronously handles client's requests.
     * When the clients quit or looses connection to the server, the proper Controller method is invoked and
     * the thread terminates.
     */
    @Override
    public void run() {
        try {
            // Initialize input and output channels
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true );

            // Start a demon thread to check connection status with clients each 5 seconds
            this.connectionCheckerDemon
                    .scheduleAtFixedRate(new ConnectionChecker(this, checkingInterval/2), 0, checkingInterval, TimeUnit.SECONDS);

            // Handle communication with client while they are connected
            while (clientConnected) {
                // Read next line
                String line = null;
                // If there is something in the buffer
                if(socket.getInputStream().available() > 0){
                    // Acquire the message
                    line = in.nextLine();
                } else {
                    // Wait some time and go back to checking the buffer
                    try {   synchronized (uselessLock_1) {  uselessLock_1.wait(100); }
                        continue;
                    } catch (InterruptedException e) {
                        // Should never happen
                        System.out.println(e.getMessage());
                        System.out.println(getClientNickname() + " client Handler crashed");
                        break;
                    }
                }

                // Handle the request
                // Check if the client wants to close the connection
                if (line.equals("quit")) {
                    break;
                } else if (line.startsWith("pong,")) {
                    // Filter heartbeat messages
                    lastKeepAliveReceived = line.substring(5);
                } else {
                    // TODO: Change this line
                    handleClientInput(line);
                    //handleClientInputDebug(line);
                }
            }
            // Call disconnection routine
            clientDisconnected();
        } catch (final IOException e) {
            System.out.println("Exited Loop Via Exception");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handles the input from the Client
     * If a client is logging in, its nickname is stored
     * @param inputLine client Input
     */
    private void handleClientInput(String inputLine) {
        // TODO: fill this function according to the protocol

        // If the client is not logged in
        if(!isLoggedIn()){
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
                    break;

                case DRAW_VISIBLE:
                    this.parent.controller.drawVisible(this.getClientNickname(), (Card) message.get(1));
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

    // Used to make the server a chatServer
    private void handleClientInputDebug(String inputLine){
        // If the client is not logged in
        if(clientNickname.isEmpty()){
            handleLogin(inputLine);
            return;
        }

        parent.sendBroadcast(getClientNickname() + " said: " + inputLine);
    }

    /**
     * This function is called every time a message is received from a client that is not logged in (has no name).
     * It checks whether the message is a login formatted message, according to the protocol.
     * Tries to log the player in (meaning it can join a game), and stores its name.
     * If it fails, it lets the client know why.
     * @param inputLine message received
     */
    private void handleLogin(String inputLine) {
        // Initialize input string parser
        Scanner scanner = new Scanner(inputLine);
        scanner.useDelimiter(",");

        try {
            if(scanner.next().equals("nc")){
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
     * Routine to call when the client disconnects
     */
    private void clientDisconnected(){
        if(isLoggedIn()) {
            try {
                // Unsubscribing from broadcast list
                parent.removeClient(this);
                // Set disconnected status in game
                parent.controller.disconnect(this.getClientNickname());
            } catch (NoSuchPlayerException ignored) {}
        }

        // Deallocate resources
        in.close();
        out.close();
        try {
            socket.close();
        } catch (IOException ignored) {}
        // Kill connection checker demon
        connectionCheckerDemon.shutdownNow();
    }

    /**
     * @return the client Nickname if it is logged in, its address otherwise
     */
    public String getClientNickname() {
        return clientNickname.orElse(socket.getRemoteSocketAddress().toString());
    }

    /**
     * @return True if the client is logged in
     */
    private boolean isLoggedIn(){
        return this.clientNickname.isPresent();
    }

    /**
     * Check if the connection is still up by sending a message to the client.
     * If the client does not send a properly formatted message back, it is considered to have disconnected
     */
    private class ConnectionChecker implements Runnable {
        private final SocketClientHandler client;
        private final int millisecondsToWait;

        /**
         * Checker builder
         * @param client to check connection to
         * @param secondsToWait before declaring the client disconnected if it fails to answer in time
         */
        public ConnectionChecker(SocketClientHandler client, int secondsToWait) {
            this.client = client;
            this.millisecondsToWait = secondsToWait*1000;
        }

        @Override
        public void run() {
            // Generate a random string
            String randomString = UUID.randomUUID().toString();
            // Set the string as the last sent
            lastKeepAliveSent = randomString;
            // Send the string
            client.send("ping," + randomString);

            // Wait some time
            try {
                synchronized (uselessLock_2) {
                    uselessLock_2.wait(millisecondsToWait);
                }
            } catch (InterruptedException e) {
                // Happens if user quits normally while the check is being performed
                System.out.println("Error during connection check");
            }

            // Check if the random string came back in time
            if(!lastKeepAliveReceived.equals(lastKeepAliveSent)){
                System.out.println("Demon says " + getClientNickname() + " disconnected!");
                clientConnected = false;
                client.connectionCheckerDemon.shutdown();
            }
        }
    }
}