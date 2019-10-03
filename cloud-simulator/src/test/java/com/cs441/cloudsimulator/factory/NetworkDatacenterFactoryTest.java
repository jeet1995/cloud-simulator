package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

public class NetworkDatacenterFactoryTest {

    public NetworkDatacenterFactory networkDatacenterFactory;
    public Config datacentersConfig;
    public NetworkDatacenter networkDatacenter;

    @Before
    public void setUp() {
        datacentersConfig = ConfigFactory.load("cloud-service-providers-test.conf").getConfigList
                (CLOUD_SERVICE_PROVIDERS).get(0).getConfig(DATACENTER);
        networkDatacenterFactory = new NetworkDatacenterFactory(new CloudSim(), datacentersConfig);
        networkDatacenter = new NetworkDatacenter(new CloudSim(), new ArrayList<>(), VmAllocationPolicy.NULL);
    }

    @Test
    public void createEdgeSwitches() throws Exception {
        List<EdgeSwitch> edgeSwitchList = networkDatacenterFactory.createEdgeSwitches(datacentersConfig.getConfig
                (EDGE_SWITCH), networkDatacenter);
        Assert.assertEquals(25, edgeSwitchList.size());
    }

    @Test
    public void createAggregateSwitches() throws Exception {
        List<AggregateSwitch> aggregateSwitchList = networkDatacenterFactory.createAggregateSwitches
                (datacentersConfig.getConfig(AGGREGATE_SWITCH), networkDatacenter);
        Assert.assertEquals(5, aggregateSwitchList.size());

    }
}