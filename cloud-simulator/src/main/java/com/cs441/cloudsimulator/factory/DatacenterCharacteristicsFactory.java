package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterCharacteristics;
import org.cloudbus.cloudsim.datacenters.DatacenterCharacteristicsSimple;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

public class DatacenterCharacteristicsFactory implements AbstractFactory<DatacenterCharacteristics, Config> {

    private Datacenter datacenter;

    public DatacenterCharacteristicsFactory(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

    @Override
    public DatacenterCharacteristics createInstance(Config config) {

        DatacenterCharacteristics datacenterCharacteristics = new DatacenterCharacteristicsSimple(this.datacenter);

        datacenterCharacteristics.setCostPerBw(config.getDouble(COST_PER_BW));
        datacenterCharacteristics.setCostPerMem(config.getDouble(COST_PER_MEM));
        datacenterCharacteristics.setCostPerSecond(config.getDouble(COST_PER_SEC));
        datacenterCharacteristics.setCostPerStorage(config.getDouble(COST_PER_STORAGE));
        datacenterCharacteristics.setOs(OS);
        datacenterCharacteristics.setVmm(VMM);

        return datacenterCharacteristics;
    }
}
