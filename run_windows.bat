@ECHO OFF

echo Run server
%M2%\mvn --file elevator-server\pom.xml jetty:run