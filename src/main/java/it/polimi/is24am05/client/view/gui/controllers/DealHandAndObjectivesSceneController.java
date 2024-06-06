package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Deal Hand and Objective Card scene Controller
 */
public class DealHandAndObjectivesSceneController implements Initializable {

    @FXML
    private AnchorPane mainBackground;
    @FXML
    private Label logField;
    private GUIRoot gui;

    @FXML
    ImageView handCard1FS;

    @FXML
    ImageView handCard1BS;
    @FXML
    ImageView handCard2FS;

    @FXML
    ImageView handCard2BS;
    @FXML
    ImageView handCard3FS;

    @FXML
    ImageView handCard3BS;

    @FXML
    ImageView objectiveCard1;
    @FXML
    ImageView objectiveCard2;
    @FXML
    private Button logout;

    Map<ImageView, String> objectivePathMap;


    private String clientNickname;

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    public void setClientNickname(String nickname) {
        this.clientNickname = nickname;
    }

    /**
     * Initializes the scene by setting up the background, the hand cards and the objectives to choose
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objectivePathMap = new HashMap<>();
        logField.setText("");
        String leftSidebackPath = getClass().getResource("/assets/images/leftSideBackground.png").toExternalForm();


        Image leftSideBackgroundImage = new Image(leftSidebackPath);
        BackgroundImage leftSideBack = new BackgroundImage(
                leftSideBackgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        mainBackground.setBackground(new Background(leftSideBack));

        handCard1FS.setPreserveRatio(false);
        AnchorPane.setTopAnchor(handCard1FS, 100.0);
        AnchorPane.setLeftAnchor(handCard1FS, 30.0);
        handCard1FS.setFitWidth(120);
        handCard1FS.setFitHeight(90);

        handCard1BS.setPreserveRatio(false);
        AnchorPane.setTopAnchor(handCard1BS, 220.0);
        AnchorPane.setLeftAnchor(handCard1BS, 30.0);
        handCard1BS.setFitWidth(120);
        handCard1BS.setFitHeight(90);

        handCard2FS.setPreserveRatio(false);
        AnchorPane.setTopAnchor(handCard2FS, 100.0);
        AnchorPane.setLeftAnchor(handCard2FS, 250.0);
        handCard2FS.setFitWidth(120);
        handCard2FS.setFitHeight(90);

        handCard2BS.setPreserveRatio(false);
        AnchorPane.setTopAnchor(handCard2BS, 220.0);
        AnchorPane.setLeftAnchor(handCard2BS, 250.0);
        handCard2BS.setFitWidth(120);
        handCard2BS.setFitHeight(90);

        handCard3FS.setPreserveRatio(false);
        AnchorPane.setTopAnchor(handCard3FS, 100.0);
        AnchorPane.setLeftAnchor(handCard3FS, 470.0);
        handCard3FS.setFitWidth(120);
        handCard3FS.setFitHeight(90);

        handCard3BS.setPreserveRatio(false);
        AnchorPane.setTopAnchor(handCard3BS, 220.0);
        AnchorPane.setLeftAnchor(handCard3BS, 470.0);
        handCard3BS.setFitWidth(120);
        handCard3BS.setFitHeight(90);

        objectiveCard1.setPreserveRatio(false);
        AnchorPane.setBottomAnchor(objectiveCard1, 220.0);
        AnchorPane.setLeftAnchor(objectiveCard1, 600.0);
        objectiveCard1.setFitWidth(120);
        objectiveCard1.setFitHeight(90);

        objectiveCard2.setPreserveRatio(false);
        AnchorPane.setBottomAnchor(objectiveCard2, 220.0);
        AnchorPane.setLeftAnchor(objectiveCard2, 750.0);
        objectiveCard2.setFitWidth(120);
        objectiveCard2.setFitHeight(90);


    }

    /**
     * Chooses the secret objective card
     * @param event Click with the mouse on Imageview
     */
    @FXML
    public void onTouch(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        gui.chooseObjective(objectivePathMap.get(source));
    }
    /**
     * Shows logs
     * @param log log to show
     */
    public void showLog(String log) {
        logField.setText(log);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> logField.setText("")
        ));
        timeline.setCycleCount(1);
        timeline.play();

    }
    /**
     * Updates the scene when it receives a new Game update
     * @param game Game update
     */
    public void update(Game game) {
        int idCard;
        String pathcardfront;
        String pathcardback;
        String path;
        String card = "";
        for (Player p : game.getPlayers()) {
            String appendfront = "/assets/images/front/";
            String appendback = "/assets/images/back/";
            if (p.getNickname().equals(clientNickname)) {

                List<Card> cards = p.getHand();
                Card c = cards.get(0);
                idCard = c.getId();
                // TODO parsing function
                if (idCard < 100 && idCard >= 10) {
                    appendback += "0";
                    appendfront += "0";
                } else if (idCard < 10) {
                    appendback += "00";
                    appendfront += "00";
                }

                pathcardfront = appendfront + idCard + ".png";
                path = getClass().getResource(pathcardfront).toExternalForm();
                handCard1FS.setImage(new Image(path));


                pathcardback = appendback + idCard + ".png";
                path = getClass().getResource(pathcardback).toExternalForm();
                handCard1BS.setImage(new Image(path));

                appendfront = "/assets/images/front/";
                appendback = "/assets/images/back/";

                c = cards.get(1);
                idCard = c.getId();

                if (idCard < 100 && idCard >= 10) {
                    appendback += "0";
                    appendfront += "0";
                } else if (idCard < 10) {
                    appendback += "00";
                    appendfront += "00";
                }
                pathcardfront = appendfront + idCard + ".png";
                path = getClass().getResource(pathcardfront).toExternalForm();
                handCard2FS.setImage(new Image(path));


                pathcardback = appendback + idCard + ".png";
                path = getClass().getResource(pathcardback).toExternalForm();
                handCard2BS.setImage(new Image(path));

                appendfront = "/assets/images/front/";
                appendback = "/assets/images/back/";
                c = cards.get(2);
                idCard = c.getId();

                if (idCard < 100 && idCard >= 10) {
                    appendback += "0";
                    appendfront += "0";
                } else if (idCard < 10) {
                    appendback += "00";
                    appendfront += "00";
                }

                pathcardfront = appendfront + idCard + ".png";
                path = getClass().getResource(pathcardfront).toExternalForm();
                handCard3FS.setImage(new Image(path));

                pathcardback = appendback + idCard + ".png";
                path = getClass().getResource(pathcardback).toExternalForm();
                handCard3BS.setImage(new Image(path));

                appendfront = "/assets/images/front/";

                card = p.getObjectivesHand()[0].name();

                pathcardfront = appendfront + card.substring(2) + ".png";
                path = getClass().getResource(pathcardfront).toExternalForm();
                objectiveCard1.setImage(new Image(path));
                objectivePathMap.put(objectiveCard1, card);

                card = p.getObjectivesHand()[1].name();
                pathcardfront = appendfront + card.substring(2) + ".png";
                path = getClass().getResource(pathcardfront).toExternalForm();
                objectiveCard2.setImage(new Image(path));
                objectivePathMap.put(objectiveCard2, card);


            }
        }
        logout.setOnAction(event -> {
            try {
                gui.logout();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
