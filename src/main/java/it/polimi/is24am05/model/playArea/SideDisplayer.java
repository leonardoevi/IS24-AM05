package it.polimi.is24am05.model.playArea;

import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.goldCard.GoldFrontSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterBackSide;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import it.polimi.is24am05.model.enums.Corner;
import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

import java.util.Formatter;
import java.util.List;
import java.util.NoSuchElementException;


// This code will probably be removed from the model and used in the view
/**
 * This class implements the methods to display a card as a string
 */
public class SideDisplayer {
    /**
     * Matrix Sizes
     */
    public static final int iSize = 3, jSize = 3;

    /**
     * Width of the central Strings
     */
    public static final int CARD_WIDTH = 7;

    /**
     * Character used to draw the card
     */
    public static final String MISSING_CORNER = "⤬", EMPTY_CORNER = "▫",
    HORIZONTAL_DASH = "-", HORIZONTAL_FULL = "—", HORIZONTAL_DOTTED = "·",
    VERTICAL_DASH = "|", VERTICAL_DOTTED = ":",
    EMPTY_CENTER = "⊙";



    // ASCII colors
    public static final String YELLOW_BOLD =        "\033[1;93m";
    public static final String BLACK_BOLD_BRIGHT =  "\033[1;90m";
    public static final String WHITE_BOLD_BRIGHT =  "\033[1;97m";
    public static final String RESET =              "\u001B[0m";
    public static final String RED_BOLD =           "\033[1;31m";
    public static final String GREEN_BOLD =         "\033[1;92m";
    public static final String PURPLE_BOLD =        "\033[1;35m";
    public static final String CYAN_BOLD =          "\033[1;96m";

    /**
     * Allows to draw a card Side
     * @param toDraw the side to draw
     * @return A 3 x 3 string matrix. The first and the last columns are 1 character wide, the center is 7 characters wide.
     */
    public static String[][] sideToString(Side toDraw){
        String[][] draw = new String[iSize][jSize];

        String cardColor = getCardColor(toDraw);

        // Draw borders
        StringBuilder topBottom = new StringBuilder(cardColor);
        // If it is a gold side
        if(toDraw instanceof GoldFrontSide || toDraw instanceof GoldBackSide){
            topBottom.append(" ");
            topBottom.append(HORIZONTAL_DASH);
            topBottom.append(YELLOW_BOLD).append(HORIZONTAL_DASH);
            topBottom.append(cardColor).append(HORIZONTAL_DASH);
            topBottom.append(YELLOW_BOLD).append(HORIZONTAL_DASH);
            topBottom.append(cardColor).append(HORIZONTAL_DASH).append(RESET);
            topBottom.append(" ");
        }
        // If it is a Starter side
        else if(toDraw instanceof StarterFrontSide || toDraw instanceof StarterBackSide){
            topBottom.append(" ");
            topBottom.append(HORIZONTAL_FULL.repeat(CARD_WIDTH - 2));
            topBottom.append(" ");
        }
        // If it is a normal side
        else {
            topBottom.append(" ");
            topBottom.append(HORIZONTAL_DASH.repeat(CARD_WIDTH - 2));
            topBottom.append(" ");
        }
        topBottom.append(RESET);

        String rightLeft = cardColor + VERTICAL_DASH + RESET;

        draw[0][1] = topBottom.toString();
        draw[2][1] = topBottom.toString();
        draw[1][0] = rightLeft;
        draw[1][2] = rightLeft;

        // Draw Corners
        for(Corner corner : Corner.getEdges()){
            // Get the coordinates of the corner
            Tuple cornerCoordinates = PlayArea.getNeighbour(new Tuple(1,1), corner);

            String cornerLetter;
            // Get the correct corner representation
            try {
                Element cornerElement = toDraw.getCorner(corner).getFirst();
                cornerLetter = getElementColor(cornerElement) + getElementLetter(cornerElement) + RESET;
            } catch (InvalidCornerException e) {
                // If the card does not have the corner
                cornerLetter = cardColor + MISSING_CORNER + RESET;
            } catch (NoSuchElementException e){
                // If the corner is empty
                cornerLetter = cardColor + EMPTY_CORNER + RESET;
            }

            // Draw the corner
            draw[cornerCoordinates.i][cornerCoordinates.j] = cornerLetter;
        }

        // Draw center
        draw[1][1] = getCentreToString(toDraw);

        return draw;
    }

    /**
     * @param resource Resource
     * @return The generic back side of a Card of the same color of th Resource
     */
    public static String[][] sideToString(Resource resource, boolean isGold){
        Side[] toSearch;

        if(isGold)
            toSearch = GoldBackSide.values();
        else
            toSearch = ResourceBackSide.values();

        for(Side side : toSearch){
            if(side.getSeed() == resource)
                return sideToString(side);
        }

        return null;
    }

    /**
     * @param originalCoord Coordinates in the PlayArea of the side
     * @return A matrix equivalent to a sideToString() in terms of dimensions and length of the Strings. It represents a spot where a card can be placed.
     */
    public static String[][] emptySideToString(Tuple originalCoord){
        String[][] draw = blankToString();

        // Draw sides
        StringBuilder topBottom = new StringBuilder(BLACK_BOLD_BRIGHT);

        topBottom.append(" ");
        topBottom.append(HORIZONTAL_DOTTED.repeat(CARD_WIDTH - 2));
        topBottom.append(" ");
        topBottom.append(RESET);

        String rightLeft = BLACK_BOLD_BRIGHT + VERTICAL_DOTTED + RESET;

        draw[0][1] = topBottom.toString();
        draw[2][1] = topBottom.toString();
        draw[1][0] = rightLeft;
        draw[1][2] = rightLeft;

        // Draw center
        Formatter formatter = new Formatter();
        formatter.format("%3s,%-3d", originalCoord.i, originalCoord.j);

        draw[1][1] = BLACK_BOLD_BRIGHT + formatter.toString() + RESET;
        formatter.close();

        return draw;
    }

    /**
     * @return A matrix equivalent to a sideToString() in terms of dimensions and length of the Strings, however all the strings are empty.
     */
    public static String[][] blankToString(){
        String[][] draw = new String[iSize][jSize];

        String sizeOne = " ", sizeWidth = " ".repeat(CARD_WIDTH);

        for(int i=0; i < iSize; i++){
            draw[i][0] = sizeOne;
            draw[i][1] = sizeWidth;
            draw[i][2] = sizeOne;
        }

        return draw;
    }


    // Useful functions

    /**
     * @param toDraw Side to get the color
     * @return the color corresponding to the card seed
     */
    public static String getCardColor(Side toDraw){
        // Return gray if the side is null
        if(toDraw == null)
            return BLACK_BOLD_BRIGHT;

        // Return yellow for starter card sides
        if(toDraw instanceof StarterBackSide || toDraw instanceof StarterFrontSide)
            return YELLOW_BOLD;

        // The card must have a seed
        Resource seed = toDraw.getSeed();
        assert seed != null;
        // Return the color of the seed
        return getElementColor(seed);
    }

    /**
     * Maps an element to its color
     * @param element resource to get the color
     * @return the color corresponding to the card seed
     */
    protected static String getElementColor(Element element){
        switch (element) {
            case Resource.PLANT -> {
                return GREEN_BOLD;
            }
            case Resource.FUNGI -> {
                return RED_BOLD;
            }
            case Resource.INSECT -> {
                return PURPLE_BOLD;
            }
            case Resource.ANIMAL -> {
                return CYAN_BOLD;
            }
            default -> {
                // For Items
                return YELLOW_BOLD;
            }
        }
    }

    /**
     * Maps an element to a Letter for display
     * @param element to display
     * @return the corresponding letter (String of length 1)
     */
    public static String getElementLetter(Element element){
        switch (element) {
            case Resource.PLANT -> {
                return "P";
            }
            case Resource.FUNGI -> {
                return "F";
            }
            case Resource.INSECT -> {
                return "I";
            }
            case Resource.ANIMAL -> {
                return "A";
            }
            case Item.MANUSCRIPT -> {
                return "M";
            }
            case Item.QUILL -> {
                return "Q";
            }
            default -> {
                // Inkwell, I already used
                return "K";
            }
        }
    }

    /**
     * @param side side to draw
     * @return a string of length CardWidth representing the center of the side
     */
    static String getCentreToString(Side side){
        StringBuilder s = new StringBuilder();

        if(side instanceof StarterBackSide) {
            s.append(YELLOW_BOLD)
                    .append(" ".repeat(CARD_WIDTH/2))
                    .append(HORIZONTAL_DASH)
                    .append(" ".repeat(CARD_WIDTH/2));
            return s.toString();
        }

        try {
            List<Element> elements = side.getCorner(Corner.CE);
            int spacing = (CARD_WIDTH - (elements.size()*2 - 1) )/2;
            if(elements.size() == 1) {
                // The card shows only 1 resource in the center (happens for most cards)
                s.append(" ".repeat(spacing));
                s.append(getElementColor(elements.getFirst())).append(getElementLetter(elements.getFirst())).append(RESET);
                s.append(" ".repeat(spacing));
            }
            if(elements.size() == 2) {
                // The card shows 2 resources in the center (happens for some Starter Card ONLY)
                s.append(" ".repeat(spacing));
                s.append(getElementColor(elements.getFirst())).append(getElementLetter(elements.getFirst())).append(RESET);
                s.append(" ");
                s.append(getElementColor(elements.get(1))).append(getElementLetter(elements.get(1))).append(RESET);
                s.append(" ".repeat(spacing));
            }
            if(elements.size() == 3){
                // The card shows 3 resources in the center (happens for some Starter Card ONLY)
                s.append(" ".repeat(spacing));
                s.append(getElementColor(elements.getFirst())).append(getElementLetter(elements.getFirst())).append(RESET);
                s.append(" ");
                s.append(getElementColor(elements.get(1))).append(getElementLetter(elements.get(1))).append(RESET);
                s.append(" ");
                s.append(getElementColor(elements.get(2))).append(getElementLetter(elements.get(2))).append(RESET);
                s.append(" ".repeat(spacing));
            }
        } catch (InvalidCornerException e) {
            // If the card doesn't have any resource in the centre
            int spacing = CARD_WIDTH/2;
            s.append(" ".repeat(spacing));
            s.append(getCardColor(side)).append(EMPTY_CENTER).append(RESET);
            s.append(" ".repeat(spacing));
        }
        return s.toString();
    }
}
