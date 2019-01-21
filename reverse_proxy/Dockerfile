FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y nginx nginx-extras

ADD nginx/start.sh /usr/local/bin/
RUN chmod a+x /usr/local/bin/start.sh
ADD nginx/nginx.conf /etc/nginx/
ADD nginx/default.conf /etc/nginx/sites-enabled/
RUN rm /etc/nginx/sites-enabled/default

CMD ["/usr/local/bin/start.sh"]

EXPOSE 80
EXPOSE 443
