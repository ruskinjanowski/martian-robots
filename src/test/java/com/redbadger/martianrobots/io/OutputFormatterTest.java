package com.redbadger.martianrobots.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.redbadger.martianrobots.domain.Orientation;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.Robot;
import java.util.List;
import org.junit.jupiter.api.Test;

class OutputFormatterTest {

    private static Robot lost(Position position, Orientation orientation) {
        Robot robot = new Robot(position, orientation);
        robot.markLost();
        return robot;
    }

    @Test
    void formatsASurvivingRobot() {
        assertEquals("1 1 E", OutputFormatter.format(new Robot(new Position(1, 1), Orientation.E)));
    }

    @Test
    void formatsALostRobotWithSuffix() {
        assertEquals("3 3 N LOST", OutputFormatter.format(lost(new Position(3, 3), Orientation.N)));
    }

    @Test
    void joinsRobotsOnePerLine() {
        List<Robot> robots = List.of(
                new Robot(new Position(1, 1), Orientation.E),
                lost(new Position(3, 3), Orientation.N));
        assertEquals("1 1 E" + System.lineSeparator() + "3 3 N LOST", OutputFormatter.format(robots));
    }

    @Test
    void formatsNoRobotsAsEmptyString() {
        assertEquals("", OutputFormatter.format(List.of()));
    }
}
