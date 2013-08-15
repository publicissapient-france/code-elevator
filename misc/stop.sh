#!/bin/sh

JAVA_HOME=`/usr/libexec/java_home --version 1.7`

# unregister participants
echo unregister scan
curl --request POST http://localhost:8080/resources/player/unregister?email=sebastian.lemerdy@gmail.com
echo unregister naive
curl --request POST http://localhost:8080/resources/player/unregister?email=slemerdy@xebia.fr
echo unregister queue
curl --request POST http://localhost:8080/resources/player/unregister?email=sebastian.lemerdy@vidal.fr
echo unregister crazy
curl --request POST http://localhost:8080/resources/player/unregister?email=eric.lemerdy@gmail.com

# end processes
jps | grep --color --regexp ParticipantServer\\\|Launcher
echo please kill all these pids
