package it.polimi.is24am05.client;

import it.polimi.is24am05.GuiRoot;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.TUI;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.model.game.Game;

public abstract class ServerHandler implements VirtualServer{
    private String nickname;
    private final ClientModel clientModel;

    protected final String serverIP;
    protected final String serverPort;

    protected ServerHandler(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        clientModel = new ClientModel();
        new GuiRoot(clientModel, this);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    // Methods invoked by the SERVER from the network (better be protected)
    protected void setGame(Game game){
        this.clientModel.setGame(game);
    }

    protected void addLog(String message){
        this.clientModel.addLog(message);
    }
}
