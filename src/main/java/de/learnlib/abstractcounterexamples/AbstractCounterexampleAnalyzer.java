/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples;

/**
 * Interface for an analyzer of {@link AbstractCounterexample}s.
 * 
 * @author Malte Isberner
 *
 */
public interface AbstractCounterexampleAnalyzer {
	
	/**
	 * Analyzes an abstract counterexample. This method returns the index of
	 * the corresponding distinguishing suffix.
	 * 
	 * @param acex the abstract counterexample
	 * @return the suffix index
	 */
	public int analyzeAbstractCounterexample(AbstractCounterexample acex);
	
	
}
