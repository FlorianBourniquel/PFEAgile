#!/bin/sh

echo ""
echo "----------Compiling-------------"
echo ""
cd main
mvn clean package shade:shade@web


echo ""
echo "----------Starting main-------------"
echo ""
cd ..
docker-compose rm -f main | true
docker-compose up --build main
