FROM nginx:alpine

COPY ./nginx.conf /etc/nginx/nginx.conf

#RUN apk add --update nodejs nodejs-npm

#RUN npm install -g @angular/cli

WORKDIR /app

COPY ./app/ .

#RUN npm install

#RUN ng build --prod

RUN cp -r /Agile/dist/.  /usr/share/nginx/html/