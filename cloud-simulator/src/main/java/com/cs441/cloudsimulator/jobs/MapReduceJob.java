package com.cs441.cloudsimulator.jobs;

import com.typesafe.config.Config;

import java.util.Random;

public class MapReduceJob implements Job {
    @Override
    public int randomizeJobRequests(Config config) {
        return config.getInt("minCloudletLength") + new Random().nextInt(5000);
    }
}
