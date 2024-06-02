package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.playArea.PlayArea;

import static it.polimi.is24am05.model.Player.HandDisplayer.matrixToString;
import static it.polimi.is24am05.model.playArea.SideDisplayer.*;

public enum Objective {
    /**
     * The Objective enum declares the 16 objective cards needed for the game
     */
    O_087(2, LayoutMultiplier.OM_087),
    O_088(2, LayoutMultiplier.OM_088),
    O_089(2, LayoutMultiplier.OM_089),
    O_090(2, LayoutMultiplier.OM_090),
    O_091(3, LayoutMultiplier.OM_091),
    O_092(3, LayoutMultiplier.OM_092),
    O_093(3, LayoutMultiplier.OM_093),
    O_094(3, LayoutMultiplier.OM_094),

    O_095(2, ItemMultiplier.IM_095),
    O_096(2, ItemMultiplier.IM_096),
    O_097(2, ItemMultiplier.IM_097),
    O_098(2, ItemMultiplier.IM_098),

    O_099(3, ItemMultiplier.IM_099),

    O_100(2, ItemMultiplier.IM_100),
    O_101(2, ItemMultiplier.IM_101),
    O_102(2, ItemMultiplier.IM_102);

    /**
     * The attribute points describes how many point each card gives you
     * and the attribute multiplier calculates how many times the placing condition
     * is true
     */
    private final int points;
    private final ObjectiveMultiplier multiplier;

    /**
     * Initializes an objective card
     * @param points Card points
     * @param multiplier Point condition
     */
    Objective(int points, ObjectiveMultiplier multiplier) {
        this.points = points;
        this.multiplier = multiplier;
    }

    /**
     * Evaluates how many points a player made with his PlayerArea
     * @param playArea The PlayerArea
     * @return The card points multiplied by how many times the placing condition is satisfied
     */
    public int evaluate(PlayArea playArea){
        return points * multiplier.compute(playArea);
    }

    public int getPoints() {
        return points;
    }

    public ObjectiveMultiplier getMultiplier() {
        return multiplier;
    }

    public String[][] toMatrix(){
        // TODO : add colors
        String[][] matrix = new String[5][3];

        matrix[0][0] = " "; matrix[0][1] = "  ⌟" + getPoints() + "⌞  "; matrix[0][2] = " ";
        matrix[1][0] = matrix[1][2] = matrix[3][0] = matrix[3][2] = "+";
        matrix[2][0] = matrix[2][2] = "|";
        matrix[4][0] = matrix[4][2] = " "; matrix[4][1] = " " + this.name() + " ";

        String f = RED_BOLD + "F" + RESET;
        String p = GREEN_BOLD + "P" + RESET;
        String a = CYAN_BOLD + "A" + RESET;
        String i = PURPLE_BOLD + "P" + RESET;
        switch (this){
            case O_087:
                matrix[1][1] = "----" + f + "--";
                matrix[2][1] = "   " + f + "   ";
                matrix[3][1] = "--" + f + "----";
                break;
            case O_088:
                matrix[1][1] = "--" + p + "----";
                matrix[2][1] = "   " + p + "   ";
                matrix[3][1] = "----" + p + "--";
                break;
            case O_089:
                matrix[1][1] = "----" + a + "--";
                matrix[2][1] = "   " + a + "   ";
                matrix[3][1] = "--" + a + "----";
                break;
            case O_090:
                matrix[1][1] = "--" + i + "----";
                matrix[2][1] = "   " + i + "   ";
                matrix[3][1] = "----" + i + "--";
                break;
            case O_091:
                matrix[1][1] = "---" + f + "---";
                matrix[2][1] = "   " + f + "   ";
                matrix[3][1] = "----" + p + "--";
                break;
            case O_092:
                matrix[1][1] = "---" + p + "---";
                matrix[2][1] = "   " + p + "   ";
                matrix[3][1] = "--" + i + "----";
                break;
            case O_093:
                matrix[1][1] = "----" + f + "--";
                matrix[2][1] = "   " + a + "   ";
                matrix[3][1] = "---" + a + "---";
                break;
            case O_094:
                matrix[1][1] = "--" + a + "----";
                matrix[2][1] = "   " + i + "   ";
                matrix[3][1] = "---" + i + "---";
                break;
            case O_095:
                matrix[1][1] = "-------";
                matrix[2][1] = " F F F ";
                matrix[3][1] = "-------";
                break;
            case O_096:
                matrix[1][1] = "-------";
                matrix[2][1] = " P P P ";
                matrix[3][1] = "-------";
                break;
            case O_097:
                matrix[1][1] = "-------";
                matrix[2][1] = " A A A ";
                matrix[3][1] = "-------";
                break;
            case O_098:
                matrix[1][1] = "-------";
                matrix[2][1] = " I I I ";
                matrix[3][1] = "-------";
                break;
            case O_099:
                matrix[1][1] = "-------";
                matrix[2][1] = " Q I M ";
                matrix[3][1] = "-------";
                break;
            case O_100:
                matrix[1][1] = "-------";
                matrix[2][1] = "  M M  ";
                matrix[3][1] = "-------";
                break;
            case O_101:
                matrix[1][1] = "-------";
                matrix[2][1] = "  I I  ";
                matrix[3][1] = "-------";
                break;
            case O_102:
                matrix[1][1] = "-------";
                matrix[2][1] = "  Q Q  ";
                matrix[3][1] = "-------";
                break;
        }

        return matrix;
    }

    public String[][] toMatrixNoName(){
        // TODO : add colors and display each objective
        String[][] matrixNoName = new String[4][3];

        String[][] matrix = toMatrix();

        for (int i = 0; i < matrixNoName.length; i++)
            System.arraycopy(matrix[i], 0, matrixNoName[i], 0, matrixNoName[i].length);

        return matrixNoName;
    }

    @Override
    public String toString() {
        return matrixToString(this.toMatrixNoName());
    }
}
