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

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static it.polimi.is24am05.model.enums.element.Resource.*;

/**
 * Game scene Controller
 */
public class GameSceneController implements Initializable {
    /**
     * Values needed in order to place cards one on top of the other snd cover only the corners
     */
    private double xcoord = 81;
    private double ycoord = 54;

    private double xshifht = 62;
    private double yshift = 30;
    private double fontSize = 14;
    @FXML
    private TextField chatMessage;
    @FXML
    private Button confirmMessage;
    @FXML
    private ComboBox<String> sender;
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
    private ImageView plateau;
    @FXML
    private Button logout;

    @FXML
    private Button buttonPlayer1;
    @FXML

    private Button buttonPlayer2;
    @FXML

    private Button buttonPlayer3;

    @FXML
    private Circle circle1;
    @FXML

    private Circle circle2;
    @FXML

    private Circle circle3;

    @FXML

    private Circle circle4;

    @FXML
    private Circle turn;


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

    /**
     * Initializes the scen by setting the decks and the hand to the left, the playAre in the center and the common objective and the plateau to the right
     * @param url
     * @param resourceBundle
     */
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


        AnchorPane.setTopAnchor(labelCommonObjectives, 40.0);
        AnchorPane.setRightAnchor(labelCommonObjectives, 90.0);

        AnchorPane.setTopAnchor(labelMyObjective, 150.0);
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
        AnchorPane.setTopAnchor(logout, 0.0);
        AnchorPane.setRightAnchor(logout, 0.0);

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


        handBackSide3.setFitWidth(120);
        handBackSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide3, 250.0);
        AnchorPane.setLeftAnchor(handBackSide3, 20.0);


        handFrontSide3.setFitWidth(120);
        handFrontSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handFrontSide3, 250.0);
        AnchorPane.setLeftAnchor(handFrontSide3, 150.0);

        AnchorPane.setTopAnchor(sender, 5.0);
        AnchorPane.setLeftAnchor(sender, 230.0);

        AnchorPane.setTopAnchor(chatMessage, 5.0);
        AnchorPane.setLeftAnchor(chatMessage, 30.0);
        chatMessage.setPromptText("Chat with someone!");

        AnchorPane.setTopAnchor(confirmMessage, 5.0);
        AnchorPane.setLeftAnchor(confirmMessage, 370.0);

        AnchorPane.setTopAnchor(myPoints, 10.0);
        AnchorPane.setRightAnchor(myPoints, 60.0);
        myPoints.setPrefWidth(200);
        myPoints.setAlignment(Pos.CENTER);
        myPoints.setText("");


        commonObjective1.setFitWidth(120);
        commonObjective1.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective1, 60.0);
        AnchorPane.setRightAnchor(commonObjective1, 20.0);


        commonObjective2.setFitWidth(120);
        commonObjective2.setFitHeight(90);
        AnchorPane.setTopAnchor(commonObjective2, 60.0);
        AnchorPane.setRightAnchor(commonObjective2, 150.0);


        myObjective.setFitWidth(120);
        myObjective.setFitHeight(90);
        AnchorPane.setTopAnchor(myObjective, 170.0);
        AnchorPane.setRightAnchor(myObjective, 100.0);


        AnchorPane.setBottomAnchor(buttonPlayer1, 20.0);
        AnchorPane.setRightAnchor(buttonPlayer1, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer2, 60.0);
        AnchorPane.setRightAnchor(buttonPlayer2, 20.0);

        AnchorPane.setBottomAnchor(buttonPlayer3, 100.0);
        AnchorPane.setRightAnchor(buttonPlayer3, 20.0);

        buttonPlayer1.setPrefWidth(200);
        // Cen
        buttonPlayer2.setPrefWidth(200);

        buttonPlayer3.setPrefWidth(200);

        circle1.setVisible(false);
        circle2.setVisible(false);
        circle3.setVisible(false);
        circle4.setVisible(false);

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
    /**
     * Shows logs
     * @param log log to show
     */
    public void showLog(String log) {
        logField.setText(log);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(6),
                event -> logField.setText("")
        ));
        timeline.setCycleCount(1);
        timeline.play();

    }
    /**
     * Updates the scene when it receives a new Game update
     * @param game Game update
     */
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
        playArea.getChildren().removeIf(node -> node instanceof StackPane || node instanceof Text);

        sender.getItems().clear();
        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(clientNickname)) {

                myPoints.setText("POINTS: " + p.getPoints());

            }else
                sender.getItems().add(p.getNickname());
        }
        sender.getItems().add("All the players");

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
            String sidePath = "/assets/images/emptydeck.png";
            sidePath = getClass().getResource(sidePath).toExternalForm();
            resourceDeckTop.setImage(new Image(sidePath));
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
            String sidePath = "/assets/images/emptydeck.png";
            sidePath = getClass().getResource(sidePath).toExternalForm();
            goldDeckTop.setImage(new Image(sidePath));
        }

        int idCard;
        String path = "";
        if (game.getResourceDeck() != null && game.getResourceDeck().getVisible() != null && !game.getResourceDeck().getVisible().isEmpty()) {
            List<Card> resourceVisible = game.getResourceDeck().getVisible().stream().toList();
            idCard = resourceVisible.get(0).getId();

            path = "/assets/images/front/";
            if (idCard >= 10) {
                path += "0";

            } else {
                path += "00";

            }
            path += idCard;
            path += ".png";
            visibleViewMap.put(resourceVisible1, path);
            path = getClass().getResource(path).toExternalForm();
            resourceVisible1.setImage(new Image(path));

            if (resourceVisible.size()==2) {
                idCard = resourceVisible.get(1).getId();

                path = "/assets/images/front/";
                if (idCard >= 10) {
                    path += "0";

                } else {
                    path += "00";

                }
                path += idCard;
                path += ".png";
                visibleViewMap.put(resourceVisible2, path);
                path = getClass().getResource(path).toExternalForm();
                resourceVisible2.setImage(new Image(path));
            }
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

            if (goldVisible.size()==2) {
                idCard = goldVisible.get(1).getId();

                path = "/assets/images/front/";

                path += "0";
                path += idCard;
                path += ".png";
                visibleViewMap.put(goldVisible2, path);
                path = getClass().getResource(path).toExternalForm();
                goldVisible2.setImage(new Image(path));
            }
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
        if(game.getPlayers() != null && !game.getPlayers().isEmpty())
            SetCircles(game.getPlayers());
        turn.setVisible(false);
        if (Objects.equals(game.getPlayers().get(game.getTurn()).getNickname(), clientNickname)){
            turn.setFill(Color.BLACK);
            turn.setRadius(25);
            turn.setVisible(true);
            AnchorPane.setBottomAnchor(turn, 200.0);
            AnchorPane.setRightAnchor(turn, 190.0);
            }
        for (Player p : game.getPlayers()) {

            if (p.getNickname().equals(clientNickname)) {
                PlacedSide[][] placedSides = p.getPlayArea().getMatrixPlayArea();

                StackPane.setMargin(playArea, new Insets(50));

                int id;
                for (int row = 0; row < placedSides.length; row++) {
                    for (int col = 0; col < placedSides[0].length; col++) {
                        if (placedSides[0].length*xshifht>= 780){
                            xcoord*=0.75;
                            ycoord*=0.75;
                            xshifht*=0.75;
                            yshift*=0.75;
                            fontSize*=0.75;
                        }
                        if (placedSides[row][col] != null && placedSides[row][col] instanceof EmptyPlacedSide) {
                            Tuple coord = placedSides[row][col].getActualCoord();
                            Label coordinates = new Label("(" + coord.i + "," + coord.j + ")");
                            coord = getCard(placedSides, placedSides[row][col]);
                            Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
                            coordinates.setFont(font);
                            StackPane region = new StackPane();
                            String imagePath2 = getClass().getResource("/assets/images/emptycard.png").toExternalForm();
                            ImageView imageView = new ImageView(new Image(imagePath2));
                            imageView.setFitWidth(xcoord);
                            imageView.setFitHeight(ycoord);
                            imageView.setPreserveRatio(true);

                            region.getChildren().addAll(imageView, coordinates);

                            double x = (coord.j) * (xshifht);
                            double y = (coord.i) * (yshift);

                            region.setLayoutX(x);
                            region.setLayoutY(y);

                            playArea.getChildren().add(region);
                        }
                    }
                }
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
                        String imagePath2 = getClass().getResource(path).toExternalForm();

                        Tuple coord = getCard(placedSides, placedSide);
                        if (coord.j*xshifht>= 780){
                            xcoord*=0.75;
                            ycoord*=0.75;
                            xshifht*=0.75;
                            yshift*=0.75;
                            fontSize*=0.75;
                        }

                        StackPane region = new StackPane();
                        ImageView imageView = new ImageView(new Image(imagePath2));
                        imageView.setFitWidth(xcoord);
                        imageView.setFitHeight(ycoord);
                        imageView.setPreserveRatio(true);
                        region.getChildren().add(imageView);

                        double x = (coord.j) * (xshifht);
                        double y = (coord.i) * (yshift);

                        region.setLayoutX(x);
                        region.setLayoutY(y);
                        playArea.getChildren().add(region);
                    }
                }
            }


        }

        List<String> nicknames = game.getPlayers().stream().map(p -> p.getNickname()).filter(n -> !n.equals(clientNickname)).toList();
        buttonPlayer1.setText("View " + nicknames.get(0));
        buttonPlayer1.setOnAction(event -> {
            try {
                gui.viewOtherPlayer(clientNickname, nicknames.get(0), game);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if (nicknames.size() > 1) {
            buttonPlayer2.setText("View " + nicknames.get(1));
            buttonPlayer2.setOnAction(event -> {
                try {
                    gui.viewOtherPlayer(clientNickname, nicknames.get(1), game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } else {
            buttonPlayer2.setVisible(false);
        }
        if (nicknames.size() > 2) {
            buttonPlayer3.setText("View " + nicknames.get(2));
            buttonPlayer3.setOnAction(event -> {
                try {
                    gui.viewOtherPlayer(clientNickname, nicknames.get(2), game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else
            buttonPlayer3.setVisible(false);

        logout.setOnAction(event -> {
            try {
                gui.logout();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Starts waiting for coordinates where to place the card
     * @param event Click with mouse on hand card
     */
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
    @FXML
    public void confirmMessage(Event event){
        String message = chatMessage.getText();
        if (message.isBlank())
            return;
        chatMessage.setText("");
        String toWhom = sender.getValue();
        gui.sendMessage(message, toWhom);
    }

    /**
     * Checks if both coordinates are set
     * @param source
     */
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

                gui.placeCard(CardServerId, isFront, row, column);
            } else {
                System.out.println("there is no card");
            }


        }
    }

    /**
     * Draws from Resource deck
     * @param event Click on Resource deck
     */
    @FXML
    public void drawResourceDeck(MouseEvent event) {
        gui.drawDeck(false);

    }
    /**
     * Draws from Gold deck
     * @param event Click on Gold deck
     */
    @FXML
    public void drawGoldDeck(MouseEvent event) {
        gui.drawDeck(true);

    }
    /**
     * Draws visible card
     * @param event Click on card
     */
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

        gui.drawVisible(CardServerId);


    }

    /**
     * Support method to place Cards correctly
     * @param placedSides Matrix of placed cards
     * @param toFind card to find
     * @return coordinates
     */
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

    /**
     * Support method that sets the circles based on the player's points
     * @param players
     */
    public void SetCircles(List<Player> players){

        Tuple coord = calculatecoord(players.getFirst().getPoints());
        circle1.setVisible(true);
        circle1.setRadius(17);
        circle1.setFill(getPaint(players.getFirst().getColor()));

        StackPane region = new StackPane();
        String imagePath2 = getClass().getResource("/assets/images/plateau.png").toExternalForm();
        ImageView imageView = new ImageView(new Image(imagePath2));
        imageView.setFitWidth(700);
        imageView.setFitHeight(350);
        imageView.setPreserveRatio(true);


        circle1.setTranslateX(coord.i);
        circle1.setTranslateY(coord.j);



        if(players.size()>=2) {
            coord = calculatecoord(players.get(1).getPoints());
            circle2.setVisible(true);
            circle2.setRadius(17);
            circle2.setFill(getPaint(players.get(1).getColor()));
            circle2.setTranslateX(coord.i);
            circle2.setTranslateY(coord.j);
        }
        if(players.size()>=3) {
            coord = calculatecoord(players.get(2).getPoints());
            circle3.setVisible(true);
            circle3.setRadius(17);
            circle3.setFill(getPaint(players.get(2).getColor()));
            circle3.setTranslateX(coord.i);
            circle3.setTranslateY(coord.j);
        }
        if(players.size()>=4) {
            coord = calculatecoord(players.get(3).getPoints());
            circle4.setVisible(true);
            circle4.setFill(getPaint(players.get(3).getColor()));
            circle4.setRadius(17);
            circle4.setTranslateX(coord.i);
            circle4.setTranslateY(coord.j);
        }

        if (players.size() == 1){
            region.getChildren().addAll(imageView, circle1);
        } else if (players.size() == 2) {
            region.getChildren().addAll(imageView, circle1, circle2);
        } else if (players.size() == 3) {
            region.getChildren().addAll(imageView, circle1, circle2, circle3);
        } else
            region.getChildren().addAll(imageView, circle1, circle2, circle3, circle4);

        region.setLayoutX(1200);
        region.setLayoutY(300);
        mainBackground.getChildren().add(region);

    }

    /**
     * Calculate coordinates where to place the circle based on the number of points
     * @param points number of points
     * @return Pixels coordinates
     */
    public Tuple calculatecoord(int points){
        Tuple coord = null;

        switch (points){
            case 0:
                coord = new Tuple(-42, 151);
                break;
            case 1:
                coord = new Tuple(0, 151);
                break;
            case 2:
                coord = new Tuple(42, 151);
                break;
            case 3:
                coord = new Tuple(61, 114);
                break;
            case 4:
                coord = new Tuple(20, 114);
                break;
            case 5:
                coord = new Tuple(-21, 114);
                break;
            case 6:
                coord = new Tuple(-62, 114);
                break;
            case 7:
                coord = new Tuple(-62, 77);
                break;
            case 8:
                coord = new Tuple(-21, 77);
                break;
            case 9:
                coord = new Tuple(20, 77);
                break;
            case 10:
                coord = new Tuple(61, 77);
                break;
            case 11:
                coord = new Tuple(61, 40);
                break;
            case 12:
                coord = new Tuple(20, 40);
                break;
            case 13:
                coord = new Tuple(-21, 40);
                break;
            case 14:
                coord = new Tuple(-62,40);
                break;
            case 15:
                coord = new Tuple(-62,3);
                break;
            case 16:
                coord = new Tuple(-21,3);
                break;
            case 17:
                coord = new Tuple(20,3);
                break;
            case 18:
                coord = new Tuple(61,3);
                break;
            case 19:
                coord = new Tuple(61,-38);
                break;
            case 20:
                coord = new Tuple(0, -57);
                break;
            case 21:
                coord = new Tuple(-62, -38);
                break;
            case 22:
                coord = new Tuple(-62, -75);
                break;
            case 23:
                coord = new Tuple(-62, -112);
                break;
            case 24:
                coord = new Tuple(-38, -143);
                break;
            case 25:
                coord = new Tuple(0, -150);
                break;
            case 26:
                coord = new Tuple(38, -143);
                break;
            case 27:
                coord = new Tuple(61, -112);
                break;
            case 28:
                coord = new Tuple(61, -75);
                break;
            case 29:
                coord = new Tuple(0, -105);
                break;
            default:
                break;
        }

        return coord;
    }

    /**
     * Based on the color choose by the player return the same color but as JavaFX library class
     * @param color player color
     * @return Paint color
     */
    public Color getPaint(it.polimi.is24am05.model.enums.Color color){
        if(color == it.polimi.is24am05.model.enums.Color.BLUE){
            return Color.BLUE;
        }
        else if (color == it.polimi.is24am05.model.enums.Color.RED){
            return Color.RED;
        }
        else if (color == it.polimi.is24am05.model.enums.Color.YELLOW){
            return Color.YELLOW;
        }else{
            return Color.GREEN;
        }

    }
}

