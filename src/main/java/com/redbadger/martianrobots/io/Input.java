package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Mission;
import com.redbadger.martianrobots.domain.World;
import java.util.List;

/** A parsed input file: the world to explore and the missions to run on it, in order. */
public record Input(World world, List<Mission> missions) {

    public Input {
        missions = List.copyOf(missions);
    }
}
