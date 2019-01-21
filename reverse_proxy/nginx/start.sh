#!/bin/bash

#echo "+ configuring symfony .env file"
#gateway=$(ip route | grep default | awk  '{print $3}')
#sed -i -e "s/DATABASE_HOST=.*$/DATABASE_HOST=$gateway/g" /var/www/do-in-sport-symfony/symfony/.env
echo "+ starting nginx"
nginx
