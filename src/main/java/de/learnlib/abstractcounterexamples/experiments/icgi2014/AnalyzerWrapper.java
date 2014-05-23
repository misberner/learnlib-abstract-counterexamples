/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples.experiments.icgi2014;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.automatalib.automata.concepts.SuffixOutput;
import net.automatalib.words.Word;
import de.learnlib.abstractcounterexamples.AbstractCounterexample;
import de.learnlib.abstractcounterexamples.PrefixTransformationAbstractCounterexample;
import de.learnlib.abstractcounterexamples.analyzers.NamedAnalyzer;
import de.learnlib.api.AccessSequenceTransformer;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.Query;
import de.learnlib.counterexamples.LocalSuffixFinder;

/**
 * Wraps a {@link NamedAnalyzer}. This class is both responsible for adapting
 * it to the standard LearnLib {@link LocalSuffixFinder} interface, and for
 * maintaining statistics. Hence, a new object of this class should be instantiated
 * for every learning process.
 *  
 * @author Malte Isberner
 *
 */
public class AnalyzerWrapper implements LocalSuffixFinder<Object, Object> {

	private final NamedAnalyzer analyzer;
	private final boolean reduce;
	private final AtomicLong totalQueries = new AtomicLong(0L);
	private final AtomicLong totalSuffixLength = new AtomicLong(0L);
	private final AtomicInteger numCes = new AtomicInteger(0);
	
	/**
	 * Constructor.
	 * @param analyzer the analyzer to be wrapped
	 * @param reduce whether or not to reduce counterexamples
	 */
	public AnalyzerWrapper(NamedAnalyzer analyzer, boolean reduce) {
		this.analyzer = analyzer;
		this.reduce = reduce;
	}
	
	/**
	 * Retrieves the name of the wrapped analyzer.
	 * @return the name of the wrapped analyzer
	 */
	public String getName() {
		return analyzer.getName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.learnlib.counterexamples.LocalSuffixFinder#findSuffixIndex(de.learnlib.api.Query, de.learnlib.api.AccessSequenceTransformer, net.automatalib.automata.concepts.SuffixOutput, de.learnlib.api.MembershipOracle)
	 */
	@Override
	public <RI, RO> int findSuffixIndex(Query<RI, RO> ceQuery,
			AccessSequenceTransformer<RI> asTransformer,
			SuffixOutput<RI, RO> hypOutput, MembershipOracle<RI, RO> oracle) {
		
		Word<RI> counterexample = ceQuery.getInput();
		
		// Create the view of an abstract counterexample
		PrefixTransformationAbstractCounterexample<RI, RO> ptAcex
			= new PrefixTransformationAbstractCounterexample<>(counterexample, oracle, asTransformer, hypOutput);
			
		AbstractCounterexample acex;
		int d;
		if(reduce) {
			// Reduce the counterexample by shifting it, the shift offset
			// can be computed as the difference in length
			acex = ptAcex.reduce();
			d = ptAcex.getLength() - acex.getLength();
		}
		else {
			acex = ptAcex;
			d = 0;
		}
		
		int idx = analyzer.analyzeAbstractCounterexample(acex);
		int suffixLength = acex.getLength() - idx;
		
		totalQueries.addAndGet(acex.getNumQueries());
		totalSuffixLength.addAndGet(suffixLength);
		numCes.incrementAndGet();
		
		// Note: There is an off-by-one mismatch between the LearnLib interface
		// and our interface
		return idx + d + 1;
	}
	
	
	/**
	 * Retrieves the total number of membership queries posed during
	 * counterexample analysis. 
	 * @return the total number of membership queries
	 */
	public long getTotalQueries() {
		return totalQueries.get();
	}
	
	/**
	 * Retrieves the average length of counterexample suffixes that were returned.
	 * @return the average suffix length.
	 */
	public double getAverageSuffixLength() {
		return totalSuffixLength.get()/(double)numCes.get();
	}

}
