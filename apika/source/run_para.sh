#!/bin/bash -x


# ApkDir=../../apk_parts/part1
ApkDir=/mnt/ta/apks/elite/apk_parts
OutDir=/mnt/ta/apks/elite/output

START=1
END=10 # including

STEP=2 #
# (end - start + 1) / step is the number of parallel running batch script


for i in $(seq $START $STEP $END)
do
    let end=i+STEP-1
    ./run_batch.sh $i $end &
done


