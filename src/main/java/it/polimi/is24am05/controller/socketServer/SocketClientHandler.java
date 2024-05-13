package it.polimi.is24am05.controller.socketServer;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;

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
    private ObjectInputStream in;

    /**
     * Output channel
     */
    private ObjectOutputStream out;

    /**
     * Used to check if connection to client is still up
     */
    private volatile String lastKeepAliveSent = "empty", lastKeepAliveReceived = "empty";
    private final ScheduledExecutorService connectionCheckerDemon = Executors.newSingleThreadScheduledExecutor();

    /**
     * Seconds to wait before checking again if client is still connected
     */
    private static final int checkingInterval = 8;

    /**
     * Flag to know whether the client is still connected
     */
    volatile boolean clientConnected = true;

    /**
     * These objects are used to allow a thread to wait() instead of sleep(),
     * avoiding busy waiting inside loops
     */
    private final Object uselessLock_1 = new Object(), uselessLock_2 = new Object();
    private final ExecutorService messageExecutor = Executors.newFixedThreadPool(4);

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
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());

            // Start a demon thread to check connection status with clients every checkingInterval seconds
            this.connectionCheckerDemon
                    .scheduleAtFixedRate(new ConnectionChecker(this, checkingInterval/2), 0, checkingInterval, TimeUnit.SECONDS);
            // Creating pool thread to handle input

            // Handle communication with client while they are connected
            while (clientConnected) {
                // Read next object
                Message message;
                // If there is something in the buffer
                if(socket.getInputStream().available() > 0){
                    // Acquire the message
                    message = (Message) in.readObject();
                } else {
                    message = null;
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
                if (message.title().equals("quitServer")) {
                    break;
                } else if (message.title().equals("pong")) {
                    // Filter heartbeat messages
                    lastKeepAliveReceived = (String) message.arguments().get("id");
                } else {
                    this.messageExecutor.submit(new Thread(()->{
                        handleClientInput(message);
                    }));
                }
            }
            // Call disconnection routine
            clientDisconnected();
        } catch (final IOException e) {
            System.out.println("Exited Loop Via Exception");
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException ignored) {
            // Should not happen
        }
    }

    /**
     * Handles the input from the Client
     * If a client is logging in, its nickname is stored
     * @param message client Input
     */
    private synchronized void handleClientInput(Message message) {
        // If the client is not logged in
        if(!isLoggedIn()) {
            handleLogin(message);
            return;
        }

        // If the message comes from a client that is logged in
        try {
            switch (message.title()) {
                case "joinGame":
                    parent.controller.newConnection((String) message.arguments().get("nickname"));
                    send(new Message("ok", Map.of("nicknames", parent.controller.getUsers())));
                    parent.server.sendBroadcast(
                        new Message("joinGame", Map.of("nickname", clientNickname)),
                        String.valueOf(clientNickname)
                    );
                    break;

                case "setNumberOfPlayers":
                    parent.controller.newConnection(
                        String.valueOf(clientNickname),
                        (int) message.arguments().get("numberOfPlayers")
                    );
                    send(new Message("ok", Map.of()));
                    break;

                case "placeStarterSide":
                    parent.controller.playStarterCard(
                        String.valueOf(clientNickname),
                        (Boolean) message.arguments().get("isFront")
                    );
                    send(new Message("ok", Map.of(
                        "playArea", parent.controller.game.getPlayArea(String.valueOf(clientNickname))
                    )));
                    parent.server.sendBroadcast(
                        new Message("placeStarterSide",
                            Map.of(
                                "nickname", String.valueOf(clientNickname),
                                "playArea", parent.controller.game.getPlayArea(String.valueOf(clientNickname))
                            )
                        ),
                        String.valueOf(clientNickname)
                    );
                    break;

                case "chooseObjectives":
                    parent.controller.chooseObjective(
                        String.valueOf(clientNickname),
                        (String) message.arguments().get("objectiveId")
                    );
                    send(new Message("ok", Map.of()));
                    break;

                case "placeSide":
                    parent.controller.placeSide(
                        String.valueOf(clientNickname),
                        (Card) message.arguments().get("card"),
                        (Boolean) message.arguments().get("isFront"),
                        (int) message.arguments().get("i"),
                        (int) message.arguments().get("j")
                    );
                    send(new Message("ok", Map.of(
                        "playArea", parent.controller.game.getPlayArea(String.valueOf(clientNickname)),
                        "points", parent.controller.game.getPoints(String.valueOf(clientNickname))
                    )));
                    parent.server.sendBroadcast(
                        new Message("placeStarterSide", Map.of(
                            "nickname", String.valueOf(clientNickname),
                            "playArea", parent.controller.game.getPlayArea(String.valueOf(clientNickname)),
                            "points", parent.controller.game.getPoints(String.valueOf(clientNickname))
                        )),
                        String.valueOf(clientNickname)
                    );
                    break;

                case "drawVisibleCard":
                    Card card = (Card) message.arguments().get("card"));
                    parent.controller.drawVisible(
                        String.valueOf(clientNickname),
                        card
                    );
                    boolean isGold = card.getId() > 40;
                    Deck deck = isGold ? parent.controller.game.getGoldDeck()
                        : parent.controller.game.getResourceDeck();
                    send(new Message("ok", Map.of(
                        "deck", deck,
                        "hand", parent.controller.game.getHand(String.valueOf(clientNickname))
                    )));
                    parent.server.sendBroadcast(
                        new Message("drawVisibleCard", Map.of(
                            "nickname", String.valueOf(clientNickname),
                            "isGold", isGold,
                            "deck", deck,
                            "hand", parent.controller.game.getBlurredHand(String.valueOf(clientNickname))
                        )),
                        String.valueOf(clientNickname)
                    );
                    break;

                case "drawDeck":
                    boolean isGold2 = (boolean) message.arguments().get("isGold");
                    parent.controller.drawDeck(
                            String.valueOf(clientNickname),

                    );
                    Deck deck2 = isGold2 ? parent.controller.game.getGoldDeck()
                        : parent.controller.game.getResourceDeck();
                    send(new Message("ok", Map.of(
                        "deck", deck2,
                        "hand", parent.controller.game.getHand(String.valueOf(clientNickname))
                    )));
                    parent.server.sendBroadcast(
                        new Message("drawVisibleCard", Map.of(
                            "nickname", String.valueOf(clientNickname),
                            "isGold", isGold2,
                            "deck", deck2,
                            "hand", parent.controller.game.getBlurredHand(String.valueOf(clientNickname))
                        )),
                        String.valueOf(clientNickname)
                    );
                    break;
            }
        } catch (FirstConnectionException e) {
            send(new Message("ok", Map.of(
                "nicknames", List.of()
            )));
        } catch (Exception e) {
            send(new Message("ko", Map.of(
                "reason", e.toString()
            )));
        }
    }

    /**
     * This function is called every time a message is received from a client that is not logged in (has no name).
     * It checks whether the message is a login formatted message, according to the protocol.
     * Tries to log the player in (meaning it can join a game), and stores its name.
     * If it fails, it lets the client know why.
     * @param message message received
     */
    private void handleLogin(Message message) {
        try {
            if(message.title().equals("joinServer")){
                String nickname = (String) message.arguments().get("nickname");
                // Set specified nickname
                if (parent.server.getJoinedClients().contains(nickname))
                    throw new Exception("Nickname already in use");
                this.clientNickname = Optional.of(nickname);
                // Subscribe to broadcast list
                parent.subscribe(this);
                // Send confirmation to client
                send(new Message("ok", Map.of(
                    "nicknames", parent.server.getJoinedClients()
                )));
            } else throw new Exception("Identify yourself");
        } catch (Exception e) {
            send(new Message("ko", Map.of(
                    "reason", e.toString()
            )));
        }
    }

    /**
     * Send a message to the correspondent client
     * @param message message to send in a string format
     */
    public synchronized void send(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ignored) {}
    }

    /**
     * Routine to call when the client disconnects
     */
    private void clientDisconnected() {
        if(isLoggedIn()) {
            try {
                // Unsubscribing from broadcast list
                parent.unsubscribe(this);
                // Set disconnected status in game
                parent.controller.disconnect(this.getClientNickname());
            } catch (NoSuchPlayerException ignored) {}
        }

        // Deallocate resources
        try {
            in.close();
            out.close();
        } catch (IOException ignored) {}
        this.messageExecutor.shutdown();
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
            client.send(new Message("ping", Map.of("id", randomString)));

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
