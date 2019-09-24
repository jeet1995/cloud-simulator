package com.cs441.cloudsimulator.context;

import org.cloudbus.cloudsim.resources.Pe;

import java.util.List;

public class VmSchedulerContext {
    private String vmSchedulerPolicy;
    private List<Pe> pes;

    public VmSchedulerContext(String vmSchedulerPolicy, List<Pe> pes) {
        this.vmSchedulerPolicy = vmSchedulerPolicy;
        this.pes = pes;
    }

    public String getVmSchedulerPolicy() {
        return vmSchedulerPolicy;
    }

    public List<Pe> getPes() {
        return pes;
    }

}
