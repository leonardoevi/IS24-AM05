package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorSceneController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;
    @FXML
    private Label errorLabel;
    private GUIRoot gui;

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setPrefWidth(400);
        errorLabel.setText("SERVER IS DOWN");
        hBox = new HBox(100);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(errorLabel);

        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(-200, 0, 0, 0));
        vBox.getChildren().addAll(hBox);
        vBox.setSpacing(30);

        String imageBackPath = getClass().getResource("/assets/images/playAreaBackground.png").toExternalForm();
        Image backgroundImage = new Image(imageBackPath);
        borderPane.setBackground(new Background(new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false))));

        borderPane.setCenter(vBox);
    }
}
