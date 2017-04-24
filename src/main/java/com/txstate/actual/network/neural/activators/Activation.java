package com.txstate.actual.network.neural.activators;

public interface Activation {
    double activate(double weightedSum);
    double derivative(double weightedSum);
    Activation copy();
}
