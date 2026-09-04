package com.redbadger.martianrobots;

import java.util.List;

/** A parsed input file: the world to explore and the missions to run on it, in order. */
public record Input(World world, List<Mission> missions) {

    public Input {
        missions = List.copyOf(missions);
    }
}
