package com.cs441.cloudsimulator.utils;

import com.cs441.cloudsimulator.factory.AbstractFactory;
import com.cs441.cloudsimulator.factory.SwitchFactory;
import com.typesafe.config.Config;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.switches.AbstractSwitch;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.RootSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;

import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.BRITE_FILE;
import static com.cs441.cloudsimulator.configs.ApplicationConstants.NETWORK_TOPOLOGY;

/**
 * This class is a utility class which configures an instance of @{@link RootSwitch} for instances of
 *
 * @{@link NetworkDatacenter}
 * It also maps instances of @{@link NetworkDatacenter} and @{@link DatacenterBroker} onto a @{@link NetworkTopology}
 */

public class NetworkDatacenterUtils {

    private NetworkDatacenterUtils() {
    }


    /**
     * This method adds a root switch.
     *
     * @param networkDatacenters List of network datacenters
     * @param simulation         The CloudSimPlus simulation entity.
     * @param config             Config to create a root switch.
     */
    public static void addRootSwitch(List<NetworkDatacenter> networkDatacenters, CloudSim simulation, Config config)
            throws Exception {

        AbstractFactory<AbstractSwitch, Config> switchFactory = new SwitchFactory(simulation, networkDatacenters.get
                (0));
        RootSwitch rootSwitch = (RootSwitch) switchFactory.createInstance(config);

        for (int i = 0; i < networkDatacenters.size(); i++) {
            List<Switch> switches = networkDatacenters.get(i).getSwitchMap();

            for (int j = 0; j < switches.size(); j++) {
                Switch switchIterated = switches.get(j);
                if (switchIterated instanceof AggregateSwitch) {
                    rootSwitch.getDownlinkSwitches().add(switchIterated);
                    switchIterated.setUplinkBandwidth(rootSwitch.getDownlinkBandwidth());
                    switchIterated.getUplinkSwitches().add(rootSwitch);
                }
            }
            networkDatacenters.get(i).addSwitch(rootSwitch);
        }
    }

    /**
     * This method maps networking components to a network topology.
     *
     * @param networkDatacenters  List of network datacenters
     * @param datacenterBrokers   List of datacenter brokers
     * @param globalReducerBroker Global reducer broker
     * @param config              Config to map nodes to *.brite file.
     */
    public static NetworkTopology mapNetworkConponentToNetworkTopology(List<NetworkDatacenter> networkDatacenters,
            List<DatacenterBroker> datacenterBrokers, DatacenterBroker globalReducerBroker, Config config) {

        NetworkTopology networkTopology = BriteNetworkTopology.getInstance(config.getConfig(NETWORK_TOPOLOGY)
                .getString(BRITE_FILE));

        for (int i = 0; i < networkDatacenters.size(); i++) {
            networkTopology.mapNode(networkDatacenters.get(i).getId(), i);
            networkTopology.mapNode(datacenterBrokers.get(i).getId(), i + networkDatacenters.size());
        }

        networkTopology.mapNode(globalReducerBroker.getId(), networkDatacenters.size() + datacenterBrokers.size());

        return networkTopology;
    }

}
