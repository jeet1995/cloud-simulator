package com.cs441.cloudsimulator.jobs;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;


/**
 * This class defines a Reducer in the context of Map-Reduce. Here it is modelled as a @{@link NetworkCloudlet}
 * since an instance of Reducer is to be executed on a virtual machine.
 */


public class Reducer extends NetworkCloudlet {

    public Reducer(long cloudletLength, int pesNumber) {
        super(cloudletLength, pesNumber);
    }

}
