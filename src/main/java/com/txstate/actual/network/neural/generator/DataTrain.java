package com.txstate.actual.network.neural.generator;

public class DataTrain {

    private double[][] inputs;
    private double[][] outputs;

    public DataTrain(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public double[][] getOutputs() {
        return outputs;
    }
}
