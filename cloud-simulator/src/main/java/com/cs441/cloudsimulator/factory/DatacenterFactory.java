package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterCharacteristics;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;

import java.util.ArrayList;
import java.util.List;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

public class DatacenterFactory implements AbstractFactory<Datacenter, Config> {

    private Simulation simulation;

    public DatacenterFactory(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public Datacenter createInstance(Config config) {
        List<? extends Config> hostConfigs = config.getConfigList(HOSTS);
        List<Host> hosts = new ArrayList<>();

        for (int i = 0; i < hostConfigs.size(); i++) {

            List<? extends Config> peConfigs = hostConfigs.get(i).getConfigList(PES);
            List<Pe> pes = new ArrayList<>();
            peConfigs.forEach(peConfig -> pes.add(new PeSimple(peConfig.getInt(PE_ID), peConfig.getDouble(MIPS), new
                    PeProvisionerSimple())));
            hosts.addAll(getHosts(hostConfigs.get(i), pes));
        }

        AbstractFactory<VmAllocationPolicy, Config> vmAllocationPolicyFactory = new VmAllocationPolicyFactory();

        Datacenter datacenter = new DatacenterSimple(this.simulation, hosts, vmAllocationPolicyFactory.createInstance
                (config));


        AbstractFactory<DatacenterCharacteristics, Config> datacenterCharacteristicsFactory = new
                DatacenterCharacteristicsFactory(datacenter);
        datacenterCharacteristicsFactory.createInstance(config);

        return datacenter;
    }

    private static List<Host> getHosts(Config hostConfig, List<Pe> pes) {
        int numHosts = hostConfig.getInt("numHosts");
        List<Host> hosts = new ArrayList<>();

        for (int i = 0; i < numHosts; i++) {
            Host host = new HostSimple(hostConfig.getLong(RAM), hostConfig.getLong(BW), hostConfig.getLong(STORAGE),
                    pes, true);
            hosts.add(host);
        }
        return hosts;
    }


}
