package it.polimi.is24am05.model.Player;

// This code will probably be removed from the model and used in the view

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.playArea.AreaDisplayer;
import it.polimi.is24am05.model.playArea.SideDisplayer;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.is24am05.model.playArea.SideDisplayer.*;

/**
 * This class is needed to display the hand of a player as a String
 */
public class HandDisplayer {
    public static int STARTER_SIDES_SPACING = 3;
    /**
     * @return The hand of the player when they have to place the starter Card
     */
    public static String CardToString(Card card){
        return matrixToString(CardToMatrix(card));
    }

    public static String[][] CardToMatrix(Card card){
        String[][] frontSide = SideDisplayer.sideToString(card.getFrontSide());
        String[][] backSide = SideDisplayer.sideToString(card.getBackSide());

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
    }

    private static String matrixToString(String[][] matrix){
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

    private static String[][] sideToSide(List<String[][]> matrices){
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
}

