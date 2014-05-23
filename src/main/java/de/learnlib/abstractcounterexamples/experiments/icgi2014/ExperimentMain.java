/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples.experiments.icgi2014;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ExperimentMain {
	
	private static int getIntEnv(String str, int defValue) {
		String val = System.getenv(str);
		
		if(val != null) {
			try {
				return Integer.parseInt(val);
			}
			catch(IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		}
		return defValue;
	}
	
	private static long getLongEnv(String str, long defValue) {
		String val = System.getenv(str);
		
		if(val != null) {
			try {
				return Long.parseLong(val);
			}
			catch(IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		}
		return defValue;
	}
	
	private static boolean getBoolEnv(String str, boolean defValue) {
		String val = System.getenv(str);
		
		if(val != null) {
			try {
				return Boolean.parseBoolean(val);
			}
			catch(IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		}
		return defValue;
	}
	
	public int minCeLength = 50;
	public int maxCeLength = 1000;
	public int ceLengthStep = 10;
	
	public int numInstances = 10;
	
	public int repeatCount = 50;
	
	private int numStates = 500;
	private int alphabetSize = 10;
	
	private String id = null;
	
	private long seed;
	
	private boolean reduceCounterexamples = true;
	
	public ExperimentMain() {
		minCeLength = getIntEnv("MIN_CE_LENGTH", minCeLength);
		maxCeLength = getIntEnv("MAX_CE_LENGTH", maxCeLength);
		ceLengthStep = getIntEnv("CE_LENGTH_STEP", ceLengthStep);
		
		numInstances = getIntEnv("NUM_INSTANCES", numInstances);
		repeatCount = getIntEnv("REPEAT_COUNT", repeatCount);
		
		id = System.getenv("EXPERIMENT_ID");
		
		if(id == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			id = sdf.format(new Date());
		}
	
		numStates = getIntEnv("NUM_STATES", numStates);
		alphabetSize = getIntEnv("ALPHABET_SIZE", alphabetSize);
		
		seed = getLongEnv("RANDOM_SEED", System.nanoTime());
		
		reduceCounterexamples = getBoolEnv("REDUCE_COUNTEREXAMPLES", reduceCounterexamples);
	}
	
	public void run() {
		System.out.println("Starting experiment with ID '" + id + "' using the following configuration:");
		
		System.out.println("==========================================");
		
		System.out.println("MIN_CE_LENGTH = " + minCeLength);
		System.out.println("MAX_CE_LENGTH = " + maxCeLength);
		System.out.println("CE_LENGTH_STEP = " + ceLengthStep);
		System.out.println("NUM_INSTANCES = " + numInstances);
		System.out.println("REPEAT_COUNT = " + repeatCount);
		System.out.println();
		System.out.println("EXPERIMENT_ID = " + id);
		System.err.println();
		System.out.println("NUM_STATES = " + numStates);
		System.out.println("ALPHABET_SIZE = " + alphabetSize);
		System.out.println();
		System.out.println("REDUCE_COUNTEREXAMPLES = " + reduceCounterexamples);
		System.out.println("RANDOM_SEED = " + seed);
		
		System.out.println("==========================================");
		
		File rootDir = new File("results/" + id);
		System.out.println("Storing results in directory " + rootDir.getAbsolutePath());
		
		Random random = new Random(seed);
		
		for(int i = 0; i < numInstances; i++) {
			File instDir = new File(rootDir, String.format("instance-%02d", i));
			System.out.println("Running experiment for instance #" + i + ". This may take some time ...");
			Experiment experiment = new Experiment(instDir, random, numStates, alphabetSize, reduceCounterexamples);
			
			try {
				experiment.run(minCeLength, maxCeLength, ceLengthStep, repeatCount);
				System.out.println("Experiment finished successfully.");
			}
			catch(Exception ex) {
				ex.printStackTrace();
				System.err.println("Experiment failed.");
			}
		}
	}
	
	
	public static void main(String[] args) {
		ExperimentMain main = new ExperimentMain();
		
		main.run();
	}


}
