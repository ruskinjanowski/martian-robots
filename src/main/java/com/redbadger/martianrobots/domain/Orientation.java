package com.redbadger.martianrobots.domain;

/**
 * A compass heading and the unit step it moves a robot. The constants are declared in clockwise
 * order, which is what {@link #left()} and {@link #right()} rotate through, and their names are
 * the symbols used in the input, so {@code Orientation.valueOf} parses them.
 */
public enum Orientation {
    N(0, 1),
    E(1, 0),
    S(0, -1),
    W(-1, 0);

    /** Cached because {@code values()} allocates a fresh array on every call. */
    private static final Orientation[] VALUES = values();

    private final int dx;
    private final int dy;

    Orientation(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }

    public Orientation left() {
        return VALUES[(ordinal() + VALUES.length - 1) % VALUES.length];
    }

    public Orientation right() {
        return VALUES[(ordinal() + 1) % VALUES.length];
    }
}
