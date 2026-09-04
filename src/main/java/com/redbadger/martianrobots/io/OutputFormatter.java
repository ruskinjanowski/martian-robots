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

    public static String format(List<Robot> robots) {
        return robots.stream()
                .map(OutputFormatter::format)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
