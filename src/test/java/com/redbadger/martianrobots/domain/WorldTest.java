package com.redbadger.martianrobots.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redbadger.martianrobots.io.Input;
import com.redbadger.martianrobots.io.InputParser;
import com.redbadger.martianrobots.io.OutputFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class WorldTest {

    private static final Position CORNER = new Position(5, 3);

    private final World world = new World(5, 3);

    private Robot run(Position start, Orientation orientation, String instructions) {
        List<Command> commands = instructions.chars()
                .mapToObj(c -> Command.valueOf(String.valueOf((char) c)))
                .toList();
        return world.run(new Mission(start, orientation, commands));
    }

    private static void assertAt(Robot robot, int x, int y, Orientation orientation) {
        assertEquals(new Position(x, y), robot.position());
        assertEquals(orientation, robot.orientation());
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
    void cannotLandOffTheGrid() {
        assertThrows(IllegalArgumentException.class,
                () -> world.run(new Mission(new Position(6, 0), Orientation.N, List.of())));
    }

    @ParameterizedTest
    @EnumSource(Command.class)
    void everyCommandIsDispatchedAndLeavesTheRobotOnTheGrid(Command command) {
        Robot robot = world.run(new Mission(new Position(1, 1), Orientation.N, List.of(command)));
        assertFalse(robot.lost());
        assertTrue(world.contains(robot.position()));
    }

    @Test
    void turnsInPlaceWithoutMoving() {
        assertAt(run(new Position(1, 1), Orientation.N, "LL"), 1, 1, Orientation.S);
        assertAt(run(new Position(1, 1), Orientation.N, "RRR"), 1, 1, Orientation.W);
    }

    @Test
    void forwardMovesOneSquareWhenTheTargetIsOnTheGrid() {
        Robot robot = run(new Position(1, 1), Orientation.E, "F");
        assertAt(robot, 2, 1, Orientation.E);
        assertFalse(robot.lost());
    }

    @Test
    void recordsRobotsInTheOrderTheyRan() {
        run(new Position(1, 1), Orientation.E, "RFRFRFRF");
        run(new Position(0, 0), Orientation.N, "F");
        List<Robot> finished = world.finishedRobots();
        assertEquals(2, finished.size());
        assertAt(finished.getFirst(), 1, 1, Orientation.E);
        assertAt(finished.get(1), 0, 1, Orientation.N);
    }

    @Test
    void movingOffTheGridLosesTheRobotAndLeavesAScent() {
        Robot lost = run(CORNER, Orientation.N, "F");
        assertTrue(lost.lost());
        assertEquals(CORNER, lost.position());
        assertTrue(world.hasScent(CORNER));
    }

    @Test
    void commandsAfterLossAreIgnored() {
        Robot lost = run(CORNER, Orientation.N, "FLFFF");
        assertAt(lost, 5, 3, Orientation.N);
    }

    @Test
    void scentStopsLaterRobotsFallingOffTheSameSquare() {
        run(CORNER, Orientation.N, "F");
        Robot survivor = run(CORNER, Orientation.N, "FRF");
        assertFalse(survivor.lost());
        assertAt(survivor, 5, 3, Orientation.E);
    }

    @Test
    void scentOnlyProtectsTheSquareItWasLeftOn() {
        run(CORNER, Orientation.N, "F");
        assertTrue(run(new Position(4, 3), Orientation.N, "F").lost());
    }

    @Test
    void scentStopsFallingInAnyDirectionFromThatSquare() {
        run(CORNER, Orientation.N, "F");
        assertFalse(run(CORNER, Orientation.E, "F").lost());
    }

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
        World world = input.world();
        input.missions().forEach(world::run);

        assertEquals("""
                1 1 E
                3 3 N LOST
                2 3 S
                """, OutputFormatter.format(world.finishedRobots()));
    }
}
