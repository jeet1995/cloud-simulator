package com.cs441.cloudsimulator.builder;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

public class DatacenterFactory {

    public static Datacenter createDatacenter(Config config) throws Exception {

        List<? extends Config> hostConfigs = config.getConfigList(HOSTS);
        List<Host> hosts = new ArrayList<>();

        for (int i = 0; i < hostConfigs.size(); i++) {
            List<? extends Config> peConfigs = hostConfigs.get(i).getConfigList(PES);
            List<Pe> pes = new ArrayList<>();
            for (int j = 0; j < peConfigs.size(); j++) {
                Config peConfig = peConfigs.get(j);
                pes.add(new Pe(peConfig.getInt(PE_ID), new PeProvisionerSimple(peConfig.getInt(MIPS))));
            }
            Config hostConfig = hostConfigs.get(i);
            hosts.add(new Host(hostConfig.getInt(HOST_ID), new RamProvisionerSimple(hostConfig.getInt(RAM)), new
                    BwProvisionerSimple(hostConfig.getInt(BW)), hostConfig.getInt(STORAGE), pes, new
                    VmSchedulerTimeShared(pes)));
        }

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(config.getString(ARCHITECTURE),
                config.getString(OS), config.getString(VMM), hosts, 0D, config.getDouble(COST_PER_SEC), config
                .getDouble(COST_PER_MEM), config.getDouble(COST_PER_STORAGE), config.getDouble(COST_PER_BW));

        return new Datacenter(config.getString(NAME), characteristics, new VmAllocationPolicySimple(hosts), new
                LinkedList<>(), 0);
    }


}
