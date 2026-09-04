package com.redbadger.martianrobots.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RobotTest {

    private final Robot robot = new Robot(new Position(1, 1), Orientation.N);

    @Test
    void landsFacingTheGivenHeadingAndNotLost() {
        assertEquals(new Position(1, 1), robot.position());
        assertEquals(Orientation.N, robot.orientation());
        assertFalse(robot.lost());
    }

    @Test
    void turningLeftRotatesInPlace() {
        robot.turnLeft();
        assertEquals(Orientation.W, robot.orientation());
        assertEquals(new Position(1, 1), robot.position());
    }

    @Test
    void turningRightRotatesInPlace() {
        robot.turnRight();
        assertEquals(Orientation.E, robot.orientation());
        assertEquals(new Position(1, 1), robot.position());
    }

    @Test
    void nextPositionIsOneStepAheadWithoutMoving() {
        assertEquals(new Position(1, 2), robot.nextPosition());
        assertEquals(new Position(1, 1), robot.position());
    }

    @Test
    void nextPositionFollowsTheCurrentHeading() {
        robot.turnRight();
        assertEquals(new Position(2, 1), robot.nextPosition());
    }

    @Test
    void movingKeepsTheHeading() {
        robot.moveTo(robot.nextPosition());
        assertEquals(new Position(1, 2), robot.position());
        assertEquals(Orientation.N, robot.orientation());
    }

    @Test
    void markLostSetsTheFlagAndLeavesPositionAlone() {
        robot.markLost();
        assertTrue(robot.lost());
        assertEquals(new Position(1, 1), robot.position());
        assertEquals(Orientation.N, robot.orientation());
    }
}
