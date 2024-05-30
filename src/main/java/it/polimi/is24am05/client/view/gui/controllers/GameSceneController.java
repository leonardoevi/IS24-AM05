package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.EmptyPlacedSide;
import it.polimi.is24am05.model.card.side.PlacedSide;

import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.Tuple;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static it.polimi.is24am05.model.enums.element.Resource.*;

public class GameSceneController implements Initializable {

    //TODO AGGIORNATE LA MAPPPAAA

    @FXML
    private Pane playArea;
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

    private Label myPoints;

    @FXML

    private Label logField;

    @FXML

    private TextField rowPlacer;
    @FXML

    private TextField columnPlacer;


    private String clientNickname;


    private Map<ImageView, String> handViewMap = new HashMap<>();


    private Map<ImageView, String> visibleViewMap = new HashMap<>();


    @FXML
    private Label labelMyObjective;


    @FXML
    private Label labelCommonObjectives;


    @FXML
    private StackPane leftSideBackground;
    @FXML
    private AnchorPane mainBackground;
    private GUIRoot gui;

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    public void setClientNickname(String nickname) {
        this.clientNickname = nickname;

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
        AnchorPane.setBottomAnchor(backgroundPlayArea, 50.0);
        AnchorPane.setRightAnchor(backgroundPlayArea, 300.0);


        AnchorPane.setTopAnchor(labelCommonObjectives, 100.0);
        AnchorPane.setRightAnchor(labelCommonObjectives, 90.0);

        AnchorPane.setTopAnchor(labelMyObjective, 250.0);
        AnchorPane.setRightAnchor(labelMyObjective, 90.0);


        Image backgroundImage = new Image(imageBackPath);


        BackgroundImage backgroundImg = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );


        backgroundPlayArea.setBackground(new Background(backgroundImg));


        AnchorPane.setTopAnchor(logField, 10.0);
        AnchorPane.setLeftAnchor(logField, 600.0);
        logField.setText("");

        resourceDeckTop.setPreserveRatio(false);
        resourceDeckTop.setFitWidth(120);
        resourceDeckTop.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceDeckTop, 130.0);
        AnchorPane.setLeftAnchor(resourceDeckTop, 20.0);


        resourceVisible1.setFitWidth(120);
        resourceVisible1.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceVisible1, 70.0);
        AnchorPane.setLeftAnchor(resourceVisible1, 150.0);


        resourceVisible2.setFitWidth(120);
        resourceVisible2.setFitHeight(90);
        AnchorPane.setTopAnchor(resourceVisible2, 200.0);
        AnchorPane.setLeftAnchor(resourceVisible2, 150.0);

        goldDeckTop.setPreserveRatio(false);
        goldDeckTop.setFitWidth(120);
        goldDeckTop.setFitHeight(90);
        AnchorPane.setTopAnchor(goldDeckTop, 370.0);
        AnchorPane.setLeftAnchor(goldDeckTop, 20.0);


        goldVisible1.setFitWidth(120);
        goldVisible1.setFitHeight(90);
        AnchorPane.setTopAnchor(goldVisible1, 340.0);
        AnchorPane.setLeftAnchor(goldVisible1, 150.0);


        goldVisible2.setFitWidth(120);
        goldVisible2.setFitHeight(90);
        AnchorPane.setTopAnchor(goldVisible2, 450.0);
        AnchorPane.setLeftAnchor(goldVisible2, 150.0);


        handBackSide1.setFitWidth(120);
        handBackSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide1, 50.0);
        AnchorPane.setLeftAnchor(handBackSide1, 20.0);


        handFrontSide1.setFitWidth(120);
        handFrontSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide1, 50.0);
        AnchorPane.setLeftAnchor(handFrontSide1, 150.0);


        handBackSide2.setFitWidth(120);
        handBackSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide2, 150.0);
        AnchorPane.setLeftAnchor(handBackSide2, 20.0);


        handFrontSide2.setFitWidth(120);
        handFrontSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide2, 150.0);
        AnchorPane.setLeftAnchor(handFrontSide2, 150.0);


        handFrontSide3.setFitWidth(120);
        handFrontSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide3, 250.0);
        AnchorPane.setLeftAnchor(handFrontSide3, 20.0);


        handBackSide3.setFitWidth(120);
        handBackSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide3, 250.0);
        AnchorPane.setLeftAnchor(handBackSide3, 150.0);

        AnchorPane.setTopAnchor(myPoints, 30.0);
        AnchorPane.setRightAnchor(myPoints, 90.0);
        myPoints.setPrefWidth(200);
        myPoints.setAlignment(Pos.CENTER);
        myPoints.setText("");


        commonObjective1.setFitWidth(120);
        commonObjective1.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective1, 130.0);
        AnchorPane.setRightAnchor(commonObjective1, 20.0);


        commonObjective2.setFitWidth(120);
        commonObjective2.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective2, 130.0);
        AnchorPane.setRightAnchor(commonObjective2, 150.0);


        myObjective.setFitWidth(120);
        myObjective.setFitHeight(90);
        AnchorPane.setTopAnchor(myObjective, 280.0);
        AnchorPane.setRightAnchor(myObjective, 100.0);


        AnchorPane.setBottomAnchor(buttonPlayer1, 30.0);
        AnchorPane.setRightAnchor(buttonPlayer1, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer2, 70.0);
        AnchorPane.setRightAnchor(buttonPlayer2, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer3, 110.0);
        AnchorPane.setRightAnchor(buttonPlayer3, 20.0);

        buttonPlayer1.setPrefWidth(200);
        // Cen
        buttonPlayer2.setPrefWidth(200);

        buttonPlayer3.setPrefWidth(200);


        AnchorPane.setBottomAnchor(rowPlacer, 390.0);
        AnchorPane.setLeftAnchor(rowPlacer, 20.0);
        rowPlacer.setMaxWidth(50);


        AnchorPane.setBottomAnchor(columnPlacer, 390.0);
        AnchorPane.setLeftAnchor(columnPlacer, 70.0);
        columnPlacer.setMaxWidth(50);

        for (int i = 0; i < 10; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 10);
            //playArea.getRowConstraints().add(rowConstraints);
        }

        for (int j = 0; j < 10; j++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / 10);
            //playArea.getColumnConstraints().add(columnConstraints);
        }

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

    public void update(Game game) {

        handViewMap.clear();
        handFrontSide1.setImage(null);
        handBackSide1.setImage(null);
        handFrontSide2.setImage(null);
        handBackSide2.setImage(null);
        handFrontSide3.setImage(null);
        handBackSide3.setImage(null);
        goldDeckTop.setImage(null);
        resourceDeckTop.setImage(null);
        resourceVisible1.setImage(null);
        resourceVisible2.setImage(null);
        goldVisible1.setImage(null);
        goldVisible2.setImage(null);
        playArea.getChildren().removeIf(node -> node instanceof StackPane || node instanceof Label);

        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(clientNickname)) {

                myPoints.setText("POINTS: " + p.getPoints());

            }
        }

        try {
            Resource resourceTop = FUNGI;

            if (game.getResourceDeck() != null && game.getResourceDeck().peek() instanceof Resource) {
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

            if (game.getGoldDeck() != null && game.getGoldDeck().peek() instanceof Resource) {
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

        int idCard;
        String path = "";
        if (game.getResourceDeck() != null && game.getResourceDeck().getVisible() != null && !game.getResourceDeck().getVisible().isEmpty()) {
            List<Card> resourceVisible = game.getResourceDeck().getVisible().stream().toList();
            idCard = resourceVisible.get(0).getId();

            path = "/assets/images/front/";
            if (idCard >= 10) {
                path += "0";

            } else if (idCard < 10) {
                path += "00";

            }
            path += idCard;
            path += ".png";
            visibleViewMap.put(resourceVisible1, path);
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
            visibleViewMap.put(resourceVisible2, path);
            path = getClass().getResource(path).toExternalForm();
            resourceVisible2.setImage(new Image(path));
        }
        if (game.getGoldDeck() != null && game.getGoldDeck().getVisible() != null && !game.getGoldDeck().getVisible().isEmpty()) {
            List<Card> goldVisible = new HashSet<>(game.getGoldDeck().getVisible()).stream().toList();
            idCard = goldVisible.get(0).getId();

            path = "/assets/images/front/";

            path += "0";
            path += idCard;
            path += ".png";
            visibleViewMap.put(goldVisible1, path);
            path = getClass().getResource(path).toExternalForm();
            goldVisible1.setImage(new Image(path));


            idCard = goldVisible.get(1).getId();

            path = "/assets/images/front/";

            path += "0";
            path += idCard;
            path += ".png";
            visibleViewMap.put(goldVisible2, path);
            path = getClass().getResource(path).toExternalForm();
            goldVisible2.setImage(new Image(path));
        }
        if (game.getSharedObjectives() != null) {
            if (game.getSharedObjectives()[0] != null) {
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
            }
        }

        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(clientNickname)) {
                path = "/assets/images/front/";
                if (p.getObjective() != null) {
                    path += p.getObjective().name().substring(2);
                    path += ".png";

                    path = getClass().getResource(path).toExternalForm();
                    myObjective.setImage(new Image(path));
                }
                String appendfront = "/assets/images/front/";
                String appendback = "/assets/images/back/";
                List<Card> cards = p.getHand();
                String pathcardfront;
                String pathcardback;
                if (p.getHand() != null && !p.getHand().isEmpty()) {
                    if (!cards.isEmpty() && cards.get(0) != null) {
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
                        handViewMap.put(handFrontSide1, pathcardfront);


                        pathcardback = appendback + idCard + ".png";

                        path = getClass().getResource(pathcardback).toExternalForm();
                        handBackSide1.setImage(new Image(path));
                        handViewMap.put(handBackSide1, pathcardback);
                        appendfront = "/assets/images/front/";
                        appendback = "/assets/images/back/";
                    }

                    if (cards.size() > 1 && cards.get(1) != null) {
                        Card c = cards.get(1);
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
                        handViewMap.put(handFrontSide2, pathcardfront);


                        pathcardback = appendback + idCard + ".png";

                        path = getClass().getResource(pathcardback).toExternalForm();
                        handBackSide2.setImage(new Image(path));
                        handViewMap.put(handBackSide2, pathcardback);

                        appendfront = "/assets/images/front/";
                        appendback = "/assets/images/back/";
                    }


                    if (cards.size() > 2 && cards.get(2) != null) {
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
                        handViewMap.put(handFrontSide3, pathcardfront);

                        pathcardback = appendback + idCard + ".png";

                        path = getClass().getResource(pathcardback).toExternalForm();
                        handBackSide3.setImage(new Image(path));
                        handViewMap.put(handBackSide3, pathcardback);
                    }
                }
                break;
            }


        }

        System.out.println("Displaying playarea");
        for (Player p : game.getPlayers()) {

            if (p.getNickname().equals(clientNickname)) {
                PlacedSide[][] placedSides = p.getPlayArea().getMatrixPlayArea();

                StackPane.setMargin(playArea, new Insets(50));

                int id;
                System.out.println("row" + placedSides.length);
                System.out.println("column" + placedSides[0].length);

                for (PlacedSide placedSide : p.getPlayArea().getOrderedPlacements()) {
                    if (placedSide.getSide() != null) {

                        path = "/assets/images/";
                        String cardId = placedSide.getSide().toString();

                        if (cardId.charAt(1) == 'B')
                            path += "back/";
                        else
                            path += "front/";

                        path += cardId.substring(4, 7);
                        path += ".png";
                        System.out.println(path);
                        String imagePath2 = getClass().getResource(path).toExternalForm();


                        StackPane region = new StackPane();
                        ImageView imageView = new ImageView(new Image(imagePath2));
                        imageView.setFitWidth(81);
                        imageView.setFitHeight(54);
                        imageView.setPreserveRatio(true);
                        region.getChildren().add(imageView);
                        Tuple coord = getCard(placedSides, placedSide);
                        double x = (coord.j) * (60);
                        double y = (coord.i) * (30);

                        region.setLayoutX(x);
                        region.setLayoutY(y);
                        playArea.getChildren().add(region);


                    }
                }

                for (int row = 0; row < placedSides.length; row++) {
                    for (int col = 0; col < placedSides[0].length; col++) {

                          if (placedSides[row][col] != null && placedSides[row][col] instanceof EmptyPlacedSide) {
                              Tuple coord = placedSides[row][col].getActualCoord();
                            Label coordinates = new Label("("+ coord.i + ","+coord.j+")");
                            coordinates.setPrefWidth(81);
                            coordinates.setPrefHeight(54);
                            double x = (row) * (68);
                            double y =(0.9*col) * (35);

                            coordinates.setLayoutX(x);
                            coordinates.setLayoutY(y);
                            playArea.getChildren().add(coordinates);
                        }
                    }
                }
            }


        }

        List<String> nicknames = game.getPlayers().stream().map(p -> p.getNickname()).filter(n -> !n.equals(clientNickname)).toList();
        buttonPlayer1.setText("View " + nicknames.get(0));
        buttonPlayer1.setOnAction(event -> {
            try {
                gui.viewOtherPlayer(nicknames.get(0), game);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if (nicknames.size() > 2) {
            buttonPlayer2.setText("View" + nicknames.get(1));
            buttonPlayer2.setOnAction(event -> {
                try {
                    gui.viewOtherPlayer(nicknames.get(1), game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } else {
            buttonPlayer2.setVisible(false);
        }
        if (nicknames.size() > 3) {
            buttonPlayer3.setText("View " + nicknames.get(2));
            buttonPlayer3.setOnAction(event -> {
                try {
                    gui.viewOtherPlayer(nicknames.get(1), game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else
            buttonPlayer3.setVisible(false);


    }

    @FXML
    public void onHandCardTouch(MouseEvent event) {

        ImageView source = (ImageView) event.getSource();


        rowPlacer.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                checkFields(source);

            }
        });


        columnPlacer.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                checkFields(source);
            }
        });


    }

    private void checkFields(ImageView source) {
        if (rowPlacer.getText() != null && rowPlacer.getText() != "" && columnPlacer.getText() != null && columnPlacer.getText() != "") {

            int row = (Integer.parseInt(rowPlacer.getText()));
            int column = (Integer.parseInt(columnPlacer.getText()));

            rowPlacer.setText("");
            columnPlacer.setText("");

            if (handViewMap.containsKey(source)) {

                boolean isFront = handViewMap.get(source).contains("/front");
                String cardId = "";
                if (isFront)
                    cardId += handViewMap.get(source).substring(21, 24);
                else
                    cardId += handViewMap.get(source).substring(20, 23);
                String CardServerId = "";
                if (Integer.parseInt(cardId) > 40 && Integer.parseInt(cardId) <= 80)
                    CardServerId = "GC_";
                else
                    CardServerId = "RC_";
                CardServerId += cardId;

                System.out.println("trying to place" + CardServerId);

                gui.placeCard(CardServerId, isFront, row, column);
            } else {
                System.out.println("there is no card");
            }


        }
    }

    @FXML
    public void drawResourceDeck(MouseEvent event) {
        gui.drawDeck(false);

    }

    @FXML
    public void drawGoldDeck(MouseEvent event) {
        gui.drawDeck(true);

    }

    @FXML
    public void drawVisible(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();


        boolean isFront = visibleViewMap.get(source).contains("/front");
        String cardId = "";
        String CardServerId = "";
        if (isFront)
            cardId += visibleViewMap.get(source).substring(21, 24);
        else
            cardId += visibleViewMap.get(source).substring(20, 23);

        if (Integer.parseInt(cardId) > 40 && Integer.parseInt(cardId) <= 80)
            CardServerId = "GC_";
        else
            CardServerId = "RC_";
        CardServerId += cardId;

        System.out.println(CardServerId);

        gui.drawVisible(CardServerId);


    }
    public Tuple getCard(PlacedSide[][] placedSides, PlacedSide toFind){
        Tuple found = null;
        for (int i = 0; i < placedSides.length; i++) {
            for (int j = 0; j < placedSides[0].length; j++) {
                if (placedSides[i][j] != null) {
                    if (placedSides[i][j].equals(toFind))
                        found = new Tuple(i, j);
                }
            }

        }
        return found;
    }
}





