#!/bin/bash


if [ -z "$1" ]; then
	echo >&2 "Must supply profile ID as first argument. Exiting ..."
	exit 1
fi

PROFILE_ID="$1"

if [ ! -f "profiles/$PROFILE_ID" ]; then
	echo >&2 "Profile $PROFILE_ID not found!"
fi

source "profiles/$PROFILE_ID"

if [ -z "$EXPERIMENT_ID" ]; then
	EXPERIMENT_ID=`date '+%Y%m%d%H%M%S'`
	echo "Experiment ID unspecified. Using $EXPERIMENT_ID."
fi


export MIN_CE_LENGTH MAX_CE_LENGTH CE_LENGTH_STEP NUM_INSTANCES REPEAT_COUNT EXPERIMENT_ID NUM_STATES ALPHABET_SIZE REDUCE_COUNTEREXAMPLES RANDOM_SEED NUM_THREADS

pushd .
cd ..
export LC_ALL=C
mvn clean compile && mvn exec:java -Dexec.mainClass="de.learnlib.abstractcounterexamples.experiments.icgi2014.KVExperimentMain"
popd

TMP_DIR=".results-tmp-$EXPERIMENT_ID"
mkdir -p "$TMP_DIR"
for f in `find ../results/"$EXPERIMENT_ID" -name '*.dat'`; do
	cat "$f" >>"$TMP_DIR"/`basename "$f"`
done

PLOTS_DIR="./plots/$EXPERIMENT_ID"

mkdir -p "$PLOTS_DIR"

./create-plots.sh "$TMP_DIR" "$PLOTS_DIR"

rm -rf "$TMP_DIR"
