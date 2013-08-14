#!/bin/sh

JAVA_HOME=`/usr/libexec/java_home --version 1.7`
JETTY_VERSION=9.0.4.v20130625

echo Install code-elevator
mvn clean install -DskipTests 1> /dev/null

echo Start server
mvn jetty:run --file elevator-server 2> /dev/null | grep RandomPassword\\\|Started\\\|exiting\\\|Elevator &

echo Launch participants
CLASSPATH=elevator-client/target/classes
CLASSPATH=${CLASSPATH}:elevator-core/target/classes
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-server/${JETTY_VERSION}/jetty-server-${JETTY_VERSION}.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/orbit/javax.servlet/3.0.0.v201112011016/javax.servlet-3.0.0.v201112011016.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-http/${JETTY_VERSION}/jetty-http-${JETTY_VERSION}.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-util/${JETTY_VERSION}/jetty-util-${JETTY_VERSION}.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-io/${JETTY_VERSION}/jetty-io-${JETTY_VERSION}.jar
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1981 elevator.engine.scan.ScanElevator 2> /dev/null &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1982 elevator.engine.naive.NaiveElevator 2> /dev/null &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1983 elevator.engine.queue.QueueElevator 2> /dev/null &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1984 elevator.engine.crazy.CrazyElevator 2> /dev/null &

read -p "Hit Enter when server is started"

echo Please log in to http://localhost:8080/#/administration.html and increase max number of user

echo Subscribe participants to server
curl --request POST http://localhost:8080/resources/player/register?email=sebastian.lemerdy@gmail.com\&pseudo=scan\&serverURL=http://localhost:1981
curl --request POST http://localhost:8080/resources/player/register?email=slemerdy@xebia.fr\&pseudo=naive\&serverURL=http://localhost:1982
curl --request POST http://localhost:8080/resources/player/register?email=sebastian.lemerdy@vidal.fr\&pseudo=queue\&serverURL=http://localhost:1983
curl --request POST http://localhost:8080/resources/player/register?email=eric.lemerdy@gmail.com\&pseudo=crazy\&serverURL=http://localhost:1984
