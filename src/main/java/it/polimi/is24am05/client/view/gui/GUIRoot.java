package it.polimi.is24am05.client.view.gui;


import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.client.view.View;
import it.polimi.is24am05.client.view.gui.controllers.*;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.game.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Extends the main class View and
 * manages the needed controllers
 */

public class GUIRoot extends View {

    GUIMain guiMain;

    //TODO : kill the thread when disconnecting
    private Thread guiThread;
    /**
     * Usefull identifier to display each View from the correct point of view
     */
    private String clientNickname;
    private NicknameRequestSceneController nicknameRequestSceneController;

    private GameSceneController gameSceneController;

    private DealStarterCardsSceneController dealStarterCardsSceneController;

    private DealHandAndObjectivesSceneController dealHandAndObjectivesSceneController;
    private ErrorSceneController errorScenecontroller;

    private OtherPlayerSceneController otherPlayerSceneController;
    private LoadWinnerController loadWinnerController;

    private boolean isLoggedOut;

    /**
     * Constructor
     * @param clientModel the client model
     * @param server the server
     */
    public GUIRoot(ClientModel clientModel, ServerHandler server) {
        super(clientModel, server);
        guiThread = new Thread(() -> GUIMain.launchApp(this));
        guiThread.start();
        isLoggedOut = false;

    }

    /**
     * In case server crashes
     */
    @Override
    public void serverUnreachable() {
        try {
            goToErrorScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //guiMain.stop();
    }

    public void goToErrorScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/is24am05/errorscene.fxml"));
        Parent root = loader.load();

        errorScenecontroller = loader.getController();
        errorScenecontroller.setGUI(this);

        Scene scene = new Scene(root,  2560, 1600);

        scene.setUserData(errorScenecontroller);
        guiMain.sceneControllerMap.put(scene, errorScenecontroller);
        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/NickNameRequestScene.css")).toExternalForm());
        changeScene(scene);

    }
    /**
     * GuiMain setter method
     * @param guiMain the object to set
     */
    public void setGuiMain(GUIMain guiMain) {
        this.guiMain = guiMain;
    }

    /**
     * Updates the view after a game Update.
     */
    @Override
    public void updateGame() {
        try {
            Game toDisplay = clientModel.getGame().orElseThrow(NullPointerException::new);

            if (toDisplay != null) {
                System.out.println(toDisplay.getGameState());
                if (toDisplay.getGameState() == GameState.PLACE_STARTER_CARDS) {
                    if (dealStarterCardsSceneController == null) {
                        dealStarterCards();
                        Platform.runLater(() -> {
                            dealStarterCardsSceneController.update(toDisplay);
                        });

                    } else {
                        if (isLoggedOut) {
                            dealStarterCards();
                            isLoggedOut = false;
                        }
                        Platform.runLater(() -> {
                            dealStarterCardsSceneController.update(toDisplay);

                        });
                    }
                } else if (toDisplay.getGameState() == GameState.CHOOSE_OBJECTIVE) {
                    if (dealHandAndObjectivesSceneController == null) {
                        dealHandsAndObjectives();
                        Platform.runLater(() -> {
                            dealHandAndObjectivesSceneController.update(toDisplay);
                        });
                    } else {
                        if (isLoggedOut) {
                            dealHandsAndObjectives();
                            isLoggedOut = false;
                        }
                        Platform.runLater(() -> {
                            dealHandAndObjectivesSceneController.update(toDisplay);
                        });

                    }
                } else if (toDisplay.getGameState() == GameState.GAME || toDisplay.getGameState() == GameState.GAME_ENDING) {
                    if (gameSceneController == null) {
                        loadGame();
                        Platform.runLater(() -> {
                            gameSceneController.update(toDisplay);
                        });

                    } else {
                        if (isLoggedOut) {
                            loadGame();
                            isLoggedOut = false;
                        }
                        Platform.runLater(() -> {
                            gameSceneController.update(toDisplay);
                        });

                    }
                    if (otherPlayerSceneController != null) {
                        Platform.runLater(() -> {
                            otherPlayerSceneController.update(toDisplay);
                        });
                    }
                }else if (toDisplay.getGameState() == GameState.END) {
                    if (loadWinnerController == null) {
                        loadWinner();
                        Platform.runLater(() -> {
                            loadWinnerController.update(toDisplay);
                        });
                    } else {
                        Platform.runLater(() -> {
                            loadWinnerController.update(toDisplay);
                        });
                    }
                }
            }
        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the view after adding a log.
     */
    @Override
    public void updateLogs() {
        try {
            String log = clientModel.getLog().getLast().toString();
            System.out.println(log);
            Scene currentScene = this.getScene();

            if (currentScene != null && guiMain != null) {
                Object controller = guiMain.getControllerForScene(currentScene);

                if (controller instanceof NicknameRequestSceneController) {

                    if (nicknameRequestSceneController != null) {
                        Platform.runLater(() -> {
                            nicknameRequestSceneController.showLog(log);
                        });
                    }
                } else if (controller instanceof GameSceneController) {
                    if (gameSceneController != null)
                        Platform.runLater(() -> {
                            gameSceneController.showLog(log);
                        });

                } else if (controller instanceof DealStarterCardsSceneController) {
                    if (dealStarterCardsSceneController != null)
                        Platform.runLater(() -> {
                            dealStarterCardsSceneController.showLog(log);
                        });
                } else if (controller instanceof DealHandAndObjectivesSceneController) {
                    if (dealHandAndObjectivesSceneController != null)
                        Platform.runLater(() -> {
                            dealHandAndObjectivesSceneController.showLog(log);
                        });

                } else if (controller instanceof OtherPlayerSceneController) {
                    if (otherPlayerSceneController != null)
                        Platform.runLater(() -> {
                            otherPlayerSceneController.showLog(log);
                        });

                } else if (controller instanceof LoadWinnerController) {
                    if (loadWinnerController != null)
                        Platform.runLater(() -> {
                            loadWinnerController.showLog(log);

                        });
                } else return;
            }
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * Returns the current scene.
     */
    public Scene getScene() {
        return guiMain.getPrimaryStage().getScene();
    }


    /**
     * Change Game's scene.
     */

    private void changeScene(Scene scene) {
        Platform.runLater(() -> {
            Stage stage = guiMain.getPrimaryStage();
            stage.setTitle("Codex Naturalis");
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * Calls the server to choose the chosen objective card
     * @param objectiveId Objective card ID
     */
    public void chooseObjective(String objectiveId) {
        server.chooseObjective(objectiveId);
    }

    /**
     * Initialize other player's view.
     */
    public void viewOtherPlayer(String clientClicked, String playerNickname, Game game) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/is24am05/otherPlayerScene.fxml"));
        Parent root = loader.load();

        otherPlayerSceneController = loader.getController();
        otherPlayerSceneController.setGUI(this);
        otherPlayerSceneController.setClientNickname(playerNickname);
        otherPlayerSceneController.setClientClicked(clientClicked);


        Scene scene = new Scene(root, 2560, 1600);

        scene.setUserData(otherPlayerSceneController);
        guiMain.sceneControllerMap.put(scene, otherPlayerSceneController);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/gameScene.css")).toExternalForm());
        changeScene(scene);
        Platform.runLater(() -> otherPlayerSceneController.update(game));
    }

    /**
     * Returns to the correct scene after viewing another player
     * @param clientNickname The nickname of the client that wants to return to his play scene
     * @param game the Game
     */

    public void returnPlay(String clientNickname, Game game) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/gameScene.fxml"));
            Parent root = loader.load();

            gameSceneController = loader.getController();
            gameSceneController.setGUI(this);
            gameSceneController.setClientNickname(clientNickname);

            Scene scene = new Scene(root, 1500, 1000);

            scene.setUserData(gameSceneController);
            guiMain.sceneControllerMap.put(scene, gameSceneController);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/gameScene.css")).toExternalForm());
            changeScene(scene);
            Platform.runLater(() -> gameSceneController.update(game));
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Initialize the first Scene where the player choose the nickname and the number of players.
     */
    public void goToFirstScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/is24am05/nicknameRequestScene.fxml"));
        Parent root = loader.load();

        nicknameRequestSceneController = loader.getController();
        nicknameRequestSceneController.setGUI(this);

        Scene scene = new Scene(root,  2560, 1600);

        scene.setUserData(nicknameRequestSceneController);
        guiMain.sceneControllerMap.put(scene, nicknameRequestSceneController);
        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/NickNameRequestScene.css")).toExternalForm());
        changeScene(scene);

    }

    /**
     * Initialize the deal hands and objectives scene
     */
    public void dealHandsAndObjectives() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/is24am05/dealHandAndObjectivesScene.fxml"));
        Parent root = loader.load();

        dealHandAndObjectivesSceneController = loader.getController();
        dealHandAndObjectivesSceneController.setGUI(this);
        dealHandAndObjectivesSceneController.setClientNickname(clientNickname);

        Scene scene = new Scene(root, 1500, 1000);

        scene.setUserData(dealHandAndObjectivesSceneController);
        guiMain.sceneControllerMap.put(scene, dealHandAndObjectivesSceneController);
        changeScene(scene);
    }

    /**
     * Sets the chosen nickname
     * @param nickname String chosen by user
     */
    public void nicknameChosen(String nickname) {
        this.clientNickname = nickname;
        server.setNickname(nickname);
        server.joinServer();
        server.joinGame();


    }

    /**
     * Places starter card
     * @param isFront the side of the Card
     */
    public void placeStarterSide(boolean isFront) {
        server.placeStarterSide(isFront);
    }

    /**
     * Sets the number of Players chosen by the first user
     * @param num
     */
    public void numberOfplayersChosen(int num) {
        server.setNumberOfPlayers(num);
    }


    /**
     * Updates the view after creating the game.
     */
    public void loadGame() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/gameScene.fxml"));
            Parent root = loader.load();

            gameSceneController = loader.getController();
            gameSceneController.setGUI(this);
            gameSceneController.setClientNickname(clientNickname);

            Scene scene = new Scene(root,  1500, 1000);

            scene.setUserData(gameSceneController);
            guiMain.sceneControllerMap.put(scene, gameSceneController);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/loadWinner.css")).toExternalForm());
            changeScene(scene);

            //gameSceneController.update();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Loads winner scene after game ended
     */
    public void loadWinner() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/loadWinner.fxml"));
            Parent root = loader.load();

            loadWinnerController = loader.getController();
            loadWinnerController.setGUI(this);

            Scene scene = new Scene(root,  1500, 1000);

            scene.setUserData(loadWinnerController);
            guiMain.sceneControllerMap.put(scene, loadWinnerController);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/loadWinner.css")).toExternalForm());
            changeScene(scene);

            //gameSceneController.update();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Draws from the 2 Decks
     * @param isGold if true draws a gold card, otherwise a resource
     */
    public void drawDeck(boolean isGold) {
        server.drawDeck(isGold);
    }
    /**
     * Draws from the 4 visible cards
     * @param cardId the ID of the chosen card
     */
    public void drawVisible(String cardId) {
        server.drawVisible(cardId);
    }

    /**
     * Updates the view when the starter cards are dealt.
     */
    public void dealStarterCards() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/polimi/is24am05/dealStarterCardsScene.fxml"));
            Parent root = loader.load();

            dealStarterCardsSceneController = loader.getController();
            dealStarterCardsSceneController.setGUI(this);
            dealStarterCardsSceneController.setNickname(clientNickname);

            Scene scene = new Scene(root,  1500, 1000);

            scene.setUserData(dealStarterCardsSceneController);
            guiMain.sceneControllerMap.put(scene, dealStarterCardsSceneController);
            // scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/is24am05/NickNameRequestScene.css")).toExternalForm());
            changeScene(scene);

            // dealStarterCardsSceneController.update();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Places a card
     * @param cardId the card ID
     * @param isFront Which side we are playing
     * @param i x coordinate
     * @param j y coordinate
     */
    public void placeCard(String cardId, boolean isFront, int i, int j) {
        server.placeSide(cardId, isFront, i, j);
    }
    public void sendMessage(String message, String receiver){
        if (!Objects.equals(receiver, "All the players"))
            server.sendDirectMessage(message, receiver);
        else
            server.sendMessage(message);
    }

    public void logout() throws IOException {
        isLoggedOut = true;
        server.leaveServer();
        goToFirstScene();
    }
}
