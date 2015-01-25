#!/bin/sh 
cd example/Moonwarriors

rm *.txt

cd ..

cd ..

./DexAnalysis -a -k example/Moonwarriors/classes.dex >example/Moonwarriors/libMethod.txt

./DexAnalysis -a -s example/Moonwarriors/classes.dex >example/Moonwarriors/userMethod.txt

./DexAnalysis -a -n example/Moonwarriors/classes.dex >example/Moonwarriors/nativeMethod.txt
