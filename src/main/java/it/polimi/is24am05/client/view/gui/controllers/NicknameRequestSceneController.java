package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Nickname scene Controller
 */
public class NicknameRequestSceneController implements Initializable {

    @FXML
    private TextField playerNickname;

    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox;
    @FXML
    private HBox hBox2;
    @FXML
    private HBox nickBox;
    @FXML
    private HBox numBox;
    @FXML
    private ComboBox<Integer> numPlays;
    private GUIRoot gui;
    @FXML
    private Button confirButton;

    @FXML
    private Button confirmNumOfPlayers;
    @FXML
    private Button logout;
    @FXML
    private Label logField;
    @FXML
    private Label nickname;

    @FXML
    private Label numPlayers;
    @FXML
    private BorderPane borderPane;


    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the scene placing the Labels, the TextField and the ComboBox
     * @param url
     * @param resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {

        logField.setText("");
        logField.setPrefWidth(700);
        playerNickname.setPromptText("Enter your nickname");

        numPlays.setPrefWidth(200);
        numPlays.getItems().addAll(2, 3, 4);

        playerNickname.setPrefWidth(200);

        hBox = new HBox(100);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(logField);

        hBox1 = new HBox(100);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(nickname, playerNickname);

        nickBox = new HBox(100);
        nickBox.setAlignment(Pos.CENTER);
        nickBox.getChildren().add(confirButton);

        hBox2 = new HBox(20);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(numPlayers, numPlays);

        numBox = new HBox(100);
        numBox.setAlignment(Pos.CENTER);
        numBox.getChildren().add(confirmNumOfPlayers);


        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(-200, 0, 0, 0));
        vBox.getChildren().addAll(hBox,hBox1, nickBox, hBox2, numBox, logout);
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
        logout.setVisible(false);


    }

    public String getPlayerNickname() {
        return playerNickname.getText();
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

    /**
     * Sets chosen nickname
     * @param event Button pressed
     */
    @FXML
    public void onButtonClicked(javafx.event.ActionEvent event) {
        logout.setVisible(true);
        String nickname = getPlayerNickname();
        gui.nicknameChosen(nickname);
    }
    public void logout (javafx.event.ActionEvent event) throws IOException {
        gui.logout();
    }
    /**
     * Sets chosen number of players
     * @param event Button pressed
     */
    @FXML
    public void numOfPlayersConfirmed(javafx.event.ActionEvent event) {
        int num = getNumPlayers();
        gui.numberOfplayersChosen(num);
    }

    public int getNumPlayers(){
        return  numPlays.getValue();
    }
}




