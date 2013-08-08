#!/bin/sh

JAVA_HOME=`/usr/libexec/java_home --version 1.7`

# unregister participants
echo unregister scan
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=scan
echo unregister naive
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=naive
echo unregister queue
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=queue
echo unregister crazy
curl --request POST http://localhost:8080/resources/player/unregister?pseudo=crazy

# end processes
jps | grep --color --regexp ParticipantServer\\\|Launcher
echo please kill all these pids
