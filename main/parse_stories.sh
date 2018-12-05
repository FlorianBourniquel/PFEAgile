#!/bin/sh

python run.py ./output/stories.txt --json --split


cp -r ./output/System/json/. /data/
cp -r ./output/System/ontology/. /data/
