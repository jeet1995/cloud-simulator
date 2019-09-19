package com.cs441.cloudsimulator;

import com.cs441.cloudsimulator.builder.DatacenterBuilder;
import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CloudSimulationRunner {

    public static void main(String[] args) throws Exception {

        CloudSim.init(1, Calendar.getInstance(), true);

        Config cloudServicesProviderConfig = ConfigFactory.load(CLOUD_SERVICES_PROVIDER_CONF);
        List<? extends Config> datacenterConfigs = cloudServicesProviderConfig.getConfigList(DATACENTERS);
        int numDatacenters = cloudServicesProviderConfig.getConfigList(DATACENTERS).size();
        List<Datacenter> datacenters = new ArrayList<>();
        for (int i = 0; i < numDatacenters; i++) {
            datacenters.add(DatacenterBuilder.buildDatacenter(datacenterConfigs.get(i)));
        }

        System.out.println(datacenters);
    }

}
