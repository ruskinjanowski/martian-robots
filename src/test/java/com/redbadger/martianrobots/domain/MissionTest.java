package com.redbadger.martianrobots.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MissionTest {

    private static Mission withCommands(int count) {
        return new Mission(new Position(0, 0), Orientation.N,
                Collections.nCopies(count, Command.F));
    }

    @Test
    void acceptsTheMaximumNumberOfCommands() {
        assertEquals(Mission.MAX_COMMANDS, withCommands(Mission.MAX_COMMANDS).commands().size());
    }

    @Test
    void rejectsMoreCommandsThanTheMaximum() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> withCommands(Mission.MAX_COMMANDS + 1));
        assertTrue(e.getMessage().contains("under 100"), e.getMessage());
    }

    @Test
    void copiesTheCommandsSoLaterEditsCannotChangeIt() {
        List<Command> commands = new ArrayList<>(List.of(Command.F));
        Mission mission = new Mission(new Position(0, 0), Orientation.N, commands);
        commands.add(Command.L);
        assertEquals(List.of(Command.F), mission.commands());
    }
}
