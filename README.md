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
