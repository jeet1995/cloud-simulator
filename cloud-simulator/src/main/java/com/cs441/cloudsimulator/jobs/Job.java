package com.cs441.cloudsimulator.jobs;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.List;

public interface Job {
    void submitCloudLetsToVms(List<Vm> vms);
}
