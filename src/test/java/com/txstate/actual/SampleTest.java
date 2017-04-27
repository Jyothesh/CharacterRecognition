package com.txstate.actual;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.springframework.core.io.ClassPathResource;

import com.txstate.actual.network.neural.NeuralNetwork;

public class SampleTest {

	public static void main(String[] args) {
    	String imageData = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000110000000000000000000000000111000000000000000000000000111111111100000000000000000111111111111100000000000000011111111111111000000000000011111111111111111100000000001111100111111111110000000000111110000111111111000000000011111000001111111100000000001111100000000111110000000000111100000000000111000000000011110000000000111100000000001111000000000011110000000000111100000000011111000000000011110000000011111100000000001111100000001111110000000000111111111111111111000000000011111111111111111100000000001111111111111111100000000000001111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    	
    	try {
            ObjectInputStream in = new ObjectInputStream(new ClassPathResource("Recognize-1492568619714.net").getInputStream());
            NeuralNetwork neuralNetwork = (NeuralNetwork) in.readObject();

            double[] inputs = new double[imageData.length()];
            for(int i = 0; i < imageData.length(); i++) {
                inputs[i] = Double.parseDouble(String.valueOf(imageData.charAt(i)));
            }

            neuralNetwork.setInputs(inputs);

            double[] output = neuralNetwork.getOutput();

            double first = 0;
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
            
            System.out.println("The Digit is : " + first);

        }

        catch(IOException e) {
        	System.out.println(e);
        }

        catch(ClassNotFoundException e) {
        	System.out.println(e);
        }
    	
    }

}
