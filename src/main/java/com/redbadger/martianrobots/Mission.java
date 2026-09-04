package com.redbadger.martianrobots;

import java.util.List;

/** One robot's landing site and the commands it has been sent. */
public record Mission(Position start, Orientation orientation, List<Command> commands) {

    public Mission {
        commands = List.copyOf(commands);
    }
}
