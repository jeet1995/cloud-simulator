package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.allocationpolicies.*;
import org.cloudbus.cloudsim.allocationpolicies.migration.VmAllocationPolicyMigrationBestFitStaticThreshold;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

public class VmAllocationPolicyFactory implements AbstractFactory<VmAllocationPolicy, Config> {

    @Override
    public VmAllocationPolicy createInstance(Config config) throws Exception {

        String vmAllocationPolicyType = config.getString(VM_ALLOCATION_POLICY);

        switch (vmAllocationPolicyType) {


        case SIMPLE:
            return new VmAllocationPolicySimple();
        case BEST_FIT:
            return new VmAllocationPolicyBestFit();
        case WORST_FIT:
            return new VmAllocationPolicyWorstFit();
        case "dynamic":
            return new VmAllocationPolicyRoundRobin();

        default:
            return new VmAllocationPolicySimple();

        }
    }

}
