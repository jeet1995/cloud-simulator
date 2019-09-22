package com.cs441.cloudsimulator.jobs;

import org.cloudbus.cloudsim.Cloudlet;

import java.util.List;

public interface JobContextualizer {

    void setCloudletProperties();
    int randomizeRequests();
    List<Cloudlet> getCloudlets();
}
