package com.txstate.actual.network.digit.generator;

import com.txstate.actual.network.digit.DataImage;
import com.txstate.actual.network.neural.generator.DataTrain;
import com.txstate.actual.network.neural.generator.DataGenerator;
import com.txstate.actual.network.service.LoadImageService;

import java.util.*;

public class TrainDataGenerator implements DataGenerator {

    Map<Integer, List<DataImage>> labelToDigitImageListMap;
    int[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public TrainDataGenerator(List<DataImage> digitImages) {
        labelToDigitImageListMap = new HashMap<Integer, List<DataImage>>();

        for (DataImage digitImage: digitImages) {

            if (labelToDigitImageListMap.get(digitImage.getLabel()) == null) {
                labelToDigitImageListMap.put(digitImage.getLabel(), new ArrayList<DataImage>());
            }

            labelToDigitImageListMap.get(digitImage.getLabel()).add(digitImage);
        }
    }

    public DataTrain getTrainingData() {
        digits = shuffle(digits);

        double[][] inputs = new double[10][LoadImageService.ROWS * LoadImageService.COLUMNS];
        double[][] outputs = new double[10][10];

        for(int i = 0; i < 10; i++) {
            inputs[i] = getRandomImageForLabel(digits[i]).getData();
            outputs[i] = getOutputFor(digits[i]);
        }

        return new DataTrain(inputs, outputs);
    }

    private int[] shuffle(int[] array) {

        Random random = new Random();
        for(int i = array.length - 1; i > 0; i--) {

            int index = random.nextInt(i + 1);

            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }

        return array;
    }

    private DataImage getRandomImageForLabel(int label) {
        Random random = new Random();
        List<DataImage> images = labelToDigitImageListMap.get(label);
        return images.get(random.nextInt(images.size()));
    }

    private double[] getOutputFor(int label) {
        double[] output = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        output[label] = 1;
        return output;
    }
}
