package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.allocationpolicies.*;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;

public class VmAllocationPolicyFactory implements AbstractFactory<VmAllocationPolicy, Config> {

    @Override
    public VmAllocationPolicy createInstance(Config config) {
        String allocationPolicy = config.getString("vmAllocationPolicy");

        switch (allocationPolicy) {
        case "SIMPLE":
            return new VmAllocationPolicySimple();
        case "BEST_FIT":
            return new VmAllocationPolicyBestFit();
        case "WORST_FIT":
            return new VmAllocationPolicyWorstFit();
        case "RANDOM":
            return new VmAllocationPolicyRandom(ContinuousDistribution.NULL);
        default:
            return new VmAllocationPolicySimple();
        }
    }
}
