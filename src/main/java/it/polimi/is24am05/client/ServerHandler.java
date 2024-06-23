package it.polimi.is24am05.client;

import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.client.view.tui.TUI;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.model.game.Game;

/**
 * Handles communication with the server
 */
public abstract class ServerHandler implements VirtualServer{
    private String nickname;
    private final ClientModel clientModel;
    private View view;

    protected final String serverIP;
    protected final String serverPort;

    protected ServerHandler(String serverIP, String serverPort, String viewType) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        clientModel = new ClientModel();

        if(viewType.equals("GUI"))
            view = new GUIRoot(clientModel, this);
        else
            view = new TUI(clientModel, this);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public View getView()
    {
        return this.view;
    }

    // Methods invoked by the SERVER from the network (better be protected)
    protected void setGame(Game game){
        this.clientModel.setGame(game);
    }

    protected void addLog(String message){
        this.clientModel.addLog(message);
    }

    protected void notifyViewServerUnreachable(){
        view.serverUnreachable();
    }
}
