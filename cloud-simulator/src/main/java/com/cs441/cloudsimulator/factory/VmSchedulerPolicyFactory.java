package com.cs441.cloudsimulator.factory;

import com.cs441.cloudsimulator.context.VmSchedulerContext;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeSharedOverSubscription;


public class VmSchedulerPolicyFactory implements AbstractFactory<VmScheduler, VmSchedulerContext> {

    @Override
    public VmScheduler createInstance(VmSchedulerContext vmSchedulerContext) {

        switch (vmSchedulerContext.getVmSchedulerPolicy()) {
        case "TIME_SHARED":
            return new VmSchedulerTimeShared(0.4);
        case "SPACE_SHARED":
            return new VmSchedulerSpaceShared(0.4);
        case "TIME_SHARED_WITH_OVER_SUBSCRIPTION":
            return new VmSchedulerTimeSharedOverSubscription(0.4);
        default:
            return new VmSchedulerTimeShared(0.4);
        }
    }
}
