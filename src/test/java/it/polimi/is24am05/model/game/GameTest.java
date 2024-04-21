package it.polimi.is24am05.model.game;

import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.goldCard.GoldFrontSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceFrontSide;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import static it.polimi.is24am05.model.Player.HandDisplayer.handToString;
import static it.polimi.is24am05.model.deck.DeckDisplayer.deckToString;
import static it.polimi.is24am05.model.playArea.SideDisplayer.sideToString;
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
                    game.placeSide(current.getNickname(), toPlace, toPlace.getBackSide(), coord.i, coord.j);
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


    // TODO: test disconnections during early stages of the game

    void GameWithDisconnections( Map<Integer, List<String>> disconnections, Map<Integer, List<String>> connections ) {
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

        int round=0;
        while(game.getGameState() != GameState.END) {
            round++;
            if(disconnections.containsKey(round))
            {
                for(String s: disconnections.get(round)) {
                    try {
                        game.disconnect(s);
                    } catch (NoSuchPlayerException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
             if(connections.containsKey(round))
            {
                for(String s: connections.get(round)) {
                    try {
                        game.reconnect(s);
                    } catch (NoSuchPlayerException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
           if(game.getConnectedPlayers().size()<2)
           {
               assertEquals(GameState.PAUSE, game.getGameState());
               continue;
           }

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
                    game.placeSide(current.getNickname(), toPlace, toPlace.getBackSide(), coord.i, coord.j);
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


    // A game played deterministically starting from a save file
    // Useful to manually verify game state evolution
    @Test
    void gameWithDisconnections() throws TooManyPlayersException, TooFewPlayersException, PlayerNamesMustBeDifferentException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException, ObjectiveNotAllowedException {
        String path = "src/test/java/it/polimi/is24am05/model/game/";
        String filename = "game_disconnections_save.sv";
        String A = "Andre", L = "Leo", M = "Manu";
        Game game;

        /*
        game = new Game(List.of("Andre", "Leo", "Manu"));

        save(game, path + filename);


        game = load(path + filename);

        game.placeStarterSide(A, StarterFrontSide.SFS_084);
        game.placeStarterSide(M, StarterFrontSide.SFS_085);
        game.placeStarterSide(L, StarterFrontSide.SFS_082);

        save(game, path + filename);

        game = load(path + filename);

        game.chooseObjective(A, Objective.O_102);
        game.chooseObjective(M, Objective.O_099);
        game.chooseObjective(L, Objective.O_095);

        save(game, path + filename);
        */

        game = load(path + filename);

        // turns: A -> M -> L

        // Without disconnections A wins in 20 turns (playing deterministically)

        // Round 1
        assertEquals(A, getCurrentPlayer(game).getNickname());
        game.disconnect(A);
        assertEquals(M, getCurrentPlayer(game).getNickname());
        //deterministicallyPlay(game, A);
        //deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        game.reconnect(A);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 2
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // TODO: fix this?
        game.disconnect(A); // now its M turn
        game.disconnect(M); // game paused
        game.disconnect(L);

        game.reconnect(M); game.reconnect(A); // game resumed, still M turn
        game.reconnect(L);

        assertEquals(GameState.GAME, game.getGameState());
        //assertEquals(M, getCurrentPlayer(game).getNickname());

        // Round 3
        //deterministicallyPlay(game, A);
        //deterministicallyDraw(game, A);

        //deterministicallyPlay(game, M);
        //deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 4
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 5
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 6
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 7
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 8
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 9
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 10
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 11
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 12
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 13
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 14
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 15
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 16
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 17
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 18
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 19
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 20
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        System.out.println(game.getGameState());
        printGameState(game);
    }

    // Same test as previous without any disconnections (useful as reference)
    @Test
    void gameWith_OUT_Disconnections() throws TooManyPlayersException, TooFewPlayersException, PlayerNamesMustBeDifferentException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException, ObjectiveNotAllowedException {
        String path = "src/test/java/it/polimi/is24am05/model/game/";
        String filename = "game_disconnections_save.sv";
        String A = "Andre", L = "Leo", M = "Manu";
        Game game;

        /*
        game = new Game(List.of("Andre", "Leo", "Manu"));

        save(game, path + filename);


        game = load(path + filename);

        game.placeStarterSide(A, StarterFrontSide.SFS_084);
        game.placeStarterSide(M, StarterFrontSide.SFS_085);
        game.placeStarterSide(L, StarterFrontSide.SFS_082);

        save(game, path + filename);

        game = load(path + filename);

        game.chooseObjective(A, Objective.O_102);
        game.chooseObjective(M, Objective.O_099);
        game.chooseObjective(L, Objective.O_095);

        save(game, path + filename);
        */

        game = load(path + filename);

        // turns: A -> M -> L

        // Without disconnections A wins in 20 turns (playing deterministically)

        // Round 1
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 2
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 3
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 4
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 5
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 6
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 7
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 8
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 9
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 10
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 11
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 12
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 13
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 14
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 15
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 16
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 17
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 18
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 19
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        // Round 20
        deterministicallyPlay(game, A);
        deterministicallyDraw(game, A);

        deterministicallyPlay(game, M);
        deterministicallyDraw(game, M);

        deterministicallyPlay(game, L);
        deterministicallyDraw(game, L);

        System.out.println(game.getGameState());
        printGameState(game);
    }


    @Test
    void multipleGameswithDisconnection() {
        Map<Integer, List<String>> disconnections=new HashMap<>();
        Map<Integer, List<String>> connections=new HashMap<>();
        disconnections.put(2, List.of("Ale"));
        disconnections.put(4, List.of("Chad"));
        disconnections.put(7, List.of("Acoustic"));
        connections.put(5, List.of("Ale"));
        connections.put(6, List.of("Chad"));
        connections.put(11, List.of("Acoustic"));

        int N = 100;

        for (int i = 0; i < N; i++)
        {
            GameWithDisconnections(disconnections, connections);
        }

         disconnections=new HashMap<>();
         connections=new HashMap<>();
        disconnections.put(1, List.of("Ale"));
        disconnections.put(2, List.of("Chad"));
        disconnections.put(3, List.of("Acoustic"));
        connections.put(5, List.of("Ale"));
        connections.put(4, List.of("Chad", "Acoustic"));



        for (int i = 0; i < N; i++)
        {
            GameWithDisconnections(disconnections, connections);
        }

        disconnections=new HashMap<>();
        connections=new HashMap<>();
        disconnections.put(1, List.of("Ale"));
        disconnections.put(4, List.of("Chad"));
        disconnections.put(10, List.of("Acoustic"));
        connections.put(3, List.of("Ale"));
        connections.put(11, List.of("Chad"));
        connections.put(12, List.of("Acoustic"));


        for (int i = 0; i < N; i++)
        {
            GameWithDisconnections(disconnections, connections);
        }

        disconnections=new HashMap<>();
        connections=new HashMap<>();
        disconnections.put(1, List.of("Ale"));
        disconnections.put(4, List.of("Chad"));
        disconnections.put(7, List.of("Acoustic"));
        connections.put(20, List.of("Ale"));
        connections.put(6, List.of("Chad"));
        connections.put(9, List.of("Acoustic"));


        for (int i = 0; i < N; i++)
        {
            GameWithDisconnections(disconnections, connections);
        }



    }


    @Test
    void serializeManualGame() throws TooManyPlayersException, TooFewPlayersException, PlayerNamesMustBeDifferentException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException, ObjectiveNotAllowedException, PlacementNotAllowedException, NotYourTurnException, InvalidSideException, InvalidCoordinatesException, InvalidCardException, NoAdjacentCardException, InvalidVisibleCardException, EmptyDeckException {
        String path = "src/test/java/it/polimi/is24am05/model/game/";
        String filename = "game_save.sv";
        String A = "Andre", L = "Leo";
        Game game;// = new Game(List.of("Leo", "Andre"));

        /*
        game = load(path + filename);

        printGameState(game);

        // Players Play starter cards
        game.placeStarterSide(A, StarterFrontSide.SFS_083);
        game.placeStarterSide(L, StarterBackSide.SBS_085);

        save(game, path + filename);
        printGameState(game);


        game = load(path + filename);

        // Players choose objective
        game.chooseObjective(L, Objective.O_101);
        game.chooseObjective(A, Objective.O_097);

        printGameState(game);

        save(game, path + filename);


        game = load(path + filename);
        printGameState(game);

        // Leo's turn
        game.placeSide(L, ResourceCard.RC_009, ResourceBackSide.RBS_009, -1,1);
        game.drawVisible(L, ResourceCard.RC_018);

        // Andre's turn
        game.placeSide(A, ResourceCard.RC_020, ResourceFrontSide.RFS_020, 1,1);
        game.drawVisible(A, ResourceCard.RC_030);

        printGameState(game);

        save(game, path + filename);
        */

        // Load a game after deck initialization, objective choices and first 2 card placements

        game = load(path + filename);

        // Leo's turn
        game.placeSide(L, ResourceCard.RC_018, ResourceBackSide.RBS_018, 1,-1);
        game.drawVisible(L, GoldCard.GC_053);

        // Andre
        game.placeSide(A, ResourceCard.RC_030, ResourceBackSide.RBS_030, 1,-1);
        game.drawDeck(A, false);

        // Leo
        game.placeSide(L, ResourceCard.RC_032, ResourceBackSide.RBS_032, -1,-1);
        game.drawVisible(L, GoldCard.GC_077);

        // Andre
        game.placeSide(A, ResourceCard.RC_003, ResourceFrontSide.RFS_003, 2,0);
        game.drawDeck(A, true);

        // Leo
        game.placeSide(L, GoldCard.GC_075, GoldBackSide.GBS_075,-2,-2);
        game.drawDeck(L, false);

        // Andre
        game.placeSide(A, GoldCard.GC_061, GoldBackSide.GBS_061, 2,-2);
        game.drawVisible(A, ResourceCard.RC_002);

        // Leo

        // Placing this card on the front lets Leo win
        // Placing it on the back lets Andre win (same points but more objectives)

        game.placeSide(L, ResourceCard.RC_039, ResourceFrontSide.RFS_039, -3,-1);
        //game.placeSide(L, ResourceCard.RC_039, ResourceBackSide.RBS_039, -3,-1);
        game.drawDeck(L, false);

        // Andre
        game.placeSide(A, ResourceCard.RC_002, ResourceFrontSide.RFS_002, -1,1);
        game.drawVisible(A, GoldCard.GC_046);

        // Leo
        game.placeSide(L, GoldCard.GC_053, GoldBackSide.GBS_053, 2,0);
        game.drawDeck(L, true);

        // Andre
        game.placeSide(A, GoldCard.GC_042, GoldFrontSide.GFS_042, -2,2);
        game.drawVisible(A, GoldCard.GC_044);

        // Leo
        game.placeSide(L, GoldCard.GC_077, GoldFrontSide.GFS_077, 0,-2);
        game.drawDeck(L, true);

        // Andre
        game.placeSide(A, ResourceCard.RC_038, ResourceFrontSide.RFS_038, -1,3);
        game.drawDeck(A, true);

        // Leo
        game.placeSide(L, ResourceCard.RC_027, ResourceBackSide.RBS_027, 0,2);
        game.drawDeck(L, true);

        // Andre
        game.placeSide(A, GoldCard.GC_067, GoldBackSide.GBS_067, 3,-3);
        game.drawVisible(A, ResourceCard.RC_005);

        // Leo
        game.placeSide(L, GoldCard.GC_063, GoldBackSide.GBS_063, -1,3);
        game.drawVisible(L, ResourceCard.RC_036);

        // Andre
        game.placeSide(A, GoldCard.GC_044, GoldFrontSide.GFS_044, 0,2);
        game.drawVisible(A, ResourceCard.RC_025);

        // Leo
        game.placeSide(L, ResourceCard.RC_036, ResourceFrontSide.RFS_036, -4,0);
        game.drawVisible(L, GoldCard.GC_076);

        // Andre
        game.placeSide(A, ResourceCard.RC_025, ResourceBackSide.RBS_025, 3,1);
        game.drawDeck(A, false);

        // Leo
        game.placeSide(L, GoldCard.GC_065, GoldFrontSide.GFS_065, -2,4);
        game.drawVisible(L, ResourceCard.RC_034);

        // Andre
        game.placeSide(A, ResourceCard.RC_023, ResourceFrontSide.RFS_023, 4,0);
        game.drawDeck(A, false);

        // Leo
        game.placeSide(L, ResourceCard.RC_034, ResourceBackSide.RBS_034, -3,1);
        game.drawDeck(L, false);

        // Andre
        game.placeSide(A, ResourceCard.RC_019, ResourceFrontSide.RFS_019, 2,2);
        game.drawDeck(A, false);

        // Leo
        game.placeSide(L, GoldCard.GC_076, GoldFrontSide.GFS_076, -2,0);
        game.drawVisible(L, GoldCard.GC_080);

        // Andre
        game.placeSide(A, ResourceCard.RC_028, ResourceBackSide.RBS_028, 5,-1);
        game.drawDeck(A, false);

        // Leo
        game.placeSide(L, ResourceCard.RC_026, ResourceBackSide.RBS_026, -3,3);
        game.drawDeck(L, true);

        // Andre
        game.placeSide(A, ResourceCard.RC_013, ResourceFrontSide.RFS_013, 3,3);
        game.drawDeck(A, true);

        // Last moves

        // Leo
        game.placeSide(L, GoldCard.GC_066, GoldFrontSide.GFS_066, -2,2);
        game.drawDeck(L ,false);

        // Andre
        game.placeSide(A, GoldCard.GC_059, GoldFrontSide.GFS_059, 4,4);
        game.drawDeck(A,true);

        printGameState(game);

        assertEquals(GameState.END, game.getGameState());
        assertEquals(1, game.getWinners().size());
        assertEquals(L,game.getWinners().getFirst().getNickname());
    }

    private void save(Game game, String fileName) {
        try {
            // Create FileOutputStream to write data to a file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            // Create ObjectOutputStream to serialize object
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            // Write object to file
            objectOut.writeObject(game);
            // Close streams
            objectOut.close();
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Game load(String fileName){
        try {
            // Create FileInputStream to read data from the file
            FileInputStream fileIn = new FileInputStream(fileName);
            // Create ObjectInputStream to deserialize object
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            // Read object from file
            Game game = (Game) objectIn.readObject();
            // Close streams
            objectIn.close();
            fileIn.close();
            return game;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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

    public static Tuple getDeterministicFreePlacingSpot(Player player){
        List<Tuple> possibleCoord;
        possibleCoord = new LinkedList<>(player.getPlayArea().getFrontier().stream().toList());
        return possibleCoord.stream().sorted((t1, t2) -> t1.hashCode() < t2.hashCode() ? 1 : -1 )
                .findFirst()
                .orElse(null);
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

    private void printGameState(Game game){
        System.out.println("\n".repeat(1));
        for(Player p : game.getPlayers()){
            System.out.println("Player: " + p.getNickname());
            System.out.println("Points: " + p.getPoints());

            System.out.println("Play area: ");
            System.out.println(new AreaDisplayer(p.getPlayArea()));

            if(p.getState() == PlayerState.PLACE_STARTER_CARD) {
                System.out.println("Hand: ");
                System.out.println(handToString(List.of(p.getStarterCard())));
            }

            if(p.getHand() != null && !p.getHand().isEmpty()) {
                System.out.println("Hand: ");
                System.out.println(handToString(p.getHand()));
            }
        }

        System.out.println("Decks: ");
        System.out.println(deckToString(game.getResourceDeck(), false, game.getGoldDeck(), true));
    }

    private void deterministicallyPlay(Game game, String nickname){
        Player player = getPlayer(game, nickname);
        List<Card> hand = player.getHand();
        Card toPlay = hand.getFirst();
        Tuple spot = getDeterministicFreePlacingSpot(player);

        try {
            game.placeSide(nickname, toPlay, toPlay.getFrontSide(), spot.i, spot.j);
            return;
        } catch (PlacementNotAllowedException | NoAdjacentCardException | InvalidCardException |
                 InvalidCoordinatesException | InvalidSideException | MoveNotAllowedException | NotYourTurnException |
                 NoSuchPlayerException ignored) {}

        try {
            game.placeSide(nickname, toPlay, toPlay.getBackSide(), spot.i, spot.j);
        } catch (PlacementNotAllowedException | NoAdjacentCardException | InvalidCardException |
                 InvalidCoordinatesException | InvalidSideException | MoveNotAllowedException | NotYourTurnException |
                 NoSuchPlayerException e) {
            throw new RuntimeException(e);
        }

    }

    private void deterministicallyDraw(Game game, String nickname){
        Player player = getPlayer(game, nickname);

        for(Card card : ResourceCard.values()) {
            try {
                game.drawVisible(nickname, card);
                return;
            } catch (MoveNotAllowedException | NotYourTurnException | NoSuchPlayerException e) {
                throw new RuntimeException(e);
            } catch (InvalidVisibleCardException e) {
                continue;
            }
        }
        for(Card card : GoldCard.values()) {
            try {
                game.drawVisible(nickname, card);
                return;
            } catch (MoveNotAllowedException | NotYourTurnException | NoSuchPlayerException e) {
                throw new RuntimeException(e);
            } catch (InvalidVisibleCardException e) {
                continue;
            }
        }
        try {
            game.drawDeck(nickname, false);
            return;
        } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException e) {
            throw new RuntimeException(e);
        } catch (EmptyDeckException ignored) {}

        try {
            game.drawDeck(nickname, true);
        } catch (MoveNotAllowedException | NoSuchPlayerException | NotYourTurnException |EmptyDeckException e) {
            throw new RuntimeException(e);
        }
    }
}