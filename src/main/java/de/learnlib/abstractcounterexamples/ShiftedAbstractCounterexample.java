/* 
 * Copyright (C) 2014 Malte Isberner
 */
package de.learnlib.abstractcounterexamples;

/**
 * An abstract counterexample derived from another abstract counterexample
 * by shifting the latter by a given offset.
 * 
 * @author Malte Isberner
 *
 */
public class ShiftedAbstractCounterexample extends AbstractCounterexample {
	
	private final AbstractCounterexample original;
	private final int shiftAmt;

	/**
	 * Constructor.
	 * @param original the original counterexample
	 * @param shiftAmt the shift amount
	 */
	public ShiftedAbstractCounterexample(AbstractCounterexample original, int shiftAmt) {
		super(original.getLength() - shiftAmt);
		this.original = original;
		this.shiftAmt = shiftAmt;
	}

	@Override
	protected int doComputeEffect(int index) {
		return original.testEffect(index + shiftAmt);
	}

	
}
