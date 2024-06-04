package it.polimi.is24am05.client.view.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class that deploys the GUI
 */
public class GUIMain extends Application {
    private Stage primaryStage;
    private static GUIMain instance;

    private static GUIRoot guiRoot;
    public Map<Scene, Object> sceneControllerMap = new HashMap<>();


    public GUIMain() {
        instance = this;
    }

    /**
     * Launches the application
     * @param guiRootInstance sets the GUI root
     */
    public static void launchApp(GUIRoot guiRootInstance) {
        guiRoot = guiRootInstance;
        Application.launch(GUIMain.class);

    }

    /**
     * Starts the application and displays first scene
     * @param stage Stage to set
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = new Stage();
        guiRoot.setGuiMain(this);
        guiRoot.goToFirstScene();

    }

    /**
     * Given a scene return the controller of that scene
     * @param scene scene to get controller from
     * @return Controller
     */
    public Object getControllerForScene(Scene scene) {
        return sceneControllerMap.get(scene);
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Stops the application
     */
    public void stop () {
        Platform.exit();
        System.exit(0);
    }

    /**
     * @return A reference to the primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


}