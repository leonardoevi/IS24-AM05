package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUIRoot;
import it.polimi.is24am05.client.model.ClientModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class DealStarterCardsSceneController implements Initializable {
    @FXML
    private AnchorPane mainBackground;
    @FXML
    private ImageView mySCFrontSide;
    @FXML
    private ImageView mySCBackSide;
    @FXML
    private ImageView player1SCBackSide;
    @FXML
    private ImageView player1SCFrontSide;
    @FXML
    private ImageView player2SCBackSide;
    @FXML
    private ImageView player2SCFrontSide;
    @FXML
    private ImageView player3SCBackSide;
    @FXML
    private ImageView player3SCFrontSide;
    @FXML
    private Label logField;
    private GUIRoot gui;

    private ClientModel client;
    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }

    public void setClientModel(ClientModel client)
    {
        this.client=client;
    }

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

    public void update()

    {

        String imagePath = getClass().getResource("/assets/images/027.png").toExternalForm();
        mySCFrontSide.setPreserveRatio(false);
        mySCFrontSide.setImage(new Image(imagePath));
        mySCFrontSide.setFitWidth(120);
        mySCFrontSide.setFitHeight(90);
        AnchorPane.setBottomAnchor(mySCFrontSide, 20.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(mySCFrontSide, 200.0);


        mySCBackSide.setPreserveRatio(false);
        mySCBackSide.setImage(new Image(imagePath));
        mySCBackSide.setFitWidth(120);
        mySCBackSide.setFitHeight(90);
        AnchorPane.setBottomAnchor(mySCBackSide, 20.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(mySCBackSide, 350.0);

        AnchorPane.setTopAnchor(logField, 20.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(logField, 600.0);
    }
}
