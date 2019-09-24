package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;

public class NetworkDatacenterFactory implements AbstractFactory<NetworkDatacenter, Config> {

    private Simulation simulation;

    public NetworkDatacenterFactory(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public NetworkDatacenter createInstance(Config config) {
        return null;
    }
}
