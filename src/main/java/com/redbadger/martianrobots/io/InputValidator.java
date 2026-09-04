package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Mission;
import com.redbadger.martianrobots.domain.World;

/**
 * Checks the rules that take more than one parsed object to see. A rule that belongs to a single
 * object is enforced by that object instead: grid bounds by {@link World}, instruction length by
 * {@link Mission}. That leaves one rule here — a robot has to land on a square of the grid it was
 * actually given, which neither the world nor the mission can check on its own.
 */
public final class InputValidator {

    private InputValidator() {
    }

    public static void validate(Input input) {
        World world = input.world();
        for (Mission mission : input.missions()) {
            if (!world.contains(mission.start())) {
                throw new InputException("Robot landing square " + mission.start().x() + " "
                        + mission.start().y() + " is outside the " + world.maxX() + " "
                        + world.maxY() + " grid");
            }
        }
    }
}
