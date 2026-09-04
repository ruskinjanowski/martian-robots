package com.redbadger.martianrobots;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SimulationTest {

    @Test
    void runsTheSampleFromTheSpec() {
        Input input = InputParser.parse("""
                5 3
                1 1 E
                RFRFRFRF

                3 2 N
                FRRFLLFFRRFLL

                0 3 W
                LLFFFLFLFL
                """);

        List<Robot> robots = Simulation.run(input);

        assertEquals(List.of(
                new Robot(new Position(1, 1), Orientation.E, false),
                new Robot(new Position(3, 3), Orientation.N, true),
                new Robot(new Position(2, 3), Orientation.S, false)),
                robots);
    }
}
