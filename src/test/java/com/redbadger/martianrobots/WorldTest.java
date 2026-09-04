package com.redbadger.martianrobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldTest {

    private static final Position CORNER = new Position(5, 3);

    private final World world = new World(5, 3);

    private void run(Position start, Orientation orientation, String instructions) {
        world.landRobot(start, orientation);
        for (char symbol : instructions.toCharArray()) {
            world.execute(Command.fromSymbol(symbol));
        }
        world.retireRobot();
    }

    @Test
    void containsSquaresFromOriginToUpperRightInclusive() {
        assertTrue(world.contains(new Position(0, 0)));
        assertTrue(world.contains(CORNER));
        assertFalse(world.contains(new Position(6, 3)));
        assertFalse(world.contains(new Position(5, 4)));
        assertFalse(world.contains(new Position(-1, 0)));
        assertFalse(world.contains(new Position(0, -1)));
    }

    @Test
    void rejectsGridsOutsideTheAllowedRange() {
        assertThrows(IllegalArgumentException.class, () -> new World(51, 0));
        assertThrows(IllegalArgumentException.class, () -> new World(0, 51));
        assertThrows(IllegalArgumentException.class, () -> new World(-1, 0));
    }

    @Test
    void landedRobotBecomesActive() {
        world.landRobot(new Position(1, 1), Orientation.E);
        assertEquals(Robot.landed(new Position(1, 1), Orientation.E), world.activeRobot().orElseThrow());
    }

    @Test
    void cannotLandWhileAnotherRobotIsActive() {
        world.landRobot(new Position(1, 1), Orientation.E);
        assertThrows(IllegalStateException.class, () -> world.landRobot(new Position(2, 2), Orientation.N));
    }

    @Test
    void cannotLandOffTheGrid() {
        assertThrows(IllegalArgumentException.class, () -> world.landRobot(new Position(6, 0), Orientation.N));
    }

    @Test
    void commandsRequireAnActiveRobot() {
        assertThrows(IllegalStateException.class, () -> world.execute(Command.F));
        assertThrows(IllegalStateException.class, world::retireRobot);
    }

    @Test
    void retiringRecordsRobotsInOrder() {
        run(new Position(1, 1), Orientation.E, "RFRFRFRF");
        run(new Position(0, 0), Orientation.N, "F");
        assertEquals(List.of(
                Robot.landed(new Position(1, 1), Orientation.E),
                Robot.landed(new Position(0, 1), Orientation.N)),
                world.finishedRobots());
        assertTrue(world.activeRobot().isEmpty());
    }

    @Test
    void movingOffTheGridLosesTheRobotAndLeavesAScent() {
        run(CORNER, Orientation.N, "F");
        Robot lost = world.finishedRobots().getFirst();
        assertTrue(lost.lost());
        assertEquals(CORNER, lost.position());
        assertTrue(world.hasScent(CORNER));
    }

    @Test
    void commandsAfterLossAreIgnored() {
        run(CORNER, Orientation.N, "FLFFF");
        Robot lost = world.finishedRobots().getFirst();
        assertEquals(CORNER, lost.position());
        assertEquals(Orientation.N, lost.orientation());
    }

    @Test
    void scentStopsLaterRobotsFallingOffTheSameSquare() {
        run(CORNER, Orientation.N, "F");
        run(CORNER, Orientation.N, "FRF");
        Robot survivor = world.finishedRobots().get(1);
        assertFalse(survivor.lost());
        assertEquals(new Position(5, 3), survivor.position());
        assertEquals(Orientation.E, survivor.orientation());
    }

    @Test
    void scentOnlyProtectsTheSquareItWasLeftOn() {
        run(CORNER, Orientation.N, "F");
        run(new Position(4, 3), Orientation.N, "F");
        assertTrue(world.finishedRobots().get(1).lost());
    }

    @Test
    void scentStopsFallingInAnyDirectionFromThatSquare() {
        run(CORNER, Orientation.N, "F");
        run(CORNER, Orientation.E, "F");
        assertFalse(world.finishedRobots().get(1).lost());
    }
}
