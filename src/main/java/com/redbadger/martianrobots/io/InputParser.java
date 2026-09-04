package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Command;
import com.redbadger.martianrobots.domain.Mission;
import com.redbadger.martianrobots.domain.Orientation;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.World;
import java.util.ArrayList;
import java.util.List;

/**
 * Turns the text format from the challenge into an {@link Input}. This is the syntax layer: it
 * checks the shape of the text — line pairing, token counts, integers, known symbols — and builds
 * the objects. The spec's limits are not checked here; {@link World} and {@link Mission} enforce
 * their own, and {@link InputValidator} covers what needs both.
 *
 * <p>The first non-blank line is the grid's upper-right corner; every following pair of non-blank
 * lines is a robot's start position and its instruction string. Blank lines anywhere are ignored.
 */
public final class InputParser {

    private InputParser() {
    }

    public static Input parse(String text) {
        Input input = read(text);
        InputValidator.validate(input);
        return input;
    }

    private static Input read(String text) {
        List<String> lines = text.lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty())
                .toList();
        if (lines.isEmpty()) {
            throw new InputException("Input is empty; expected a grid size on the first line");
        }
        World world = readWorld(lines.getFirst());

        List<Mission> missions = new ArrayList<>();
        for (int i = 1; i < lines.size(); i += 2) {
            if (i + 1 >= lines.size()) {
                throw new InputException("Robot on line '" + lines.get(i) + "' has no instruction line");
            }
            missions.add(readMission(lines.get(i), lines.get(i + 1)));
        }
        return new Input(world, missions);
    }

    private static World readWorld(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length != 2) {
            throw new InputException("Grid line must be two integers, got '" + line + "'");
        }
        try {
            return new World(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            throw new InputException("Grid line must be two integers, got '" + line + "'");
        } catch (IllegalArgumentException e) {
            throw new InputException(e.getMessage() + " in '" + line + "'");
        }
    }

    private static Mission readMission(String positionLine, String instructionLine) {
        String[] parts = positionLine.split("\\s+");
        if (parts.length != 3 || parts[2].length() != 1) {
            throw new InputException("Robot position must be 'x y orientation', got '" + positionLine + "'");
        }
        Position start = readPosition(parts, positionLine);
        Orientation orientation = readOrientation(parts[2].charAt(0), positionLine);

        List<Command> commands = new ArrayList<>(instructionLine.length());
        for (char symbol : instructionLine.toCharArray()) {
            commands.add(readCommand(symbol, instructionLine));
        }
        try {
            return new Mission(start, orientation, commands);
        } catch (IllegalArgumentException e) {
            throw new InputException(e.getMessage() + " in '" + instructionLine + "'");
        }
    }

    private static Position readPosition(String[] parts, String line) {
        try {
            return new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            throw new InputException("Robot position must be 'x y orientation', got '" + line + "'");
        }
    }

    private static Orientation readOrientation(char symbol, String line) {
        try {
            return Orientation.valueOf(String.valueOf(symbol));
        } catch (IllegalArgumentException e) {
            throw new InputException("Unknown orientation: " + symbol + " in '" + line + "'");
        }
    }

    private static Command readCommand(char symbol, String line) {
        try {
            return Command.valueOf(String.valueOf(symbol));
        } catch (IllegalArgumentException e) {
            throw new InputException("Unknown command: " + symbol + " in '" + line + "'");
        }
    }
}
