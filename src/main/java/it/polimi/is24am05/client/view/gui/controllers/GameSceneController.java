package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.EmptyPlacedSide;
import it.polimi.is24am05.model.card.side.PlacedSide;

import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;


import java.net.URL;
import java.util.*;

import static it.polimi.is24am05.model.enums.element.Resource.*;

public class GameSceneController  implements Initializable  {

    //TODO AGGIORNATE LA MAPPPAAA

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
    private ImageView handBackSide1;
    @FXML
    private ImageView handFrontSide1;
    @FXML
    private ImageView handBackSide2;
    @FXML
    private ImageView handFrontSide2;
    @FXML
    private ImageView handBackSide3;
    @FXML
    private ImageView handFrontSide3;

    @FXML
    private ImageView commonObjective1;
    @FXML
    private ImageView commonObjective2;
    @FXML


    private ImageView myObjective;
    @FXML

    private Button buttonPlayer1;
    @FXML

    private Button buttonPlayer2;
    @FXML

    private Button buttonPlayer3;
    @FXML

    private Button buttonPlayer4;

    @FXML

    private Label myPoints;

    @FXML

    private Label logField;

    @FXML

    private TextField rowPlacer;
    @FXML

    private TextField columnPlacer;


    private String clientNickname;

    private boolean isTextField1Filled = false;
    private boolean isTextField2Filled = false;

    private Map<ImageView, String> imageViewMap=new HashMap<>();


    @FXML
    private Label labelMyObjective;


    @FXML
    private Label labelCommonObjectives;


    @FXML
    private StackPane leftSideBackground;
    @FXML
    private AnchorPane mainBackground;
    private GUIRoot gui;
    public void setGUI(GUIRoot gui)
    {
        this.gui=gui;
    }

    public void setClientNickname(String nickname){
        this.clientNickname=nickname;

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
        AnchorPane.setLeftAnchor(backgroundPlayArea, 300.0);

        backgroundPlayArea.setPrefHeight(900);
        backgroundPlayArea.setPrefWidth(900);




        AnchorPane.setTopAnchor(labelCommonObjectives, 100.0);
        AnchorPane.setRightAnchor(labelCommonObjectives, 90.0);

        AnchorPane.setTopAnchor(labelMyObjective, 250.0);
        AnchorPane.setRightAnchor(labelMyObjective, 90.0);






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



        /*
        for (int i = 0; i < 10; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100 / 10.0);
            playArea.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100 / 10.0);
            playArea.getRowConstraints().add(rowConstraints);
        }
        StackPane.setMargin(playArea, new Insets(50));
        playArea.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for (int i = 0; i < 10; i++) {
                playArea.getColumnConstraints().get(i).setPrefWidth(newValue.doubleValue() / 10);
            }
        });

        playArea.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for (int i = 0; i < 10; i++) {
                playArea.getRowConstraints().get(i).setPrefHeight(newValue.doubleValue() / 10);
            }
        });

         */
        /*
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {

                if((row+col)%2==0) {

                    ImageView card = new ImageView(new Image(imagePath));

                    String imagePath2 = getClass().getResource("/assets/images/front/027.png").toExternalForm();
                    StackPane region = new StackPane();
                    region.setBackground(new Background(new BackgroundImage(new Image(imagePath2),  BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(1.0, 1.0, true, true, false, false))));
                    playArea.add(region, col, row);

                }
            }
        }

         */
        AnchorPane.setTopAnchor(logField, 10.0);
        AnchorPane.setLeftAnchor(logField, 600.0);
        logField.setText("");

        resourceDeckTop.setPreserveRatio(false);
      // resourceDeckTop.setImage(new Image(imageBackPath));
        resourceDeckTop.setFitWidth(120);
        resourceDeckTop.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceDeckTop, 130.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(resourceDeckTop, 20.0);

       // resourceVisible1.setImage(new Image(imageBackPath));
        resourceVisible1.setFitWidth(120);
        resourceVisible1.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceVisible1, 70.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(resourceVisible1, 150.0);

      //  resourceVisible2.setImage(new Image(imageBackPath));
        resourceVisible2.setFitWidth(120);
        resourceVisible2.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceVisible2, 200.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(resourceVisible2, 150.0);

        goldDeckTop.setPreserveRatio(false);
      //  goldDeckTop.setImage(new Image(imageBackPath));
        goldDeckTop.setFitWidth(120);
        goldDeckTop.setFitHeight(90);
        AnchorPane.setTopAnchor(goldDeckTop, 370.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(goldDeckTop, 20.0);

       // goldVisible1.setImage(new Image(imageBackPath));
        goldVisible1.setFitWidth(120);
        goldVisible1.setFitHeight(90);
        AnchorPane.setTopAnchor(goldVisible1, 340.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(goldVisible1, 150.0);


    //    goldVisible2.setImage(new Image(imageBackPath));
        goldVisible2.setFitWidth(120);
        goldVisible2.setFitHeight(90);
        AnchorPane.setTopAnchor(goldVisible2, 450.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(goldVisible2, 150.0);


    //    handBackSide1.setImage(new Image(imageBackPath));
        handBackSide1.setFitWidth(120);
        handBackSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide1, 50.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handBackSide1, 20.0);


     //   handFrontSide1.setImage(new Image(imageBackPath));
        handFrontSide1.setFitWidth(120);
        handFrontSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide1, 50.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handFrontSide1, 150.0);

    //    handBackSide2.setImage(new Image(imageBackPath));
        handBackSide2.setFitWidth(120);
        handBackSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide2, 150.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handBackSide2, 20.0);

    //    handFrontSide2.setImage(new Image(imageBackPath));
        handFrontSide2.setFitWidth(120);
        handFrontSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide2, 150.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handFrontSide2, 150.0);

     //   handFrontSide3.setImage(new Image(imageBackPath));
        handFrontSide3.setFitWidth(120);
        handFrontSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide3, 250.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handFrontSide3, 20.0);

   //     handBackSide3.setImage(new Image(imageBackPath));
        handBackSide3.setFitWidth(120);
        handBackSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide3, 250.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(handBackSide3, 150.0);

        AnchorPane.setTopAnchor(myPoints, 30.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(myPoints, 90.0);
        myPoints.setPrefWidth(200); // Imposta la larghezza preferita del Label
        myPoints.setAlignment(Pos.CENTER);
        myPoints.setText("");

        // commonObjective1.setImage(new Image(imageBackPath));
        commonObjective1.setFitWidth(120);
        commonObjective1.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective1, 130.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(commonObjective1, 20.0);

      //  commonObjective2.setImage(new Image(imageBackPath));
        commonObjective2.setFitWidth(120);
        commonObjective2.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective2, 130.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(commonObjective2, 150.0);


     //   myObjective.setImage(new Image(imageBackPath));
        myObjective.setFitWidth(120);
        myObjective.setFitHeight(90);
        AnchorPane.setTopAnchor(myObjective, 280.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(myObjective, 100.0);




        AnchorPane.setBottomAnchor(buttonPlayer1, 30.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer1, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer2, 70.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer2, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer3, 110.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer3, 20.0);
        AnchorPane.setBottomAnchor(buttonPlayer4, 140.0); // Distanza dal bordo superiore
        AnchorPane.setRightAnchor(buttonPlayer4, 20.0);
        buttonPlayer1.setPrefWidth(200); // Imposta la larghezza preferita del Button
       // Cen
        buttonPlayer2.setPrefWidth(200); // Imposta la larghezza preferita del Button

        buttonPlayer3.setPrefWidth(200); // Imposta la larghezza preferita del Button

        buttonPlayer4.setPrefWidth(200); // Imposta la larghezza preferita del Button


        AnchorPane.setBottomAnchor(rowPlacer, 390.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(rowPlacer, 20.0);
        rowPlacer.setMaxWidth(50);





        AnchorPane.setBottomAnchor(columnPlacer, 390.0); // Distanza dal bordo superiore
        AnchorPane.setLeftAnchor(columnPlacer, 70.0);
        columnPlacer.setMaxWidth(50);

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
    public void update(Game game) {

        try {
            Resource resourceTop = FUNGI;

            if (game.getResourceDeck().peek() instanceof Resource) {
                resourceTop = (Resource) game.getResourceDeck().peek();
            }
            String sidePath = "/assets/images/back/";
            switch (resourceTop) {
                case FUNGI: {
                    sidePath += "001.png";
                    break;
                }
                case PLANT: {
                    sidePath += "011.png";
                    break;
                }
                case ANIMAL: {
                    sidePath += "021.png";
                    break;
                }
                case INSECT: {
                    sidePath += "031.png";
                    break;
                }

                default:
                    break;
            }

            sidePath = getClass().getResource(sidePath).toExternalForm();
            resourceDeckTop.setImage(new Image(sidePath));
        } catch (EmptyDeckException e) {
            // logField.setText("resource deck empty");
        }

        try {
            Resource goldTop = FUNGI;

            if (game.getGoldDeck().peek() instanceof Resource) {
                goldTop = (Resource) game.getGoldDeck().peek();
            }

            String sidePath = "/assets/images/back/";
            switch (goldTop) {
                case FUNGI: {
                    sidePath += "041.png";
                    break;
                }
                case PLANT: {
                    sidePath += "051.png";
                    break;
                }
                case ANIMAL: {
                    sidePath += "061.png";
                    break;
                }
                case INSECT: {
                    sidePath += "071.png";
                    break;
                }

                default:
                    break;
            }
            sidePath = getClass().getResource(sidePath).toExternalForm();
            goldDeckTop.setImage(new Image(sidePath));
        } catch (EmptyDeckException e) {
            // logField.setText("resource deck empty");
        }

        List<Card> resourceVisible = game.getResourceDeck().getVisible().stream().toList();
        int idCard = resourceVisible.get(0).getId();

        String path = "/assets/images/front/";
        if (idCard >= 10) {
            path += "0";

        } else if (idCard < 10) {
            path += "00";

        }
        path += idCard;
        path += ".png";

        path = getClass().getResource(path).toExternalForm();
        resourceVisible1.setImage(new Image(path));


        idCard = resourceVisible.get(1).getId();

        path = "/assets/images/front/";
        if (idCard >= 10) {
            path += "0";

        } else if (idCard < 10) {
            path += "00";

        }
        path += idCard;
        path += ".png";
        path = getClass().getResource(path).toExternalForm();
        resourceVisible2.setImage(new Image(path));


        List<Card> goldVisible = new HashSet<>(game.getGoldDeck().getVisible()).stream().toList();
        idCard = goldVisible.get(0).getId();

        path = "/assets/images/front/";

        path += "0";
        path += idCard;
        path += ".png";
        path = getClass().getResource(path).toExternalForm();
        goldVisible1.setImage(new Image(path));


        idCard = goldVisible.get(1).getId();

        path = "/assets/images/front/";

        path += "0";
        path += idCard;
        path += ".png";
        path = getClass().getResource(path).toExternalForm();
        goldVisible2.setImage(new Image(path));


        path = "/assets/images/front/";
        path += game.getSharedObjectives()[0].name().substring(2);
        path += ".png";
        path = getClass().getResource(path).toExternalForm();
        commonObjective1.setImage(new Image(path));

        path = "/assets/images/front/";
        path += game.getSharedObjectives()[1].name().substring(2);
        path += ".png";
        path = getClass().getResource(path).toExternalForm();
        commonObjective2.setImage(new Image(path));

        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(clientNickname)) {
                path = "/assets/images/front/";
                path += p.getObjective().name().substring(2);
                path += ".png";

                path = getClass().getResource(path).toExternalForm();
                myObjective.setImage(new Image(path));

                String appendfront = "/assets/images/front/";
                String appendback = "/assets/images/back/";

                List<Card> cards = p.getHand();
                String pathcardfront;
                String pathcardback;

                if(cards.size()>0 && cards.get(0)!=null) {
                    Card c = cards.get(0);
                    idCard = c.getId();
                    // TODO parsing function
                    if (idCard < 100 && idCard >= 10) {
                        appendback += "0";
                        appendfront += "0";
                    } else if (idCard < 10) {
                        appendback += "00";
                        appendfront += "00";
                    }


                    pathcardfront = appendfront + idCard + ".png";

                    path = getClass().getResource(pathcardfront).toExternalForm();
                    handFrontSide1.setImage(new Image(path));
                    imageViewMap.put(handFrontSide1, pathcardfront);


                    pathcardback = appendback + idCard + ".png";

                    path = getClass().getResource(pathcardback).toExternalForm();
                    handBackSide1.setImage(new Image(path));
                    imageViewMap.put(handBackSide1, pathcardback);
                    appendfront = "/assets/images/front/";
                    appendback = "/assets/images/back/";
                }

                if (cards.size()>1 && cards.get(1)!=null) {
                   Card  c = cards.get(1);
                    idCard = c.getId();

                    if (idCard < 100 && idCard >= 10) {
                        appendback += "0";
                        appendfront += "0";
                    } else if (idCard < 10) {
                        appendback += "00";
                        appendfront += "00";
                    }
                    pathcardfront = appendfront + idCard + ".png";

                    path = getClass().getResource(pathcardfront).toExternalForm();
                    handFrontSide2.setImage(new Image(path));
                    imageViewMap.put(handFrontSide2, pathcardfront);


                    pathcardback = appendback + idCard + ".png";

                    path = getClass().getResource(pathcardback).toExternalForm();
                    handBackSide2.setImage(new Image(path));
                    imageViewMap.put(handBackSide2, pathcardback);

                    appendfront = "/assets/images/front/";
                    appendback = "/assets/images/back/";
                }


                if (cards.size()>2 && cards.get(2)!=null) {
                    Card c = cards.get(2);
                    idCard = c.getId();

                    if (idCard < 100 && idCard >= 10) {
                        appendback += "0";
                        appendfront += "0";
                    } else if (idCard < 10) {
                        appendback += "00";
                        appendfront += "00";
                    }

                    pathcardfront = appendfront + idCard + ".png";

                    path = getClass().getResource(pathcardfront).toExternalForm();
                    handFrontSide3.setImage(new Image(path));
                    imageViewMap.put(handFrontSide3, pathcardfront);

                    pathcardback = appendback + idCard + ".png";

                    path = getClass().getResource(pathcardback).toExternalForm();
                    handBackSide3.setImage(new Image(path));
                    imageViewMap.put(handBackSide3, pathcardback);

                    break;
                }

            }
        }

        System.out.println("Displaying playarea");
        for (Player p : game.getPlayers()) {

            if (p.getNickname().equals(clientNickname)) {
                PlacedSide[][] placedSides = p.getPlayArea().getMatrixPlayArea();

                for (int i = 0; i < placedSides.length; i++) {
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setPercentHeight(100 / placedSides.length);
                    playArea.getRowConstraints().add(rowConstraints);
                }
                for (int i = 0; i < placedSides[0].length; i++) {
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setPercentWidth(100 / placedSides[0].length);
                    playArea.getColumnConstraints().add(columnConstraints);
                }

                StackPane.setMargin(playArea, new Insets(50));
                /*
                playArea.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    for (int i = 0; i < 10; i++) {
                        playArea.getColumnConstraints().get(i).setPrefWidth(newValue.doubleValue() / 10);
                    }
                });

                playArea.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    for (int i = 0; i < 10; i++) {
                        playArea.getRowConstraints().get(i).setPrefHeight(newValue.doubleValue() / 10);
                    }
                });

                 */
                int id;
                System.out.println("row" + placedSides.length);
                System.out.println("column" + placedSides[0].length);


                for (int row = 0; row < placedSides.length  ; row++) {
                    for (int col = 0; col < placedSides[0].length; col++) {

                        if ( (row + col) % 2 == 0) {

                            if (placedSides[row][col] != null && !(placedSides[row][col] instanceof EmptyPlacedSide)) {

                                path = "/assets/images/";

                                String cardId = placedSides[row][col].getSide().toString();

                                if (cardId.charAt(1) == 'B')
                                    path += "back/";
                                else
                                    path += "front/";

                                path += cardId.substring(4, 7);
                                path+=".png";
                                System.out.println(path);
                                String imagePath2 = getClass().getResource(path).toExternalForm();

                                StackPane region = new StackPane();
                                region.setBackground(new Background(new BackgroundImage(new Image(imagePath2), BackgroundRepeat.NO_REPEAT,
                                        BackgroundRepeat.NO_REPEAT,
                                        BackgroundPosition.CENTER,
                                        new BackgroundSize(1.0, 1.0, true, true, false, false))));
                                playArea.add(region, col, row);

                            }
                            else if(placedSides[row][col]!=null &&  placedSides[row][col] instanceof EmptyPlacedSide)
                            {
                                StackPane region = new StackPane();
                                int rowTrans=row-2;
                                int columnTrans=col-2;
                                 region.getChildren().clear();
                                region.getChildren().add(new Label("(" + placedSides[row][col].getActualCoord().i + " , " + placedSides[row][col].getActualCoord().j +")"));
                                playArea.add(region, col, row);

                            }
                        }
                    }
                }
                break;

            }
        }
    }
    @FXML
    public void onHandCardTouch(MouseEvent event)
    {

        ImageView source = (ImageView) event.getSource();

        /*
        rowPlacer.textProperty().addListener((observable, oldValue, newValue) -> {
            isTextField1Filled = !newValue.trim().isEmpty();
            checkFields(source);
        });
        columnPlacer.textProperty().addListener((observable, oldValue, newValue) -> {
            isTextField2Filled = !newValue.trim().isEmpty();
            checkFields(source);
        });
*/



    }

    private void checkFields(ImageView source) {
        if (isTextField1Filled && isTextField2Filled) {

            int row =(Integer.parseInt(rowPlacer.getText()));
            int column=(Integer.parseInt(columnPlacer.getText()));

            boolean isFront = imageViewMap.get(source).contains("/front");
            String cardId = "";
            if (isFront)
                cardId+= imageViewMap.get(source).substring(21, 24);
            else
                cardId+= imageViewMap.get(source).substring(20, 23);
            String CardServerId = "";
            if (Integer.parseInt(cardId)>= 40 && Integer.parseInt(cardId)<80)
                CardServerId = "GC_";
            else
                CardServerId = "RC_";
            CardServerId+= cardId;

           // System.out.println(CardServerId);

            gui.placeCard(CardServerId, isFront, row, column);

            /*

            for (var node : playArea.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer colIndex = GridPane.getColumnIndex(node);
                if (rowIndex != null && rowIndex == row && colIndex != null && colIndex == column) {
                    Region region= (Region) node;

                    region.setBackground(new Background(new BackgroundImage(source.getImage(), BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(1.0, 1.0, true, true, false, false))));
                }
            // Entrambi i campi sono completi, esegui l'azione desiderata

        }

             */





    }
    }
}





