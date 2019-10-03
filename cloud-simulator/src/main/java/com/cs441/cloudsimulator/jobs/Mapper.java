package com.cs441.cloudsimulator.jobs;

import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;

import java.util.Objects;

/**
 * This class defines a Mapper in the context of Map-Reduce. Here it is modelled as a @{@link NetworkCloudlet}
 * since an instance of Mapper is to be executed on a virtual machine.
 */

public class Mapper extends NetworkCloudlet {

    private long mapperId;

    public Mapper(long cloudletLength, int pesNumber) {
        super(cloudletLength, pesNumber);
    }

    public void setMapperId(long mapperId) {
        this.mapperId = mapperId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Mapper))
            return false;
        if (!super.equals(o))
            return false;
        Mapper mapper = (Mapper) o;
        return mapperId == mapper.mapperId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mapperId);
    }
}
