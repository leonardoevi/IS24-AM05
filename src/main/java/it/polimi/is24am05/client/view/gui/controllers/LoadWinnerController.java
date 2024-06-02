package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.time.format.TextStyle;
import java.util.ResourceBundle;

public class LoadWinnerController implements Initializable {
    private GUIRoot gui;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label text;
    @FXML
    private Label winners;

    @FXML
    private Label logField;
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

        AnchorPane.setTopAnchor(text, 50.0);
        AnchorPane.setLeftAnchor(text, 0.0);
        AnchorPane.setRightAnchor(text, 0.0);
        text.setText("GAME ENDED");
        anchorPane.setBackground(new Background(leftSideBack));

    }
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

    public void update(Game game) {
        String text = "";
        if (game.getWinners().size()>1){
            text += "The winners are: \n";
            for(Player p : game.getWinners()){
               text +=p.getNickname();
               text +="\n";
            }

        }else{
            text+="The winners is: " + game.getWinners().getFirst().getNickname();
        }

        winners.setText(text);
        AnchorPane.setLeftAnchor(winners, 0.0);
        AnchorPane.setRightAnchor(winners, 0.0);

    }

}
