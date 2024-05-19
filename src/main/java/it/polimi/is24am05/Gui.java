package it.polimi.is24am05;

import it.polimi.is24am05.controllers.ConnectionInfoSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource("connectionInfoScene.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root, 320, 240);

        scene.getStylesheets().add(Gui.class.getResource("ConnectionInfoScene.css").toExternalForm());
        ConnectionInfoSceneController controller = (ConnectionInfoSceneController) fxmlLoader.getController();

        stage.setTitle("Hello!");
        stage.setScene(scene);
         stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}