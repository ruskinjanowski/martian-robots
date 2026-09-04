package com.redbadger.martianrobots;

/** A grid coordinate. Immutable; equality and hashing are by value so it can key a set of scents. */
public record Position(int x, int y) {

    public Position step(Orientation orientation) {
        return new Position(x + orientation.dx(), y + orientation.dy());
    }
}
