package it.polimi.is24am05.client.view.gui;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.client.view.gui.controllers.*;
import it.polimi.is24am05.model.game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class GUIRoot extends View {

    GUIMain guiMain;
    private WaitingRoomSceneController waitingRoomSceneController;

    private NicknameRequestSceneController nicknameRequestSceneController;

    private GameSceneController gameSceneController;

    private DealStarterCardsSceneController dealStarterCardsSceneController;

    private DealHandAndObjectivesSceneController dealHandAndObjectivesSceneController;

    public GUIRoot(ClientModel clientModel, ServerHandler server) {
        super(clientModel, server);
        GUIMain.launchApp(this);

    }

    @Override
    public void serverUnreachable() {
        // TODO : manuel questo metodo viene chiamato quando la connessione al server si perde (oppure fallisce all'inizio) tutto si deve chiudere.
    }

    public void seGuiMain(GUIMain guiMain)
    {
        this.guiMain=guiMain;
    }
    @Override
    public void updateGame() {

        Game toDisplay;
        try {
             toDisplay = clientModel.getGame().orElseThrow(NullPointerException::new);
             if(toDisplay==null)
                  return;

        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        }

    }
    @Override
    public void updateLogs() {

            String log = "Devi inserire il numero dei giocatori";
        /*
        try {
            log=clientModel.getLog().getLast();
        } catch (NullPointerException ignored) {}
        *
         */
            Scene currentScene = this.getScene();

            if (currentScene != null && guiMain != null) {
                Object controller = guiMain.getControllerForScene(currentScene);

                if (controller instanceof NicknameRequestSceneController) {

                    if (nicknameRequestSceneController != null) {

                        Platform.runLater(() -> {
                            nicknameRequestSceneController.showLog(log);
                            // nicknameRequestSceneController.resetPlayerNickname();
                        });
                    }

                }  else if (controller instanceof GameSceneController) {
                    if(gameSceneController!=null)
                    Platform.runLater(() -> {
                        gameSceneController.showLog(log);
                        // nicknameRequestSceneController.resetPlayerNickname();
                    });

                } else if (controller instanceof DealStarterCardsSceneController) {
                    if(dealStarterCardsSceneController!=null)
                    Platform.runLater(() -> {
                        dealStarterCardsSceneController.showLog(log);
                        // nicknameRequestSceneController.resetPlayerNickname();
                    });
                }

                else if (controller instanceof DealHandAndObjectivesSceneController)
                {
                    if(dealHandAndObjectivesSceneController!=null)
                    Platform.runLater(() -> {
                        dealHandAndObjectivesSceneController.showLog(log);
                        // nicknameRequestSceneController.resetPlayerNickname();
                    });

                }
                else return;
            }


    }
    /** returns the current scene. */
    public Scene getScene() {
        return guiMain.getPrimaryStage().getScene();
    }


    /** Game's scene. */

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

    public void goToFirstScene() throws IOException {
        // When the server requires the client nickname, switch to the Nickname Request scene.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("nicknameRequestScene.fxml"));
        Parent root = loader.load();

        nicknameRequestSceneController = loader.getController();
        nicknameRequestSceneController.setGUI(this);

        Scene scene = new Scene(root);

        scene.setUserData(nicknameRequestSceneController);
        guiMain.sceneControllerMap.put(scene, nicknameRequestSceneController);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("NickNameRequestScene.css")).toExternalForm());
        changeScene(scene);

    }
    public void dealHandsAndObjectives() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("dealHandAndObjectivesScene.fxml"));
        Parent root = loader.load();

        dealHandAndObjectivesSceneController = loader.getController();


        dealHandAndObjectivesSceneController.setGUI(this);

        Scene scene = new Scene(root);
        scene.setUserData(dealHandAndObjectivesSceneController);
        guiMain.sceneControllerMap.put(scene, dealHandAndObjectivesSceneController);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("gameScene.css")).toExternalForm());

        changeScene(scene);

    }

    public void nicknameChosen(String nickname)
    {

      //  server.setNickname(nickname);
        //  server.joinServer();

      // updateLogs();
        System.out.println("2");
        //server.setNickname(nickname);
        // server.joinServer();

    }

    public void numberOfplayersChosen(int num)
    {
        /*

        updateLogs();

        dealStarterCards();
        */
        System.out.println("1");
       // server.setNumberOfPlayers(num);
      updateLogs();
        try {
            dealHandsAndObjectives();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            scene.setUserData(gameSceneController);
            guiMain.sceneControllerMap.put(scene, gameSceneController);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("gameScene.css")).toExternalForm());

            changeScene(scene);
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(3),
                    event -> updateLogs()
            ));
            timeline.setCycleCount(1);
            timeline.play();
            //controller.update();
        } catch(IOException e) {
            System.out.println("Error");
        }
    }

    public void dealStarterCards() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("dealStarterCardsScene.fxml"));
            Parent root = loader.load();

            dealStarterCardsSceneController = loader.getController();


            dealStarterCardsSceneController.setGUI(this);
            dealStarterCardsSceneController.setClientModel(clientModel);
            Scene scene = new Scene(root);
            scene.setUserData(dealStarterCardsSceneController);
            guiMain.sceneControllerMap.put(scene, dealStarterCardsSceneController);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("gameScene.css")).toExternalForm());

            changeScene(scene);
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(3),
                    event -> updateLogs()
            ));
            timeline.setCycleCount(1);
            timeline.play();
            //controller.update();
        } catch(IOException e) {
            System.out.println("Error");
        }
    }
    /**
     * Updates the view after adding a log.
     */





}