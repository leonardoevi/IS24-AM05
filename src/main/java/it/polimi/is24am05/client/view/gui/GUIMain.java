package it.polimi.is24am05.client.view.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GUIMain extends Application {
    private Stage primaryStage;
    private static GUIMain instance;

    private static GUIRoot guiRoot;
    public Map<Scene, Object> sceneControllerMap = new HashMap<>();


    public GUIMain() {
        instance = this;
    }

    public static GUIMain getInstance() {
        return instance;
    }

    public static void launchApp(GUIRoot guiRootInstance) {
        guiRoot = guiRootInstance;
        Application.launch(GUIMain.class);

    }


    public static GUIRoot getGuiRoot() {
        return guiRoot;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = new Stage();
        guiRoot.seGuiMain(this);

        guiRoot.goToFirstScene();

    }
    public void loadScene(Scene scene, Object controller) throws IOException {

        sceneControllerMap.put(scene, controller);

    }
    public Object getControllerForScene(Scene scene) {
        return sceneControllerMap.get(scene);
    }

    public static void main(String[] args) {
        launch();
    }

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