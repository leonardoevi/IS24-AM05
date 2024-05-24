package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class NicknameRequestSceneController implements Initializable {

    @FXML
    private TextField playerNickname;

    @FXML
    private TextField numOfPlayers;

    private GUIRoot gui;
    @FXML
    private Button confirButton;

    @FXML
    private Button confirmNumOfPlayers;
    @FXML
    private Label logField;



    boolean askNumOfPlayers=false;




    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }

    @Override
    public void initialize (URL url, ResourceBundle resources) {

        logField.setText("");
        playerNickname.setPromptText("enter your nickname");
        numOfPlayers.setPromptText("enter your nickname");

    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    public int getNumPlayers(){return Integer.parseInt(numOfPlayers.getText()); }

    /**
     * Fills the player nickname text field with an empty string.
     */
    public void resetPlayerNickname () {
        playerNickname.setText("");
    }
   public void resetNumOfPlayers()
   {
       numOfPlayers.setText("");
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

    @FXML
    public void onButtonClicked() {

        //for join server

            String nickname = getPlayerNickname();

            gui.nicknameChosen(nickname);

    }

        @FXML
        public void numOfPlayersConfirmed() {

            //for join server
            int numPlayers = getNumPlayers();

            gui.numberOfplayersChosen(numPlayers);


            //  gui.gameCreated();

        }
    }




