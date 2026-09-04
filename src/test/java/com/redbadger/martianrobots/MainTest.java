package com.redbadger.martianrobots;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    private final ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errBytes = new ByteArrayOutputStream();
    private final PrintStream out = new PrintStream(outBytes, true, StandardCharsets.UTF_8);
    private final PrintStream err = new PrintStream(errBytes, true, StandardCharsets.UTF_8);

    private static String resource(String name) throws IOException {
        try (InputStream in = MainTest.class.getResourceAsStream("/" + name)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String stdout() {
        return outBytes.toString(StandardCharsets.UTF_8);
    }

    private String stderr() {
        return errBytes.toString(StandardCharsets.UTF_8);
    }

    @Test
    void readsFromStdinWhenNoArgumentIsGiven() throws IOException {
        InputStream stdin = new ByteArrayInputStream(resource("sample-input.txt").getBytes(StandardCharsets.UTF_8));

        int exit = Main.run(new String[0], stdin, out, err);

        assertEquals(0, exit);
        assertEquals(resource("sample-output.txt"), stdout());
        assertEquals("", stderr());
    }

    @Test
    void readsFromFileWhenAPathIsGiven(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("input.txt");
        Files.writeString(file, resource("sample-input.txt"));

        int exit = Main.run(new String[] {file.toString()}, InputStream.nullInputStream(), out, err);

        assertEquals(0, exit);
        assertEquals(resource("sample-output.txt"), stdout());
    }

    @Test
    void reportsMissingFile(@TempDir Path dir) {
        int exit = Main.run(new String[] {dir.resolve("missing.txt").toString()},
                InputStream.nullInputStream(), out, err);

        assertEquals(2, exit);
        assertTrue(stderr().contains("not found"), stderr());
        assertEquals("", stdout());
    }

    @Test
    void reportsInvalidInputOnStderrWithExitCodeOne() {
        InputStream stdin = new ByteArrayInputStream("5 3\n1 1 E\nFXF\n".getBytes(StandardCharsets.UTF_8));

        int exit = Main.run(new String[0], stdin, out, err);

        assertEquals(1, exit);
        assertTrue(stderr().startsWith("Invalid input:"), stderr());
        assertTrue(stderr().contains("FXF"), stderr());
        assertEquals("", stdout());
    }

    @Test
    void rejectsTooManyArguments() {
        int exit = Main.run(new String[] {"a", "b"}, InputStream.nullInputStream(), out, err);

        assertEquals(2, exit);
        assertTrue(stderr().contains("Usage"), stderr());
    }
}
