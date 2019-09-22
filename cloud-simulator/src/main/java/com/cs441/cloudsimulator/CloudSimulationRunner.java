package com.cs441.cloudsimulator;

import com.cs441.cloudsimulator.builder.DatacenterFactory;
import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

import com.cs441.cloudsimulator.jobs.Job;
import com.cs441.cloudsimulator.jobs.JobContextualizer;
import com.cs441.cloudsimulator.jobs.WorkflowJob;
import com.cs441.cloudsimulator.jobs.WorkflowJobContextualizer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CloudSimulationRunner {

    public static void main(String[] args) throws Exception {

        CloudSim.init(1, Calendar.getInstance(), true);

        Config cloudServicesProviderConfig = ConfigFactory.load(CLOUD_SERVICES_PROVIDER_CONF);
        Config workflowJobConfig = ConfigFactory.load("workflow-job.conf");

        List<? extends Config> datacenterConfigs = cloudServicesProviderConfig.getConfigList(DATACENTERS);
        List<Datacenter> datacenters = new ArrayList<>();
        DatacenterBroker datacenterBroker = new DatacenterBroker("Wally");

        JobContextualizer workflowJobContextualizer = new WorkflowJobContextualizer(workflowJobConfig);
        workflowJobContextualizer.setCloudletProperties();

        Job workflowJob = new WorkflowJob(datacenterBroker, workflowJobContextualizer);

        datacenterConfigs.forEach(datacenterConfig -> {
            try {
                datacenters.add(DatacenterFactory.createDatacenter(datacenterConfig));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        List<Vm> vms = ((WorkflowJobContextualizer) workflowJobContextualizer).getVms();
        List<Cloudlet> cloudlets = ((WorkflowJobContextualizer) workflowJobContextualizer).getCloudlets();

        datacenterBroker.submitVmList(vms);
        datacenterBroker.submitCloudletList(cloudlets);

    }

}
