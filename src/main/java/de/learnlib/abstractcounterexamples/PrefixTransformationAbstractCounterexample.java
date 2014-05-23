package de.learnlib.abstractcounterexamples;

import net.automatalib.automata.concepts.SuffixOutput;
import net.automatalib.words.Word;

import com.google.common.base.Objects;

import de.learnlib.api.AccessSequenceTransformer;
import de.learnlib.api.MembershipOracle;
import de.learnlib.oracles.MQUtil;


/**
 * An abstract counterexample whose data is derived from prefix transformation.
 * While this is the only way of deriving abstract counterexamples discussed in the
 * paper, in principle other ways are possible, too.
 * 
 * @author Malte Isberner
 *
 * @param <I> input symbol type
 * @param <O> output type
 */
public class PrefixTransformationAbstractCounterexample<I, O> extends AbstractCounterexample {
	
	private final Word<I> counterexample;
	private final MembershipOracle<I, O> oracle;
	private final AccessSequenceTransformer<I> asTransformer;
	private final SuffixOutput<I,O> hypothesis;

	
	public PrefixTransformationAbstractCounterexample(
			Word<I> counterexample,
			MembershipOracle<I, O> oracle,
			AccessSequenceTransformer<I> asTransformer,
			SuffixOutput<I, O> hypothesis) {
		super(counterexample.length());
		this.counterexample = counterexample;
		this.oracle = oracle;
		this.asTransformer = asTransformer;
		this.hypothesis = hypothesis;
	}


	/*
	 * (non-Javadoc)
	 * @see de.learnlib.abstractcounterexamples.AbstractCounterexample#doComputeEffect(int)
	 */
	@Override
	protected int doComputeEffect(int index) {
		Word<I> prefix = counterexample.prefix(index);
		Word<I> suffix = counterexample.subWord(index);
		Word<I> transformedPrefix = asTransformer.transformAccessSequence(prefix);
		
		O hypOut = hypothesis.computeSuffixOutput(transformedPrefix, suffix);
		O oracleOut = MQUtil.output(oracle, transformedPrefix, suffix);
		
		return Objects.equal(hypOut, oracleOut) ? 1 : 0;
	}
	
	/**
	 * Reduces the counterexample, i.e., strips a maximal prefix that is an
	 * access sequence off the counterexample.
	 * @return the reduced counterexample
	 */
	public AbstractCounterexample reduce() {
		int m = getLength();
		int i;
		for(i = 0; i < m - 1; i++) {
			Word<I> prefix = counterexample.prefix(i+1);
			if(!asTransformer.isAccessSequence(prefix)) {
				break;
			}
		}
		
		if(i == 0) {
			return this;
		}
		
		return new ShiftedAbstractCounterexample(this, i);
	}

}
