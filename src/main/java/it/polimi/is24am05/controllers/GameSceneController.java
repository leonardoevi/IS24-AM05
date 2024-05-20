package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController  implements Initializable  {

    @FXML
    private GridPane playArea;
    private GUI gui;
    public void setGUI(GUI gui)
    {
        this.gui=gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
    public void update()
    {
        String imagePath = getClass().getResource("/assets/images/027.png").toExternalForm();

        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                ImageView card = new ImageView(new Image(imagePath));
                Label label=new Label("diocane");
                card.setFitHeight(100); // Imposta l'altezza dell'immagine
               card.setFitWidth(150);  // Imposta la larghezza dell'immagine


                GridPane.setMargin(card, new Insets(-10, -10, 0, 0)); // Sovrapposizione sugli angoli


                playArea.add(card, col, row);
            }
        }
    }

}
