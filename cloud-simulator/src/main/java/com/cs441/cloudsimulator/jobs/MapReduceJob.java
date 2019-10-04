package com.cs441.cloudsimulator.jobs;

import com.typesafe.config.Config;

import java.util.Random;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.MIN_CLOUDLET_LENGTH;

public class MapReduceJob implements Job {
    @Override
    public int randomizeJobRequests(Config config) {
        return config.getInt(MIN_CLOUDLET_LENGTH) + new Random().nextInt(5000);
    }
}
