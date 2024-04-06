package it.polimi.is24am05.model.playArea;

import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceFrontSide;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
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
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.objective.Objective;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AreaDisplayerTest {
    @Test
    void testGeneratedGameToString() {
        Game game = simulateGame();

        // Print winner PlayArea

        Player winner = game.getWinners().getFirst();

        // Old Way
        // Print statistics
        System.out.println(winner.getNickname() + ":");
        System.out.println("Points:" + winner.getPoints());
        System.out.println("Cards Played: " + winner.getPlayArea().getPlayArea().keySet().size());


        System.out.println("PlayArea:");
        System.out.println(new AreaDisplayer(winner.getPlayArea()).toString());
        System.out.println();
    }


    // Methods used by simulateGame()
    private static Player getCurrentPlayer(Game game){
        return game.getPlayers().get(game.getTurn());
    }

    private static Tuple getRandomFreePlacingSpot(Player player){
        List<Tuple> possibleCoord;
        possibleCoord = new LinkedList<>(player.getPlayArea().getFrontier().stream().toList());
        Collections.shuffle(possibleCoord);

        return possibleCoord.getFirst();
    }

    private static PlayArea generatePlayArea(){
        PlayArea playArea = new PlayArea();

        try {
            // Place a card in (0 0)
            playArea.playSide(StarterFrontSide.SFS_085, new Tuple(0, 0));
            // Place a card in (-1, -1)
            playArea.playSide(ResourceBackSide.RBS_003, new Tuple(-1, -1));
            // Place a card in (-1, 1)
            playArea.playSide(ResourceFrontSide.RFS_001, new Tuple(-1, 1));
        } catch (InvalidCoordinatesException | NoAdjacentCardException | PlacementNotAllowedException e){
            throw new RuntimeException();
        }

        return playArea;
    }

    private static Game simulateGame() {
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

            while(true) {
                // Draw from ResourceDeck
                if (randomDecision == 0) {
                    try {
                        game.drawDeck(current.getNickname(), false);
                        break;
                    } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                        throw new RuntimeException(e);
                    } catch (EmptyDeckException e) {
                        randomDecision += 1;
                    }
                }
                // Draw from GoldDeck
                if (randomDecision == 1) {
                    try {
                        game.drawDeck(current.getNickname(), false);
                        break;
                    } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                        throw new RuntimeException(e);
                    } catch (EmptyDeckException e) {
                        randomDecision += 1;
                    }
                }
                // Draw a Visible Resource Card
                if (randomDecision == 2) {
                    try {
                        game.drawVisible(current.getNickname(), game.getResourceDeck().getVisible().stream().findAny().orElse(null));
                        break;
                    } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidVisibleCardException e) {
                        randomDecision += 1;
                    }
                }
                // Draw a Visible Gold Card
                if (randomDecision == 3) {
                    try {
                        game.drawVisible(current.getNickname(), game.getGoldDeck().getVisible().stream().findAny().orElse(null));
                        break;
                    } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidVisibleCardException e) {
                        randomDecision = 0;
                    }
                }
            }

            // Current player State should be PLACE
            assertEquals(PlayerState.PLACE, current.getState());
        }

        return game;
    }
}