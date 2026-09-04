package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Robot;
import java.util.List;
import java.util.stream.Collectors;

/** Renders finished robots in the challenge's output format, one per line. */
public final class OutputFormatter {

    private OutputFormatter() {
    }

    public static String format(Robot robot) {
        String line = robot.position().x() + " " + robot.position().y() + " " + robot.orientation();
        return robot.lost() ? line + " LOST" : line;
    }

    /**
     * The whole report: one line per robot, each terminated by a newline, so a run with no robots
     * prints nothing at all. Newlines are always {@code \n} rather than the platform separator, so
     * the output is byte-identical everywhere and can be diffed against a fixture.
     */
    public static String format(List<Robot> robots) {
        return robots.stream()
                .map(robot -> format(robot) + "\n")
                .collect(Collectors.joining());
    }
}
