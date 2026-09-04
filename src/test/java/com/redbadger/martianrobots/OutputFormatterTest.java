package com.redbadger.martianrobots;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class OutputFormatterTest {

    @Test
    void formatsASurvivingRobot() {
        Robot robot = Robot.landed(new Position(1, 1), Orientation.E);
        assertEquals("1 1 E", OutputFormatter.format(robot));
    }

    @Test
    void formatsALostRobotWithSuffix() {
        Robot robot = Robot.landed(new Position(3, 3), Orientation.N).markLost();
        assertEquals("3 3 N LOST", OutputFormatter.format(robot));
    }

    @Test
    void joinsRobotsOnePerLine() {
        List<Robot> robots = List.of(
                Robot.landed(new Position(1, 1), Orientation.E),
                Robot.landed(new Position(3, 3), Orientation.N).markLost());
        assertEquals("1 1 E" + System.lineSeparator() + "3 3 N LOST", OutputFormatter.format(robots));
    }

    @Test
    void formatsNoRobotsAsEmptyString() {
        assertEquals("", OutputFormatter.format(List.of()));
    }
}
