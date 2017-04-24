package com.txstate.actual.som;

/**
 * Abstract class for the Kohonen neural network
 * This class has methods to calculate the dot product, size, etc
 */
import java.util.*;

import com.txstate.actual.som.training.TrainingSet;

abstract public class Network {

	public final static double NEURON_ON=0.9;
	public final static double NEURON_OFF=0.1;
	protected double output[];
	protected double totalError;
	protected int inputNeuronCount;
	protected int outputNeuronCount;
	protected Random random = new Random(System.currentTimeMillis());

	abstract public void learn() throws RuntimeException;
	abstract void trial(double input[]);

	public double[] getOutput() {
		return output;
	}

	public double calculateTrialError(TrainingSet train) throws RuntimeException {
		int i, tclass ;
		double diff ;
		totalError = 0.0 ;

		for(int t=0; t < train.getTrainingSetCount(); t++) {
			trial(train.getOutputSet(t));
			tclass = (int)(train.getClassify(train.getInputCount()-1));

			for (i=0; i < train.getOutputCount(); i++) {
				if (tclass == i ) {
					diff = NEURON_ON - output[i] ; 
				} else {
					diff = NEURON_OFF - output[i] ;
				}
				totalError += diff * diff ;
			}

			for (i=0; i < train.getOutputCount(); i++) {
				diff = train.getOutput(t,i) - output[i] ;
				totalError += diff * diff ;
			}
		}

		totalError /= (double) train.getTrainingSetCount(); ;
		return totalError;
	}

	public static double vectorLength(double v[]) {
		double rtn = 0.0 ;
		for ( int i=0;i<v.length;i++ )
			rtn += v[i] * v[i];
		return rtn;
	}

	public double dotProduct(double vec1[], double vec2[]) {
		int k, m,v;
		double rtn = 0.0;

		k = vec1.length / 4;
		m = vec1.length % 4;

		v = 0;
		while ((k--) > 0) {
			rtn += vec1[v] * vec2[v];
			rtn += vec1[v+1] * vec2[v+1];
			rtn += vec1[v+2] * vec2[v+2];
			rtn += vec1[v+3] * vec2[v+3];
			v+=4;
		}

		while ((m--) > 0) {
			rtn += vec1[v] * vec2[v];
			v++;
		}

		return rtn;
	}

	public void randomizeWeights( double weight[][] ) {
		double r ;
		int temp = (int)(3.464101615 / (2. * Math.random())) ;

		for (int y=0; y < weight.length; y++) {
			for (int x=0; x < weight[0].length; x++) {
				r = (double) random.nextInt(Integer.MAX_VALUE) + (double) 
						random.nextInt(Integer.MAX_VALUE) -
						(double) random.nextInt(Integer.MAX_VALUE) - (double) 
						random.nextInt(Integer.MAX_VALUE) ;
				weight[y][x] = temp * r ;
			}
		}
	}
}