package com.redbadger.martianrobots;

import java.util.List;

/** Runs each mission against the world in turn and returns the robots as they finished. */
public final class Simulation {

    private Simulation() {
    }

    public static List<Robot> run(Input input) {
        World world = input.world();
        for (Mission mission : input.missions()) {
            world.landRobot(mission.start(), mission.orientation());
            for (Command command : mission.commands()) {
                world.execute(command);
            }
            world.retireRobot();
        }
        return world.finishedRobots();
    }
}
