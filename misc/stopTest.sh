#!/bin/sh

JAVA_HOME=`/usr/libexec/java_home --version 1.7`

# unregister participants
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=scan
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=naive
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=queue
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=crazy

# end processes
jps | grep --color --regexp Participant\\\|Launcher
echo please kill all these pids