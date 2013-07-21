#!/bin/sh

JAVA_HOME=`/usr/libexec/java_home --version 1.7`

mvn install

# launch server
cd elevator-server
mvn jetty:run &

# launch participants
cd ../elevator-client
CLASSPATH=/Users/seb/src/code-elevator/elevator-client/target/classes
CLASSPATH=${CLASSPATH}:/Users/seb/src/code-elevator/elevator-core/target/classes
CLASSPATH=${CLASSPATH}:/Users/seb/.m2/repository/org/eclipse/jetty/jetty-server/9.0.3.v20130506/jetty-server-9.0.3.v20130506.jar
CLASSPATH=${CLASSPATH}:/Users/seb/.m2/repository/org/eclipse/jetty/orbit/javax.servlet/3.0.0.v201112011016/javax.servlet-3.0.0.v201112011016.jar
CLASSPATH=${CLASSPATH}:/Users/seb/.m2/repository/org/eclipse/jetty/jetty-http/9.0.3.v20130506/jetty-http-9.0.3.v20130506.jar
CLASSPATH=${CLASSPATH}:/Users/seb/.m2/repository/org/eclipse/jetty/jetty-util/9.0.3.v20130506/jetty-util-9.0.3.v20130506.jar
CLASSPATH=${CLASSPATH}:/Users/seb/.m2/repository/org/eclipse/jetty/jetty-io/9.0.3.v20130506/jetty-io-9.0.3.v20130506.jar
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1981 elevator.engine.scan.ScanElevator &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1982 elevator.engine.naive.NaiveElevator &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1983 elevator.engine.queue.QueueElevator &
java -classpath ${CLASSPATH} elevator.participant.ParticipantServer 1984 elevator.engine.crazy.CrazyElevator &


