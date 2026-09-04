package com.redbadger.martianrobots.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redbadger.martianrobots.domain.Command;
import com.redbadger.martianrobots.domain.Mission;
import com.redbadger.martianrobots.domain.Orientation;
import com.redbadger.martianrobots.domain.Position;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputParserTest {

    @Test
    void parsesTheSampleInput() {
        Input input = InputParser.parse("""
                5 3
                1 1 E
                RFRFRFRF

                3 2 N
                FRRFLLFFRRFLL

                0 3 W
                LLFFFLFLFL
                """);

        assertEquals(5, input.world().maxX());
        assertEquals(3, input.world().maxY());
        assertEquals(3, input.missions().size());

        Mission first = input.missions().getFirst();
        assertEquals(new Position(1, 1), first.start());
        assertEquals(Orientation.E, first.orientation());
        assertEquals(List.of(Command.R, Command.F, Command.R, Command.F,
                Command.R, Command.F, Command.R, Command.F), first.commands());

        Mission last = input.missions().getLast();
        assertEquals(new Position(0, 3), last.start());
        assertEquals(Orientation.W, last.orientation());
        assertEquals(10, last.commands().size());
    }

    @Test
    void ignoresBlankLinesAndSurroundingWhitespace() {
        Input input = InputParser.parse("""


                  5 3

                1 1 E

                RFRFRFRF


                """);
        assertEquals(1, input.missions().size());
        assertEquals(new Position(1, 1), input.missions().getFirst().start());
    }

    @Test
    void parsesASingleInstruction() {
        Input input = InputParser.parse("5 3\n1 1 E\nF\n");
        assertEquals(List.of(Command.F), input.missions().getFirst().commands());
    }

    @Test
    void acceptsAWorldWithNoRobots() {
        Input input = InputParser.parse("5 3\n");
        assertTrue(input.missions().isEmpty());
    }

    @Test
    void acceptsTheMaximumCoordinate() {
        Input input = InputParser.parse("50 50\n50 50 N\nF\n");
        assertEquals(new Position(50, 50), input.missions().getFirst().start());
    }

    @Test
    void acceptsNinetyNineInstructions() {
        Input input = InputParser.parse("5 3\n1 1 E\n" + "F".repeat(99) + "\n");
        assertEquals(99, input.missions().getFirst().commands().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "5\n",
            "5 3 1\n",
            "x 3\n",
            "51 3\n",
            "5 3\n1 1\n",
            "5 3\n1 1 E\n",
            "5 3\n1 1 X\nF\n",
            "5 3\n1 x E\nF\n",
            "5 3\n51 1 E\nF\n",
            "5 3\n-1 1 E\nF\n",
            "5 3\n1 1 E\nFXF\n",
    })
    void rejectsMalformedInput(String text) {
        assertThrows(InputException.class, () -> InputParser.parse(text));
    }

    @Test
    void rejectsInstructionStringsOfOneHundredOrMore() {
        String text = "5 3\n1 1 E\n" + "F".repeat(100) + "\n";
        InputException e = assertThrows(InputException.class, () -> InputParser.parse(text));
        assertTrue(e.getMessage().contains("under 100"));
    }

    @Test
    void errorMessagesNameTheOffendingLine() {
        InputException e = assertThrows(InputException.class,
                () -> InputParser.parse("5 3\n1 1 E\nFXF\n"));
        assertTrue(e.getMessage().contains("FXF"), e.getMessage());
    }
}
