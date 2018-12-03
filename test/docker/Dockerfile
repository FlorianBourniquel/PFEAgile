FROM acedesign/visualnarrator

RUN apt update -y

RUN apt-get install -y openjdk-8-jdk

RUN apt-get install -y maven

RUN mkdir /app

WORKDIR /app

RUN mkdir /scripts

COPY ./parse_stories_multi.sh /scripts/

RUN chmod +x /scripts/parse_stories_multi.sh

ENTRYPOINT ["mvn", "clean", "test"]