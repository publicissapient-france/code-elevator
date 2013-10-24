#!/bin/sh

usage='Usage: ./bees.sh url appid [password]\n\tEg: ./bees.sh http://foo.bar.eu.cloudbees.net bar/foo secret'

if [ -z "$1" ]
then
    echo "Missing elevator server url"
    echo $usage
    exit 0
fi

if [ -z "$2" ]
then
    echo "Missing CloudBees appid"
    echo $usage
    exit 0
fi

if [ -z "$3" ]
then
    echo "Using default password"
    PASSWORD="admin"
else
    PASSWORD_BEES="-P ADMIN_PASSWORD=$3"
    PASSWORD=$3
fi

curl --basic --user :$PASSWORD --url $1/resources/players.csv > players.csv
echo "Players exported in players.csv"

bees app:deploy --appid $2 --endPoint eu $PASSWORD_BEES --type tomcat7 target/*.war
echo bees app:deploy --appid $2 --endPoint eu $PASSWORD_BEES --type tomcat7 target/*.war

curl --basic --user :$PASSWORD --form players=@players.csv --url $1/resources/players.csv > uploadResult.txt
echo "Players imported. Result in uploadResult.txt"
