# Martian Robots

A solution to the Red Badger "Martian Robots" coding challenge in Java.

Robots land on a rectangular grid on Mars and follow instruction strings. A robot that
moves off the grid is lost, but it leaves a scent on the square it fell from, and later
robots refuse any instruction that would carry them off the grid from a scented square.

## Running

Input is read from a file given as the only argument, or from standard input when no
argument is given. Output is one line per robot on standard output.

### With Docker only

```bash
docker build -t martian-robots .
```

```bash
docker run -i --rm martian-robots < src/test/resources/sample-input.txt
```

The image builds and runs the full test suite before producing the runtime image, so a
successful `docker build` is also a green test run.

### With Java 21 or later, and Maven

```bash
mvn package
```

```bash
java -jar target/martian-robots.jar src/test/resources/sample-input.txt
```

Or pipe input in:

```bash
java -jar target/martian-robots.jar < src/test/resources/sample-input.txt
```

To run only the tests:

```bash
mvn test
```

## Input and output

```
5 3
1 1 E
RFRFRFRF

3 2 N
FRRFLLFFRRFLL

0 3 W
LLFFFLFLFL
```

produces

```
1 1 E
3 3 N LOST
2 3 S
```

## Approach

An object-oriented design was used to simulate the state of the world, and how the state of the
world can change is defined as functions. Additionally, the input processing and deserialization
were added as a separate package.

**Future command types.** The spec anticipates new commands. `Command` is deliberately kept as a
plain vocabulary of symbols with no behaviour of its own — what a command *does* depends on the
grid and the scents, which only the world knows, so `World.actionFor` maps each command to its
action. Adding a command is one new constant and one new case.

**Coding approach.** As most development workflows include AI I explicitly tried to show what
was AI generated and what I had input on. I let AI generate as much of the code as possible
and then reviewed the code. As can be seen there were a number of changes I made, to improve code
readability code structure and maintainability.
