package it.polimi.is24am05.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    /**
     * Name of the last saved file
     */
    private static String lastFileSaved = "unBelNiente";

    public GameSaver(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void run() {
        // Save last file name
        String toDelete = lastFileSaved;

        // Save new file and record its name
        lastFileSaved = timeToString();
        controller.saveGame(lastFileSaved);

        // Delete last file
        try{
            Files.delete(Paths.get(toDelete));
        } catch (IOException ignored) {}
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
