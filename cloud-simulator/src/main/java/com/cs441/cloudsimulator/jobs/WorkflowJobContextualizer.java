package com.cs441.cloudsimulator.jobs;


import com.typesafe.config.Config;
import org.cloudbus.cloudsim.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is a representation of the contextualization of a WorkflowJob.
 * Generates the number of requests and cloudlet properties.
 */

public class WorkflowJobContextualizer implements JobContextualizer {

    private int requests;
    private Config config;
    private List<Cloudlet> cloudlets;
    private List<Vm> vms;

    public WorkflowJobContextualizer(Config config) {
        this.config = config;
        this.cloudlets = new ArrayList<>();
        this.vms = new ArrayList<>();
    }

    @Override
    public void setCloudletProperties() {

        List<? extends Config> activityConfigs = this.config.getConfigList("activities");

        int cloudletLength = 0;
        int cloudLetFileSize = 0;
        int cloudLetOutputFileSize = 0;

        UtilizationModel utilizationModel = new UtilizationModelFull();

        for (int i = 0; i < activityConfigs.size(); i++) {
            cloudletLength += activityConfigs.get(i).getInt("taskLength");
            cloudLetFileSize += activityConfigs.get(i).getInt("inputFileSize");
            cloudLetOutputFileSize += activityConfigs.get(i).getInt("outputFileSize");
        }

        requests = randomizeRequests();

        for (int i = 0; i < requests; i++) {
            cloudlets.add(new Cloudlet(i, cloudletLength, 0, cloudLetFileSize, cloudLetOutputFileSize, new
                    UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull()));
        }

        List<? extends Config> vmConfigs = this.config.getConfigList("vms");

        for (int i = 0; i < vmConfigs.size(); i++) {

            this.vms.add(new Vm(i, 0, vmConfigs.get(i).getDouble("mips"), vmConfigs.get(i).getInt("pesNumber"),
                    vmConfigs.get(i).getInt("ram"), vmConfigs.get(i).getLong("bw"), vmConfigs.get(i).getLong("size"),
                    vmConfigs.get(i).getString("vmm"), new CloudletSchedulerTimeShared()));
        }

    }

    @Override
    public int randomizeRequests() {
        Random random = new Random();
        requests = random.nextInt(20);

        if (requests < config.getInt("min.requests"))
            requests = config.getInt("min.requests");

        return requests;
    }

    public List<Cloudlet> getCloudlets() {
        return cloudlets;
    }

    public List<Vm> getVms() {
        return vms;
    }
}
