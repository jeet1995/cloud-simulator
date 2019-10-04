package com.cs441.cloudsimulator.utils;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Identifiable;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.builders.tables.TableColumn;

import java.util.List;

/**
 * This class displays cloudlet execution data in a tabular format.
 */

public class DecoratedCloudletsTableBuilder extends CloudletsTableBuilder {
    private static final String TIME_FORMAT = "%.0f";
    private static final String SECONDS = "Seconds";
    private static final String CPU_CORES = "CPU cores";


    public DecoratedCloudletsTableBuilder(List<? extends Cloudlet> list) {
        super(list);
    }

    @Override
    protected void createTableColumns() {
        final String ID = "ID";
        addColumnDataFunction(getTable().addColumn("Cloudlet", ID), Identifiable::getId);
        addColumnDataFunction(getTable().addColumn("Status "), cloudlet -> cloudlet.getStatus().name());
        addColumnDataFunction(getTable().addColumn("DC", ID), cloudlet -> cloudlet.getVm().getHost().getDatacenter()
                .getId());
        addColumnDataFunction(getTable().addColumn("Host", ID), cloudlet -> cloudlet.getVm().getHost().getId());
        addColumnDataFunction(getTable().addColumn("Host PEs ", CPU_CORES), cloudlet -> cloudlet.getVm().getHost()
                .getWorkingPesNumber());
        addColumnDataFunction(getTable().addColumn("VM", ID), cloudlet -> cloudlet.getVm().getId());
        addColumnDataFunction(getTable().addColumn("VM PEs   ", CPU_CORES), cloudlet -> cloudlet.getVm()
                .getNumberOfPes());
        addColumnDataFunction(getTable().addColumn("CloudletLen", "MI"), Cloudlet::getLength);
        addColumnDataFunction(getTable().addColumn("CloudletPEs", CPU_CORES), Cloudlet::getNumberOfPes);

        TableColumn col = getTable().addColumn("StartTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, Cloudlet::getExecStartTime);

        col = getTable().addColumn("FinishTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cl -> roundTime(cl, cl.getFinishTime()));

        col = getTable().addColumn("ExecTime", SECONDS).setFormat(TIME_FORMAT);
        addColumnDataFunction(col, cl -> roundTime(cl, cl.getActualCpuTime()));

        addColumnDataFunction(getTable().addColumn("Cloudlet Type"), Cloudlet::getClass);
        addColumnDataFunction(getTable().addColumn("Accumulated Bandwidth Cost"), Cloudlet::getAccumulatedBwCost);
        addColumnDataFunction(getTable().addColumn("Actual CPU time"), Cloudlet::getActualCpuTime);
        addColumnDataFunction(getTable().addColumn("Actual RAM utilization"), Cloudlet::getUtilizationOfRam);


    }

    private double roundTime(final Cloudlet cloudlet, final double time) {
        final double fraction = cloudlet.getExecStartTime() - (int) cloudlet.getExecStartTime();
        return Math.round(time - fraction);
    }

}
