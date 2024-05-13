package it.polimi.is24am05.model.Player;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.goldCard.GoldFrontSide;
import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.AlwaysTrueMultiplier;
import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.CornerMultiplier;
import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.GoldCardMultiplier;
import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.ItemMultiplier;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.playArea.AreaDisplayer;
import it.polimi.is24am05.model.playArea.SideDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.is24am05.model.playArea.SideDisplayer.*;

/**
 * This class is needed to display the hand of a player as a String
 */
public class HandDisplayer {
    public static final String WHITE_BOLD = "\033[0;37m";
    public static int STARTER_SIDES_SPACING = 3;
    private static final int COLOR_LEN = 10;
    /**
     * @return The hand of the player when they have to place the starter Card
     */
    public static String CardToString(Card card){
        return matrixToString(CardToMatrix(card));
    }

    public static String[][] CardToMatrix(Card card){
        String[][] frontSide = SideDisplayer.sideToString(card.getFrontSide());
        String[][] backSide = SideDisplayer.sideToString(card.getBackSide());

        /*
         Edit sides to display useful information
         editTopEdge(frontSide, card.getFrontSide());
         editBottomEdge(frontSide, card.getFrontSide());
         editCentre(frontSide, card.getFrontSide());
        */

        String[][] result = new String[5][frontSide[0].length * 2 + 3];
        fillMatrix(result);

        // Create title
        String title = "- " +  "CardID:  " + String.format("%02d", card.getId()) + " -";
        String padding = " ".repeat(((CARD_WIDTH*2 + 4 + STARTER_SIDES_SPACING) - (title.length()))/2);
        title = padding + title + padding;

        // Put labels
        result[4][1] = BLACK_BOLD_BRIGHT + "front  " + RESET;
        result[4][7] = BLACK_BOLD_BRIGHT + "   back" + RESET;

        // Put sides
        AreaDisplayer.put(frontSide, result, 1,0);
        AreaDisplayer.put(backSide, result, 1,6);

        result[0][4] = title;

        return result;
    }

    public static String handToString(List<Card> cards){
        List<String[][]> handMatrix = new ArrayList<>();

        String[][] spacer =    {{"       "},
                {"       "},
                {"   ·   "},
                {"       "},
                {"       "}};

        for(Card c : cards) {
            handMatrix.add(CardToMatrix(c));
            handMatrix.add(spacer);
        }
        handMatrix.removeLast();

        return matrixToString(sideToSide(handMatrix));

        /*
        StringBuilder s = new StringBuilder();

        for(Card card : cards){
            s.append("Card ID: ").append(card.getId());

            s.append("\n");
        }

        return s.toString();

         */
    }

    public static String matrixToString(String[][] matrix){
        // Return the matrix as a string
        StringBuilder sb = new StringBuilder();
        for(String[] row : matrix){
            for (String s : row){
                sb.append(s);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void fillMatrix(String[][] matrix){
        for (int j=0; j<matrix[0].length; j++)
            matrix[0][j] = "";

        for(int i=1; i< matrix.length; i++){
            for (int j=0; j<matrix[0].length; j++){
                matrix[i][j] = " ";
            }
        }

    }

    public static String[][] sideToSide(List<String[][]> matrices){
        int rows = matrices.getFirst().length;
        int columns = matrices.stream()
                .mapToInt(x -> x[0].length)
                .sum();

        String[][] result = new String[rows][columns];

        // Copy matrices in the result matrix
        int offset = 0;
        for (String[][] matrix : matrices) {
            AreaDisplayer.put(matrix, result, 0, offset);
            offset += matrix[0].length;
        }

        return result;
    }

    public static void editTopEdge(String[][] original, Side side){
        // Ignore sides that dont assign points
        if(side instanceof GoldBackSide || side instanceof ResourceBackSide)
            return;

        int points = side.getPoints();
        if(points == 0)
            return;

        String old = original[0][1];

        // Find central character to replace
        int idx = findCenterVisibleChar(old);

        String firstHalf = old.substring(0, idx);
        String secondHalf = old.substring(idx + 1);

        // Replace the top edge
        if(side instanceof GoldFrontSide)
            original[0][1] = firstHalf + YELLOW_BOLD + points + RESET + secondHalf;
        else
            original[0][1] = firstHalf + YELLOW_BOLD + points + SideDisplayer.getCardColor(side) + secondHalf;

        return;
    }

    public static void editBottomEdge(String[][] original, Side side){
        // Only for gold front sides
        if(!( side instanceof GoldFrontSide)) return;

        Map<Resource, Integer> condition = new HashMap<>(side.getPlacementConditions());

        int conditionCount = 0;
        for(Resource resource : condition.keySet())
            conditionCount += condition.get(resource);

        // Edit bottom side accordingly
        String CARD_COLOR = SideDisplayer.getCardColor(side);
        String bottom= "";

        Resource r1, r2, r3, r4, r5;
        r1 = getResource(condition);
        r2 = getResource(condition);
        r3 = getResource(condition);
        if(conditionCount == 3){
            bottom = CARD_COLOR + " -" + BLACK_BOLD_BRIGHT +
                    SideDisplayer.getElementLetter(r1) +
                    SideDisplayer.getElementLetter(r2) +
                    SideDisplayer.getElementLetter(r3) +
                    CARD_COLOR + "- " + RESET;
        } else if (conditionCount == 4) {
            r4 = getResource(condition);

            bottom = " " + BLACK_BOLD_BRIGHT +
                    SideDisplayer.getElementLetter(r1) +
                    SideDisplayer.getElementLetter(r2) +
                    CARD_COLOR + "-" + BLACK_BOLD_BRIGHT +
                    SideDisplayer.getElementLetter(r3) +
                    SideDisplayer.getElementLetter(r4) + RESET +
                    " ";

        } else if (conditionCount == 5){
            r4 = getResource(condition);
            r5 = getResource(condition);

            bottom = " " + BLACK_BOLD_BRIGHT +
                    SideDisplayer.getElementLetter(r1) +
                    SideDisplayer.getElementLetter(r2) +
                    SideDisplayer.getElementLetter(r3) +
                    SideDisplayer.getElementLetter(r4) +
                    SideDisplayer.getElementLetter(r5) + RESET + " ";

        }

        original[2][1] = bottom;
    }

    private static int findCenterVisibleChar(String string){
        int dashesToSkip = 3;
        int idx = 0;
        while (dashesToSkip > 0){
            if(string.charAt(idx) == '-')
                dashesToSkip--;
            idx++;
        }
        idx--;
        return idx;
    }

    public static void editCentre(String[][] original, Side side){
        if(!(side instanceof GoldFrontSide))
            return;

        GoldCardMultiplier multiplier = side.getMultiplier();

        if(multiplier == AlwaysTrueMultiplier.ALWAYSTRUE)
            return;

        String newCentre;
        if(multiplier == CornerMultiplier.CORNER)
            newCentre = "   " + WHITE_BOLD + "◰" + RESET + "   ";
        else{
            newCentre = "   " + WHITE_BOLD +
                    SideDisplayer.getElementLetter(((ItemMultiplier) multiplier).getItem()).toLowerCase() + RESET +
                    "   ";
        }

        original[1][1] = newCentre;
    }

    private static Resource getResource(Map<Resource, Integer> condition){
        for(Resource resource : Resource.values()){
            if(!condition.containsKey(resource))
                continue;

            int contained = condition.get(resource);
            if(contained > 0){
                condition.put(resource, contained - 1);
                return resource;
            }
        }
        return null;
    }
}

