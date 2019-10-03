package com.cs441.cloudsimulator.jobs;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletTask;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelStochastic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

/**
 * This class models the ResourceManager in the context of a Map-Reduce implementation, say Hadoop. It shards data
 * across various mappers and reducers and defines the execution units (VMs in our case) on which each task is to run.
 */

public class ResourceManager {

    private List<Mapper> mappers;
    private List<Reducer> reducers;
    private JobTracker jobTracker;
    private DatacenterBroker datacenterBroker;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    public ResourceManager(DatacenterBroker datacenterBroker, JobTracker jobTracker) {
        this.datacenterBroker = datacenterBroker;
        this.jobTracker = jobTracker;
    }

    /**
     * Initialize mappers and reducers associated with the Job
     *
     * @param config MapReduceJob config
     */
    public void executeJob(Config config) {

        LOGGER.info("Loading properties pertaining to map-reduce job config");

        int inputFileSizeInGBs = config.getInt(INPUT_FILE_SIZE);
        int inputFileSizePerMapperInMBs = config.getInt(INPUT_FILE_SIZE_PER_MAPPER);
        int numMappers = getNumMappers(inputFileSizeInGBs, inputFileSizePerMapperInMBs);
        int numReducers = getNumReducers(numMappers, config.getInt(OUTPUT_FILE_SIZE_PER_MAPPER), config.getInt
                (INPUT_FILE_SIZE_PER_REDUCER));

        LOGGER.info("Creating {} mappers pertaining to the map-reduce job", numMappers);

        mappers = computeAndCreateMappers(numMappers, config);

        LOGGER.info("Creating {} reducers pertaining to the map-reduce job", numReducers);

        reducers = computeAndCreateReducers(numReducers, config);

        LOGGER.info("Resolving the mapper-reducer pair.");

        jobTracker.resolveMapperReducerPair(mappers, reducers);
    }


    /**
     * @param config     MapReduceJob config
     * @param numMappers The no. of mappers required for our job
     */
    public List<Mapper> computeAndCreateMappers(int numMappers, Config config) {

        List<Mapper> mappers = new ArrayList<>();


        for (int i = 0; i < numMappers; i++) {
            Mapper mapper = new Mapper(config.getInt(TASK_LENGTH_PER_MAPPER), config.getInt(PES_PER_MAPPER));
            mapper.setId(generateUniqueId(i));
            mapper.setBroker(datacenterBroker);
            mapper.setOutputSize(config.getInt(OUTPUT_FILE_SIZE_PER_MAPPER));
            mapper.setMemory(config.getInt(MEMORY_NEEDED_PER_MAPPER));
            mapper.setFileSize(config.getInt(INPUT_FILE_SIZE_PER_MAPPER));
            mapper.setUtilizationModel(new UtilizationModelStochastic());
            mapper.addOnFinishListener(jobTracker::onMapperFinishListenerSubmitReducer);
            CloudletTask task = new CloudletExecutionTask(mapper.getTasks().size(), config.getInt
                    (TASK_LENGTH_PER_MAPPER));
            task.setMemory(4000);
            mapper.addTask(task);
            mappers.add(mapper);
        }

        return mappers;
    }

    /**
     * @param config      MapReduceJob config
     * @param numReducers The no. of reducers required for our job
     */
    public List<Reducer> computeAndCreateReducers(int numReducers, Config config) {
        List<Reducer> reducers = new ArrayList<>();

        for (int i = 0; i < numReducers; i++) {
            Reducer reducer = new Reducer(config.getInt(TASK_LENGTH_PER_REDUCER), config.getInt(PES_PER_REDUCER));
            reducer.setId(generateUniqueId(i));
            reducer.setBroker(datacenterBroker);
            reducer.setOutputSize(config.getInt(OUTPUT_FILE_SIZE_PER_REDUCER));
            reducer.setMemory(config.getInt(MEMORY_NEEDED_PER_REDUCER));
            reducer.setUtilizationModel(new UtilizationModelStochastic());
            CloudletTask task = new CloudletExecutionTask(reducer.getTasks().size(), config.getInt
                    (TASK_LENGTH_PER_REDUCER));
            task.setMemory(4000);
            reducer.addTask(task);
            reducers.add(reducer);
        }

        return reducers;
    }

    private int getNumMappers(int inputFileSizeInGBs, int inputFileSizePerMapperInMBs) {
        return inputFileSizeInGBs * 1024 / inputFileSizePerMapperInMBs;
    }

    private int getNumReducers(int numMappers, int outputFileSizePerMapper, int inputFileSizePerReducer) {
        return numMappers * outputFileSizePerMapper / inputFileSizePerReducer;
    }

    public List<Mapper> getMappers() {
        return mappers;
    }

    public List<Reducer> getReducers() {
        return reducers;
    }

    private long generateUniqueId(int val) {
        return val + datacenterBroker.getId();
    }
}
