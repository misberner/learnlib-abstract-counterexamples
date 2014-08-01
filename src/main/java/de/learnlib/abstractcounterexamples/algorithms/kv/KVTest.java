package de.learnlib.abstractcounterexamples.algorithms.kv;

import java.util.Random;

import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.automata.Automata;
import net.automatalib.util.automata.random.RandomAutomata;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import de.learnlib.abstractcounterexamples.analyzers.Analyzers;
import de.learnlib.oracles.DefaultQuery;
import de.learnlib.oracles.MQUtil;
import de.learnlib.oracles.SimulatorOracle.DFASimulatorOracle;

public class KVTest {
	
	public static void main(String[] args) {
		Alphabet<Integer> alphabet = Alphabets.integers(0, 9);
		CompactDFA<Integer> dfa = RandomAutomata.randomDFA(new Random(), 2000, alphabet);
		
		
		DFASimulatorOracle<Integer> oracle = new DFASimulatorOracle<>(dfa);
		
		KearnsVaziraniDFA<Integer> learner = new KearnsVaziraniDFA<Integer>(dfa.getInputAlphabet(), oracle, Analyzers.RIVEST_SCHAPIRE, true);
		
		learner.startLearning();
		
		Word<Integer> ceWord;
		
		while((ceWord = Automata.findSeparatingWord(dfa, learner.getHypothesisModel(), dfa.getInputAlphabet())) != null) {
			DefaultQuery<Integer,Boolean> ce = MQUtil.query(oracle, ceWord);
			boolean res = learner.refineHypothesis(ce);
			assert res;
		}
	}
}
