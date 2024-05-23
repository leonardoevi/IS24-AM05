package it.polimi.is24am05;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.TUI;
import it.polimi.is24am05.client.view.View;
import javafx.application.Application;

public class GuiRoot extends View {

    GUIMain guiMain;
    @Override
    public void updateGame() {


    }

    @Override
    public void updateLogs() {

    }
    public GuiRoot(ClientModel clientModel, ServerHandler server) {
        super(clientModel, server);
        new Thread(() -> Application.launch(GUIMain.class)).start();
        // Attendi fino a quando l'istanza di MyJavaFXApp è disponibile
        GUIMain.waitForStart();
        this.guiMain = GUIMain.getInstance();
    }

}
