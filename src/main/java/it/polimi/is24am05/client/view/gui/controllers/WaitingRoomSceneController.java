package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;


public class WaitingRoomSceneController implements Initializable {



    private GUIRoot gui;

    @FXML
    VBox playersList;
    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }


  public void showPlayers(Set<String> nicknames)
  {
      for(String nickname: nicknames)
      {
          playersList.getChildren().add(new Label(nickname));
      }

  }


    @Override
    public void initialize (URL url, ResourceBundle resources) {


    }

    public void changeScene()
    {
        System.out.println("i switching");
       gui.gameCreated();
    }





}




