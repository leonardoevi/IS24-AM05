package it.polimi.is24am05.model.objective;

import org.junit.jupiter.api.Test;

import static it.polimi.is24am05.model.Player.HandDisplayer.matrixToString;
import static org.junit.jupiter.api.Assertions.*;

class ObjectiveTest {

    @Test
    void print() {
        for(Objective objective : Objective.values()) {
            System.out.println(matrixToString(objective.toMatrix()));
        }
    }

    @Test
    void toStringTest(){
        for(Objective objective : Objective.values()) {
            System.out.println(objective.toString());
        }
    }

}