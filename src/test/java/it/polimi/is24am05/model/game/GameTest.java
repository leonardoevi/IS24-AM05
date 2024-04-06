package it.polimi.is24am05.model.game;

import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.EmptyPlacedSide;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.exceptions.deck.InvalidVisibleCardException;
import it.polimi.is24am05.model.exceptions.game.*;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.exceptions.player.InvalidCardException;
import it.polimi.is24am05.model.exceptions.player.InvalidSideException;
import it.polimi.is24am05.model.exceptions.player.InvalidStarterSideException;
import it.polimi.is24am05.model.exceptions.player.ObjectiveNotAllowedException;
import it.polimi.is24am05.model.objective.Objective;
import it.polimi.is24am05.model.playArea.AreaDisplayer;
import it.polimi.is24am05.model.playArea.Tuple;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void Game1() {

        // Simulate a game being played
        // Verify only the correct methods can be invoked
        // Verify the correct evolution of the game model

        Game game;
        List<String> players = List.of("Acoustic", "Andrea", "Chad", "Ale");

        // Test constructor
        assertThrows(PlayerNamesMustBeDifferentException.class, () -> new Game(List.of("Acoustic", "Acoustic")));
        assertThrows(TooFewPlayersException.class, () -> new Game(List.of("Acoustic")));
        assertThrows(TooManyPlayersException.class, () -> new Game(List.of("Acoustic", "Andrea", "Chad", "Ale", "Intern")));

        assertDoesNotThrow(() -> new Game(List.of("Acoustic", "Andrea", "Chad", "Ale")));
        try {
            game = new Game(players);
        } catch (TooManyPlayersException | TooFewPlayersException | PlayerNamesMustBeDifferentException e) { throw new RuntimeException(e); }

        // Game state should be PLACE_STARTER_CARDS
        assertEquals(GameState.PLACE_STARTER_CARDS, game.getGameState());

        // No moves except for placeStarterSide() should be allowed
        // The exception is thrown regardless of the parameters
        assertThrows(MoveNotAllowedException.class, () -> game.chooseObjective(players.getFirst(), Objective.O_088));
        assertThrows(MoveNotAllowedException.class, () -> game.placeSide("pippo", null, null,0,0));
        assertThrows(MoveNotAllowedException.class, () -> game.drawDeck("pippo", true));
        assertThrows(MoveNotAllowedException.class, () -> game.drawVisible("pippo", null));

        // Make sure each player places their card
        for(String name : players){
            Card starterCard = game.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(name))
                    .findFirst()
                    .get()
                    .getStarterCard();

            try {
                game.placeStarterSide(name, starterCard.getBackSide());
            } catch (InvalidStarterSideException | MoveNotAllowedException | NoSuchPlayerException e) {throw new RuntimeException(e);}
        }

        // Game state should be CHOOSE_OBJECTIVE
        assertEquals(GameState.CHOOSE_OBJECTIVE, game.getGameState());

        // No moves except for chooseObjective() should be allowed
        // The exception is thrown regardless of the parameters
        assertThrows(MoveNotAllowedException.class, () -> game.placeStarterSide("pippo", null));
        assertThrows(MoveNotAllowedException.class, () -> game.placeSide("pippo", null, null,0,0));
        assertThrows(MoveNotAllowedException.class, () -> game.drawDeck("pippo", true));
        assertThrows(MoveNotAllowedException.class, () -> game.drawVisible("pippo", null));

        // Each player chooses an objective
        for(String name : players){
            Objective[] toChooseFrom = game.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(name))
                    .findFirst()
                    .get()
                    .getObjectivesHand();

            Objective choice = toChooseFrom[new Random().nextInt(toChooseFrom.length)];

            try {
                game.chooseObjective(name, choice);
            } catch (MoveNotAllowedException | ObjectiveNotAllowedException | NoSuchPlayerException e) {throw new RuntimeException(e);}
        }

        // Game state should be GAME
        assertEquals(GameState.GAME, game.getGameState());

        // No moves except for placeSide(), drawDeck() and drawVisible() should be allowed
        // The exception is thrown regardless of the parameters
        assertThrows(MoveNotAllowedException.class, () -> game.placeStarterSide("pippo", null));
        assertThrows(MoveNotAllowedException.class, () -> game.chooseObjective("pippo", null));

        // Repeat nr_of_players * N - 1 times
        int N = 5;

        for(int i=0; i < players.size()*N - 1; i++) {
            Player current = getCurrentPlayer(game);

            // Current player State should be PLACE
            assertEquals(PlayerState.PLACE, current.getState());

            // Others players may not make any move
            for (Player other : game.getPlayers()) {
                if (other.getNickname().equals(current.getNickname())) continue;

                assertThrows(NotYourTurnException.class, () -> game.drawDeck(other.getNickname(), false));
                assertThrows(NotYourTurnException.class, () -> game.drawVisible(other.getNickname(), null));
                assertThrows(NotYourTurnException.class, () -> game.placeSide(other.getNickname(), null, null, 0, 0));
            }

            // Current player plays a card
            Card toPlace = current.getHand().getFirst();
            Tuple coord = getRandomFreePlacingSpot(current);
            try {
                game.placeSide(current.getNickname(), toPlace, toPlace.getBackSide(), coord.i, coord.j);
            } catch (MoveNotAllowedException | NoAdjacentCardException | InvalidCardException |
                     InvalidCoordinatesException | InvalidSideException | PlacementNotAllowedException |
                     NotYourTurnException | NoSuchPlayerException e) {
                throw new RuntimeException(e);
            }

            // If there is something to draw
            assert !game.getGoldDeck().getVisible().isEmpty() || !game.getResourceDeck().getVisible().isEmpty();

            // Current player State should be DRAW
            assertEquals(PlayerState.DRAW, current.getState());

            // Others players may not make any move
            for (Player other : game.getPlayers()) {
                if (other.getNickname().equals(current.getNickname())) continue;

                assertThrows(NotYourTurnException.class, () -> game.drawDeck(other.getNickname(), false));
                assertThrows(NotYourTurnException.class, () -> game.drawVisible(other.getNickname(), null));
                assertThrows(NotYourTurnException.class, () -> game.placeSide(other.getNickname(), null, null, 0, 0));
            }

            // Current Player Draws a card
            int randomDecision = new Random().nextInt(4);

            // Draw from ResourceDeck
            if(randomDecision == 0){
                try {
                    game.drawDeck(current.getNickname(), false);
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (EmptyDeckException e) {
                    randomDecision += 1;
                }
            }
            // Draw from GoldDeck
            if(randomDecision == 1){
                try {
                    game.drawDeck(current.getNickname(), false);
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (EmptyDeckException e) {
                    randomDecision += 1;
                }
            }
            // Draw a Visible Resource Card
            if(randomDecision == 2){
                try {
                    game.drawVisible(current.getNickname(), game.getResourceDeck().getVisible().stream().findAny().get());
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (InvalidVisibleCardException e) {
                    randomDecision += 1;
                }
            }
            // Draw a Visible Gold Card
            if(randomDecision == 3){
                try {
                    game.drawVisible(current.getNickname(), game.getGoldDeck().getVisible().stream().findAny().get());
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (InvalidVisibleCardException e) {
                    randomDecision = 0;
                }
            }

            // Current player State should be PLACE
            assertEquals(PlayerState.PLACE, current.getState());
        }


    }

    @Test
    void Game2() {
        // Set up the same game as before


        // Simulate a game being played
        // Verify only the correct methods can be invoked
        // Verify the correct evolution of the game model

        Game game;
        List<String> players = List.of("Acoustic", "Andrea", "Chad", "Ale");

        // Test constructor
        assertThrows(PlayerNamesMustBeDifferentException.class, () -> new Game(List.of("Acoustic", "Acoustic")));
        assertThrows(TooFewPlayersException.class, () -> new Game(List.of("Acoustic")));
        assertThrows(TooManyPlayersException.class, () -> new Game(List.of("Acoustic", "Andrea", "Chad", "Ale", "Intern")));

        assertDoesNotThrow(() -> new Game(List.of("Acoustic", "Andrea", "Chad", "Ale")));
        try {
            game = new Game(players);
        } catch (TooManyPlayersException | TooFewPlayersException | PlayerNamesMustBeDifferentException e) { throw new RuntimeException(e); }

        // Game state should be PLACE_STARTER_CARDS
        assertEquals(GameState.PLACE_STARTER_CARDS, game.getGameState());

        // No moves except for placeStarterSide() should be allowed
        // The exception is thrown regardless of the parameters
        assertThrows(MoveNotAllowedException.class, () -> game.chooseObjective(players.getFirst(), Objective.O_088));
        assertThrows(MoveNotAllowedException.class, () -> game.placeSide("pippo", null, null,0,0));
        assertThrows(MoveNotAllowedException.class, () -> game.drawDeck("pippo", true));
        assertThrows(MoveNotAllowedException.class, () -> game.drawVisible("pippo", null));

        // Make sure each player places their card
        for(String name : players){
            Card starterCard = game.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(name))
                    .findFirst()
                    .get()
                    .getStarterCard();

            try {
                game.placeStarterSide(name, starterCard.getBackSide());
            } catch (InvalidStarterSideException | MoveNotAllowedException | NoSuchPlayerException e) {throw new RuntimeException(e);}
        }

        // Game state should be CHOOSE_OBJECTIVE
        assertEquals(GameState.CHOOSE_OBJECTIVE, game.getGameState());

        // No moves except for chooseObjective() should be allowed
        // The exception is thrown regardless of the parameters
        assertThrows(MoveNotAllowedException.class, () -> game.placeStarterSide("pippo", null));
        assertThrows(MoveNotAllowedException.class, () -> game.placeSide("pippo", null, null,0,0));
        assertThrows(MoveNotAllowedException.class, () -> game.drawDeck("pippo", true));
        assertThrows(MoveNotAllowedException.class, () -> game.drawVisible("pippo", null));

        // Each player chooses an objective
        for(String name : players){
            Objective[] toChooseFrom = game.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(name))
                    .findFirst()
                    .get()
                    .getObjectivesHand();

            Objective choice = toChooseFrom[new Random().nextInt(toChooseFrom.length)];

            try {
                game.chooseObjective(name, choice);
            } catch (MoveNotAllowedException | ObjectiveNotAllowedException | NoSuchPlayerException e) {throw new RuntimeException(e);}
        }

        // Game state should be GAME
        assertEquals(GameState.GAME, game.getGameState());

        // No moves except for placeSide(), drawDeck() and drawVisible() should be allowed
        // The exception is thrown regardless of the parameters
        assertThrows(MoveNotAllowedException.class, () -> game.placeStarterSide("pippo", null));
        assertThrows(MoveNotAllowedException.class, () -> game.chooseObjective("pippo", null));


        while(game.getGameState() != GameState.END) {
            Player current = getCurrentPlayer(game);

            // Current player State should be PLACE
            assertEquals(PlayerState.PLACE, current.getState());

            // Others players may not make any move
            for (Player other : game.getPlayers()) {
                if (other.getNickname().equals(current.getNickname())) continue;

                assertThrows(NotYourTurnException.class, () -> game.drawDeck(other.getNickname(), false));
                assertThrows(NotYourTurnException.class, () -> game.drawVisible(other.getNickname(), null));
                assertThrows(NotYourTurnException.class, () -> game.placeSide(other.getNickname(), null, null, 0, 0));
            }

            // Current player plays a card
            Card toPlace = current.getHand().getFirst();
            Tuple coord = getRandomFreePlacingSpot(current);
            try {
                // Try placing it facing up
                game.placeSide(current.getNickname(), toPlace, toPlace.getFrontSide(), coord.i, coord.j);
            } catch (MoveNotAllowedException | NoAdjacentCardException | InvalidCardException |
                     InvalidCoordinatesException | InvalidSideException |
                     NotYourTurnException | NoSuchPlayerException e) {
                throw new RuntimeException(e);
            } catch (PlacementNotAllowedException e){
                try {
                    // Place it facing down
                    game.placeSide(current.getNickname(), toPlace, toPlace.getFrontSide(), coord.i, coord.j);
                } catch (MoveNotAllowedException | NoAdjacentCardException | InvalidCardException |
                         InvalidCoordinatesException | InvalidSideException | PlacementNotAllowedException |
                         NotYourTurnException | NoSuchPlayerException ex) {
                    throw new RuntimeException(ex);
                }
            }

            // If there is nothing to draw
            if( game.getGoldDeck().getVisible().isEmpty() && game.getResourceDeck().getVisible().isEmpty()){
                continue;
            }

            // Current player State should be DRAW
            assertEquals(PlayerState.DRAW, current.getState());

            // Others players may not make any move
            for (Player other : game.getPlayers()) {
                if (other.getNickname().equals(current.getNickname())) continue;

                assertThrows(NotYourTurnException.class, () -> game.drawDeck(other.getNickname(), false));
                assertThrows(NotYourTurnException.class, () -> game.drawVisible(other.getNickname(), null));
                assertThrows(NotYourTurnException.class, () -> game.placeSide(other.getNickname(), null, null, 0, 0));
            }

            // Current Player Draws a card
            int randomDecision = new Random().nextInt(4);

            // Draw from ResourceDeck
            if(randomDecision == 0){
                try {
                    game.drawDeck(current.getNickname(), false);
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (EmptyDeckException e) {
                    randomDecision += 1;
                }
            }
            // Draw from GoldDeck
            if(randomDecision == 1){
                try {
                    game.drawDeck(current.getNickname(), false);
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (EmptyDeckException e) {
                    randomDecision += 1;
                }
            }
            // Draw a Visible Resource Card
            if(randomDecision == 2){
                try {
                    game.drawVisible(current.getNickname(), game.getResourceDeck().getVisible().stream().findAny().orElse(null));
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (InvalidVisibleCardException e) {
                    randomDecision += 1;
                }
            }
            // Draw a Visible Gold Card
            if(randomDecision == 3){
                try {
                    game.drawVisible(current.getNickname(), game.getGoldDeck().getVisible().stream().findAny().orElse(null));
                } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                    throw new RuntimeException(e);
                } catch (InvalidVisibleCardException e) {
                    randomDecision = 0;
                }
            }

            // Current player State should be PLACE
            assertEquals(PlayerState.PLACE, current.getState());
        }

        // Get winners
        List<Player> winners = game.getWinners();

        // Must have at least one winner
        assertFalse(winners.isEmpty());

        // Announce winners
        if(winners.size() == 1){
            System.out.println("The winner is: " + winners.getFirst().getNickname());
        } else {
            String names = "";
            for(Player winner : winners){
                names = names + winner.getNickname() + " ";
            }
            System.out.println("The winners are " + names);
        }
        System.out.println();

        // Print statistics
        for(String name : players){
            Player p = getPlayer(game, name);
            System.out.println(name + ":");
            System.out.println("Points:" + p.getPoints());
            System.out.println("Cards Played: " + p.getPlayArea().getPlayArea().keySet().size());
            System.out.println("PlayArea:");
            System.out.println(new AreaDisplayer(p.getPlayArea()));


            System.out.println();
        }

    }

    @Test
    void multipleGames() {
        int N = 100;

        for (int i = 0; i < N; i++)
            Game2();
    }

    // Used to get the player of the game that is expected to make a move.
    // Might throw exceptions if invoked when the game state is not GAME
    public static Player getCurrentPlayer(Game game){
        return game.getPlayers().get(game.getTurn());
    }

    private static Player getPlayer(Game game, String name){
        return game.getPlayers().stream()
                .filter(p -> p.getNickname().equals(name))
                .findFirst()
                .get();
    }

    public static Tuple getRandomFreePlacingSpot(Player player){
        List<Tuple> possibleCoord;
        possibleCoord = new LinkedList<>(player.getPlayArea().getFrontier().stream().toList());
        Collections.shuffle(possibleCoord);

        return possibleCoord.getFirst();
    }

    private static String PlayAreaToString(PlacedSide[][] matrix){
        String ret = "";
        for(PlacedSide[] row : matrix){
            for(PlacedSide placedSide : row){
                // Replace this with correct implementation of toString method
                if(placedSide instanceof EmptyPlacedSide)
                    ret += " " + placedSide.getActualCoord().toString() + " ";
                else
                    ret += " " + placedSide.getSide().toString() + " ";
            }
            ret += "\n";
        }
        return ret;
    }
}