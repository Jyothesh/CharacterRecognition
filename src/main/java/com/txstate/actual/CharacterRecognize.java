package com.txstate.actual;

/**
 * This class has the main method that calls the main method for recognizing the character. 
 */
import com.txstate.actual.gui.OCR;

public class CharacterRecognize {

	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		OCR ocr = new OCR();
		ocr.show();
	}
}
