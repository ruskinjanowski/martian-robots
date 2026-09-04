package com.redbadger.martianrobots.domain;

/**
 * A robot on the grid. Mutable: it carries its own position, heading and lost flag, and updates
 * them in place as it works through its commands. It knows how to turn and where its next square
 * would be, but not whether that square is on the grid — that rule belongs to the {@link World}.
 */
public final class Robot {

    private Position position;
    private Orientation orientation;
    private boolean lost;

    public Robot(Position position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Position position() {
        return position;
    }

    public Orientation orientation() {
        return orientation;
    }

    public boolean lost() {
        return lost;
    }

    /** The square directly ahead, whether or not it is on the grid. */
    public Position nextPosition() {
        return position.step(orientation);
    }

    public void turnLeft() {
        orientation = orientation.left();
    }

    public void turnRight() {
        orientation = orientation.right();
    }

    public void moveTo(Position newPosition) {
        position = newPosition;
    }

    public void markLost() {
        lost = true;
    }
}
