#!/usr/bin/env bash

docker run --name code-elevator-container code-elevator
#replace 9000 by whatever port you want to expose ie 80
docker run --detach --publish 9000:8080 --volumes-from code-elevator-container jetty:9.3.11-jre8
