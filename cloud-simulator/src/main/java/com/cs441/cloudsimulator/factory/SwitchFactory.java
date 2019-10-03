package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.switches.AbstractSwitch;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.RootSwitch;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;


/**
 * This class defines a factory to create an instance of the @{@link AbstractSwitch} class.
 */
public class SwitchFactory implements AbstractFactory<AbstractSwitch, Config> {

    private CloudSim simulation;
    private NetworkDatacenter networkDatacenter;

    public SwitchFactory(CloudSim simulation, NetworkDatacenter networkDatacenter) {
        this.simulation = simulation;
        this.networkDatacenter = networkDatacenter;
    }


    /**
     * Creates an instance of either {@link EdgeSwitch}, {@link AggregateSwitch} or {@link RootSwitch} classes.
     *
     * @param config
     * @return abstractSwitch
     */
    @Override
    public AbstractSwitch createInstance(Config config) throws Exception {

        String switchType = config.getString("switchType");

        switch (switchType) {

        case EDGE_SWITCH:
            EdgeSwitch edgeSwitch = new EdgeSwitch(simulation, networkDatacenter);
            edgeSwitch.setPorts(config.getInt("numPorts"));
            edgeSwitch.setSwitchingDelay(config.getDouble("switchingDelay"));
            edgeSwitch.setDownlinkBandwidth(config.getDouble("downlinkBandwidth"));
            return edgeSwitch;
        case AGGREGATE_SWITCH:
            AggregateSwitch aggregateSwitch = new AggregateSwitch(simulation, networkDatacenter);
            aggregateSwitch.setPorts(config.getInt("numPorts"));
            aggregateSwitch.setSwitchingDelay(config.getDouble("switchingDelay"));
            aggregateSwitch.setDownlinkBandwidth(config.getDouble("downlinkBandwidth"));
            return aggregateSwitch;
        case ROOT_SWITCH:
            RootSwitch rootSwitch = new RootSwitch(simulation, networkDatacenter);
            rootSwitch.setPorts(config.getInt("numPorts"));
            rootSwitch.setSwitchingDelay(config.getDouble("switchingDelay"));
            rootSwitch.setDownlinkBandwidth(config.getDouble("downlinkBandwidth"));
            return rootSwitch;

        }
        return null;
    }
}
