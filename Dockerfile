FROM maven:3.3.9-jdk-8-onbuild
VOLUME /var/lib/jetty/webapps
CMD cp /usr/src/app/elevator-server/target/*.war /var/lib/jetty/webapps/root.war
