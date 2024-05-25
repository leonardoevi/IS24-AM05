package it.polimi.is24am05.client.view.gui;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.client.view.gui.controllers.*;
import it.polimi.is24am05.model.enums.state.GameState;
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
        /*

        Game toDisplay;
        try {
            System.out.println("eccmi");
             toDisplay = clientModel.getGame().orElseThrow(NullPointerException::new);
            System.out.println("son qui");

             if(toDisplay!=null)
                 System.out.println(toDisplay.getGameState());
                 if(toDisplay.getGameState()== GameState.PLACE_STARTER_CARDS) {
                     if(dealStarterCardsSceneController==null)
                     {
                         dealStarterCards();
                     }
                     else{
                         dealStarterCardsSceneController.update();
                     }

                 }







        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        }

         */

    }
    @Override
    public void updateLogs() {



        try {
            System.out.println("loggggg");
            String log=clientModel.getLog().getLast().toString();
            System.out.println(log);
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
        } catch (NullPointerException ignored) {}



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
        loader.setLocation(getClass().getResource("/it/polimi/is24am05/nicknameRequestScene.fxml"));
        Parent root = loader.load();

        nicknameRequestSceneController = loader.getController();
        nicknameRequestSceneController.setGUI(this);

        Scene scene = new Scene(root);

        scene.setUserData(nicknameRequestSceneController);
        guiMain.sceneControllerMap.put(scene, nicknameRequestSceneController);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/NickNameRequestScene.css")).toExternalForm());
        changeScene(scene);

    }
    public void dealHandsAndObjectives() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/is24am05/dealHandAndObjectivesScene.fxml"));
        Parent root = loader.load();

        dealHandAndObjectivesSceneController = loader.getController();


        dealHandAndObjectivesSceneController.setGUI(this);

        Scene scene = new Scene(root);
        scene.setUserData(dealHandAndObjectivesSceneController);
        guiMain.sceneControllerMap.put(scene, dealHandAndObjectivesSceneController);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/gameScene.css")).toExternalForm());

        changeScene(scene);

    }

    public void nicknameChosen(String nickname)
    {


      server.setNickname(nickname);
        System.out.println(server.getNickname());

     // server.joinServer();
        System.out.println("DOPO");

    }

    public void numberOfplayersChosen(int num)
    {
        /*

        updateLogs();

        dealStarterCards();
        */
        System.out.println("PRIMA NUM");
        server.setNumberOfPlayers(num);
        System.out.println("DOPO NUM");





    }

    public void switchToWaitingRoom() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/waitingRoomScene.fxml"));
            Parent root = loader.load();

            WaitingRoomSceneController controller = loader.getController();
            controller.setGUI(this);

            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/WaitingRoomScene.css")).toExternalForm());

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
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/gameScene.fxml"));
            Parent root = loader.load();

            gameSceneController = loader.getController();
            gameSceneController.setGUI(this);

            Scene scene = new Scene(root);
            scene.setUserData(gameSceneController);
            guiMain.sceneControllerMap.put(scene, gameSceneController);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/gameScene.css")).toExternalForm());

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
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/dealStarterCardsScene.fxml"));
            Parent root = loader.load();

            dealStarterCardsSceneController = loader.getController();
            dealStarterCardsSceneController.setGUI(this);

            Scene scene = new Scene(root);

            scene.setUserData(dealStarterCardsSceneController);
            guiMain.sceneControllerMap.put(scene, dealStarterCardsSceneController);
           // scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/NickNameRequestScene.css")).toExternalForm());
            changeScene(scene);

            dealStarterCardsSceneController.update();
        } catch(IOException e) {
            System.out.println("Error");
        }
    }
    /**
     * Updates the view after adding a log.
     */





}
