package com.redbadger.martianrobots;

/** The state of a robot. Immutable; every change produces a new instance. */
public record Robot(Position position, Orientation orientation, boolean lost) {

    public static Robot landed(Position position, Orientation orientation) {
        return new Robot(position, orientation, false);
    }

    public Robot facing(Orientation newOrientation) {
        return new Robot(position, newOrientation, lost);
    }

    public Robot at(Position newPosition) {
        return new Robot(newPosition, orientation, lost);
    }

    public Robot markLost() {
        return new Robot(position, orientation, true);
    }
}
