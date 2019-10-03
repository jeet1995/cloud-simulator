package com.cs441.cloudsimulator.factory;

public interface AbstractFactory<T, O> {
    T createInstance(O object) throws Exception;
}
