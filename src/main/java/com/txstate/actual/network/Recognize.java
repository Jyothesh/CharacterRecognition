package com.txstate.actual.network;

import com.txstate.actual.network.digit.DataImage;
import com.txstate.actual.network.digit.generator.TrainDataGenerator;
import com.txstate.actual.network.neural.Backpropagation;
import com.txstate.actual.network.neural.Layer;
import com.txstate.actual.network.neural.NeuralNetwork;
import com.txstate.actual.network.neural.Neuron;
import com.txstate.actual.network.neural.activators.LinearActivation;
import com.txstate.actual.network.neural.activators.SigmoidActivation;
import com.txstate.actual.network.neural.generator.DataTrain;
import com.txstate.actual.network.service.LoadImageService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

public class Recognize {

    public static void main(String[] args) throws IOException {

        LoadImageService trainingService = new LoadImageService("/train/train-labels-idx1-ubyte.dat", "/train/train-images-idx3-ubyte.dat");
        LoadImageService testService = new LoadImageService("/test/t10k-labels-idx1-ubyte.dat", "/test/t10k-images-idx3-ubyte.dat");

        NeuralNetwork neuralNetwork = new NeuralNetwork("Recognize");

        Neuron inputBias = new Neuron(new LinearActivation());
        inputBias.setOutput(1);

        Layer inputLayer = new Layer(null, inputBias);

        for(int i = 0; i < LoadImageService.ROWS * LoadImageService.COLUMNS; i++) {
            Neuron neuron = new Neuron(new SigmoidActivation());
            neuron.setOutput(0);
            inputLayer.addNeuron(neuron);
        }

        Neuron hiddenBias = new Neuron(new LinearActivation());
        hiddenBias.setOutput(1);

        Layer hiddenLayer = new Layer(inputLayer, hiddenBias);

        long numberOfHiddenNeurons = Math.round((2.0 / 3.0) * (LoadImageService.ROWS * LoadImageService.COLUMNS) + 10);

        for(int i = 0; i < numberOfHiddenNeurons; i++) {
            Neuron neuron = new Neuron(new SigmoidActivation());
            neuron.setOutput(0);
            hiddenLayer.addNeuron(neuron);
        }

        Layer outputLayer = new Layer(hiddenLayer);

        //10 output neurons - 1 for each digit
        for(int i = 0; i < 10; i++) {
            Neuron neuron = new Neuron(new SigmoidActivation());
            neuron.setOutput(0);
            outputLayer.addNeuron(neuron);
        }

        neuralNetwork.addLayer(inputLayer);
        neuralNetwork.addLayer(hiddenLayer);
        neuralNetwork.addLayer(outputLayer);

        TrainDataGenerator trainingDataGenerator = new TrainDataGenerator(trainingService.loadDigitImages());
        Backpropagation backpropagator = new Backpropagation(neuralNetwork, 0.1, 0.9, 0);
        backpropagator.train(trainingDataGenerator, 0.005);
        neuralNetwork.persist();

        TrainDataGenerator testDataGenerator = new TrainDataGenerator(testService.loadDigitImages());
        DataTrain testData = testDataGenerator.getTrainingData();

        for(int i = 0; i < testData.getInputs().length; i++) {
            double[] input = testData.getInputs()[i];
            double[] output = testData.getOutputs()[i];

            int digit = 0;
            boolean found = false;
            while(digit < 10 && !found) {
                found = (output[digit] == 1);
                digit++;
            }

            neuralNetwork.setInputs(input);
            double[] receivedOutput = neuralNetwork.getOutput();

            double max = receivedOutput[0];
            double recognizedDigit = 0;
            for(int j = 0; j < receivedOutput.length; j++) {
                if(receivedOutput[j] > max) {
                    max = receivedOutput[j];
                    recognizedDigit = j;
                }
            }

            System.out.println("Recognized " + (digit - 1) + " as " + recognizedDigit + ". Corresponding output value was " + max);
        }
    }
}
