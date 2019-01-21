#!/usr/bin/env bash
echo ""
echo "----------Building main-------------"
echo ""
cd main
mvn clean package shade:shade@web

echo ""
echo "----------Building front_end-------------"
echo ""
cd ..
cd front_end/Agile
npm install
ng build --prod --output-path ../docker/dist
cd ../..

echo ""
echo "----------Startint app-------------"
echo ""
docker-compose build

