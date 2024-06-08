package it.polimi.is24am05.client.view.tui;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.game.Game;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class TUI extends View {
    private final InputReader inputReader;

    public TUI(ClientModel clientModel, ServerHandler server) {
        super(clientModel, server);

        inputReader = new InputReader();
        Thread t = new Thread(inputReader);
        t.setName("Stdin InputReader");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void updateGame() {
        try {
            Game toPrint = clientModel.getGame().orElseThrow(NullPointerException::new);
            System.out.println(toPrint.toString());
        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        }
    }

    @Override
    public void updateLogs() {
        try {
            System.out.println(clientModel.getLog().getLast());
        } catch (NullPointerException ignored) {}
    }

    public void serverUnreachable() {
        inputReader.stopInputReader();
    }

    class InputReader implements Runnable{
        private final Scanner stdin = new Scanner(System.in);
        private boolean stdinClosed = false;

        @Override
        public void run() {
            while (!stdinClosed) {
                try {
                    String line = stdin.nextLine();
                    if(stdinClosed)
                        break;

                    handleInput(line);
                } catch (IllegalStateException e) {
                    break;
                }
            }
        }

        private void handleInput(String input){
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter(" ");

            try {

                String command = scanner.next();

                switch (command) {
                    case "1":
                        server.setNickname(scanner.next());
                        break;

                    case "2":
                        server.joinServer();
                        break;

                    case "3":
                        server.setNumberOfPlayers(parseInt(scanner.next()));
                        break;

                    case "4":
                        server.joinGame();
                        break;

                    case "5":
                        server.placeStarterSide(parseBoolean(scanner.next()));
                        break;

                    case "6":
                        server.chooseObjective(scanner.next());
                        break;

                    case "7":
                        server.placeSide(getCardName(Card.getCard(parseInt(scanner.next()))), parseBoolean(scanner.next()), parseInt(scanner.next()), parseInt(scanner.next()));
                        break;

                    case "8":
                        server.drawDeck(parseBoolean(scanner.next()));
                        break;

                    case "9":
                        server.drawVisible(getCardName(Card.getCard(parseInt(scanner.next()))));
                        break;

                    case "11": // Quitting
                        server.disconnect();
                        stopInputReader();
                        break;

                    default:
                        throw new NoSuchElementException();
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }

        private void stopInputReader(){
            if(!stdinClosed)
                System.out.println("Connection to server unavailable, exiting.");
            stdinClosed = true;
        }
    }

    private String getCardName(Card card) throws NoSuchElementException {
        if(card instanceof ResourceCard)
            return ((ResourceCard) card).name();
        else if(card instanceof GoldCard)
            return ((GoldCard) card).name();
        else
            throw new NoSuchElementException();
    }
}
