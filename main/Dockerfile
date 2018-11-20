FROM acedesign/visualnarrator

RUN apt-get update 

RUN apt-get install -y default-jre

RUN apt-get install -y apt-utils graphviz

RUN mkdir /data

COPY target/main-1.0-SNAPSHOT-jar-with-dependencies.jar .

COPY parse_stories.sh .

ENTRYPOINT ["java", "-jar", "main-1.0-SNAPSHOT-jar-with-dependencies.jar"]