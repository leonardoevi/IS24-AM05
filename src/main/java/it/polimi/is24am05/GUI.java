package it.polimi.is24am05;

import it.polimi.is24am05.controllers.NicknameRequestSceneController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUI {

  //  private ClientManager clientManager;

    /** Reference to the {@code GUIMain} class. */
    private final GUIMain guiMain;

    /** List of player's nicknames. */
    private List<String> nicknameList;

    // ---------- SCENE'S REFERENCES ----------
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


    public void askNickname() throws IOException {
        // When the server requires the client nickname, switch to the Nickname Request scene.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NicknameRequestScene.fxml"));
        Parent root = loader.load();

        nicknameRequestSceneController = loader.getController();
        // nicknameRequestSceneController.setOnNicknameConfirmedListener(this::onNicknameConfirmed);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/lobbyStyle.css")).toExternalForm());
        changeScene(scene);
    }


   /*
    public void askPlayersNumber() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/NumPlayerRequestScene.fxml"));
            Parent root = loader.load();

            NumPlayerRequestSceneController controller = loader.getController();
           // controller.setClientManager(clientManager);
           //  controller.setGUI(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/lobbyStyle.css").toExternalForm());

            changeScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    */
    private void changeScene(Scene scene) {
        Platform.runLater(() -> {
            Stage stage = guiMain.getPrimaryStage();
         //   stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Publisher material/Icon 50x50px.png"))));
            stage.setTitle("My Shelfie");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();
        });
    }

}
