package com.redbadger.martianrobots;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The Martian surface and everything on it: the grid bounds, the robot currently taking
 * commands, the robots that have finished (in order), and the scents left by lost robots.
 * This is the only mutable object in the simulation.
 */
public final class World {

    public static final int MAX_COORDINATE = 50;

    private final int maxX;
    private final int maxY;
    private final List<Robot> finished = new ArrayList<>();
    private final Set<Position> scents = new HashSet<>();
    private Robot active;

    public World(int maxX, int maxY) {
        if (maxX < 0 || maxY < 0 || maxX > MAX_COORDINATE || maxY > MAX_COORDINATE) {
            throw new IllegalArgumentException(
                    "Grid upper-right corner must be within 0.." + MAX_COORDINATE + ", got " + maxX + " " + maxY);
        }
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public int maxX() {
        return maxX;
    }

    public int maxY() {
        return maxY;
    }

    public boolean contains(Position position) {
        return position.x() >= 0 && position.x() <= maxX
                && position.y() >= 0 && position.y() <= maxY;
    }

    public boolean hasScent(Position position) {
        return scents.contains(position);
    }

    /** Places a new robot on the grid. Only one robot may be active at a time. */
    public void landRobot(Position position, Orientation orientation) {
        if (active != null) {
            throw new IllegalStateException("A robot is already active; retire it before landing another");
        }
        if (!contains(position)) {
            throw new IllegalArgumentException("Landing position " + position + " is off the grid");
        }
        active = Robot.landed(position, orientation);
    }

    /** Applies a command to the active robot. Commands after the robot is lost are ignored. */
    public void execute(Command command) {
        if (active == null) {
            throw new IllegalStateException("No active robot to command");
        }
        if (active.lost()) {
            return;
        }
        active = command.apply(active, this);
        if (active.lost()) {
            scents.add(active.position());
        }
    }

    /** Ends the active robot's run, recording it in the finished list. */
    public Robot retireRobot() {
        if (active == null) {
            throw new IllegalStateException("No active robot to retire");
        }
        Robot done = active;
        active = null;
        finished.add(done);
        return done;
    }

    public Optional<Robot> activeRobot() {
        return Optional.ofNullable(active);
    }

    public List<Robot> finishedRobots() {
        return List.copyOf(finished);
    }
}
