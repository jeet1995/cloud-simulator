package com.cs441.cloudsimulator.jobs;


import com.typesafe.config.Config;

public interface Job {

    int randomizeJobRequests(Config config);

}
