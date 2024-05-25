package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.client.model.ClientModel;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DealStarterCardsSceneController implements Initializable {
    @FXML
    private AnchorPane mainBackground;
    @FXML
    private ImageView mySCFrontSide;
    @FXML
    private ImageView mySCBackSide;
    @FXML
    private ImageView player1SCBackSide;
    @FXML
    private ImageView player1SCFrontSide;
    @FXML
    private ImageView player2SCBackSide;
    @FXML
    private ImageView player2SCFrontSide;
    @FXML
    private ImageView player3SCBackSide;
    @FXML
    private ImageView player3SCFrontSide;
    @FXML
    private Label logField;

    @FXML
    private Label playSC;

    private GUIRoot gui;

    private ClientModel client;
    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }

    public void setClientModel(ClientModel client)
    {
        this.client=client;
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
        playSC.setText("choose the side of the SC you want to play ");
        AnchorPane.setBottomAnchor(playSC, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(playSC, 600.0);

        mainBackground.setBackground(new Background(leftSideBack));
        mySCFrontSide.setPreserveRatio(false);
        AnchorPane.setBottomAnchor(mySCFrontSide, 400.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(mySCFrontSide, 600.0);
        mySCFrontSide.setFitWidth(120);
        mySCFrontSide.setFitHeight(90);


        mySCBackSide.setPreserveRatio(false);
        AnchorPane.setBottomAnchor(mySCBackSide, 400.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(mySCBackSide, 750.0);
        mySCBackSide.setFitWidth(120);
        mySCBackSide.setFitHeight(90);

        player1SCFrontSide.setPreserveRatio(false);
        AnchorPane.setTopAnchor(player1SCFrontSide, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(player1SCFrontSide, 30.0);
       player1SCFrontSide.setFitWidth(120);
        player1SCFrontSide.setFitHeight(90);

        player1SCBackSide.setPreserveRatio(false);
        AnchorPane.setTopAnchor(player1SCBackSide, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(player1SCBackSide, 180.0);
        player1SCBackSide.setFitWidth(120);
        player1SCBackSide.setFitHeight(90);

        player2SCFrontSide.setPreserveRatio(false);
        AnchorPane.setTopAnchor(player2SCFrontSide, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(player2SCFrontSide, 400.0);
        player2SCFrontSide.setFitWidth(120);
        player2SCFrontSide.setFitHeight(90);


        player2SCBackSide.setPreserveRatio(false);
        AnchorPane.setTopAnchor(player2SCBackSide, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(player2SCBackSide, 550.0);
        player2SCBackSide.setFitWidth(120);
        player2SCBackSide.setFitHeight(90);

        player3SCFrontSide.setPreserveRatio(false);
        AnchorPane.setTopAnchor(player3SCFrontSide, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(player3SCFrontSide, 770.0);
        player3SCFrontSide.setFitWidth(120);
        player3SCFrontSide.setFitHeight(90);


        player3SCBackSide.setPreserveRatio(false);
        AnchorPane.setTopAnchor(player3SCBackSide, 100.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(player3SCBackSide, 920.0);
        player3SCBackSide.setFitWidth(120);
        player3SCBackSide.setFitHeight(90);


        String imagePath = getClass().getResource("/assets/images/front/027.png").toExternalForm();
        player1SCBackSide.setImage(new Image(imagePath));


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

    //if the currentScene is DealStarterCardsSceneController-> update it

    public void update()

    {

            String imagePath = getClass().getResource("/assets/images/front/027.png").toExternalForm();
      //  player1SCBackSide.setImage(new Image(imagePath));
            /*
            player1SCBackSide.setImage(new Image(imagePath));
        player1SCFrontSide.setImage(new Image(imagePath));
        mySCFrontSide.setImage(new Image(imagePath));

        mySCBackSide.setImage(new Image(imagePath));
        player2SCBackSide.setImage(new Image(imagePath));
        player2SCFrontSide.setImage(new Image(imagePath));
        player3SCFrontSide.setImage(new Image(imagePath));
        player3SCFrontSide.setImage(new Image(imagePath));



         SC_081(StarterFrontSide.SFS_081, StarterBackSide.SBS_081),
    SC_082(StarterFrontSide.SFS_082, StarterBackSide.SBS_082),
    SC_083(StarterFrontSide.SFS_083, StarterBackSide.SBS_083),
    SC_084(StarterFrontSide.SFS_084, StarterBackSide.SBS_084),
    SC_085(StarterFrontSide.SFS_085, StarterBackSide.SBS_085),
    SC_086(StarterFrontSide.SFS_086, StarterBackSide.SBS_086);


            */
       int id= StarterCard.SC_081.getId();
       System.out.println(id);

        List<ImageView> listImageView=new ArrayList<>();
        listImageView.add(mySCFrontSide);
        listImageView.add(mySCBackSide);
        listImageView.add(player1SCFrontSide);
        listImageView.add(player1SCBackSide);
        listImageView.add(player2SCFrontSide);
        listImageView.add(player2SCBackSide);
        listImageView.add(player3SCFrontSide);
        listImageView.add(player3SCBackSide);

        for(Player p: client.getGame().get().getPlayers())
        {
            int idCard=p.getStarterCard().getId();
            String pathcardfront = "/assets/images/front/0" + id + ".png";
            String path = getClass().getResource(pathcardfront).toExternalForm();
            ImageView curr=listImageView.getFirst();
            curr.setImage(new Image(path));
            listImageView.removeFirst();

            String pathcardback = "/assets/images/back/0" + id + ".png";
            path = getClass().getResource(pathcardback).toExternalForm();
            curr=listImageView.getFirst();
            curr.setImage(new Image(path));
            listImageView.removeFirst();


        }





    }
}
