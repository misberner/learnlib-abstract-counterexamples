/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples.analyzers;

import de.learnlib.abstractcounterexamples.AbstractCounterexample;

/**
 * This is a utility class, acting as a container for several {@link NamedAnalyzer}s.
 * 
 * @author Malte Isberner
 *
 */
public abstract class Analyzers {
	
	/**
	 * Analyzer that linearly scans through the abstract counterexample in ascending
	 * order.
	 */
	public static NamedAnalyzer LINEAR_ASC = new NamedAnalyzer("LinearAsc") {
		@Override
		public int analyzeAbstractCounterexample(AbstractCounterexample acex) {
			return AnalysisAlgorithms.linearSearchAscending(acex, 0, acex.getLength());
		}
	};
	
	/**
	 * Analyzer that linearly scans through the abstract counterexample in descending
	 * order.
	 */
	public static NamedAnalyzer LINEAR_DESC = new NamedAnalyzer("LinearDesc") {
		@Override
		public int analyzeAbstractCounterexample(AbstractCounterexample acex) {
			return AnalysisAlgorithms.linearSearchDescending(acex, 0, acex.getLength());
		}
	};
	
	/**
	 * Analyzer that searches for a suffix index using binary search.
	 */
	public static NamedAnalyzer RIVEST_SCHAPIRE = new NamedAnalyzer("RivestSchapire") {
		@Override
		public int analyzeAbstractCounterexample(AbstractCounterexample acex) {
			return AnalysisAlgorithms.binarySearch(acex, 0, acex.getLength());
		}
	};
	
	/**
	 * Eager version of Rivest&Schapire's algorithm.
	 */
	public static NamedAnalyzer RIVEST_SCHAPIRE_EAGER = new NamedAnalyzer("RivestSchapireEager") {
		@Override
		public int analyzeAbstractCounterexample(AbstractCounterexample acex) {
			return AnalysisAlgorithms.binarySearchEager(acex, 0, acex.getLength());
		}
	};
	
	/**
	 * Analyzer that searches for a suffix index using exponential search.
	 */
	public static NamedAnalyzer EXPONENTIAL = new NamedAnalyzer("Exponential") {
		@Override
		public int analyzeAbstractCounterexample(AbstractCounterexample acex) {
			return AnalysisAlgorithms.exponentialSearch(acex, 0, acex.getLength());
		}
	};
	
	/**
	 * Analyzer that searches for a suffix index using partition search.
	 */
	public static NamedAnalyzer PARTITION = new NamedAnalyzer("Partition") {
		@Override
		public int analyzeAbstractCounterexample(AbstractCounterexample acex) {
			return AnalysisAlgorithms.partitionSearch(acex, 0, acex.getLength());
		}
	};
	
	
	private static NamedAnalyzer[] VALUES = {
		// LINEAR_ASC, // Removed because it is not interesting
		LINEAR_DESC,
		RIVEST_SCHAPIRE,
		RIVEST_SCHAPIRE_EAGER,
		EXPONENTIAL,
		PARTITION,
	};
	
	
	/**
	 * Retrieves an array of all enabled analyzers. The returned array
	 * may be modified.
	 * @return an array of all enabled analyzers
	 */
	public static NamedAnalyzer[] values() {
		return VALUES.clone();
	}
	
	/*
	 * Constructor.
	 */
	private Analyzers() {
		throw new AssertionError("Class should not be instantiated");
	}

}
