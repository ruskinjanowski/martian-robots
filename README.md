# Martian Robots

A solution to the Red Badger "Martian Robots" coding challenge in Java 25.

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

### With Java 25 and Maven

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

Invalid input is reported on standard error with exit code 1. A missing file or bad
arguments give exit code 2.

## Design

All classes live in `com.redbadger.martianrobots`.

- **`World`** is the single mutable object. It holds the grid bounds, the robot currently
  taking commands, the ordered list of finished robots, and the set of scented squares.
  Its API is `landRobot`, `execute`, and `retireRobot`. Only one robot is active at a
  time, matching the spec's rule that robots run sequentially.
- **`Robot`**, **`Position`**, and **`Orientation`** are immutable values. Every change
  to a robot produces a new instance.
- **`Command`** is an enum whose constants each implement `apply(Robot, World)`. `F` asks
  the world whether the target square is on the grid and whether the current square is
  scented. Adding a new command means adding one constant.
- **`InputParser`** validates the text format and the spec's limits (coordinates at most
  50, instruction strings under 100 characters) and produces a `World` plus a list of
  `Mission`s. **`Simulation`** drives the world through the missions.
  **`OutputFormatter`** renders the results. **`Main`** wires these to the command line.

### Decisions on points the spec leaves open

- Scents are stored as a set of positions rather than by searching the lost robots,
  because the scent belongs to the ground and a set gives a direct constant-time check.
- A lost robot reports the orientation it had when it fell off.
- Blank lines anywhere in the input are ignored, so the blank line between robots is
  optional and leading or trailing blank lines are harmless.
- A scented square protects against falling off in any direction from that square, not
  just the direction the original robot fell.
- The instruction limit is enforced as at most 99 characters, reading "less than 100
  characters" literally.
