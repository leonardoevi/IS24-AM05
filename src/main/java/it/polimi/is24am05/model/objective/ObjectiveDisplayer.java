package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.playArea.SideDisplayer;

import java.util.Arrays;
import java.util.Objects;

import static it.polimi.is24am05.model.playArea.AreaDisplayer.put;
import static it.polimi.is24am05.model.playArea.SideDisplayer.RESET;
import static it.polimi.is24am05.model.playArea.SideDisplayer.YELLOW_BOLD;

public class ObjectiveDisplayer {
    private static final String BORDER = "+";

    public static String[][] layoutObjectiveToString(Objective objective) {
        /*
        String[][] objCard = SideDisplayer.blankToString(BORDER);

        Resource[][] layout = ((LayoutMultiplier) objective.getMultiplier()).getLayout();

        String[][] letterLayout = new String[layout.length][layout[0].length];

        int joffset = 2;
        for (int i = 0; i < layout.length; i++) {
            char[] array = objCard[i][1].toCharArray();
            for (int j = 0; j < layout[0].length; j++) {
                if(layout[i][j] != null) {
                    array[j+joffset] = getElementLetter(layout[i][j]).charAt(0);
                }
            }
            objCard[i][1] = new String(array);
        }

        return objCard;

     */

        Resource[][] tmpLayout = ((LayoutMultiplier) objective.getMultiplier()).getLayout();

        tmpLayout = editLayout(tmpLayout);

        int jShift = 0;
        if(tmpLayout.length == 4){
            jShift = 0;
        }

        // A matrix 4 times as large is needed (length and height double and are increased by 1)
        String[][] stringMatrix = new String[tmpLayout.length *2 + 1][(tmpLayout[0].length + jShift)*2 + 1];

        // Fill matrix with empty Strings of the correct size
        String[][] blankMatrix = SideDisplayer.blankToString();
        for (int i = 0; i < stringMatrix.length - 2; i += 2) {
            for (int j = 0; j < stringMatrix[0].length - 2; j += 2) {
                put(blankMatrix, stringMatrix, i, j);
            }
        }

        for (int i = 0; i < tmpLayout.length; i++) {
            for (int j = 0; j < tmpLayout[0].length; j++) {
                if(tmpLayout[i][j] == null)
                    continue;

                put(SideDisplayer.sideToString(tmpLayout[i][j], false), stringMatrix, i*2, (j+jShift)*2);
            }
        }

        // Add a border
        String[][] toReturn = new String[stringMatrix.length + 2][stringMatrix[0].length + 2 + 2];

        put(stringMatrix, toReturn, 1,2);

        for(int i=0; i< toReturn.length; i++) {
            toReturn[i][0] = YELLOW_BOLD + BORDER + RESET;
            toReturn[i][1] = " ";
            toReturn[i][toReturn[0].length - 1] = YELLOW_BOLD + BORDER + RESET;
            toReturn[i][toReturn[0].length - 2] =  " ";
        }

        for(int j=0; j< toReturn[0].length; j++) {
            toReturn[0][j] = "";
            toReturn[toReturn.length - 1][j] = "";
        }
        toReturn[0][0] = toReturn[toReturn.length - 1][0] = YELLOW_BOLD + "+ + + + + + + + + + + + + + +" + RESET;

        return toReturn;
    }

    private static Resource[][] editLayout(Resource[][] tmpLayout) {
        if(tmpLayout.length == 3)
            return tmpLayout;

        Resource[][] newLayout = new Resource[3][2];

        // Remove the empty row from the tmpLayout

        int idx = 0;

        for (int i = 0; i < tmpLayout.length; i++) {
            if(Arrays.stream(tmpLayout[i]).allMatch(Objects::isNull)){
                continue;
            }
            System.arraycopy(tmpLayout[i], 0, newLayout[idx], 0, tmpLayout[0].length);

            idx ++;
        }

        Resource[][] toRet = new Resource[3][3];

        int jShift = 0;
        if(newLayout[1][1] == null)
            jShift = 1;

        for (int i = 0; i < toRet.length; i++) {
            System.arraycopy(newLayout[i], 0, toRet[i], jShift, newLayout[0].length);

        }

        return toRet;

    }

    // Returns a back side of the same color of the seed
    private static ResourceBackSide getBackSide(Resource seed) {
        switch (seed) {
            case Resource.PLANT -> {
                return ResourceBackSide.RBS_011;
            }
            case Resource.FUNGI -> {
                return ResourceBackSide.RBS_001;
            }
            case Resource.INSECT -> {
                return ResourceBackSide.RBS_031;
            }
            default -> {
                return ResourceBackSide.RBS_021;
            }
        }
    }
}
