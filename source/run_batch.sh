#!/bin/bash


ApkDir=../../apk_parts/part5

for apk in $(find $ApkDir -iname "*.apk")
do
    echo $apk
    ./apika.sh $apk
done


