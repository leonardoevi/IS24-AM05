package it.polimi.is24am05.client.view;

import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.model.game.Game;

public class View implements Observer{
    private final ClientModel clientModel;

    public View(ClientModel clientModel) {
        this.clientModel = clientModel;
        clientModel.addObserver(this);
    }

    @Override
    public void updateGame() {
        try {
            Game toPrint = clientModel.getGame().orElseThrow(NullPointerException::new);
            System.out.println(toPrint);
        } catch (NullPointerException ignored) {}
    }

    @Override
    public void updateLogs() {
        try {
            System.out.println(clientModel.getLog().getLast());
        } catch (NullPointerException ignored) {}
    }
}
