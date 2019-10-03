package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.resources.Pe;

import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

/**
 * This class defines a factory to create an instance of the @{@link NetworkHost} class.
 */

public class NetworkHostFactory implements AbstractFactory<NetworkHost, Config> {

    List<Pe> pes;

    public NetworkHostFactory(List<Pe> pes) {
        this.pes = pes;
    }

    /**
     * This method creates an instance of the @{@link NetworkHost} class.
     *
     * @param config The config pertaining to the @{@link NetworkHost} to be created.
     * @return networkHost
     */

    @Override
    public NetworkHost createInstance(Config config) {
        return new NetworkHost(config.getLong(RAM), config.getLong(BW), config.getLong(STORAGE), pes);
    }
}
