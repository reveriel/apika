#!/bin/bash


ApkDir=../../apk_parts/part6

for apk in $(find $ApkDir -iname "*.apk")
do
    echo $apk
    ./apika.sh $apk
done


