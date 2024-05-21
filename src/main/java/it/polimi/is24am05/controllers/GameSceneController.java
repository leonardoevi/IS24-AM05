package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
        AnchorPane.setBottomAnchor(backgroundPlayArea, 50.0);
        AnchorPane.setLeftAnchor(backgroundPlayArea, 300.0);
        backgroundPlayArea.setPrefSize(900, 900);
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
        StackPane.setMargin(playArea, new Insets(50));
        String imagePath = getClass().getResource("/assets/images/027.png").toExternalForm();
        for (int i = 0; i < 20; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100 / 20.0);
            playArea.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100 / 20.0);
            playArea.getRowConstraints().add(rowConstraints);
        }

        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                /*
                ImageView card = new ImageView(new Image(imagePath));
                Label label=new Label("diocane");
                card.setFitHeight(50); // Imposta l'altezza dell'immagine
                card.setFitWidth(90);  // Imposta la larghezza dell'immagine


                GridPane.setMargin(card, new Insets(0, 0, 0, 0)); // Sovrapposizione sugli angoli


                playArea.add(card, col, row);

                 */
                Label label = new Label("R" + row + ", C" + col);

                // Aggiungi la Label alla cella corrispondente nel GridPane
                playArea.add(label, col, row);
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










    }
    public void update()
    {

    }

}
