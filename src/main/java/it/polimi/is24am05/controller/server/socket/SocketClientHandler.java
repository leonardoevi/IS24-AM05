package it.polimi.is24am05.controller.server.socket;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.game.Game;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClientHandler extends ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;

    protected String lastHeartBeat = "andrea";

    /**
     * This thread is necessary to handle server input asynchronously
     * This way answering to ping messages is immediate
     * Must be a single thread to avoid synchronization problems
     */
    private final ExecutorService inputHandler = Executors.newSingleThreadExecutor();

    /**
     * This thread periodically check the connection to the client
     */
    private final ScheduledExecutorService connectionChecker = Executors.newSingleThreadScheduledExecutor();

    public SocketClientHandler(Controller controller, Server server, Socket socket) throws IOException {
        super(controller, server);
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());

        // Start checking connection
        connectionChecker.scheduleAtFixedRate(new ConnectionChecker(this.socket), 1, 4, TimeUnit.SECONDS);
    }

    @Override
    public void setGame(Game toSend) {
        send(new Message("Game", Map.of("game", toSend)));
    }

    @Override
    public void addLog(String log) {
        send(new Message("Log", Map.of("log", log)));
    }

    private void send(Message message){
        synchronized(this.out){
            try {
                out.reset();
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                //System.out.println("Error sending message: " + message.title() + " to " + getNickname());
            }
        }
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            // Should not happen
            System.out.println("[" + getNickname() + "] Error getting input stream, returning");
            return;
        }
        while(true){
            try{
                Message message = (Message) in.readObject();

                // Handle heartbeat messages right away
                if(message.title().equals("pong"))
                    lastHeartBeat = (String) message.arguments().get("key");
                else if(message.title().equals("ping"))
                    send(new Message("pong", Map.of("key", message.arguments().get("key"))));
                else
                    inputHandler.submit(() -> handleClientInput(message));

            } catch (Exception e){
                //System.out.println("Error reading from socket: " + getNickname() + " -> " + e.getMessage());
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException ignored) {}
        inputHandler.shutdown();
        connectionChecker.shutdown();
        disconnect();
    }

    private void handleClientInput(Message message) {
        switch (message.title()) {
            case "joinServer":
                super.joinServer((String) message.arguments().get("nickname"));
                break;
            case "joinGame":
                super.joinGame();
                break;
            case "setNumberOfPlayers":
                super.setNumberOfPlayers((int) message.arguments().get("numberOfPlayers"));
                break;
            case "placeStarterSide":
                super.placeStarterSide((boolean) message.arguments().get("isFront"));
                break;
            case "chooseObjective":
                super.chooseObjective((String) message.arguments().get("objectiveId"));
                break;
            case "placeSide":
                super.placeSide(
                        (String) message.arguments().get("cardId"),
                        (boolean) message.arguments().get("isFront"),
                        (int) message.arguments().get("i"),
                        (int) message.arguments().get("j")
                );
                break;
            case "drawVisible":
                super.drawVisible((String) message.arguments().get("cardId"));
                break;
            case "drawDeck":
                super.drawDeck((boolean) message.arguments().get("isGold"));
                break;

            case "leaveServer":
                super.leaveServer();
                break;

            case "disconnect":
                super.disconnect();
                break;
            case "message":
                super.sendMessage((String) message.arguments().get("message"));
                break;
            case "directMessage":
                super.sendMessage((String) message.arguments().get("message"), (String) message.arguments().get("recipient"));
                break;
        }
    }

    /**
     * This class sends a heartbeat message to the client, if the client doesn't answer with the appropriate message,
     * the socket is closed.
     */
    public class ConnectionChecker implements Runnable {
        private final Socket socket;

        public ConnectionChecker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // Generate a random string
            String heartBeat = UUID.randomUUID().toString();

            // Send the string to the server
            send(new Message("ping", Map.of("key", heartBeat)));

            // Wait for 1 seconds
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}

            // If the string did not come back, close the socket
            if(!heartBeat.equals(lastHeartBeat)) {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
