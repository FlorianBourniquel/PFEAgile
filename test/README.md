#Projet de test

Ce projet sert a tester la correction de la modification que nous avons effectué sur le fonctionnement de visual narrator.
Dans ce test, nous executons visual narrator avec et sans la modification et comparons les resultats a travers plusieurs criteres:
- L'égalité des classes et des relations généré dans les deux cas
- Le niveau de déviation par rapport des resulats avec modification par rapport au fonctionnement normal



##Execution
Pour executer ce test vous aurez uniquement besoin de Docker.
Les étapes a suivre sont les suivantes : 
1. Déplacez vous dans le repertoire /docker
2. Construisez l'image du test a partir du Dockerfile : `docker build -t test_modifs .`
3. Placez vous ensuite a la racine du repertoire courant /test
4. Demarrer le container de test avec la commande suivante:
`docker run -it -v /root/.m2/:/root/.m2 -v repertoire_courant:/app -v repertoire_courant/output:/usr/src/app/output --rm test_modifs`
