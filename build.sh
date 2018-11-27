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
ng build --prod --output ../docker/dist
cd ../..

echo ""
echo "----------Startint app-------------"
echo ""
docker-compose build
docker-compose up

