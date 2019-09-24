package com.cs441.cloudsimulator.jobs;


import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.List;

public class WorkflowJob implements Job {

    private List<Cloudlet> cloudlets;
    private DatacenterBroker broker;
    private JobContextualizer workflowJobContextualizer;

    public WorkflowJob(DatacenterBroker broker, JobContextualizer workflowJobContextualizer) {
        this.broker = broker;
        this.workflowJobContextualizer = workflowJobContextualizer;
        this.cloudlets = workflowJobContextualizer.getCloudlets();
    }

    @Override
    public void submitCloudLetsToVms(List<Vm> vms) {

    }

}
