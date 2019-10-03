package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.MIPS;

/**
 * This class defines a factory to create an instance of the @{@link Pe} class.
 */

public class PeFactory implements AbstractFactory<Pe, Config> {


    /**
     * This method creates an instance of the @{@link Pe} class.
     *
     * @param config The config pertaining to the @{@link Pe} to be created.
     */

    @Override
    public Pe createInstance(Config config) {
        return new PeSimple(config.getDouble(MIPS), new PeProvisionerSimple());
    }
}
