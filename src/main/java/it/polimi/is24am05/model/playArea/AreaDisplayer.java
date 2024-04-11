package it.polimi.is24am05.model.playArea;

import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Item;

import java.util.*;

import static it.polimi.is24am05.model.playArea.SideDisplayer.RESET;

// This code will probably be removed from the model and used in the view
/**
 * This class is needed to display the PlayArea as a String
 */
public class AreaDisplayer {
    private final PlayArea playArea;

    private final int iShift, jShift;
    private final int iLength, jLength;

    public AreaDisplayer(PlayArea playArea) {
        this.playArea = playArea;

        // Make room for the "frame" of empty columns and rows
        int actualMinI = playArea.minI - 1;
        int actualMinJ = playArea.minJ - 1;
        int actualMaxI = playArea.maxI + 1;
        int actualMaxJ = playArea.maxJ + 1;

        // Calculate shifts
        int shift = 0;
        while(actualMinI + shift < 0)
            shift++;
        this.iShift = shift;

        shift = 0;
        while(actualMinJ + shift < 0)
            shift++;
        this.jShift = shift;

        // Calculate lengths necessary to fit the area in a matrix
        this.iLength = actualMaxI - actualMinI + 1;
        this.jLength = actualMaxJ - actualMinJ + 1;
    }

    /**
     * @return The playArea, represented as a string.
     */
    @Override
    public String toString() {
        String[][] stringMatrix = areaToMatrix();

        String[] visibleElementsScore = elementMapToString();

        int i = 0;
        // Return the matrix as a string
        StringBuilder sb = new StringBuilder();
        for(String[] row : stringMatrix){
            for (String s : row){
                sb.append(s);
            }
            if(i < visibleElementsScore.length){
                sb.append(" " + visibleElementsScore[i]);
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Copies a String[][] top inside a bigger String[][] bottom
     * top[0][0] will be placed in bottom[i][j], and the rest of the matrix accordingly
     * WARNING - DOES NOT CHECK FOR INDEXES OUT OF BOUND
     * @param top Matrix to insert
     * @param bottom Matrix to be modified
     * @param i starting i coordinate
     * @param j starting j coordinate
     */
    public static void put(String[][] top, String[][] bottom, int i, int j) {
        int m = top.length;
        int n = top[0].length;

        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if(top[row][col] != null)
                    bottom[i + row][j + col] = top[row][col];
            }
        }
    }

    /**
     * @param playAreaCoordinates original coordinates
     * @return a Tuple containing the new coordinates to start drawing the side in the new matrix
     */
    private Tuple transpose(Tuple playAreaCoordinates){
        return new Tuple((playAreaCoordinates.i + this.iShift)*2, (playAreaCoordinates.j + this.jShift)*2);
    }

    /**
     * @return A vector of String, each containing the counter of visible elements, if any is present.
     */
    private String[] elementMapToString(){
        Map<Element, Integer> visibleElements = this.playArea.getVisibleElements();

        Map<String, String> elementName = Map.of(
                "ANIMAL" , "Animal    ",
                "INSECT" , "Insect    ",
                "FUNGI" , "Fungi     ",
                "PLANT" , "Plant     ",
                "MANUSCRIPT" , "Manuscript",
                "INKWELL" , "inKwell   ",
                "QUILL", "Quill     "
        );

        // Remove elements that are not visible
        for(Element element : Element.values())
            if(visibleElements.get(element) == 0)
                visibleElements.remove(element);

        // If there are no visible elements (Unlikely)
        if(visibleElements.isEmpty())
            return new String[0];

        // Otherwise
        List<String> toRet = new ArrayList<>();

        boolean lineAdded = false;
        for(Element element : Element.values()){
            // Don't display elements if none is visible
            if(!visibleElements.containsKey(element))
                continue;

            // Add an empty line between resources and items
            if(element instanceof Item && !lineAdded){
                toRet.add(" "); lineAdded = true; }

            // Add the element count to the array
            StringBuilder s = new StringBuilder(SideDisplayer.getElementColor(element));
            s.append(elementName.get(element.toString())).append("\t")
                    .append(visibleElements.get(element))
                    .append(RESET);
            toRet.add(s.toString());
        }

        // Return the array
        return toRet.toArray(String[]::new);
    }

    public String[][] areaToMatrix(){
        // Allocate the String matrix that will contain the sides.
        // Each side is represented as a 3 x 3 matrix.
        // However, each side overlaps with others neighbouring sides.
        // * — * — *
        // | X | Y |
        // * — * — *

        // A matrix 4 times as large is needed (length and height double and are increased by 1)
        String[][] stringMatrix = new String[iLength *2 + 1][jLength*2 + 1];

        // Fill matrix with empty Strings of the correct size
        String[][] blankMatrix = SideDisplayer.blankToString();
        for (int i = 0; i < stringMatrix.length - 2; i += 2) {
            for (int j = 0; j < stringMatrix[0].length - 2; j += 2) {
                put(blankMatrix, stringMatrix, i, j);
            }
        }

        // Put cards in the matrix

        // Start with the Empty Slots
        for(Tuple coord : playArea.getFrontier()){
            Tuple newCoord = transpose(coord);
            put(SideDisplayer.emptySideToString(coord), stringMatrix, newCoord.i, newCoord.j);
        }

        // Draw Cards in the order they were played
        for(PlacedSide placedSide : this.playArea.getOrderedPlacements()){
            Tuple newCoord = transpose(placedSide.getActualCoord());
            put(SideDisplayer.sideToString(placedSide.getSide()), stringMatrix, newCoord.i, newCoord.j);
        }

        return stringMatrix;
    }
}
