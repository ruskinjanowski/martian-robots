package com.redbadger.martianrobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrientationTest {

    @ParameterizedTest
    @CsvSource({"N,W", "W,S", "S,E", "E,N"})
    void turningLeftGoesAnticlockwise(Orientation from, Orientation expected) {
        assertEquals(expected, from.left());
    }

    @ParameterizedTest
    @CsvSource({"N,E", "E,S", "S,W", "W,N"})
    void turningRightGoesClockwise(Orientation from, Orientation expected) {
        assertEquals(expected, from.right());
    }

    @ParameterizedTest
    @CsvSource({"N,0,1", "E,1,0", "S,0,-1", "W,-1,0"})
    void eachHeadingHasAUnitDelta(Orientation orientation, int dx, int dy) {
        assertEquals(dx, orientation.dx());
        assertEquals(dy, orientation.dy());
    }

    @ParameterizedTest
    @CsvSource({"N,N", "E,E", "S,S", "W,W"})
    void parsesFromSymbol(char symbol, Orientation expected) {
        assertEquals(expected, Orientation.fromSymbol(symbol));
    }

    @org.junit.jupiter.api.Test
    void rejectsUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> Orientation.fromSymbol('X'));
    }
}
