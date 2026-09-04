package com.redbadger.martianrobots.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MissionTest {

    @Test
    void copiesTheCommandsSoLaterEditsCannotChangeIt() {
        List<Command> commands = new ArrayList<>(List.of(Command.F));
        Mission mission = new Mission(new Position(0, 0), Orientation.N, commands);
        commands.add(Command.L);
        assertEquals(List.of(Command.F), mission.commands());
    }
}
