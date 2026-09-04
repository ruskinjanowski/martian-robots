package com.redbadger.martianrobots;

/** A compass heading. Declared in clockwise order so turning is a step through the constants. */
public enum Orientation {
    N(0, 1),
    E(1, 0),
    S(0, -1),
    W(-1, 0);

    private static final Orientation[] CLOCKWISE = values();

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
        return CLOCKWISE[(ordinal() + CLOCKWISE.length - 1) % CLOCKWISE.length];
    }

    public Orientation right() {
        return CLOCKWISE[(ordinal() + 1) % CLOCKWISE.length];
    }

    public static Orientation fromSymbol(char symbol) {
        return switch (symbol) {
            case 'N' -> N;
            case 'E' -> E;
            case 'S' -> S;
            case 'W' -> W;
            default -> throw new IllegalArgumentException("Unknown orientation: " + symbol);
        };
    }
}
