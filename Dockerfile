FROM maven:3.2.5-jdk-8-onbuild
VOLUME /var/lib/jetty/webapps
CMD cp /usr/src/app/elevator-server/target/*.war /var/lib/jetty/webapps/root.war
