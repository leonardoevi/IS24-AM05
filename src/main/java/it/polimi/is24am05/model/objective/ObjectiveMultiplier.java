package it.polimi.is24am05.model.objective;


import it.polimi.is24am05.model.playArea.PlayArea;

public interface ObjectiveMultiplier {

    /**
     * param Player P passes  his PlayerArea to compute how many points the objective card gives
     * @return how many times the objective card's points condition is satisfied by the PlayerArea
     */
    int compute(PlayArea playArea);
}
