package it.polimi.is24am05;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.controllers.DealStarterCardsSceneController;
import it.polimi.is24am05.controllers.GameSceneController;
import it.polimi.is24am05.controllers.NicknameRequestSceneController;
import it.polimi.is24am05.controllers.WaitingRoomSceneController;
import it.polimi.is24am05.model.game.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUI  {



    /** Reference to the GUIMain class. */
    private final GUIMain guiMain;

   private WaitingRoomSceneController waitingRoomSceneController;
    /** Nickname request's scene. */
    private NicknameRequestSceneController nicknameRequestSceneController;

    private GameSceneController gameSceneController;
    /** Game's scene. */

    //TODO: change with gameCloned
    private Game gameCloned;
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
            //TODO: DELETE THS
            controller.changeScene();
        } catch(IOException e) {
            System.out.println("Error");
        }
    }
    /**
     * Updates the view after creating the game.
     */
    public void gameCreated() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("gameScene.fxml"));
            Parent root = loader.load();

            gameSceneController = loader.getController();
            gameSceneController.setGUI(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("gameScene.css")).toExternalForm());

            changeScene(scene);
           //controller.update();
        } catch(IOException e) {
            System.out.println("Error");
        }
    }
    /**
     * Updates the view after adding a log.
     */
    public  void addedLog()
    {
        if(waitingRoomSceneController!=null)
            Platform.runLater(()-> {waitingRoomSceneController.showPlayers(gameCloned.getNicknames());});
      //  if(
             //  gameSceneController!=null
     //   )
            //Platform.runLater(()-> {gameSceneController.showPlayers(gameCloned.getNicknames());});

    }







    /**
     * Updates the view after placing the starter side.
     */
    public  void placedStarterSide(){};

    /**
     * Updates the view after another player placed the starter side.
     */
    public  void otherPlacedStarterSide(){};

    /**
     * Updates the view after dealing the hands and objectives.
     */
    public  void handsAndObjectivesDealt(){};

    /**
     * Updates the view after choosing the objective.
     */
    public  void chosenObjective(){};

    /**
     * Updates the view after starting the game.
     */
    public  void gameStarted(){};

    /**
     * Updates the view after placing a side.
     */
    public  void placedSide(){};

    /**
     * Updates the view after another player placed a side.
     */
    public  void otherPlacedSide(){};

    /**
     * Updates the view after drawing a visible card.
     */
    public  void drawnVisible(){};

    /**
     * Updates the view after another player draws a visible card.
     */
    public  void otherDrawnVisible(){};

    /**
     * Updates the view after drawing from a deck.
     */
    public  void drawnDeck(){};

    /**
     * Updates the view after another player draws from a deck.
     */
    public  void otherDrawnDeck(){};

    /**
     * Updates the view after resuming the game.
     */
    public  void gameResumed(){};

    /**
     * Updates the view after another player reconnected to the game.
     */
    public  void otherGameResumed(){};

    /**
     * Updates the view after another player quit the game.
     */
    public  void otherQuitGame(){};

    /**
     * Updates the view after pausing the game.
     */
    public  void gamePaused(){};



}
