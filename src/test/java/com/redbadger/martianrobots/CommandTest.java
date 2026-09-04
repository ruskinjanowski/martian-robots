package com.redbadger.martianrobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CommandTest {

    private final World world = new World(5, 3);

    @Test
    void leftTurnsAnticlockwise() {
        Robot robot = Robot.landed(new Position(1, 1), Orientation.N);
        assertEquals(Orientation.W, Command.L.apply(robot, world).orientation());
    }

    @Test
    void rightTurnsClockwise() {
        Robot robot = Robot.landed(new Position(1, 1), Orientation.N);
        assertEquals(Orientation.E, Command.R.apply(robot, world).orientation());
    }

    @Test
    void forwardMovesOneSquareWhenOnGrid() {
        Robot robot = Robot.landed(new Position(1, 1), Orientation.E);
        Robot moved = Command.F.apply(robot, world);
        assertEquals(new Position(2, 1), moved.position());
        assertFalse(moved.lost());
    }

    @Test
    void forwardOffTheGridLosesTheRobotAtItsLastPosition() {
        Robot robot = Robot.landed(new Position(5, 3), Orientation.N);
        Robot lost = Command.F.apply(robot, world);
        assertTrue(lost.lost());
        assertEquals(new Position(5, 3), lost.position());
        assertEquals(Orientation.N, lost.orientation());
    }

    @Test
    void forwardOffTheGridFromAScentedSquareIsIgnored() {
        World scented = new World(5, 3);
        scented.landRobot(new Position(5, 3), Orientation.N);
        scented.execute(Command.F);
        scented.retireRobot();

        Robot robot = Robot.landed(new Position(5, 3), Orientation.N);
        Robot result = Command.F.apply(robot, scented);
        assertEquals(robot, result);
    }

    @ParameterizedTest
    @CsvSource({"L,L", "R,R", "F,F"})
    void parsesFromSymbol(char symbol, Command expected) {
        assertEquals(expected, Command.fromSymbol(symbol));
    }

    @Test
    void rejectsUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> Command.fromSymbol('X'));
    }
}
