package com.cs441.cloudsimulator;

import com.cs441.cloudsimulator.factory.AbstractFactory;
import com.cs441.cloudsimulator.factory.DatacenterFactory;
import com.cs441.cloudsimulator.jobs.WorkflowJobContextualizer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.VANILLA_DATACENTERS_CONF;
import static com.cs441.cloudsimulator.configs.ApplicationConstants.WORKFLOW_JOB_CONF;


public class CloudSimulationRunner {

    private static final Logger logger = LoggerFactory.getLogger(CloudSimulationRunner.class);

    public static void main(String[] args) {

        logger.info("Load configuration files");

        List<Config> cloudServicesProviderConfigs = new ArrayList<>();

        Config vanillaDatacentersConfig = ConfigFactory.load(VANILLA_DATACENTERS_CONF);
        Config networkDatacentersConfig = ConfigFactory.load("network-datacenters.conf");

        cloudServicesProviderConfigs.add(vanillaDatacentersConfig);
        cloudServicesProviderConfigs.add(networkDatacentersConfig);

        Config workflowJobConfig = ConfigFactory.load(WORKFLOW_JOB_CONF);

        List<? extends Config> cloudServiceProvider = cloudServicesProviderConfigs.get(0).getConfigList("cloudServiceProvider");

        cloudServiceProvider.forEach(datacentersConfig -> {
            int numDatacenters = datacentersConfig.getInt("numDatacenters");
            CloudSim cloudSim = new CloudSim();
            for (int i = 0; i < numDatacenters; i++) {
                createDatacenter(datacentersConfig.getConfig("datacenter"), cloudSim);
            }
            List<DatacenterBroker> datacenterBrokers = new ArrayList<>(numDatacenters);
            List<WorkflowJobContextualizer> workflowJobContextualizers = new ArrayList<>(numDatacenters);

            for (int i = 0; i < numDatacenters; i++) {
                datacenterBrokers.add(createDatacenterBroker(datacentersConfig, cloudSim));
                workflowJobContextualizers.add(new WorkflowJobContextualizer(workflowJobConfig, datacenterBrokers.get(i)));
                workflowJobContextualizers.get(i).setCloudletProperties();
                datacenterBrokers.get(i).submitCloudletList(workflowJobContextualizers.get(i).getCloudlets());
                datacenterBrokers.get(i).submitVmList(workflowJobContextualizers.get(i).getVms());
            }
            cloudSim.start();

            logger.info("Simulation results for vmAllocationPolicy {}", datacentersConfig.getConfig("datacenter").getString("vmAllocationPolicy"));

            for (int i = 0; i < numDatacenters; i++) {
                new CloudletsTableBuilder(datacenterBrokers.get(i).getCloudletFinishedList()).build();
            }
        });

    }

    private static void createDatacenter(Config datacenterConfig, CloudSim simulation) {
        AbstractFactory<Datacenter, Config> datacenterFactory = new DatacenterFactory(simulation);
        datacenterFactory.createInstance(datacenterConfig);
    }

    private static DatacenterBroker createDatacenterBroker(Config config, CloudSim simulation) {
        return new DatacenterBrokerSimple(simulation);
    }



}
