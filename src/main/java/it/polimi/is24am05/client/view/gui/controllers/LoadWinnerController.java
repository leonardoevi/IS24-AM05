package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Load Winner scene Controller
 */
public class LoadWinnerController implements Initializable {
    private GUIRoot gui;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vBox;
    @FXML
    private Label winners;

    @FXML
    private Label logField;

    /**
     * Initializes the scene by simply setting the background
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String leftSidebackPath = getClass().getResource("/assets/images/leftSideBackground.png").toExternalForm();
        Image leftSideBackgroundImage = new Image(leftSidebackPath);
        BackgroundImage leftSideBack = new BackgroundImage(
                leftSideBackgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );
        borderPane.setBackground(new Background(leftSideBack));

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
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }
    /**
     * Updates the scene when it receives a new Game update
     * @param game Game update
     */
    public void update(Game game) {

        String text = "";
        if (game.getWinners().size()>1){
            text += "The winners are: \n";
            for(Player p : game.getWinners()){
               text +=p.getNickname();
               text +="\n";
            }

        }else{
            text+="The winner is: " + game.getWinners().getFirst().getNickname();
        }

        winners.setText(text);

        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(-200, 0, 0, 0));
        vBox.getChildren().add(winners);
        vBox.setSpacing(30);

        borderPane.setCenter(vBox);

    }

}
