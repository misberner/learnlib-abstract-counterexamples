/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples.experiments.icgi2014;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.util.automata.Automata;
import net.automatalib.util.automata.random.RandomAutomata;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;
import net.automatalib.words.impl.Alphabets;
import de.learnlib.abstractcounterexamples.algorithms.kv.KearnsVaziraniDFA;
import de.learnlib.abstractcounterexamples.analyzers.Analyzers;
import de.learnlib.abstractcounterexamples.analyzers.NamedAnalyzer;
import de.learnlib.cache.dfa.DFACacheOracle;
import de.learnlib.cache.dfa.DFACaches;
import de.learnlib.oracles.CounterOracle.DFACounterOracle;
import de.learnlib.oracles.DefaultQuery;
import de.learnlib.oracles.SimulatorOracle.DFASimulatorOracle;

public class KVExperiment {
	
	public static int numThreads = -1;
	
	static {
		
		try {
			String numThreadsString = System.getenv("NUM_THREADS");
			if(numThreadsString != null) {
				numThreads = Integer.parseInt(numThreadsString);
			}
		}
		catch(IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		
		if(numThreads == -1) {
			numThreads = Runtime.getRuntime().availableProcessors();
		}
	}
	
	private final File outputDir;
	private final CompactDFA<Integer> dfa;
	private final Random random;
	
	private class Job implements Callable<Void> {
		
		private final NamedAnalyzer analyzer;
		private final int ceLength;
		//private final int id;
		private final Random random;
		private final Writer writer;
		
		public Job(NamedAnalyzer analyzer, int ceLength, Random random, Writer writer) {
			this.analyzer = analyzer;
			this.ceLength = ceLength;
			this.random = random;
			this.writer = writer;
		}
		
		@Override
		public Void call() throws Exception {
			DFASimulatorOracle<Integer> oracle = new DFASimulatorOracle<>(dfa);
			
			DFACounterOracle<Integer> counter = new DFACounterOracle<>(oracle, "");
			
			DFACacheOracle<Integer> cache = DFACaches.createTreeCache(dfa.getInputAlphabet(), counter);
			
			KearnsVaziraniDFA<Integer> learner = new KearnsVaziraniDFA<>(dfa.getInputAlphabet(), cache, analyzer, true);
			
			learner.startLearning();
			
			try {
				DefaultQuery<Integer, Boolean> ceQuery;
				while((ceQuery = generateCounterexample(learner.getHypothesisModel(), ceLength, random)) != null) {
					learner.refineHypothesis(ceQuery);
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
			//System.err.println("Execution " + analyzer.getName() + "#" + id + " for CE length " + ceLength + " finished, "
			//		+ "required " + wrapper.getTotalQueries() + " queries, average suffix length: " + wrapper.getAverageSuffixLength());
			
			synchronized(writer) {
				writer.write(String.format("%4d\t%8d\t%12d\t%3.4f\n", ceLength, learner.getTotalCEQueries(), counter.getCount(), learner.getAveragePrefixLength()));
				writer.flush();
			}
			
			return null;
		}	
	}
	
	public KVExperiment(File outputDir, Random random, int numStates, int alphabetSize, boolean reduceCounterexamples) {
		this.dfa = createDFA(random, numStates, alphabetSize);
		this.outputDir = outputDir;
		this.random = random;
	}
	
	public void run(int minCeLength, int maxCeLength, int ceLengthStep, int repeatCount) throws InterruptedException, IOException {
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		outputDir.mkdirs();
		
		List<BufferedWriter> writers = new ArrayList<>();
		try {
			for(NamedAnalyzer analyzer : Analyzers.kvValues()) {
				String name = analyzer.getName();
				File outputFile = new File(outputDir, name + ".dat");
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
				writers.add(writer);
				
				
				for(int ceLength = minCeLength; ceLength <= maxCeLength; ceLength += ceLengthStep) {
					for(int i = 0; i < repeatCount; i++) {
						// Create per-job random number generator, derived from the main random number generator
						// Note that the seeds of these are deterministic
						Random jobRandom = new Random(random.nextLong());
						
						Job job
							= new Job(analyzer, ceLength, jobRandom, writer);
						
						executor.submit(job);
					}
				}
			}
			
		
			executor.shutdown();
			executor.awaitTermination(100, TimeUnit.DAYS);
		}
		finally {
			for(Writer w : writers) {
				try {
					w.close();
				}
				catch(IOException ex) {
					ex.printStackTrace(); // print, but don't do anything
				}
			}
		}
	}
	
	
	private static CompactDFA<Integer> createDFA(Random random, int numStates, int alphabetSize) {
		Alphabet<Integer> alphabet = Alphabets.integers(0, alphabetSize - 1);
		
		CompactDFA<Integer> dfa;
		
		do {
			dfa = RandomAutomata.randomDFA(random, numStates, alphabet);
		}
		while(dfa.size() < numStates);
		
		return dfa;
	}
	
	private static <I> DefaultQuery<I,Boolean>
	generateCounterexample(Random random, CompactDFA<I> target, DFA<?,I> hypothesis, int ceLength) {
		Alphabet<I> alphabet = target.getInputAlphabet();
		
		if(Automata.findSeparatingWord(target, hypothesis, alphabet) == null) {
			return null;
		}
		
		Word<I> word;
		
		do {
			WordBuilder<I> wb = new WordBuilder<>();
			for(int i = 0; i < ceLength; i++) {
				wb.append(alphabet.getSymbol(random.nextInt(alphabet.size())));
			}
			word = wb.toWord();
		} while(target.accepts(word) == hypothesis.accepts(word));
		
		return new DefaultQuery<>(word, target.computeOutput(word));
	}
	
	private DefaultQuery<Integer,Boolean>
	generateCounterexample(DFA<?,Integer> hypothesis, int ceLength, Random random) {
		return generateCounterexample(random, dfa, hypothesis, ceLength);
	}
}
