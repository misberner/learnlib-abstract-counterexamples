#!/bin/bash

RESULTS_DIR="$1"

PLOTS_DIR="$2"

if [ -z "$RESULTS_DIR" ]; then
	echo 2>&1 "Must supply results directory. Exiting."
	exit 1
fi

if [ -z "$PLOTS_DIR" ]; then
	echo 2>&1 "Must supply plots directory. Exiting."
	exit 1
fi

PLOT_GPL="`readlink -f plot.gpl`"

mkdir -p "$PLOTS_DIR"

for f in "$RESULTS_DIR"/*.dat; do
	./aggregate.py "$f" "$PLOTS_DIR"/`basename "$f"`
done

cd "$PLOTS_DIR"
gnuplot "$PLOT_GPL"

echo "Plots created in $PLOTS_DIR"



