package com.cs441.cloudsimulator.jobs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.core.CloudSim;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobTrackerTest {

    public List<Mapper> mappers = new ArrayList<>();
    public List<Reducer> reducers = new ArrayList<>();
    public Config mapReduceJobConfig = ConfigFactory.load("map-reduce-job-test.conf");
    public JobTracker jobTracker = new JobTracker(new CloudSim(), mapReduceJobConfig);
    public Map<Mapper, Reducer> mapperReducerMap = new HashMap<>();

    @Before
    public void setUp() throws Exception {

        for (int i = 0; i < 2; i++) {
            Mapper mapper = new Mapper(2, 2);
            mapper.setId(i);
            mappers.add(mapper);
        }

        for (int i = 0; i < 1; i++) {
            Reducer reducer = new Reducer(2, 2);
            reducer.setId(i);
            reducers.add(reducer);
        }

        mapperReducerMap.put(mappers.get(0), reducers.get(0));
        mapperReducerMap.put(mappers.get(1), reducers.get(0));

    }

    @Test
    public void resolveMapperReducerPair() {
        jobTracker.resolveMapperReducerPair(mappers, reducers);
        Map<Mapper, Reducer> map = jobTracker.getMapperReducerMap();

        Assert.assertEquals(2, map.entrySet().size());
        Assert.assertEquals(reducers.get(0), map.get(mappers.get(0)));
        Assert.assertEquals(reducers.get(0), map.get(mappers.get(1)));

    }
}