package it.polimi.is24am05.model.objective;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.is24am05.model.Player.HandDisplayer.matrixToString;
import static it.polimi.is24am05.model.playArea.AreaDisplayer.put;

public class SharedObjectiveDisplayer {
    private final Objective[] sharedObjectives;

    public SharedObjectiveDisplayer(Objective[] sharedObjectives) {
        this.sharedObjectives = sharedObjectives;
    }

    @Override
    public String toString() {
        int numberOfSharedObjectives = sharedObjectives.length;

        if (numberOfSharedObjectives == 0)
            return "";

        List<String[][]> matrices = new ArrayList<>();
        for (Objective objective : sharedObjectives) {
            matrices.add(objective.toMatrix());
        }

        int oHeight = Objective.O_088.toMatrix().length;
        int oLen = Objective.O_088.toMatrix()[0].length;

        String[][] matrix = new String[oHeight][(oLen+1)*numberOfSharedObjectives - 1];

        int j = 0;
        for (Objective objective : sharedObjectives) {
            put(objective.toMatrix(), matrix, 0, j);
            j += oLen;
            try{
                for (int i = 0; i < oHeight; i++) {
                    matrix[i][j] = "   ";
                }
                j++;
            } catch (IndexOutOfBoundsException ignored) {}
        }

        return matrixToString(matrix);
    }

    public String toString(String options) {
        int numberOfSharedObjectives = sharedObjectives.length;

        if (numberOfSharedObjectives == 0)
            return "";

        List<String[][]> matrices = new ArrayList<>();
        for (Objective objective : sharedObjectives) {
            matrices.add(objective.toMatrixNoName());
        }

        int oHeight = Objective.O_088.toMatrix().length-1;
        int oLen = Objective.O_088.toMatrix()[0].length;

        String[][] matrix = new String[oHeight][(oLen+1)*numberOfSharedObjectives - 1];

        int j = 0;
        for (Objective objective : sharedObjectives) {
            put(objective.toMatrixNoName(), matrix, 0, j);
            j += oLen;
            try{
                for (int i = 0; i < oHeight; i++) {
                    matrix[i][j] = "   ";
                }
                j++;
            } catch (IndexOutOfBoundsException ignored) {}
        }

        return matrixToString(matrix);
    }
}
