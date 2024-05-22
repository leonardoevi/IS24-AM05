package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUI;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController  implements Initializable  {

    @FXML
    private GridPane playArea;
    @FXML
    private StackPane backgroundPlayArea;
    @FXML
    private ImageView resourceDeckTop;
    @FXML
    private ImageView goldDeckTop;
    @FXML
    private ImageView resourceVisible1;
    @FXML
    private ImageView resourceVisible2;
    @FXML
    private ImageView goldVisible1;
    @FXML
    private ImageView goldVisible2;
    @FXML
    private ImageView handBackSide1;
    @FXML
    private ImageView handFrontSide1;
    @FXML
    private ImageView handBackSide2;
    @FXML
    private ImageView handFrontSide2;
    @FXML
    private ImageView handBackSide3;
    @FXML
    private ImageView handFrontSide3;

    @FXML
    private ImageView commonObjective1;
    @FXML
    private ImageView commonObjective2;
    @FXML


    private ImageView myObjective;
    @FXML

    private Button buttonPlayer1;
    @FXML

    private Button buttonPlayer2;
    @FXML

    private Button buttonPlayer3;
    @FXML

    private Button buttonPlayer4;

    @FXML

    private Label myPoints;





    @FXML
    private StackPane leftSideBackground;
    @FXML
    private AnchorPane mainBackground;
    private GUI gui;
    public void setGUI(GUI gui)
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

        mainBackground.setBackground(new Background(leftSideBack));

        String imageBackPath = getClass().getResource("/assets/images/playAreaBackground.png").toExternalForm();
        AnchorPane.setTopAnchor(backgroundPlayArea, 50.0);
        AnchorPane.setLeftAnchor(backgroundPlayArea, 300.0);
        AnchorPane.setBottomAnchor(backgroundPlayArea, 50.0);
        AnchorPane.setRightAnchor(backgroundPlayArea, 300.0);






        Image backgroundImage = new Image(imageBackPath); // Sostituisci con il percorso della tua immagine

        // Crea un BackgroundImage
        BackgroundImage backgroundImg = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        // Imposta il Background al AnchorPane
        backgroundPlayArea.setBackground(new Background(backgroundImg));

        String imagePath = getClass().getResource("/assets/images/027.png").toExternalForm();
        for (int i = 0; i < 20; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100 / 20.0);
            playArea.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100 / 20.0);
            playArea.getRowConstraints().add(rowConstraints);
        }
        StackPane.setMargin(playArea, new Insets(50));
        playArea.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for (int i = 0; i < 20; i++) {
                playArea.getColumnConstraints().get(i).setPrefWidth(newValue.doubleValue() / 20);
            }
        });

        playArea.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for (int i = 0; i < 20; i++) {
                playArea.getRowConstraints().get(i).setPrefHeight(newValue.doubleValue() / 20);
            }
        });
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {

                if((row+col)%2==0) {

                    ImageView card = new ImageView(new Image(imagePath));


/*




                    playArea.add(card, col, row);




                }





                 */
                    String imagePath2 = getClass().getResource("/assets/images/027.png").toExternalForm();
                   StackPane region = new StackPane();
                  region.setBackground(new Background(new BackgroundImage(new Image(imagePath2),  BackgroundRepeat.NO_REPEAT,
                          BackgroundRepeat.NO_REPEAT,
                          BackgroundPosition.CENTER,
                          new BackgroundSize(1.0, 1.0, true, true, false, false))));
                    playArea.add(region, col, row);

                }
            }
        }

       resourceDeckTop.setPreserveRatio(false);
        resourceDeckTop.setImage(new Image(imageBackPath));
        resourceDeckTop.setFitWidth(120);
        resourceDeckTop.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceDeckTop, 130.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(resourceDeckTop, 20.0);

        resourceVisible1.setImage(new Image(imageBackPath));
        resourceVisible1.setFitWidth(120);
        resourceVisible1.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceVisible1, 70.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(resourceVisible1, 150.0);

        resourceVisible2.setImage(new Image(imageBackPath));
        resourceVisible2.setFitWidth(120);
        resourceVisible2.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceVisible2, 200.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(resourceVisible2, 150.0);

        goldDeckTop.setPreserveRatio(false);
        goldDeckTop.setImage(new Image(imageBackPath));
        goldDeckTop.setFitWidth(120);
        goldDeckTop.setFitHeight(90);
        AnchorPane.setTopAnchor(goldDeckTop, 370.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(goldDeckTop, 20.0);

        goldVisible1.setImage(new Image(imageBackPath));
        goldVisible1.setFitWidth(120);
        goldVisible1.setFitHeight(90);
        AnchorPane.setTopAnchor(goldVisible1, 340.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(goldVisible1, 150.0);

        goldVisible2.setImage(new Image(imageBackPath));
        goldVisible2.setFitWidth(120);
        goldVisible2.setFitHeight(90);
        AnchorPane.setTopAnchor(goldVisible2, 450.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(goldVisible2, 150.0);


        handBackSide1.setImage(new Image(imageBackPath));
        handBackSide1.setFitWidth(120);
        handBackSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide1, 50.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handBackSide1, 20.0);

        handFrontSide1.setImage(new Image(imageBackPath));
        handFrontSide1.setFitWidth(120);
        handFrontSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide1, 50.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handFrontSide1, 150.0);

        handBackSide2.setImage(new Image(imageBackPath));
        handBackSide2.setFitWidth(120);
        handBackSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide2, 150.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handBackSide2, 20.0);

        handFrontSide2.setImage(new Image(imageBackPath));
        handFrontSide2.setFitWidth(120);
        handFrontSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide2, 150.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handFrontSide2, 150.0);

        handFrontSide3.setImage(new Image(imageBackPath));
        handFrontSide3.setFitWidth(120);
        handFrontSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide3, 250.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handFrontSide3, 20.0);

        handBackSide3.setImage(new Image(imageBackPath));
        handBackSide3.setFitWidth(120);
        handBackSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide3, 250.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handBackSide3, 150.0);

        AnchorPane.setTopAnchor(myPoints, 30.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(myPoints, 90.0);
        myPoints.setPrefWidth(200); // Imposta la larghezza preferita del Label
        myPoints.setAlignment(Pos.CENTER);

        commonObjective1.setImage(new Image(imageBackPath));
        commonObjective1.setFitWidth(120);
        commonObjective1.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective1, 130.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(commonObjective1, 20.0);

        commonObjective2.setImage(new Image(imageBackPath));
        commonObjective2.setFitWidth(120);
        commonObjective2.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective2, 130.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(commonObjective2, 150.0);


        myObjective.setImage(new Image(imageBackPath));
        myObjective.setFitWidth(120);
        myObjective.setFitHeight(90);
        AnchorPane.setTopAnchor(myObjective, 280.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(myObjective, 100.0);




        AnchorPane.setBottomAnchor(buttonPlayer1, 30.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer1, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer2, 70.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer2, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer3, 110.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer3, 20.0);
        AnchorPane.setBottomAnchor(buttonPlayer4, 140.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer4, 20.0);
        buttonPlayer1.setPrefWidth(200); // Imposta la larghezza preferita del Button
        buttonPlayer1.setAlignment(Pos.CENTER); // Cen
        buttonPlayer2.setPrefWidth(200); // Imposta la larghezza preferita del Button
        buttonPlayer2.setAlignment(Pos.CENTER); // Cen
        buttonPlayer3.setPrefWidth(200); // Imposta la larghezza preferita del Button
        buttonPlayer3.setAlignment(Pos.CENTER); // Cen
        buttonPlayer4.setPrefWidth(200); // Imposta la larghezza preferita del Button
        buttonPlayer4.setAlignment(Pos.CENTER);




    }
    public void update()
    {

    }

}
