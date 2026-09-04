package com.redbadger.martianrobots;

/**
 * An instruction a robot can carry out. Each constant knows its input symbol and how it
 * changes a robot; adding a new command means adding a constant here.
 */
public enum Command {
    L('L') {
        @Override
        Robot apply(Robot robot, World world) {
            return robot.facing(robot.orientation().left());
        }
    },
    R('R') {
        @Override
        Robot apply(Robot robot, World world) {
            return robot.facing(robot.orientation().right());
        }
    },
    F('F') {
        @Override
        Robot apply(Robot robot, World world) {
            Position next = robot.position().step(robot.orientation());
            if (world.contains(next)) {
                return robot.at(next);
            }
            if (world.hasScent(robot.position())) {
                return robot;
            }
            return robot.markLost();
        }
    };

    private final char symbol;

    Command(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }

    abstract Robot apply(Robot robot, World world);

    public static Command fromSymbol(char symbol) {
        for (Command command : values()) {
            if (command.symbol == symbol) {
                return command;
            }
        }
        throw new IllegalArgumentException("Unknown command: " + symbol);
    }
}
