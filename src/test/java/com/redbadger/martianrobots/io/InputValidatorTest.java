package com.redbadger.martianrobots.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redbadger.martianrobots.domain.Command;
import com.redbadger.martianrobots.domain.Mission;
import com.redbadger.martianrobots.domain.Orientation;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.World;
import java.util.List;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

    private static Input input(Position start) {
        return new Input(new World(5, 3), List.of(new Mission(start, Orientation.N, List.of(Command.F))));
    }

    @Test
    void acceptsALandingSquareOnTheGrid() {
        assertDoesNotThrow(() -> InputValidator.validate(input(new Position(5, 3))));
    }

    @Test
    void acceptsAWorldWithNoRobots() {
        assertDoesNotThrow(() -> InputValidator.validate(new Input(new World(5, 3), List.of())));
    }

    @Test
    void rejectsALandingSquareOutsideTheGridEvenWhenWithinTheMaximum() {
        InputException e = assertThrows(InputException.class,
                () -> InputValidator.validate(input(new Position(50, 1))));
        assertTrue(e.getMessage().contains("outside the 5 3 grid"), e.getMessage());
    }

    @Test
    void rejectsANegativeLandingSquare() {
        assertThrows(InputException.class, () -> InputValidator.validate(input(new Position(-1, 0))));
    }

    @Test
    void checksEveryMissionNotJustTheFirst() {
        Input input = new Input(new World(5, 3), List.of(
                new Mission(new Position(1, 1), Orientation.N, List.of(Command.F)),
                new Mission(new Position(9, 9), Orientation.N, List.of(Command.F))));
        assertThrows(InputException.class, () -> InputValidator.validate(input));
    }
}
