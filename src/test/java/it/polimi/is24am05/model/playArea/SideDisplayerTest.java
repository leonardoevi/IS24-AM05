package it.polimi.is24am05.model.playArea;

import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.goldCard.GoldFrontSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceFrontSide;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterBackSide;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import org.junit.jupiter.api.Test;

import static it.polimi.is24am05.model.playArea.SideDisplayer.sideToString;

class SideDisplayerTest {

    @Test
    void sideToStringTest() {
        // Print all card sides
        for(Side side : ResourceFrontSide.values()){
            System.out.println("Side: " + side);

            // Get the matrix
            String[][] draw = sideToString(side);
            // Print the matrix
            System.out.print(matrixToString(draw));

            System.out.println();
        }
        for(Side side : GoldFrontSide.values()){
            System.out.println("Side: " + side);

            // Get the matrix
            String[][] draw = sideToString(side);
            // Print the matrix
            System.out.print(matrixToString(draw));

            System.out.println();
        }
        for(Side side : StarterFrontSide.values()){
            System.out.println("Side: " + side);

            // Get the matrix
            String[][] draw = sideToString(side);
            // Print the matrix
            System.out.print(matrixToString(draw));

            System.out.println();
        }
        for(Side side : ResourceBackSide.values()){
            System.out.println("Side: " + side);

            // Get the matrix
            String[][] draw = sideToString(side);
            // Print the matrix
            System.out.print(matrixToString(draw));

            System.out.println();
        }
        for(Side side : GoldBackSide.values()){
            System.out.println("Side: " + side);

            // Get the matrix
            String[][] draw = sideToString(side);
            // Print the matrix
            System.out.print(matrixToString(draw));

            System.out.println();
        }
        for(Side side : StarterBackSide.values()){
            System.out.println("Side: " + side);

            // Get the matrix
            String[][] draw = sideToString(side);
            // Print the matrix
            System.out.print(matrixToString(draw));

            System.out.println();
        }
    }
    @Test
    void emptyToStringTest(){
        System.out.println(matrixToString(SideDisplayer.emptySideToString(new Tuple(0,0))));
    }

    private String matrixToString(String[][] matrix){
        StringBuilder sb = new StringBuilder();
        for(String[] row : matrix) {
            for (String s : row) {
                sb.append(s);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}