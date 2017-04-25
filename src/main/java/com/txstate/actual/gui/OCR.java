package com.txstate.actual.gui;

/**
 * This is the main class that uses the swing for displaying the frames and panels
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.txstate.actual.imageprocess.Entry;
import com.txstate.actual.som.KNetwork;
import com.txstate.actual.som.training.TrainingSet;
import com.txstate.actual.util.Sample;
import com.txstate.actual.util.SampleData;

public class OCR extends JFrame implements Runnable {

	private static final long serialVersionUID = 1991505277097327489L;

	static final int DOWNSAMPLE_WIDTH = 5;
	static final int DOWNSAMPLE_HEIGHT = 7;
	Entry entry;
	Sample sample;
	@SuppressWarnings("rawtypes")
	DefaultListModel letterListModel = new DefaultListModel();
	KNetwork net;
	Thread trainThread = null;

	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JButton downSample = new javax.swing.JButton();
	javax.swing.JButton add = new javax.swing.JButton();
	javax.swing.JButton clear = new javax.swing.JButton();
	javax.swing.JButton recognize = new javax.swing.JButton();
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	javax.swing.JButton load = new javax.swing.JButton();
	javax.swing.JButton save = new javax.swing.JButton();
	javax.swing.JButton train = new javax.swing.JButton();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JLabel tries = new javax.swing.JLabel();
	javax.swing.JLabel lastError = new javax.swing.JLabel();
	javax.swing.JLabel bestError = new javax.swing.JLabel();
	javax.swing.JLabel JLabel8 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();

	public OCR()
	{
		getContentPane().setLayout(null);
		setTitle("Digit Recognizer");
		setSize(500, 500);
		setVisible(false);
		getContentPane().setLayout(null);

		entry = new Entry();
		//entry.reshape(168,25,200,128);
		this.entry.setLocation(20, 50);
		this.entry.setSize(200, 200);

		sample = new Sample(DOWNSAMPLE_WIDTH,DOWNSAMPLE_HEIGHT);
		//sample.reshape(307,210,65,70);
		entry.setSample(sample);

		//Add Button to add it training set
		add.setText("Add");
		add.setActionCommand("Add");
		add.setBounds(250,50,100,24);

		//Clear the Canvas
		clear.setText("Clear");
		clear.setActionCommand("Clear");
		clear.setBounds(360,50,100,24);
		
		//Recognize the Character which is drawn
		recognize.setText("Recognize");
		recognize.setActionCommand("Recognize");
		recognize.setBounds(250,90,100,24);

		//Load the trained network
		load.setText("Load");
		load.setActionCommand("Load");
		load.setBounds(360,90,100,24);

		//Save the Trained Network
		save.setText("Save");
		save.setActionCommand("Save");
		save.setBounds(250,130,100,24);

		//Train the Network
		train.setText("Train");
		train.setActionCommand("Begin Training");
		train.setBounds(360,130,100,24);


		JLabel5.setText("Draw Characters Here");
		getContentPane().add(JLabel5);
		JLabel5.setBounds(20,20,144,12);
		
		getContentPane().add(entry);
		getContentPane().add(add);
		getContentPane().add(clear);
		getContentPane().add(recognize);
		getContentPane().add(load);
		getContentPane().add(save);
		getContentPane().add(train);

		ActionListener lSymAction = new ActionListener();
		clear.addActionListener(lSymAction);
		add.addActionListener(lSymAction);
		load.addActionListener(lSymAction);
		save.addActionListener(lSymAction);
		train.addActionListener(lSymAction);
		recognize.addActionListener(lSymAction);
	}

	class ActionListener implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if ( object == clear )
				clear_actionPerformed(event);
			else if ( object == add )
				add_actionPerformed(event);
			else if ( object == load )
				load_actionPerformed(event);
			else if ( object == save )
				save_actionPerformed(event);
			else if ( object == train )
				train_actionPerformed(event);
			else if ( object == recognize )
				recognize_actionPerformed(event);
		}
	}

	void clear_actionPerformed(java.awt.event.ActionEvent event) {
		entry.clear();
		sample.getData().clear();
		sample.repaint();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	void add_actionPerformed(java.awt.event.ActionEvent event) {
		int i;
		String letter = JOptionPane.showInputDialog("Enter the letter you would like to assign to the character");
		if(letter == null)
			return;

		if(letter.length() > 1) {
			JOptionPane.showMessageDialog(this, "Enter only single letter.","Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		entry.downSample();
		SampleData sampleData = (SampleData)sample.getData().clone();
		sampleData.setLetter(letter.charAt(0));

		for(i=0; i < letterListModel.size(); i++) {
			Comparable str = (Comparable)letterListModel.getElementAt(i);
			if(str.equals(letter)) {
				JOptionPane.showMessageDialog(this, "That letter is already defined, delete it first!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if(str.compareTo(sampleData) > 0) {
				letterListModel.add(i, sampleData);
				return;
			}
		}
		
		letterListModel.add(letterListModel.size(), sampleData);
		entry.clear();
		sample.repaint();
	}

	@SuppressWarnings("unchecked")
	void load_actionPerformed(java.awt.event.ActionEvent event) {
		try {
			FileReader f = new FileReader( new File("E:\\course work\\codeBase\\github\\CharacterRecognition\\src\\main\\resources\\Trained.dat") );
			BufferedReader r = new BufferedReader(f);
			String line;
			int i=0;
			letterListModel.clear();

			while((line = r.readLine()) != null) {
				//System.out.println(line);
				SampleData ds =	new	SampleData(line.charAt(0), OCR.DOWNSAMPLE_WIDTH, OCR.DOWNSAMPLE_HEIGHT); 
				letterListModel.add(i++, ds);
				int idx=2;
				
				for(int y=0; y < ds.getHeight(); y++) {
					for(int x=0; x < ds.getWidth(); x++) {
						ds.setData(x, y, line.charAt(idx++) == '1');
					}
				}
			}

			r.close();
			f.close();
			clear_actionPerformed(null);
			JOptionPane.showMessageDialog(this,	"Loaded from 'Trained'.", "Training", JOptionPane.PLAIN_MESSAGE);
		} catch ( Exception e ) {
			JOptionPane.showMessageDialog(this, "Error: " + e, "Training", JOptionPane.ERROR_MESSAGE);
		}
	}

	void save_actionPerformed(java.awt.event.ActionEvent event) {
		try {
			OutputStream os = new FileOutputStream("E:\\course work\\codeBase\\github\\CharacterRecognition\\src\\main\\resources\\Trained1.dat",false );
			PrintStream ps = new PrintStream(os);

			for(int i=0; i < letterListModel.size(); i++) {
				SampleData ds = (SampleData) letterListModel.elementAt(i);
				ps.print(ds.getLetter() + ":");
				for(int y=0; y < ds.getHeight(); y++) {
					for(int x=0; x < ds.getWidth(); x++) {
						ps.print(ds.getData(x,y) ? "1" : "0");
					}
				}
				ps.println("");
			}

			ps.close();
			os.close();
			clear_actionPerformed(null);
			JOptionPane.showMessageDialog(this, "Saved to 'Trained.dat'.", "Training", JOptionPane.PLAIN_MESSAGE);
		} catch ( Exception e ) {
			JOptionPane.showMessageDialog(this,"Error: " + e, "Training", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void run() {
		try {
			int inputNeuron = OCR.DOWNSAMPLE_HEIGHT * OCR.DOWNSAMPLE_WIDTH;
			int outputNeuron = letterListModel.size();
			TrainingSet set = new TrainingSet(inputNeuron, outputNeuron);
			set.setTrainingSetCount(letterListModel.size());

			for(int t=0; t < letterListModel.size(); t++) {
				int idx=0;
				SampleData ds = (SampleData) letterListModel.getElementAt(t);
				for(int y=0; y < ds.getHeight(); y++) {
					for(int x=0; x < ds.getWidth(); x++) {
						set.setInput(t,idx++,ds.getData(x,y)?.5:-.5);
					}
				}
			}

			net = new KNetwork(inputNeuron, outputNeuron, this);
			net.setTrainingSet(set);
			net.learn();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this,"Error: " + e, "Training", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateStats(long trial,double error,double best) {
		if((((trial%100)!=0) || (trial==10)) && !net.halt)
			return;

		if(net.halt) {
			trainThread = null;
			train.setText("Training");
			JOptionPane.showMessageDialog(this, "Training has Done", "Training", JOptionPane.PLAIN_MESSAGE);
		}
		
		UpdateStats stats = new UpdateStats();
		stats._tries = trial;
		stats._lastError=error;
		stats._bestError=best;
		try {
			SwingUtilities.invokeAndWait(stats);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this,"Error: " + e, "Training", JOptionPane.ERROR_MESSAGE);
		}
	}

	void train_actionPerformed(java.awt.event.ActionEvent event) {
		if(trainThread == null) {
			train.setText("Stop");
			train.repaint();
			trainThread = new Thread(this);
			trainThread.start();
		} else {
			net.halt=true;
		}
	}

	void recognize_actionPerformed(java.awt.event.ActionEvent event) {
		if(net == null) {
			JOptionPane.showMessageDialog(this,	"Network need to be trained first", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		entry.downSample();
		double input[] = new double[5*7];
		int idx=0;
		SampleData ds = sample.getData();
		
		for(int y=0; y < ds.getHeight(); y++) {
			for(int x=0; x < ds.getWidth(); x++) {
				input[idx++] = ds.getData(x,y)?.5:-.5;
			}
		}

		double normfac[] = new double[1];
		double synth[] = new double[1];
		int best = net.winner(input, normfac, synth);
		char map[] = mapNeurons();
		JOptionPane.showMessageDialog(this," The Letter is : " + map[best] , "Recognize", JOptionPane.PLAIN_MESSAGE);
		clear_actionPerformed(null);
	}

	char[] mapNeurons() {
		char map[] = new char[letterListModel.size()];
		double normfac[] = new double[1];
		double synth[] = new double[1];

		for(int i=0; i < map.length; i++)
			map[i]='?';
		for(int i=0; i < letterListModel.size(); i++) {
			double input[] = new double[5*7];
			int idx=0;
			SampleData ds = (SampleData) letterListModel.getElementAt(i);
			
			for(int y=0; y < ds.getHeight(); y++) {
				for(int x=0; x < ds.getWidth(); x++) {
					input[idx++] = ds.getData(x,y)?.5:-.5;
				}
			}

			int best = net.winner(input, normfac, synth);
			map[best] = ds.getLetter();
		}
		return map;
	}

	public class UpdateStats implements Runnable {
		long _tries;
		double _lastError;
		double _bestError;

		public void run()
		{
			tries.setText(""+_tries);
			lastError.setText(""+_lastError);
			bestError.setText(""+_bestError);
		}
	}
}