#!/bin/sh

usage='Usage: ./bees.sh password url appid appid\n\tEg: ./bees.sh foobar http://foo.bar.eu.cloudbees.net bar/foo'

if [ -z "$1" ]
then
    echo "Missing elevator server password"
    echo $usage
    exit 0
fi

if [ -z "$2" ]
then
    echo "Missing elevator server url"
    echo $usage
    exit 0
fi

if [ -z "$3" ]
then
    echo "Missing CloudBees appid"
    echo $usage
    exit 0
fi

curl --basic --user :$1 --url $2/resources/players.csv > players.csv
echo "Players exported in players.csv"

bees app:deploy --appid $3 --endPoint eu --type tomcat7 target/*.war

curl --basic --user :$1 --form players=@players.csv --url $2/resources/players.csv > uploadResult.txt
echo "Players imported. Result in uploadResult.txt"
