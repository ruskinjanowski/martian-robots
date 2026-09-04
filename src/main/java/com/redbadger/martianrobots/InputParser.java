package com.redbadger.martianrobots;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns the text format from the challenge into an {@link Input}. The first non-blank line is
 * the grid's upper-right corner; every following pair of non-blank lines is a robot's start
 * position and its instruction string. Blank lines anywhere are ignored.
 */
public final class InputParser {

    public static final int MAX_INSTRUCTION_LENGTH = 99;

    private InputParser() {
    }

    public static Input parse(String text) {
        List<String> lines = text.lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty())
                .toList();
        if (lines.isEmpty()) {
            throw new InputException("Input is empty; expected a grid size on the first line");
        }
        World world = parseWorld(lines.getFirst());

        List<Mission> missions = new ArrayList<>();
        for (int i = 1; i < lines.size(); i += 2) {
            if (i + 1 >= lines.size()) {
                throw new InputException("Robot on line '" + lines.get(i) + "' has no instruction line");
            }
            missions.add(parseMission(lines.get(i), lines.get(i + 1)));
        }
        return new Input(world, missions);
    }

    private static World parseWorld(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length != 2) {
            throw new InputException("Grid line must be two integers, got '" + line + "'");
        }
        try {
            return new World(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            throw new InputException("Grid line must be two integers, got '" + line + "'");
        } catch (IllegalArgumentException e) {
            throw new InputException(e.getMessage());
        }
    }

    private static Mission parseMission(String positionLine, String instructionLine) {
        String[] parts = positionLine.split("\\s+");
        if (parts.length != 3 || parts[2].length() != 1) {
            throw new InputException(
                    "Robot position must be 'x y orientation', got '" + positionLine + "'");
        }
        Position start;
        Orientation orientation;
        try {
            start = new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            orientation = Orientation.fromSymbol(parts[2].charAt(0));
        } catch (NumberFormatException e) {
            throw new InputException(
                    "Robot position must be 'x y orientation', got '" + positionLine + "'");
        } catch (IllegalArgumentException e) {
            throw new InputException(e.getMessage() + " in '" + positionLine + "'");
        }
        if (start.x() > World.MAX_COORDINATE || start.y() > World.MAX_COORDINATE
                || start.x() < 0 || start.y() < 0) {
            throw new InputException(
                    "Robot position must be within 0.." + World.MAX_COORDINATE + ", got '" + positionLine + "'");
        }

        if (instructionLine.length() > MAX_INSTRUCTION_LENGTH) {
            throw new InputException("Instruction string must be under 100 characters, got "
                    + instructionLine.length() + " in '" + instructionLine + "'");
        }
        List<Command> commands = new ArrayList<>(instructionLine.length());
        for (char symbol : instructionLine.toCharArray()) {
            try {
                commands.add(Command.fromSymbol(symbol));
            } catch (IllegalArgumentException e) {
                throw new InputException(e.getMessage() + " in '" + instructionLine + "'");
            }
        }
        return new Mission(start, orientation, commands);
    }
}
