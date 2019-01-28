#!/bin/bash


# ApkDir=../../apk_parts/part1
ApkDir=/mnt/ta/apks/elite/apk_parts
OutDir=/mnt/ta/apks/elite/output

START=1
END=10 # including

START=$1
END=$2 # including

for i in $(seq $START $END)
do
    for apk in $(find $ApkDir/part$i -iname "*.apk")
    do
        echo $apk
        ./apika.sh $apk $OutDir/part$i
    done
done


