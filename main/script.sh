#!/bin/sh

#echo "-----------------------------------\n\n"
#echo "---------PARSING STRORIES----------\n"
#echo "-----------------------------------\n\n"

#cat ./output/stories.txt | while read line
#do
#	echo $line > ./output/story.txt
#   python run.py ./output/story.txt --json
#   echo $line
#done

#cp -r ./output/System/json/. /data/
#cp -r ./output/System/ontology/. /data/


#echo "-----------------------------------\n\n"
#echo "------BUILDING FINAL GRAPH---------\n"
#echo "-----------------------------------\n\n"

java -jar main-1.0-SNAPSHOT-jar-with-dependencies.jar


echo "-----------------------------------\n\n"
echo "--GRAPH: http://localhost:7474/----\n"
echo "-----------------------------------\n\n"