package it.polimi.is24am05.controller;

import it.polimi.is24am05.model.game.Game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class will save the game to a file
 */
public class GameSaver implements Runnable {
    /**
     * Reference to the game that will be saved
     */
    private final Controller controller;

    public GameSaver(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void run() {
        controller.saveGame(timeToString());
    }

    private static String timeToString() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");

        // Format the current date and time

        return now.format(formatter);
    }
}
