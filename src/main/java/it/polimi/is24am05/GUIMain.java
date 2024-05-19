package it.polimi.is24am05;

import it.polimi.is24am05.controllers.ConnectionInfoSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIMain extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = new Stage();
        GUI guiInterface = new GUI(this);
        FXMLLoader fxmlLoader = new FXMLLoader(GUIMain.class.getResource("connectionInfoScene.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root, 320, 240);

        scene.getStylesheets().add(GUIMain.class.getResource("ConnectionInfoScene.css").toExternalForm());
        ConnectionInfoSceneController controller = (ConnectionInfoSceneController) fxmlLoader.getController();
        controller.setGUI(guiInterface);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
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