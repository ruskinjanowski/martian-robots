package com.redbadger.martianrobots;

import com.redbadger.martianrobots.domain.Mission;
import com.redbadger.martianrobots.domain.World;
import com.redbadger.martianrobots.io.Input;
import com.redbadger.martianrobots.io.InputException;
import com.redbadger.martianrobots.io.InputParser;
import com.redbadger.martianrobots.io.OutputFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * Command-line entry point. Reads the input from the file named by the first argument, or
 * from standard input when no argument is given, and prints one line per robot.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.exit(run(args, System.in, System.out, System.err));
    }

    static int run(String[] args, InputStream stdin, PrintStream out, PrintStream err) {
        if (args.length > 1) {
            err.println("Usage: martian-robots [input-file]");
            err.println("Reads from standard input when no file is given.");
            return 2;
        }
        String text;
        try {
            text = args.length == 1 ? Files.readString(Path.of(args[0])) : readAll(stdin);
        } catch (NoSuchFileException e) {
            err.println("Input file not found: " + e.getFile());
            return 2;
        } catch (IOException e) {
            err.println("Could not read input: " + e.getMessage());
            return 2;
        }

        try {
            Input input = InputParser.parse(text);
            World world = input.world();
            for (Mission mission : input.missions()) {
                world.run(mission);
            }
            out.println(OutputFormatter.format(world.finishedRobots()));
            return 0;
        } catch (InputException e) {
            err.println("Invalid input: " + e.getMessage());
            return 1;
        }
    }

    private static String readAll(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
}
