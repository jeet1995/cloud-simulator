package com.cs441.cloudsimulator.configs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * This class loads the configuration files with the help of the typesafe library. Read @link{https://github.com/lightbend/config}
 */
public class ApplicationConfig {

    public static final Config cloudServiceProvidersConf = ConfigFactory.load("cloud-service-providers.conf");
    public static final Config mapReduceJobConf = ConfigFactory.load("map-reduce-job.conf");
    public static final Config applicationConf = ConfigFactory.load();
}
