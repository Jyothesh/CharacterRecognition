package com.txstate.actual.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.txstate.actual.network.neural.NeuralNetwork;

public class DigitRecognitionControllerTest {
	
	private double first = 0;
	private String imageData;
	NeuralNetwork neuralNetwork;
	
	@Before
	public void setUp() {
    	try {
            ObjectInputStream in = new ObjectInputStream(new ClassPathResource("Recognize-1492568619714.net").getInputStream());
            neuralNetwork = (NeuralNetwork) in.readObject();
        } catch(Exception e) {
        	System.out.println(e);
        }
	}

	@Test
	public void testZero() {
		//fail("Not yet implemented");
		imageData = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000110000000000000000000000000111000000000000000000000000111111111100000000000000000111111111111100000000000000011111111111111000000000000011111111111111111100000000001111100111111111110000000000111110000111111111000000000011111000001111111100000000001111100000000111110000000000111100000000000111000000000011110000000000111100000000001111000000000011110000000000111100000000011111000000000011110000000011111100000000001111100000001111110000000000111111111111111111000000000011111111111111111100000000001111111111111111100000000000001111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		
		double[] inputs = new double[imageData.length()];
        for(int i = 0; i < imageData.length(); i++) {
            inputs[i] = Double.parseDouble(String.valueOf(imageData.charAt(i)));
        }

        neuralNetwork.setInputs(inputs);

        double[] output = neuralNetwork.getOutput();
        double second = 0;
        double third = 0;

        double firstConfidence = output[0];
        double secondConfidence = output[0];
        double thirdConfidence = output[0];
        
        for(int j = 0; j < output.length; j++) {
            if(output[j] > firstConfidence) {
                thirdConfidence = secondConfidence;
                secondConfidence = firstConfidence;
                firstConfidence = output[j];

                third = second;
                second = first;
                first = j;
            }

            else if(output[j] > secondConfidence) {
                thirdConfidence = secondConfidence;
                secondConfidence = output[j];

                third = second;
                second = j;
            }

            else if(output[j] > thirdConfidence) {
                thirdConfidence = output[j];
                third = j;
            }
        }
        
        assertEquals(0.0, first, 0);
	}
	
	@Test
	public void testEight() {
		//fail("Not yet implemented");
		imageData = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011110000000000000000000000001111111100000000000000000000111111110000000000000000000011111111111000000000000000001110111111100000000000000000111000111110000000000000000011110000111000000000000000001111111111100000000000000000111111111110000000000000000011111111111110000000000000000001111111111100000000000000000000111111110000000000000000000111111111000000000000000000111111001100000000000000000011111000110000000000000000001110000011000000000000000000111000011100000000000000000011111111110000000000000000001111111111000000000000000000111111111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		
		double[] inputs = new double[imageData.length()];
        for(int i = 0; i < imageData.length(); i++) {
            inputs[i] = Double.parseDouble(String.valueOf(imageData.charAt(i)));
        }

        neuralNetwork.setInputs(inputs);

        double[] output = neuralNetwork.getOutput();
        double second = 0;
        double third = 0;

        double firstConfidence = output[0];
        double secondConfidence = output[0];
        double thirdConfidence = output[0];
        
        for(int j = 0; j < output.length; j++) {
            if(output[j] > firstConfidence) {
                thirdConfidence = secondConfidence;
                secondConfidence = firstConfidence;
                firstConfidence = output[j];

                third = second;
                second = first;
                first = j;
            }

            else if(output[j] > secondConfidence) {
                thirdConfidence = secondConfidence;
                secondConfidence = output[j];

                third = second;
                second = j;
            }

            else if(output[j] > thirdConfidence) {
                thirdConfidence = output[j];
                third = j;
            }
        }
        
        assertEquals(8.0, first, 0);
	}

}
