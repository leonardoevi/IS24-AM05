package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.card.side.EmptyPlacedSide;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.enums.element.*;
import it.polimi.is24am05.model.playArea.PlayArea;

public enum LayoutMultiplier implements ObjectiveMultiplier{
    /**
     * The ResourceMultiplier enum declares the 8 Objective
     * multiplier which are characterized by their points
     * condition. Each condition can be represented in a
     * 3x3 matrix that describes the needed layout.
     */
    OM_087(new Resource[][]{new Resource[]{null, null, Resource.FUNGI},
                            new Resource[]{null, Resource.FUNGI, null},
                            new Resource[]{Resource.FUNGI, null, null}}),
    OM_088(new Resource[][]{new Resource[]{Resource.PLANT, null, null},
                            new Resource[]{null, Resource.PLANT, null},
                            new Resource[]{null, null, Resource.PLANT}}),
    OM_089(new Resource[][]{new Resource[]{null, null, Resource.ANIMAL},
                            new Resource[]{null, Resource.ANIMAL, null},
                            new Resource[]{Resource.ANIMAL, null, null}}),
    OM_090(new Resource[][]{new Resource[]{Resource.INSECT, null, null},
                            new Resource[]{null, Resource.INSECT, null},
                            new Resource[]{null, null, Resource.INSECT}}),
    OM_091(new Resource[][]{new Resource[]{Resource.FUNGI, null},
                            new Resource[]{null, null},
                            new Resource[]{Resource.FUNGI, null},
                            new Resource[]{null, Resource.PLANT}}),
    OM_092(new Resource[][]{new Resource[]{null, Resource.PLANT},
                            new Resource[]{null, null},
                            new Resource[]{null, Resource.PLANT},
                            new Resource[]{Resource.INSECT, null}}),
    OM_093(new Resource[][]{new Resource[]{null, Resource.FUNGI},
                            new Resource[]{Resource.ANIMAL, null},
                            new Resource[]{null, null},
                            new Resource[]{Resource.ANIMAL, null}}),
    OM_094(new Resource[][]{new Resource[]{Resource.ANIMAL, null},
                            new Resource[]{null, Resource.INSECT},
                            new Resource[]{null, null},
                            new Resource[]{null, Resource.INSECT}})
    ;
    /**
     * The resources attribute is the 3x3 matrix that describes the layout
     */
    private final Resource[][] layout;

    /**
     * Initializes a ResourceMultiplier
     * @param layout 3x3 matrix for Point Condition
     */
    LayoutMultiplier(Resource[][] layout) {
        this.layout = layout;
    }

    /**
     * Given the PlayerArea builds up a Resource matrix and then checks
     * how many times the points condition is satisfied
     * @param playArea PlayerArea
     * @return how many times the points condition is satisfied
     */
    @Override
    public int compute(PlayArea playArea) {
        PlacedSide[][] tmp = playArea.getMatrixPlayArea();
        Resource[][] res = new Resource[tmp.length][tmp[0].length];
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                if (tmp[i][j] != null && !(tmp[i][j] instanceof EmptyPlacedSide))
                    res[i][j] = tmp[i][j].getSide().getSeed();
            }
        }
        int result = 0;

        for (int i = 0; i <= res.length - layout.length; i++) {
            for (int j = 0; j <= res[0].length - layout[0].length; j++) {

                if (matches(res, layout, i, j)){
                    //Once a placing condition is satisfied all the values in the bigger matrix
                    //matching with the layout are set to null to avoid duplicates

                    for (int k = i; k < i + layout.length; k++) {
                        for (int l = j; l < j + layout[0].length; l++) {
                            if(layout[k-i][l-j]!= null) {
                                res[k][l] = null;
                            }
                        }
                    }
                    result+=1;
                }

            }

        }
        return result;
    }

    /**
     * Support function for compute() method
     * @param res The PlayerArea matrix
     * @param resources The 3x3 matrix
     * @param startRow The row of the bigger matrix from where it starts checking
     * @param startCol The column of the bigger matrix from where it starts checking
     * @return true if the 2 matrices are equal, false otherwise
     */
    private static boolean matches(Resource[][] res, Resource[][] resources, int startRow, int startCol){

        for (int i = 0; i < resources.length; i++) {
            for (int j = 0; j < resources[0].length; j++) {
                if (resources[i][j] != null){
                    if (res[startRow + i][startCol + j] != resources[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Resource[][] getLayout() {
        return layout;
    }
}
