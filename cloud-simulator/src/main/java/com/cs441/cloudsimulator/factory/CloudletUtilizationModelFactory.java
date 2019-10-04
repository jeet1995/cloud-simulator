package com.cs441.cloudsimulator.factory;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelAbstract;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelStochastic;

import static com.cs441.cloudsimulator.configs.ApplicationConstants.*;

/**
 * This class defines a factory to create an instance of the @{@link UtilizationModelAbstract} class.
 */

public class CloudletUtilizationModelFactory implements AbstractFactory<UtilizationModelAbstract, Config> {



    /**
     * This method creates an instance of the @{@link UtilizationModelAbstract} class.
     *
     * @param config The config pertaining to the @{@link org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet} to be created.
     */
    @Override
    public UtilizationModelAbstract createInstance(Config config) {

        String utilizationType = config.getString(CLOUDLET_UTILIZATION_MODEL);

        switch (utilizationType) {

        case UTILIZATION_MODEL_FULL:
            return new UtilizationModelFull();
        case UTILIZATION_MODEL_STOCHASTIC:
            return new UtilizationModelStochastic();
        default:
            return new UtilizationModelFull();

        }

    }
}
