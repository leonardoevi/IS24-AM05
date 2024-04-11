package it.polimi.is24am05.model.objective;

import org.junit.jupiter.api.Test;

import static it.polimi.is24am05.model.Player.HandDisplayer.matrixToString;
import static it.polimi.is24am05.model.objective.ObjectiveDisplayer.layoutObjectiveToString;

class ObjectiveDisplayerTest {
    @Test
    void LayoutObjectiveTest(){
        for(Objective objective : Objective.values()){
            if(! (objective.getMultiplier() instanceof LayoutMultiplier) )
                continue;
            System.out.println(objective);
            System.out.println(matrixToString(layoutObjectiveToString(objective)));
        }
    }

}