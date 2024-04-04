package it.polimi.is24am05.model.game;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterBackSide;
import it.polimi.is24am05.model.exceptions.game.*;
import it.polimi.is24am05.model.exceptions.player.InvalidStarterSideException;
import it.polimi.is24am05.model.objective.Objective;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void placeStarterSide() {

        // da scrivere perchè fanno cagare

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
        // No moves except for placeStarterSide() should be allowed
        assertThrows(MoveNotAllowedException.class, () -> game.chooseObjective(players.getFirst(), Objective.O_088));
        assertThrows(MoveNotAllowedException.class, () -> game.placeSide("pippo", null, null,0,0));

        // Make sure each player places their card
        for(String name : players){
            for(Side side : StarterBackSide.values()){
                try {
                    game.placeStarterSide(name, side);
                } catch (InvalidStarterSideException | MoveNotAllowedException | NoSuchPlayerException ignored) {}
            }
        }

        // Game state should be

    }

    @Test
    void chooseObjective() {
    }

    @Test
    void placeSide() {
    }

    @Test
    void drawDeck() {
    }

    @Test
    void drawVisible() {
    }
}