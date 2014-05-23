LearnLib: Abstract Counterexample Analysis Framework
=================================

This project contains an abstract counterexample analysis framework for Active Automata Learning. The problem of analyzing a counterexample and extracting a suitable suffix from it is reduced to the problem of, given a 0/1-sequence that starts with 0 and ends with 1, finding a pair of adjacent indices with different elements. As a consequence, counterexample analysis algorithms can be specified in a very concise and easy-to-understand-manner.


Evaluation Scripts
---------------------------
Provided with this project is a collection of scripts for evaluating different counterexample analysis algorithms, and for visualizing the evaluation results. These scripts are contained in the `evaluation` subdirectory. The central evaluation script can be invoked using

```sh
cd evaluation
./perform-evaluation.sh <profile-name>
```

`<profile-name>` corresponds to a file in the `evaluation/profiles/` directory. A pre-defined profile, `icgi2014`, is contained. However, other profiles may be defined as well by creating a text file with the respective name and contents. The contents of a profile file are simple Bash variable assignments in `KEY=value` form.

The predefined `icgi2014` profile lists all variables that may be set in a profile file. Though they should be pretty much self-explanatory, a brief description is given here:

* `MIN_CE_LENGTH`: the minimum length of counterexamples to consider (inclusive)
* `MAX_CE_LENGTH`: the maximum length of counterexamples to consider (inclusive)
* `CE_LENGTH_STEP`: the number by which to increment the counterexample length in each round
* `NUM_INSTANCES`: the number of random DFA instances to generate. The results will be averaged over all instances
* `REPEAT_COUNT`: the number of experiment repetitions. This only affects the counterexample generation, not the DFA generation.
* `EXPERIMENT_ID`: an identifier that will be used in the generated directory structure. If unset, the current date/time in the format `YYYYmmddHHMMSS` will be used
* `NUM_STATES`: the number of states of the generated DFA
* `ALPHABET_SIZE`: the number of alphabet symbols of the generated DFA
* `REDUCE_COUNTEREXAMPLES`: whether two prune a maximal access sequence prefix from the counterexample
* `RANDOM_SEED`: the seed value used for the random number generator, to makes results reproducible. If unset, the seed will be derived from the current time.
* `NUM_THREADS`: the number of threads to run in parallel. If unset, this will be set to the number of processors visible to the JVM

After the `perform-evaluation.sh` script has completed execution, the results can be found in the `evaluation/plots/<experiment-id>/` subdirectory: two PNG files containing the plotted graphs, and the `.dat` files containing the raw data in the following format:

```
<ce length>		<average number of queries> <std.dev. of number of queries>		<average suffix length> <std.dev. of average suffix length>
```


Prerequisites
-----------------------------
Running the evaluation scripts requires (a sufficiently recent version of) the following software to be installed on your system:
* [Gnuplot](http://www.gnuplot.info/)
* [Apache Maven](http://maven.apache.org/)
* [Java 1.7+](http://www.java.com/)
* [Python 2.7](http://www.python.org/)

