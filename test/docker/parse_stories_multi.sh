#!/bin/sh

for file in /app/input/multi/*
do
    python /usr/src/app/run.py $file --json
done
