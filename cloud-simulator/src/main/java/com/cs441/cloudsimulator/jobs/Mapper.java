package com.cs441.cloudsimulator.jobs;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;

/**
 * This class defines a Mapper in the context of Map-Reduce. Here it is modelled as a @{@link NetworkCloudlet}
 * since an instance of Mapper is to be executed on a virtual machine.
 */

public class Mapper extends NetworkCloudlet {

    public Mapper(long cloudletLength, int pesNumber) {
        super(cloudletLength, pesNumber);
    }
}
