package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyRoundRobin;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

/**
 * This class defines a factory to create an instance of the @{@link VmAllocationPolicy} class.
 */


public class VmAllocationPolicyFactory implements AbstractFactory<VmAllocationPolicy, Config> {


    /**
     * Creates an instance of either {@link VmAllocationPolicyBestFit}, {@link VmAllocationPolicySimple} or {@link VmAllocationPolicyRoundRobin} classes.
     *
     * @param config
     * @return vmAllocationPolicy
     */
    @Override
    public VmAllocationPolicy createInstance(Config config) throws Exception {

        String vmAllocationPolicyType = config.getString(VM_ALLOCATION_POLICY);

        switch (vmAllocationPolicyType) {


        case SIMPLE:
            return new VmAllocationPolicySimple();
        case BEST_FIT:
            return new VmAllocationPolicyBestFit();
        case ROUND_ROBIN:
            return new VmAllocationPolicyRoundRobin();
        default:
            return new VmAllocationPolicySimple();

        }
    }

}
