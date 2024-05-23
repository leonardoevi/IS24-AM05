package it.polimi.is24am05.model.objective;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class SharedObjectiveDisplayerTest {

    @Test
    void testToString() {
        int N = 100;
        for (int i = 0; i < N; i++) {
            generateObjList();
        }

    }

    void generateObjList(){
        List<Objective> sharedObjectives = Arrays.asList(Objective.values());
        Collections.shuffle(sharedObjectives);

        System.out.println(new SharedObjectiveDisplayer(sharedObjectives.stream().limit(ThreadLocalRandom.current().nextInt(0, Objective.values().length + 1)).toArray(Objective[]::new)));
    }
}