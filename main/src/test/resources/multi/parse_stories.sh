#!/bin/sh

fileNamePattern="stories.*\.txt"
for file in ./
do
    echo $file
done

#cat ./output/stories.txt | while read line
#do
#   echo $line > ./output/story.txt
#   python run.py ./output/story.txt --json
#   echo $line
#done

#cp -r ./output/System/json/. /data/
#cp -r ./output/System/ontology/. /data/