FROM florianbourniquel/visualnarrator

RUN apt-get update

RUN apt-get install -y openjdk-8-jdk

RUN apt-get install -y maven

RUN apt-get install -y apt-utils graphviz

RUN mkdir /app

WORKDIR /app

RUN mkdir /scripts

COPY ./parse_stories_multi.sh /scripts/
COPY ./parse_stories_single.sh /scripts/

RUN chmod +x /scripts/parse_stories_multi.sh
RUN chmod +x /scripts/parse_stories_single.sh

ENTRYPOINT ["mvn", "clean", "test"]