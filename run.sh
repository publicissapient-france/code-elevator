#!/usr/bin/env bash

docker run --name code-elevator-container code-elevator
docker run --detach --publish 8080:8080 --volumes-from code-elevator-container jetty:9.3.11-jre8
