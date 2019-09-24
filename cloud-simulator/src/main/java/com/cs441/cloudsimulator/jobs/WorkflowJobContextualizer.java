package com.cs441.cloudsimulator.jobs;


import com.typesafe.config.Config;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a representation of the contextualization of a WorkflowJob.
 * Generates the number of requests and cloudlet properties.
 */

public class WorkflowJobContextualizer implements JobContextualizer {

    private int requests;
    private Config config;
    private List<Cloudlet> cloudlets;
    private List<Vm> vms;
    private DatacenterBroker datacenterBroker;

    public WorkflowJobContextualizer(Config config, DatacenterBroker datacenterBroker) {
        this.config = config;
        this.cloudlets = new ArrayList<>();
        this.vms = new ArrayList<>();
        this.datacenterBroker = datacenterBroker;
    }

    @Override
    public void setCloudletProperties() {


        this.requests = randomizeRequests();

        for (int i = 0; i < requests; i++) {
            Cloudlet cloudlet = new CloudletSimple(500, 1, new UtilizationModelFull());
            this.cloudlets.add(cloudlet);
        }

        List<? extends Config> vmConfigs = this.config.getConfigList("vms");

        for (int i = 0; i < vmConfigs.size(); i++) {

            this.vms.add(new VmSimple(1000, 1, new CloudletSchedulerTimeShared()));
        }

    }

    @Override
    public int randomizeRequests() {
        requests = 100;
        return requests;
    }

    public List<Cloudlet> getCloudlets() {
        return cloudlets;
    }

    public List<Vm> getVms() {
        return vms;
    }
}
