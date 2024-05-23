package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUIRoot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

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
    private GUIRoot gui;
    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
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
        String imagePath = getClass().getResource("/assets/images/027.png").toExternalForm();
        mainBackground.setBackground(new Background(leftSideBack));


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



    }
}
