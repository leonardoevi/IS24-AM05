package it.polimi.is24am05.controllers;

import it.polimi.is24am05.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class WaitingRoomSceneController implements Initializable {



    private GUI gui;
    public void setGUI(GUI gui)
    {
        this.gui=gui;
    }





    @Override
    public void initialize (URL url, ResourceBundle resources) {


    }

    public void changeScene()
    {
        System.out.println("i switching");
       gui.switchToGame();
    }





}




