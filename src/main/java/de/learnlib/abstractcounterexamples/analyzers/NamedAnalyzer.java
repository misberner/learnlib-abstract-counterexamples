/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples.analyzers;

import de.learnlib.abstractcounterexamples.AbstractCounterexampleAnalyzer;


/**
 * An abstract counterexample analyzer that carries a name.
 * 
 * @author Malte Isberner
 *
 */
public abstract class NamedAnalyzer implements AbstractCounterexampleAnalyzer {
	
	private final String name;

	/**
	 * Constructor.
	 * @param name the name of the counterexample analyzer
	 */
	public NamedAnalyzer(String name) {
		this.name = name;
	}
	

	/**
	 * Retrieves the name of this analyzer.
	 * @return the name of this analyzer
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
	
}
