/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples.analyzers;

import de.learnlib.abstractcounterexamples.AbstractCounterexample;

/**
 * Abstract counterexample analysis algorithms.
 * <p>
 * All of the algorithms contained in this class takes as arguments:
 * <ul>
 * <li>an {@link AbstractCounterexample} <code>acex</code>,</li>
 * <li>the lower bound of the search range <code>low</code>, and</li>
 * <li>the upper bound of the search range <code>high</code>.
 * </ul>
 * For a valid input, all of the methods in this class will return an
 * index <code>i</code> such that <code>acex.testEffect(i) != acex.testEffect(i+1)</code>.
 * The input is valid iff <code>high > low</code>, <code>acex.testEffect(low) == 0</code>,
 * and <code>acex.testEffect(high) == 1</code>.
 * 
 * @author Malte Isberner
 *
 */
public class AnalysisAlgorithms {
	
	/**
	 * Scan linearly through the counterexample in ascending order.
	 * 
	 * @param acex the abstract counterexample
	 * @param low the lower bound of the search range
	 * @param high the upper bound of the search range
	 * @return an index <code>i</code> such that
	 * <code>acex.testEffect(i) != acex.testEffect(i+1)</code>
	 */
	public static int linearSearchAscending(AbstractCounterexample acex, int low, int high) {
		int cur;
		for(cur = low + 1; cur < high; cur++) {
			if(acex.testEffect(cur) == 1) {
				break;
			}
		}
		return (cur-1);
	}
	
	/**
	 * Scan linearly through the counterexample in descending order.
	 * 
	 * @param acex the abstract counterexample
	 * @param low the lower bound of the search range
	 * @param high the upper bound of the search range
	 * @return an index <code>i</code> such that
	 * <code>acex.testEffect(i) != acex.testEffect(i+1)</code>
	 */
	public static int linearSearchDescending(AbstractCounterexample acex, int low, int high) {
		int cur;
		for(cur = high-1; cur > low; cur--) {
			if(acex.testEffect(cur) == 0) {
				break;
			}
		}
		return cur;
	}
	
	/**
	 * Search for a suffix index using an exponential search.
	 * 
	 * @param acex the abstract counterexample
	 * @param low the lower bound of the search range
	 * @param high the upper bound of the search range
	 * @return an index <code>i</code> such that
	 * <code>acex.testEffect(i) != acex.testEffect(i+1)</code>
	 */
	public static int exponentialSearch(AbstractCounterexample acex, int low, int high) {
		int ofs = 1;
		
		while(high - ofs > low) {
			if(acex.testEffect(high - ofs) == 0) {
				low = high - ofs;
				break;
			}
			high -= ofs;
			ofs *= 2;
		}
		
		return binarySearch(acex, low, high);
	}
	
	/**
	 * Search for a suffix index using a binary search.
	 * 
	 * @param acex the abstract counterexample
	 * @param low the lower bound of the search range
	 * @param high the upper bound of the search range
	 * @return an index <code>i</code> such that
	 * <code>acex.testEffect(i) != acex.testEffect(i+1)</code>
	 */
	public static int binarySearch(AbstractCounterexample acex, int low, int high) {
		while(high - low > 1) {
			int mid = low + (high - low)/2;
			if(acex.testEffect(mid) == 0) {
				low = mid;
			}
			else {
				high = mid;
			}
		}
		
		return low;
	}
	
	/**
	 *  Search for a suffix index using a partition search
	 * 
	 * @param acex the abstract counterexample
	 * @param low the lower bound of the search range
	 * @param high the upper bound of the search range
	 * @return an index <code>i</code> such that
	 * <code>acex.testEffect(i) != acex.testEffect(i+1)</code>
	 */
	public static int partitionSearch(AbstractCounterexample acex, int low, int high) {
		int span = high - low + 1;
		double logSpan = Math.log(span)/Math.log(2);
		
		int step = (int)(span/logSpan);
		
		while(high - step > low) {
			if(acex.testEffect(high - step) == 0) {
				low = high - step;
				break;
			}
			high -= step;
		}
		
		return binarySearch(acex, low, high);
	}
	
	
	/**
	 * Search for a suffix index using an eager binary search.
	 * 
	 * @param acex the abstract counterexample
	 * @param low the lower bound of the search range
	 * @param high the upper bound of the search range
	 * @return an index <code>i</code> such that
	 * <code>acex.testEffect(i) != acex.testEffect(i+1)</code>
	 */
	public static int binarySearchEager(AbstractCounterexample acex, int low, int high) {
		high--;
		
		while(high > low) {
			int mid = low + (high - low)/2;
			
			int val = acex.testCombinedEffect(mid);
			if(val == 1) {
				return mid;
			}
			else if(val == 0) {
				low = mid + 1;
			}
			else { // val == 2
				high = mid - 1;
			}
		}
		
		return low;
	}

}
