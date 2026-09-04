package com.redbadger.martianrobots;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Martian surface and everything on it: the grid bounds, the robots that have finished (in
 * order), and the scents left by lost robots. It runs one mission at a time, which is what the
 * spec's sequential robots amount to.
 */
public final class World {

    public static final int MAX_COORDINATE = 50;

    private final int maxX;
    private final int maxY;
    private final List<Robot> finished = new ArrayList<>();
    private final Set<Position> scents = new HashSet<>();

    public World(int maxX, int maxY) {
        if (maxX < 0 || maxY < 0 || maxX > MAX_COORDINATE || maxY > MAX_COORDINATE) {
            throw new IllegalArgumentException(
                    "Grid upper-right corner must be within 0.." + MAX_COORDINATE + ", got " + maxX + " " + maxY);
        }
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Lands a robot at the mission's starting square, runs its commands to completion, and records
     * it in the finished list. Commands issued after the robot is lost are ignored.
     */
    public Robot run(Mission mission) {
        if (!contains(mission.start())) {
            throw new IllegalArgumentException("Landing position " + mission.start() + " is off the grid");
        }
        Robot robot = new Robot(mission.start(), mission.orientation());
        for (Command command : mission.commands()) {
            if (robot.lost()) {
                break;
            }
            execute(robot, command);
        }
        finished.add(robot);
        return robot;
    }

    private void execute(Robot robot, Command command) {
        switch (command) {
            case L -> robot.turnLeft();
            case R -> robot.turnRight();
            case F -> moveForward(robot);
        }
    }

    /**
     * Moving off the grid loses the robot and leaves a scent on the square it fell from, unless
     * that square is already scented, in which case the robot ignores the command and stays put.
     */
    private void moveForward(Robot robot) {
        Position next = robot.nextPosition();
        if (contains(next)) {
            robot.moveTo(next);
        } else if (!hasScent(robot.position())) {
            scents.add(robot.position());
            robot.markLost();
        }
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

    public List<Robot> finishedRobots() {
        return List.copyOf(finished);
    }
}
