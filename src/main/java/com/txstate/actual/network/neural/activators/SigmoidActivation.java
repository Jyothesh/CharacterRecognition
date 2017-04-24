package com.txstate.actual.network.neural.activators;

import java.io.Serializable;

public class SigmoidActivation implements Activation, Serializable {
    public double activate(double weightedSum) {
        return 1.0 / (1 + Math.exp(-1.0 * weightedSum));
    }

    public double derivative(double weightedSum) {
        return weightedSum * (1.0 - weightedSum);
    }

    public SigmoidActivation copy() {
        return new SigmoidActivation();
    }
}
