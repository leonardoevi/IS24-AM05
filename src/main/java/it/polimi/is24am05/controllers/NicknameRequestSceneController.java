package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUI;
import it.polimi.is24am05.GUIMain;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NicknameRequestSceneController implements Initializable {

    @FXML
    private TextField playerNickname;

    private GUI gui;
    @FXML
    private Button confirButton;



    public void setGUI(GUI gui)
    {
        this.gui=gui;
    }

    @Override
    public void initialize (URL url, ResourceBundle resources) {


        playerNickname.setPromptText("enter your nickname");

    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    /**
     * Fills the player nickname text field with an empty string.
     */
    public void resetPlayerNickname () {
        playerNickname.setText("");
    }

    @FXML
    public void onButtonClicked() {
        String nickname = getPlayerNickname();
        System.out.println(nickname);
        gui.switchToGame();

        }
    }




