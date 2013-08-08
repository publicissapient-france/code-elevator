#!/bin/sh

JAVA_HOME=`/usr/libexec/java_home --version 1.7`
JETTY_VERSION=9.0.4.v20130625

echo install code-elevator
mvn clean install -DskipTests 1> /dev/null

mvn jetty:run --file elevator-server/pom.xml &

# launch participants
CLASSPATH=elevator-client/target/classes
CLASSPATH=${CLASSPATH}:elevator-core/target/classes
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-server/${JETTY_VERSION}/jetty-server-${JETTY_VERSION}.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/orbit/javax.servlet/3.0.0.v201112011016/javax.servlet-3.0.0.v201112011016.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-http/${JETTY_VERSION}/jetty-http-${JETTY_VERSION}.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-util/${JETTY_VERSION}/jetty-util-${JETTY_VERSION}.jar
CLASSPATH=${CLASSPATH}:${HOME}/.m2/repository/org/eclipse/jetty/jetty-io/${JETTY_VERSION}/jetty-io-${JETTY_VERSION}.jar
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1981 elevator.engine.scan.ScanElevator &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1982 elevator.engine.naive.NaiveElevator &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1983 elevator.engine.queue.QueueElevator &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1984 elevator.engine.crazy.CrazyElevator &
