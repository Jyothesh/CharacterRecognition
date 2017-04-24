package com.txstate.actual.network.neural.activators;

import java.io.Serializable;

public class LinearActivation implements Activation, Serializable {
    public double activate(double weightedSum) {
        return weightedSum;
    }

    public double derivative(double weightedSum) {
        return 1;
    }

    public Activation copy() {
        return new LinearActivation();
    }
}
