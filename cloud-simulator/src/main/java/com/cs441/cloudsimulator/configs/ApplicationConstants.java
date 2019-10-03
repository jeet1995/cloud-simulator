package com.cs441.cloudsimulator.configs;

public class ApplicationConstants {

    /**
     * Constants pertaining to the cloud services provider configuration
     */
    public static String ARCHITECTURE = ApplicationConfig.applicationConf.getString("ARCHITECTURE");
    public static String CLOUD_SERVICE_PROVIDERS = ApplicationConfig.applicationConf.getString
            ("CLOUD_SERVICE_PROVIDERS");
    public static String DATACENTER = ApplicationConfig.applicationConf.getString("DATACENTER");
    public static String VMS = ApplicationConfig.applicationConf.getString("VMS");
    public static String DATACENTERS = ApplicationConfig.applicationConf.getString("DATACENTERS");
    public static String NUM_DATACENTERS = ApplicationConfig.applicationConf.getString("NUM_DATACENTERS");
    public static String PES = ApplicationConfig.applicationConf.getString("PES");
    public static String RAM = ApplicationConfig.applicationConf.getString("RAM");
    public static String BW = ApplicationConfig.applicationConf.getString("BW");
    public static String STORAGE = ApplicationConfig.applicationConf.getString("STORAGE");
    public static String PE_ID = ApplicationConfig.applicationConf.getString("PE_ID");
    public static String MIPS = ApplicationConfig.applicationConf.getString("MIPS");
    public static String VMM = ApplicationConfig.applicationConf.getString("VMM");
    public static String OS = ApplicationConfig.applicationConf.getString("OS");
    public static String COST_PER_MEM = ApplicationConfig.applicationConf.getString("COST_PER_MEM");
    public static String COST_PER_BW = ApplicationConfig.applicationConf.getString("COST_PER_BW");
    public static String COST_PER_STORAGE = ApplicationConfig.applicationConf.getString("COST_PER_STORAGE");
    public static String COST_PER_SEC = ApplicationConfig.applicationConf.getString("COST_PER_SEC");

    /**
     * Constants required by an instance of the @{@link org.cloudbus.cloudsim.hosts.network.NetworkHost} class.
     */

    public static String NUM_HOSTS = ApplicationConfig.applicationConf.getString("NUM_HOSTS");
    public static String HOSTS = ApplicationConfig.applicationConf.getString("HOSTS");
    public static String VM_SCHEDULER_POLICY = ApplicationConfig.applicationConf.getString("VM_SCHEDULER_POLICY");
    public static final String VM_SCHEDULER_POLICY_TIME_SHARED = "VM_SCHEDULER_POLICY_TIME_SHARED";
    public static final String VM_SCHEDULER_POLICY_SPACE_SHARED = "VM_SCHEDULER_POLICY_SPACE_SHARED";


    /**
     * Constants required by an instance of the @{@link org.cloudbus.cloudsim.vms.network.NetworkVm} class.
     */

    public static String SIZE = ApplicationConfig.applicationConf.getString("SIZE");
    public static String CLOUDLET_SCHEDULER_POLICY = ApplicationConfig.applicationConf.getString
            ("CLOUDLET_SCHEDULER_POLICY");
    public static final String TIME_SHARED = "TIME_SHARED";
    public static final String SPACE_SHARED = "SPACE_SHARED";

    /**
     * Constants required by an instance of the @{@link org.cloudbus.cloudsim.network.switches.AbstractSwitch} class.
     */
    public static final String ROOT_SWITCH = "rootSwitch";
    public static final String AGGREGATE_SWITCH = "aggregateSwitch";
    public static final String EDGE_SWITCH = "edgeSwitch";
    public static String NUM_SWITCHES = ApplicationConfig.applicationConf.getString("NUM_SWITCHES");

    public static String GLOBAL_REDUCER_BROKER = ApplicationConfig.applicationConf.getString("GLOBAL_REDUCER_BROKER");

    /**
     * Constants when mapping components of a network namely -
     *
     * @{@link org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter},
     * @{@link org.cloudbus.cloudsim.brokers.DatacenterBroker} to a network topology with the help of a *.brite file.
     */

    public static String NETWORK_TOPOLOGY = ApplicationConfig.applicationConf.getString("NETWORK_TOPOLOGY");
    public static String BRITE_FILE = ApplicationConfig.applicationConf.getString("BRITE_FILE");
    public static String VM_ALLOCATION_POLICY = ApplicationConfig.applicationConf.getString("VM_ALLOCATION_POLICY");
    public static final String SIMPLE = "SIMPLE";
    public static final String BEST_FIT = "BEST_FIT";
    public static final String WORST_FIT = "WORST_FIT";

    /**
     * Constants pertaining to a map-reduce job
     */

    public static final String INPUT_FILE_SIZE = ApplicationConfig.applicationConf.getString("INPUT_FILE_SIZE");
    public static final String PES_PER_MAPPER = ApplicationConfig.applicationConf.getString("PES_PER_MAPPER");
    public static final String INPUT_FILE_SIZE_PER_MAPPER = ApplicationConfig.applicationConf.getString
            ("INPUT_FILE_SIZE_PER_MAPPER");
    public static final String MEMORY_NEEDED_PER_MAPPER = ApplicationConfig.applicationConf.getString
            ("MEMORY_NEEDED_PER_MAPPER");
    public static final String TASK_LENGTH_PER_MAPPER = ApplicationConfig.applicationConf.getString
            ("TASK_LENGTH_PER_MAPPER");
    public static final String OUTPUT_FILE_SIZE_PER_MAPPER = ApplicationConfig.applicationConf.getString
            ("OUTPUT_FILE_SIZE_PER_MAPPER");
    public static final String PES_PER_REDUCER = ApplicationConfig.applicationConf.getString("PES_PER_REDUCER");
    public static final String MEMORY_NEEDED_PER_REDUCER = ApplicationConfig.applicationConf.getString
            ("MEMORY_NEEDED_PER_REDUCER");
    public static final String INPUT_FILE_SIZE_PER_REDUCER = ApplicationConfig.applicationConf.getString
            ("INPUT_FILE_SIZE_PER_REDUCER");
    public static final String OUTPUT_FILE_SIZE_PER_REDUCER = ApplicationConfig.applicationConf.getString
            ("OUTPUT_FILE_SIZE_PER_REDUCER");
    public static final String TASK_LENGTH_PER_REDUCER = ApplicationConfig.applicationConf.getString
            ("TASK_LENGTH_PER_REDUCER");

}
