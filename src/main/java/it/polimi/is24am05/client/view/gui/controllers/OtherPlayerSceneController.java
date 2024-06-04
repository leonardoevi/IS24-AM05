package it.polimi.is24am05.client.view.gui.controllers;

import it.polimi.is24am05.client.view.gui.GUIRoot;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.side.EmptyPlacedSide;
import it.polimi.is24am05.model.card.side.PlacedSide;

import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
 * Other Player scene Controller
 */
public class OtherPlayerSceneController implements Initializable {
    /**
     * Values needed in order to place cards one on top of the other snd cover only the corners
     */
    private double xcoord = 81;
    private double ycoord = 54;

    private double xshifht = 62;
    private double yshift = 30;
    private double fontSize = 14;

    @FXML
    private Pane playArea;
    @FXML
    private StackPane backgroundPlayArea;
    @FXML
    private ImageView handBackSide1;

    @FXML
    private ImageView handBackSide2;

    @FXML
    private ImageView handBackSide3;


    @FXML

    private Label myPoints;

    @FXML

    private Label logField;
    @FXML

    private Circle turn;


    private String clientNickname;
    private String clientClicked;


    private Map<ImageView, String> handViewMap = new HashMap<>();


    @FXML
    private StackPane leftSideBackground;
    @FXML
    private AnchorPane mainBackground;
    @FXML
    private Button Return;
    private GUIRoot gui;

    private List<ImageView> HandImageViewList;

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    public void setClientNickname(String nickname) {
        this.clientNickname = nickname;

    }
    public void setClientClicked(String nickname) {
        this.clientClicked = nickname;

    }

    /**
     * Initializes a similar scen to the game scene but it only shows the backside of the hand cards, their playArea and how many points they have
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        HandImageViewList = new ArrayList<>();
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


        Image backgroundImage = new Image(imageBackPath);

        // Crea un BackgroundImage
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

        AnchorPane.setBottomAnchor(Return, 30.0);
        AnchorPane.setRightAnchor(Return, 20.0);
        Return.setPrefWidth(200);

        handBackSide1.setFitWidth(120);
        handBackSide1.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide1, 50.0);
        AnchorPane.setLeftAnchor(handBackSide1, 20.0);

        handBackSide2.setFitWidth(120);
        handBackSide2.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide2, 150.0);
        AnchorPane.setLeftAnchor(handBackSide2, 20.0);

        handBackSide3.setFitWidth(120);
        handBackSide3.setFitHeight(90);
        AnchorPane.setBottomAnchor(handBackSide3, 250.0);
        AnchorPane.setLeftAnchor(handBackSide3, 20.0);

        AnchorPane.setTopAnchor(myPoints, 30.0);
        AnchorPane.setRightAnchor(myPoints, 90.0);
        myPoints.setPrefWidth(200);
        myPoints.setAlignment(Pos.CENTER);
        myPoints.setText("");


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
                Duration.seconds(3),
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

        handBackSide1.setImage(null);
        handBackSide2.setImage(null);
        handBackSide3.setImage(null);

        HandImageViewList.clear();
        playArea.getChildren().removeIf(node -> node instanceof StackPane || node instanceof Text);

        for (Player p : game.getPlayers()) {
            if (p.getNickname().equals(clientNickname)) {
                myPoints.setText("POINTS: " + p.getPoints());
                try {
                    List<Resource> resources = game.getBlurredHand(clientNickname);
                    HandImageViewList.add(handBackSide1);
                    HandImageViewList.add(handBackSide2);
                    HandImageViewList.add(handBackSide3);

                    List<Card> hand = p.getHand();
                    String path = getPath(hand.getFirst());
                    path = getClass().getResource(path).toExternalForm();
                    handBackSide1.setImage(new Image(path));

                    path = getPath(hand.get(1));
                    path = getClass().getResource(path).toExternalForm();
                    handBackSide2.setImage(new Image(path));

                    if (hand.size()>2){
                        path = getPath(hand.get(2));
                        path = getClass().getResource(path).toExternalForm();
                        handBackSide3.setImage(new Image(path));
                    }

                } catch (NoSuchPlayerException e) {
                    throw new RuntimeException(e);
                }
                myPoints.setText("POINTS: " + p.getPoints());


            }
        }

        turn.setVisible(false);
        if (Objects.equals(game.getPlayers().get(game.getTurn()).getNickname(), clientNickname)){
            turn.setFill(Color.BLACK);
            turn.setRadius(25);
            turn.setVisible(true);
            AnchorPane.setBottomAnchor(turn, 200.0);
            AnchorPane.setRightAnchor(turn, 190.0);
        }
        System.out.println("Displaying playarea");
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
                            Label coordinates = new Label("("+ coord.i + ","+coord.j+")");
                            Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
                            coordinates.setFont(font);
                            StackPane region = new StackPane();
                            String imagePath2 = getClass().getResource("/assets/images/emptycard.png").toExternalForm();
                            ImageView imageView = new ImageView(new Image(imagePath2));
                            imageView.setFitWidth(xcoord);
                            imageView.setFitHeight(ycoord);
                            imageView.setPreserveRatio(true);

                            region.getChildren().addAll(imageView, coordinates);
                            coord = getCard(placedSides, placedSides[row][col]);
                            double x = (coord.j) * (xshifht);
                            double y = (coord.i) * (yshift);

                            region.setLayoutX(x);
                            region.setLayoutY(y);

                            playArea.getChildren().add(region);
                        }
                    }
                    String path = "";
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


        }
        Return.setOnAction(event -> {
            gui.returnPlay(clientClicked, game);
        });

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
     * Given a Card based on if its Gold or Resource and based on their resource returns a generic backside path card
     * @param c Card
     * @return Path
     */
    public String getPath(Card c) {
    String sidePath = "/assets/images/back/";
    if (c.getBackSide() instanceof GoldBackSide) {
        Resource r = c.getBackSide().getSeed();
        if (r.equals(Resource.FUNGI)) {
            sidePath += "041.png";
        } else if (r.equals(Resource.PLANT)) {
            sidePath += "051.png";
        } else if (r.equals(Resource.ANIMAL)) {
            sidePath += "061.png";
        } else {
            sidePath += "071.png";
        }
    } else {
        Resource r = c.getBackSide().getSeed();
        if (r.equals(Resource.FUNGI)) {
            sidePath += "001.png";
        } else if (r.equals(Resource.PLANT)) {
            sidePath += "011.png";
        } else if (r.equals(Resource.ANIMAL)) {
            sidePath += "021.png";
        } else {
            sidePath += "031.png";
        }
    }
    return sidePath;
}
}
