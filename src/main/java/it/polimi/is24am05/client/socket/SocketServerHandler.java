package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.socket.Message;
import it.polimi.is24am05.controller.server.socket.SocketClientHandler;
import it.polimi.is24am05.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketServerHandler extends ServerHandler {
    private final Socket socket;
    private final ObjectOutputStream outputStream;

    protected volatile String lastHeartBeat = "alesio";

    public static void main(String[] args) {
        try {
            new SocketServerHandler("localhost", "6969", "GUI");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public SocketServerHandler(String serverIP, String serverPort, String viewType) throws IOException {
        super(serverIP, serverPort, viewType);

        try {
            this.socket = new Socket(serverIP, Integer.parseInt(serverPort));
        } catch (IOException e) {
            notifyViewServerUnreachable();
            throw new IOException("Socket connection to server failed");
        }
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(new SocketServerReader(this, socket)).start();
    }

    @Override
    public void joinServer() {
        send(new Message("joinServer", Map.of("nickname", this.getNickname())));
    }

    @Override
    public void leaveServer() {
        send(new Message("leaveServer", Map.of()));
    }

    @Override
    public void joinGame() {
        send(new Message("joinGame", Map.of()));
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        send(new Message("setNumberOfPlayers", Map.of("numberOfPlayers", numberOfPlayers)));
    }

    @Override
    public void placeStarterSide(boolean isFront) {
        send(new Message("placeStarterSide", Map.of("isFront", isFront)));
    }

    @Override
    public void chooseObjective(String objectiveId) {
        send(new Message("chooseObjective", Map.of("objectiveId", objectiveId)));
    }

    @Override
    public void placeSide(String cardId, boolean isFront, int i, int j) {
        send(new Message("placeSide", Map.of(
                "cardId", cardId,
                "isFront", isFront,
                "i", i,
                "j", j
        )));
    }

    @Override
    public void drawVisible(String cardId) {
        send(new Message("drawVisible", Map.of("cardId", cardId)));
    }

    @Override
    public void drawDeck(boolean isGold) {
        send(new Message("drawDeck", Map.of("isGold", isGold)));
    }

    @Override
    public void disconnect() {
        send(new Message("disconnect", Map.of()));
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing Socket");
        }
    }

    @Override
    public void sendMessage(String message) {
        send(new Message("message", Map.of("message", message)));
    }

    @Override
    public void sendDirectMessage(String message, String recipient) {
        send(new Message("directMessage", Map.of("message", message, "recipient", recipient)));
    }

    public synchronized void send(Message message) {
        try {

            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Failed sending message");
            System.out.println(e.getMessage());
        }
    }

    public class SocketServerReader implements Runnable {

        private final SocketServerHandler socketServerHandler;
        private final Socket socket;

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

        public SocketServerReader(SocketServerHandler socketServerHandler, Socket socket) {
            this.socketServerHandler = socketServerHandler;
            this.socket = socket;

            // Start checking connection
            connectionChecker.scheduleAtFixedRate(new ConnectionChecker(this.socket), 1, 4, TimeUnit.SECONDS);
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    Message message = (Message) inputStream.readObject();

                    handleServerInput(message);
                }
            } catch (Exception e) {
                //System.out.println("Socket Reader exiting");
                connectionChecker.shutdown();
                inputHandler.shutdown();
                socketServerHandler.notifyViewServerUnreachable();
            }
        }

        private void handleServerInput(Message message) {

            switch (message.title()) {
                case "ping":
                    send(new Message("pong", Map.of("key", message.arguments().get("key"))));
                    break;

                case "pong":
                    lastHeartBeat = (String) message.arguments().get("key");
                    break;

                case "Game":
                    inputHandler.submit(() -> socketServerHandler.setGame((Game) message.arguments().get("game")));
                    break;

                case "Log":
                    inputHandler.submit(() -> socketServerHandler.addLog((String) message.arguments().get("log")));
                    break;

                default:
                    System.out.println("Unknown message: " + message.title() + message.arguments());
                    break;
            }
        }
    }

    /**
     * This class sends a heartbeat message to the server, if the server doesn't answer with the appropriate message,
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
            //System.out.println("Ping: " + heartBeat);
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
            //else
            //    System.out.println("Pong: " + lastHeartBeat);
        }
    }
}
