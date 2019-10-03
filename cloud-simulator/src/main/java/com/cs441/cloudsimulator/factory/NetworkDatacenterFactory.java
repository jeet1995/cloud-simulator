package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.DatacenterCharacteristics;
import org.cloudbus.cloudsim.datacenters.DatacenterCharacteristicsSimple;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.switches.AbstractSwitch;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

/**
 * This class defines a factory to create an instance of the @{@link NetworkDatacenter} class.
 */

public class NetworkDatacenterFactory implements AbstractFactory<NetworkDatacenter, Config> {

    private CloudSim simulation;
    private Config config;

    public NetworkDatacenterFactory(CloudSim simulation, Config config) {
        this.simulation = simulation;
        this.config = config;
    }

    /**
     * This method creates an instance of the @{@link NetworkDatacenter} class.
     *
     * @param config The config pertaining to the @{@link NetworkDatacenter} to be created.
     * @return networkDatacenter
     */

    @Override
    public NetworkDatacenter createInstance(Config config) throws Exception {


        List<NetworkHost> networkHosts = new ArrayList<>();
        List<? extends Config> hostsConfig = config.getConfigList(HOSTS);

        for (int i = 0; i < hostsConfig.size(); i++) {
            List<Pe> pes = getPes(hostsConfig.get(i));
            List<NetworkHost> hostsByConfig = getNetworkHosts(hostsConfig.get(i), pes);

            for (int j = 0; j < hostsByConfig.size(); j++) {
                hostsByConfig.get(j).setVmScheduler(new VmSchedulerTimeShared());
                networkHosts.add(hostsByConfig.get(j));
            }
        }


        NetworkDatacenter networkDatacenter = new NetworkDatacenter(simulation, networkHosts, new
                VmAllocationPolicyFactory().createInstance(config));

        DatacenterCharacteristics datacenterCharacteristics = new DatacenterCharacteristicsSimple(networkDatacenter);

        createNetwork(networkDatacenter, networkHosts);

        networkDatacenter.getCharacteristics().setArchitecture(config.getString(ARCHITECTURE));
        networkDatacenter.getCharacteristics().setOs(config.getString(ARCHITECTURE));
        networkDatacenter.getCharacteristics().setCostPerStorage(config.getDouble(COST_PER_STORAGE));
        networkDatacenter.getCharacteristics().setCostPerMem(config.getDouble(COST_PER_MEM));
        networkDatacenter.getCharacteristics().setCostPerBw(config.getDouble(COST_PER_BW));
        networkDatacenter.getCharacteristics().setCostPerSecond(config.getDouble(COST_PER_SEC));

        return networkDatacenter;

    }


    /**
     * This method creates a list of instances of the @{@link Pe} class.
     *
     * @param config Configuration to create processing elements.
     * @return List
     */
    private List<Pe> getPes(Config config) throws Exception {


        List<? extends Config> peConfigs = config.getConfigList(PES);
        AbstractFactory<Pe, Config> peFactory = new PeFactory();
        List<Pe> pes = new ArrayList<>();

        for (int i = 0; i < peConfigs.size(); i++) {
            Pe pe = peFactory.createInstance(peConfigs.get(i));
            pes.add(pe);
        }
        return pes;
    }


    /**
     * This method creates a list of instances of the @{@link NetworkHost} class.
     *
     * @param config Configuration to create processing elements.
     * @return List
     */
    private List<NetworkHost> getNetworkHosts(Config config, List<Pe> pes) throws Exception {

        AbstractFactory<NetworkHost, Config> hostFactory = new NetworkHostFactory(pes);
        List<NetworkHost> hosts = new ArrayList<>();

        for (int j = 0; j < config.getInt(NUM_HOSTS); j++) {
            NetworkHost host = hostFactory.createInstance(config);
            hosts.add(host);
        }

        return hosts;
    }

    /**
     * This method creates a network consisting of a datacenter and hosts.
     *
     * @param networkDatacenter The network datacenter which is a part of the network.
     * @param networkHosts      The network hosts which are a part of the network.
     */
    private void createNetwork(NetworkDatacenter networkDatacenter, List<NetworkHost> networkHosts) throws Exception {

        List<EdgeSwitch> edgeSwitches = createEdgeSwitches(config.getConfig(EDGE_SWITCH), networkDatacenter);
        List<AggregateSwitch> aggregateSwitches = createAggregateSwitches(config.getConfig(AGGREGATE_SWITCH),
                networkDatacenter);

        for (NetworkHost networkHost : networkHosts) {
            int edgeSwitchIdx = Math.round(networkHost.getId() % Integer.MAX_VALUE) / edgeSwitches.get(0).getPorts();
            networkHost.setEdgeSwitch(edgeSwitches.get(edgeSwitchIdx));
        }

        for (int i = 0; i < edgeSwitches.size(); i++) {
            int aggregateSwitchIdx = i / aggregateSwitches.get(0).getPorts();
            AggregateSwitch aggregateSwitch = aggregateSwitches.get(aggregateSwitchIdx);
            edgeSwitches.get(i).getUplinkSwitches().add(aggregateSwitch);
            edgeSwitches.get(i).setUplinkBandwidth(aggregateSwitch.getDownlinkBandwidth());
            aggregateSwitch.getDownlinkSwitches().add(edgeSwitches.get(i));
        }


    }

    /**
     * This method creates a list of instances of the @{@link EdgeSwitch} class.
     *
     * @param config            Configuration to create edge switches.
     * @param networkDatacenter The network datacenter which participates in the connection.
     * @return List
     */
    public List<EdgeSwitch> createEdgeSwitches(Config config, NetworkDatacenter networkDatacenter) throws Exception {
        List<EdgeSwitch> edgeSwitches = new ArrayList<>();
        AbstractFactory<AbstractSwitch, Config> edgeSwitchFactory = new SwitchFactory(simulation, networkDatacenter);
        for (int i = 0; i < config.getInt(NUM_SWITCHES); i++) {
            EdgeSwitch edgeSwitch = (EdgeSwitch) edgeSwitchFactory.createInstance(config);
            edgeSwitches.add(edgeSwitch);
            networkDatacenter.addSwitch(edgeSwitch);
        }
        return edgeSwitches;
    }


    /**
     * This method creates a list of instances of the @{@link AggregateSwitch} class.
     *
     * @param config            Configuration to create aggregate switches.
     * @param networkDatacenter The network datacenter which participates in the connection.
     * @return List
     */
    public List<AggregateSwitch> createAggregateSwitches(Config config, NetworkDatacenter networkDatacenter) throws
            Exception {
        List<AggregateSwitch> aggregateSwitches = new ArrayList<>();
        AbstractFactory<AbstractSwitch, Config> aggregateSwitchFactory = new SwitchFactory(simulation,
                networkDatacenter);

        for (int i = 0; i < config.getInt(NUM_SWITCHES); i++) {
            AggregateSwitch aggregateSwitch = (AggregateSwitch) aggregateSwitchFactory.createInstance(config);
            aggregateSwitches.add(aggregateSwitch);
            networkDatacenter.addSwitch(aggregateSwitch);
        }
        return aggregateSwitches;
    }


}
