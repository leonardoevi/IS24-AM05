package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUIRoot;
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

    private GUIRoot gui;
    @FXML
    private Button confirButton;
    @FXML
    private Label logField;
    @FXML
    private Label questionLabel;


    boolean askNumOfPlayers=false;




    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }

    @Override
    public void initialize (URL url, ResourceBundle resources) {
        questionLabel.setText("Nickname");
        logField.setText("");
        playerNickname.setPromptText("enter your nickname");

    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    public int getNumPlayers(){return Integer.parseInt(playerNickname.getText().toString()); }

    /**
     * Fills the player nickname text field with an empty string.
     */
    public void resetPlayerNickname () {
        playerNickname.setText("");
    }

    public void showLog(String log)
    {
        logField.setText(log);
        if(log.equals("Nickname non valido"));
            resetPlayerNickname();
        if(log.equals("Devi inserire il numero dei giocatori"))
           {
               askNumOfPlayers=true;
               questionLabel.setText("Num of players");
               playerNickname.setText("");
               playerNickname.setPromptText("enter the number of players");
           }
        if(log.equals("numero di giocatori errato"))
        {
            askNumOfPlayers=true;
            questionLabel.setText("Num of players");
            playerNickname.setText("");
            playerNickname.setPromptText("enter the number of players");
        }
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event ->logField.setText("")
        ));
        timeline.setCycleCount(1);
        timeline.play();

    }

    @FXML
    public void onButtonClicked() {
        if(askNumOfPlayers==false)
        {
        String nickname = getPlayerNickname();

           gui.nicknameChosen(nickname);
        }

        else{

            int numPlayers=getNumPlayers();

            gui.numberOfplayersChosen(numPlayers);

        }



        //  gui.gameCreated();

        }
    }




