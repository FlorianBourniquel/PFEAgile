FROM acedesign/visualnarrator

RUN apt-get update 

RUN apt-get install -y default-jre

RUN apt-get install -y apt-utils graphviz

RUN apt-get install -y r-base

RUN apt-get install -y pandoc

RUN mkdir /data

RUN mkdir /graphs

COPY Install.R .

#RUN Rscript ./Install.R

EXPOSE 8080

COPY parse_stories.sh .

COPY target/main-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "main-1.0-SNAPSHOT.jar"]