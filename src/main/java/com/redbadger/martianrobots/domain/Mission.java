package com.redbadger.martianrobots.domain;

import java.util.List;

/**
 * One robot's landing site and the commands it has been sent. The spec caps an instruction string
 * at under 100 characters, so a mission carrying more commands than that cannot be built.
 */
public record Mission(Position start, Orientation orientation, List<Command> commands) {

    public static final int MAX_COMMANDS = 99;

    public Mission {
        commands = List.copyOf(commands);
        if (commands.size() > MAX_COMMANDS) {
            throw new IllegalArgumentException(
                    "Instruction string must be under 100 characters, got " + commands.size());
        }
    }
}
