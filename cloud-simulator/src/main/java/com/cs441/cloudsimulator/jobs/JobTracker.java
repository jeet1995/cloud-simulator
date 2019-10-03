package com.cs441.cloudsimulator.jobs;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.vms.network.NetworkVm;
import org.cloudsimplus.listeners.CloudletEventInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.GLOBAL_REDUCER_BROKER;


/**
 * This class creates a mapping between the instances of class @{@link Mapper} and @{@link Reducer}.
 * */
public class JobTracker {

    private Map<Mapper, Reducer> mapperReducerMap;
    private DatacenterBroker reducerBroker;
    private List<NetworkVm> vms;

    public JobTracker(CloudSim sim) {
        mapperReducerMap = new HashMap<>();
        reducerBroker = new DatacenterBrokerSimple(sim, GLOBAL_REDUCER_BROKER);
        vms = new ArrayList<>();
    }

    /**
     * This method listens to an instance of class @{@link Mapper} which corresponds to an event when the Mapper finishes. It then
     * adds the corresponding instance of the class @{@link Reducer} and tries to submit it through a reducerBroker.
     *
     * @param eventInfo An event generated by a Mapper finishing.
     * */
    public void onMapperFinishListenerSubmitReducer(CloudletEventInfo eventInfo) {
        Mapper mapper = (Mapper) eventInfo.getCloudlet();
        System.out.println("Mapper " + mapper.getId() + " finished" + ", Reducer is " + mapperReducerMap.get(mapper)
                .getClass().toString());

        if (this.vms.isEmpty()) {
            NetworkVm networkVm =  new NetworkVm(1000, 2);
            this.vms.add(networkVm);
        }


        /**
         * Submit vms specific to reducers.
         * */

        if (reducerBroker.getVmWaitingList().size() == 0) {
            reducerBroker.submitVmList(vms);
        }

        Reducer reducer = mapperReducerMap.get(mapper);
        reducer.setBroker(reducerBroker);
        reducer.setVm(vms.get(0));

        reducerBroker.submitCloudlet(reducer);

    }


    /**
     * This method creates a mapping between instances of @{@link Mapper} and @{@link Reducer}.
     *
     * @param mappers List of mappers.
     * @param reducers List of reducers.
     * */
    public void resolveMapperReducerPair(List<Mapper> mappers, List<Reducer> reducers) {

        if (mappers.size() > reducers.size()) {
            for (int i = 0; i < mappers.size(); i++) {
                mapperReducerMap.put(mappers.get(i), reducers.get(i % reducers.size()));
            }
        } else {
            for (int i = 0; i < reducers.size(); i++) {
                mapperReducerMap.put(mappers.get(i % mappers.size()), reducers.get(i));
            }
        }
    }

    public DatacenterBroker getReducerBroker() {
        return reducerBroker;
    }

    public Map<Mapper, Reducer> getMapperReducerMap() {
        return mapperReducerMap;
    }
}