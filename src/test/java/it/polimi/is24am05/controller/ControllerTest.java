package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.exceptions.game.MoveNotAllowedException;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.exceptions.game.NotYourTurnException;
import it.polimi.is24am05.model.exceptions.player.InvalidStarterSideException;
import it.polimi.is24am05.model.objective.Objective;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.util.List;

import static it.polimi.is24am05.model.game.GameTest.deterministicallyPlay;
import static org.junit.jupiter.api.Assertions.*;

// Tests must be run in order
class ControllerTest {
    private final String fileName = "TestSave.sv";

    // Tests no 1
    // Testing saving and reloading a game just after game initialization
    @Test
    @Order(1)
    void save1() throws IOException, InvalidNumUsersException, FirstConnectionException, ConnectionRefusedException {
        Controller controller = new Controller();

        controller.newConnection("Leo", 2);
        controller.saveGame(fileName);
        controller.newConnection("Alex");

        System.out.println(controller.game.getPlayers().stream().map(Player::getNickname).toList());

        controller.saveGame(fileName);
    }
    @Test
    @Order(2)
    void load1() throws IOException, ClassNotFoundException, ConnectionRefusedException, FirstConnectionException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        Controller controller = new Controller(Controller.loadGame(fileName));
        String p1 = controller.game.getPlayers().get(0).getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();
        List<String> players = List.of(p1, p2);

        //System.out.println(controller.game);

        // No moves should be performed
        for(String player : players) {
            // Methods parameters should not be important, as exception is immediately thrown
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.chooseObjective(player, "ALesioPippone"));
            assertThrows(RuntimeException.class, () -> controller.placeSide(player, null, false, 6 , 9));
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.drawVisible(player, null));
            assertThrows(RuntimeException.class, () -> controller.drawDeck(player, true));
        }

        controller.newConnection(p1);
        controller.newConnection(p2);

        controller.playStarterCard(p1, true);
        controller.playStarterCard(p2, false);
        //System.out.println(controller.game);

        for(Objective o : Objective.values()) {
            try {
                controller.chooseObjective(p1, o.name());
            } catch (Exception ignored) {}

            try {
                controller.chooseObjective(p2, o.name());
            } catch (Exception ignored) {}
        }
        //System.out.println(controller.game);

        assertEquals(controller.game.getGameState(), GameState.GAME);
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p1);
    }

    // Tests no 2
    // Testing saving and reloading a game just after starter card placement
    @Test
    @Order(3)
    void save2() throws IOException, InvalidNumUsersException, FirstConnectionException, ConnectionRefusedException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        Controller controller = new Controller();

        controller.newConnection("Leo", 2);
        controller.newConnection("Alex");

        controller.playStarterCard("Leo", true);

        controller.saveGame(fileName);
    }
    @Test
    @Order(4)
    void load2() throws IOException, ClassNotFoundException, ConnectionRefusedException, FirstConnectionException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        Controller controller = new Controller(Controller.loadGame(fileName));
        String p1 = controller.game.getPlayers().get(0).getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();
        List<String> players = List.of(p1, p2);

        // No moves should be performed
        for(String player : players) {
            // Methods parameters should not be important, as exception is immediately thrown
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.chooseObjective(player, "ALesioPippone"));
            assertThrows(RuntimeException.class, () -> controller.placeSide(player, null, false, 6 , 9));
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.drawVisible(player, null));
            assertThrows(RuntimeException.class, () -> controller.drawDeck(player, true));
        }

        controller.newConnection(p1);

        // No moves should be performed
        for(String player : players) {
            // Methods parameters should not be important, as exception is immediately thrown
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.chooseObjective(player, "ALesioPippone"));
            assertThrows(RuntimeException.class, () -> controller.placeSide(player, null, false, 6 , 9));
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.drawVisible(player, null));
            assertThrows(RuntimeException.class, () -> controller.drawDeck(player, true));
        }

        controller.newConnection(p2);

        controller.playStarterCard("Alex", false);

        for(Objective o : Objective.values()) {
            try {
                controller.chooseObjective(p1, o.name());
            } catch (Exception ignored) {}

            try {
                controller.chooseObjective(p2, o.name());
            } catch (Exception ignored) {}
        }
        //System.out.println(controller.game);

        assertEquals(controller.game.getGameState(), GameState.GAME);
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p1);
    }

    // Tests no 3
    // Testing saving and reloading after objectives have been chosen
    @Test
    @Order(5)
    void save3() throws IOException, InvalidNumUsersException, FirstConnectionException, ConnectionRefusedException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        Controller controller = new Controller();

        controller.newConnection("Leo", 2);
        controller.saveGame(fileName);
        controller.newConnection("Alex");

        // Both players play a starter card
        controller.playStarterCard("Leo", true);
        controller.playStarterCard("Alex", true);

        // Both players choose an objective
        for(Objective o : Objective.values()) {
            try {
                controller.chooseObjective("Leo", o.name());
            } catch (Exception ignored) {}

            try {
                controller.chooseObjective("Alex", o.name());
            } catch (Exception ignored) {}
        }

        String p1 = controller.game.getPlayers().get(0).getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();

        System.out.println(p1);

        controller.saveGame(fileName);
    }
    @Test
    @Order(6)
    void load3() throws IOException, ClassNotFoundException, ConnectionRefusedException, FirstConnectionException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        Controller controller = new Controller(Controller.loadGame(fileName));
        String p1 = controller.game.getPlayers().get(0).getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();
        List<String> players = List.of(p1, p2);

        System.out.println(p1);

        // No moves should be performed
        for(String player : players) {
            // Methods parameters should not be important, as exception is immediately thrown
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.chooseObjective(player, "ALesioPippone"));
            assertThrows(RuntimeException.class, () -> controller.placeSide(player, null, false, 6 , 9));
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.drawVisible(player, null));
            assertThrows(RuntimeException.class, () -> controller.drawDeck(player, true));
        }

        controller.newConnection(p1);
        controller.newConnection(p2);

        assertEquals(controller.game.getGameState(), GameState.GAME);
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p1);
    }

    // Tests no 4
    // Testing saving and reloading after first player places a card
    @Test
    @Order(7)
    void save4() throws IOException, InvalidNumUsersException, FirstConnectionException, ConnectionRefusedException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        Controller controller = new Controller();

        controller.newConnection("Leo", 2);
        controller.saveGame(fileName);
        controller.newConnection("Alex");

        // Both players play a starter card
        controller.playStarterCard("Leo", true);
        controller.playStarterCard("Alex", true);

        // Both players choose an objective
        for(Objective o : Objective.values()) {
            try {
                controller.chooseObjective("Leo", o.name());
            } catch (Exception ignored) {}

            try {
                controller.chooseObjective("Alex", o.name());
            } catch (Exception ignored) {}
        }

        String p1 = controller.game.getPlayers().getFirst().getNickname();
        deterministicallyPlay(controller.game, p1);

        System.out.println(p1);

        controller.saveGame(fileName);
    }
    @Test
    @Order(8)
    void load4() throws IOException, ClassNotFoundException, ConnectionRefusedException, FirstConnectionException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException, NotYourTurnException, EmptyDeckException {
        Controller controller = new Controller(Controller.loadGame(fileName));
        String p1 = controller.game.getPlayers().get(0).getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();
        List<String> players = List.of(p1, p2);

        System.out.println(p1);

        // No moves should be performed
        for(String player : players) {
            // Methods parameters should not be important, as exception is immediately thrown
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.chooseObjective(player, "ALesioPippone"));
            assertThrows(RuntimeException.class, () -> controller.placeSide(player, null, false, 6 , 9));
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.drawVisible(player, null));
            assertThrows(RuntimeException.class, () -> controller.drawDeck(player, true));
        }

        controller.newConnection(p1);
        controller.newConnection(p2);

        assertEquals(controller.game.getGameState(), GameState.GAME);
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p1);

        // p1 draws a card
        controller.drawDeck(p1, false);

        // Its p2 turn
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p2);
    }

    // Tests no 5
    // Testing saving and reloading after first player has concluded his turn
    @Test
    @Order(9)
    void save5() throws IOException, InvalidNumUsersException, FirstConnectionException, ConnectionRefusedException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException, NotYourTurnException, EmptyDeckException {
        Controller controller = new Controller();

        controller.newConnection("Leo", 2);
        controller.saveGame(fileName);
        controller.newConnection("Alex");

        // Both players play a starter card
        controller.playStarterCard("Leo", true);
        controller.playStarterCard("Alex", true);

        // Both players choose an objective
        for(Objective o : Objective.values()) {
            try {
                controller.chooseObjective("Leo", o.name());
            } catch (Exception ignored) {}

            try {
                controller.chooseObjective("Alex", o.name());
            } catch (Exception ignored) {}
        }

        String p1 = controller.game.getPlayers().getFirst().getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();
        deterministicallyPlay(controller.game, p1);
        controller.drawDeck(p1, false);

        // Its p2 turn
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p2);
        System.out.println(p2);

        controller.saveGame(fileName);
    }
    @Test
    @Order(10)
    void load5() throws IOException, ClassNotFoundException, ConnectionRefusedException, FirstConnectionException {
        Controller controller = new Controller(Controller.loadGame(fileName));
        String p1 = controller.game.getPlayers().get(0).getNickname();
        String p2 = controller.game.getPlayers().get(1).getNickname();
        List<String> players = List.of(p1, p2);

        System.out.println(p2);

        // No moves should be performed
        for(String player : players) {
            // Methods parameters should not be important, as exception is immediately thrown
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.chooseObjective(player, "ALesioPippone"));
            assertThrows(RuntimeException.class, () -> controller.placeSide(player, null, false, 6 , 9));
            assertThrows(RuntimeException.class, () -> controller.playStarterCard(player, true));
            assertThrows(RuntimeException.class, () -> controller.drawVisible(player, null));
            assertThrows(RuntimeException.class, () -> controller.drawDeck(player, true));
        }

        controller.newConnection(p2);
        controller.newConnection(p1);

        assertEquals(controller.game.getGameState(), GameState.GAME);

        // Its p2 turn
        assertEquals(controller.game.getPlayers().get(controller.game.getTurn()).getNickname(), p2);
    }

}