package com.cs441.cloudsimulator.broker;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link CustomizedDatacenterBroker} provides a modification to the {@link DatacenterBroker}
 * provided by the CloudSim project.
 *
 * */

public class CustomizedDatacenterBroker extends DatacenterBroker {

    private String strategy;
    private Map<Vm, Cloudlet> vmCloudletMap;

    public CustomizedDatacenterBroker(String name, String strategy) throws Exception {
        super(name);
        this.strategy = strategy;
        this.vmCloudletMap = new HashMap<>();
    }
}
