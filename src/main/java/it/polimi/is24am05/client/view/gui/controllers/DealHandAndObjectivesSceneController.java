package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class DealHandAndObjectivesSceneController implements Initializable {

    @FXML
    private AnchorPane mainBackground;
    @FXML
    private Label logField;
    private GUIRoot gui;
    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    }
    public void showLog(String log)
    {
        logField.setText(log);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event ->logField.setText("")
        ));
        timeline.setCycleCount(1);
        timeline.play();

    }
}
