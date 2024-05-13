package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.playArea.PlayArea;

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
    public String[][] toMatrix()
    {
        return ObjectiveDisplayer.layoutObjectiveToString(Objective.this);
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(String[] row : toMatrix()){
            for (String s : row){
                sb.append(s);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
