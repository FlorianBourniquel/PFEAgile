FROM acedesign/visualnarrator

RUN apt-get update 

RUN apt-get install -y default-jre

RUN mkdir /data

COPY target/main-1.0-SNAPSHOT-jar-with-dependencies.jar .

COPY script.sh .

ENTRYPOINT ["./script.sh"]