#!/bin/sh

# subscribe participants to server
curl --request POST http://localhost:8080/resources/player/register?email=sebastian.lemerdy@gmail.com\&pseudo=scan\&serverURL=http://localhost:1981
curl --request POST http://localhost:8080/resources/player/register?email=slemerdy@xebia.fr\&pseudo=naive\&serverURL=http://localhost:1982
curl --request POST http://localhost:8080/resources/player/register?email=sebastian.lemerdy@vidal.fr\&pseudo=queue\&serverURL=http://localhost:1983
curl --request POST http://localhost:8080/resources/player/register?email=eric.lemerdy@gmail.com\&pseudo=crazy\&serverURL=http://localhost:1984

# increase max number of users
for (( i=0 ; i < 10 ; i=i+1 )) ; do curl http://localhost:8080/resources/admin/increaseMaxNumberOfUsers ; done
