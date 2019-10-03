package com.cs441.cloudsimulator;

import com.cs441.cloudsimulator.configs.ApplicationConfig;
import com.cs441.cloudsimulator.factory.AbstractFactory;
import com.cs441.cloudsimulator.factory.NetworkDatacenterFactory;
import com.cs441.cloudsimulator.jobs.Client;
import com.cs441.cloudsimulator.jobs.JobTracker;
import com.cs441.cloudsimulator.jobs.MapReduceJob;
import com.cs441.cloudsimulator.jobs.Mapper;
import com.cs441.cloudsimulator.utils.DecoratedCloudletsTableBuilder;
import com.cs441.cloudsimulator.utils.NetworkDatacenterUtils;
import com.typesafe.config.Config;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.vms.network.NetworkVm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

public class MapReduceJobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapReduceJobRunner.class);

    public static void main(String[] args) throws Exception {

        List<? extends Config> datacentersConfig = ApplicationConfig.cloudServiceProvidersConf.getConfigList
                (CLOUD_SERVICE_PROVIDERS);
        Config mapReduceJobConfig = ApplicationConfig.mapReduceJobConf;


        for (int i = 0; i < datacentersConfig.size(); i++) {

            LOGGER.info("Loading datacenter network {}", (i + 1));

            CloudSim simulation = new CloudSim();

            int numDatacenters = datacentersConfig.get(i).getInt(NUM_DATACENTERS);

            LOGGER.info("Creating {} datacenters in datacenter network : {}", numDatacenters, (i + 1));

            List<NetworkDatacenter> networkDatacenters = new ArrayList<>();
            List<DatacenterBroker> datacenterBrokers = new ArrayList<>();

            for (int j = 0; j < numDatacenters; j++) {
                NetworkDatacenter networkDatacenter = createNetworkDatacenter(simulation, datacentersConfig.get(i)
                        .getConfig(DATACENTER));
                DatacenterBroker datacenterBroker = new DatacenterBrokerSimple(simulation);
                datacenterBrokers.add(datacenterBroker);
                networkDatacenters.add(networkDatacenter);
            }

            JobTracker jobTracker = new JobTracker(simulation);

            LOGGER.info("Adding root switch to datacenter network {}", (i + 1));

            NetworkDatacenterUtils.addRootSwitch(networkDatacenters, simulation, datacentersConfig.get(i).getConfig
                    (DATACENTER).getConfig(ROOT_SWITCH));

            LOGGER.info("Mapping datacenter network {} to network topology", (i + 1));

            NetworkTopology networkTopology = NetworkDatacenterUtils.mapNetworkConponentToNetworkTopology
                    (networkDatacenters, datacenterBrokers, jobTracker.getReducerBroker(), datacentersConfig.get(i)
                            .getConfig(DATACENTER));

            simulation.setNetworkTopology(networkTopology);

            LOGGER.info("Submitting map-reduce job to datacenter network {}", (i + 1));

            submitJob(datacenterBrokers, mapReduceJobConfig, jobTracker);
            simulation.start();
            printSimulationResults(datacenterBrokers, jobTracker);

        }
    }

    private static NetworkDatacenter createNetworkDatacenter(CloudSim sim, Config config) throws Exception {
        AbstractFactory<NetworkDatacenter, Config> networkDatacenterFactory = new NetworkDatacenterFactory(sim, config);
        return networkDatacenterFactory.createInstance(config);
    }

    private static void submitJob(List<DatacenterBroker> datacenterBrokers, Config mapReduceJobConfig, JobTracker
            jobTracker) throws Exception {
        for (int j = 0; j < datacenterBrokers.size(); j++) {
            Client client = new Client(new MapReduceJob(), mapReduceJobConfig, datacenterBrokers.get(j), jobTracker);
            List<NetworkCloudlet> cloudlets = client.submitJob();
            List<NetworkVm> networkVms = client.requestVms();
            List<Mapper> mappers = new ArrayList<>();
            for (NetworkCloudlet cloudlet : cloudlets) {
                if (cloudlet instanceof Mapper)
                    mappers.add((Mapper) cloudlet);
            }

            LOGGER.info("Datacenter broker {} submitting cloudlets", datacenterBrokers.get(j).getId());

            datacenterBrokers.get(j).submitCloudletList(mappers);


            LOGGER.info("Datacenter broker {} submitting network vms", datacenterBrokers.get(j).getId());

            datacenterBrokers.get(j).submitVmList(networkVms);
        }
    }

    private static void printSimulationResults(List<DatacenterBroker> datacenterBrokers, JobTracker jobTracker) {
        for (int j = 0; j < datacenterBrokers.size(); j++) {
            new DecoratedCloudletsTableBuilder(datacenterBrokers.get(j).getCloudletFinishedList()).build();
        }
        new DecoratedCloudletsTableBuilder(jobTracker.getReducerBroker().getCloudletFinishedList()).build();
    }
}
