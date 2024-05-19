package it.polimi.is24am05;

import it.polimi.is24am05.controllers.NicknameRequestSceneController;
import it.polimi.is24am05.controllers.WaitingRoomSceneController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUI {



    /** Reference to the GUIMain class. */
    private final GUIMain guiMain;


    /** Nickname request's scene. */
    private NicknameRequestSceneController nicknameRequestSceneController;
    /** Game's scene. */
   // private GameSceneController gameSceneController;

    /**
     * This constructor takes in input the reference to the current {@code GUIMain} object.
     * @param guiMainReference The reference to the current {@code GUIMain} object.
     */
    public GUI (GUIMain guiMainReference) {
        this.guiMain = guiMainReference;
    }




    private void changeScene(Scene scene) {
        Platform.runLater(() -> {
            Stage stage = guiMain.getPrimaryStage();
         //   stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Publisher material/Icon 50x50px.png"))));
            stage.setTitle("My Shelfie");
            stage.setScene(scene);

            stage.setFullScreen(true);

            stage.show();
        });
    }

    public void askNickname() throws IOException {
        // When the server requires the client nickname, switch to the Nickname Request scene.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("nicknameRequestScene.fxml"));
        Parent root = loader.load();

       nicknameRequestSceneController = loader.getController();
        nicknameRequestSceneController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("NickNameRequestScene.css")).toExternalForm());
        changeScene(scene);
    }

    public void switchToWaitingRoom() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("waitingRoomScene.fxml"));
            Parent root = loader.load();

            WaitingRoomSceneController controller = loader.getController();
            controller.setGUI(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("WaitingRoomScene.css")).toExternalForm());

            changeScene(scene);
        } catch(IOException e) {
            System.out.println("Error");
        }
    }


}
