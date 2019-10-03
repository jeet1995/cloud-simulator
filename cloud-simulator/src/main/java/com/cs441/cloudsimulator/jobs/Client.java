package com.cs441.cloudsimulator.jobs;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.vms.network.NetworkVm;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

/**
 * This class denotes a client which submits a map-reduce job.
 * */

public class Client {

    private Job job;
    private Config config;
    private DatacenterBroker datacenterBroker;
    private ResourceManager resourceManager;
    private JobTracker jobTracker;

    public Client(Job job, Config config, DatacenterBroker datacenterBroker, JobTracker jobTracker) {
        this.job = job;
        this.config = config;
        this.datacenterBroker = datacenterBroker;
        this.jobTracker = jobTracker;
    }

    /**
     * This method generates cloudlets corresponding to the job.
     *
     * @return List of network cloudlets which would complete the job.
     * */

    public List<NetworkCloudlet> submitJob() {

        List<NetworkCloudlet> cloudlets = new ArrayList<>();

        this.resourceManager = new ResourceManager(datacenterBroker, jobTracker);

        resourceManager.executeJob(config);

        List<Mapper> mappers = resourceManager.getMappers();
        List<Reducer> reducers = resourceManager.getReducers();

        for (Mapper mapper : mappers) {
            mapper.setBroker(datacenterBroker);
            cloudlets.add(mapper);
        }

        for (Reducer reducer : reducers) {
            reducer.setBroker(datacenterBroker);
            cloudlets.add(reducer);
        }

        return cloudlets;
    }


    /**
     *
     * */
    public List<NetworkVm> requestVms() {
        List<? extends Config> vmConfigs = config.getConfigList(VMS);
        List<NetworkVm> vms = new ArrayList<>();

        for (int i = 0; i < vmConfigs.size(); i++) {
            NetworkVm vm = new NetworkVm(vmConfigs.get(i).getLong(MIPS), vmConfigs.get(i).getInt(PES));

            vms.add(vm);
        }
        return vms;
    }

}
