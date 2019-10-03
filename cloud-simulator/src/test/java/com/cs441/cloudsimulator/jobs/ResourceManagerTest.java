package com.cs441.cloudsimulator.jobs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ResourceManagerTest {

    public ResourceManager resourceManager;
    public Config mapReduceJobConf;

    @Before
    public void setUp() throws Exception {

        CloudSim sim = new CloudSim();


        DatacenterBroker datacenterBroker = new DatacenterBrokerSimple(sim);
        JobTracker jobTracker = new JobTracker(sim);

        resourceManager = new ResourceManager(datacenterBroker, jobTracker);
        mapReduceJobConf = ConfigFactory.load("map-reduce-job-test.conf");
    }


    @Test
    public void computeAndCreateMappers() {
        List<Mapper> mappers = resourceManager.computeAndCreateMappers(7, mapReduceJobConf);
        Assert.assertEquals(7, mappers.size());
    }

    @Test
    public void computeAndCreateReducers() {
        List<Reducer> reducers = resourceManager.computeAndCreateReducers(9, mapReduceJobConf);
        Assert.assertEquals(9, reducers.size());
    }

}