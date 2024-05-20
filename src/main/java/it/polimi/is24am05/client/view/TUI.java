package it.polimi.is24am05.client.view;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.model.game.Game;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class TUI extends View {
    public TUI(ClientModel clientModel, ServerHandler server) {
        super(clientModel, server);

        new Thread(new InputReader()).start();
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

    class InputReader implements Runnable{
        private volatile boolean quit = false;

        @Override
        public void run() {
            final Scanner stdin = new Scanner(System.in);
            while (true) {
                String line = stdin.nextLine();
                handleInput(line);
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
                        server.placeSide(scanner.next(), parseBoolean(scanner.next()), parseInt(scanner.next()), parseInt(scanner.next()));
                        break;

                    case "8":
                        server.drawDeck(parseBoolean(scanner.next()));
                        break;

                    case "9":
                        server.drawVisible(scanner.next());
                        break;

                    case "10":
                        server.disconnect();
                        quit = true;
                        break;

                    default:
                        throw new NoSuchElementException();
                }
            } catch (NoSuchElementException e) {
                System.out.println("Invalid input");;
            }
        }
    }
}
