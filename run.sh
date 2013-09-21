#!/bin/sh
if [ ! -f elevator-server/target/elevator-server-1.0-SNAPSHOT.war ];
then
	echo "\nNothin' ready, building binaries\n\n"
	mvn clean install
	if [ $? -ne 0 ]; then
		echo "\nc'mon, can't build, can't run the server...\n\n"
		exit $?
	fi
fi
echo "\nRunnin' server\n\n"
mvn --file elevator-server/pom.xml jetty:run
