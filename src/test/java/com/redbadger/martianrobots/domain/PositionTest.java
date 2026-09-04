package com.redbadger.martianrobots.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PositionTest {

    @ParameterizedTest
    @CsvSource({"N,2,4", "E,3,3", "S,2,2", "W,1,3"})
    void stepsOneSquareInTheGivenDirection(Orientation orientation, int x, int y) {
        assertEquals(new Position(x, y), new Position(2, 3).step(orientation));
    }

    @Test
    void steppingDoesNotMutateTheOriginal() {
        Position original = new Position(2, 3);
        original.step(Orientation.N);
        assertEquals(new Position(2, 3), original);
    }
}
