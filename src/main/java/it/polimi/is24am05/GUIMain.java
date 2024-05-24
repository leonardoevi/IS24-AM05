package it.polimi.is24am05;

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
    private static final CountDownLatch latch = new CountDownLatch(1);
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
        new Thread(() -> Application.launch(GUIMain.class)).start();
        waitForStart();
    }

    public static void waitForStart() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static GUIRoot getGuiRoot() {
        return guiRoot;
    }

    @Override
    public void start(Stage stage) throws IOException {

        guiRoot.seGuiMain(this);
        this.primaryStage = new Stage();
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