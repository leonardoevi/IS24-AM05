package it.polimi.is24am05.controller.server.socket;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class SocketClientHandler extends ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;

    public SocketClientHandler(Controller controller, Server server, Socket socket) throws IOException {
        super(controller, server);
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void setGame(Game game) {
        send(new Message("Game", Map.of("game", game)));
    }

    @Override
    public void addLog(String log) {
        send(new Message("Log", Map.of("log", log)));
    }

    private void send(Message message){
        synchronized(this.out){
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.out.println("Error sending message: " + message + " to " + getNickname());
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
                handleClientInput(message);
            } catch (Exception e){
                System.out.println(e.getMessage());
                break;
            }
        }
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
            case "disconnect":
                super.disconnect();
                break;
        }
    }



}
