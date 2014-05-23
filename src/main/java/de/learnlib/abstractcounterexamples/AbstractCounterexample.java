/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples;

import java.util.Arrays;

/**
 * Base class for abstract counterexamples.
 * 
 * @author Malte Isberner
 *
 */
public abstract class AbstractCounterexample {
	
	private final int[] values;
	private int numQueries = 0;
	
	/**
	 * Constructor.
	 * @param m length of the counterexample
	 */
	public AbstractCounterexample(int m) {
		this.values = new int[m + 1];
		Arrays.fill(values, -1);
		values[0] = 0;
		values[m] = 1;
	}
	
	/**
	 * Retrieves the length of the abstract counterexample
	 * @return the length of the counterexample
	 */
	public int getLength() {
		return values.length - 1;
	}
	
	/**
	 * Tests the effect of performing a prefix transformation for
	 * the given index. If the prefix transformation causes hypothesis
	 * and target system to agree, 1 is returned, and 0 otherwise.
	 * <p>
	 * This method corresponds to the &alpha; mapping from the paper.
	 * 
	 * @param index the index for the prefix transformation
	 * @return 1 if prefix transformation causes target and hypothesis
	 * to agree, 0 otherwise.
	 */
	public int testEffect(int index) {
		if(index < 0 || index >= values.length) {
			throw new IndexOutOfBoundsException("" + index);
		}
		
		if(values[index] == -1) {
			values[index] = computeEffect(index);
		}
		return values[index];
	}
	
	/**
	 * Tests the combined effect of two adjacent prefix transformation.
	 * The result of this method is equal to
	 * <code>testEffect(index) + testEffect(index + 1)</code>.
	 * <p>
	 * This method corresponds to the &beta; mapping from the paper.
	 * 
	 * @param index the index for the prefix transformation
	 * @return the sum of adjacent prefix transformation effect values
	 */
	public int testCombinedEffect(int index) {
		if(index < 0 || index >= values.length - 1) {
			throw new IndexOutOfBoundsException("" + index);
		}
		
		return testEffect(index) + testEffect(index+1);
	}
	
	/**
	 * Computes the effect of a prefix transformation.
	 */
	protected int computeEffect(int index) {
		numQueries++;
		return doComputeEffect(index);
	}
	
	/**
	 * Retrieves the number of queries (i.e., invokations of
	 * {@link #testEffect(int)} that were made.
	 * @return the number of queries that were made
	 */
	public int getNumQueries() {
		return numQueries;
	}
	
	/**
	 * This method is responsible for actually performing a prefix transformation,
	 * and computing its effect.
	 */
	protected abstract int doComputeEffect(int index);
}
